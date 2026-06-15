package net.daveyx0.primitivemobs.entity.passive;

import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMobPassive;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.daveyx0.primitivemobs.core.PrimitiveMobsVillagerProfessions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class EntityLostMiner extends Villager implements IMultiMobPassive {
   private static final EntityDataAccessor<Boolean> IS_SAVED = SynchedEntityData.defineId(EntityLostMiner.class, EntityDataSerializers.BOOLEAN);
   Player currentPlayer = null;

   public EntityLostMiner(EntityType<? extends EntityLostMiner> type, Level worldIn) {
      super(type, worldIn);
   }

   @Override
   public boolean canPickUpLoot() {
      return false;
   }

   @Override
   public boolean wantsToPickUp(ItemStack stack) {
      return false;
   }

   private void ensurePickaxeEquipped() {
      if (!this.level().isClientSide && this.getMainHandItem().isEmpty()) {
         this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_PICKAXE));
      }
   }

   @Override
   public void tick() {
      super.tick();
      this.ensurePickaxeEquipped();
      if (!this.isSaved() && this.tickCount % 5 == 0) {
         Player entityplayer = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), (double)8.0F, false);
         Player entityplayer1 = this.level().getNearestPlayer(this.getX(), this.getY(), this.getZ(), (double)2.0F, false);
         if (entityplayer != null && entityplayer1 == null) {
            this.currentPlayer = entityplayer;
         } else {
            this.currentPlayer = null;
         }

         var particle = ParticleTypes.SPLASH;
         if (this.isNotScared()) {
            particle = ParticleTypes.HAPPY_VILLAGER;
         }

         float f = 0.01745278F;
         double d = this.getX() - Math.sin((double)(this.getYRot() * f)) / (double)3.0F;
         double d1 = this.getY() + this.random.nextDouble() / (double)3.0F;
         double d2 = this.getZ() + Math.cos((double)(this.getYRot() * f)) / (double)3.0F;
         this.level().addParticle(particle, d, d1 + 1.8, d2, (double)0.0F, (double)0.0F, (double)0.0F);
      }

      if (!this.isSaved() && this.currentPlayer != null) {
         this.getNavigation().moveTo(this.currentPlayer, (double)0.65F);
      }

   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
      SpawnGroupData data = super.finalizeSpawn(levelAccessor, difficulty, spawnType, livingdata, tag);
      this.setVillagerData(this.getVillagerData().setProfession(PrimitiveMobsVillagerProfessions.MINER_PROFESSION.get()));
      this.setVillagerXp(1);
      this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_PICKAXE));
      this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
      return data;
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      if (!this.isSaved() && hand == InteractionHand.MAIN_HAND && player.getMainHandItem().isEmpty()) {
         new BlockPos(this.blockPosition());
         boolean flag = this.isNotScared();
         if (flag) {
            this.setSaved(true);

            for(int i = 0; i < 8; ++i) {
               this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() + (double)this.random.nextFloat() + (double)1.0F, this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), (double)0.0F, (double)0.0F, (double)0.0F);
            }

            if (!this.level().isClientSide) {
               for(int j = 0; j < this.getEmeralds()[0] + this.random.nextInt(this.getEmeralds()[1]); ++j) {
                  this.spawnAtLocation(Items.EMERALD, 1);
               }
            }
         }

         InteractionResult result = flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
         this.ensurePickaxeEquipped();
         return result;
      } else {
         InteractionResult result = super.mobInteract(player, hand);
         this.ensurePickaxeEquipped();
         return result;
      }
   }

   public boolean isNotScared() {
      return this.level().canSeeSky(new BlockPos((int)this.getX(), (int)(this.getY() + (double)this.getEyeHeight()), (int)this.getZ())) && this.getY() > (double)50.0F;
   }

   public int[] getEmeralds() {
      return PrimitiveMobsConfigSpecial.getLostMinerLootRange();
   }

   @Override
   protected SoundEvent getAmbientSound() {
      if (PrimitiveMobsConfigSpecial.getLostMinerSounds()) {
         return this.isTrading() ? SoundEvents.VILLAGER_TRADE : SoundEvents.VILLAGER_AMBIENT;
      } else {
         return null;
      }
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return PrimitiveMobsConfigSpecial.getLostMinerSounds() ? SoundEvents.VILLAGER_HURT : SoundEvents.NOTE_BLOCK_BASS.get();
   }

   @Override
   protected SoundEvent getDeathSound() {
      return PrimitiveMobsConfigSpecial.getLostMinerSounds() ? SoundEvents.VILLAGER_DEATH : SoundEvents.NOTE_BLOCK_BASS.get();
   }

   @Override
   public VillagerData getVillagerData() {
      if (this.isDeadOrDying()) {
         return PrimitiveMobsVillagerProfessions.stripForZombification(super.getVillagerData());
      }
      return super.getVillagerData();
   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return !this.isSaved();
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(IS_SAVED, false);
   }

   public void setSaved(boolean b) {
      this.entityData.set(IS_SAVED, b);
   }

   public boolean isSaved() {
      return (Boolean)this.entityData.get(IS_SAVED);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("Saved", this.isSaved());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setSaved(compound.getBoolean("Saved"));
   }

   @Override
   public boolean checkSpawnRules(net.minecraft.world.level.LevelAccessor levelAccessor, MobSpawnType reason) {
      return super.checkSpawnRules(levelAccessor, reason) && this.getY() < (double)50.0F && this.getY() > (double)20.0F;
   }
}

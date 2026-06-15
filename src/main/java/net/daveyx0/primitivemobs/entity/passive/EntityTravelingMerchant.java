package net.daveyx0.primitivemobs.entity.passive;

import com.google.common.collect.Sets;
import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMobPassive;
import net.daveyx0.multimob.entity.ai.EntityAITemptItemStack;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.daveyx0.primitivemobs.core.PrimitiveMobsVillagerProfessions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;

public class EntityTravelingMerchant extends Villager implements IMultiMobPassive {
   private static final EntityDataAccessor<Boolean> CAN_DESPAWN = SynchedEntityData.defineId(EntityTravelingMerchant.class, EntityDataSerializers.BOOLEAN);

   public EntityTravelingMerchant(EntityType<? extends EntityTravelingMerchant> type, Level worldIn) {
      super(type, worldIn);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(8, new EntityAITemptItemStack(this, 1.1, false, Sets.newHashSet(new ItemStack[]{new ItemStack(Items.EMERALD)})));
      if (!PrimitiveMobsConfigSpecial.getTravelerVisit()) {
         this.goalSelector.getAvailableGoals().removeIf(wrappedGoal -> {
            Goal goal = wrappedGoal.getGoal();
            String className = goal.getClass().getSimpleName();
            return className.contains("MoveIndoors") || className.contains("RestrictOpenDoor") || className.contains("OpenDoor")
               || className.contains("MoveThroughVillage") || className.contains("MoveTowardsRestriction");
         });
      }

   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(CAN_DESPAWN, true);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
      SpawnGroupData data = super.finalizeSpawn(levelAccessor, difficulty, spawnType, livingdata, tag);
      this.setVillagerData(this.getVillagerData().setProfession(PrimitiveMobsVillagerProfessions.MERCHANT_PROFESSION.get()));
      this.setVillagerXp(1);
      return data;
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      boolean flag = itemstack.getItem() == Items.EMERALD_BLOCK;
      if (flag && (Boolean)this.entityData.get(CAN_DESPAWN)) {
         this.consumeItemFromStack(player, itemstack);
         this.setCanDespawn(false);

         for(int i = 0; i < 8; ++i) {
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() + (double)this.random.nextFloat() + (double)1.0F, this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), (double)0.0F, (double)0.0F, (double)0.0F);
         }

         return InteractionResult.SUCCESS;
      } else {
         return super.mobInteract(player, hand);
      }
   }

   public void setCanDespawn(boolean b) {
      this.entityData.set(CAN_DESPAWN, b);
   }

   protected void consumeItemFromStack(Player player, ItemStack stack) {
      if (!player.getAbilities().instabuild) {
         stack.shrink(1);
      }

   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return (Boolean)this.entityData.get(CAN_DESPAWN);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("canDespawn", (Boolean)this.entityData.get(CAN_DESPAWN));
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setCanDespawn(compound.getBoolean("canDespawn"));
   }

   @Override
   public VillagerData getVillagerData() {
      if (this.isDeadOrDying()) {
         return PrimitiveMobsVillagerProfessions.stripForZombification(super.getVillagerData());
      }
      return super.getVillagerData();
   }

   @Override
   public boolean checkSpawnRules(net.minecraft.world.level.LevelAccessor levelAccessor, MobSpawnType reason) {
      int i = Mth.floor(this.getX());
      int j = Mth.floor(this.getBoundingBox().minY);
      int k = Mth.floor(this.getZ());
      BlockPos blockpos = new BlockPos(i, j, k);
      return (this.level().getBlockState(blockpos.below()).getBlock() == Blocks.GRASS_BLOCK || this.level().getBlockState(blockpos.below()).getBlock() == Blocks.DIRT || this.level().getBlockState(blockpos.below()).getBlock() == Blocks.STONE || this.level().getBlockState(blockpos.below()).getBlock() == Blocks.SAND || this.level().getBlockState(blockpos.below()).getBlock() == Blocks.GRAVEL) && j > 20 && this.level().getRawBrightness(blockpos, 0) > 8 && super.checkSpawnRules(levelAccessor, reason);
   }
}

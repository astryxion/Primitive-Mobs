package net.daveyx0.primitivemobs.entity.monster;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Optional;
import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.multimob.message.MMMessageRegistry;
import net.daveyx0.multimob.message.MessageMMParticle;
import net.neoforged.neoforge.network.PacketDistributor;
import net.daveyx0.multimob.util.NBTUtil;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EntityMimic extends Monster implements IMultiMob {
   private static final EntityDataAccessor<Optional<BlockState>> CHEST = SynchedEntityData.defineId(EntityMimic.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);
   public float nommingb;
   public float nommingc;
   public float nommingd;
   public float nomminge;
   public float nommingh;
   public float rotation;
   private boolean explode;
   int explosionTimer = 0;

   public EntityMimic(EntityType<? extends EntityMimic> type, Level worldIn) {
      super(type, worldIn);
      this.setDimensions(0.9F, 0.9F);
      this.explode = false;
   }

   private void setDimensions(float width, float height) {
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 40.0D)
         .add(Attributes.FOLLOW_RANGE, 40.0D)
         .add(Attributes.MOVEMENT_SPEED, 0.4000000417232513D)
         .add(Attributes.ATTACK_DAMAGE, 7.0D)
         .add(Attributes.ARMOR, 10.0D)
         .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
   }

   @Override
   protected void registerGoals() {
      int prio = 0;
      ++prio;
      this.goalSelector.addGoal(prio, new FloatGoal(this));
      ++prio;
      this.goalSelector.addGoal(prio, new MeleeAttackGoal(this, 1.15, false));
      ++prio;
      this.goalSelector.addGoal(prio, new MoveTowardsRestrictionGoal(this, (double)1.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new LookAtPlayerGoal(this, Player.class, 8.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new RandomLookAroundGoal(this));
      int attackPrio = 1;
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new HurtByTargetGoal(this));
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new NearestAttackableTargetGoal<>(this, Player.class, true));
   }

   public void setToExplode() {
      this.explode = true;
   }

   @Override
   public void aiStep() {
      if (this.onGround() && (this.getDeltaMovement().x > 0.05 || this.getDeltaMovement().z > 0.05 || this.getDeltaMovement().x < -0.05 || this.getDeltaMovement().z < -0.05)) {
         this.setDeltaMovement(this.getDeltaMovement().x, 0.4, this.getDeltaMovement().z);
      }

      if (this.horizontalCollision) {
         this.setDeltaMovement(this.getDeltaMovement().x, (double)0.5F, this.getDeltaMovement().z);
      }

      this.nomminge = this.nommingb;
      this.nommingd = this.nommingc;
      this.nommingc = (float)((double)this.nommingc + 3.2);
      if (this.nommingc < 0.0F) {
         this.nommingc = 0.0F;
      }

      if (this.nommingc > 0.2F) {
         this.nommingc = 0.2F;
      }

      if (this.nommingh < 0.2F) {
         this.nommingh = 0.2F;
      }

      this.nommingh = (float)((double)this.nommingh * 0.9);
      this.nommingb += this.nommingh * 2.0F;
      this.rotation = (-(float)Math.PI / 4F);
      if (this.random.nextInt(80) == 0) {
         float f = 0.01745278F;
         double d = this.getX() - Math.sin((double)(this.getYRot() * f)) / (double)3.0F;
         double d1 = this.getY() + this.random.nextDouble() / (double)3.0F;
         double d2 = this.getZ() + Math.cos((double)(this.getYRot() * f)) / (double)3.0F;

         for(int j = 0; j < 12; ++j) {
            this.level().addParticle(ParticleTypes.SPLASH, d, d1 + 0.3, d2, (double)0.0F, (double)0.0F, (double)0.0F);
         }
      }

      if (this.explode) {
         this.setSpeed(0.0F);
         MMMessageRegistry.getNetwork().sendToAll(new MessageMMParticle(10, 10, (float)this.getX() + (this.random.nextFloat() - this.random.nextFloat()), (float)this.getY() + 1.0F + (this.random.nextFloat() - this.random.nextFloat()), (float)this.getZ() + (this.random.nextFloat() - this.random.nextFloat()), (double)0.0F, (double)0.0F, (double)0.0F, 0));
         if (this.explosionTimer == 0) {
            this.playSound(SoundEvents.TNT_PRIMED, 1.0F, 1.0F);
         } else if (this.explosionTimer >= 40 && !this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.MOB);
            this.discard();
         }

         ++this.explosionTimer;
      }

      super.aiStep();
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return SoundEvents.CHEST_LOCKED;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.CHEST_CLOSE;
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      Optional<BlockState> chest = Optional.of(Blocks.CHEST.defaultBlockState());
      builder.define(CHEST, chest);
      super.defineSynchedData(builder);
   }

   public void setChest(BlockState chest) {
      Optional<BlockState> newChest = Optional.of(chest);
      this.entityData.set(CHEST, newChest);
   }

   public BlockState getChest() {
      Optional<BlockState> state = (Optional)this.entityData.get(CHEST);
      return state != null ? (BlockState)state.orElse(Blocks.CHEST.defaultBlockState()) : Blocks.CHEST.defaultBlockState();
   }

   @Nullable
   @Override
   protected ResourceKey<LootTable> getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_MIMIC;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      NBTUtil.setBlockStateToNBT(this.getChest(), "chestState", compound);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setChest(NBTUtil.getBlockStateFromNBT("chestState", compound));
   }
}

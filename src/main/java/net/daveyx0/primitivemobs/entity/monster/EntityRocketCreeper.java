package net.daveyx0.primitivemobs.entity.monster;

import java.util.Collection;
import javax.annotation.Nullable;
import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity;
import net.daveyx0.multimob.common.capabilities.ITameableEntity;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityRocketCreeper extends EntityPrimitiveCreeper implements IMultiMob {
   private float explosionRadius = 3.0F;
   int timeBeforeJumping;
   private static final EntityDataAccessor<Boolean> IS_ROCKET = SynchedEntityData.defineId(EntityRocketCreeper.class, EntityDataSerializers.BOOLEAN);

   public EntityRocketCreeper(EntityType<? extends EntityRocketCreeper> type, Level worldIn) {
      super(type, worldIn);
      this.setRocket(false);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(2, new SwellGoal(this));
      this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, (double)1.0F, 1.2));
      this.goalSelector.addGoal(4, new MeleeAttackGoal(this, (double)1.0F, false));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
      this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
   }

   @Override
   protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
      if (!this.isInWater()) {
         this.checkInsideBlocks();
      }

      if (onGroundIn) {
         if (this.fallDistance > 0.0F) {
            state.getBlock().fallOn(this.level(), state, pos, this, this.fallDistance);
         }

         this.fallDistance = 0.0F;
      } else if (y < (double)0.0F) {
         this.fallDistance = (float)((double)this.fallDistance - y);
      }

   }

   @Override
   protected void playStepSound(BlockPos pos, BlockState blockIn) {
      if (!this.isRocket()) {
         super.playStepSound(pos, blockIn);
      }
   }

   public static AttributeSupplier.Builder createAttributes() {
      return EntityPrimitiveCreeper.createMobAttributes()
         .add(Attributes.MOVEMENT_SPEED, 0.35);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(IS_ROCKET, false);
   }

   @Override
   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      if (this.isRocket()) {
         this.explode();
      }
      return false;
   }

   private void explode() {
      if (!this.level().isClientSide) {
         boolean flag = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
         float f = this.isPowered() ? 2.0F : 1.0F;
         ITameableEntity tameable = (ITameableEntity)EntityUtil.getCapability(this, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, (Direction)null);
         if (tameable != null && tameable.isTamed()) {
            this.hurt(this.damageSources().explosion(this, this), 1.0F);
            this.setRocket(false);
         } else {
            this.dead = true;
            this.discard();
         }

         this.level().explode(this, this.getX(), this.getY(), this.getZ(), this.explosionRadius * f, flag ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
         this.spawnLingeringCloud();
      }

   }

   public boolean hasEnoughSpaceToJump(Entity entityIn) {
      boolean flag = true;
      if (!PrimitiveMobsConfigSpecial.getRocketCreeperAlwaysJump()) {
         for(int i = 0; i < 5; ++i) {
            flag = this.level().clip(new net.minecraft.world.level.ClipContext(
               new Vec3(this.getX(), this.getY() + (double)this.getEyeHeight() + (double)i, this.getZ()),
               new Vec3(entityIn.getX(), entityIn.getY() + (double)entityIn.getEyeHeight(), entityIn.getZ()),
               net.minecraft.world.level.ClipContext.Block.COLLIDER,
               net.minecraft.world.level.ClipContext.Fluid.NONE,
               this)).getType() == net.minecraft.world.phys.HitResult.Type.MISS;
         }
      }

      return flag;
   }

   @Override
   public void tick() {
      if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) > (double)25.0F) {
         this.setIgnitedTime(0);
         this.setSwellDir(-1);
      }

      if (this.getSwellDir() > 0) {
         ++this.timeBeforeJumping;
      } else {
         this.timeBeforeJumping = 0;
      }

      if (this.timeBeforeJumping > 15 && this.isAlive() && this.getTarget() != null && this.hasEnoughSpaceToJump(this.getTarget())) {
         this.setIgnitedTime(0);
         int swellDir = this.getSwellDir();
         if (swellDir > 0 && this.onGround()) {
            if (this.level().isClientSide) {
               this.level().addParticle(ParticleTypes.SMOKE, this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() - (double)(this.random.nextFloat() - this.random.nextFloat()) - (double)1.0F, this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), (double)0.0F, (double)0.0F, (double)0.0F);
            }

            this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 0.5F);
            Vec3 motion = this.getDeltaMovement();
            double newMotionX = (this.getTarget().getX() - this.getX()) / (double)6.0F;
            double newMotionZ = (this.getTarget().getZ() - this.getZ()) / (double)6.0F;
            this.setDeltaMovement(newMotionX, (double)1.2F, newMotionZ);
            this.setRocket(true);
         }
      }

      super.tick();
   }

   @Nullable
   @Override
   protected ResourceLocation getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_ROCKETCREEPER;
   }

   public void setRocket(boolean rocket) {
      this.entityData.set(IS_ROCKET, rocket);
   }

   public boolean isRocket() {
      return (Boolean)this.entityData.get(IS_ROCKET);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("Rocket", this.isRocket());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setRocket(compound.getBoolean("Rocket"));
   }

   private void spawnLingeringCloud() {
      Collection<MobEffectInstance> collection = this.getActiveEffects();
      if (!collection.isEmpty()) {
         AreaEffectCloud entityareaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
         entityareaeffectcloud.setRadius(2.5F);
         entityareaeffectcloud.setRadiusOnUse(-0.5F);
         entityareaeffectcloud.setWaitTime(10);
         entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
         entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float)entityareaeffectcloud.getDuration());

         for(MobEffectInstance potioneffect : collection) {
            entityareaeffectcloud.addEffect(new MobEffectInstance(potioneffect));
         }

         this.level().addFreshEntity(entityareaeffectcloud);
      }

   }

   @Override
   public MobCategory getClassification(boolean forSpawnCount) {
      return MobCategory.CREATURE;
   }
}

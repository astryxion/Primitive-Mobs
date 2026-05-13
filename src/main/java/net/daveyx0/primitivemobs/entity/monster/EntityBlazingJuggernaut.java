package net.daveyx0.primitivemobs.entity.monster;

import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

public class EntityBlazingJuggernaut extends Monster implements IMultiMob {
   private float heightOffset = 0.5F;
   private int heightOffsetUpdateTime;

   public EntityBlazingJuggernaut(EntityType<? extends EntityBlazingJuggernaut> type, Level worldIn) {
      super(type, worldIn);
      this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
      this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
      this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
      this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
      this.blocksBuilding = true;
      this.xpReward = 10;
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.ATTACK_DAMAGE, (double)5.0F)
         .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
         .add(Attributes.FOLLOW_RANGE, (double)48.0F);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(4, new AIChargeAttack(this));
      this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, (double)1.0F));
      this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
      this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
   }

   @Override
   protected boolean shouldDespawnInPeaceful() {
      return false;
   }

   @Override
   public float getVoicePitch() {
      return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.BLAZE_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource damageSource) {
      return SoundEvents.BLAZE_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.BLAZE_DEATH;
   }

   @Override
   public float getLightLevelDependentMagicValue() {
      return 15.0F;
   }

   public float getBrightness() {
      return 1.0F;
   }

   @Override
   public void aiStep() {
      if (!this.onGround() && this.getDeltaMovement().y < (double)0.0F) {
         this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
      }

      if (this.level().isClientSide) {
         if (this.random.nextInt(24) == 0 && !this.isSilent()) {
            this.level().playLocalSound(this.getX() + (double)0.5F, this.getY() + (double)0.5F, this.getZ() + (double)0.5F, SoundEvents.BLAZE_BURN, this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
         }

         for(int i = 0; i < 2; ++i) {
            this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + (this.random.nextDouble() - (double)0.5F) * (double)this.getBbWidth(), this.getY() + this.random.nextDouble() * (double)this.getBbHeight(), this.getZ() + (this.random.nextDouble() - (double)0.5F) * (double)this.getBbWidth(), (double)0.0F, (double)0.0F, (double)0.0F);
         }
      }

      super.aiStep();
   }

   @Override
   protected void customServerAiStep() {
      if (this.isInWaterOrBubble()) {
         this.hurt(this.damageSources().drown(), 1.0F);
      }

      --this.heightOffsetUpdateTime;
      if (this.heightOffsetUpdateTime <= 0) {
         this.heightOffsetUpdateTime = 100;
         this.heightOffset = 0.5F + (float)this.random.nextGaussian() * 3.0F;
      }

      LivingEntity entitylivingbase = this.getTarget();
      if (entitylivingbase != null && entitylivingbase.getY() + (double)entitylivingbase.getEyeHeight() > this.getY() + (double)this.getEyeHeight() + (double)this.heightOffset) {
         Vec3 motion = this.getDeltaMovement();
         this.setDeltaMovement(motion.x, motion.y + ((double)0.3F - motion.y) * (double)0.3F, motion.z);
         this.hasImpulse = true;
      }

      super.customServerAiStep();
   }

   @Nullable
   @Override
   protected ResourceLocation getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_BLAZINGJUGGERNAUT;
   }

   @Override
   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      return false;
   }

   @Override
   public boolean displayFireAnimation() {
      return false;
   }

   @Override
   protected boolean isAlwaysExperienceDropper() {
      return true;
   }

   @Override
   public MobCategory getClassification(boolean forSpawnCount) {
      return MobCategory.CREATURE;
   }

   static class AIChargeAttack extends Goal {
      private final EntityBlazingJuggernaut blaze;
      private int attackCooldown;

      public AIChargeAttack(EntityBlazingJuggernaut blazeIn) {
         this.blaze = blazeIn;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE));
      }

      @Override
      public boolean canUse() {
         LivingEntity entitylivingbase = this.blaze.getTarget();
         return entitylivingbase != null && entitylivingbase.isAlive();
      }

      @Override
      public void start() {
         this.attackCooldown = 0;
      }

      @Override
      public void stop() {
      }

      @Override
      public void tick() {
         LivingEntity entitylivingbase = this.blaze.getTarget();
         double d0 = this.blaze.distanceToSqr(entitylivingbase);
         ++this.attackCooldown;
         if (d0 < (double)5.0F) {
            if (this.attackCooldown > 10) {
               this.attackCooldown = 0;
               this.blaze.doHurtTarget(entitylivingbase);
            }

            this.blaze.getMoveControl().setWantedPosition(entitylivingbase.getX(), entitylivingbase.getY(), entitylivingbase.getZ(), (double)1.0F);
            if (entitylivingbase.getY() < this.blaze.getY()) {
               Vec3 motion = this.blaze.getDeltaMovement();
               this.blaze.setDeltaMovement(motion.x, motion.y - 0.1, motion.z);
            }
         } else if (d0 < (double)30.0F) {
            double d1 = entitylivingbase.getX() - this.blaze.getX();
            double d2 = entitylivingbase.getBoundingBox().minY + (double)(entitylivingbase.getBbHeight() / 2.0F) - (this.blaze.getY() + (double)(this.blaze.getBbHeight() / 2.0F));
            double d3 = entitylivingbase.getZ() - this.blaze.getZ();
            if (this.attackCooldown > 5) {
               Vec3 motion = this.blaze.getDeltaMovement();
               double newMotionX = motion.x + (Math.signum(d1) * (double)0.5F - motion.x) * 0.8;
               double newMotionY = motion.y + (Math.signum(d2) * (double)0.7F - motion.y) * 0.8;
               double newMotionZ = motion.z + (Math.signum(d3) * (double)0.5F - motion.z) * 0.8;
               this.blaze.setDeltaMovement(newMotionX, newMotionY, newMotionZ);
               float f = (float)(Math.atan2(newMotionZ, newMotionX) * (double)180.0F / Math.PI) - 90.0F;
               this.blaze.zza = 1.5F;
               this.attackCooldown = 0;
            }

            this.blaze.getLookControl().setLookAt(entitylivingbase, 10.0F, 10.0F);
         } else {
            this.blaze.getNavigation().stop();
            this.blaze.getMoveControl().setWantedPosition(entitylivingbase.getX(), entitylivingbase.getY(), entitylivingbase.getZ(), (double)1.0F);
         }

         super.tick();
      }
   }
}

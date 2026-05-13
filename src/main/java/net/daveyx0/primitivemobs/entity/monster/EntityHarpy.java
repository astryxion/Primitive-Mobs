package net.daveyx0.primitivemobs.entity.monster;

import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.EntityMMFlyingMob;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.daveyx0.primitivemobs.core.PrimitiveMobsSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityHarpy extends EntityMMFlyingMob implements IMultiMob {
   public EntityHarpy(EntityType<? extends EntityMMFlyingMob> type, Level worldIn) {
      super(type, worldIn);
      this.moveControl = new EntityHarpyFlyHelper(this);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new AIHarpyLift(this, (double)1.5F, false));
      int attackPrio = 1;
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new HurtByTargetGoal(this));
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new NearestAttackableTargetGoal<>(this, Player.class, true));
   }

   public static AttributeSupplier.Builder createAttributes() {
      return EntityMMFlyingMob.createAttributes()
         .add(Attributes.MAX_HEALTH, (double)20.0F)
         .add(Attributes.FLYING_SPEED, 0.5000000059604645)
         .add(Attributes.MOVEMENT_SPEED, (double)0.2F);
   }

   @Nullable
   @Override
   public SoundEvent getAmbientSound() {
      return PrimitiveMobsSoundEvents.ENTITY_HARPY_IDLE.get();
   }

   @Override
   public void tick() {
      if (this.isVehicle()) {
         this.setDeltaMovement(this.getDeltaMovement().x, (double)0.25F, this.getDeltaMovement().z);
         if (!this.level().isClientSide && (this.getDistanceToGround(new BlockPos((int)this.getX(), (int)this.getY(), (int)this.getZ())) >= (double)20.0F || !this.level().isEmptyBlock(new BlockPos((int)this.getX(), (int)(this.getY() + (double)1.0F), (int)this.getZ())))) {
            this.ejectPassengers();
         }
      }

      super.tick();
   }

   public double getDistanceToGround(BlockPos pos) {
      for(int i = 0; i < 64; ++i) {
         BlockPos currentPos = pos.below(i);
         if (!this.level().isEmptyBlock(currentPos)) {
            return this.distanceToSqr((double)currentPos.getX(), (double)currentPos.getY(), (double)currentPos.getZ());
         }
      }

      return (double)20.0F;
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (!this.level().isClientSide && this.isVehicle()) {
         this.ejectPassengers();
      }

      return super.hurt(source, amount);
   }

   @Override
   public boolean canRiderInteract() {
      return true;
   }

   @Override
   protected void pushEntities() {
   }

   @Override
   public boolean shouldRiderSit() {
      return false;
   }

   @Override
   public double getPassengersRidingOffset() {
      return this.isVehicle() && !this.getPassengers().isEmpty() && this.getPassengers().get(0) != null ? (double)((Entity)this.getPassengers().get(0)).getBbHeight() : super.getPassengersRidingOffset();
   }

   @Override
   protected void positionRider(Entity entity, Entity.MoveFunction moveFunction) {
      super.positionRider(entity, moveFunction);
      if (entity instanceof LivingEntity) {
         moveFunction.accept(entity, this.getX(), this.getY() - this.getPassengersRidingOffset(), this.getZ());
         if (entity.isShiftKeyDown()) {
            entity.setShiftKeyDown(false);
         }
      }

   }

   @Nullable
   @Override
   protected ResourceLocation getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_HARPY;
   }

   @Override
   public float getVoicePitch() {
      return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.8F;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return PrimitiveMobsSoundEvents.ENTITY_HARPY_HURT.get();
   }

   @Override
   protected SoundEvent getDeathSound() {
      return PrimitiveMobsSoundEvents.ENTITY_HARPY_HURT.get();
   }

   public class AIHarpyLift extends MeleeAttackGoal {
      public AIHarpyLift(PathfinderMob creature, double speedIn, boolean useLongMemory) {
         super(creature, speedIn, useLongMemory);
      }

      @Override
      public boolean canUse() {
         return super.canUse() && !this.mob.isVehicle();
      }

      @Override
      protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
         double d0 = this.getAttackReachSqr(p_190102_1_);
         if (p_190102_2_ <= d0 && this.getTicksUntilNextAttack() <= 0) {
            if (!this.mob.getTarget().isVehicle() && this.mob.level().canSeeSky(new BlockPos((int)this.mob.getX(), (int)this.mob.getY(), (int)this.mob.getZ()))) {
               this.mob.getTarget().startRiding(this.mob);
            } else {
               this.mob.swing(InteractionHand.MAIN_HAND);
               this.mob.doHurtTarget(p_190102_1_);
            }

            this.resetAttackCooldown();
         }

      }
   }

   public class EntityHarpyFlyHelper extends MoveControl {
      public EntityHarpyFlyHelper(Mob p_i47418_1_) {
         super(p_i47418_1_);
      }

      @Override
      public void tick() {
         if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            this.mob.setNoGravity(true);
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedY - this.mob.getY();
            double d2 = this.wantedZ - this.mob.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 < (double)2.5000003E-7F) {
               this.mob.setYya(0.0F);
               this.mob.setZza(0.0F);
               return;
            }

            float f = (float)(Mth.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 10.0F));
            float f1;
            if (this.mob.onGround()) {
               f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
               this.mob.setSpeed(f1);
            } else {
               f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
               this.mob.setSpeed(f1 * 2.5F);
            }

            double d4 = (double)Mth.sqrt((float)(d0 * d0 + d2 * d2));
            float f2 = (float)(-(Mth.atan2(d1, d4) * (180D / Math.PI)));
            this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f2, 10.0F));
            this.mob.setYya(d1 > (double)0.0F ? f1 : -f1);
         } else {
            this.mob.setNoGravity(false);
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
         }

      }
   }
}

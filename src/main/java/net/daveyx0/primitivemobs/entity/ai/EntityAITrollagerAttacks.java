package net.daveyx0.primitivemobs.entity.ai;

import java.util.EnumSet;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.daveyx0.primitivemobs.entity.IAnimatedMob;
import net.daveyx0.primitivemobs.entity.monster.EntityTrollager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;

public class EntityAITrollagerAttacks<T extends Monster & IAnimatedMob> extends Goal {
   private final EntityTrollager entity;
   private final double moveSpeedAmp;
   private final float maxAttackDistance;
   private final float meleeAttackDistance;
   private int attackTime = -1;
   private int animTime = -1;
   private int attackDelay = -1;
   private int seeTime;
   private boolean isAttacking;

   public EntityAITrollagerAttacks(EntityTrollager entity, double moveSpeed, float meleeAttackDistance, float maxAttackDistance) {
      this.entity = entity;
      this.moveSpeedAmp = moveSpeed;
      this.maxAttackDistance = maxAttackDistance * maxAttackDistance;
      this.meleeAttackDistance = meleeAttackDistance * meleeAttackDistance;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      return this.entity.getTarget() != null && !this.entity.isStone();
   }

   @Override
   public boolean canContinueToUse() {
      return this.canUse();
   }

   @Override
   public void start() {
      super.start();
      this.seeTime = 0;
      this.attackTime = -1;
      this.animTime = -1;
      this.isAttacking = false;
      this.determineAttackAndPerform();
   }

   @Override
   public void stop() {
      super.stop();
      this.entity.setAnimationState(0);
      this.seeTime = 0;
      this.attackTime = -1;
      this.animTime = -1;
      this.isAttacking = false;
   }

   public boolean checkCanThrow() {
      LivingEntity entitylivingbase = this.entity.getTarget();
      EntityTrollager troll = this.entity;
      return entitylivingbase != null && troll != null ? troll.canBlockAreaSeeEntity(entitylivingbase) : false;
   }

   @Override
   public void tick() {
      this.determineAttackAndPerform();
   }

   public void determineAttackAndPerform() {
      LivingEntity entitylivingbase = this.entity.getTarget();
      if (entitylivingbase != null) {
         double distanceToEnemy = this.entity.distanceToSqr(entitylivingbase.getX(), entitylivingbase.getBoundingBox().minY, entitylivingbase.getZ());
         boolean canSeeEnemy = this.entity.getSensing().hasLineOfSight(entitylivingbase);
         this.entity.getNavigation().moveTo(entitylivingbase, this.moveSpeedAmp);
         boolean isWithinMeleeRange = distanceToEnemy <= (double)this.meleeAttackDistance * (double)this.meleeAttackDistance;
         boolean isWithinAttackRange = distanceToEnemy <= (double)this.maxAttackDistance * (double)this.maxAttackDistance;
         boolean canThrowBlock = this.checkCanThrow();
         boolean canPerformMeleeAttack = isWithinMeleeRange && canSeeEnemy;
         boolean canPerformSmashAttack = PrimitiveMobsConfigSpecial.getTrollDestruction() && (isWithinAttackRange && (!canSeeEnemy || !canThrowBlock) || this.entity.level().random.nextInt(4) == 0) && !this.entity.isPassenger();
         boolean canPerformThrowAttack = canSeeEnemy && canThrowBlock && !this.entity.isPassenger();
         if (--this.attackTime <= 0 && this.isAttacking) {
            if (this.entity.getAnimationState() == 3) {
               this.entity.performAction(entitylivingbase, 1);
               this.entity.setAnimationState(4);
               this.animTime = 20;
            } else if (this.entity.getAnimationState() == 0) {
               this.entity.performAction(entitylivingbase, 2);
               this.entity.setAnimationState(6);
               this.animTime = 7;
            } else {
               this.entity.performAction(entitylivingbase, 0);
               this.entity.setAnimationState(2);
               this.animTime = 20;
            }

            this.attackTime = Integer.MAX_VALUE;
         }

         if (this.animTime >= 0 && --this.animTime == 0) {
            if (this.entity.getAnimationState() == 4) {
               this.entity.setAnimationState(5);
               this.animTime = 10;
            } else if (this.entity.getAnimationState() == 6) {
               this.entity.setAnimationState(7);
               this.animTime = 7;
            } else {
               this.animTime = -1;
               this.isAttacking = false;
            }
         }

         if (!this.isAttacking) {
            if (canPerformMeleeAttack) {
               this.entity.setAnimationState(0);
               this.attackTime = 10;
               this.isAttacking = true;
            } else if (canPerformSmashAttack) {
               this.entity.setAnimationState(3);
               this.attackTime = 40;
               this.isAttacking = true;
            } else if (canPerformThrowAttack) {
               this.entity.setAnimationState(1);
               this.attackTime = 30;
               this.isAttacking = true;
            } else {
               this.entity.setAnimationState(0);
               this.attackTime = Integer.MAX_VALUE;
            }
         }

         this.entity.getNavigation().moveTo(entitylivingbase, this.moveSpeedAmp);
         this.entity.getLookControl().setLookAt(entitylivingbase, 30.0F, 30.0F);
      }

   }
}

package net.daveyx0.primitivemobs.entity.ai;

import net.daveyx0.primitivemobs.entity.monster.EntityMotherSpider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class EntityAIFollowerHurtByTarget extends TargetGoal {
   EntityMotherSpider user;
   LivingEntity[] followers;
   LivingEntity attacker;
   LivingEntity currentFollower;
   private int timestamp;

   private static final TargetingConditions TARGETING = TargetingConditions.forCombat().ignoreLineOfSight();

   public EntityAIFollowerHurtByTarget(EntityMotherSpider user) {
      super(user, false);
      this.user = user;
   }

   @Override
   public boolean canUse() {
      if (!this.user.hasFollowers()) {
         return false;
      } else {
         this.followers = this.user.getFollowers();

         for(int i = 0; i < this.followers.length; ++i) {
            LivingEntity entitylivingbase = this.followers[i];
            if (entitylivingbase != null) {
               this.currentFollower = entitylivingbase;
               if (this.attacker == null) {
                  this.attacker = entitylivingbase.getLastHurtByMob();
               }

               int j = entitylivingbase.getLastHurtByMobTimestamp();
               if (j != this.timestamp && this.canAttack(this.attacker, TARGETING) && this.user.shouldAttackEntity(this.attacker, entitylivingbase)) {
                  return true;
               }

               this.currentFollower = null;
            }
         }

         return false;
      }
   }

   @Override
   public void start() {
      this.mob.setTarget(this.attacker);
      if (this.currentFollower != null) {
         this.timestamp = this.currentFollower.getLastHurtByMobTimestamp();
      }

      super.start();
   }
}

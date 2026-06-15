package net.daveyx0.primitivemobs.entity.ai;

import java.util.EnumSet;
import net.daveyx0.primitivemobs.entity.monster.EntityPrimitiveTameableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class EntityAIPrimitiveOwnerHurtByTarget extends TargetGoal {
   private final EntityPrimitiveTameableMob tameable;
   private LivingEntity attacker;
   private int timestamp;

   private static final TargetingConditions TARGETING = TargetingConditions.forCombat().ignoreLineOfSight();

   public EntityAIPrimitiveOwnerHurtByTarget(EntityPrimitiveTameableMob tameableIn) {
      super(tameableIn, false);
      this.tameable = tameableIn;
      this.setFlags(EnumSet.of(Flag.TARGET));
   }

   @Override
   public boolean canUse() {
      if (!this.tameable.isTamed()) {
         return false;
      }

      LivingEntity owner = this.tameable.getOwner();
      if (owner == null) {
         return false;
      }

      this.attacker = owner.getLastHurtByMob();
      int i = owner.getLastHurtByMobTimestamp();
      return i != this.timestamp && this.attacker != null && this.canAttack(this.attacker, TARGETING) && this.tameable.shouldAttackEntity(this.attacker, owner);
   }

   @Override
   public void start() {
      this.mob.setTarget(this.attacker);
      LivingEntity owner = this.tameable.getOwner();
      if (owner != null) {
         this.timestamp = owner.getLastHurtByMobTimestamp();
      }

      super.start();
   }
}

package net.daveyx0.primitivemobs.entity.monster;

import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.multimob.entity.ai.EntityAIBackOffFromEntity;
import net.daveyx0.primitivemobs.core.PrimitiveMobsEntityRegistry;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.daveyx0.primitivemobs.entity.item.EntityPrimitiveTNTPrimed;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;

public class EntityFestiveCreeper extends EntityPrimitiveCreeper implements IMultiMob {
   public EntityFestiveCreeper(EntityType<? extends EntityFestiveCreeper> type, Level worldIn) {
      super(type, worldIn);
      this.blocksBuilding = true;
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(2, new EntityAIThrowTNT(this));
      this.goalSelector.addGoal(3, new EntityAIBackOffFromEntity(this, (double)7.5F, true));
      this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Cat.class, 6.0F, (double)1.0F, 1.2));
      this.goalSelector.addGoal(5, new MeleeAttackGoal(this, (double)1.0F, false));
      this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (target) -> !(target instanceof Player player) || this.canTargetPlayer(player)));
      this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
   }

   public static AttributeSupplier.Builder createAttributes() {
      return EntityPrimitiveCreeper.createMobAttributes()
         .add(Attributes.MOVEMENT_SPEED, 0.35);
   }

   @Nullable
   @Override
   protected ResourceLocation getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_FESTIVECREEPER;
   }

   public class EntityAIThrowTNT extends Goal {
      EntityFestiveCreeper creeper;
      LivingEntity target;
      float power;
      int attackCooldown;

      public EntityAIThrowTNT(EntityFestiveCreeper entityFestiveCreeper) {
         this.creeper = entityFestiveCreeper;
         this.power = 1.5F;
         this.attackCooldown = 0;
      }

      @Override
      public boolean canUse() {
         this.target = this.creeper.getTarget();
         if (this.target == null) {
            return false;
         } else if (this.target instanceof Player player && !this.creeper.canTargetPlayer(player)) {
            return false;
         } else if (!this.target.isAlive()) {
            return false;
         } else {
            return (double)this.creeper.distanceTo(this.target) > (double)2.0F && this.creeper.distanceToSqr(this.target) < (double)144.0F && this.creeper.hasLineOfSight(this.target);
         }
      }

      @Override
      public boolean canContinueToUse() {
         return this.canUse();
      }

      @Override
      public void stop() {
         this.target = null;
         this.attackCooldown = 0;
      }

      @Override
      public void tick() {
         if (this.creeper.isPowered()) {
            this.power = 3.0F;
         }

         if (this.target != null && --this.attackCooldown <= 0) {
            if (!EntityFestiveCreeper.this.level().isClientSide) {
               EntityPrimitiveTNTPrimed tnt = new EntityPrimitiveTNTPrimed(PrimitiveMobsEntityRegistry.PRIMITIVE_TNT_PRIMED.get(), this.creeper.level(), this.creeper.getX(), this.creeper.getY(), this.creeper.getZ(), this.creeper, this.power, 30);
               tnt.moveTo(this.creeper.getX(), this.creeper.getY(), this.creeper.getZ(), this.creeper.getYRot(), 0.0F);
               double motionX = (this.target.getX() - tnt.getX()) / (double)18.0F;
               double motionY = (this.target.getY() - tnt.getY()) / (double)18.0F + (double)0.5F;
               double motionZ = (this.target.getZ() - this.creeper.getZ()) / (double)18.0F;
               tnt.setDeltaMovement(motionX, motionY, motionZ);
               this.creeper.level().addFreshEntity(tnt);
            }

            this.creeper.playSound(SoundEvents.TNT_PRIMED, this.creeper.getSoundVolume(), this.creeper.getVoicePitch());
            this.attackCooldown = 60;
         }

      }
   }
}

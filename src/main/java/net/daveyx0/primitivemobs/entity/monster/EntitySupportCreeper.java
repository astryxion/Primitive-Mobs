package net.daveyx0.primitivemobs.entity.monster;

import java.util.List;
import javax.annotation.Nullable;
import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity;
import net.daveyx0.multimob.common.capabilities.ITameableEntity;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;

public class EntitySupportCreeper extends EntityPrimitiveCreeper {
   private boolean switchedToSwellGoals;

   public EntitySupportCreeper(EntityType<? extends EntitySupportCreeper> type, Level worldIn) {
      super(type, worldIn);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(2, new EntityAIBuffMob(this));
      this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 6.0F, (double)1.0F, 1.2));
      this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, (double)1.0F, 1.2));
      this.goalSelector.addGoal(4, new MeleeAttackGoal(this, (double)1.0F, false));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
   }

   public static AttributeSupplier.Builder createAttributes() {
      return EntityPrimitiveCreeper.createMobAttributes()
         .add(Attributes.MOVEMENT_SPEED, (double)0.25F);
   }

   @Override
   public void tick() {
      if (!this.switchedToSwellGoals && !this.isEggTamed() && this.getHealth() < this.getMaxHealth() / 2.0F) {
         this.switchedToSwellGoals = true;
         this.goalSelector.getAvailableGoals().removeIf((taskEntry) -> taskEntry.getGoal() instanceof AvoidEntityGoal);
         this.goalSelector.addGoal(2, new SwellGoal(this));
         this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (target) -> !(target instanceof Player player) || this.canTargetPlayer(player)));
      }

      super.tick();
   }

   @Nullable
   @Override
   protected ResourceLocation getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_SUPPORTCREEPER;
   }

   public class EntityAIBuffMob extends Goal {
      EntitySupportCreeper creeper;
      LivingEntity mobIdol;
      int strength;
      private int repathCooldown;
      private int searchCooldown;
      private boolean chargedAlly;

      public EntityAIBuffMob(EntitySupportCreeper entitySupportCreeper) {
         this.creeper = entitySupportCreeper;
         this.mobIdol = null;
         this.strength = 1;
      }

      @Override
      public boolean canUse() {
         ITameableEntity tameable = (ITameableEntity)EntityUtil.getCapability(this.creeper, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, (Direction)null);
         return tameable == null || !tameable.isTamed() || tameable.getFollowState() != 0;
      }

      @Override
      public boolean canContinueToUse() {
         ITameableEntity tameable = (ITameableEntity)EntityUtil.getCapability(this.creeper, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, (Direction)null);
         if (tameable != null && tameable.isTamed() && tameable.getFollowState() == 0) {
            return false;
         }
         return this.mobIdol != null && this.mobIdol.isAlive() && this.creeper.distanceToSqr(this.mobIdol) <= 625.0D;
      }

      @Override
      public void start() {
         this.searchCooldown = 0;
         this.repathCooldown = 0;
         this.chargedAlly = false;
         this.mobIdol = this.findMobToSupport();
      }

      @Override
      public void stop() {
         this.mobIdol = null;
         this.chargedAlly = false;
         this.creeper.getNavigation().stop();
      }

      public LivingEntity findMobToSupport() {
         ITameableEntity tameable = (ITameableEntity)EntityUtil.getCapability(this.creeper, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, (Direction)null);
         if (tameable != null && tameable.isTamed() && tameable.getFollowState() != 0) {
            LivingEntity owner = tameable.getOwner(this.creeper);
            if (owner != null && owner.distanceToSqr(this.creeper) < 30.0D) {
               return owner;
            }
            return null;
         }

         if (tameable != null && tameable.isTamed()) {
            return null;
         }

         List<Entity> list = this.creeper.level().getEntities(this.creeper, this.creeper.getBoundingBox().inflate(10.0D, 4.0D, 10.0D));
         Monster closest = null;
         double closestDist = Double.MAX_VALUE;

         for (Entity entity : list) {
            if (entity instanceof Monster mob && !(entity instanceof EntitySupportCreeper) && mob.getActiveEffects().isEmpty()) {
               double dist = this.creeper.distanceToSqr(mob);
               if (dist < closestDist) {
                  closestDist = dist;
                  closest = mob;
               }
            }
         }

         return closest;
      }

      @Override
      public void tick() {
         if (this.mobIdol == null || !this.mobIdol.isAlive()) {
            if (--this.searchCooldown > 0) {
               return;
            }
            this.searchCooldown = 20;
            this.chargedAlly = false;
            this.mobIdol = this.findMobToSupport();
            return;
         }

         this.strength = this.creeper.isPowered() ? 2 : 1;

         double distSq = this.creeper.distanceToSqr(this.mobIdol);
         if (distSq > 4.0D) {
            if (--this.repathCooldown <= 0) {
               this.repathCooldown = 10;
               this.creeper.getNavigation().moveTo(this.mobIdol, 1.0D);
            }
         } else {
            this.creeper.getNavigation().stop();
         }

         if (this.mobIdol instanceof Creeper ally) {
            if (!this.chargedAlly && !ally.isPowered() && !this.creeper.level().isClientSide) {
               this.chargedAlly = true;
               LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.creeper.level());
               if (bolt != null) {
                  bolt.setDamage(0.0F);
                  ally.thunderHit((ServerLevel)this.creeper.level(), bolt);
                  bolt.discard();
               }
            }

            this.applyEffectIfMissing(ally, MobEffects.DAMAGE_RESISTANCE);
            this.applyEffectIfMissing(this.creeper, MobEffects.DAMAGE_RESISTANCE);
         } else {
            if (this.mobIdol instanceof EntityTrollager trollager) {
               trollager.isBeingSupported = true;
            }

            this.applyEffectIfMissing(this.mobIdol, MobEffects.DAMAGE_BOOST);
            this.applyEffectIfMissing(this.mobIdol, MobEffects.MOVEMENT_SPEED);
            this.applyEffectIfMissing(this.mobIdol, MobEffects.DAMAGE_RESISTANCE);
         }

         this.applyEffectIfMissing(this.creeper, MobEffects.MOVEMENT_SPEED);
      }

      private void applyEffectIfMissing(LivingEntity entity, net.minecraft.world.effect.MobEffect effect) {
         if (entity.getEffect(effect) == null) {
            entity.addEffect(new MobEffectInstance(effect, 100, this.strength));
         }
      }
   }
}

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
      if (!this.isEggTamed() && this.getHealth() < this.getMaxHealth() / 2.0F) {
         while(this.goalSelector.getAvailableGoals().stream().filter((taskEntry) -> taskEntry.getGoal() instanceof AvoidEntityGoal).findFirst().isPresent()) {
            this.goalSelector.getAvailableGoals().stream().filter((taskEntry) -> taskEntry.getGoal() instanceof AvoidEntityGoal).findFirst().ifPresent((taskEntry) -> this.goalSelector.removeGoal(taskEntry.getGoal()));
         }

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
         } else {
            return this.mobIdol != null && this.mobIdol.isAlive() && this.creeper.distanceToSqr(this.mobIdol) <= (double)625.0F;
         }
      }

      @Override
      public void start() {
         this.mobIdol = this.findMobToSupport();
      }

      @Override
      public void stop() {
         this.mobIdol = null;
      }

      public LivingEntity findMobToSupport() {
         ITameableEntity tameable = (ITameableEntity)EntityUtil.getCapability(this.creeper, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, (Direction)null);
         if (tameable != null && tameable.isTamed() && tameable.getFollowState() != 0) {
            if (tameable.getOwner(this.creeper) != null && tameable.getOwner(this.creeper).distanceToSqr(this.creeper) < (double)30.0F) {
               return tameable.getOwner(this.creeper);
            }
         } else if (tameable == null || !tameable.isTamed()) {
            List<Entity> list = this.creeper.level().getEntities(this.creeper, this.creeper.getBoundingBox().inflate((double)10.0F, (double)4.0F, (double)10.0F));
            Monster mob = null;
            double d0 = Double.MAX_VALUE;

            for(Entity entity : list) {
               if (entity != null && entity instanceof Monster && !(entity instanceof EntitySupportCreeper)) {
                  Monster mob1 = (Monster)entity;
                  if (mob1.getActiveEffects().isEmpty()) {
                     double d1 = this.creeper.distanceToSqr(mob1);
                     if (d1 <= d0) {
                        d0 = d1;
                        mob = mob1;
                     }
                  }
               }
            }

            if (mob != null) {
               return mob;
            }
         }

         return null;
      }

      @Override
      public void tick() {
         if (this.mobIdol == null) {
            this.mobIdol = this.findMobToSupport();
         } else {
            if (this.creeper.isPowered()) {
               this.strength = 2;
            } else {
               this.strength = 1;
            }

            if ((double)this.creeper.distanceTo(this.mobIdol) > (double)2.0F) {
               this.creeper.getNavigation().moveTo(this.mobIdol, (double)1.0F);
            }

            if (this.mobIdol instanceof Creeper) {
               Creeper entitycreeper = (Creeper)this.mobIdol;
               if (!entitycreeper.isPowered() && !EntitySupportCreeper.this.level().isClientSide) {
                  LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(EntitySupportCreeper.this.level());
                  bolt.setDamage(0.0F);
                  entitycreeper.thunderHit((ServerLevel)EntitySupportCreeper.this.level(), bolt);
                  bolt.discard();
               }

               if (entitycreeper.getEffect(MobEffects.DAMAGE_RESISTANCE) == null) {
                  entitycreeper.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, this.strength));
               }

               if (this.creeper.getEffect(MobEffects.DAMAGE_RESISTANCE) == null) {
                  this.creeper.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, this.strength));
               }
            } else {
               if (this.mobIdol instanceof EntityTrollager) {
                  EntityTrollager entitytrollager = (EntityTrollager)this.mobIdol;
                  entitytrollager.isBeingSupported = true;
               }

               if (this.mobIdol.getEffect(MobEffects.DAMAGE_BOOST) == null) {
                  this.mobIdol.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, this.strength));
               }

               if (this.mobIdol.getEffect(MobEffects.MOVEMENT_SPEED) == null) {
                  this.mobIdol.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, this.strength));
               }

               if (this.mobIdol.getEffect(MobEffects.DAMAGE_RESISTANCE) == null) {
                  this.mobIdol.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, this.strength));
               }
            }

            if (this.creeper.getEffect(MobEffects.MOVEMENT_SPEED) == null) {
               this.creeper.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, this.strength));
            }
         }

      }
   }
}

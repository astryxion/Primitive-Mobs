package net.daveyx0.primitivemobs.entity.monster;

import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity;
import net.daveyx0.multimob.common.capabilities.ITameableEntity;
import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity.EventHandler;
import net.daveyx0.multimob.util.EntityUtil;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

public class EntityPrimitiveCreeper extends Creeper {
   private int timeSinceIgnited;
   private int lastActiveTime;
   private static final EntityDataAccessor<Boolean> BABY = SynchedEntityData.defineId(EntityPrimitiveCreeper.class, EntityDataSerializers.BOOLEAN);
   protected int growingAge;
   protected int forcedAge;
   protected int forcedAgeTimer;
   private float ageWidth = -1.0F;
   private float ageHeight;
   private float currentScale = 1.0F;

   public EntityPrimitiveCreeper(EntityType<? extends EntityPrimitiveCreeper> type, Level worldIn) {
      super(type, worldIn);
      this.ageWidth = 0.6F;
      this.ageHeight = 1.7F;
   }

   @Override
   public void tick() {
      if (this instanceof EntityFestiveCreeper) {
         this.timeSinceIgnited = 0;
      }

      if (this.getTarget() != null) {
         ITameableEntity tameable = (ITameableEntity)EntityUtil.getCapability(this, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, (Direction)null);
         if (tameable != null && tameable.isTamed() && tameable.getFollowState() == 0) {
            this.setIgnitedTime(0);
            this.setSwellDir(-1);
         }
      }

      super.tick();
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      ITameableEntity tameable = (ITameableEntity)EntityUtil.getCapability(this, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, (Direction)null);
      if (tameable != null && tameable.isTamed()) {
         return !source.is(DamageTypes.CRAMMING) && !source.is(DamageTypes.FELL_OUT_OF_WORLD) && !source.is(DamageTypes.IN_FIRE) && !source.is(DamageTypes.ON_FIRE) && super.hurt(source, amount);
      } else {
         return super.hurt(source, amount);
      }
   }

   public void setIgnitedTime(int time) {
      this.timeSinceIgnited = time;
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(BABY, false);
   }

   public int getGrowingAge() {
      if (this.level().isClientSide) {
         return (Boolean)this.entityData.get(BABY) ? -1 : 1;
      } else {
         return this.growingAge;
      }
   }

   public void ageUp(int growthSeconds, boolean updateForcedAge) {
      int i = this.getGrowingAge();
      int j = i;
      i += growthSeconds * 20;
      if (i > 0) {
         i = 0;
         if (j < 0) {
            this.onGrowingAdult();
         }
      }

      int k = i - j;
      this.setGrowingAge(i);
      if (updateForcedAge) {
         this.forcedAge += k;
         if (this.forcedAgeTimer == 0) {
            this.forcedAgeTimer = 40;
         }
      }

      if (this.getGrowingAge() == 0) {
         this.setGrowingAge(this.forcedAge);
      }

   }

   public void addGrowth(int growth) {
      this.ageUp(growth, false);
   }

   public void setGrowingAge(int age) {
      this.entityData.set(BABY, age < 0);
      this.growingAge = age;
      this.setScaleForAge(this.isBaby());
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("Age", this.getGrowingAge());
      compound.putInt("ForcedAge", this.forcedAge);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setGrowingAge(compound.getInt("Age"));
      this.forcedAge = compound.getInt("ForcedAge");
   }

   @Override
   public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
      if (BABY.equals(key)) {
         this.setScaleForAge(this.isBaby());
      }

      super.onSyncedDataUpdated(key);
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (this.level().isClientSide) {
         if (this.forcedAgeTimer > 0) {
            if (this.forcedAgeTimer % 4 == 0) {
               this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (double)(this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double)this.getBbWidth(), this.getY() + (double)0.5F + (double)(this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double)(this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double)this.getBbWidth(), (double)0.0F, (double)0.0F, (double)0.0F);
            }

            --this.forcedAgeTimer;
         }
      } else {
         int i = this.getGrowingAge();
         if (i < 0) {
            ++i;
            this.setGrowingAge(i);
            if (i == 0) {
               this.onGrowingAdult();
            }
         } else if (i > 0) {
            --i;
            this.setGrowingAge(i);
         }
      }

      ITameableEntity tameable = (ITameableEntity)EntityUtil.getCapability(this, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, (Direction)null);
      if (tameable != null && tameable.isTamed() && this.isBaby()) {
         EventHandler.resetEntityTargetAI(this);
      }

   }

   protected void onGrowingAdult() {
   }

   @Override
   public boolean isBaby() {
      return this.getGrowingAge() < 0;
   }

   public void setScaleForAge(boolean child) {
      this.setScale(child ? 0.5F : 1.0F);
   }

   protected final void setScale(float scale) {
      this.currentScale = scale;
      this.refreshDimensions();
   }

   @Override
   public EntityDimensions getDimensions(Pose pose) {
      return super.getDimensions(pose).scale(this.isBaby() ? 0.5F : 1.0F);
   }
}

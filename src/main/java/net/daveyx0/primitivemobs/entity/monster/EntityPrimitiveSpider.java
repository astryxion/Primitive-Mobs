package net.daveyx0.primitivemobs.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EntityPrimitiveSpider extends EntityPrimitiveTameableMob {
   private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(EntityPrimitiveSpider.class, EntityDataSerializers.BYTE);

   public EntityPrimitiveSpider(EntityType<? extends EntityPrimitiveSpider> type, Level worldIn) {
      super(type, worldIn);
   }

   @Override
   protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
      return new Vec3(0.0, (double)(dimensions.height() * 0.5F), 0.0);
   }

   @Override
   protected PathNavigation createNavigation(Level worldIn) {
      return new WallClimberNavigation(this, worldIn);
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(CLIMBING, (byte)0);
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.level().isClientSide) {
         this.setBesideClimbableBlock(this.horizontalCollision);
      }

   }

   public static AttributeSupplier.Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 16.0)
         .add(Attributes.MOVEMENT_SPEED, 0.3);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.SPIDER_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.SPIDER_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.SPIDER_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos pos, BlockState state) {
      this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
   }

   @Override
   public boolean onClimbable() {
      return this.isBesideClimbableBlock();
   }

   @Override
   public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
   }

   @Override
   public boolean canBeAffected(MobEffectInstance effectInstance) {
      return effectInstance.getEffect() == MobEffects.POISON ? false : super.canBeAffected(effectInstance);
   }

   public boolean isBesideClimbableBlock() {
      return ((Byte)this.entityData.get(CLIMBING) & 1) != 0;
   }

   public void setBesideClimbableBlock(boolean climbing) {
      byte b0 = (Byte)this.entityData.get(CLIMBING);
      if (climbing) {
         b0 = (byte)(b0 | 1);
      } else {
         b0 = (byte)(b0 & -2);
      }

      this.entityData.set(CLIMBING, b0);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData livingdata) {
      livingdata = super.finalizeSpawn(levelAccessor, difficulty, spawnType, livingdata);
      if (this.random.nextInt(100) == 0) {
         Skeleton entityskeleton = new Skeleton(EntityType.SKELETON, this.level());
         entityskeleton.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
         entityskeleton.finalizeSpawn(levelAccessor, difficulty, MobSpawnType.JOCKEY, (SpawnGroupData)null);
         levelAccessor.addFreshEntity(entityskeleton);
         entityskeleton.startRiding(this);
      }

      if (livingdata == null) {
         livingdata = new Spider.SpiderEffectsGroupData();
         if (this.level().getDifficulty() == Difficulty.HARD && this.random.nextFloat() < 0.1F * difficulty.getSpecialMultiplier()) {
            ((Spider.SpiderEffectsGroupData)livingdata).setRandomEffect(this.random);
         }
      }

      if (livingdata instanceof Spider.SpiderEffectsGroupData) {
         Holder<MobEffect> mobeffect = ((Spider.SpiderEffectsGroupData)livingdata).effect;
         if (mobeffect != null) {
            this.addEffect(new MobEffectInstance(mobeffect, Integer.MAX_VALUE));
         }
      }

      return livingdata;
   }

   @Override
   protected EntityDimensions getDefaultDimensions(Pose pose) {
      return super.getDefaultDimensions(pose).withEyeHeight(0.65F);
   }

   @Override
   public boolean isWithinMeleeAttackRange(LivingEntity entity) {
      double reach = this instanceof EntityBabySpider ? (double)(2.0F + entity.getBbWidth()) : (double)(4.0F + entity.getBbWidth());
      return this.distanceToSqr(entity) <= reach * reach;
   }

   static class AISpiderAttack extends MeleeAttackGoal {
      EntityPrimitiveSpider spider;

      public AISpiderAttack(EntityPrimitiveSpider spider) {
         super(spider, 1.0, true);
         this.spider = spider;
      }

      @Override
      public boolean canContinueToUse() {
         float f = this.mob.getLightLevelDependentMagicValue();
         if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
            this.mob.setTarget((LivingEntity)null);
            return false;
         } else {
            return super.canContinueToUse();
         }
      }

   }

   static class AISpiderTarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
      public AISpiderTarget(EntityPrimitiveSpider spider, Class<T> classTarget) {
         super(spider, classTarget, true);
      }

      @Override
      public boolean canUse() {
         float f = this.mob.getLightLevelDependentMagicValue();
         return f >= 0.5F ? false : super.canUse();
      }
   }

   public static class GroupData implements SpawnGroupData {
      public Holder<MobEffect> effect;

      public void setRandomEffect(RandomSource rand) {
         int i = rand.nextInt(5);
         if (i <= 1) {
            this.effect = MobEffects.MOVEMENT_SPEED;
         } else if (i <= 2) {
            this.effect = MobEffects.DAMAGE_BOOST;
         } else if (i <= 3) {
            this.effect = MobEffects.REGENERATION;
         } else if (i <= 4) {
            this.effect = MobEffects.INVISIBILITY;
         }

      }
   }
}

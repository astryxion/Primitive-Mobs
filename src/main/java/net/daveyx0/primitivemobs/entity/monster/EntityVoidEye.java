package net.daveyx0.primitivemobs.entity.monster;

import java.util.List;
import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.EntityMMFlyingCreature;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.multimob.entity.ai.EntityAIFlyingAround;
import net.daveyx0.multimob.entity.ai.EntityAISenseEntityNearestPlayer;
import net.daveyx0.primitivemobs.message.PrimitiveMobsMessageRegistry;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.daveyx0.primitivemobs.core.PrimitiveMobsSoundEvents;
import net.daveyx0.primitivemobs.message.MessageTeleportEye;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityTeleportEvent;

public class EntityVoidEye extends EntityMMFlyingCreature implements IMultiMob {
   private static final EntityDataAccessor<Integer> TARGET_ENTITY = SynchedEntityData.defineId(EntityVoidEye.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> CAN_SEE_TARGET = SynchedEntityData.defineId(EntityVoidEye.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> DOES_TELEPORT = SynchedEntityData.defineId(EntityVoidEye.class, EntityDataSerializers.BOOLEAN);
   private LivingEntity targetedEntity;
   private int clientSideAttackTime;

   public EntityVoidEye(EntityType<? extends EntityVoidEye> type, Level worldIn) {
      super(type, worldIn);
      this.setNoGravity(true);
      this.setTeleports(false);
   }

   @Override
   protected PathNavigation createNavigation(Level level) {
      FlyingPathNavigation nav = new FlyingPathNavigation(this, level) {
         @Override
         public boolean isStableDestination(BlockPos pos) {
            return !this.level.getBlockState(pos).isSolid();
         }
      };
      return nav;
   }

   public static AttributeSupplier.Builder createAttributes() {
      return EntityMMFlyingCreature.createAttributes()
         .add(Attributes.ATTACK_DAMAGE, (double)5.0F)
         .add(Attributes.MOVEMENT_SPEED, (double)1.0F)
         .add(Attributes.FOLLOW_RANGE, (double)30.0F)
         .add(Attributes.MAX_HEALTH, (double)15.0F);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new AIVoidEyeAttack(this));
      this.goalSelector.addGoal(2, new EntityAIFlyingAround(this));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(0, new EntityAISenseEntityNearestPlayer(this, 18));
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(TARGET_ENTITY, 0);
      this.entityData.define(CAN_SEE_TARGET, false);
      this.entityData.define(DOES_TELEPORT, false);
   }

   public void setCanSeeTarget(boolean sees) {
      this.entityData.set(CAN_SEE_TARGET, sees);
   }

   public boolean canSeeTarget() {
      return (Boolean)this.entityData.get(CAN_SEE_TARGET);
   }

   public void setTeleports(boolean sees) {
      this.entityData.set(DOES_TELEPORT, sees);
   }

   public boolean doesTeleport() {
      return (Boolean)this.entityData.get(DOES_TELEPORT);
   }

   public float getAttackAnimationScale(float p_175477_1_) {
      return ((float)this.clientSideAttackTime + p_175477_1_) / (float)this.getAttackDuration();
   }

   @Override
   protected float getStandingEyeHeight(net.minecraft.world.entity.Pose pose, net.minecraft.world.entity.EntityDimensions dimensions) {
      return dimensions.height * 0.5F;
   }

   @Override
   public void aiStep() {
      if (this.level().isClientSide && this.hasTargetedEntity() && this.clientSideAttackTime < this.getAttackDuration()) {
         ++this.clientSideAttackTime;
      }

      if (!this.level().isClientSide) {
         if (this.getTargetedEntity() != null && this.hasLineOfSight(this.getTargetedEntity())) {
            this.setCanSeeTarget(true);
         } else {
            this.setCanSeeTarget(false);
         }
      }

      if (this.doesTeleport()) {
         this.teleportRandomly();
      }

      if (this.hasTargetedEntity()) {
         this.setYRot(this.yHeadRot);
      }

      super.aiStep();
   }

   @Override
   public void travel(Vec3 travelVector) {
      if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
         if (this.isInWater()) {
            this.moveRelative(0.02F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8));
         } else if (this.isInLava()) {
            this.moveRelative(0.02F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
         } else {
            float friction = 0.91F;
            if (this.onGround()) {
               friction = this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.91F;
            }
            float f1 = 0.16277137F / (friction * friction * friction);
            this.moveRelative(this.onGround() ? 0.1F * f1 : 0.02F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale((double)friction));
         }
      }
      this.calculateEntityAnimation(false);
   }

   @Nullable
   public LivingEntity getTargetedEntity() {
      if (!this.hasTargetedEntity()) {
         return null;
      } else if (this.level().isClientSide) {
         if (this.targetedEntity != null) {
            return this.targetedEntity;
         } else {
            Entity entity = this.level().getEntity((Integer)this.entityData.get(TARGET_ENTITY));
            if (entity instanceof LivingEntity) {
               this.targetedEntity = (LivingEntity)entity;
               return this.targetedEntity;
            } else {
               return null;
            }
         }
      } else {
         return this.getTarget();
      }
   }

   @Override
   public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
      super.onSyncedDataUpdated(key);
      if (TARGET_ENTITY.equals(key)) {
         this.clientSideAttackTime = 0;
         this.targetedEntity = null;
      }

   }

   public int getAttackDuration() {
      return 75;
   }

   private void setTargetedEntity(int entityId) {
      this.entityData.set(TARGET_ENTITY, entityId);
   }

   public boolean hasTargetedEntity() {
      return (Integer)this.entityData.get(TARGET_ENTITY) != 0;
   }

   public int getTargetedEntityID() {
      return (Integer)this.entityData.get(TARGET_ENTITY);
   }

   protected boolean isMovementNoisy() {
      return false;
   }

   @Override
   public void tick() {
      super.tick();
      this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + (double)0.5F, this.getZ(), (double)0.0F, (double)0.0F, (double)0.0F);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (this.isInvulnerableTo(source)) {
         return false;
      } else {
         boolean flag = super.hurt(source, amount);
         if (source.getEntity() != null && this.getRandom().nextInt(5) != 0 && this.level().isClientSide) {
            this.setTeleports(true);
            PrimitiveMobsMessageRegistry.getPrimitiveNetwork().sendToServer(new MessageTeleportEye(true, this.getUUID().toString()));
         }

         return flag;
      }
   }

   protected boolean teleportRandomly() {
      double d0 = this.getX() + (this.getRandom().nextDouble() - (double)0.5F) * (double)16.0F;
      double d1 = this.getY() + (double)(this.getRandom().nextInt(32) - 16);
      double d2 = this.getZ() + (this.getRandom().nextDouble() - (double)0.5F) * (double)16.0F;
      return this.teleportToPosition(d0, d1, d2);
   }

   @Nullable
   @Override
   protected ResourceLocation getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_VOIDEYE;
   }

   private boolean teleportToPosition(double x, double y, double z) {
      EntityTeleportEvent.EnderEntity event = new EntityTeleportEvent.EnderEntity(this, x, y, z);
      if (MinecraftForge.EVENT_BUS.post(event)) {
         return false;
      } else {
         boolean flag = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
         if (flag) {
            this.level().playSound((Player)null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            this.setTeleports(false);
         }

         return flag;
      }
   }

   @Nullable
   @Override
   public SoundEvent getAmbientSound() {
      return PrimitiveMobsSoundEvents.ENTITY_VOIDEYE_IDLE.get();
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.ENDERMITE_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENDERMAN_DEATH;
   }

   public boolean checkSpawnRules(Level level, net.minecraft.world.entity.MobSpawnType spawnType) {
      if (this.level().dimension() == Level.END) {
         for(Entity entity : this.level().getEntities(this, new AABB(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE), e -> true)) {
            if (entity instanceof EnderDragon) {
               return false;
            }
         }
      }

      return super.checkSpawnRules(level, spawnType);
   }

   static class AIVoidEyeAttack extends Goal {
      private final EntityVoidEye eye;
      private int tickCounter;

      public AIVoidEyeAttack(EntityVoidEye eye) {
         this.eye = eye;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         LivingEntity entitylivingbase = this.eye.getTarget();
         List<EntityVoidEye> voidEyesList = this.eye.level().getEntitiesOfClass(EntityVoidEye.class, this.eye.getBoundingBox().inflate((double)20.0F, (double)20.0F, (double)20.0F));
         if (voidEyesList != null && !voidEyesList.isEmpty() && !this.eye.hasTargetedEntity()) {
            for(EntityVoidEye voidEye : voidEyesList) {
               if (entitylivingbase != null && voidEye.hasTargetedEntity() && voidEye.getTargetedEntityID() == entitylivingbase.getId()) {
                  return false;
               }
            }
         }

         return entitylivingbase != null && entitylivingbase.isAlive();
      }

      @Override
      public void start() {
         this.tickCounter = -10;
         this.eye.getNavigation().stop();
         this.eye.getLookControl().setLookAt(this.eye.getTarget(), 90.0F, 90.0F);
         this.eye.noPhysics = true;
      }

      @Override
      public void stop() {
         this.eye.setTargetedEntity(0);
         this.eye.setTarget((LivingEntity)null);
         this.eye.noPhysics = false;
      }

      @Override
      public void tick() {
         LivingEntity entitylivingbase = this.eye.getTarget();
         this.eye.getNavigation().stop();
         this.eye.getMoveControl().setWantedPosition(this.eye.getX(), this.eye.getY(), this.eye.getZ(), 0);
         this.eye.getLookControl().setLookAt(entitylivingbase, 90.0F, 90.0F);
         ++this.tickCounter;
         if (this.tickCounter == 0) {
            this.eye.playSound(SoundEvents.GUARDIAN_ATTACK, 1.0F, 1.0F);
            this.eye.setTargetedEntity(this.eye.getTarget().getId());
         } else if (this.tickCounter >= this.eye.getAttackDuration()) {
            if (this.eye.canSeeTarget()) {
               entitylivingbase.hurt(this.eye.damageSources().indirectMagic(this.eye, this.eye), (float)this.eye.getAttributeValue(Attributes.ATTACK_DAMAGE));
            }

            entitylivingbase.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 240, 0));
            entitylivingbase.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 240, 0));
            entitylivingbase.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 240, 0));
            this.eye.setTarget((LivingEntity)null);
         }

         super.tick();
      }
   }
}

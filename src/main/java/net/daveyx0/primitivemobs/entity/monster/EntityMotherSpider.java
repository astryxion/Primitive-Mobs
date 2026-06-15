package net.daveyx0.primitivemobs.entity.monster;

import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigMobs;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.daveyx0.primitivemobs.core.PrimitiveMobsEntityRegistry;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.daveyx0.primitivemobs.core.PrimitiveMobsSoundEvents;
import net.daveyx0.primitivemobs.entity.ai.EntityAIFollowerHurtByTarget;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;

public class EntityMotherSpider extends EntityPrimitiveSpider implements IMultiMob {
   private final int minFollowers = 3;
   private final int maxFollowers = PrimitiveMobsConfigSpecial.getMaxSpiderFamilySize();
   private LivingEntity[] followers;
   private int riderNavTick;
   /** When > 0, baby spiders are spawned one at a time after this many ticks between each. */
   private int pendingBabySpawnTicks;
   private int babiesLeftToSpawn;
   private static final EntityDataAccessor<Boolean> IS_ANGRY = SynchedEntityData.defineId(EntityMotherSpider.class, EntityDataSerializers.BOOLEAN);

   public EntityMotherSpider(EntityType<? extends EntityMotherSpider> type, Level worldIn) {
      super(type, worldIn);
      this.followers = new LivingEntity[this.maxFollowers];
   }

   public static AttributeSupplier.Builder createAttributes() {
      return EntityPrimitiveTameableMob.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 30.0D)
         .add(Attributes.MOVEMENT_SPEED, 0.3D)
         .add(Attributes.ATTACK_DAMAGE, 4.0D);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      if (!PrimitiveMobsConfigMobs.enableSpiderFamily) {
         this.discard();
      }

      this.entityData.define(IS_ANGRY, false);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (!this.level().isClientSide && amount > 0.0F) {
         Entity attacker = source.getEntity();
         if (attacker instanceof LivingEntity living && !(attacker instanceof EntityBabySpider)) {
            this.setIsAngry(true);
            this.setTarget(living);
         }
      }

      return super.hurt(source, amount);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
      this.goalSelector.addGoal(4, new EntityPrimitiveSpider.AISpiderAttack(this, false));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new EntityAIFollowerHurtByTarget(this));
      this.targetSelector.addGoal(3, new EntityPrimitiveSpider.AISpiderTarget<>(this, Player.class, false));
      this.targetSelector.addGoal(4, new EntityPrimitiveSpider.AISpiderTarget<>(this, IronGolem.class, false));
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.level().isClientSide && this.babiesLeftToSpawn > 0 && --this.pendingBabySpawnTicks <= 0) {
         this.spawnNextBabyFollower();
         this.babiesLeftToSpawn--;
         this.pendingBabySpawnTicks = 4;
      }

      if (!this.getPassengers().isEmpty()) {
         if (this.getPassengers().get(0) instanceof EntityBabySpider) {
            EntityBabySpider baby = (EntityBabySpider)this.getPassengers().get(0);
            if (baby != null && ++this.riderNavTick >= 10) {
               this.riderNavTick = 0;
               baby.getNavigation().moveTo(this.getNavigation().getPath(), 1.5D);
               if (this.getMoveControl().hasWanted()) {
                  baby.getMoveControl().setWantedPosition(this.getMoveControl().getWantedX(), this.getMoveControl().getWantedY(), this.getMoveControl().getWantedZ(), this.getMoveControl().getSpeedModifier());
               }
            }
         }

         this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.18D);
      } else {
         this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3D);
      }

      if (this.isAngry()) {
         this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0D);
         if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SMOKE, this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() + (double)this.random.nextFloat(), this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), 0.0D, 0.0D, 0.0D);
         }
      }

   }

   @Override
   public boolean checkSpawnObstruction(LevelReader level) {
      return !this.level().containsAnyLiquid(this.getBoundingBox()) && this.level().getEntities(this, this.getBoundingBox()).isEmpty();
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return PrimitiveMobsSoundEvents.ENTITY_MOTHERSPIDER_SCREECH.get();
   }

   public void addFollower(LivingEntity follower) {
      for(int i = 0; i < this.followers.length; ++i) {
         if (this.followers[i] == null) {
            this.followers[i] = follower;
            return;
         }
      }

   }

   public LivingEntity[] getFollowers() {
      return this.followers;
   }

   public boolean hasFollowers() {
      if (this.getFollowers() == null) {
         return false;
      } else {
         for(int i = 0; i < this.followers.length; ++i) {
            if (this.followers[i] != null) {
               return true;
            }
         }

         return false;
      }
   }

   private void queueBabyFollowers() {
      if (this.level().isClientSide || !this.isAlive()) {
         return;
      }

      int queued = 0;

      for (int i = 0; i < this.maxFollowers; ++i) {
         if (this.random.nextInt(2) == 0 || i <= 3) {
            ++queued;
         }
      }

      this.babiesLeftToSpawn = queued;
      this.pendingBabySpawnTicks = 1;
   }

   private void spawnNextBabyFollower() {
      EntityBabySpider entityBabySpider = new EntityBabySpider(PrimitiveMobsEntityRegistry.BABY_SPIDER.get(), this.level());
      entityBabySpider.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
      entityBabySpider.setTamed(true);
      entityBabySpider.setOwnerId(this.getUUID());
      this.level().addFreshEntity(entityBabySpider);
      if (!this.isVehicle()) {
         entityBabySpider.startRiding(this);
      }
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag) {
      this.getAttribute(Attributes.FOLLOW_RANGE).addTransientModifier(new AttributeModifier("Random spawn bonus", this.random.nextGaussian() * 0.05D, AttributeModifier.Operation.MULTIPLY_BASE));
      if (spawnData == null) {
         spawnData = new EntityPrimitiveSpider.GroupData();
         if (this.level().getDifficulty() == Difficulty.HARD && this.level().random.nextFloat() < 0.1F * difficulty.getSpecialMultiplier()) {
            ((EntityPrimitiveSpider.GroupData)spawnData).setRandomEffect(this.level().random);
         }
      }

      if (spawnData instanceof EntityPrimitiveSpider.GroupData) {
         MobEffect effect = ((EntityPrimitiveSpider.GroupData)spawnData).effect;
         if (effect != null) {
            this.addEffect(new MobEffectInstance(effect, Integer.MAX_VALUE));
         }
      }

      if (!this.level().isClientSide) {
         this.queueBabyFollowers();
      }

      return spawnData;
   }

   public void setIsAngry(boolean begging) {
      this.entityData.set(IS_ANGRY, begging);
   }

   public boolean isAngry() {
      return this.entityData.get(IS_ANGRY);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("Angry", this.isAngry());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setIsAngry(compound.getBoolean("Angry"));
   }

   @Override
   protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
      super.positionRider(passenger, moveFunction);
      float f = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F));
      float f1 = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F));
      float f2 = 0.0F;
      if (passenger instanceof EntityBabySpider) {
         f2 = 0.25F;
      }

      moveFunction.accept(passenger, this.getX() + (double)(0.1F * f), this.getY() + (double)(this.getBbHeight() * 0.5F + f2) + passenger.getMyRidingOffset() + 0.0D, this.getZ() - (double)(0.1F * f1));
      if (passenger instanceof LivingEntity) {
         ((LivingEntity)passenger).yBodyRot = this.yBodyRot;
      }

   }

   @Nullable
   @Override
   protected ResourceLocation getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_MOTHERSPIDER;
   }
}

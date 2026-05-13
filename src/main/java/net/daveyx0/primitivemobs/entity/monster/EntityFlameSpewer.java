package net.daveyx0.primitivemobs.entity.monster;

import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMobLava;
import net.daveyx0.primitivemobs.core.PrimitiveMobsEntityRegistry;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.daveyx0.primitivemobs.core.PrimitiveMobsSoundEvents;
import net.daveyx0.primitivemobs.entity.item.EntityFlameSpit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class EntityFlameSpewer extends Monster implements RangedAttackMob, IMultiMobLava {
   private static final EntityDataAccessor<Byte> ON_FIRE = SynchedEntityData.defineId(EntityFlameSpewer.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Byte> IN_DANGER = SynchedEntityData.defineId(EntityFlameSpewer.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Integer> ATTACK_TIME = SynchedEntityData.defineId(EntityFlameSpewer.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Float> ATTACK_SIGNAL = SynchedEntityData.defineId(EntityFlameSpewer.class, EntityDataSerializers.FLOAT);

   public EntityFlameSpewer(EntityType<? extends EntityFlameSpewer> type, Level worldIn) {
      super(type, worldIn);
      this.blocksBuilding = true;
      this.setOnFire(false);
      this.setInDanger(false);
      this.setAttackTime(10);
      this.setAttackSignal(0.0F);
      this.setPathfindingMalus(BlockPathTypes.LAVA, 10.0F);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(3, new AIGoToLava(this));
      this.goalSelector.addGoal(4, new AIFlameSpewAttack(this));
      this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.ATTACK_DAMAGE, (double)6.0F)
         .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
         .add(Attributes.FOLLOW_RANGE, (double)48.0F);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(ON_FIRE, (byte)0);
      this.entityData.define(IN_DANGER, (byte)0);
      this.entityData.define(ATTACK_TIME, 0);
      this.entityData.define(ATTACK_SIGNAL, 0.0F);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return PrimitiveMobsSoundEvents.ENTITY_FLAMESPEWER_IDLE.get();
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SoundEvents.GENERIC_BURN;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.GENERIC_EXTINGUISH_FIRE;
   }

   @Override
   public boolean isInvulnerableTo(DamageSource source) {
      return this.getAttackTime() < 10 || this.getAttackSignal() > 0.0F;
   }

   public void setAttackTime(int time) {
      this.entityData.set(ATTACK_TIME, time);
   }

   public void setAttackSignal(float signal) {
      this.entityData.set(ATTACK_SIGNAL, signal);
   }

   public int getAttackTime() {
      return (Integer)this.entityData.get(ATTACK_TIME);
   }

   public float getAttackSignal() {
      return (Float)this.entityData.get(ATTACK_SIGNAL);
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.isInLava() && !this.isInWater()) {
         if (this.tickCount % 25 == 0) {
            this.hurt(this.damageSources().dryOut(), 1.0F);
            this.jumpFromGround();
            this.setZza(1.0F);
         }
      } else {
         if (!this.level().isEmptyBlock(new BlockPos((int)this.getX(), (int)(this.getY() + (double)0.5F), (int)this.getZ()))) {
            Vec3 delta = this.getDeltaMovement();
            this.setDeltaMovement(delta.x, 0.1, delta.z);
         } else {
            Vec3 delta = this.getDeltaMovement();
            this.setDeltaMovement(delta.x, (double)0.0F, delta.z);
         }

         if (this.isInWater() && this.tickCount % 15 == 0) {
            this.hurt(this.damageSources().dryOut(), 4.0F);
         }
      }

   }

   @Nullable
   @Override
   public ItemEntity spawnAtLocation(ItemStack stack, float offsetY) {
      if (stack.isEmpty()) {
         return null;
      } else {
         ItemEntity entityitem = new ItemEntity(this.level(), this.getX(), this.getY() + (double)1.5F, this.getZ(), stack);
         entityitem.setDefaultPickUpDelay();

         for(int i = 0; i < 50; ++i) {
            Vec3 vec = net.minecraft.world.entity.ai.util.DefaultRandomPos.getPosAway(this, 10, 7, this.position());
            if (vec != null) {
               entityitem.setDeltaMovement(
                  (vec.x - entityitem.getX()) / (double)18.0F,
                  (vec.y - entityitem.getY()) / (double)18.0F + (double)0.5F,
                  (vec.z - entityitem.getZ()) / (double)18.0F
               );
               break;
            }
         }

         this.level().addFreshEntity(entityitem);
         return entityitem;
      }
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("AttackTime", this.getAttackTime());
      compound.putFloat("AttackSignal", this.getAttackSignal());
      compound.putBoolean("onFire", this.isOnFire());
      compound.putBoolean("inDanger", this.isInDanger());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setAttackTime(compound.getInt("AttackTime"));
      this.setAttackSignal(compound.getFloat("AttackSignal"));
      this.setOnFire(compound.getBoolean("onFire"));
      this.setInDanger(compound.getBoolean("inDanger"));
   }

   @Override
   public void performRangedAttack(LivingEntity target, float distanceFactor) {
   }

   @Override
   public void setAggressive(boolean aggressive) {
   }

   @Nullable
   @Override
   protected ResourceLocation getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_FLAMESPEWER;
   }

   public boolean isOnFire() {
      return ((Byte)this.entityData.get(ON_FIRE) & 1) != 0;
   }

   public void setOnFire(boolean onFire) {
      byte b0 = (Byte)this.entityData.get(ON_FIRE);
      if (onFire) {
         b0 = (byte)(b0 | 1);
      } else {
         b0 = (byte)(b0 & -2);
      }

      this.entityData.set(ON_FIRE, b0);
   }

   public boolean isInDanger() {
      return ((Byte)this.entityData.get(IN_DANGER) & 1) != 0;
   }

   public void setInDanger(boolean inDanger) {
      byte b0 = (Byte)this.entityData.get(IN_DANGER);
      if (inDanger) {
         b0 = (byte)(b0 | 1);
      } else {
         b0 = (byte)(b0 & -2);
      }

      this.entityData.set(IN_DANGER, b0);
   }

   @Override
   protected boolean isAlwaysExperienceDropper() {
      return true;
   }

   @Override
   public boolean checkSpawnObstruction(LevelReader level) {
      return level.isUnobstructed(this);
   }

   @Override
   public boolean checkSpawnRules(net.minecraft.world.level.LevelAccessor level, MobSpawnType spawnType) {
      return this.getY() < (double)64.0F;
   }

   @Override
   public MobCategory getClassification(boolean forSpawnCount) {
      return forSpawnCount && MobCategory.MONSTER == MobCategory.MONSTER ? MobCategory.CREATURE : super.getClassification(forSpawnCount);
   }

   static class AIFlameSpewAttack extends Goal {
      private final EntityFlameSpewer spewer;
      private int attackStep;
      private int attackTime;
      private float attackSignal;
      private boolean performingAttack;
      private boolean hasSeenPlayerThisAttack;

      public AIFlameSpewAttack(EntityFlameSpewer spewerIn) {
         this.spewer = spewerIn;
         this.setFlags(java.util.EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         LivingEntity entitylivingbase = this.spewer.getTarget();
         return entitylivingbase != null && entitylivingbase.isAlive();
      }

      @Override
      public void start() {
         this.attackStep = 10;
         this.attackSignal = 0.0F;
         this.attackTime = 100;
         this.performingAttack = false;
         this.hasSeenPlayerThisAttack = false;
      }

      @Override
      public boolean canContinueToUse() {
         return this.canUse();
      }

      @Override
      public void stop() {
         this.spewer.setOnFire(false);
         this.attackSignal = 0.0F;
         this.attackStep = 10;
         this.attackTime = 100;
         this.spewer.setAttackTime(this.attackStep);
         this.spewer.setAttackSignal(this.attackSignal);
         this.spewer.setOnFire(false);
         this.spewer.setInDanger(false);
         this.performingAttack = false;
      }

      @Override
      public void tick() {
         --this.attackTime;
         LivingEntity entitylivingbase = this.spewer.getTarget();
         if (!this.performingAttack && this.spewer.isInLava() && this.spewer.hasLineOfSight(entitylivingbase)) {
            if (this.attackTime <= 50) {
               this.spewer.setOnFire(false);
               this.hasSeenPlayerThisAttack = false;
            }

            if (!this.spewer.isOnFire()) {
               if (this.spewer.hasLineOfSight(entitylivingbase)) {
                  this.hasSeenPlayerThisAttack = true;
               }

               this.attackStep = this.attackTime * 2 / 10;
               if (this.attackTime <= 3) {
                  this.attackSignal += 0.05F;
               }
            }
         }

         double d0 = this.spewer.distanceToSqr(entitylivingbase);
         if (d0 < (double)5.0F && !this.spewer.isInLava()) {
            this.spewer.setInDanger(true);
            if (this.attackTime <= 0) {
               this.attackTime = 20;
               this.spewer.doHurtTarget(entitylivingbase);
            }
         } else if (d0 < this.getFollowDistance() / (double)2.0F * (this.getFollowDistance() / (double)2.0F) && this.spewer.isInLava()) {
            this.spewer.setInDanger(false);
            double d1 = entitylivingbase.getX() - this.spewer.getX();
            float halfHeight = entitylivingbase.getBbHeight() / 2.0F;
            double d2 = entitylivingbase.getBoundingBox().minY + (double)(halfHeight + 0.25F) - (this.spewer.getY() + (double)(this.spewer.getBbHeight() / 2.0F));
            double d3 = entitylivingbase.getZ() - this.spewer.getZ();
            if (this.attackTime <= 0) {
               ++this.attackStep;
               this.attackSignal -= 0.05F;
               if (this.attackStep == 1) {
                  this.attackTime = 30;
                  this.spewer.setOnFire(true);
                  this.performingAttack = true;
               } else if (this.attackStep <= 10) {
                  this.attackTime = 3;
                  this.spewer.setOnFire(true);
                  this.performingAttack = true;
               } else {
                  this.attackTime = 100;
                  this.attackStep = 10;
                  this.performingAttack = false;
               }

               if (this.attackStep > 1 && this.hasSeenPlayerThisAttack) {
                  float f = Mth.sqrt(Mth.sqrt((float)d0) * 0.1F);
                  this.spewer.level().levelEvent((Player)null, 1018, new BlockPos((int)this.spewer.getX(), (int)this.spewer.getY(), (int)this.spewer.getZ()), 0);

                  for(int i = 0; i < 1; ++i) {
                     EntityFlameSpit entitysmallfireball = new EntityFlameSpit(this.spewer.level(), this.spewer, d1 + this.spewer.getRandom().nextGaussian() * (double)0.02F * (double)f, d2 - this.spewer.getRandom().nextGaussian() * (double)0.02F * (double)f, d3 + this.spewer.getRandom().nextGaussian() * (double)0.02F * (double)f);
                     entitysmallfireball.setPos(entitysmallfireball.getX(), this.spewer.getY() + (double)(this.spewer.getBbHeight() / 2.0F) - (double)0.5F, entitysmallfireball.getZ());
                     this.spewer.level().addFreshEntity(entitysmallfireball);
                  }
               }
            }
         } else if (this.spewer.isInLava() && this.spewer.hasLineOfSight(entitylivingbase)) {
            double d1 = entitylivingbase.getX() - this.spewer.getX();
            double d3 = entitylivingbase.getZ() - this.spewer.getZ();
            Vec3 delta = this.spewer.getDeltaMovement();
            this.spewer.setDeltaMovement(d1 * 0.01, delta.y, d3 * 0.01);
            this.spewer.setInDanger(false);
            this.spewer.setOnFire(false);
         } else {
            this.spewer.getNavigation().stop();
            this.spewer.setInDanger(false);
            this.spewer.setOnFire(false);
         }

         this.spewer.getLookControl().setLookAt(entitylivingbase, 10.0F, 10.0F);
         if (this.attackSignal < 0.0F) {
            this.attackSignal = 0.0F;
         } else if ((double)this.attackSignal > 0.4) {
            this.attackSignal = 0.4F;
         }

         this.spewer.setAttackSignal(this.attackSignal);
         this.spewer.setAttackTime(this.attackStep);
         super.tick();
      }

      private double getFollowDistance() {
         AttributeInstance iattributeinstance = this.spewer.getAttribute(Attributes.FOLLOW_RANGE);
         return iattributeinstance == null ? (double)16.0F : iattributeinstance.getValue();
      }
   }

   static class AIGoToLava extends MoveToBlockGoal {
      private final EntityFlameSpewer spewer;

      public AIGoToLava(EntityFlameSpewer spewer) {
         super(spewer, (double)0.7F, 25);
         this.spewer = spewer;
      }

      @Override
      public boolean canUse() {
         return !this.spewer.isInLava() && super.canUse();
      }

      @Override
      public void tick() {
         super.tick();
         this.spewer.getLookControl().setLookAt((double)this.blockPos.getX() + (double)0.5F, (double)(this.blockPos.getY() + 1), (double)this.blockPos.getZ() + (double)0.5F, 10.0F, (float)this.spewer.getMaxHeadXRot());
         if (this.isReachedTarget()) {
            this.nextStartTick = 10;
         }

      }

      @Override
      protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
         Block block = worldIn.getBlockState(pos).getBlock();
         return block == Blocks.LAVA && worldIn.isEmptyBlock(pos.above()) && worldIn.getBlockState(pos.east()).getBlock() == Blocks.LAVA && worldIn.getBlockState(pos.west()).getBlock() == Blocks.LAVA && worldIn.getBlockState(pos.south()).getBlock() == Blocks.LAVA && worldIn.getBlockState(pos.north()).getBlock() == Blocks.LAVA;
      }
   }
}

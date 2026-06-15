package net.daveyx0.primitivemobs.entity.monster;

import javax.annotation.Nullable;
import java.util.EnumSet;
import net.daveyx0.multimob.common.capabilities.CapabilityTameableEntity;
import net.daveyx0.multimob.common.capabilities.ITameableEntity;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.multimob.entity.ai.EntityAITameableFollowOwner;
import net.daveyx0.primitivemobs.entity.ai.EntityAIPrimitiveOwnerHurtByTarget;
import net.daveyx0.primitivemobs.entity.ai.EntityAIPrimitiveOwnerHurtTarget;
import net.daveyx0.multimob.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityBabySpider extends EntityPrimitiveSpider implements IMultiMob {
   private int rideAttemptDelay;
   boolean initChild;
   private int jumpMountTicks;
   private LivingEntity cachedOwner;
   private int ownerCacheTick;
   private int lastGrowthLevel = -1;
   private static final EntityDataAccessor<Byte> DYE_COLOR = SynchedEntityData.defineId(EntityBabySpider.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Integer> GROWTH_LEVEL = SynchedEntityData.defineId(EntityBabySpider.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> IS_JUMPING = SynchedEntityData.defineId(EntityBabySpider.class, EntityDataSerializers.BOOLEAN);

   public EntityBabySpider(EntityType<? extends EntityBabySpider> type, Level worldIn) {
      super(type, worldIn);
      this.rideAttemptDelay = this.random.nextInt(100);
      this.initChild = false;
      this.setTamed(false);
      this.setGrowthLevel(0);
   }

   public static AttributeSupplier.Builder createAttributes() {
      return EntityPrimitiveTameableMob.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 8.0D)
         .add(Attributes.MOVEMENT_SPEED, 0.3D)
         .add(Attributes.ATTACK_DAMAGE, 2.0D);
   }

   @Nullable
   @Override
   public LivingEntity getOwner() {
      if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
         return this.cachedOwner;
      }
      if (this.tickCount - this.ownerCacheTick < 100) {
         return this.cachedOwner;
      }
      this.ownerCacheTick = this.tickCount;
      this.cachedOwner = super.getOwner();
      return this.cachedOwner;
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(GROWTH_LEVEL, 0);
      this.entityData.define(DYE_COLOR, (byte)0);
      this.entityData.define(IS_JUMPING, false);
   }

   @Override
   protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
      return dimensions.height / 2.0F;
   }

   @Nullable
   @Override
   public LivingEntity getControllingPassenger() {
      if (this.getGrowthLevel() >= 5) {
         Entity entity = this.getFirstPassenger();
         if (entity instanceof LivingEntity) {
            return (LivingEntity)entity;
         }
      }
      return null;
   }

   @Override
   public boolean onClimbable() {
      int j = Mth.floor(this.getX());
      int k = Mth.floor(this.getY());
      int l = Mth.floor(this.getZ());
      boolean side1 = this.level().isEmptyBlock(new BlockPos(j + 1, k, l));
      boolean side2 = this.level().isEmptyBlock(new BlockPos(j - 1, k, l));
      boolean side3 = this.level().isEmptyBlock(new BlockPos(j, k, l + 1));
      boolean side4 = this.level().isEmptyBlock(new BlockPos(j, k, l - 1));
      return this.isBesideClimbableBlock() || side1 || side2 || side3 || side4;
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new FloatGoal(this));
      this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
      this.goalSelector.addGoal(4, new EntityPrimitiveSpider.AISpiderAttack(this));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
      this.goalSelector.addGoal(6, new AIBabyFollowOwner(this, 1.0D, 8.0F, 2.0F));
      this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new EntityAIPrimitiveOwnerHurtByTarget(this));
      this.targetSelector.addGoal(3, new EntityAIPrimitiveOwnerHurtTarget(this));
   }

   @Override
   protected boolean isImmobile() {
      ITameableEntity tameable = (ITameableEntity)EntityUtil.getCapability(this, CapabilityTameableEntity.TAMEABLE_ENTITY_CAPABILITY, (Direction)null);
      return tameable != null && tameable.isTamed() && tameable.getFollowState() == 0;
   }

   @Override
   public boolean isPushable() {
      return !this.isVehicle();
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      LivingEntity owner = this.getOwner();
      if (owner instanceof EntityMotherSpider) {
         ((EntityMotherSpider)owner).setIsAngry(true);
      }

      return source.is(DamageTypes.IN_WALL) ? !this.isPassenger() : super.hurt(source, amount);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.isPassenger()) {
         this.getNavigation().stop();
      }
      LivingEntity owner = this.getOwner();
      if (!this.initChild && owner != null && owner instanceof EntityMotherSpider) {
         ((EntityMotherSpider)owner).addFollower(this);
         this.initChild = true;
      }

      if (this.rideAttemptDelay++ > 100 && owner != null && !owner.isVehicle() && owner instanceof EntityMotherSpider) {
         this.startRiding(owner);
      }

      if (this.level().isClientSide && !this.isTamed() && this.getOwnerId() == null) {
         this.level().addParticle(ParticleTypes.SPLASH, this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() + (double)(this.random.nextFloat() - this.random.nextFloat()) + 1.0D, this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), 0.0D, 0.0D, 0.0D);
      }

      int currentGrowth = this.getGrowthLevel();
      if (currentGrowth != this.lastGrowthLevel) {
         this.lastGrowthLevel = currentGrowth;
         this.refreshDimensions();
      }
   }

   @Override
   public EntityDimensions getDimensions(Pose pose) {
      switch (this.getGrowthLevel()) {
         case 0:
            return EntityDimensions.scalable(0.5F, 0.3F);
         case 1:
            return EntityDimensions.scalable(0.7F, 0.4F);
         case 2:
            return EntityDimensions.scalable(0.9F, 0.5F);
         case 3:
            return EntityDimensions.scalable(1.1F, 0.6F);
         case 4:
            return EntityDimensions.scalable(1.3F, 0.7F);
         case 5:
            return EntityDimensions.scalable(1.5F, 0.8F);
         default:
            return super.getDimensions(pose);
      }
   }

   public void setGrowthLevel(int saplings) {
      this.entityData.set(GROWTH_LEVEL, saplings);
   }

   public int getGrowthLevel() {
      return this.entityData.get(GROWTH_LEVEL);
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      boolean flag = itemstack.getItem() == Items.SPIDER_EYE;
      if (!flag) {
         if (!itemstack.isEmpty() && itemstack.getItem() instanceof DyeItem) {
            DyeColor enumdyecolor = ((DyeItem)itemstack.getItem()).getDyeColor();
            if (this.getEyeColor() != enumdyecolor) {
               this.setEyeColor(enumdyecolor);
               itemstack.shrink(1);
            }
         }

         return super.mobInteract(player, hand);
      } else {
         this.consumeItemFromStack(player, itemstack);
         this.setGrowthLevel(this.getGrowthLevel() + 1);

         for(int i = 0; i < 8; ++i) {
            this.level().addParticle(ParticleTypes.SMOKE, this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() + (double)this.random.nextFloat() + 1.0D, this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), 0.0D, 0.0D, 0.0D);
         }

         if (this.getGrowthLevel() == 5) {
            this.setEyeColor(getRandomEyeColor(this.level().random));
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3D + (double)this.random.nextFloat() * 0.1D);
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0D);
            this.setHealth(30.0F);
         }

         return InteractionResult.SUCCESS;
      }
   }

   protected void consumeItemFromStack(Player player, ItemStack stack) {
      if (!player.getAbilities().instabuild) {
         stack.shrink(1);
      }

   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("Growth", this.getGrowthLevel());
      compound.putBoolean("Jumping", this.getIsJumping());
      compound.putByte("Color", (byte)this.getEyeColor().getId());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setGrowthLevel(compound.getInt("Growth"));
      this.setIsJumping(compound.getBoolean("Jumping"));
      this.setEyeColor(DyeColor.byId(compound.getByte("Color")));
   }

   public DyeColor getEyeColor() {
      return DyeColor.byId(this.entityData.get(DYE_COLOR) & 15);
   }

   public void setEyeColor(DyeColor color) {
      byte b0 = this.entityData.get(DYE_COLOR);
      this.entityData.set(DYE_COLOR, (byte)(b0 & 240 | color.getId() & 15));
   }

   @Override
   public void travel(Vec3 travelVector) {
      if (this.jumpMountTicks > 0) {
         --this.jumpMountTicks;
      }

      if (this.isVehicle() && this.getControllingPassenger() != null) {
         LivingEntity entitylivingbase = this.getControllingPassenger();
         this.setYRot(entitylivingbase.getYRot());
         this.yRotO = this.getYRot();
         this.setXRot(entitylivingbase.getXRot() * 0.5F);
         this.setRot(this.getYRot(), this.getXRot());
         this.yHeadRot = this.getYRot();
         this.yBodyRot = this.yHeadRot;
         float strafe = entitylivingbase.xxa * 0.5F;
         float forward = entitylivingbase.zza;
         if (!this.level().isClientSide) {
            this.setBesideClimbableBlock(this.horizontalCollision);
         }

         if (forward <= 0.0F) {
            forward *= 0.5F;
         }

         if (this.isInWater() && !this.isBesideClimbableBlock()) {
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.x, 0.02D, motion.z);
         }

         this.setSpeed(this.getSpeed() * 0.5F);
         if (this.isControlledByLocalInstance()) {
            this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) / 1.5F);
            super.travel(new Vec3(strafe, travelVector.y, forward));
         }

         this.calculateEntityAnimation(false);
      } else {
         super.travel(travelVector);
      }

   }

   @Override
   protected float getJumpPower() {
      float jumpBoost = this.hasEffect(MobEffects.JUMP) ? 0.1F * ((float)this.getEffect(MobEffects.JUMP).getAmplifier() + 1.0F) : 0.0F;
      return 0.62F * this.getBlockJumpFactor() + jumpBoost;
   }

   public void setIsJumping(boolean b) {
      this.entityData.set(IS_JUMPING, b);
   }

   public boolean getIsJumping() {
      return this.entityData.get(IS_JUMPING);
   }

   public static DyeColor getRandomEyeColor(RandomSource random) {
      int i = random.nextInt(DyeColor.values().length);
      return DyeColor.byId(i);
   }

   static class AIBabyFollowOwner extends Goal {
      private final EntityBabySpider spider;
      private LivingEntity owner;
      private final double followSpeed;
      private final float minDist;
      private final float maxDist;
      private int timeToRecalcPath;
      private float oldWaterCost;

      public AIBabyFollowOwner(EntityBabySpider spider, double followSpeed, float maxDist, float minDist) {
         this.spider = spider;
         this.followSpeed = followSpeed;
         this.maxDist = maxDist;
         this.minDist = minDist;
         this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
      }

      @Override
      public boolean canUse() {
         LivingEntity owner = this.spider.getOwner();
         if (owner == null || !owner.isAlive()) {
            return false;
         }
         if (this.spider.isPassenger()) {
            return false;
         }
         if (this.spider.distanceToSqr(owner) < (double)(this.minDist * this.minDist)) {
            return false;
         }
         this.owner = owner;
         return true;
      }

      @Override
      public boolean canContinueToUse() {
         return !this.spider.getNavigation().isDone() && this.owner != null && this.owner.isAlive() && this.spider.distanceToSqr(this.owner) > (double)(this.maxDist * this.maxDist);
      }

      @Override
      public void start() {
         this.timeToRecalcPath = 0;
         this.oldWaterCost = this.spider.getPathfindingMalus(net.minecraft.world.level.pathfinder.BlockPathTypes.WATER);
         this.spider.setPathfindingMalus(net.minecraft.world.level.pathfinder.BlockPathTypes.WATER, 0.0F);
      }

      @Override
      public void stop() {
         this.owner = null;
         this.spider.getNavigation().stop();
         this.spider.setPathfindingMalus(net.minecraft.world.level.pathfinder.BlockPathTypes.WATER, this.oldWaterCost);
      }

      @Override
      public void tick() {
         this.spider.getLookControl().setLookAt(this.owner, 10.0F, (float)this.spider.getMaxHeadYRot());
         if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.spider.getNavigation().moveTo(this.owner, this.followSpeed) && !this.spider.isLeashed() && !this.spider.isPassenger() && this.spider.distanceToSqr(this.owner) >= 144.0D) {
               int i = net.minecraft.util.Mth.floor(this.owner.getX()) - 2;
               int j = net.minecraft.util.Mth.floor(this.owner.getZ()) - 2;
               int k = net.minecraft.util.Mth.floor(this.owner.getBoundingBox().minY);
               for (int l = 0; l <= 4; ++l) {
                  for (int i1 = 0; i1 <= 4; ++i1) {
                     if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.canTeleportTo(i, j, k, l, i1)) {
                        this.spider.moveTo((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.spider.getYRot(), this.spider.getXRot());
                        this.spider.getNavigation().stop();
                        return;
                     }
                  }
               }
            }
         }
      }

      private boolean canTeleportTo(int x, int z, int y, int xOff, int zOff) {
         BlockPos blockpos = new BlockPos(x + xOff, y - 1, z + zOff);
         return this.spider.level().getBlockState(blockpos).entityCanStandOnFace(this.spider.level(), blockpos, this.spider, Direction.UP) && this.spider.level().isEmptyBlock(blockpos.above()) && this.spider.level().isEmptyBlock(blockpos.above(2));
      }
   }
}

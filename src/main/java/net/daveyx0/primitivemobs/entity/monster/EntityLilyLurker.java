package net.daveyx0.primitivemobs.entity.monster;

import java.util.List;
import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.EntityMMSwimmingCreature;
import net.daveyx0.multimob.entity.IMultiMobWater;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityLilyLurker extends EntityMMSwimmingCreature implements IMultiMobWater, Enemy {
   private static final EntityDataAccessor<Boolean> IS_CAMOUFLAGED = SynchedEntityData.defineId(EntityLilyLurker.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDimensions DISGUISED_SIZE = EntityDimensions.scalable(0.5F, 0.98F);
   private static final EntityDimensions EXPOSED_SIZE = EntityDimensions.scalable(0.5F, 0.5F);
   private static final Vec3 RISE = new Vec3(0.0D, 0.05D, 0.0D);
   private int calmTicks;
   private int landTicks;
   private int ambushCooldown;

   public EntityLilyLurker(EntityType<? extends EntityLilyLurker> type, Level worldIn) {
      super(type, worldIn);
      this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.LILY_PAD));
      this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false) {
         @Override
         public boolean canUse() {
            return !EntityLilyLurker.this.isCamouflaged() && super.canUse();
         }
      });
      this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1.0D, 10) {
         @Override
         public boolean canUse() {
            return !EntityLilyLurker.this.isCamouflaged() && super.canUse();
         }
      });
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false) {
         @Override
         public boolean canUse() {
            return !EntityLilyLurker.this.isCamouflaged() && super.canUse();
         }
      });
      this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Squid.class, false, false) {
         @Override
         public boolean canUse() {
            return !EntityLilyLurker.this.isCamouflaged() && super.canUse();
         }
      });
   }

   public static AttributeSupplier.Builder createAttributes() {
      return EntityMMSwimmingCreature.createAttributes()
         .add(Attributes.MAX_HEALTH, 20.0D)
         .add(Attributes.ATTACK_DAMAGE, 4.0D)
         .add(Attributes.MOVEMENT_SPEED, 1.0D)
         .add(Attributes.FOLLOW_RANGE, 30.0D);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(IS_CAMOUFLAGED, true);
   }

   @Override
   protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
      return dimensions.height * 0.25F;
   }

   @Override
   public EntityDimensions getDimensions(Pose pose) {
      return this.isCamouflaged() ? DISGUISED_SIZE : EXPOSED_SIZE;
   }

   @Override
   public boolean canBreatheUnderwater() {
      return true;
   }

   @Override
   public boolean isPushedByFluid() {
      return !this.isCamouflaged() && super.isPushedByFluid();
   }

   @Override
   protected void playStepSound(BlockPos pos, net.minecraft.world.level.block.state.BlockState blockIn) {
   }

   @Override
   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return true;
   }

   @Override
   public void tick() {
      if (this.isCamouflaged()) {
         this.tickDisguised();
      } else {
         this.tickExposed();
      }
      super.tick();
   }

   private void tickDisguised() {
      if (!this.isInWater()) {
         this.setCamouflaged(false);
         return;
      }
      this.setNoGravity(true);
      this.setYRot(0.25F);
      this.yHeadRot = 0.25F;
      this.yBodyRot = 0.25F;
      this.getNavigation().stop();
      this.calmTicks = 0;
      if (!this.level().isClientSide) {
         if (!this.level().getFluidState(this.blockPosition().above()).isEmpty()) {
            this.move(MoverType.SELF, RISE);
         }
         if (--this.ambushCooldown <= 0) {
            this.ambushCooldown = 5;
            AABB range = this.getBoundingBox().inflate(1.0D);
            List<LivingEntity> nearby = this.level().getEntitiesOfClass(LivingEntity.class, range, (entity) -> {
               if (entity == this) {
                  return false;
               }
               if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
                  return false;
               }
               return true;
            });
            if (!nearby.isEmpty()) {
               this.setTarget(nearby.get(0));
               this.setCamouflaged(false);
            }
         }
      }
   }

   private void tickExposed() {
      this.setNoGravity(false);
      if (!this.level().isClientSide) {
         LivingEntity target = this.getTarget();
         if (this.isInWater()) {
            this.landTicks = 0;
            if ((target == null || !target.isAlive()) && ++this.calmTicks > 250) {
               this.calmTicks = 0;
               this.setCamouflaged(true);
            }
         } else if (++this.landTicks > 100) {
            this.hurt(this.damageSources().drown(), 3.0F);
            this.jumpFromGround();
            this.setDeltaMovement((this.random.nextFloat() - this.random.nextFloat()) / 2.0F, this.getDeltaMovement().y, (this.random.nextFloat() - this.random.nextFloat()) / 2.0F);
            this.landTicks = 80;
         }
      }
   }

   @Override
   protected void jumpInLiquid(net.minecraft.tags.TagKey<net.minecraft.world.level.material.Fluid> fluidTag) {
      if (!this.isCamouflaged()) {
         super.jumpInLiquid(fluidTag);
      }
   }

   @Override
   public void push(Entity entityIn) {
      if (entityIn.getBoundingBox().minY <= this.getBoundingBox().minY) {
         super.push(entityIn);
      }
   }

   @Override
   public boolean isPushable() {
      return !this.isCamouflaged();
   }

   @Override
   protected boolean isImmobile() {
      return this.isCamouflaged() || super.isImmobile();
   }

   @Override
   public boolean isInWall() {
      return this.isCamouflaged() ? false : super.isInWall();
   }

   @Override
   public boolean doHurtTarget(Entity entity) {
      this.setCamouflaged(false);
      return super.doHurtTarget(entity);
   }

   public ItemEntity dropItemStack(ItemStack itemIn, float offsetY) {
      return this.spawnAtLocation(itemIn, offsetY);
   }

   @Nullable
   @Override
   protected ResourceLocation getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_LILYLURKER;
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      this.setCamouflaged(false);
      return source.is(DamageTypes.DROWN) && this.isInWater() ? false : super.hurt(source, amount);
   }

   public boolean isCamouflaged() {
      return this.entityData.get(IS_CAMOUFLAGED);
   }

   public void setCamouflaged(boolean camouflaged) {
      if (this.isCamouflaged() != camouflaged) {
         this.entityData.set(IS_CAMOUFLAGED, camouflaged);
         this.refreshDimensions();
      }
   }

   @Override
   public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
      if (IS_CAMOUFLAGED.equals(key)) {
         this.refreshDimensions();
      }
      super.onSyncedDataUpdated(key);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("Camouflaged", this.isCamouflaged());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setCamouflaged(compound.getBoolean("Camouflaged"));
   }

   @Override
   public boolean checkSpawnObstruction(LevelReader level) {
      return level.isUnobstructed(this);
   }

   @Override
   public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
      return this.getY() > 45.0D && this.getY() < (double)this.level().getSeaLevel() && this.level().getFluidState(this.blockPosition()).is(FluidTags.WATER);
   }
}

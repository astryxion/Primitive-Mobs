package net.daveyx0.primitivemobs.entity.monster;

import java.util.List;
import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.EntityMMSwimmingCreature;
import net.daveyx0.multimob.entity.IMultiMobWater;
import net.daveyx0.multimob.entity.ai.EntityAISwimmingUnderwater;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityLilyLurker extends EntityMMSwimmingCreature implements IMultiMobWater, Enemy {
   int aggroTimer;
   int timeOnLand;
   private static final EntityDataAccessor<Boolean> IS_CAMOUFLAGED = SynchedEntityData.defineId(EntityLilyLurker.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> TIME_REGROW = SynchedEntityData.defineId(EntityLilyLurker.class, EntityDataSerializers.INT);

   public EntityLilyLurker(EntityType<? extends EntityLilyLurker> type, Level worldIn) {
      super(type, worldIn);
      this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Blocks.LILY_PAD));
      this.setCamouflaged(true);
      this.aggroTimer = 0;
      this.timeOnLand = 0;
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(1, new MeleeAttackGoal(this, (double)1.0F, true));
      this.goalSelector.addGoal(2, new EntityAISwimmingUnderwater(this));
      this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
      int attackPrio = 1;
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new HurtByTargetGoal(this));
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new NearestAttackableTargetGoal<>(this, Squid.class, false, false));
   }

   @Override
   protected float getStandingEyeHeight(net.minecraft.world.entity.Pose pose, net.minecraft.world.entity.EntityDimensions size) {
      return this.getBbHeight() * 0.25F;
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
      return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
   }

   public static AttributeSupplier.Builder createAttributes() {
      return EntityMMSwimmingCreature.createAttributes()
         .add(Attributes.ATTACK_DAMAGE, (double)4.0F)
         .add(Attributes.MOVEMENT_SPEED, (double)1.0F)
         .add(Attributes.FOLLOW_RANGE, (double)30.0F);
   }

   @Override
   protected void defineSynchedData() {
      this.entityData.define(IS_CAMOUFLAGED, false);
      this.entityData.define(TIME_REGROW, 0);
      super.defineSynchedData();
   }

   @Override
   public boolean isPushable() {
      return true;
   }

   @Override
   protected void playStepSound(BlockPos pos, net.minecraft.world.level.block.state.BlockState blockIn) {
   }

   @Override
   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return true;
   }

   @Nullable
   public AABB getCollideAgainstBox(Entity entityIn) {
      return this.isCamouflaged() ? entityIn.getBoundingBox() : null;
   }

   @Nullable
   public AABB getCollisionBoundingBox() {
      return this.isCamouflaged() ? this.getBoundingBox() : null;
   }

   @Override
   public void tick() {
      if (this.isCamouflaged()) {
         if (!this.isInWater()) {
            this.setCamouflaged(false);
         }

         this.resetFallDistance();
         this.refreshDimensions();
         this.setYRot(0.25F);
         this.yBodyRot = 0.25F;
         this.setNoGravity(true);
         this.getNavigation().moveTo((net.minecraft.world.level.pathfinder.Path)null, (double)0.0F);
         if (!this.level().isClientSide && EntityUtil.distanceToSurface(this, this.level()) > 1.5F) {
            this.move(MoverType.SELF, new Vec3((double)0.0F, 0.05, (double)0.0F));
         }

         List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate((double)1.0F, (double)1.0F, (double)1.0F));
         LivingEntity base = null;
         if (this.getTarget() != null && this.getTarget().isAlive()) {
            this.setCamouflaged(false);
         } else {
            for(Entity entity : list) {
               if (entity != null && entity instanceof LivingEntity) {
                  if (entity instanceof Player) {
                     Player player = (Player)entity;
                     if (player.isCreative()) {
                        continue;
                     }
                  }

                  base = (LivingEntity)entity;
                  this.setTarget(base);
               }
            }
         }

         this.aggroTimer = 0;
      } else {
         this.setNoGravity(false);
         this.refreshDimensions();
         if (this.isInWater() && (this.getTarget() == null || !this.getTarget().isAlive()) && ++this.aggroTimer > 250) {
            this.aggroTimer = 0;
            this.setCamouflaged(true);
         }

         if (!this.isInWater()) {
            if (++this.timeOnLand > 100) {
               this.hurt(this.damageSources().dryOut(), 3.0F);
               this.jumpFromGround();
               Vec3 delta = this.getDeltaMovement();
               this.setDeltaMovement((double)((this.getRandom().nextFloat() - this.getRandom().nextFloat()) / 2.0F), delta.y, (double)((this.getRandom().nextFloat() - this.getRandom().nextFloat()) / 2.0F));
               this.timeOnLand = 80;
            }
         } else {
            this.timeOnLand = 0;
         }
      }

      super.tick();
   }

   @Override
   public void push(Entity entityIn) {
      if (entityIn instanceof Boat) {
         if (entityIn.getBoundingBox().minY < this.getBoundingBox().maxY) {
            super.push(entityIn);
         }
      } else if (entityIn.getBoundingBox().minY <= this.getBoundingBox().minY) {
         super.push(entityIn);
      }

   }

   @Override
   protected boolean isImmobile() {
      return this.isCamouflaged() || super.isImmobile();
   }

   @Override
   public boolean isInWall() {
      return !this.isCamouflaged() ? super.isInWall() : false;
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

   public void setCamouflaged(boolean camouflaged) {
      this.entityData.set(IS_CAMOUFLAGED, camouflaged);
   }

   public boolean isCamouflaged() {
      return (Boolean)this.entityData.get(IS_CAMOUFLAGED);
   }

   public void setTimeToRegrow(int time) {
      this.entityData.set(TIME_REGROW, time);
   }

   public int getTimeToRegrow() {
      return (Integer)this.entityData.get(TIME_REGROW);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("Camouflaged", this.isCamouflaged());
      compound.putInt("RegrowTime", this.getTimeToRegrow());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setCamouflaged(compound.getBoolean("Camouflaged"));
      this.setTimeToRegrow(compound.getInt("RegrowTime"));
   }

   @Override
   public boolean checkSpawnObstruction(net.minecraft.world.level.LevelReader level) {
      return level.isUnobstructed(this);
   }

   @Override
   public boolean checkSpawnRules(net.minecraft.world.level.LevelAccessor level, MobSpawnType spawnType) {
      return this.getY() > (double)45.0F && this.getY() < (double)this.level().getSeaLevel();
   }
}

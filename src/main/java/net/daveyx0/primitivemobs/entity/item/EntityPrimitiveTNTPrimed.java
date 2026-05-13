package net.daveyx0.primitivemobs.entity.item;

import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityPrimitiveTNTPrimed extends Entity {
   private static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(EntityPrimitiveTNTPrimed.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Float> STRENGTH = SynchedEntityData.defineId(EntityPrimitiveTNTPrimed.class, EntityDataSerializers.FLOAT);
   private LivingEntity tntPlacedBy;
   private int fuse;
   private float strength;

   public EntityPrimitiveTNTPrimed(EntityType<? extends EntityPrimitiveTNTPrimed> type, Level worldIn) {
      super(type, worldIn);
      this.blocksBuilding = true;
      this.fuse = 80;
      this.strength = 1.5F;
   }

   public EntityPrimitiveTNTPrimed(EntityType<? extends EntityPrimitiveTNTPrimed> type, Level worldIn, double x, double y, double z, LivingEntity igniter, float power, int fuse) {
      this(type, worldIn);
      this.setPos(x, y, z);
      float f = (float)(Math.random() * (Math.PI * 2D));
      this.setDeltaMovement((double)(-((float)Math.sin((double)f)) * 0.02F), (double)0.2F, (double)(-((float)Math.cos((double)f)) * 0.02F));
      this.setFuse(fuse);
      this.setStrength(power);
      this.xo = x;
      this.yo = y;
      this.zo = z;
      this.tntPlacedBy = igniter;
   }

   @Override
   protected void defineSynchedData() {
      this.entityData.define(FUSE, 80);
      this.entityData.define(STRENGTH, 1.5F);
   }

   protected boolean isMovementNoisy() {
      return false;
   }

   @Override
   public boolean isPickable() {
      return !this.isRemoved();
   }

   @Override
   public void tick() {
      this.xo = this.getX();
      this.yo = this.getY();
      this.zo = this.getZ();
      if (!this.isNoGravity()) {
         this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double)-0.04F, 0.0D));
      }

      this.move(MoverType.SELF, this.getDeltaMovement());
      this.setDeltaMovement(this.getDeltaMovement().multiply((double)0.98F, (double)0.98F, (double)0.98F));
      if (this.onGround()) {
         this.setDeltaMovement(this.getDeltaMovement().multiply((double)0.7F, (double)-0.5F, (double)0.7F));
      }

      --this.fuse;
      if (this.fuse <= 0) {
         this.discard();
         if (!this.level().isClientSide) {
            this.explode();
         }
      } else {
         this.updateInWaterStateAndDoFluidPushing();
         this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + (double)0.5F, this.getZ(), (double)0.0F, (double)0.0F, (double)0.0F);
      }

   }

   private void explode() {
      boolean flag = true;
      if (!PrimitiveMobsConfigSpecial.getFestiveCreeperDestruction()) {
         flag = false;
      } else {
         flag = this.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING);
      }

      this.level().explode(this, this.getX(), this.getY() + (double)(this.getBbHeight() / 16.0F), this.getZ(), this.strength, flag ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag compound) {
      compound.putShort("Fuse", (short)this.getFuse());
      compound.putFloat("Strength", (float)((short)((int)this.getStrength())));
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag compound) {
      this.setFuse(compound.getShort("Fuse"));
      this.setStrength(compound.getFloat("Strength"));
   }

   public LivingEntity getTntPlacedBy() {
      return this.tntPlacedBy;
   }

   @Override
   public float getEyeHeight(net.minecraft.world.entity.Pose pose) {
      return 0.0F;
   }

   public void setFuse(int fuseIn) {
      this.entityData.set(FUSE, fuseIn);
      this.fuse = fuseIn;
   }

   public void setStrength(float strengthIn) {
      this.entityData.set(STRENGTH, strengthIn);
      this.strength = strengthIn;
   }

   @Override
   public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
      if (FUSE.equals(key)) {
         this.fuse = this.getFuseDataManager();
      }

      if (STRENGTH.equals(key)) {
         this.strength = this.getStrengthDataManager();
      }

   }

   public int getFuseDataManager() {
      return (Integer)this.entityData.get(FUSE);
   }

   public float getStrengthDataManager() {
      return (Float)this.entityData.get(STRENGTH);
   }

   public int getFuse() {
      return this.fuse;
   }

   public float getStrength() {
      return this.strength;
   }
}

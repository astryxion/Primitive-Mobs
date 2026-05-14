package net.daveyx0.primitivemobs.entity.item;

import com.google.common.collect.Lists;
import net.daveyx0.multimob.util.NBTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EntityThrownBlock extends Entity {
   private LivingEntity owner;
   private BlockState fallTile;
   public CompoundTag tileEntityData;
   protected static final EntityDataAccessor<BlockPos> ORIGIN = SynchedEntityData.defineId(EntityThrownBlock.class, EntityDataSerializers.BLOCK_POS);

   public EntityThrownBlock(EntityType<? extends EntityThrownBlock> type, Level worldIn) {
      super(type, worldIn);
      this.blocksBuilding = true;
   }

   public EntityThrownBlock(EntityType<? extends EntityThrownBlock> type, Level worldIn, double x, double y, double z, LivingEntity igniter, BlockPos blockPos) {
      this(type, worldIn);
      this.setPos(x, y, z);
      float f = (float)(Math.random() * (Math.PI * 2D));
      this.setDeltaMovement((double)(-((float)Math.sin((double)f)) * 0.02F), (double)0.2F, (double)(-((float)Math.cos((double)f)) * 0.02F));
      this.xo = x;
      this.yo = y;
      this.zo = z;
      this.owner = igniter;
      this.setOrigin(blockPos);
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      builder.define(ORIGIN, BlockPos.ZERO);
   }

   public void setOrigin(BlockPos p_184530_1_) {
      this.entityData.set(ORIGIN, p_184530_1_);
   }

   public BlockPos getOrigin() {
      return (BlockPos)this.entityData.get(ORIGIN);
   }

   @Override
   public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
      for(Entity entity : Lists.newArrayList(this.level().getEntities(this, this.getBoundingBox().inflate(1.2, 1.2, 1.2)))) {
         if (this.owner == null) {
            entity.hurt(this.damageSources().thrown(this, this), 10.0F);
         } else {
            entity.hurt(this.damageSources().thrown(this, this.owner), 10.0F);
         }
      }

      return false;
   }

   protected boolean isMovementNoisy() {
      return false;
   }

   @Override
   public boolean isPickable() {
      return !this.isRemoved();
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag compound) {
      NBTUtil.setBlockPosToNBT(this.getOrigin(), "BlockPos", compound);
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag compound) {
      this.setOrigin(NBTUtil.getBlockPosFromNBT("BlockPos", compound));
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

         for(int i = 0; i < 36; ++i) {
            BlockState originState = this.level().getBlockState(this.getOrigin());
            this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, originState), this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() + (double)0.5F, this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), (double)(this.random.nextFloat() - this.random.nextFloat()), (double)1.0F, (double)(this.random.nextFloat() - this.random.nextFloat()));
         }

         this.discard();
      }

   }

   private void explode() {
      boolean flag = true;
   }

   public LivingEntity getOwner() {
      return this.owner;
   }

   @Override
   public boolean isAttackable() {
      return false;
   }

   public Level getWorldObj() {
      return this.level();
   }
}

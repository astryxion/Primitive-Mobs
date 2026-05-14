package net.daveyx0.primitivemobs.entity.monster;

import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.multimob.entity.ai.EntityAISenseEntityNearestPlayer;
import net.daveyx0.multimob.message.MMMessageRegistry;
import net.daveyx0.multimob.message.MessageMMParticle;
import net.daveyx0.multimob.util.NBTUtil;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.daveyx0.primitivemobs.core.PrimitiveMobsEntityRegistry;
import net.daveyx0.primitivemobs.core.PrimitiveMobsSoundEvents;
import net.daveyx0.primitivemobs.entity.IAnimatedMob;
import net.daveyx0.primitivemobs.entity.ai.EntityAITrollagerAttacks;
import net.daveyx0.primitivemobs.entity.item.EntityThrownBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;

public class EntityTrollager extends Monster implements IAnimatedMob, IMultiMob {
   private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(EntityTrollager.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<BlockPos> CURRENT_THROWN_BLOCK = SynchedEntityData.defineId(EntityTrollager.class, EntityDataSerializers.BLOCK_POS);
   private static final EntityDataAccessor<Boolean> IS_STONE = SynchedEntityData.defineId(EntityTrollager.class, EntityDataSerializers.BOOLEAN);
   private int previousState = 0;
   private float animVar = 0.0F;
   private float previousYawStone = -2.0F;
   private float previousPitchStone = -2.0F;
   private float previousYawHeadStone = -2.0F;
   public boolean isBeingSupported;

   public EntityTrollager(EntityType<? extends EntityTrollager> type, Level worldIn) {
      super(type, worldIn);
      this.isBeingSupported = false;
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
         .add(Attributes.ATTACK_DAMAGE, (double)8.0F)
         .add(Attributes.MAX_HEALTH, (double)50.0F)
         .add(Attributes.KNOCKBACK_RESISTANCE, (double)1.0F);
   }

   @Override
   protected void registerGoals() {
      int prio = 0;
      ++prio;
      this.goalSelector.addGoal(prio, new FloatGoal(this));
      ++prio;
      this.goalSelector.addGoal(prio, new MoveTowardsRestrictionGoal(this, (double)1.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new EntityAITrollagerAttacks(this, (double)1.25F, 2.5F, 20.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new RandomStrollGoal(this, (double)1.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new LookAtPlayerGoal(this, Player.class, 8.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new RandomLookAroundGoal(this));
      int attackPrio = 1;
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new HurtByTargetGoal(this));
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new EntityAISenseEntityNearestPlayer(this, 40));
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata) {
      livingdata = super.finalizeSpawn(level, difficulty, reason, livingdata);
      if (this.level().random.nextInt(20) == 0) {
         EntityGoblin goblin = new EntityGoblin(PrimitiveMobsEntityRegistry.GOBLIN.get(), this.level());
         goblin.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
         goblin.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null);
         this.level().addFreshEntity(goblin);
         goblin.startRiding(this);
      }

      return livingdata;
   }

   @Override
   protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
      return new Vec3(0.0, (double)(dimensions.height() * 0.9F), 0.0);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.isVehicle() && !this.level().isClientSide) {
         if (this.getPassengers().get(0) != null && this.getPassengers().get(0) instanceof LivingEntity) {
            Mob ridingEntity = (Mob)this.getPassengers().get(0);
            if (ridingEntity.getTarget() != null) {
               this.setTarget(ridingEntity.getTarget());
            }
         }

         if (this.horizontalCollision) {
            this.jumpFromGround();
         }
      }

      if (this.getTarget() != null && this.getTarget().isRemoved()) {
         this.setTarget((LivingEntity)null);
      }

      this.animationHandling();
      if (this.getAnimationState() != 1 && this.getAnimationState() != 2 && this.tickCount % 5 == 0) {
         this.setThrowingBlockFromFloor();
      }

      if (this.isBeingSupported) {
         this.setStone(false);
      }

      if (this.isStone()) {
         this.resetFallDistance();
         this.getNavigation().moveTo((Path)null, (double)0.0F);
         this.setTarget((LivingEntity)null);
         this.setNoAi(true);
         if (this.previousYawStone == -2.0F) {
            this.previousYawStone = this.getYRot();
            this.previousYawHeadStone = this.yBodyRot;
            this.previousPitchStone = this.getXRot();
         }

         this.setYRot(this.previousYawStone);
         this.setXRot(this.previousPitchStone);
         this.yHeadRot = this.previousYawHeadStone;
         if (this.isVehicle() && this.getPassengers().get(0) != null) {
            ((Entity)this.getPassengers().get(0)).stopRiding();
         }
      } else {
         this.previousPitchStone = -2.0F;
         this.previousYawHeadStone = -2.0F;
         this.previousYawStone = -2.0F;
         this.setNoAi(false);
      }

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
   public boolean skipAttackInteraction(Entity p_85031_1_) {
      if (this.isStone() && p_85031_1_ != null && p_85031_1_ instanceof LivingEntity) {
         ItemStack stack = ((LivingEntity)p_85031_1_).getItemBySlot(EquipmentSlot.MAINHAND);
         return stack == null || !stack.is(Blocks.STONE.asItem());
      } else {
         return false;
      }
   }

   public ItemEntity dropItemStack(ItemStack itemIn, float offsetY) {
      return this.spawnAtLocation(itemIn, offsetY);
   }

   @Override
   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean wasRecentlyHit) {
      if (!this.isStone() && !this.level().isClientSide) {
         int lootingModifier = source.getEntity() instanceof LivingEntity livingentity ? EnchantmentHelper.getEnchantmentLevel(livingentity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), livingentity) : 0;
         int i = 1 + this.random.nextInt(2);
         if (lootingModifier > 0) {
            i += this.random.nextInt(lootingModifier + 1);
         }

         for(int j = 0; j < i; ++j) {
            ItemStack newStack = this.getRandomLoot();
            if (newStack != null) {
               this.dropItemStack(newStack, 1.0F);
            }
         }
      } else if (this.isStone() && !this.level().isClientSide) {
         this.dropItemStack(new ItemStack(Blocks.STONE, 32), 1.0F);
      }

   }

   public ItemStack getRandomLoot() {
      int chance = this.random.nextInt(100);
      if (chance > 50) {
         return new ItemStack(Blocks.COBBLESTONE);
      } else {
         return chance > 10 ? new ItemStack(Items.NAME_TAG) : new ItemStack(Items.GOLD_INGOT);
      }
   }

   @Override
   public void aiStep() {
      boolean flag = false;
      if (this.level().isDay() && !this.level().isClientSide) {
         float f = this.getLightLevelDependentMagicValue();
         if (this.level().canSeeSky(new BlockPos((int)this.getX(), (int)(this.getY() + (double)this.getEyeHeight()), (int)this.getZ()))) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
            if (!itemstack.isEmpty()) {
               this.setStone(false);
            } else {
               this.setStone(true);
            }
         } else {
            this.setStone(false);
         }
      } else if (!this.level().isDay() && !this.level().isClientSide) {
         this.setStone(false);
      }

      super.aiStep();
   }

   public void setStone(boolean b) {
      this.entityData.set(IS_STONE, b);
   }

   public boolean isStone() {
      return (Boolean)this.entityData.get(IS_STONE);
   }

   public void animationHandling() {
      if (!this.level().isClientSide || this.isStone()) {
         return;
      }

      if (this.getPreviousAnimationState() != this.getAnimationState()) {
         this.setPreviousAnimationState(this.getAnimationState());
         this.animVar = 0.0F;
      }

      if (this.animVar < 1.0F) {
         this.animVar += 0.01F;
      } else {
         this.animVar = 1.0F;
      }
   }

   public void setThrowingBlockFromFloor() {
      for(int i = 1; i < 64; ++i) {
         BlockPos blockPos = new BlockPos((int)this.getX(), (int)(this.getBoundingBox().minY - (double)i), (int)this.getZ());
         if (blockPos != null) {
            if (this.getThrownBlock().equals(blockPos)) {
               break;
            }

            BlockState state = this.level().getBlockState(blockPos);
            if (state != null && state.getBlock().defaultBlockState().canOcclude()) {
               this.setThrownBlock(blockPos);
               break;
            }
         }
      }

   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(ANIMATION_STATE, 0);
      builder.define(CURRENT_THROWN_BLOCK, BlockPos.ZERO);
      builder.define(IS_STONE, false);
   }

   @Override
   public void setAnimationState(int state) {
      this.entityData.set(ANIMATION_STATE, state);
   }

   @Override
   public int getAnimationState() {
      return (Integer)this.entityData.get(ANIMATION_STATE);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return this.isStone() ? null : PrimitiveMobsSoundEvents.ENTITY_TROLLAGER_IDLE.value();
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return this.isStone() ? SoundEvents.STONE_HIT : PrimitiveMobsSoundEvents.ENTITY_TROLLAGER_HIT.value();
   }

   @Override
   protected SoundEvent getDeathSound() {
      return PrimitiveMobsSoundEvents.ENTITY_TROLLAGER_DEATH.value();
   }

   @Override
   protected float getSoundVolume() {
      return 1.0F;
   }

   @Override
   public float getVoicePitch() {
      return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.5F;
   }

   protected SoundEvent getStepSound() {
      return SoundEvents.IRON_GOLEM_STEP;
   }

   public boolean canBlockAreaSeeEntity(Entity entityIn) {
      boolean flag = true;

      for(int i = 0; i < 4; ++i) {
         flag = this.level().clip(new net.minecraft.world.level.ClipContext(
            new Vec3(this.getX(), this.getY() + (double)this.getEyeHeight() + (double)i, this.getZ()),
            new Vec3(entityIn.getX(), entityIn.getY() + (double)entityIn.getEyeHeight(), entityIn.getZ()),
            net.minecraft.world.level.ClipContext.Block.COLLIDER,
            net.minecraft.world.level.ClipContext.Fluid.NONE,
            this)).getType() == net.minecraft.world.phys.HitResult.Type.MISS;
      }

      return flag;
   }

   @Override
   public void performAction(LivingEntity target, int id) {
      switch (id) {
         case 0:
            Vec3 look = this.getLookAngle();
            double spawnX = this.getX() + look.x * 1.25D;
            double spawnY = this.getY() + 4.0D;
            double spawnZ = this.getZ() + look.z * 1.25D;
            EntityThrownBlock thrownBlock = new EntityThrownBlock(PrimitiveMobsEntityRegistry.THROWN_BLOCK.get(), this.level(), spawnX, this.getY(), spawnZ, this, this.getThrownBlock());
            thrownBlock.moveTo(spawnX, spawnY, spawnZ, this.getYRot(), 0.0F);
            double motionX = (target.getX() - thrownBlock.getX()) / (double)18.0F;
            double motionY = (target.getY() - thrownBlock.getY()) / (double)18.0F + (double)0.5F;
            double motionZ = (target.getZ() - thrownBlock.getZ()) / (double)18.0F;
            thrownBlock.setDeltaMovement(motionX, motionY, motionZ);
            this.level().addFreshEntity(thrownBlock);
            this.playSound(PrimitiveMobsSoundEvents.ENTITY_TROLLAGER_ATTACK.value(), this.getSoundVolume(), ((this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
            break;
         case 1:
            double distanceX = this.getLookControl().getWantedX() - this.getX();
            double distanceZ = this.getLookControl().getWantedZ() - this.getZ();
            double length = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);
            if (length != (double)0.0F) {
               distanceX /= length;
               distanceZ /= length;
            }

            double addedHeight = (double)0.0F;
            if (target.getY() > this.getY()) {
               addedHeight = (double)0.5F;
            } else if (target.getY() < this.getY()) {
               addedHeight = (double)-0.5F;
            }

            double explosionX = this.getX() + distanceX * (double)2.0F;
            double explosionZ = this.getZ() + distanceZ * (double)2.0F;
            double explosionY = this.getY() + addedHeight;
            boolean griefingFlag = true;
            if (!this.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING) || !PrimitiveMobsConfigSpecial.getTrollDestruction()) {
               griefingFlag = false;
            }

            this.newExplosion(this, explosionX, this.getY() + (double)this.getEyeHeight(), explosionZ, 3.0F, false, griefingFlag);
            MMMessageRegistry.getNetwork().sendToAll(new MessageMMParticle(1, 10, (float)explosionX, (float)explosionY, (float)explosionZ, (double)1.0F, (double)0.0F, (double)0.0F, 0));
            this.playSound(PrimitiveMobsSoundEvents.ENTITY_TROLLAGER_ATTACK.value(), this.getSoundVolume(), ((this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
            break;
         case 2:
            if (this.getTarget() != null && !this.isStone()) {
               double d0 = this.getAttackReachSqr(this.getTarget()) + (double)4.0F;
               double d1 = this.distanceToSqr(this.getTarget().getX(), this.getTarget().getBoundingBox().minY, this.getTarget().getZ());
               boolean canSee = this.getSensing().hasLineOfSight(this.getTarget());
               if (d1 <= d0 && canSee) {
                  this.doHurtTarget(this.getTarget());
               }

               this.playSound(PrimitiveMobsSoundEvents.ENTITY_TROLLAGER_ATTACK.value(), this.getSoundVolume(), ((this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }
      }

   }

   protected double getAttackReachSqr(LivingEntity attackTarget) {
      return (double)(this.getBbWidth() * 2.0F * this.getBbWidth() * 2.0F + attackTarget.getBbWidth());
   }

   public Explosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking) {
      Explosion.BlockInteraction blockInteraction = isSmoking ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP;
      Explosion explosion = new Explosion(this.level(), entityIn, x, y, z, strength, isFlaming, blockInteraction);
      if (EventHooks.onExplosionStart(this.level(), explosion)) {
         return explosion;
      } else {
         explosion.explode();
         explosion.finalizeExplosion(true);
         return explosion;
      }
   }

   public void setThrownBlock(BlockPos pos) {
      this.entityData.set(CURRENT_THROWN_BLOCK, pos);
   }

   public BlockPos getThrownBlock() {
      BlockPos pos = (BlockPos)this.entityData.get(CURRENT_THROWN_BLOCK);
      return pos != null ? pos : BlockPos.ZERO;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      NBTUtil.setBlockPosToNBT(this.getThrownBlock(), "ThrownBlock", compound);
      compound.putBoolean("Stone", this.isStone());
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setThrownBlock(NBTUtil.getBlockPosFromNBT("ThrownBlock", compound));
      this.setStone(compound.getBoolean("Stone"));
   }

   @Override
   public int getPreviousAnimationState() {
      return this.previousState;
   }

   @Override
   public void setPreviousAnimationState(int state) {
      this.previousState = state;
   }

   @Override
   public float getAnimVar() {
      return this.animVar;
   }

   @Override
   public boolean checkSpawnRules(net.minecraft.world.level.LevelAccessor level, MobSpawnType spawnType) {
      boolean flag = true;
      if (this.getY() > (double)40.0F && this.level().canSeeSky(new BlockPos((int)this.getX(), (int)(this.getY() + (double)this.getEyeHeight()), (int)this.getZ()))) {
         flag = this.random.nextInt(5) == 0;
      }

      return flag && super.checkSpawnRules(level, spawnType);
   }

   @Nullable
   public AABB getCollideAgainstBox(Entity entityIn) {
      return this.isStone() ? entityIn.getBoundingBox() : super.getBoundingBox();
   }

   @Nullable
   public AABB getCollideBox() {
      return this.isStone() ? this.getBoundingBox() : null;
   }

   @Override
   public void setAnimVar(float var) {
      this.animVar = var;
   }
}

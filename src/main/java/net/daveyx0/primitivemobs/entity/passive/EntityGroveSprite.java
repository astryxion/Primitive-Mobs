package net.daveyx0.primitivemobs.entity.passive;

import com.google.common.collect.Sets;
import java.awt.Color;
import java.util.Optional;
import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMobPassive;
import net.daveyx0.multimob.message.MMMessageRegistry;
import net.daveyx0.multimob.message.MessageMMParticle;
import net.daveyx0.multimob.util.ColorUtil;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.multimob.util.NBTUtil;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.daveyx0.primitivemobs.core.PrimitiveMobs;
import net.daveyx0.primitivemobs.core.PrimitiveMobsItems;
import net.daveyx0.primitivemobs.core.PrimitiveMobsSoundEvents;
import net.daveyx0.primitivemobs.entity.ai.EntityAIGroveSpriteTempt;
import net.daveyx0.primitivemobs.item.ItemGroveSpriteSap;
import net.daveyx0.primitivemobs.message.MessagePrimitiveColorSap;
import net.daveyx0.primitivemobs.message.PrimitiveMobsMessageRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

public class EntityGroveSprite extends PathfinderMob implements IMultiMobPassive {
   private float LeavesR = 0.0F;
   private float LeavesG = 0.0F;
   private float LeavesB = 0.0F;
   private float LogR = 0.0F;
   private float LogG = 0.0F;
   private float LogB = 0.0F;
   private float LogTopR = 0.0F;
   private float LogTopG = 0.0F;
   private float LogTopB = 0.0F;
   private boolean changedColor = false;
   private static final EntityDataAccessor<Boolean> IS_CINDER = SynchedEntityData.defineId(EntityGroveSprite.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> SAPLING_AMOUNT = SynchedEntityData.defineId(EntityGroveSprite.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> LEAVES_ID = SynchedEntityData.defineId(EntityGroveSprite.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Integer> LOG_ID = SynchedEntityData.defineId(EntityGroveSprite.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<BlockPos> LEAVES_POS = SynchedEntityData.defineId(EntityGroveSprite.class, EntityDataSerializers.BLOCK_POS);
   private static final EntityDataAccessor<Boolean> IS_BEGGING = SynchedEntityData.defineId(EntityGroveSprite.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> CAN_DESPAWN = SynchedEntityData.defineId(EntityGroveSprite.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Integer> SAPLING_TIMER = SynchedEntityData.defineId(EntityGroveSprite.class, EntityDataSerializers.INT);
   protected Block spawnableBlock;

   public EntityGroveSprite(EntityType<? extends EntityGroveSprite> type, Level worldIn) {
      super(type, worldIn);
      this.spawnableBlock = Blocks.GRASS_BLOCK;
      this.handDropChances[0] = 2.0F;
      this.handDropChances[1] = 2.0F;
   }

   @Override
   protected void registerGoals() {
      int prio = 0;
      ++prio;
      this.goalSelector.addGoal(prio, new FloatGoal(this));
      ++prio;
      this.goalSelector.addGoal(prio, new MeleeAttackGoal(this, (double)1.0F, false));
      ++prio;
      this.goalSelector.addGoal(prio, new RandomStrollGoal(this, (double)1.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new LookAtPlayerGoal(this, Player.class, 8.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new MoveTowardsRestrictionGoal(this, (double)1.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new EntityAIManageSaplings(this));
      ++prio;
      this.goalSelector.addGoal(prio, new EntityAIGroveSpriteTempt(this, 1.1, false, Sets.newHashSet(new ItemStack[]{this.getMainHandItem(), new ItemStack(Items.BONE_MEAL)})));
      ++prio;
      this.goalSelector.addGoal(prio, new RandomLookAroundGoal(this));
      int attackPrio = 1;
      ++attackPrio;
      this.targetSelector.addGoal(attackPrio, new HurtByTargetGoal(this));
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
      if (!this.level().isClientSide) {
         this.determineLogAndLeaves();
         BlockState leavesState = this.getLeaves();
         ItemStack sapling = saplingFromLeaves(leavesState);
         ItemStack sap = new ItemStack(PrimitiveMobsItems.WONDER_SAP.get(), 1);
         if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && !sapling.isEmpty()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, sapling);
         }

         this.setItemSlot(EquipmentSlot.OFFHAND, sap);
         this.setSaplingAmount(1 + this.random.nextInt(4));
         this.setSaplingTimer(this.random.nextInt(1000) + 1000);
      }

      return super.finalizeSpawn(levelAccessor, difficulty, spawnType, livingdata, tag);
   }

   private static ItemStack saplingFromLeaves(BlockState leavesState) {
      ResourceLocation blockId = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(leavesState.getBlock());
      if (blockId != null) {
         String path = blockId.getPath();
         if (path.endsWith("_leaves")) {
            ResourceLocation saplingId = new ResourceLocation(blockId.getNamespace(), path.substring(0, path.length() - 7) + "_sapling");
            net.minecraft.world.item.Item item = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(saplingId);
            if (item != null && item != net.minecraft.world.item.Items.AIR) {
               return new ItemStack(item);
            }
         }
      }
      return ItemStack.EMPTY;
   }

   private void determineLogAndLeaves() {
      Object[] tree = null;
      if (PrimitiveMobs.instance != null) {
         // DynamicTrees integration check removed - proxy pattern no longer used in 1.20.1
      }

      if (tree == null || tree.length == 0) {
         tree = EntityUtil.searchTree(this, (double)10.0F);
      }

      if (tree != null && tree.length > 0) {
         this.setLog((BlockState)tree[0]);
         this.setLeaves((BlockState)tree[1]);
         this.setLeavesPos((BlockPos)tree[2]);
      }

   }

   public static AttributeSupplier.Builder createAttributes() {
      return PathfinderMob.createMobAttributes()
         .add(Attributes.FOLLOW_RANGE, (double)40.0F)
         .add(Attributes.MOVEMENT_SPEED, 0.23000000298023224)
         .add(Attributes.ATTACK_DAMAGE, (double)3.0F);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(IS_CINDER, false);
      this.entityData.define(SAPLING_AMOUNT, 0);
      this.entityData.define(LEAVES_ID, Block.getId(Blocks.OAK_LEAVES.defaultBlockState()));
      this.entityData.define(LOG_ID, Block.getId(Blocks.OAK_LOG.defaultBlockState()));
      this.entityData.define(LEAVES_POS, new BlockPos(0, 0, 0));
      this.entityData.define(CAN_DESPAWN, true);
      this.entityData.define(IS_BEGGING, false);
      this.entityData.define(SAPLING_TIMER, 0);
   }

   @Override
   public void tick() {
      if (this.isOnFire()) {
         this.setCinderSprite(true);
         this.changedColor = false;
      }

      if (this.isCinderSprite()) {
         this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY() + (double)this.getBbHeight() + (double)0.2F, this.getZ(), (double)0.0F, (double)0.0F, (double)0.0F);
      } else {
         this.setSaplingTimer(this.getSaplingTimer() - 1);
         if (this.getSaplingTimer() <= 0) {
            this.setSaplingAmount(this.getSaplingAmount() + 1);

            for(int i = 0; i < 8; ++i) {
               this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() + (double)this.random.nextFloat() + (double)1.0F, this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), (double)0.0F, (double)0.0F, (double)0.0F);
            }

            this.setSaplingTimer(this.random.nextInt(1000) + 1000);
         }
      }

      if (this.level().isClientSide && !this.changedColor) {
         this.changedColor = true;
         if (!this.isCinderSprite()) {
            this.setLeavesRGB(this.getColor(this.level(), this.getLeaves(), this.getLeavesPos(), (Direction)null));
            this.setLogRGB(this.getColor(this.level(), this.getLog(), BlockPos.ZERO, Direction.WEST));
            this.setLogTopRGB(this.getColor(this.level(), this.getLog(), BlockPos.ZERO, (Direction)null));
            ItemStack clientSap = this.getOffhandItem();
            if (this.getLog() != null) {
               int[] logTop = this.getColor(this.level(), this.getLog(), BlockPos.ZERO, (Direction)null);
               Color logTopColor = new Color(logTop[0], logTop[1], logTop[2]);
               ItemGroveSpriteSap.setColor(clientSap, logTopColor.hashCode());
               PrimitiveMobsMessageRegistry.getPrimitiveNetwork().sendToServer(new MessagePrimitiveColorSap(ItemGroveSpriteSap.getColor(clientSap), this.getUUID().toString()));
            }
         } else {
            this.setLeavesRGB(new int[]{177, 100, 0});
            this.setLogRGB(new int[]{90, 86, 80});
            this.setLogTopRGB(new int[]{102, 98, 94});
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Blocks.DEAD_BUSH));
            this.noPhysics = true;
         }
      }

      super.tick();
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if ((source == this.damageSources().onFire() || source == this.damageSources().inFire() || source == this.damageSources().lava()) && this.isCinderSprite()) {
         return false;
      }
      return super.hurt(source, amount);
   }

   @Override
   public boolean doHurtTarget(Entity entityIn) {
      float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
      int i = 0;
      if (entityIn instanceof LivingEntity) {
         f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)entityIn).getMobType());
         i += EnchantmentHelper.getKnockbackBonus(this);
      }

      boolean flag = entityIn.hurt(this.damageSources().mobAttack(this), f);
      if (flag) {
         if (i > 0 && entityIn instanceof LivingEntity) {
            ((LivingEntity)entityIn).knockback((float)i * 0.5F, (double)Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))));
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.x * 0.6, motion.y, motion.z * 0.6);
         }

         if (this.isCinderSprite()) {
            entityIn.setSecondsOnFire(8);
         }

         if (entityIn instanceof Player) {
            Player entityplayer = (Player)entityIn;
            ItemStack itemstack = this.getMainHandItem();
            ItemStack itemstack1 = entityplayer.isUsingItem() ? entityplayer.getUseItem() : ItemStack.EMPTY;
            if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().canPerformAction(itemstack1, net.minecraftforge.common.ToolActions.SHIELD_BLOCK)) {
               float f1 = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
               if (this.random.nextFloat() < f1) {
                  entityplayer.getCooldowns().addCooldown(itemstack1.getItem(), 100);
                  this.level().broadcastEntityEvent(entityplayer, (byte)30);
               }
            }
         }

         this.doEnchantDamageEffects(this, entityIn);
      }

      return flag;
   }

   @OnlyIn(Dist.CLIENT)
   public int[] getColor(Level worldIn, BlockState state, @Nullable BlockPos pos, @Nullable Direction face) {
      if (state.getBlock() != Blocks.AIR) {
         int[] newColor = new int[3];
         if (face != null) {
            newColor = ColorUtil.getBlockStateColor(state, pos, worldIn, face, true);
         } else {
            newColor = ColorUtil.getBlockStateColor(state, pos, worldIn, true);
         }

         if (newColor != null) {
            if (ColorUtil.isColorInvalid(newColor)) {
               if (state.getBlock() == this.getLeaves().getBlock()) {
                  newColor = new int[]{79, 146, 38, 255};
               } else if (state.getBlock() == this.getLog().getBlock() && face == Direction.WEST) {
                  newColor = new int[]{70, 59, 46, 255};
               } else if (state.getBlock() == this.getLeaves().getBlock()) {
                  newColor = new int[]{152, 126, 98, 255};
               }
            }

            return newColor;
         }
      }

      return new int[]{255, 255, 255, 255};
   }

   public ItemEntity dropItemStack(ItemStack itemIn, float offsetY) {
      return this.spawnAtLocation(itemIn, offsetY);
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      boolean flag = itemstack.getItem() == this.getMainHandItem().getItem() && itemstack.getDamageValue() == this.getMainHandItem().getDamageValue();
      if (this.isCinderSprite()) {
         return InteractionResult.PASS;
      } else if (flag) {
         this.consumeItemFromStack(player, itemstack);
         if ((Boolean)this.entityData.get(CAN_DESPAWN)) {
            this.setCanDespawn(false);
         }

         this.setSaplingAmount(this.getSaplingAmount() + 1);

         for(int i = 0; i < 8; ++i) {
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() + (double)this.random.nextFloat() + (double)1.0F, this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), (double)0.0F, (double)0.0F, (double)0.0F);
         }

         this.playSound(PrimitiveMobsSoundEvents.ENTITY_GROVESPRITE_THANKS.get(), 1.0F, 1.0F);
         return InteractionResult.SUCCESS;
      } else if (itemstack.getItem() == Items.GOLD_INGOT) {
         this.consumeItemFromStack(player, itemstack);

         for(int i = 0; i < 8; ++i) {
            this.level().addParticle(ParticleTypes.HEART, this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() + (double)this.random.nextFloat() + (double)1.0F, this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), (double)0.0F, (double)0.0F, (double)0.0F);
         }

         this.playSound(PrimitiveMobsSoundEvents.ENTITY_GROVESPRITE_THANKS.get(), 1.0F, 1.0F);
         this.setHealth(this.getMaxHealth());
         return InteractionResult.SUCCESS;
      } else if (itemstack.getItem() == Items.BONE_MEAL && this.getSaplingAmount() > 0) {
         if (this.getLog() != null) {
            this.consumeItemFromStack(player, itemstack);

            for(int i = 0; i < 8; ++i) {
               this.level().addParticle(ParticleTypes.ENCHANTED_HIT, this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() + (double)this.random.nextFloat() + (double)1.0F, this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), (double)0.0F, (double)0.0F, (double)0.0F);
            }

            this.setSaplingAmount(this.getSaplingAmount() - 1);
            this.playSound(PrimitiveMobsSoundEvents.ENTITY_GROVESPRITE_THANKS.get(), 1.0F, 1.0F);
            if (!this.level().isClientSide) {
               ItemStack sap = this.getOffhandItem().copy();
               sap.setCount(this.random.nextInt(4) + 1);
               ItemEntity item = this.spawnAtLocation(sap, 1.0F);
               item.setDefaultPickUpDelay();
            }
         }

         return InteractionResult.SUCCESS;
      } else {
         return super.mobInteract(player, hand);
      }
   }

   public void dropWonderSap() {
   }

   protected void consumeItemFromStack(Player player, ItemStack stack) {
      if (!player.getAbilities().instabuild) {
         stack.shrink(1);
      }

   }

   @Override
   protected void dropCustomDeathLoot(DamageSource source, int lootingModifier, boolean wasRecentlyHit) {
      super.dropCustomDeathLoot(source, lootingModifier, wasRecentlyHit);
      if (this.isCinderSprite()) {
         ItemStack coal = new ItemStack(Items.CHARCOAL, this.random.nextInt(5) + lootingModifier);
         this.spawnAtLocation(coal, 1.0F);
      }

   }

   @Override
   public float getVoicePitch() {
      return this.isCinderSprite() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
   }

   public float[] getLeavesRGB() {
      return new float[]{this.LeavesR, this.LeavesG, this.LeavesB};
   }

   public void setLeavesRGB(int[] RGB) {
      this.LeavesR = (float)RGB[0];
      this.LeavesG = (float)RGB[1];
      this.LeavesB = (float)RGB[2];
   }

   public float[] getLogRGB() {
      return new float[]{this.LogR, this.LogG, this.LogB};
   }

   public void setLogRGB(int[] RGB) {
      this.LogR = (float)RGB[0];
      this.LogG = (float)RGB[1];
      this.LogB = (float)RGB[2];
   }

   public float[] getLogTopRGB() {
      return new float[]{this.LogTopR, this.LogTopG, this.LogTopB};
   }

   public void setLogTopRGB(int[] RGB) {
      this.LogTopR = (float)RGB[0];
      this.LogTopG = (float)RGB[1];
      this.LogTopB = (float)RGB[2];
   }

   public void setCinderSprite(boolean cinder) {
      this.entityData.set(IS_CINDER, cinder);
   }

   public boolean isCinderSprite() {
      return (Boolean)this.entityData.get(IS_CINDER);
   }

   public void setSaplingAmount(int saplings) {
      this.entityData.set(SAPLING_AMOUNT, saplings);
   }

   public int getSaplingAmount() {
      return (Integer)this.entityData.get(SAPLING_AMOUNT);
   }

   public void setSaplingTimer(int timer) {
      this.entityData.set(SAPLING_TIMER, timer);
   }

   public int getSaplingTimer() {
      return (Integer)this.entityData.get(SAPLING_TIMER);
   }

   public void setLeaves(BlockState leaves) {
      this.entityData.set(LEAVES_ID, Block.getId(leaves));
   }

   public BlockState getLeaves() {
      int id = this.entityData.get(LEAVES_ID);
      BlockState state = Block.stateById(id);
      return state != null && !state.isAir() ? state : Blocks.OAK_LEAVES.defaultBlockState();
   }

   public void setLog(BlockState log) {
      this.entityData.set(LOG_ID, Block.getId(log));
   }

   public BlockState getLog() {
      int id = this.entityData.get(LOG_ID);
      BlockState state = Block.stateById(id);
      return state != null && !state.isAir() ? state : Blocks.OAK_LOG.defaultBlockState();
   }

   public void setLeavesPos(BlockPos pos) {
      this.entityData.set(LEAVES_POS, pos);
   }

   public BlockPos getLeavesPos() {
      BlockPos pos = (BlockPos)this.entityData.get(LEAVES_POS);
      return pos != null ? pos : new BlockPos(0, 0, 0);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return PrimitiveMobsSoundEvents.ENTITY_GROVESPRITE_IDLE.get();
   }

   public void setIsBegging(boolean begging) {
      this.entityData.set(IS_BEGGING, begging);
   }

   public boolean isBegging() {
      return (Boolean)this.entityData.get(IS_BEGGING);
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F;
   }

   public void setCanDespawn(boolean b) {
      this.entityData.set(CAN_DESPAWN, b);
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      NBTUtil.setBlockPosToNBT(this.getLeavesPos(), "LeavesPos", compound);
      NBTUtil.setBlockStateToNBT(this.getLeaves(), "LeavesState", compound);
      NBTUtil.setBlockStateToNBT(this.getLog(), "LogState", compound);
      compound.putInt("SaplingAmount", this.getSaplingAmount());
      compound.putInt("SaplingTimer", this.getSaplingTimer());
      compound.putBoolean("isCinder", this.isCinderSprite());
      compound.putBoolean("canDespawn", (Boolean)this.entityData.get(CAN_DESPAWN));
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setLeavesPos(NBTUtil.getBlockPosFromNBT("LeavesPos", compound));
      this.setLeaves(NBTUtil.getBlockStateFromNBT("LeavesState", compound));
      this.setLog(NBTUtil.getBlockStateFromNBT("LogState", compound));
      this.setSaplingAmount(compound.getInt("SaplingAmount"));
      this.setSaplingTimer(compound.getInt("SaplingTimer"));
      this.setCinderSprite(compound.getBoolean("isCinder"));
      this.setCanDespawn(compound.getBoolean("canDespawn"));
   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return (Boolean)this.entityData.get(CAN_DESPAWN);
   }

   @Override
   public boolean checkSpawnRules(net.minecraft.world.level.LevelAccessor levelAccessor, MobSpawnType reason) {
      int i = Mth.floor(this.getX());
      int j = Mth.floor(this.getBoundingBox().minY);
      int k = Mth.floor(this.getZ());
      BlockPos blockpos = new BlockPos(i, j, k);
      return this.level().getBlockState(blockpos.below()).getBlock() == this.spawnableBlock && this.level().getRawBrightness(blockpos, 0) > 8 && super.checkSpawnRules(levelAccessor, reason);
   }

   static class EntityAIManageSaplings extends MoveToBlockGoal {
      private final EntityGroveSprite sprite;
      private int manageDelay = 0;
      boolean isNearGoal = false;
      int type = 0;
      private int timeoutCounter;
      boolean placeSapling = true;
      boolean hasChosen = false;

      public EntityAIManageSaplings(EntityGroveSprite sprite) {
         super(sprite, (double)0.7F, 16);
         this.sprite = sprite;
         this.manageDelay = sprite.level().random.nextInt(100) + 100;
      }

      @Override
      public boolean canUse() {
         --this.manageDelay;
         return this.searchForDestination() && this.manageDelay <= 0;
      }

      @Override
      public void tick() {
         super.tick();
         this.sprite.getLookControl().setLookAt((double)this.blockPos.getX() + (double)0.5F, (double)this.blockPos.getY(), (double)this.blockPos.getZ() + (double)0.5F, 10.0F, (float)this.sprite.getMaxHeadYRot());
         if (this.sprite.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(this.blockPos.above())) < (double)12.0F) {
            this.isNearGoal = true;
         }

         if (this.isReachedTarget()) {
            if (this.type == 0) {
               this.manageDelay = this.sprite.level().random.nextInt(200) + 200;
               BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), this.sprite.level(), this.blockPos);
               MMMessageRegistry.getNetwork().send(PacketDistributor.ALL.noArg(), new MessageMMParticle(19, 10, (float)this.blockPos.getX() + 0.5F + (this.sprite.random.nextFloat() - this.sprite.random.nextFloat()), (float)this.blockPos.getY() + 0.5F, (float)this.blockPos.getZ() + 0.5F + (this.sprite.random.nextFloat() - this.sprite.random.nextFloat()), (double)0.0F, (double)0.0F, (double)0.0F, 0));
               this.isNearGoal = false;
            } else {
               this.manageDelay = this.sprite.level().random.nextInt(300) + 300;
               ItemStack heldItem = this.sprite.getMainHandItem();
               if (heldItem != null && !heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem) {
                  BlockItem item = (BlockItem)heldItem.getItem();
                  BlockState placeState = item.getBlock().defaultBlockState();
                  this.sprite.level().setBlock(this.blockPos, placeState, 3);
                  this.sprite.level().playLocalSound((double)this.blockPos.getX(), (double)this.blockPos.getY(), (double)this.blockPos.getZ(), SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                  this.sprite.setSaplingAmount(this.sprite.getSaplingAmount() - 1);
               }

               this.isNearGoal = false;
            }

            this.hasChosen = false;
            this.placeSapling = true;
         }

      }

      private boolean searchForDestination() {
         int i = 16;
         int j = 1;
         BlockPos blockpos = this.sprite.blockPosition();
         boolean flag = this.sprite.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING);
         if ((!flag || !PrimitiveMobsConfigSpecial.getGroveSpritesPlant()) && (this.sprite.getSaplingAmount() <= 0 || !this.hasChosen && this.sprite.random.nextInt(20) != 0)) {
            this.placeSapling = false;
         }

         this.hasChosen = true;

         for(int k = 0; k <= 1; k = k > 0 ? -k : 1 - k) {
            for(int l = 0; l < i; ++l) {
               for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                  for(int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                     BlockPos blockpos1 = blockpos.offset(i1, k - 1, j1);
                     if (this.sprite.isWithinRestriction(blockpos1)) {
                        if (this.placeSapling && this.sprite.getSaplingAmount() > 0 && this.isValidTarget(this.sprite.level(), blockpos1)) {
                           this.blockPos = blockpos1;
                           this.type = 1;
                           return true;
                        }

                        if (!this.placeSapling && this.shouldMoveToSapling(this.sprite.level(), blockpos1)) {
                           this.blockPos = blockpos1;
                           this.type = 0;
                           return true;
                        }
                     }
                  }
               }
            }
         }

         this.hasChosen = false;
         return false;
      }

      @Override
      protected boolean isReachedTarget() {
         return this.isNearGoal;
      }

      protected boolean shouldMoveToSapling(Level worldIn, BlockPos pos) {
         ItemStack heldItem = this.sprite.getMainHandItem();
         if (heldItem != null && !heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem) {
            BlockState state = worldIn.getBlockState(pos);
            if (state != null && state.getBlock() != this.sprite.getLeaves().getBlock()) {
               Block block = worldIn.getBlockState(pos).getBlock();
               ItemStack droppedItem = new ItemStack(block, 1);
               if (droppedItem != null && this.sprite.getMainHandItem() != null && this.sprite.getMainHandItem().getItem() == droppedItem.getItem() && this.sprite.getMainHandItem().getDamageValue() == droppedItem.getDamageValue()) {
                  BlockPos abovePos = pos.above();
                  if (worldIn.isEmptyBlock(abovePos)) {
                     return true;
                  }
               }
            }
         }

         return false;
      }

      @Override
      protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
         ItemStack heldItem = this.sprite.getMainHandItem();
         if (heldItem != null && !heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem) {
            BlockItem item = (BlockItem)heldItem.getItem();
            Block block = worldIn.getBlockState(pos).getBlock();
            if (block != null && worldIn.getBlockState(pos).isAir()) {
               return true;
            }
         }

         return false;
      }
   }
}

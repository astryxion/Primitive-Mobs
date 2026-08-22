package net.daveyx0.primitivemobs.entity.monster;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMob;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EntityYeti extends Monster implements IMultiMob, OwnableEntity {
   private static final EntityDataAccessor<Float> HUNGRY_AMOUNT = SynchedEntityData.defineId(EntityYeti.class, EntityDataSerializers.FLOAT);
   private static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(EntityYeti.class, EntityDataSerializers.OPTIONAL_UUID);
   private static final Item[] FOOD_ITEMS = new Item[]{
      Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.BEEF, Items.COOKED_BEEF, Items.CHICKEN, Items.COOKED_CHICKEN, Items.MUTTON, Items.COOKED_MUTTON
   };

   private int eatingTimer;
   private int attackAnimationTick;

   public EntityYeti(EntityType<? extends EntityYeti> type, Level world) {
      super(type, world);
      this.eatingTimer = 0;
      this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
      this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.0D));
      this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
      this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MOVEMENT_SPEED, 0.23D)
         .add(Attributes.MAX_HEALTH, 100.0D)
         .add(Attributes.ATTACK_DAMAGE, 10.0D)
         .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
         .add(Attributes.FOLLOW_RANGE, 16.0D);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(HUNGRY_AMOUNT, 0.0F);
      this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
   }

   @Override
   public void aiStep() {
      super.aiStep();

      if (this.attackAnimationTick > 0) {
         --this.attackAnimationTick;
      }

      if (this.getHungryAmount() < 5000.0F) {
         this.setHungryAmount(this.getHungryAmount() + 1.0F);
      } else {
         this.setHungryAmount(5000.0F);
      }

      if (this.level().isClientSide && this.getHungryAmount() > 4500.0F) {
         this.level().addParticle(ParticleTypes.SMOKE,
            this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
            this.getY() + 3.5D + (this.random.nextFloat() - this.random.nextFloat()),
            this.getZ() + (this.random.nextFloat() - this.random.nextFloat()),
            0.0D, 0.0D, 0.0D);
      }

      if (!this.level().isClientSide) {
         if (this.getHungryAmount() > 2500.0F && this.getTarget() == null && this.eatingTimer <= 0) {
            ItemEntity item = this.findItemsOnGround();
            Player playerWithFood = this.findPlayerWithItems();
            if (item != null) {
               this.getNavigation().moveTo(item, 1.0D);
               if (this.distanceTo(item) <= 2.5D) {
                  this.startEating(item.getItem().copy());
                  item.getItem().shrink(1);
                  if (item.getItem().isEmpty()) {
                     item.discard();
                  }
                  Player nearby = this.level().getNearestPlayer(this, 12.0D);
                  if (nearby != null) {
                     this.setOwnerUUID(nearby.getUUID());
                  }
               }
            } else if (playerWithFood != null) {
               if (this.distanceTo(playerWithFood) >= 5.0D) {
                  this.getNavigation().moveTo(playerWithFood, 1.0D);
               } else {
                  this.getNavigation().stop();
               }
            }
         } else {
            this.followAndDefendOwner();
         }

         if (this.getTarget() == null) {
            this.findEnemyToAttack();
         }
      }

      if (this.eatingTimer > 0 && !this.getMainHandItem().isEmpty()) {
         this.setTarget(null);
         this.eatingTimer--;
         if (this.eatingTimer % 2 == 0) {
            if (this.level().isClientSide) {
               this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getMainHandItem()),
                  this.getX(), this.getY() + 3.5D, this.getZ() - 1.4D,
                  this.random.nextFloat() - this.random.nextFloat(), 0.1D, this.random.nextFloat() - this.random.nextFloat());
            }
            this.playSound(SoundEvents.GENERIC_EAT, 0.5F + 0.5F * this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
         }
         if (this.eatingTimer == 1) {
            this.setHungryAmount(0.0F);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            this.eatingTimer = 0;
            this.setHealth(100.0F);
            if (this.level().isClientSide) {
               for (int i = 0; i < 8; i++) {
                  this.level().addParticle(ParticleTypes.HEART,
                     this.getX() + (this.random.nextFloat() - this.random.nextFloat()),
                     this.getY() + 3.5D + (this.random.nextFloat() - this.random.nextFloat()),
                     this.getZ() + (this.random.nextFloat() - this.random.nextFloat()),
                     0.0D, 0.0D, 0.0D);
               }
            }
         }
      }
   }

   @Override
   public boolean doHurtTarget(Entity target) {
      this.attackAnimationTick = 10;
      this.level().broadcastEntityEvent(this, (byte)4);
      return super.doHurtTarget(target);
   }

   @Override
   public void handleEntityEvent(byte id) {
      if (id == 4) {
         this.attackAnimationTick = 10;
      } else {
         super.handleEntityEvent(id);
      }
   }

   public int getAttackAnimationTick() {
      return this.attackAnimationTick;
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (super.hurt(source, amount)) {
         this.setDeltaMovement(this.getDeltaMovement().multiply(0.1D, 1.0D, 0.1D));
         return true;
      }
      return false;
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!stack.isEmpty() && this.getHungryAmount() > 2500.0F && this.isFoodItem(stack.getItem())) {
         if (!this.level().isClientSide) {
            this.startEating(stack.copy());
            this.setOwnerUUID(player.getUUID());
            this.setTarget(null);
            if (!player.getAbilities().instabuild) {
               stack.shrink(1);
            }
         }
         return InteractionResult.sidedSuccess(this.level().isClientSide);
      }
      return super.mobInteract(player, hand);
   }

   private void startEating(ItemStack stack) {
      ItemStack held = stack.copy();
      held.setCount(1);
      this.setItemSlot(EquipmentSlot.MAINHAND, held);
      this.eatingTimer = 50;
      this.setTarget(null);
   }

   private boolean isFoodItem(Item item) {
      for (Item food : FOOD_ITEMS) {
         if (item == food) {
            return true;
         }
      }
      return false;
   }

   @Nullable
   public Player findPlayerWithItems() {
      Player player = this.level().getNearestPlayer(this, 12.0D);
      if (player != null && this.isFoodItem(player.getMainHandItem().getItem())) {
         return player;
      }
      return null;
   }

   public void followAndDefendOwner() {
      LivingEntity owner = this.getOwner();
      if (!(owner instanceof Player player)) {
         return;
      }
      if (player.getLastHurtMob() != null && player.getLastHurtMob() != this) {
         this.setTarget(player.getLastHurtMob());
      } else if (player.getLastHurtByMob() != null && player.getLastHurtByMob() != this) {
         this.setTarget(player.getLastHurtByMob());
      } else if (this.distanceTo(player) >= 3.5D) {
         this.getNavigation().moveTo(player, 1.0D);
      } else {
         this.getNavigation().stop();
      }
   }

   @Nullable
   public ItemEntity findItemsOnGround() {
      List<ItemEntity> list = this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
      for (ItemEntity item : list) {
         if (this.isFoodItem(item.getItem().getItem())) {
            return item;
         }
      }
      return null;
   }

   protected void findEnemyToAttack() {
      List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
      for (LivingEntity entity : list) {
         if (entity == this || entity == this.getOwner()) {
            continue;
         }
         if (entity instanceof EntityYeti) {
            continue;
         }
         if (entity instanceof Slime || entity instanceof Monster) {
            this.setTarget(entity);
            return;
         }
         if (entity instanceof Animal && this.getHungryAmount() > 4500.0F) {
            this.setTarget(entity);
            return;
         }
      }

      if (this.getHungryAmount() > 4500.0F && this.eatingTimer <= 0) {
         Player player = this.level().getNearestPlayer(this, 12.0D);
         if (player != null && !player.getUUID().equals(this.getOwnerUUID()) && this.hasLineOfSight(player)) {
            this.setTarget(player);
         }
      } else if (this.getHungryAmount() > 2500.0F && this.eatingTimer <= 0) {
         Player player = this.level().getNearestPlayer(this, 4.0D);
         if (player != null && !player.getUUID().equals(this.getOwnerUUID()) && this.hasLineOfSight(player)) {
            this.setTarget(player);
         }
      }
   }

   public float getHungryAmount() {
      return this.entityData.get(HUNGRY_AMOUNT);
   }

   public void setHungryAmount(float amount) {
      this.entityData.set(HUNGRY_AMOUNT, amount);
   }

   public void setOwnerUUID(@Nullable UUID uuid) {
      this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
   }

   @Nullable
   @Override
   public UUID getOwnerUUID() {
      return this.entityData.get(OWNER_UNIQUE_ID).orElse(null);
   }

   @Nullable
   @Override
   public LivingEntity getOwner() {
      try {
         UUID uuid = this.getOwnerUUID();
         return uuid == null ? null : this.level().getPlayerByUUID(uuid);
      } catch (IllegalArgumentException e) {
         return null;
      }
   }

   @Override
   public boolean removeWhenFarAway(double distanceToClosestPlayer) {
      return this.getOwnerUUID() == null;
   }

   @Override
   protected void playStepSound(BlockPos pos, BlockState state) {
      this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
   }

   @Override
   public float getVoicePitch() {
      return (this.random.nextFloat() - this.random.nextFloat()) * 0.5F + 0.1F;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.VILLAGER_HURT;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.VILLAGER_AMBIENT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.VILLAGER_DEATH;
   }

   @Nullable
   @Override
   protected ResourceLocation getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_YETI;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putFloat("Hungry", this.getHungryAmount());
      if (this.getOwnerUUID() == null) {
         compound.putString("OwnerUUID", "");
      } else {
         compound.putString("OwnerUUID", this.getOwnerUUID().toString());
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setHungryAmount(compound.getFloat("Hungry"));
      String owner = compound.getString("OwnerUUID");
      if (!owner.isEmpty()) {
         try {
            this.setOwnerUUID(UUID.fromString(owner));
         } catch (IllegalArgumentException ignored) {
            this.setOwnerUUID(null);
         }
      }
   }
}

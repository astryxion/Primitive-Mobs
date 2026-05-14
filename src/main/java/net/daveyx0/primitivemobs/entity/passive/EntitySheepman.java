package net.daveyx0.primitivemobs.entity.passive;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMobPassive;
import net.daveyx0.multimob.entity.ai.EntityAITemptItemStack;
import net.daveyx0.primitivemobs.core.PrimitiveMobsVillagerProfessions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.common.IShearable;

public class EntitySheepman extends Villager implements IShearable, IMultiMobPassive {
   private static final EntityDataAccessor<Byte> DYE_COLOR = SynchedEntityData.defineId(EntitySheepman.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Boolean> CAN_DESPAWN = SynchedEntityData.defineId(EntitySheepman.class, EntityDataSerializers.BOOLEAN);
   private static final Map<DyeColor, ItemLike> WOOL_BY_DYE = Map.ofEntries(
      Map.entry(DyeColor.WHITE, Blocks.WHITE_WOOL),
      Map.entry(DyeColor.ORANGE, Blocks.ORANGE_WOOL),
      Map.entry(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL),
      Map.entry(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL),
      Map.entry(DyeColor.YELLOW, Blocks.YELLOW_WOOL),
      Map.entry(DyeColor.LIME, Blocks.LIME_WOOL),
      Map.entry(DyeColor.PINK, Blocks.PINK_WOOL),
      Map.entry(DyeColor.GRAY, Blocks.GRAY_WOOL),
      Map.entry(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL),
      Map.entry(DyeColor.CYAN, Blocks.CYAN_WOOL),
      Map.entry(DyeColor.PURPLE, Blocks.PURPLE_WOOL),
      Map.entry(DyeColor.BLUE, Blocks.BLUE_WOOL),
      Map.entry(DyeColor.BROWN, Blocks.BROWN_WOOL),
      Map.entry(DyeColor.GREEN, Blocks.GREEN_WOOL),
      Map.entry(DyeColor.RED, Blocks.RED_WOOL),
      Map.entry(DyeColor.BLACK, Blocks.BLACK_WOOL)
   );
   private int sheepTimer;

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(8, new EntityAITemptItemStack(this, 1.1, false, Sets.newHashSet(new ItemStack[]{new ItemStack(Items.WHEAT)})));
   }

   public EntitySheepman(EntityType<? extends EntitySheepman> type, Level worldIn) {
      super(type, worldIn);
   }

   @Override
   protected void defineSynchedData(SynchedEntityData.Builder builder) {
      super.defineSynchedData(builder);
      builder.define(DYE_COLOR, (byte)0);
      builder.define(CAN_DESPAWN, true);
   }

   public void setCanDespawn(boolean b) {
      this.entityData.set(CAN_DESPAWN, b);
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData livingdata) {
      SpawnGroupData data = super.finalizeSpawn(levelAccessor, difficulty, spawnType, livingdata);
      int profession = this.random.nextInt(3);
      switch (profession) {
         case 0:
            this.setVillagerData(this.getVillagerData().setProfession(PrimitiveMobsVillagerProfessions.SHEEPMAN_PROFESSION_SCAVENGER.get()));
            this.setFleeceColor(DyeColor.RED);
            break;
         case 1:
            this.setVillagerData(this.getVillagerData().setProfession(PrimitiveMobsVillagerProfessions.SHEEPMAN_PROFESSION_ALCHEMIST.get()));
            this.setFleeceColor(DyeColor.ORANGE);
            break;
         case 2:
            this.setVillagerData(this.getVillagerData().setProfession(PrimitiveMobsVillagerProfessions.SHEEPMAN_PROFESSION_THIEF.get()));
            this.setFleeceColor(DyeColor.BLACK);
      }

      this.setVillagerXp(1);
      return data;
   }

   @Override
   public void aiStep() {
      if (this.level().isClientSide) {
         this.sheepTimer = Math.max(0, this.sheepTimer - 1);
      }

      super.aiStep();
   }

   @Override
   protected float getSoundVolume() {
      return 1.1111F;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return SoundEvents.SHEEP_AMBIENT;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return SoundEvents.SHEEP_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.SHEEP_DEATH;
   }

   @Override
   protected void playStepSound(BlockPos pos, net.minecraft.world.level.block.state.BlockState blockIn) {
      this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
   }

   private static final Map<DyeColor, ResourceKey<LootTable>> LOOT_TABLE_BY_COLOR = Map.ofEntries(
      Map.entry(DyeColor.WHITE, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/white"))),
      Map.entry(DyeColor.ORANGE, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/orange"))),
      Map.entry(DyeColor.MAGENTA, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/magenta"))),
      Map.entry(DyeColor.LIGHT_BLUE, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/light_blue"))),
      Map.entry(DyeColor.YELLOW, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/yellow"))),
      Map.entry(DyeColor.LIME, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/lime"))),
      Map.entry(DyeColor.PINK, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/pink"))),
      Map.entry(DyeColor.GRAY, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/gray"))),
      Map.entry(DyeColor.LIGHT_GRAY, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/light_gray"))),
      Map.entry(DyeColor.CYAN, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/cyan"))),
      Map.entry(DyeColor.PURPLE, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/purple"))),
      Map.entry(DyeColor.BLUE, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/blue"))),
      Map.entry(DyeColor.BROWN, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/brown"))),
      Map.entry(DyeColor.GREEN, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/green"))),
      Map.entry(DyeColor.RED, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/red"))),
      Map.entry(DyeColor.BLACK, ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/black")))
   );

   @Override
   protected ResourceKey<LootTable> getDefaultLootTable() {
      if (this.getSheared()) {
         return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep"));
      } else {
         return LOOT_TABLE_BY_COLOR.getOrDefault(this.getFleeceColor(), ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep/white")));
      }
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack itemstack = player.getItemInHand(hand);
      boolean flag = itemstack.getItem() == Items.WHITE_WOOL;
      if (flag && (Boolean)this.entityData.get(CAN_DESPAWN)) {
         this.consumeItemFromStack(player, itemstack);
         this.setCanDespawn(false);

         for(int i = 0; i < 8; ++i) {
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (double)(this.random.nextFloat() - this.random.nextFloat()), this.getY() + (double)this.random.nextFloat() + (double)1.0F, this.getZ() + (double)(this.random.nextFloat() - this.random.nextFloat()), (double)0.0F, (double)0.0F, (double)0.0F);
         }

         return InteractionResult.SUCCESS;
      } else if (itemstack.getItem() == Items.SHEARS && !this.getSheared() && !this.isBaby()) {
         if (!this.level().isClientSide) {
            this.setSheared(true);
            int i = 1 + this.random.nextInt(3);

            for(int j = 0; j < i; ++j) {
               ItemEntity entityitem = this.spawnAtLocation(new ItemStack(WOOL_BY_DYE.get(this.getFleeceColor()), 1), 1.0F);
               entityitem.setDeltaMovement(entityitem.getDeltaMovement().add(
                  (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F),
                  (double)(this.random.nextFloat() * 0.05F),
                  (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
            }
         }

         itemstack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
         this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
         return InteractionResult.SUCCESS;
      } else {
         return super.mobInteract(player, hand);
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
      compound.putBoolean("Sheared", this.getSheared());
      compound.putByte("Color", (byte)this.getFleeceColor().getId());
      compound.putBoolean("canDespawn", (Boolean)this.entityData.get(CAN_DESPAWN));
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setSheared(compound.getBoolean("Sheared"));
      this.setFleeceColor(DyeColor.byId(compound.getByte("Color")));
      this.setCanDespawn(compound.getBoolean("canDespawn"));
   }

   public DyeColor getFleeceColor() {
      return DyeColor.byId((Byte)this.entityData.get(DYE_COLOR) & 15);
   }

   public void setFleeceColor(DyeColor color) {
      byte b0 = (Byte)this.entityData.get(DYE_COLOR);
      this.entityData.set(DYE_COLOR, (byte)(b0 & 240 | color.getId() & 15));
   }

   public boolean getSheared() {
      return ((Byte)this.entityData.get(DYE_COLOR) & 16) != 0;
   }

   public void setSheared(boolean sheared) {
      byte b0 = (Byte)this.entityData.get(DYE_COLOR);
      if (sheared) {
         this.entityData.set(DYE_COLOR, (byte)(b0 | 16));
      } else {
         this.entityData.set(DYE_COLOR, (byte)(b0 & -17));
      }

   }

   @Override
   public boolean isShearable(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
      return !this.getSheared() && !this.isBaby();
   }

   @Override
   public List<ItemStack> onSheared(@Nullable Player player, ItemStack item, Level level, BlockPos pos) {
      this.setSheared(true);
      int i = 1 + this.random.nextInt(3);
      List<ItemStack> ret = new ArrayList<>();

      for(int j = 0; j < i; ++j) {
         ret.add(new ItemStack(WOOL_BY_DYE.get(this.getFleeceColor()), 1));
      }

      this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
      return ret;
   }

   @Override
   @SuppressWarnings("unchecked")
   public Villager getBreedOffspring(ServerLevel serverLevel, AgeableMob ageable) {
      EntitySheepman entitysheep = (EntitySheepman)ageable;
      EntitySheepman entitysheep1 = new EntitySheepman((EntityType<? extends EntitySheepman>)(EntityType<?>)this.getType(), serverLevel);
      return entitysheep1;
   }

   private DyeColor getDyeColorMixFromParents(EntitySheepman father, EntitySheepman mother) {
      DyeColor color1 = father.getFleeceColor();
      DyeColor color2 = mother.getFleeceColor();
      if (color1 == color2) {
         return color1;
      }
      return this.level().random.nextBoolean() ? color1 : color2;
   }

   @Override
   public boolean removeWhenFarAway(double distance) {
      return (Boolean)this.entityData.get(CAN_DESPAWN);
   }

   @Override
   public void thunderHit(ServerLevel serverLevel, LightningBolt lightningBolt) {
      if (!this.level().isClientSide && !this.isRemoved()) {
         Sheep sheep = EntityType.SHEEP.create(this.level());
         sheep.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
         sheep.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(new BlockPos(sheep.blockPosition())), MobSpawnType.CONVERSION, (SpawnGroupData)null);
         sheep.setNoAi(this.isNoAi());
         sheep.setColor(this.getFleeceColor());
         if (this.hasCustomName()) {
            sheep.setCustomName(this.getCustomName());
            sheep.setCustomNameVisible(this.isCustomNameVisible());
         }

         this.level().addFreshEntity(sheep);
         this.discard();
      }

   }
}

package net.daveyx0.primitivemobs.entity.passive;

import java.util.List;
import com.google.common.collect.Sets;
import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMobPassive;
import net.daveyx0.multimob.entity.ai.EntityAIGrabItemFromFloor;
import net.daveyx0.multimob.entity.ai.EntityAIStealFromPlayer;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class EntityFilchLizard extends PathfinderMob implements IMultiMobPassive {
   private int itemChance = 4;
   private AvoidEntityGoal<Player> avoidEntity;
   public ItemStack[] stealItems;
   protected Block spawnableBlock;

   public EntityFilchLizard(EntityType<? extends EntityFilchLizard> type, Level worldIn) {
      super(type, worldIn);
      this.spawnableBlock = Blocks.SAND;
      this.handDropChances[0] = 0.0F;
      this.handDropChances[1] = 0.0F;
   }

   @Override
   protected void registerGoals() {
      int prio = 0;
      this.stealItems = getSpawnLootItems(this, this.getStealLootTable(), new ItemStack(Items.GOLD_INGOT));
      this.goalSelector.addGoal(prio++, new FloatGoal(this));
      this.goalSelector.addGoal(prio++, new PanicGoal(this, (double)1.25F));
      this.goalSelector.addGoal(prio++, new EntityAIGrabItemFromFloor(this, 1.2, Sets.newHashSet(this.stealItems), true));
      this.goalSelector.addGoal(prio++, new EntityAIStealFromPlayer(this, 0.8, Sets.newHashSet(this.stealItems), true));
      this.goalSelector.addGoal(prio++, new AIAvoidWhenNasty(this, Player.class, 16.0F, (double)1.0F, 1.33));
      this.goalSelector.addGoal(prio++, new RandomStrollGoal(this, (double)1.0F));
      this.goalSelector.addGoal(prio++, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(prio++, new RandomLookAroundGoal(this));
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (!this.getMainHandItem().isEmpty()) {
         this.refreshDimensions();
      } else {
         this.refreshDimensions();
      }

   }

   @Nullable
   protected ResourceLocation getSpawnLootTable() {
      return PrimitiveMobsLootTables.FILCHLIZARD_SPAWN;
   }

   @Nullable
   protected ResourceLocation getStealLootTable() {
      return PrimitiveMobsLootTables.FILCHLIZARD_STEAL;
   }

   public static AttributeSupplier.Builder createAttributes() {
      return PathfinderMob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, (double)8.0F)
         .add(Attributes.MOVEMENT_SPEED, 0.25000000417232515);
   }

   @Override
   protected void customServerAiStep() {
      super.customServerAiStep();
   }

   @Nullable
   @Override
   public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData livingdata, @Nullable net.minecraft.nbt.CompoundTag tag) {
      int chance = PrimitiveMobsConfigSpecial.getFilchLizardLootChance();
      if (chance > 0 && (chance >= 100 || this.random.nextInt(100 / chance) == 0)) {
         while(this.getMainHandItem().isEmpty() && !this.level().isClientSide) {
            this.setItemSlot(EquipmentSlot.MAINHAND, getSpawnLootItem(this, this.getSpawnLootTable(), new ItemStack(Items.GOLD_INGOT)));
         }
      }

      return super.finalizeSpawn(levelAccessor, difficulty, spawnType, livingdata, tag);
   }

   @Nullable
   private static ItemStack getSpawnLootItem(net.minecraft.world.entity.Entity entityIn, ResourceLocation resourceLootTable, ItemStack defaultItem) {
      if (resourceLootTable != null && entityIn.level() instanceof net.minecraft.server.level.ServerLevel) {
         net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) entityIn.level();
         net.minecraft.world.level.storage.loot.LootTable loottable = serverLevel.getServer().getLootData().getLootTable(resourceLootTable);
         net.minecraft.world.level.storage.loot.LootParams lootparams = new net.minecraft.world.level.storage.loot.LootParams.Builder(serverLevel)
            .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY, entityIn)
            .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, entityIn.position())
            .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.GIFT);
         List<ItemStack> items = loottable.getRandomItems(lootparams);
         if (!items.isEmpty()) {
            return items.get(0);
         }
      }
      return defaultItem;
   }

   @Nullable
   private static ItemStack[] getSpawnLootItems(net.minecraft.world.entity.Entity entityIn, ResourceLocation resourceLootTable, ItemStack defaultItem) {
      ItemStack[] arrayOfItems = null;
      if (resourceLootTable != null && entityIn.level() instanceof net.minecraft.server.level.ServerLevel) {
         net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) entityIn.level();
         net.minecraft.world.level.storage.loot.LootTable loottable = serverLevel.getServer().getLootData().getLootTable(resourceLootTable);
         net.minecraft.world.level.storage.loot.LootParams lootparams = new net.minecraft.world.level.storage.loot.LootParams.Builder(serverLevel)
            .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY, entityIn)
            .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, entityIn.position())
            .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.GIFT);
         List<ItemStack> listOfItems = loottable.getRandomItems(lootparams);
         arrayOfItems = new ItemStack[listOfItems.size()];
         int i = 0;
         for (ItemStack itemstack : listOfItems) {
            arrayOfItems[i] = itemstack;
            ++i;
         }
      }
      if (arrayOfItems == null) {
         arrayOfItems = new ItemStack[]{defaultItem};
      }
      return arrayOfItems;
   }

   public ItemEntity dropItemStack(ItemStack itemIn, float offsetY) {
      return this.spawnAtLocation(itemIn, offsetY);
   }

   @Override
   public boolean hurt(DamageSource par1DamageSource, float par2) {
      if (par1DamageSource.getEntity() != null) {
         this.lookAt(par1DamageSource.getEntity(), 180.0F, 180.0F);
      }

      ItemStack stack = this.getMainHandItem();
      if (!stack.isEmpty() && !this.level().isClientSide) {
         ItemStack newStack = stack.copy();
         this.dropItemStack(newStack, 1.0F);
         this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
      }

      return super.hurt(par1DamageSource, par2);
   }

   @Override
   public boolean checkSpawnRules(net.minecraft.world.level.LevelAccessor levelAccessor, MobSpawnType reason) {
      int i = Mth.floor(this.getX());
      int j = Mth.floor(this.getBoundingBox().minY);
      int k = Mth.floor(this.getZ());
      BlockPos blockpos = new BlockPos(i, j, k);
      return this.level().getBlockState(blockpos.below()).getBlock() == this.spawnableBlock && this.level().getRawBrightness(blockpos, 0) > 8 && super.checkSpawnRules(levelAccessor, reason);
   }

   static class AIAvoidWhenNasty extends AvoidEntityGoal {
      public AIAvoidWhenNasty(PathfinderMob theEntityIn, Class classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
         super(theEntityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
      }

      @Override
      public boolean canUse() {
         return !this.mob.getMainHandItem().isEmpty() && super.canUse();
      }
   }
}

package net.daveyx0.primitivemobs.entity.passive;

import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMobPassive;
import net.daveyx0.multimob.entity.ai.EntityAITemptItemStack;
import net.daveyx0.primitivemobs.config.PrimitiveMobsConfigSpecial;
import net.daveyx0.primitivemobs.core.PrimitiveMobsItems;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;

public class EntityDodo extends Chicken implements IMultiMobPassive {
   public int timeUntilNextShed;
   public int timeUntilNextDodoEgg;
   private static final ItemStack[] BREEDINGITEMS;

   public EntityDodo(EntityType<? extends EntityDodo> type, Level worldIn) {
      super(type, worldIn);
      this.timeUntilNextShed = this.random.nextInt(6000) + 6000;
      this.timeUntilNextDodoEgg = this.random.nextInt(10000) + 10000;
   }

   @Override
   protected void registerGoals() {
      this.goalSelector.addGoal(0, new FloatGoal(this));
      this.goalSelector.addGoal(1, new PanicGoal(this, 1.4));
      this.goalSelector.addGoal(2, new BreedGoal(this, (double)1.0F));
      this.goalSelector.addGoal(3, new EntityAITemptItemStack(this, (double)1.0F, this.getMainHandItem(), false));
      this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
   }

   public ItemStack getRandomBreedingItem() {
      return BREEDINGITEMS[this.random.nextInt(BREEDINGITEMS.length)];
   }

   @Override
   public boolean isFood(ItemStack stack) {
      return stack.getItem() == this.getMainHandItem().getItem() && stack.getDamageValue() == this.getMainHandItem().getDamageValue();
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (this.isInLove() && this.getMainHandItem().getItem() != Items.AIR) {
         this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.AIR));
      }

      if (!this.isInLove() && !this.level().isClientSide && (this.getMainHandItem().isEmpty() || this.getMainHandItem().getItem() == Items.AIR)) {
         this.setItemInHand(InteractionHand.MAIN_HAND, this.getRandomBreedingItem());
      }

      if (!this.level().isClientSide && !this.isBaby() && !this.isChickenJockey() && --this.timeUntilNextShed <= 0 && PrimitiveMobsConfigSpecial.dodoMycelium) {
         this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
         this.setGrassToMycelium();
         this.timeUntilNextShed = this.random.nextInt(6000) + 6000;
      }

      if (!this.level().isClientSide && !this.isBaby() && !this.isChickenJockey() && --this.timeUntilNextDodoEgg <= 0) {
         this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
         this.spawnAtLocation(PrimitiveMobsItems.DODO_EGG.get(), 1);
         this.timeUntilNextDodoEgg = this.random.nextInt(10000) + 10000;
      }

   }

   @Override
   @SuppressWarnings("unchecked")
   public EntityDodo getBreedOffspring(ServerLevel serverLevel, AgeableMob ageable) {
      return new EntityDodo((EntityType<? extends EntityDodo>)(EntityType<?>)this.getType(), serverLevel);
   }

   public void setGrassToMycelium() {
      BlockPos pos = new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getBoundingBox().minY - 0.1), Mth.floor(this.getZ()));
      BlockState state = this.level().getBlockState(pos);
      Block block = state.getBlock();
      if (block != null && block instanceof GrassBlock && !this.level().isClientSide) {
         this.level().setBlock(pos, Blocks.MYCELIUM.defaultBlockState(), 3);
      }

   }

   @Override
   public float getVoicePitch() {
      return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.1F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F;
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      if (compound.contains("DodoEggTime")) {
         this.timeUntilNextDodoEgg = compound.getInt("DodoEggTime");
      }

      if (compound.contains("ShedTime")) {
         this.timeUntilNextShed = compound.getInt("ShedTime");
      }

   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("ShedTime", this.timeUntilNextShed);
      compound.putInt("DodoEggTime", this.timeUntilNextDodoEgg);
   }

   @Nullable
   @Override
   protected ResourceLocation getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_DODO;
   }

   @Override
   public boolean checkSpawnRules(net.minecraft.world.level.LevelAccessor levelAccessor, net.minecraft.world.entity.MobSpawnType reason) {
      int i = Mth.floor(this.getX());
      int j = Mth.floor(this.getBoundingBox().minY);
      int k = Mth.floor(this.getZ());
      BlockPos blockpos = new BlockPos(i, j, k);
      BlockState iblockstate = this.level().getBlockState((new BlockPos(this.blockPosition())).below());
      boolean flag = iblockstate.isValidSpawn(this.level(), (new BlockPos(this.blockPosition())).below(), this.getType());
      return this.level().getBlockState(blockpos.below()).getBlock() == Blocks.MYCELIUM && this.level().getRawBrightness(blockpos, 0) > 8 && this.getWalkTargetValue(new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getBoundingBox().minY), Mth.floor(this.getZ()))) >= 0.0F && flag;
   }

   static {
      BREEDINGITEMS = new ItemStack[]{new ItemStack(Items.INK_SAC), new ItemStack(Items.BEETROOT), new ItemStack(Items.OAK_LOG), new ItemStack(Items.SPRUCE_LOG), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.POISONOUS_POTATO), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.BEETROOT_SEEDS), new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.PUMPKIN_SEEDS), new ItemStack(Items.MELON_SEEDS), new ItemStack(Items.BONE), new ItemStack(Items.MELON_SLICE), new ItemStack(Items.MELON), new ItemStack(Items.PUMPKIN)};
   }
}

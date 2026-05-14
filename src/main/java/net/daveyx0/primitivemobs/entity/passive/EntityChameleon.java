package net.daveyx0.primitivemobs.entity.passive;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import javax.annotation.Nullable;
import net.daveyx0.multimob.entity.IMultiMobPassive;
import net.daveyx0.multimob.util.ColorUtil;
import net.daveyx0.primitivemobs.core.PrimitiveMobsLootTables;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class EntityChameleon extends Animal implements IMultiMobPassive {
   private float R;
   private float G;
   private float B;
   private float NewR;
   private float NewG;
   private float NewB;
   private int colorSpeed = 4;
   private BlockState currentState;
   private int currentMultiplier;

   public EntityChameleon(EntityType<? extends EntityChameleon> type, Level worldIn) {
      super(type, worldIn);
      this.setSkinRGB(new int[]{0, 125, 25});
   }

   @Override
   public float maxUpStep() {
      return 1.0F;
   }

   @Override
   protected void registerGoals() {
      int prio = 0;
      ++prio;
      this.goalSelector.addGoal(prio, new FloatGoal(this));
      ++prio;
      this.goalSelector.addGoal(prio, new PanicGoal(this, (double)1.25F));
      ++prio;
      this.goalSelector.addGoal(prio, new FollowParentGoal(this, 1.1));
      ++prio;
      this.goalSelector.addGoal(prio, new BreedGoal(this, (double)1.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new RandomStrollGoal(this, (double)1.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new LookAtPlayerGoal(this, Player.class, 6.0F));
      ++prio;
      this.goalSelector.addGoal(prio, new RandomLookAroundGoal(this));
   }

   public static AttributeSupplier.Builder createAttributes() {
      return Animal.createMobAttributes()
         .add(Attributes.MAX_HEALTH, (double)8.0F)
         .add(Attributes.MOVEMENT_SPEED, (double)0.2F);
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F;
   }

   @Override
   @SuppressWarnings("unchecked")
   public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageable) {
      return new EntityChameleon((EntityType<? extends EntityChameleon>)(EntityType<?>)this.getType(), serverLevel);
   }

   @Nullable
   @Override
   protected ResourceKey<LootTable> getDefaultLootTable() {
      return PrimitiveMobsLootTables.ENTITIES_CHAMELEON;
   }

   @Override
   public void tick() {
      if (this.isInWater() && !this.horizontalCollision) {
         this.setDeltaMovement(this.getDeltaMovement().x, 0.02, this.getDeltaMovement().z);
      }

      if (this.level().isClientSide) {
         this.changeColor(this);
      }

      if (this.R != this.NewR || this.G != this.NewG || this.B != this.NewB) {
         for(int i = 0; i < this.colorSpeed; ++i) {
            if (this.R > this.NewR) {
               --this.R;
            } else if (this.R < this.NewR) {
               ++this.R;
            }

            if (this.G > this.NewG) {
               --this.G;
            } else if (this.G < this.NewG) {
               ++this.G;
            }

            if (this.B > this.NewB) {
               --this.B;
            } else if (this.B < this.NewB) {
               ++this.B;
            }
         }
      }

      super.tick();
   }

   @Override
   public boolean isFood(ItemStack stack) {
      return stack.getItem() == Items.SPIDER_EYE;
   }

   public float[] getSkinRGB() {
      return new float[]{this.R, this.G, this.B};
   }

   public void setSkinRGB(int[] RGB) {
      this.R = (float)RGB[0];
      this.G = (float)RGB[1];
      this.B = (float)RGB[2];
   }

   public float[] getNewSkinRGB() {
      return new float[]{this.NewR, this.NewG, this.NewB};
   }

   public void setNewSkinRGB(int[] RGB) {
      this.NewR = (float)RGB[0];
      this.NewG = (float)RGB[1];
      this.NewB = (float)RGB[2];
   }

   @OnlyIn(Dist.CLIENT)
   public void changeColor(Entity entity) {
      int i = Mth.floor(entity.getX());
      int j = Mth.floor(entity.getBoundingBox().minY);
      int k = Mth.floor(entity.getZ());
      if (entity.level().getBlockState(new BlockPos(i, j, k)).getBlock() == Blocks.AIR) {
         j = Mth.floor(entity.getBoundingBox().minY - 0.1);
      }

      BlockPos pos = new BlockPos(i, j, k);
      BlockState state = entity.level().getBlockState(pos);
      int colorMultiplier = Minecraft.getInstance().getBlockColors().getColor(state, this.level(), pos, 0);
      if (state.getBlock() != Blocks.AIR && (this.currentState != state || this.currentMultiplier != colorMultiplier)) {
         this.currentState = state;
         this.currentMultiplier = colorMultiplier;
         int[] newColor = ColorUtil.getBlockStateColor(state, pos, this.level(), true);
         if (newColor != null) {
            if (ColorUtil.isColorInvalid(newColor)) {
               newColor = new int[]{63, 118, 42, 255};
            }

            this.setNewSkinRGB(newColor);
         }
      }

   }
}

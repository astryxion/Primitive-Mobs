package net.daveyx0.primitivemobs.item;

import java.awt.Color;
import java.util.List;
import javax.annotation.Nullable;
import net.daveyx0.multimob.message.MMMessageRegistry;
import net.daveyx0.multimob.util.ColorUtil;
import net.daveyx0.primitivemobs.message.MessagePrimitiveColor;
import net.daveyx0.primitivemobs.message.PrimitiveMobsMessageRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemCamouflageArmor extends ArmorItem implements DyeableLeatherItem {
   private float R;
   private float G;
   private float B;
   private float NewR;
   private float NewG;
   private float NewB;
   private int colorSpeed = 4;
   private BlockState currentState;
   private int currentMultiplier;

   public ItemCamouflageArmor(ArmorMaterials material, ArmorItem.Type type, Item.Properties properties) {
      super(material, type, properties);
      this.setSkinRGB(new int[]{255, 255, 255});
   }

   @Override
   public void onArmorTick(ItemStack armor, Level world, Player player) {
      if (player != null && player.level().isClientSide && !this.getCannotChange(armor)) {
         this.changeColor(player);
         this.setColor(armor, (new Color((int)this.getSkinRGB()[0], (int)this.getSkinRGB()[1], (int)this.getSkinRGB()[2])).hashCode());
         if (this.currentState != null) {
            this.setColorBlockState(armor, this.currentState);
         }

         PrimitiveMobsMessageRegistry.getPrimitiveNetwork().sendToServer(new MessagePrimitiveColor(this.getColor(armor), this.getEquipmentSlot(), player.getUUID().toString()));
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

      player.inventoryMenu.broadcastChanges();
      super.onArmorTick(armor, world, player);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
      if (!stack.isEmpty() && stack.getItem() instanceof ItemCamouflageArmor) {
         ItemCamouflageArmor armor = (ItemCamouflageArmor)stack.getItem();
         if (armor.getCannotChange(stack)) {
            tooltip.add(Component.literal("Camouflage: disabled"));
         } else {
            tooltip.add(Component.literal("Camouflage: enabled"));
         }

         new Color(armor.getColor(stack));
         BlockState state = armor.getColorBlockState(stack);
         if (state != null) {
            String name = new ItemStack(state.getBlock()).getHoverName().getString();
            if (name.equals(Blocks.AIR.getName().getString())) {
               tooltip.add(Component.literal("Block: " + state.getBlock().getName().getString()));
            } else {
               tooltip.add(Component.literal("Block: " + new ItemStack(state.getBlock()).getHoverName().getString()));
            }
         }
      }

      super.appendHoverText(stack, worldIn, tooltip, flagIn);
   }

   public boolean hasOverlay(ItemStack stack) {
      return true;
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
      int colorMultiplier = Minecraft.getInstance().getBlockColors().getColor(state, entity.level(), pos, 0);
      if (state.getBlock() != Blocks.AIR && (this.currentState != state || this.currentMultiplier != colorMultiplier)) {
         this.currentState = state;
         this.currentMultiplier = colorMultiplier;
         int[] newColor = ColorUtil.getBlockStateColor(state, pos, entity.level(), true);
         if (newColor != null) {
            if (ColorUtil.isColorInvalid(newColor)) {
               newColor = new int[]{255, 255, 255, 255};
            }

            this.setNewSkinRGB(newColor);
         }
      }

   }

   @Override
   public boolean hasCustomColor(ItemStack stack) {
      CompoundTag nbttagcompound = stack.getTag();
      return nbttagcompound != null && nbttagcompound.contains("display", 10) ? nbttagcompound.getCompound("display").contains("color", 3) : false;
   }

   @Override
   public int getColor(ItemStack stack) {
      CompoundTag nbttagcompound = stack.getTag();
      if (nbttagcompound != null) {
         CompoundTag nbttagcompound1 = nbttagcompound.getCompound("display");
         if (nbttagcompound1 != null && nbttagcompound1.contains("color", 3)) {
            return nbttagcompound1.getInt("color");
         }
      }

      return 16777215;
   }

   @Override
   public void clearColor(ItemStack stack) {
      CompoundTag nbttagcompound = stack.getTag();
      if (nbttagcompound != null) {
         CompoundTag nbttagcompound1 = nbttagcompound.getCompound("display");
         if (nbttagcompound1.contains("color")) {
            nbttagcompound1.remove("color");
         }
      }

   }

   @Nullable
   public BlockState getColorBlockState(ItemStack stack) {
      CompoundTag nbttagcompound = stack.getTag();
      if (nbttagcompound == null || !nbttagcompound.contains("BlockStateTag", 10)) {
         return Blocks.AIR.defaultBlockState();
      }
      return NbtUtils.readBlockState(net.minecraft.core.registries.BuiltInRegistries.BLOCK.asLookup(), nbttagcompound.getCompound("BlockStateTag"));
   }

   private BlockState readBlockState(CompoundTag tag) {
      if (tag.contains("BlockStateTag", 10)) {
         return NbtUtils.readBlockState(net.minecraft.core.registries.BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("BlockStateTag"));
      }
      return Blocks.AIR.defaultBlockState();
   }

   public void setColorBlockState(ItemStack stack, BlockState state) {
      CompoundTag nbttagcompound = stack.getOrCreateTag();
      nbttagcompound.put("BlockStateTag", NbtUtils.writeBlockState(state));
   }

   @Override
   public void setColor(ItemStack stack, int color) {
      CompoundTag nbttagcompound = stack.getOrCreateTag();

      CompoundTag nbttagcompound1 = nbttagcompound.getCompound("display");
      if (!nbttagcompound.contains("display", 10)) {
         nbttagcompound.put("display", nbttagcompound1);
      }

      nbttagcompound1.putInt("color", color);
   }

   public boolean getCannotChange(ItemStack stack) {
      CompoundTag nbttagcompound = stack.getTag();
      if (nbttagcompound == null) {
         return false;
      } else {
         return nbttagcompound.contains("change") ? nbttagcompound.getBoolean("change") : false;
      }
   }

   public void setCannotChange(ItemStack stack, boolean set) {
      CompoundTag nbttagcompound = stack.getOrCreateTag();
      nbttagcompound.putBoolean("change", set);
   }

   public static void setCamouflageArmorNBT(LivingEntity entity, EquipmentSlot slot) {
      ItemStack stack = entity.getItemBySlot(slot);
      if (stack.getItem() instanceof ItemCamouflageArmor) {
         ItemCamouflageArmor item = (ItemCamouflageArmor)stack.getItem();
         if (!item.getCannotChange(stack) && entity.level().isClientSide) {
            int color = ColorUtil.getBlockColor(entity);
            if (color < -1) {
               item.setColor(stack, color);
               item.setColorBlockState(stack, ColorUtil.getBlockState(entity));
               PrimitiveMobsMessageRegistry.getPrimitiveNetwork().sendToServer(new MessagePrimitiveColor(item.getColor(stack), slot, entity.getUUID().toString()));
            }
         }
      }

   }
}

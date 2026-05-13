package net.daveyx0.primitivemobs.item;

import java.util.List;
import javax.annotation.Nullable;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.multimob.util.NBTUtil;
import net.daveyx0.primitivemobs.entity.passive.EntityGroveSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemGroveSpriteSap extends ItemPrimitive {
   public ItemGroveSpriteSap(Item.Properties properties) {
      super(properties);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
      CompoundTag nbttagcompound = stack.getTag();
      if (nbttagcompound != null) {
         if (nbttagcompound.contains("LogStateID")) {
            BlockState logBlockState = NBTUtil.getBlockStateFromNBT("LogState", nbttagcompound);
            tooltip.add(Component.literal("Type: " + EntityUtil.getBlockStateName(logBlockState)));
         }
      } else {
         tooltip.add(Component.literal("Type: Unknown"));
      }

   }

   public static void setSapLogState(EntityGroveSprite sprite, ItemStack stack) {
      if (sprite != null) {
         CompoundTag itemtagcompound = new CompoundTag();
         if (stack.hasTag()) {
            itemtagcompound = stack.getTag();
         }

         if (sprite.getLog() != null) {
            NBTUtil.setBlockStateToNBT(sprite.getLog(), "LogState", itemtagcompound);
            stack.setTag(itemtagcompound);
         }
      }

   }

   public static void setColor(ItemStack stack, int color) {
      CompoundTag nbttagcompound = stack.getTag();
      if (nbttagcompound == null) {
         nbttagcompound = new CompoundTag();
         stack.setTag(nbttagcompound);
      }

      CompoundTag nbttagcompound1 = nbttagcompound.getCompound("display");
      if (!nbttagcompound.contains("display", 10)) {
         nbttagcompound.put("display", nbttagcompound1);
      }

      nbttagcompound1.putInt("color", color);
   }

   public static ItemStack getLogFromSap(ItemStack sap, int logAmount) {
      CompoundTag nbttagcompound = sap.getTag();
      if (nbttagcompound != null && nbttagcompound.contains("LogStateID")) {
         BlockState logBlockState = NBTUtil.getBlockStateFromNBT("LogState", nbttagcompound);
         ItemStack log = new ItemStack(logBlockState.getBlock(), logAmount);
         return log;
      } else {
         return ItemStack.EMPTY;
      }
   }

   public static int getColor(ItemStack stack) {
      CompoundTag nbttagcompound = stack.getTag();
      if (nbttagcompound != null) {
         CompoundTag nbttagcompound1 = nbttagcompound.getCompound("display");
         if (nbttagcompound1 != null && nbttagcompound1.contains("color", 3)) {
            return nbttagcompound1.getInt("color");
         }
      }

      return 16777215;
   }
}

package net.daveyx0.primitivemobs.item;

import java.util.List;
import javax.annotation.Nullable;
import net.daveyx0.multimob.util.EntityUtil;
import net.daveyx0.multimob.util.NBTUtil;
import net.daveyx0.primitivemobs.entity.passive.EntityGroveSprite;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ItemGroveSpriteSap extends ItemPrimitive {
   public ItemGroveSpriteSap(Item.Properties properties) {
      super(properties);
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
      CompoundTag nbttagcompound = getCustomTag(stack);
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
         CompoundTag itemtagcompound = getCustomTag(stack);
         if (itemtagcompound == null) {
            itemtagcompound = new CompoundTag();
         }

         if (sprite.getLog() != null) {
            NBTUtil.setBlockStateToNBT(sprite.getLog(), "LogState", itemtagcompound);
            setCustomTag(stack, itemtagcompound);
         }
      }

   }

   public static void setColor(ItemStack stack, int color) {
      CompoundTag nbttagcompound = getOrCreateCustomTag(stack);
      CompoundTag nbttagcompound1 = nbttagcompound.getCompound("display");
      if (!nbttagcompound.contains("display", 10)) {
         nbttagcompound.put("display", nbttagcompound1);
      }

      nbttagcompound1.putInt("color", color);
      setCustomTag(stack, nbttagcompound);
   }

   public static ItemStack getLogFromSap(ItemStack sap, int logAmount) {
      CompoundTag nbttagcompound = getCustomTag(sap);
      if (nbttagcompound != null && nbttagcompound.contains("LogStateID")) {
         BlockState logBlockState = NBTUtil.getBlockStateFromNBT("LogState", nbttagcompound);
         ItemStack log = new ItemStack(logBlockState.getBlock(), logAmount);
         return log;
      } else {
         return ItemStack.EMPTY;
      }
   }

   public static int getColor(ItemStack stack) {
      CompoundTag nbttagcompound = getCustomTag(stack);
      if (nbttagcompound != null) {
         CompoundTag nbttagcompound1 = nbttagcompound.getCompound("display");
         if (nbttagcompound1 != null && nbttagcompound1.contains("color", 3)) {
            return nbttagcompound1.getInt("color");
         }
      }

      return 16777215;
   }

   public static int getTintColor(ItemStack stack) {
      return hasCustomColor(stack) ? getColor(stack) : -1;
   }

   public static boolean hasCustomColor(ItemStack stack) {
      CompoundTag nbttagcompound = getCustomTag(stack);
      return nbttagcompound != null && nbttagcompound.contains("display", 10) && nbttagcompound.getCompound("display").contains("color", 3);
   }

   @Nullable
   private static CompoundTag getCustomTag(ItemStack stack) {
      return stack.has(DataComponents.CUSTOM_DATA) ? stack.get(DataComponents.CUSTOM_DATA).copyTag() : null;
   }

   private static CompoundTag getOrCreateCustomTag(ItemStack stack) {
      CompoundTag nbttagcompound = getCustomTag(stack);
      return nbttagcompound != null ? nbttagcompound : new CompoundTag();
   }

   private static void setCustomTag(ItemStack stack, CompoundTag tag) {
      stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
   }
}

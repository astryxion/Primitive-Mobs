package net.daveyx0.primitivemobs.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class ItemPrimitiveFood extends Item {
   public ItemPrimitiveFood(int amount, float saturation, boolean isWolfFood, Item.Properties properties) {
      super(properties.food(new FoodProperties.Builder()
         .nutrition(amount)
         .saturationMod(saturation)
         .build()));
   }
}

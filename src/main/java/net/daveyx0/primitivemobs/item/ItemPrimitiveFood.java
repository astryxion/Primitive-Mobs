package net.daveyx0.primitivemobs.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class ItemPrimitiveFood extends Item {
   public ItemPrimitiveFood(int amount, float saturation, boolean isWolfFood, Item.Properties properties) {
      super(properties.food(buildFood(amount, saturation)));
   }

   private static FoodProperties buildFood(int amount, float saturation) {
      FoodProperties.Builder builder = new FoodProperties.Builder().nutrition(amount).saturationModifier(saturation);
      if (amount == 4 && saturation == 1.2F) {
         builder.effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F);
      }

      return builder.build();
   }
}

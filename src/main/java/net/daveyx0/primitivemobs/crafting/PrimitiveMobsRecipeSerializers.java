package net.daveyx0.primitivemobs.crafting;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PrimitiveMobsRecipeSerializers {

   public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, "primitivemobs");

   public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CamouflageToggleRecipe>> CAMOUFLAGE_TOGGLE =
      RECIPE_SERIALIZERS.register("camouflage_toggle", CamouflageToggleRecipe.Serializer::new);

   public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<WonderSapRecipe>> WONDERSAP_CONVERT =
      RECIPE_SERIALIZERS.register("wondersap_convert", WonderSapRecipe.Serializer::new);

   public static void init(IEventBus modEventBus) {
      RECIPE_SERIALIZERS.register(modEventBus);
   }
}

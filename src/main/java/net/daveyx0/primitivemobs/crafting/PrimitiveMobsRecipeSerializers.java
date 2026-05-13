package net.daveyx0.primitivemobs.crafting;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PrimitiveMobsRecipeSerializers {

   public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "primitivemobs");

   public static final RegistryObject<RecipeSerializer<CamouflageToggleRecipe>> CAMOUFLAGE_TOGGLE =
      RECIPE_SERIALIZERS.register("camouflage_toggle", CamouflageToggleRecipe.Serializer::new);

   public static final RegistryObject<RecipeSerializer<WonderSapRecipe>> WONDERSAP_CONVERT =
      RECIPE_SERIALIZERS.register("wondersap_convert", WonderSapRecipe.Serializer::new);

   public static void init(IEventBus modEventBus) {
      RECIPE_SERIALIZERS.register(modEventBus);
   }
}

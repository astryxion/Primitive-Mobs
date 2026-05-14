package net.daveyx0.primitivemobs.crafting;

import com.mojang.serialization.MapCodec;
import net.daveyx0.primitivemobs.item.ItemGroveSpriteSap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class WonderSapRecipe extends ShapelessRecipe {

   public WonderSapRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
      super(group, category, result, ingredients);
   }

   @Override
   public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
      ItemStack output = super.assemble(inv, registries);
      if (!output.isEmpty()) {
         for(int i = 0; i < inv.size(); ++i) {
            ItemStack ingredient = inv.getItem(i);
            if (!ingredient.isEmpty() && ingredient.getItem() instanceof ItemGroveSpriteSap) {
               ItemStack log = ItemGroveSpriteSap.getLogFromSap(ingredient, 1);
               if (!log.isEmpty()) {
                  output = log;
               }
            }
         }
      }

      return output;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return PrimitiveMobsRecipeSerializers.WONDERSAP_CONVERT.get();
   }

   public static class Serializer implements RecipeSerializer<WonderSapRecipe> {
      private final ShapelessRecipe.Serializer vanilla = new ShapelessRecipe.Serializer();

      @Override
      public MapCodec<WonderSapRecipe> codec() {
         return vanilla.codec().xmap(
            shapeless -> new WonderSapRecipe(shapeless.getGroup(), shapeless.category(), shapeless.getResultItem(RegistryAccess.EMPTY), NonNullList.copyOf(shapeless.getIngredients())),
            recipe -> new ShapelessRecipe(recipe.getGroup(), recipe.category(), recipe.getResultItem(RegistryAccess.EMPTY), recipe.getIngredients())
         );
      }

      @Override
      public StreamCodec<RegistryFriendlyByteBuf, WonderSapRecipe> streamCodec() {
         return vanilla.streamCodec().map(
            shapeless -> new WonderSapRecipe(shapeless.getGroup(), shapeless.category(), shapeless.getResultItem(RegistryAccess.EMPTY), NonNullList.copyOf(shapeless.getIngredients())),
            recipe -> new ShapelessRecipe(recipe.getGroup(), recipe.category(), recipe.getResultItem(RegistryAccess.EMPTY), recipe.getIngredients())
         );
      }
   }
}

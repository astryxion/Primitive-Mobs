package net.daveyx0.primitivemobs.crafting;

import com.google.gson.JsonObject;
import net.daveyx0.primitivemobs.item.ItemGroveSpriteSap;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class WonderSapRecipe extends ShapelessRecipe {

   public WonderSapRecipe(ResourceLocation id, String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
      super(id, group, category, result, ingredients);
   }

   @Override
   public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
      ItemStack output = super.assemble(inv, registryAccess);
      if (!output.isEmpty()) {
         for(int i = 0; i < inv.getContainerSize(); ++i) {
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
      @Override
      public WonderSapRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
         ShapelessRecipe base = RecipeSerializer.SHAPELESS_RECIPE.fromJson(recipeId, json);
         return new WonderSapRecipe(recipeId, base.getGroup(), base.category(), base.getResultItem(RegistryAccess.EMPTY), base.getIngredients());
      }

      @Override
      public WonderSapRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
         ShapelessRecipe base = RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(recipeId, buffer);
         return new WonderSapRecipe(recipeId, base.getGroup(), base.category(), base.getResultItem(RegistryAccess.EMPTY), base.getIngredients());
      }

      @Override
      public void toNetwork(FriendlyByteBuf buffer, WonderSapRecipe recipe) {
         RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buffer, recipe);
      }
   }
}

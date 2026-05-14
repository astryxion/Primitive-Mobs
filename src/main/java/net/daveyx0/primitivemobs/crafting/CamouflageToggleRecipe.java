package net.daveyx0.primitivemobs.crafting;

import com.mojang.serialization.MapCodec;
import net.daveyx0.primitivemobs.item.ItemCamouflageArmor;
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

public class CamouflageToggleRecipe extends ShapelessRecipe {

   public CamouflageToggleRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
      super(group, category, result, ingredients);
   }

   @Override
   public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
      ItemStack output = super.assemble(inv, registries);
      if (!output.isEmpty()) {
         for(int i = 0; i < inv.size(); ++i) {
            ItemStack ingredient = inv.getItem(i);
            if (!ingredient.isEmpty() && output.getItem().equals(ingredient.getItem()) && output.getItem() instanceof ItemCamouflageArmor) {
               ItemCamouflageArmor armor = (ItemCamouflageArmor)output.getItem();
               armor.setColor(output, armor.getColor(ingredient));
               armor.setColorBlockState(output, armor.getColorBlockState(ingredient));
               armor.setCannotChange(output, !armor.getCannotChange(ingredient));
               output.setDamageValue(ingredient.getDamageValue());
            }
         }
      }

      return output;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return PrimitiveMobsRecipeSerializers.CAMOUFLAGE_TOGGLE.get();
   }

   public static class Serializer implements RecipeSerializer<CamouflageToggleRecipe> {
      private final ShapelessRecipe.Serializer vanilla = new ShapelessRecipe.Serializer();

      @Override
      public MapCodec<CamouflageToggleRecipe> codec() {
         return vanilla.codec().xmap(
            shapeless -> new CamouflageToggleRecipe(shapeless.getGroup(), shapeless.category(), shapeless.getResultItem(RegistryAccess.EMPTY), NonNullList.copyOf(shapeless.getIngredients())),
            recipe -> new ShapelessRecipe(recipe.getGroup(), recipe.category(), recipe.getResultItem(RegistryAccess.EMPTY), recipe.getIngredients())
         );
      }

      @Override
      public StreamCodec<RegistryFriendlyByteBuf, CamouflageToggleRecipe> streamCodec() {
         return vanilla.streamCodec().map(
            shapeless -> new CamouflageToggleRecipe(shapeless.getGroup(), shapeless.category(), shapeless.getResultItem(RegistryAccess.EMPTY), NonNullList.copyOf(shapeless.getIngredients())),
            recipe -> new ShapelessRecipe(recipe.getGroup(), recipe.category(), recipe.getResultItem(RegistryAccess.EMPTY), recipe.getIngredients())
         );
      }
   }
}

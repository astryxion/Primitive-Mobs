package net.daveyx0.primitivemobs.client.renderer.entity;

import net.daveyx0.primitivemobs.client.models.ModelBlazingJuggernaut;
import net.daveyx0.primitivemobs.entity.monster.EntityBlazingJuggernaut;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBlazingJuggernaut extends MobRenderer<EntityBlazingJuggernaut, ModelBlazingJuggernaut<EntityBlazingJuggernaut>> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("primitivemobs", "blazing_juggernaut"), "main");
   private static final ResourceLocation BLAZINGJUGGERNAUT_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/blazingjuggernaut/blazingjuggernaut.png");

   public RenderBlazingJuggernaut(EntityRendererProvider.Context context) {
      super(context, new ModelBlazingJuggernaut<>(context.bakeLayer(MODEL_LAYER)), 0.5F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityBlazingJuggernaut entity) {
      return BLAZINGJUGGERNAUT_TEXTURES;
   }
}

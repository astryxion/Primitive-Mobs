package net.daveyx0.primitivemobs.client.renderer.entity;

import net.daveyx0.primitivemobs.client.models.ModelFilchLizard;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerHeldItemCustom;
import net.daveyx0.primitivemobs.entity.passive.EntityFilchLizard;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("unchecked")
@OnlyIn(Dist.CLIENT)
public class RenderFilchLizard extends MobRenderer<EntityFilchLizard, ModelFilchLizard> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "filch_lizard"), "main");
   private static final ResourceLocation FILCHLIZARD_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/filchlizard/filchlizard.png");

   public RenderFilchLizard(EntityRendererProvider.Context context) {
      super(context, new ModelFilchLizard(context.bakeLayer(MODEL_LAYER)), 0.4F);
      this.addLayer((RenderLayer) new LayerHeldItemCustom(this));
   }

   @Override
   public ResourceLocation getTextureLocation(EntityFilchLizard entity) {
      return FILCHLIZARD_TEXTURES;
   }
}

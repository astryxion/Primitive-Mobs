package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.daveyx0.primitivemobs.client.models.ModelTravelingMerchant;
import net.daveyx0.primitivemobs.entity.passive.EntityTravelingMerchant;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTravelingMerchant extends MobRenderer<EntityTravelingMerchant, ModelTravelingMerchant<EntityTravelingMerchant>> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("primitivemobs", "traveling_merchant"), "main");
   private static final ResourceLocation TRAVELINGMERCHANT_TEXTURE = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/villager/travelingmerchant.png");

   public RenderTravelingMerchant(EntityRendererProvider.Context context) {
      super(context, new ModelTravelingMerchant<>(context.bakeLayer(MODEL_LAYER)), 0.5F);
   }

   @SuppressWarnings("unchecked")
   public ModelTravelingMerchant<EntityTravelingMerchant> getMainModel() {
      return (ModelTravelingMerchant<EntityTravelingMerchant>) super.getModel();
   }

   @Override
   public ResourceLocation getTextureLocation(EntityTravelingMerchant entity) {
      return TRAVELINGMERCHANT_TEXTURE;
   }

   @Override
   protected void scale(EntityTravelingMerchant entitylivingbaseIn, PoseStack poseStack, float partialTickTime) {
      float f = 0.9375F;
      if (entitylivingbaseIn.isBaby()) {
         f = (float)((double)f * (double)0.5F);
         this.shadowRadius = 0.25F;
      } else {
         this.shadowRadius = 0.5F;
      }

      poseStack.scale(f, f, f);
   }
}

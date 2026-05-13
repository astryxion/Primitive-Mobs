package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.daveyx0.primitivemobs.client.models.ModelLilyLurker;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerHeldItemCustom;
import net.daveyx0.primitivemobs.entity.monster.EntityLilyLurker;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("unchecked")
@OnlyIn(Dist.CLIENT)
public class RenderLilyLurker extends MobRenderer<EntityLilyLurker, ModelLilyLurker<EntityLilyLurker>> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "lily_lurker"), "main");
   private static final ResourceLocation LILYLURKER_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/lilylurker/lilylurker.png");

   public RenderLilyLurker(EntityRendererProvider.Context context) {
      super(context, new ModelLilyLurker<>(context.bakeLayer(MODEL_LAYER)), 0.5F);
      this.addLayer((RenderLayer) new LayerHeldItemCustom(this));
   }

   @Override
   public ResourceLocation getTextureLocation(EntityLilyLurker entity) {
      return LILYLURKER_TEXTURES;
   }

   @Override
   protected void scale(EntityLilyLurker lurker, PoseStack poseStack, float par2) {
      if (lurker.isCamouflaged()) {
         poseStack.translate(0.0F, -0.24F, 0.0F);
      }
   }
}

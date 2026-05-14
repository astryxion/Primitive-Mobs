package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.entity.monster.EntityBabySpider;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerBabySpiderEyes<T extends EntityBabySpider> extends RenderLayer<T, SpiderModel<T>> {
   private static final ResourceLocation SPIDER_EYES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/spider_eyes.png");

   public LayerBabySpiderEyes(RenderLayerParent<T, SpiderModel<T>> spiderRendererIn) {
      super(spiderRendererIn);
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      float r, g, b;
      if (entitylivingbaseIn.hasCustomName() && "jeb_".equals(entitylivingbaseIn.getCustomName().getString())) {
         int i2 = entitylivingbaseIn.tickCount / 25 + entitylivingbaseIn.getId();
         int j1 = DyeColor.values().length;
         int k1 = i2 % j1;
         int l = (i2 + 1) % j1;
         float f = ((float)(entitylivingbaseIn.tickCount % 25) + partialTicks) / 25.0F;
         float[] afloat1 = getDyeColorArray(DyeColor.byId(k1));
         float[] afloat2 = getDyeColorArray(DyeColor.byId(l));
         r = afloat1[0] * (1.0F - f) + afloat2[0] * f;
         g = afloat1[1] * (1.0F - f) + afloat2[1] * f;
         b = afloat1[2] * (1.0F - f) + afloat2[2] * f;
      } else if (entitylivingbaseIn.getGrowthLevel() < 5) {
         r = 0.6509804F;
         g = 0.007843138F;
         b = 0.007843138F;
      } else {
         float[] afloat = getDyeColorArray(entitylivingbaseIn.getEyeColor());
         r = afloat[0];
         g = afloat[1];
         b = afloat[2];
      }

      VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(SPIDER_EYES));
      int color = FastColor.ARGB32.color(255, (int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F));
      this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
   }

   private static float[] getDyeColorArray(DyeColor color) {
      int rgb = color.getTextureDiffuseColor();
      return new float[]{
         (float)FastColor.ARGB32.red(rgb) / 255.0F,
         (float)FastColor.ARGB32.green(rgb) / 255.0F,
         (float)FastColor.ARGB32.blue(rgb) / 255.0F
      };
   }
}

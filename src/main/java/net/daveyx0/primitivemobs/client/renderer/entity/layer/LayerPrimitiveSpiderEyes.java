package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.entity.monster.EntityPrimitiveSpider;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerPrimitiveSpiderEyes<T extends EntityPrimitiveSpider> extends RenderLayer<T, SpiderModel<T>> {
   private static final ResourceLocation SPIDER_EYES = ResourceLocation.parse("textures/entity/spider_eyes.png");

   public LayerPrimitiveSpiderEyes(RenderLayerParent<T, SpiderModel<T>> spiderRendererIn) {
      super(spiderRendererIn);
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(SPIDER_EYES));
      this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
   }
}

package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.client.models.ModelFlameSpewer;
import net.daveyx0.primitivemobs.client.renderer.entity.RenderFlameSpewer;
import net.daveyx0.primitivemobs.entity.monster.EntityFlameSpewer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class LayerLavaSkin extends RenderLayer<EntityFlameSpewer, ModelFlameSpewer> {
   private final ModelFlameSpewer lavaModel;
   private final ResourceLocation lavaTexture;

   public LayerLavaSkin(RenderFlameSpewer renderer, EntityRendererProvider.Context context) {
      super(renderer);
      this.lavaTexture = new ResourceLocation("minecraft", "textures/block/lava_flow.png");
      this.lavaModel = new ModelFlameSpewer(context.bakeLayer(RenderFlameSpewer.LAVA_LAYER), true, true);
   }

   @Override
   @SuppressWarnings("unchecked")
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityFlameSpewer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      poseStack.pushPose();
      this.getParentModel().copyPropertiesTo(this.lavaModel);
      if (this.lavaModel instanceof ModelFlameSpewer) {
         ((ModelFlameSpewer)this.lavaModel).setFlameSpewerModelAttributes((ModelFlameSpewer)this.getParentModel());
      }

      float f = (float)entitylivingbaseIn.tickCount + partialTicks;
      float vScroll = (float)Math.round(f) * 0.03125F;

      float alpha = 1.0F;
      if (entitylivingbaseIn instanceof EntityFlameSpewer) {
         alpha = (10.0F - (float)((EntityFlameSpewer)entitylivingbaseIn).getAttackTime()) / 10.0F;
      }

      this.lavaModel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
      this.lavaModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.energySwirl(this.lavaTexture, 0.0F, vScroll));
      this.lavaModel.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
      poseStack.popPose();
   }
}

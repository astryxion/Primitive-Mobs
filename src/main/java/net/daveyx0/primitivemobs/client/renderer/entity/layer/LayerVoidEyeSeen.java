package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.client.models.ModelVoidEye;
import net.daveyx0.primitivemobs.client.renderer.entity.RenderVoidEye;
import net.daveyx0.primitivemobs.entity.monster.EntityVoidEye;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerVoidEyeSeen extends RenderLayer<EntityVoidEye, ModelVoidEye<EntityVoidEye>> {
   private static final ResourceLocation VOIDEYESEEN_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/voideye/voideye_glow.png");
   private final ModelVoidEye<EntityVoidEye> voideyeModel;

   public LayerVoidEyeSeen(RenderVoidEye voideyeRendererIn, EntityRendererProvider.Context context) {
      super(voideyeRendererIn);
      this.voideyeModel = new ModelVoidEye<EntityVoidEye>(context.bakeLayer(RenderVoidEye.SEEN_LAYER), false);
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityVoidEye entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (entitylivingbaseIn.canSeeTarget()) {
         this.getParentModel().copyPropertiesTo(this.voideyeModel);
         this.voideyeModel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
         this.voideyeModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
         VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(VOIDEYESEEN_TEXTURES));
         this.voideyeModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
      }
   }
}

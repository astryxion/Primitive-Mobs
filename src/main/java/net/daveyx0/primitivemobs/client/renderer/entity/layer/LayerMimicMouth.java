package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.client.models.ModelMimic;
import net.daveyx0.primitivemobs.client.renderer.entity.RenderMimic;
import net.daveyx0.primitivemobs.entity.monster.EntityMimic;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerMimicMouth extends RenderLayer<EntityMimic, ModelMimic<EntityMimic>> {
   private static final ResourceLocation MIMICMOUTH_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/mimic/chest.png");
   private final ModelMimic<EntityMimic> mimicModel;

   public LayerMimicMouth(RenderMimic mimicRendererIn, EntityRendererProvider.Context context) {
      super(mimicRendererIn);
      this.mimicModel = new ModelMimic<EntityMimic>(context.bakeLayer(RenderMimic.MOUTH_LAYER));
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityMimic entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      this.getParentModel().copyPropertiesTo(this.mimicModel);
      this.mimicModel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
      this.mimicModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(MIMICMOUTH_TEXTURES));
      this.mimicModel.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
   }
}

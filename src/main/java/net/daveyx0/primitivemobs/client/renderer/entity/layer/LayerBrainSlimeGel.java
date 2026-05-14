package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.client.models.ModelBrainSlime;
import net.daveyx0.primitivemobs.client.renderer.entity.RenderBrainSlime;
import net.daveyx0.primitivemobs.entity.monster.EntityBrainSlime;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerBrainSlimeGel extends RenderLayer<EntityBrainSlime, ModelBrainSlime<EntityBrainSlime>> {
   private static final ResourceLocation SLIME_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/brainslime/slime_brain.png");
   private final ModelBrainSlime<EntityBrainSlime> slimeModel;

   public LayerBrainSlimeGel(RenderBrainSlime slimeRendererIn, EntityRendererProvider.Context context) {
      super(slimeRendererIn);
      this.slimeModel = new ModelBrainSlime<EntityBrainSlime>(context.bakeLayer(RenderBrainSlime.OUTER_LAYER), false);
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityBrainSlime entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!entitylivingbaseIn.isInvisible()) {
         this.getParentModel().copyPropertiesTo(this.slimeModel);
         this.slimeModel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
         this.slimeModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
         VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(SLIME_TEXTURES));
         this.slimeModel.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), -1);
      }
   }
}

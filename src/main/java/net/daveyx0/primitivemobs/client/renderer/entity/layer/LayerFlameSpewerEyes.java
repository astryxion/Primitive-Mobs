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
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerFlameSpewerEyes extends RenderLayer<EntityFlameSpewer, ModelFlameSpewer> {
   private static final ResourceLocation FLAMESPEWER_EYES_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/flamespewer/flamespewer_lava.png");
   private final ModelFlameSpewer model;

   public LayerFlameSpewerEyes(RenderFlameSpewer spewerRenderer, EntityRendererProvider.Context context) {
      super(spewerRenderer);
      this.model = new ModelFlameSpewer(context.bakeLayer(RenderFlameSpewer.EYES_LAYER), false, true);
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityFlameSpewer spewer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      float alpha = (10.0F - (float)spewer.getAttackTime()) / 10.0F;
      this.getParentModel().copyPropertiesTo(this.model);
      this.model.prepareMobModel(spewer, limbSwing, limbSwingAmount, partialTicks);
      this.model.setupAnim(spewer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(FLAMESPEWER_EYES_TEXTURES));
      int color = FastColor.ARGB32.color((int)(alpha * 255.0F), 255, 255, 255);
      this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(spewer, 0.0F), color);
   }
}

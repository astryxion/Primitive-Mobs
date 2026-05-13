package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.client.models.ModelGroveSprite;
import net.daveyx0.primitivemobs.client.renderer.entity.RenderGroveSprite;
import net.daveyx0.primitivemobs.entity.passive.EntityGroveSprite;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerGroveSpriteStump extends RenderLayer<EntityGroveSprite, ModelGroveSprite> {
   private static final ResourceLocation GROVESTUMP_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/grovesprite/grovestump.png");
   private final ModelGroveSprite spriteModel;

   public LayerGroveSpriteStump(RenderGroveSprite spriteRendererIn, EntityRendererProvider.Context context) {
      super(spriteRendererIn);
      this.spriteModel = new ModelGroveSprite(context.bakeLayer(RenderGroveSprite.STUMP_LAYER), false);
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityGroveSprite entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      float[] RGB = entitylivingbaseIn.getLogTopRGB();
      this.getParentModel().copyPropertiesTo(this.spriteModel);
      this.spriteModel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
      this.spriteModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(GROVESTUMP_TEXTURES));
      this.spriteModel.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), RGB[0] / 255.0F, RGB[1] / 255.0F, RGB[2] / 255.0F, 1.0F);
   }
}

package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.client.models.ModelPrimitiveCreeper;
import net.daveyx0.primitivemobs.client.renderer.entity.RenderPrimitiveCreeper;
import net.daveyx0.primitivemobs.entity.monster.EntityPrimitiveCreeper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerPrimitiveCreeperCharge extends RenderLayer<EntityPrimitiveCreeper, ModelPrimitiveCreeper> {
   private static final ResourceLocation LIGHTNING_TEXTURE = ResourceLocation.parse("textures/entity/creeper/creeper_armor.png");
   private final ModelPrimitiveCreeper creeperModel;

   public LayerPrimitiveCreeperCharge(RenderPrimitiveCreeper creeperRendererIn, EntityRendererProvider.Context context) {
      super(creeperRendererIn);
      this.creeperModel = new ModelPrimitiveCreeper(context.bakeLayer(RenderPrimitiveCreeper.CHARGE_LAYER));
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityPrimitiveCreeper entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (entitylivingbaseIn.isPowered()) {
         float f = (float)entitylivingbaseIn.tickCount + partialTicks;
         this.getParentModel().copyPropertiesTo(this.creeperModel);
         this.creeperModel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
         this.creeperModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
         VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.energySwirl(LIGHTNING_TEXTURE, f * 0.01F, f * 0.01F));
         this.creeperModel.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), -8421505);
      }
   }
}

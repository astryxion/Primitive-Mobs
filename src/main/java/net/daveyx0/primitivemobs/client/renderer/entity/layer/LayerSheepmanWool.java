package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.client.models.ModelSheepman;
import net.daveyx0.primitivemobs.client.renderer.entity.RenderSheepman;
import net.daveyx0.primitivemobs.entity.passive.EntitySheepman;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

public class LayerSheepmanWool extends RenderLayer<EntitySheepman, ModelSheepman> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("primitivemobs", "textures/entity/villager/sheepman.png");
   private final ModelSheepman sheepModel;

   public LayerSheepmanWool(RenderSheepman sheepRendererIn, EntityRendererProvider.Context context) {
      super(sheepRendererIn);
      this.sheepModel = new ModelSheepman(context.bakeLayer(RenderSheepman.WOOL_LAYER), 1);
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntitySheepman entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!entitylivingbaseIn.getSheared() && !entitylivingbaseIn.isInvisible()) {
         float r, g, b;
         if (entitylivingbaseIn.hasCustomName() && "jeb_".equals(entitylivingbaseIn.getCustomName().getString())) {
            int i = entitylivingbaseIn.tickCount / 25 + entitylivingbaseIn.getId();
            int j = DyeColor.values().length;
            int k = i % j;
            int l = (i + 1) % j;
            float f = ((float)(entitylivingbaseIn.tickCount % 25) + partialTicks) / 25.0F;
            float[] afloat1 = Sheep.getColorArray(DyeColor.byId(k));
            float[] afloat2 = Sheep.getColorArray(DyeColor.byId(l));
            r = afloat1[0] * (1.0F - f) + afloat2[0] * f;
            g = afloat1[1] * (1.0F - f) + afloat2[1] * f;
            b = afloat1[2] * (1.0F - f) + afloat2[2] * f;
         } else {
            float[] afloat = Sheep.getColorArray(entitylivingbaseIn.getFleeceColor());
            r = afloat[0];
            g = afloat[1];
            b = afloat[2];
         }

         this.getParentModel().copyPropertiesTo(this.sheepModel);
         this.sheepModel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
         this.sheepModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
         VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
         this.sheepModel.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), r, g, b, 1.0F);
      }
   }
}

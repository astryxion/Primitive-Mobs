package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.daveyx0.primitivemobs.client.models.ModelBrainSlime;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerBrainSlimeGel;
import net.daveyx0.primitivemobs.entity.monster.EntityBrainSlime;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBrainSlime extends MobRenderer<EntityBrainSlime, ModelBrainSlime<EntityBrainSlime>> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "brain_slime"), "main");
   public static final ModelLayerLocation OUTER_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "brain_slime"), "outer");
   private static final ResourceLocation SLIME_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/brainslime/slime_brain.png");

   public RenderBrainSlime(EntityRendererProvider.Context context) {
      super(context, new ModelBrainSlime<EntityBrainSlime>(context.bakeLayer(MODEL_LAYER), true), 0.4F);
      this.addLayer(new LayerBrainSlimeGel(this, context));
   }

   @Override
   public void render(EntityBrainSlime entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      this.shadowRadius = 0.25F * (float)entity.getSize();
      super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
   }

   @Override
   protected void scale(EntityBrainSlime entitylivingbaseIn, PoseStack poseStack, float partialTickTime) {
      poseStack.scale(0.999F, 0.999F, 0.999F);
      float f1 = (float)entitylivingbaseIn.getSize();
      if (entitylivingbaseIn.getSaturation() >= 10) {
         f1 += ((float)entitylivingbaseIn.getSaturation() - 10.0F) / 90.0F;
      }

      float f2 = (entitylivingbaseIn.oSquish + (entitylivingbaseIn.squish - entitylivingbaseIn.oSquish) * partialTickTime) / (f1 * 0.5F + 1.0F);
      float f3 = 1.0F / (f2 + 1.0F);
      float f4 = entitylivingbaseIn.suckinge + (entitylivingbaseIn.suckingb - entitylivingbaseIn.suckinge) * partialTickTime;
      float f5 = entitylivingbaseIn.suckingd + (entitylivingbaseIn.suckingc - entitylivingbaseIn.suckingd) * partialTickTime;
      float f6 = (Mth.sin(f4) + 0.5F) * f5 * 1.5F;
      poseStack.scale(f3 * f1, 1.0F / f3 * f1 + f6, f3 * f1);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityBrainSlime entity) {
      return SLIME_TEXTURES;
   }
}

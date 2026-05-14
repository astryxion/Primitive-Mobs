package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.daveyx0.primitivemobs.client.models.ModelTreasureSlime;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerHeldItemSlime;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerTreasureSlimeGel;
import net.daveyx0.primitivemobs.entity.monster.EntityTreasureSlime;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTreasureSlime extends MobRenderer<EntityTreasureSlime, ModelTreasureSlime<EntityTreasureSlime>> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("primitivemobs", "treasure_slime"), "main");
   public static final ModelLayerLocation OUTER_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("primitivemobs", "treasure_slime"), "outer");
   private static final ResourceLocation SLIME_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/treasureslime/slime_treasure.png");

   public RenderTreasureSlime(EntityRendererProvider.Context context) {
      super(context, new ModelTreasureSlime<EntityTreasureSlime>(context.bakeLayer(MODEL_LAYER), true), 0.4F);
      this.addLayer(new LayerHeldItemSlime(this));
      this.addLayer(new LayerTreasureSlimeGel(this, context));
   }

   @Override
   public void render(EntityTreasureSlime entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      this.shadowRadius = 0.25F * (float)entity.getSize();
      super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
   }

   @Override
   protected void scale(EntityTreasureSlime entitylivingbaseIn, PoseStack poseStack, float partialTickTime) {
      poseStack.scale(0.999F, 0.999F, 0.999F);
      float f1 = (float)entitylivingbaseIn.getSize();
      float f2 = (entitylivingbaseIn.oSquish + (entitylivingbaseIn.squish - entitylivingbaseIn.oSquish) * partialTickTime) / (f1 * 0.5F + 1.0F);
      float f3 = 1.0F / (f2 + 1.0F);
      poseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityTreasureSlime entity) {
      return SLIME_TEXTURES;
   }
}

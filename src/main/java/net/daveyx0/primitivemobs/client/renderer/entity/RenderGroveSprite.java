package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.daveyx0.primitivemobs.client.models.ModelGroveSprite;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerGroveSpriteLeaves;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerGroveSpriteStump;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerHeldItemCustom;
import net.daveyx0.primitivemobs.entity.passive.EntityGroveSprite;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unchecked")
public class RenderGroveSprite extends MobRenderer<EntityGroveSprite, ModelGroveSprite> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "grove_sprite"), "main");
   public static final ModelLayerLocation STUMP_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "grove_sprite"), "stump");
   public static final ModelLayerLocation LEAVES_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "grove_sprite"), "leaves");
   private static final ResourceLocation GROVEBASE_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/grovesprite/grovebase.png");
   private static final ResourceLocation GROVEBASE_CINDER_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/grovesprite/grovecinder.png");

   public RenderGroveSprite(EntityRendererProvider.Context context) {
      super(context, new ModelGroveSprite(context.bakeLayer(MODEL_LAYER), false), 0.3F);
      this.addLayer((RenderLayer) new LayerHeldItemCustom(this));
      this.addLayer(new LayerGroveSpriteLeaves(this, context));
      this.addLayer(new LayerGroveSpriteStump(this, context));
   }

   @Override
   public ResourceLocation getTextureLocation(EntityGroveSprite entity) {
      return entity.isCinderSprite() ? GROVEBASE_CINDER_TEXTURES : GROVEBASE_TEXTURES;
   }

   @Override
   public void render(EntityGroveSprite entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      float[] RGB = entity.getLogRGB();
      RenderSystem.setShaderColor(RGB[0] / 255.0F, RGB[1] / 255.0F, RGB[2] / 255.0F, 1.0F);
      super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   @Override
   protected void scale(EntityGroveSprite entitylivingbaseIn, PoseStack poseStack, float partialTickTime) {
      poseStack.translate(0.0F, 0.9F, 0.0F);
   }
}

package net.daveyx0.primitivemobs.client.renderer.entity;

import net.daveyx0.primitivemobs.client.models.ModelEmpty;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerHeldItemHaunted;
import net.daveyx0.primitivemobs.entity.monster.EntityHauntedTool;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderHauntedTool extends MobRenderer<EntityHauntedTool, ModelEmpty<EntityHauntedTool>> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "haunted_tool"), "main");
   private static final ResourceLocation FAKE_TEXTURE = new ResourceLocation("primitivemobs", "textures/entity/mimic/haunted_tool.png");

   public RenderHauntedTool(EntityRendererProvider.Context context) {
      super(context, new ModelEmpty<>(context.bakeLayer(MODEL_LAYER)), 0.2F);
      this.addLayer(new LayerHeldItemHaunted(this));
   }

   protected float getFlipDegrees(EntityHauntedTool entityHauntedPickaxe) {
      return 90.0F;
   }

   @Override
   public ResourceLocation getTextureLocation(EntityHauntedTool entity) {
      return FAKE_TEXTURE;
   }
}

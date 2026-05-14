package net.daveyx0.primitivemobs.client.renderer.entity;

import net.daveyx0.primitivemobs.client.models.ModelFlameSpewer;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerFlameSpewerEyes;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerLavaSkin;
import net.daveyx0.primitivemobs.entity.monster.EntityFlameSpewer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderFlameSpewer extends MobRenderer<EntityFlameSpewer, ModelFlameSpewer> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("primitivemobs", "flame_spewer"), "main");
   public static final ModelLayerLocation EYES_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("primitivemobs", "flame_spewer"), "eyes");
   public static final ModelLayerLocation LAVA_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("primitivemobs", "flame_spewer"), "lava");
   private static final ResourceLocation FLAMESPEWER_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/flamespewer/flamespewer.png");

   public RenderFlameSpewer(EntityRendererProvider.Context context) {
      super(context, new ModelFlameSpewer(context.bakeLayer(MODEL_LAYER), false, true), 0.5F);
      this.addLayer(new LayerLavaSkin(this, context));
      this.addLayer(new LayerFlameSpewerEyes(this, context));
   }

   @Override
   public ResourceLocation getTextureLocation(EntityFlameSpewer entity) {
      return FLAMESPEWER_TEXTURES;
   }
}

package net.daveyx0.primitivemobs.client.renderer.entity;

import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerHeldItemCustom;
import net.daveyx0.primitivemobs.entity.monster.EntitySkeletonWarrior;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings("unchecked")
@OnlyIn(Dist.CLIENT)
public class RenderSkeletonWarrior extends HumanoidMobRenderer<EntitySkeletonWarrior, SkeletonModel<EntitySkeletonWarrior>> {
   private static final ResourceLocation SKELETONWARRIOR_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/skeletonwarrior/skeletonwarrior.png");

   public RenderSkeletonWarrior(EntityRendererProvider.Context context) {
      super(context, new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON)), 0.5F);
      this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
      this.addLayer(new HumanoidArmorLayer<>(this,
              new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON_INNER_ARMOR)),
              new SkeletonModel<>(context.bakeLayer(ModelLayers.SKELETON_OUTER_ARMOR)),
              context.getModelManager()));
      this.addLayer((RenderLayer) new LayerHeldItemCustom(this));
   }

   @Override
   public ResourceLocation getTextureLocation(EntitySkeletonWarrior entity) {
      return SKELETONWARRIOR_TEXTURES;
   }
}

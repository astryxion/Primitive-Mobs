package net.daveyx0.primitivemobs.client.renderer.entity;

import net.daveyx0.primitivemobs.client.models.ModelHarpy;
import net.daveyx0.primitivemobs.entity.monster.EntityHarpy;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderHarpy extends MobRenderer<EntityHarpy, ModelHarpy<EntityHarpy>> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("primitivemobs", "harpy"), "main");
   private static final ResourceLocation HARPY_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/harpy/harpy.png");

   public RenderHarpy(EntityRendererProvider.Context context) {
      super(context, new ModelHarpy<>(context.bakeLayer(MODEL_LAYER)), 0.5F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityHarpy entity) {
      return HARPY_TEXTURES;
   }

   @Override
   protected float getBob(EntityHarpy livingBase, float partialTicks) {
      return this.getCustomBob(livingBase, partialTicks);
   }

   private float getCustomBob(EntityHarpy harpy, float p_192861_2_) {
      float f = harpy.oFlap + (harpy.flap - harpy.oFlap) * p_192861_2_;
      float f1 = harpy.oFlapSpeed + (harpy.flapSpeed - harpy.oFlapSpeed) * p_192861_2_;
      return (Mth.sin(f) + 1.0F) * f1;
   }
}

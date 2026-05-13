package net.daveyx0.primitivemobs.client.renderer.entity;

import net.daveyx0.primitivemobs.client.models.ModelDodo;
import net.daveyx0.primitivemobs.entity.passive.EntityDodo;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderDodo extends MobRenderer<EntityDodo, ModelDodo<EntityDodo>> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "dodo"), "main");
   private static final ResourceLocation DODO_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/rareanimals/dodo.png");

   public RenderDodo(EntityRendererProvider.Context context) {
      super(context, new ModelDodo<>(context.bakeLayer(MODEL_LAYER)), 0.4F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityDodo entity) {
      return DODO_TEXTURES;
   }

   @Override
   protected float getBob(EntityDodo livingBase, float partialTicks) {
      float f = livingBase.oFlap + (livingBase.flap - livingBase.oFlap) * partialTicks;
      float f1 = livingBase.oFlapSpeed + (livingBase.flapSpeed - livingBase.oFlapSpeed) * partialTicks;
      return (Mth.sin(f) + 1.0F) * f1;
   }
}

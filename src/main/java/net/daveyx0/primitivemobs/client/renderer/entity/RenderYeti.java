package net.daveyx0.primitivemobs.client.renderer.entity;

import net.daveyx0.primitivemobs.client.models.ModelYeti;
import net.daveyx0.primitivemobs.entity.monster.EntityYeti;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderYeti extends MobRenderer<EntityYeti, ModelYeti> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "yeti"), "main");
   private static final ResourceLocation YETI_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/yeti/yeti.png");

   public RenderYeti(EntityRendererProvider.Context context) {
      super(context, new ModelYeti(context.bakeLayer(MODEL_LAYER)), 0.9F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityYeti entity) {
      return YETI_TEXTURES;
   }
}

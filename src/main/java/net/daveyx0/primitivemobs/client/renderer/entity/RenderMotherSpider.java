package net.daveyx0.primitivemobs.client.renderer.entity;

import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerPrimitiveSpiderEyes;
import net.daveyx0.primitivemobs.entity.monster.EntityMotherSpider;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderMotherSpider<T extends EntityMotherSpider> extends MobRenderer<T, SpiderModel<T>> {
   private static final ResourceLocation MOTHER_SPIDER_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/spiderfamily/motherspider.png");

   public RenderMotherSpider(EntityRendererProvider.Context context) {
      super(context, new SpiderModel<>(context.bakeLayer(ModelLayers.SPIDER)), 0.3F);
      this.addLayer(new LayerPrimitiveSpiderEyes<>(this));
   }

   protected float getFlipDegrees(T entityLivingBaseIn) {
      return 180.0F;
   }

   @Override
   public ResourceLocation getTextureLocation(T entity) {
      return MOTHER_SPIDER_TEXTURES;
   }
}

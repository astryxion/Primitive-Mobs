package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerBabySpiderEyes;
import net.daveyx0.primitivemobs.entity.monster.EntityBabySpider;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBabySpider<T extends EntityBabySpider> extends MobRenderer<T, SpiderModel<T>> {
   private static final ResourceLocation BABY_SPIDER_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/spiderfamily/babyspider.png");
   private static final ResourceLocation SPIDER_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/spiderfamily/spider.png");

   public RenderBabySpider(EntityRendererProvider.Context context) {
      super(context, new SpiderModel<>(context.bakeLayer(ModelLayers.SPIDER)), 0.3F);
      this.addLayer(new LayerBabySpiderEyes<>(this));
   }

   protected float getFlipDegrees(T entityLivingBaseIn) {
      return 180.0F;
   }

   @Override
   protected void scale(T entitybabyspider, PoseStack poseStack, float partialTickTime) {
      switch (entitybabyspider.getGrowthLevel()) {
         case 0:
            poseStack.scale(0.5F, 0.5F, 0.5F);
            break;
         case 1:
            poseStack.scale(0.6F, 0.6F, 0.6F);
            break;
         case 2:
            poseStack.scale(0.7F, 0.7F, 0.7F);
            break;
         case 3:
            poseStack.scale(0.8F, 0.8F, 0.8F);
            break;
         case 4:
            poseStack.scale(0.9F, 0.9F, 0.9F);
            break;
         case 5:
            poseStack.scale(1.0F, 1.0F, 1.0F);
      }
   }

   @Override
   public ResourceLocation getTextureLocation(T entity) {
      return entity.getGrowthLevel() >= 5 ? SPIDER_TEXTURES : BABY_SPIDER_TEXTURES;
   }
}

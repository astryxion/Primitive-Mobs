package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.daveyx0.primitivemobs.client.models.ModelLostMiner;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerHeldItemCustom;
import net.daveyx0.primitivemobs.entity.passive.EntityLostMiner;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("unchecked")
@OnlyIn(Dist.CLIENT)
public class RenderLostMiner extends MobRenderer<EntityLostMiner, ModelLostMiner> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "lost_miner"), "main");
   private static final ResourceLocation LOST_MINER_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/villager/lostminer.png");

   public RenderLostMiner(EntityRendererProvider.Context context) {
      super(context, new ModelLostMiner(context.bakeLayer(MODEL_LAYER)), 0.5F);
      this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
      this.addLayer((RenderLayer) new LayerHeldItemCustom(this));
   }

   @SuppressWarnings("unchecked")
   public ModelLostMiner getMainModel() {
      return (ModelLostMiner) super.getModel();
   }

   @Override
   public ResourceLocation getTextureLocation(EntityLostMiner entity) {
      return LOST_MINER_TEXTURES;
   }

   @Override
   protected void scale(EntityLostMiner entitylivingbaseIn, PoseStack poseStack, float partialTickTime) {
      float f = 0.9375F;
      if (entitylivingbaseIn.isBaby()) {
         f = (float)((double)f * (double)0.5F);
         this.shadowRadius = 0.25F;
      } else {
         this.shadowRadius = 0.5F;
      }

      poseStack.scale(f, f, f);
   }
}

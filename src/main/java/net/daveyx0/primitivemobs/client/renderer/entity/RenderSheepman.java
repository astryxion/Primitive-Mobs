package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.daveyx0.primitivemobs.client.models.ModelSheepman;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerSheepmanWool;
import net.daveyx0.primitivemobs.entity.passive.EntitySheepman;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderSheepman extends MobRenderer<EntitySheepman, ModelSheepman> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "sheepman"), "main");
   public static final ModelLayerLocation WOOL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "sheepman"), "wool");
   private static final ResourceLocation SHEEPMAN_TEXTURE = new ResourceLocation("primitivemobs", "textures/entity/villager/sheepman.png");

   public RenderSheepman(EntityRendererProvider.Context context) {
      super(context, new ModelSheepman(context.bakeLayer(MODEL_LAYER), 0), 0.5F);
      this.addLayer(new LayerSheepmanWool(this, context));
   }

   @SuppressWarnings("unchecked")
   public ModelSheepman getMainModel() {
      return (ModelSheepman) super.getModel();
   }

   @Override
   public ResourceLocation getTextureLocation(EntitySheepman entity) {
      return SHEEPMAN_TEXTURE;
   }

   @Override
   protected void scale(EntitySheepman entitylivingbaseIn, PoseStack poseStack, float partialTickTime) {
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

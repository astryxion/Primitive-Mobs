package net.daveyx0.primitivemobs.client.renderer.entity;

import java.util.Calendar;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.daveyx0.primitivemobs.client.models.ModelMimic;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerMimicMouth;
import net.daveyx0.primitivemobs.entity.monster.EntityMimic;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderMimic extends MobRenderer<EntityMimic, ModelMimic<EntityMimic>> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "mimic"), "main");
   public static final ModelLayerLocation MOUTH_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "mimic"), "mouth");
   private static final ResourceLocation CHEST_TEXTURES = new ResourceLocation("minecraft", "textures/entity/chest/normal.png");
   private static final ResourceLocation CHEST_CHRISTMAS_TEXTURES = new ResourceLocation("minecraft", "textures/entity/chest/christmas.png");
   private boolean isChristmas;

   public RenderMimic(EntityRendererProvider.Context context) {
      super(context, new ModelMimic<>(context.bakeLayer(MODEL_LAYER)), 0.45F);
      this.addLayer(new LayerMimicMouth(this, context));
      Calendar calendar = Calendar.getInstance();
      if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) {
         this.isChristmas = true;
      }
   }

   protected void isInAir(EntityMimic entitymimic, PoseStack poseStack) {
      if (!entitymimic.onGround()) {
         if (entitymimic.getDeltaMovement().y > 0.1 && entitymimic.getDeltaMovement().y < (double)0.5F) {
            poseStack.mulPose(Axis.XN.rotationDegrees(25.0F));
         } else {
            poseStack.mulPose(Axis.XN.rotationDegrees((float)(entitymimic.getDeltaMovement().y * (double)70.0F)));
         }
      }
   }

   @Override
   protected float getBob(EntityMimic mimic, float f) {
      float f1 = mimic.nomminge + (mimic.nommingb - mimic.nomminge) * f;
      float f2 = mimic.nommingd + (mimic.nommingc - mimic.nommingd) * f;
      return (Mth.sin(f1) + 0.2F) * f2 + mimic.rotation;
   }

   @Override
   protected void scale(EntityMimic p_77041_1_, PoseStack poseStack, float p_77041_2_) {
      this.isInAir(p_77041_1_, poseStack);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityMimic mimic) {
      return this.isChristmas ? CHEST_CHRISTMAS_TEXTURES : CHEST_TEXTURES;
   }
}

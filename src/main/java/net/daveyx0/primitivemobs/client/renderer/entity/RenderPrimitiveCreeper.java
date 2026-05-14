package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.daveyx0.primitivemobs.client.models.ModelPrimitiveCreeper;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerPrimitiveCreeperCharge;
import net.daveyx0.primitivemobs.entity.monster.EntityFestiveCreeper;
import net.daveyx0.primitivemobs.entity.monster.EntityPrimitiveCreeper;
import net.daveyx0.primitivemobs.entity.monster.EntityRocketCreeper;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderPrimitiveCreeper extends MobRenderer<EntityPrimitiveCreeper, ModelPrimitiveCreeper> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("primitivemobs", "primitive_creeper"), "main");
   public static final ModelLayerLocation CHARGE_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("primitivemobs", "primitive_creeper"), "charge");
   private static final ResourceLocation ROCKETCREEPER_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/rocketcreeper/rocketcreeper.png");
   private static final ResourceLocation FESTIVECREEPER_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/festivecreeper/festivecreeper.png");
   private static final ResourceLocation SUPPORTCREEPER_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/supportcreeper/supportcreeper.png");

   public RenderPrimitiveCreeper(EntityRendererProvider.Context context) {
      super(context, new ModelPrimitiveCreeper(context.bakeLayer(MODEL_LAYER)), 0.5F);
      this.addLayer(new LayerPrimitiveCreeperCharge(this, context));
   }

   @Override
   protected void scale(EntityPrimitiveCreeper entitylivingbaseIn, PoseStack poseStack, float partialTickTime) {
      float f = entitylivingbaseIn.getSwelling(partialTickTime);
      float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
      f = Mth.clamp(f, 0.0F, 1.0F);
      f *= f;
      f *= f;
      float f2 = (1.0F + f * 0.4F) * f1;
      float f3 = (1.0F + f * 0.1F) / f1;
      poseStack.scale(f2, f3, f2);
      if (entitylivingbaseIn instanceof EntityRocketCreeper) {
         EntityRocketCreeper creeper = (EntityRocketCreeper)entitylivingbaseIn;
         if (!creeper.onGround() && creeper.isRocket()) {
            if (creeper.getDeltaMovement().y > 0.1 && creeper.getDeltaMovement().y < (double)0.5F) {
               poseStack.mulPose(Axis.XN.rotationDegrees(25.0F));
            } else {
               poseStack.mulPose(Axis.XN.rotationDegrees((float)(creeper.getDeltaMovement().y * (double)175.0F)));
            }
         }
      }
   }

   @Override
   protected float getWhiteOverlayProgress(EntityPrimitiveCreeper entitylivingbaseIn, float partialTickTime) {
      float f = entitylivingbaseIn.getSwelling(partialTickTime);
      if ((int)(f * 10.0F) % 2 == 0) {
         return 0.0F;
      } else {
         return Mth.clamp(f, 0.5F, 1.0F);
      }
   }

   @Override
   public ResourceLocation getTextureLocation(EntityPrimitiveCreeper entity) {
      if (entity instanceof EntityFestiveCreeper) {
         return FESTIVECREEPER_TEXTURES;
      } else {
         return entity instanceof EntityRocketCreeper ? ROCKETCREEPER_TEXTURES : SUPPORTCREEPER_TEXTURES;
      }
   }
}

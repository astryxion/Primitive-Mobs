package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.daveyx0.primitivemobs.client.models.ModelEnchantedBook;
import net.daveyx0.primitivemobs.entity.monster.EntityEnchantedBook;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderEchantedBook extends MobRenderer<EntityEnchantedBook, ModelEnchantedBook<EntityEnchantedBook>> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("primitivemobs", "enchanted_book"), "main");
   private static final ResourceLocation BOOK_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/enchantedbook/book.png");

   public RenderEchantedBook(EntityRendererProvider.Context context) {
      super(context, new ModelEnchantedBook<>(context.bakeLayer(MODEL_LAYER)), 0.4F);
   }

   @Override
   protected void scale(EntityEnchantedBook entityliving, PoseStack poseStack, float f) {
      float f1 = entityliving.floatinge + (entityliving.floatingb - entityliving.floatinge) * f;
      float f2 = entityliving.floatingd + (entityliving.floatingc - entityliving.floatingd) * f;
      float f3 = (Mth.sin(f1) + 0.5F) * f2 * 1.5F;
      float var6 = f3 - 0.8F;
      poseStack.translate(0.0F, var6, 0.0F);
      poseStack.mulPose(Axis.XP.rotationDegrees(-30.0F));
   }

   @Override
   protected float getBob(EntityEnchantedBook entityDEnchantedBook, float f) {
      float f1 = entityDEnchantedBook.floatinge + (entityDEnchantedBook.floatingb - entityDEnchantedBook.floatinge) * f;
      float f2 = entityDEnchantedBook.floatingd + (entityDEnchantedBook.floatingc - entityDEnchantedBook.floatingd) * f;
      return (Mth.sin(f1) + 0.2F) * f2;
   }

   @Override
   public ResourceLocation getTextureLocation(EntityEnchantedBook entity) {
      return BOOK_TEXTURES;
   }
}

package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.daveyx0.primitivemobs.client.models.ModelEmpty;
import net.daveyx0.primitivemobs.entity.monster.EntityHauntedTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerHeldItemHaunted extends RenderLayer<EntityHauntedTool, ModelEmpty<EntityHauntedTool>> {

   public LayerHeldItemHaunted(LivingEntityRenderer<EntityHauntedTool, ModelEmpty<EntityHauntedTool>> toolRendererIn) {
      super(toolRendererIn);
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityHauntedTool entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      ItemStack itemstack = entity.getMainHandItem();
      float f1 = entity.floatinge + (entity.floatingb - entity.floatinge) * limbSwing;
      float f2 = entity.floatingd + (entity.floatingc - entity.floatingd) * limbSwing;
      float f3 = (Mth.sin(f1) + 0.5F) * f2 * 1.5F;
      float var6 = f3 + 0.5F;
      if (!itemstack.isEmpty()) {
         poseStack.pushPose();
         poseStack.translate(0.0, 0.0, -0.75);
         poseStack.translate(0.0, (double)(var6 - 1.0F), 0.0);
         poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
         poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
         Item item = itemstack.getItem();
         if (item instanceof BlockItem) {
            poseStack.translate(0.0F, 0.0625F, -0.25F);
            poseStack.scale(0.375F, -0.375F, 0.375F);
         } else {
            poseStack.translate(0.0F, -1.35F, 0.0F);
            poseStack.scale(0.7F, 0.7F, 0.7F);
         }

         Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemstack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, poseStack, bufferSource, entity.level(), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), entity.getId());
         poseStack.popPose();
      }
   }
}

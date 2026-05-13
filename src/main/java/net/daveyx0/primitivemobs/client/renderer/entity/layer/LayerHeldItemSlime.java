package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.daveyx0.primitivemobs.client.models.ModelTreasureSlime;
import net.daveyx0.primitivemobs.entity.monster.EntityTreasureSlime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerHeldItemSlime extends RenderLayer<EntityTreasureSlime, ModelTreasureSlime<EntityTreasureSlime>> {

   public LayerHeldItemSlime(LivingEntityRenderer<EntityTreasureSlime, ModelTreasureSlime<EntityTreasureSlime>> slimeRendererIn) {
      super(slimeRendererIn);
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityTreasureSlime entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      ItemStack itemstack = entitylivingbaseIn.getMainHandItem();
      if (!itemstack.isEmpty()) {
         poseStack.pushPose();
         poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
         poseStack.translate(0.0, 0.0, 0.0);
         Item item = itemstack.getItem();
         if (item instanceof BlockItem) {
            poseStack.translate(0.0F, -1.15F, 0.0F);
            poseStack.scale(0.525F, -0.525F, 0.525F);
         } else {
            poseStack.translate(0.0F, -1.35F, 0.0F);
            poseStack.scale(0.7F, 0.7F, 0.7F);
         }

         Minecraft.getInstance().getItemRenderer().renderStatic(entitylivingbaseIn, itemstack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, poseStack, bufferSource, entitylivingbaseIn.level(), packedLight, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), entitylivingbaseIn.getId());
         if (bufferSource instanceof MultiBufferSource.BufferSource immediate) {
            immediate.endBatch();
         }
         poseStack.popPose();
      }
   }
}

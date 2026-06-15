package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.daveyx0.primitivemobs.client.models.ModelFilchLizard;
import net.daveyx0.primitivemobs.client.models.ModelGroveSprite;
import net.daveyx0.primitivemobs.client.models.ModelLilyLurker;
import net.daveyx0.primitivemobs.client.models.ModelLostMiner;
import net.daveyx0.primitivemobs.client.models.ModelTrollager;
import net.daveyx0.primitivemobs.entity.monster.EntityLilyLurker;
import net.daveyx0.primitivemobs.entity.monster.EntitySkeletonWarrior;
import net.daveyx0.primitivemobs.entity.monster.EntityTrollager;
import net.daveyx0.primitivemobs.entity.passive.EntityFilchLizard;
import net.daveyx0.primitivemobs.entity.passive.EntityGroveSprite;
import net.daveyx0.primitivemobs.entity.passive.EntityLostMiner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("unchecked")
@OnlyIn(Dist.CLIENT)
public class LayerHeldItemCustom extends RenderLayer<LivingEntity, EntityModel<LivingEntity>> {
   protected final LivingEntityRenderer<?, ?> livingEntityRenderer;

   public LayerHeldItemCustom(LivingEntityRenderer<?, ?> livingEntityRendererIn) {
      super((LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) (LivingEntityRenderer<?, ?>) livingEntityRendererIn);
      this.livingEntityRenderer = livingEntityRendererIn;
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      ItemStack itemstack = entitylivingbaseIn.getMainHandItem();
      if (!itemstack.isEmpty() || entitylivingbaseIn instanceof EntityTrollager troll && troll.getAnimationState() == 1) {
         poseStack.pushPose();
         if (this.getParentModel().young) {
            poseStack.translate(0.0F, 0.625F, 0.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(20.0F));
            poseStack.scale(0.5F, 0.5F, 0.5F);
         }

         if (entitylivingbaseIn instanceof EntityGroveSprite) {
            EntityGroveSprite sprite = (EntityGroveSprite)entitylivingbaseIn;
            if (sprite.getSaplingAmount() > 0) {
               ModelGroveSprite spriteModel = (ModelGroveSprite)this.livingEntityRenderer.getModel();
               this.renderHeldItemSprite(poseStack, bufferSource, packedLight, entitylivingbaseIn, itemstack, spriteModel.bipedRightArm);
            }
         } else if (entitylivingbaseIn instanceof EntityFilchLizard) {
            EntityFilchLizard lizard = (EntityFilchLizard)entitylivingbaseIn;
            if (!lizard.getMainHandItem().isEmpty()) {
               ModelFilchLizard lizardModel = (ModelFilchLizard)this.livingEntityRenderer.getModel();
               this.renderHeldItemLizard(poseStack, bufferSource, packedLight, entitylivingbaseIn, itemstack, lizardModel.leg1);
            }
         } else if (entitylivingbaseIn instanceof EntitySkeletonWarrior) {
            EntitySkeletonWarrior warrior = (EntitySkeletonWarrior)entitylivingbaseIn;
            if (!warrior.getBackItem().isEmpty()) {
               itemstack = warrior.getBackItem();
               SkeletonModel<?> skeletonModel = (SkeletonModel<?>)this.livingEntityRenderer.getModel();
               this.renderBackItemSkeletonWarrior(poseStack, bufferSource, packedLight, entitylivingbaseIn, itemstack, skeletonModel.body);
            }
         } else if (entitylivingbaseIn instanceof EntityLilyLurker) {
            EntityLilyLurker lurker = (EntityLilyLurker)entitylivingbaseIn;
            if (!lurker.getMainHandItem().isEmpty()) {
               itemstack = lurker.getMainHandItem();
               ModelLilyLurker<?> lurkerModel = (ModelLilyLurker<?>)this.livingEntityRenderer.getModel();
               this.renderLily(poseStack, bufferSource, packedLight, entitylivingbaseIn, itemstack, lurkerModel.root1);
            }
         } else if (entitylivingbaseIn instanceof EntityTrollager trollager) {
            BlockState state = trollager.level().getBlockState(trollager.getThrownBlock());
            if (state != null && state != trollager.level().getBlockState(BlockPos.ZERO)) {
               itemstack = new ItemStack(state.getBlock());
               ModelTrollager trollModel = (ModelTrollager)this.livingEntityRenderer.getModel();
               this.renderThrownBlock(poseStack, bufferSource, packedLight, entitylivingbaseIn, itemstack, trollModel.blockHolder);
            }
         } else if (entitylivingbaseIn instanceof EntityLostMiner) {
            EntityLostMiner miner = (EntityLostMiner)entitylivingbaseIn;
            ModelLostMiner minerModel = (ModelLostMiner)this.livingEntityRenderer.getModel();
            this.renderHeldItemLostMiner(poseStack, bufferSource, packedLight, entitylivingbaseIn, itemstack, minerModel.armRightHand, false);
         }

         poseStack.popPose();
      }
   }

   private void renderHeldItemSprite(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, ItemStack itemStack, ModelPart renderer) {
      if (!itemStack.isEmpty()) {
         poseStack.pushPose();
         if (entity.isShiftKeyDown()) {
            poseStack.translate(0.0F, 0.2F, 0.0F);
         }

         renderer.translateAndRotate(poseStack);
         poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
         poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
         boolean flag = true;
         poseStack.translate((float)(flag ? -1 : 1) / 16.0F + 0.1F, 0.125F, -0.275F);
         Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, flag, poseStack, bufferSource, entity.level(), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), entity.getId());
         poseStack.popPose();
      }
   }

   private void renderHeldItemLostMiner(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, ItemStack itemStack, ModelPart renderer, boolean state) {
      if (!itemStack.isEmpty()) {
         if (state) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YN.rotationDegrees(90.0F));
            poseStack.translate(0.25F, 0.15F, -0.1F);
            Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, true, poseStack, bufferSource, entity.level(), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), entity.getId());
            poseStack.popPose();
         } else {
            poseStack.pushPose();
            renderer.translateAndRotate(poseStack);
            poseStack.translate(-0.25F, -0.5F, -0.1F);
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, true, poseStack, bufferSource, entity.level(), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), entity.getId());
            poseStack.popPose();
         }
      }
   }

   private void renderHeldItemLizard(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, ItemStack itemStack, ModelPart renderer) {
      if (!itemStack.isEmpty()) {
         poseStack.pushPose();
         if (entity.isShiftKeyDown()) {
            poseStack.translate(0.0F, 0.2F, 0.0F);
         }

         poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
         poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
         poseStack.mulPose(Axis.ZP.rotationDegrees(20.0F));
         boolean flag = true;
         poseStack.translate(-0.55F, -1.0F, -0.05F);
         Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, flag, poseStack, bufferSource, entity.level(), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), entity.getId());
         poseStack.popPose();
      }
   }

   private void renderBackItemSkeletonWarrior(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, ItemStack itemStack, ModelPart renderer) {
      if (!itemStack.isEmpty()) {
         poseStack.pushPose();
         if (entity.isShiftKeyDown()) {
            poseStack.translate(0.0F, 0.2F, 0.0F);
         }

         poseStack.mulPose(Axis.YN.rotationDegrees(90.0F));
         boolean flag = true;
         poseStack.translate(0.1F, 0.15F, -0.1F);
         Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, flag, poseStack, bufferSource, entity.level(), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), entity.getId());
         poseStack.popPose();
      }
   }

   private void renderLily(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, ItemStack itemStack, ModelPart renderer) {
      if (!itemStack.isEmpty()) {
         poseStack.pushPose();
         poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
         boolean flag = true;
         poseStack.translate(-0.025F, -0.2F, -0.9F);
         poseStack.scale(2.0F, 2.0F, 2.0F);
         Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, flag, poseStack, bufferSource, entity.level(), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), entity.getId());
         poseStack.popPose();
      }
   }

   private void renderThrownBlock(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntity entity, ItemStack itemStack, ModelPart renderer) {
      if (!itemStack.isEmpty()) {
         poseStack.pushPose();
         poseStack.scale(1.5F, 1.5F, 1.5F);
         if (entity.isShiftKeyDown()) {
            poseStack.translate(0.0F, 0.2F, 0.0F);
         }

         renderer.translateAndRotate(poseStack);
         poseStack.translate(0.5F, 0.5F, 0.0F);
         Minecraft.getInstance().getItemRenderer().renderStatic(entity, itemStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, true, poseStack, bufferSource, entity.level(), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), entity.getId());
         poseStack.popPose();
      }
   }
}

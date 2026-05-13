package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.daveyx0.primitivemobs.entity.item.EntityFlameSpit;
import net.daveyx0.primitivemobs.entity.item.EntityPrimitiveThrowable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class RenderFlyingItem<T extends Entity> extends EntityRenderer<T> {
   private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

   public RenderFlyingItem(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      if (!(entity instanceof EntityFlameSpit)) {
         poseStack.pushPose();
         poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
         poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
         this.itemRenderer.renderStatic(this.getStackToRender(entity), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), entity.getId());
         poseStack.popPose();
         super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
      }
   }

   public ItemStack getStackToRender(T entityIn) {
      if (entityIn instanceof EntityPrimitiveThrowable) {
         EntityPrimitiveThrowable egg = (EntityPrimitiveThrowable)entityIn;
         return egg.getItemFromEntity();
      } else {
         return new ItemStack(Items.SNOWBALL);
      }
   }

   @Override
   public ResourceLocation getTextureLocation(T entity) {
      return TextureAtlas.LOCATION_BLOCKS;
   }
}

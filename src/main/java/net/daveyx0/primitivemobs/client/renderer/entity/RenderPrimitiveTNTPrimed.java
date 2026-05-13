package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.daveyx0.primitivemobs.entity.item.EntityPrimitiveTNTPrimed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class RenderPrimitiveTNTPrimed extends EntityRenderer<EntityPrimitiveTNTPrimed> {
   public RenderPrimitiveTNTPrimed(EntityRendererProvider.Context context) {
      super(context);
      this.shadowRadius = 0.25F;
   }

   @Override
   public void render(EntityPrimitiveTNTPrimed entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      poseStack.pushPose();
      poseStack.translate(0.0F, 0.5F, 0.0F);
      int fuse = entity.getFuse();
      if ((float)fuse - partialTicks + 1.0F < 10.0F) {
         float f = 1.0F - ((float)fuse - partialTicks + 1.0F) / 10.0F;
         f = Mth.clamp(f, 0.0F, 1.0F);
         f *= f;
         f *= f;
         float f1 = 1.0F + f * 0.3F;
         poseStack.scale(f1, f1, f1);
      }

      poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
      poseStack.translate(-0.5F, -0.5F, 0.5F);
      if (entity.getStrength() < 3.0F) {
         poseStack.scale(0.5F, 0.5F, 0.5F);
      }

      TntMinecartRenderer.renderWhiteSolidBlock(Minecraft.getInstance().getBlockRenderer(), Blocks.TNT.defaultBlockState(), poseStack, bufferSource, packedLight, fuse / 5 % 2 == 0);
      poseStack.popPose();
      super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityPrimitiveTNTPrimed entity) {
      return TextureAtlas.LOCATION_BLOCKS;
   }
}

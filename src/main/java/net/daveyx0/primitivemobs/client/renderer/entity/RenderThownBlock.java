package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.daveyx0.primitivemobs.entity.item.EntityThrownBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class RenderThownBlock extends EntityRenderer<EntityThrownBlock> {
   public RenderThownBlock(EntityRendererProvider.Context context) {
      super(context);
      this.shadowRadius = 0.25F;
   }

   @Override
   public void render(EntityThrownBlock entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      if (entity.getOrigin() != null) {
         Level level = entity.level();
         BlockState iblockstate = level.getBlockState(entity.getOrigin());
         if (iblockstate.getRenderShape() == RenderShape.MODEL && iblockstate != level.getBlockState(entity.blockPosition()) && iblockstate.getRenderShape() != RenderShape.INVISIBLE) {
            poseStack.pushPose();
            BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
            poseStack.translate(-0.5, 0.0, -0.5);
            BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
            RenderType renderType = ItemBlockRenderTypes.getChunkRenderType(iblockstate);
            blockrendererdispatcher.getModelRenderer().tesselateBlock(level, blockrendererdispatcher.getBlockModel(iblockstate), iblockstate, blockpos, poseStack, bufferSource.getBuffer(renderType), false, RandomSource.create(), Mth.getSeed(entity.getOrigin()), OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
            super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
         }
      }
   }

   @Override
   public ResourceLocation getTextureLocation(EntityThrownBlock entity) {
      return TextureAtlas.LOCATION_BLOCKS;
   }
}

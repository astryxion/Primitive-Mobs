package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.daveyx0.primitivemobs.client.models.ModelChameleon;
import net.daveyx0.primitivemobs.entity.passive.EntityChameleon;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderChameleon extends MobRenderer<EntityChameleon, ModelChameleon> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "chameleon"), "main");
   private static final ResourceLocation CHAMELEON_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/chameleon/chameleon.png");

   public RenderChameleon(EntityRendererProvider.Context context) {
      super(context, new ModelChameleon(context.bakeLayer(MODEL_LAYER)), 0.4F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityChameleon entity) {
      return CHAMELEON_TEXTURES;
   }

   @Override
   public void render(EntityChameleon entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
   }
}

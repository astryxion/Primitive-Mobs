package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.daveyx0.primitivemobs.client.models.ModelVoidEye;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerVoidEyeSeen;
import net.daveyx0.primitivemobs.entity.monster.EntityVoidEye;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RenderVoidEye extends MobRenderer<EntityVoidEye, ModelVoidEye<EntityVoidEye>> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "void_eye"), "main");
   public static final ModelLayerLocation SEEN_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "void_eye"), "seen");
   private static final ResourceLocation VOIDEYE_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/voideye/voideye.png");
   private static final ResourceLocation VOIDEYE_BEAM_TEXTURE = new ResourceLocation("primitivemobs", "textures/entity/voideye/voideye_beam.png");
   private static final ResourceLocation VOIDEYE_BEAM_WEAK_TEXTURE = new ResourceLocation("primitivemobs", "textures/entity/voideye/voideye_beam_weak.png");

   public RenderVoidEye(EntityRendererProvider.Context context) {
      super(context, new ModelVoidEye<EntityVoidEye>(context.bakeLayer(MODEL_LAYER), true), 0.5F);
      this.addLayer(new LayerVoidEyeSeen(this, context));
   }

   @Override
   public boolean shouldRender(EntityVoidEye livingEntity, Frustum camera, double camX, double camY, double camZ) {
      if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) {
         return true;
      } else {
         if (livingEntity.hasTargetedEntity()) {
            LivingEntity entitylivingbase = livingEntity.getTargetedEntity();
            if (entitylivingbase != null) {
               Vec3 vec3d = this.getPosition(entitylivingbase, (double)entitylivingbase.getBbHeight() * (double)0.5F, 1.0F);
               Vec3 vec3d1 = this.getPosition(livingEntity, (double)livingEntity.getEyeHeight(), 1.0F);
               if (camera.isVisible(new AABB(vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y, vec3d.z))) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   @Override
   public void render(EntityVoidEye entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
      LivingEntity entitylivingbase = entity.getTargetedEntity();
      if (entitylivingbase != null) {
         float f = entity.getAttackAnimationScale(partialTicks);
         ResourceLocation beamTexture = entity.canSeeTarget() ? VOIDEYE_BEAM_TEXTURE : VOIDEYE_BEAM_WEAK_TEXTURE;

         float f2 = (float)entity.level().getGameTime() + partialTicks;
         float f3 = f2 * 0.5F % 1.0F;
         float f4 = entity.getEyeHeight();
         poseStack.pushPose();
         poseStack.translate(0.0, (double)f4, 0.0);
         Vec3 vec3d = this.getPosition(entitylivingbase, (double)entitylivingbase.getBbHeight() * (double)0.5F, partialTicks);
         Vec3 vec3d1 = this.getPosition(entity, (double)f4, partialTicks);
         Vec3 vec3d2 = vec3d.subtract(vec3d1);
         double d0 = vec3d2.length() + (double)1.0F;
         vec3d2 = vec3d2.normalize();
         float f5 = (float)Math.acos(vec3d2.y);
         float f6 = (float)Math.atan2(vec3d2.z, vec3d2.x);
         poseStack.mulPose(Axis.YP.rotationDegrees((((float)Math.PI / 2F) + -f6) * (180F / (float)Math.PI)));
         poseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float)Math.PI)));
         double d1 = (double)f2 * 0.05 * (double)-1.5F;
         float f7 = f * f;
         int j = 64 + (int)(f7 * 191.0F);
         int k = 32 + (int)(f7 * 191.0F);
         int l = 128 - (int)(f7 * 128.0F);
         double d4 = (double)0.0F + Math.cos(d1 + 2.356194490192345) * 0.282;
         double d5 = (double)0.0F + Math.sin(d1 + 2.356194490192345) * 0.282;
         double d6 = (double)0.0F + Math.cos(d1 + (Math.PI / 4D)) * 0.282;
         double d7 = (double)0.0F + Math.sin(d1 + (Math.PI / 4D)) * 0.282;
         double d8 = (double)0.0F + Math.cos(d1 + 3.9269908169872414) * 0.282;
         double d9 = (double)0.0F + Math.sin(d1 + 3.9269908169872414) * 0.282;
         double d10 = (double)0.0F + Math.cos(d1 + 5.497787143782138) * 0.282;
         double d11 = (double)0.0F + Math.sin(d1 + 5.497787143782138) * 0.282;
         double d12 = (double)0.0F + Math.cos(d1 + Math.PI) * 0.2;
         double d13 = (double)0.0F + Math.sin(d1 + Math.PI) * 0.2;
         double d14 = (double)0.0F + Math.cos(d1 + (double)0.0F) * 0.2;
         double d15 = (double)0.0F + Math.sin(d1 + (double)0.0F) * 0.2;
         double d16 = (double)0.0F + Math.cos(d1 + (Math.PI / 2D)) * 0.2;
         double d17 = (double)0.0F + Math.sin(d1 + (Math.PI / 2D)) * 0.2;
         double d18 = (double)0.0F + Math.cos(d1 + (Math.PI * 1.5D)) * 0.2;
         double d19 = (double)0.0F + Math.sin(d1 + (Math.PI * 1.5D)) * 0.2;
         double d22 = (double)(-1.0F + f3);
         double d23 = d0 * (double)2.5F + d22;

         VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(beamTexture));
         Matrix4f matrix4f = poseStack.last().pose();
         Matrix3f matrix3f = poseStack.last().normal();

         vertex(vertexConsumer, matrix4f, matrix3f, (float)d12, (float)d0, (float)d13, j, k, l, 255, 0.4999F, (float)d23);
         vertex(vertexConsumer, matrix4f, matrix3f, (float)d12, 0.0F, (float)d13, j, k, l, 255, 0.4999F, (float)d22);
         vertex(vertexConsumer, matrix4f, matrix3f, (float)d14, 0.0F, (float)d15, j, k, l, 255, 0.0F, (float)d22);
         vertex(vertexConsumer, matrix4f, matrix3f, (float)d14, (float)d0, (float)d15, j, k, l, 255, 0.0F, (float)d23);
         vertex(vertexConsumer, matrix4f, matrix3f, (float)d16, (float)d0, (float)d17, j, k, l, 255, 0.4999F, (float)d23);
         vertex(vertexConsumer, matrix4f, matrix3f, (float)d16, 0.0F, (float)d17, j, k, l, 255, 0.4999F, (float)d22);
         vertex(vertexConsumer, matrix4f, matrix3f, (float)d18, 0.0F, (float)d19, j, k, l, 255, 0.0F, (float)d22);
         vertex(vertexConsumer, matrix4f, matrix3f, (float)d18, (float)d0, (float)d19, j, k, l, 255, 0.0F, (float)d23);
         double d24 = (double)0.0F;
         if (entity.tickCount % 2 == 0) {
            d24 = (double)0.5F;
         }

         vertex(vertexConsumer, matrix4f, matrix3f, (float)d4, (float)d0, (float)d5, j, k, l, 255, (float)0.5F, (float)(d24 + (double)0.5F));
         vertex(vertexConsumer, matrix4f, matrix3f, (float)d6, (float)d0, (float)d7, j, k, l, 255, (float)1.0F, (float)(d24 + (double)0.5F));
         vertex(vertexConsumer, matrix4f, matrix3f, (float)d10, (float)d0, (float)d11, j, k, l, 255, (float)1.0F, (float)d24);
         vertex(vertexConsumer, matrix4f, matrix3f, (float)d8, (float)d0, (float)d9, j, k, l, 255, (float)0.5F, (float)d24);
         poseStack.popPose();
      }
   }

   private static void vertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, float x, float y, float z, int r, int g, int b, int a, float u, float v) {
      consumer.vertex(pose, x, y, z).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
   }

   private Vec3 getPosition(LivingEntity entityLivingBaseIn, double p_177110_2_, float p_177110_4_) {
      double d0 = entityLivingBaseIn.xOld + (entityLivingBaseIn.getX() - entityLivingBaseIn.xOld) * (double)p_177110_4_;
      double d1 = p_177110_2_ + entityLivingBaseIn.yOld + (entityLivingBaseIn.getY() - entityLivingBaseIn.yOld) * (double)p_177110_4_;
      double d2 = entityLivingBaseIn.zOld + (entityLivingBaseIn.getZ() - entityLivingBaseIn.zOld) * (double)p_177110_4_;
      return new Vec3(d0, d1, d2);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityVoidEye entity) {
      return VOIDEYE_TEXTURES;
   }
}

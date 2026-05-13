package net.daveyx0.primitivemobs.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.client.models.ModelTreasureSlime;
import net.daveyx0.primitivemobs.client.renderer.entity.RenderTreasureSlime;
import net.daveyx0.primitivemobs.core.PrimitiveMobsItems;
import net.daveyx0.primitivemobs.entity.monster.EntityTreasureSlime;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerTreasureSlimeGel extends RenderLayer<EntityTreasureSlime, ModelTreasureSlime<EntityTreasureSlime>> {
   private static final ResourceLocation SLIME_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/treasureslime/slime_treasure.png");
   private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
   private final ModelTreasureSlime<EntityTreasureSlime> slimeModel;

   public LayerTreasureSlimeGel(RenderTreasureSlime slimeRendererIn, EntityRendererProvider.Context context) {
      super(slimeRendererIn);
      this.slimeModel = new ModelTreasureSlime<EntityTreasureSlime>(context.bakeLayer(RenderTreasureSlime.OUTER_LAYER), false);
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityTreasureSlime entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!entitylivingbaseIn.isInvisible()) {
         float r, g, b;
         if (entitylivingbaseIn.hasCustomName() && "jeb_".equals(entitylivingbaseIn.getCustomName().getString())) {
            int i = entitylivingbaseIn.tickCount / 25 + entitylivingbaseIn.getId();
            int j = DyeColor.values().length;
            int k = i % j;
            int l = (i + 1) % j;
            float f = ((float)(entitylivingbaseIn.tickCount % 25) + partialTicks) / 25.0F;
            float[] afloat1 = Sheep.getColorArray(DyeColor.byId(k));
            float[] afloat2 = Sheep.getColorArray(DyeColor.byId(l));
            r = afloat1[0] * (1.0F - f) + afloat2[0] * f;
            g = afloat1[1] * (1.0F - f) + afloat2[1] * f;
            b = afloat1[2] * (1.0F - f) + afloat2[2] * f;
         } else {
            float[] RGB = entitylivingbaseIn.getSkinRGB();
            r = RGB[0] / 255.0F;
            g = RGB[1] / 255.0F;
            b = RGB[2] / 255.0F;
         }

         this.getParentModel().copyPropertiesTo(this.slimeModel);
         this.slimeModel.prepareMobModel(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
         this.slimeModel.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
         VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(SLIME_TEXTURES));
         this.slimeModel.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), r, g, b, 1.0F);

         if (entitylivingbaseIn.getMainHandItem() != null && !entitylivingbaseIn.getMainHandItem().isEmpty() && entitylivingbaseIn.getMainHandItem().getItem() == PrimitiveMobsItems.CAMOUFLAGE_DYE.get()) {
            float f2 = (float)entitylivingbaseIn.tickCount + partialTicks;
            VertexConsumer glowConsumer = bufferSource.getBuffer(RenderType.energySwirl(LIGHTNING_TEXTURE, f2 * 0.01F, f2 * 0.01F));
            this.slimeModel.renderToBuffer(poseStack, glowConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 0.5F, 0.5F, 0.5F, 1.0F);
         }
      }
   }
}

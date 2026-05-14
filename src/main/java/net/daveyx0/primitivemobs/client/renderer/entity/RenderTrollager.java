package net.daveyx0.primitivemobs.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.daveyx0.primitivemobs.client.models.ModelTrollager;
import net.daveyx0.primitivemobs.client.renderer.entity.layer.LayerHeldItemCustom;
import net.daveyx0.primitivemobs.entity.monster.EntityTrollager;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings("unchecked")
@OnlyIn(Dist.CLIENT)
public class RenderTrollager extends MobRenderer<EntityTrollager, ModelTrollager> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("primitivemobs", "trollager"), "main");
   private static final ResourceLocation TROLLAGER_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/troll/troll.png");
   private static final ResourceLocation TROLLAGER_STONED_TEXTURES = ResourceLocation.fromNamespaceAndPath("primitivemobs", "textures/entity/troll/troll_stoned.png");

   public RenderTrollager(EntityRendererProvider.Context context) {
      super(context, new ModelTrollager(context.bakeLayer(MODEL_LAYER)), 2.35F);
      this.addLayer((RenderLayer) new LayerHeldItemCustom(this));
   }

   @Override
   public ResourceLocation getTextureLocation(EntityTrollager entity) {
      return entity.isStone() ? TROLLAGER_STONED_TEXTURES : TROLLAGER_TEXTURES;
   }

   @Override
   protected void scale(EntityTrollager entitylivingbaseIn, PoseStack poseStack, float partialTickTime) {
      poseStack.scale(2.0F, 2.0F, 2.0F);
   }
}

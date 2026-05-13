package net.daveyx0.primitivemobs.client.renderer.entity;

import net.daveyx0.primitivemobs.client.models.ModelGoblin;
import net.daveyx0.primitivemobs.entity.monster.EntityGoblin;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderGoblin extends HumanoidMobRenderer<EntityGoblin, ModelGoblin<EntityGoblin>> {
   public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(new ResourceLocation("primitivemobs", "goblin"), "main");
   private static final ResourceLocation GOBLIN_TEXTURES = new ResourceLocation("primitivemobs", "textures/entity/goblin/goblin.png");

   public RenderGoblin(EntityRendererProvider.Context context) {
      super(context, new ModelGoblin<>(context.bakeLayer(MODEL_LAYER)), 0.5F);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityGoblin entity) {
      return GOBLIN_TEXTURES;
   }
}

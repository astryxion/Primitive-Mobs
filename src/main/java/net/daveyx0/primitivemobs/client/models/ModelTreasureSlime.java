package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.daveyx0.primitivemobs.entity.monster.EntityTreasureSlime;
import net.minecraft.world.entity.Entity;

public class ModelTreasureSlime<T extends Entity> extends EntityModel<T> {
    private final ModelPart slimeBodies;
    private final ModelPart slimeRightEye;
    private final ModelPart slimeLeftEye;
    private final ModelPart slimeMouth;
    private final boolean hasEyes;
    private T currentEntity;

    public ModelTreasureSlime(ModelPart root, boolean hasEyes) {
        this.hasEyes = hasEyes;
        if (hasEyes) {
            this.slimeBodies = null;
            this.slimeRightEye = root.getChild("slime_right_eye");
            this.slimeLeftEye = root.getChild("slime_left_eye");
            this.slimeMouth = root.getChild("slime_mouth");
        } else {
            this.slimeBodies = root.getChild("slime_bodies");
            this.slimeRightEye = null;
            this.slimeLeftEye = null;
            this.slimeMouth = null;
        }
    }

    public static LayerDefinition createInnerBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("slime_right_eye",
            CubeListBuilder.create().texOffs(32, 0).addBox(-3.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F),
            PartPose.ZERO);
        partdefinition.addOrReplaceChild("slime_left_eye",
            CubeListBuilder.create().texOffs(32, 4).addBox(1.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F),
            PartPose.ZERO);
        partdefinition.addOrReplaceChild("slime_mouth",
            CubeListBuilder.create().texOffs(32, 8).addBox(0.0F, 21.0F, -3.5F, 1.0F, 1.0F, 1.0F),
            PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public static LayerDefinition createOuterBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("slime_bodies",
            CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F),
            PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.currentEntity = entity;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        int tintedColor = color;
        if (this.hasEyes && this.currentEntity instanceof EntityTreasureSlime) {
            float[] RGB = ((EntityTreasureSlime) this.currentEntity).getSkinRGB();
            float alpha = (float)(color >> 24 & 255) / 255.0F;
            if (alpha == 0.0F) {
                alpha = 1.0F;
            }
            float red = (float)(color >> 16 & 255) / 255.0F;
            float green = (float)(color >> 8 & 255) / 255.0F;
            float blue = (float)(color & 255) / 255.0F;
            red *= RGB[0] / 255.0F;
            green *= RGB[1] / 255.0F;
            blue *= RGB[2] / 255.0F;
            tintedColor = ((int)(alpha * 255.0F) << 24) | ((int)(red * 255.0F) << 16) | ((int)(green * 255.0F) << 8) | (int)(blue * 255.0F);
        }
        poseStack.translate(0.0F, 0.001F, 0.0F);
        if (this.slimeBodies != null) {
            this.slimeBodies.render(poseStack, vertexConsumer, packedLight, packedOverlay, tintedColor);
        }
        if (this.slimeRightEye != null) {
            this.slimeRightEye.render(poseStack, vertexConsumer, packedLight, packedOverlay, tintedColor);
            this.slimeLeftEye.render(poseStack, vertexConsumer, packedLight, packedOverlay, tintedColor);
            this.slimeMouth.render(poseStack, vertexConsumer, packedLight, packedOverlay, tintedColor);
        }
    }
}

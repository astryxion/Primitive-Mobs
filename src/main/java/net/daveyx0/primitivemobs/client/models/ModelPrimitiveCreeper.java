package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.entity.monster.EntityPrimitiveCreeper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelPrimitiveCreeper extends EntityModel<EntityPrimitiveCreeper> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    protected float childYOffset;
    protected float childZOffset;

    public ModelPrimitiveCreeper(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.childYOffset = 8.0F;
        this.childZOffset = 4.0F;
    }

    public static LayerDefinition createBodyLayer() {
        return createBodyLayer(CubeDeformation.NONE);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation),
            PartPose.offset(0.0F, 6.0F, 0.0F));
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation),
            PartPose.offset(0.0F, 6.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_hind_leg",
            CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation),
            PartPose.offset(-2.0F, 18.0F, 4.0F));
        partdefinition.addOrReplaceChild("left_hind_leg",
            CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation),
            PartPose.offset(2.0F, 18.0F, 4.0F));
        partdefinition.addOrReplaceChild("right_front_leg",
            CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation),
            PartPose.offset(-2.0F, 18.0F, -4.0F));
        partdefinition.addOrReplaceChild("left_front_leg",
            CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation),
            PartPose.offset(2.0F, 18.0F, -4.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(EntityPrimitiveCreeper entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.head.xRot = headPitch / (180F / (float)Math.PI);
        this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        if (this.young) {
            float var8 = 2.0F;
            poseStack.pushPose();
            poseStack.translate(0.0F, this.childYOffset * 0.0625F + 0.125F, this.childZOffset * 0.0625F - 0.25F);
            this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            poseStack.popPose();
            poseStack.pushPose();
            poseStack.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
            poseStack.translate(0.0F, 28.0F * 0.0625F - 0.25F, 0.0F);
            this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.rightHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.leftHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.rightFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.leftFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            poseStack.popPose();
        } else {
            this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.rightHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.leftHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.rightFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.leftFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }
    }
}

package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.entity.monster.EntityYeti;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ModelYeti extends EntityModel<EntityYeti> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart armLeft;
    private final ModelPart nose;
    public final ModelPart armRight;
    private final ModelPart armLeftHide;
    private final ModelPart armRightHide;
    private final ModelPart headHide;
    private final ModelPart legRightHide;
    private final ModelPart legLeftHide;

    public ModelYeti(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.legLeft = root.getChild("leg_left");
        this.legRight = root.getChild("leg_right");
        this.armLeft = root.getChild("arm_left");
        this.nose = root.getChild("nose");
        this.armRight = root.getChild("arm_right");
        this.armLeftHide = root.getChild("arm_left_hide");
        this.armRightHide = root.getChild("arm_right_hide");
        this.headHide = root.getChild("head_hide");
        this.legRightHide = root.getChild("leg_right_hide");
        this.legLeftHide = root.getChild("leg_left_hide");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -9.5F, -5.0F, 8.0F, 10.0F, 8.0F),
            PartPose.offset(0.0F, -9.0F, -1.0F));
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-8.0F, -10.5F, -6.0F, 16.0F, 21.0F, 12.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 3.5F, 0.1745329F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg_left",
            CubeListBuilder.create().texOffs(69, 3).mirror().addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F),
            PartPose.offset(4.0F, 10.0F, 5.0F));
        partdefinition.addOrReplaceChild("leg_right",
            CubeListBuilder.create().texOffs(69, 3).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F),
            PartPose.offset(-4.0F, 10.0F, 5.0F));
        partdefinition.addOrReplaceChild("arm_left",
            CubeListBuilder.create().texOffs(57, 24).mirror().addBox(-6.0F, -3.0F, -3.0F, 6.0F, 23.0F, 6.0F),
            PartPose.offsetAndRotation(8.0F, -5.0F, 2.0F, 0.0F, 3.141593F, 0.0F));
        partdefinition.addOrReplaceChild("nose",
            CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-1.0F, -2.5F, -7.0F, 2.0F, 5.0F, 2.0F),
            PartPose.offsetAndRotation(0.0F, -9.0F, -1.0F, -0.1570796F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("arm_right",
            CubeListBuilder.create().texOffs(57, 24).mirror().addBox(-6.0F, -3.0F, -3.0F, 6.0F, 23.0F, 6.0F),
            PartPose.offset(-8.0F, -5.0F, 2.0F));
        partdefinition.addOrReplaceChild("arm_left_hide",
            CubeListBuilder.create().texOffs(57, 55).mirror().addBox(-6.5F, -3.5F, -3.5F, 7.0F, 17.0F, 7.0F),
            PartPose.offsetAndRotation(8.0F, -5.0F, 2.0F, 0.0F, 3.141593F, 0.0F));
        partdefinition.addOrReplaceChild("arm_right_hide",
            CubeListBuilder.create().texOffs(57, 55).mirror().addBox(-6.5F, -3.533333F, -3.5F, 7.0F, 17.0F, 7.0F),
            PartPose.offset(-8.0F, -5.0F, 2.0F));
        partdefinition.addOrReplaceChild("head_hide",
            CubeListBuilder.create().texOffs(0, 54).mirror().addBox(-4.5F, -10.0F, -5.5F, 9.0F, 11.0F, 9.0F),
            PartPose.offset(0.0F, -9.0F, -1.0F));
        partdefinition.addOrReplaceChild("leg_right_hide",
            CubeListBuilder.create().texOffs(83, 24).mirror().addBox(-3.5F, 0.0F, -3.5F, 7.0F, 10.0F, 7.0F),
            PartPose.offset(-4.0F, 10.0F, 5.0F));
        partdefinition.addOrReplaceChild("leg_left_hide",
            CubeListBuilder.create().texOffs(83, 24).mirror().addBox(-3.5F, 0.0F, -3.5F, 7.0F, 10.0F, 7.0F),
            PartPose.offset(4.0F, 10.0F, 5.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(EntityYeti entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float i = 0.0F;
        if (i < 20.0F) {
            this.armRight.xRot = -2.0F + 1.5F * this.triangleWave(i - ageInTicks, 10.0F);
            this.armLeft.xRot = 2.0F + 1.5F * this.triangleWave(i - ageInTicks, 10.0F);
        } else {
            this.armRight.xRot = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
            this.armLeft.xRot = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        }

        this.head.yRot = netHeadYaw / 57.29578F;
        this.head.xRot = headPitch / 57.29578F;
        this.nose.yRot = this.headHide.yRot = this.head.yRot;
        this.nose.xRot = this.headHide.xRot = this.head.xRot;
        this.armRightHide.xRot = this.armRight.xRot;
        this.armLeftHide.xRot = this.armLeft.xRot;
        this.legRight.xRot = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.legRightHide.xRot = this.legRight.xRot;
        this.legLeft.xRot = -1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.legLeftHide.xRot = this.legLeft.xRot;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.armLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.nose.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.armRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.armLeftHide.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.armRightHide.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.headHide.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legRightHide.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legLeftHide.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    private float triangleWave(float p_78172_1_, float p_78172_2_) {
        return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
    }
}

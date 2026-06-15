package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.entity.passive.EntityLostMiner;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelLostMiner extends EntityModel<EntityLostMiner> implements HeadedModel {
    public final ModelPart head;
    private final ModelPart body;
    public final ModelPart arms;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart armLeftShoulder;
    private final ModelPart armLeftHand;
    private final ModelPart armRightShoulder;
    public final ModelPart armRightHand;
    private EntityLostMiner currentEntity;

    public ModelLostMiner(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.arms = root.getChild("arms");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.armLeftShoulder = root.getChild("arm_left_shoulder");
        this.armLeftHand = root.getChild("arm_left_hand");
        this.armRightShoulder = root.getChild("arm_right_shoulder");
        this.armRightHand = root.getChild("arm_right_hand");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition headDef = partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F),
            PartPose.ZERO);
        headDef.addOrReplaceChild("nose",
            CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F),
            PartPose.offset(0.0F, -2.0F, 0.0F));
        headDef.addOrReplaceChild("hat1",
            CubeListBuilder.create().texOffs(28, 51).mirror().addBox(-4.5F, -10.5F, -4.5F, 9.0F, 4.0F, 9.0F),
            PartPose.ZERO);
        headDef.addOrReplaceChild("hat2",
            CubeListBuilder.create().texOffs(28, 46).mirror().addBox(-1.0F, -10.0F, -5.6F, 2.0F, 2.0F, 1.0F),
            PartPose.ZERO);
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F),
            PartPose.ZERO);
        partdefinition.addOrReplaceChild("arms",
            CubeListBuilder.create().texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F),
            PartPose.offsetAndRotation(0.0F, 3.0F, -1.0F, -0.75F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg",
            CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
            PartPose.offset(-2.0F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg",
            CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
            PartPose.offset(2.0F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("arm_left_shoulder",
            CubeListBuilder.create().texOffs(44, 0).mirror().addBox(2.0F, -4.0F, -1.5F, 4.0F, 4.0F, 4.0F),
            PartPose.offsetAndRotation(2.0F, 3.0F, 1.0F, 1.396263F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("arm_left_hand",
            CubeListBuilder.create().texOffs(44, 9).mirror().addBox(2.0F, -8.0F, -5.5F, 4.0F, 8.0F, 4.0F),
            PartPose.offsetAndRotation(2.0F, 3.0F, 1.0F, 1.396263F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("arm_right_shoulder",
            CubeListBuilder.create().texOffs(44, 0).mirror().addBox(-6.0F, -4.0F, -1.5F, 4.0F, 4.0F, 4.0F),
            PartPose.offsetAndRotation(-2.0F, 3.0F, 1.0F, 1.396263F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("arm_right_hand",
            CubeListBuilder.create().texOffs(44, 9).mirror().addBox(-6.0F, -8.0F, -5.5F, 4.0F, 8.0F, 4.0F),
            PartPose.offsetAndRotation(-2.0F, 3.0F, 1.0F, 1.396263F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(EntityLostMiner entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.currentEntity = entity;
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.arms.y = 3.0F;
        this.arms.z = -1.0F;
        this.arms.xRot = -0.75F;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;
        this.armLeftShoulder.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F + 1.396263F;
        this.armLeftHand.xRot = this.armLeftShoulder.xRot;
        this.armRightShoulder.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F + 1.396263F;
        this.armRightHand.xRot = this.armRightShoulder.xRot;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        if (this.currentEntity != null) {
            this.renderArms(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha, this.currentEntity);
        }
    }

    @Override
    public ModelPart getHead() {
        return this.head;
    }

    public void renderArms(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, EntityLostMiner lostMiner) {
        this.armLeftShoulder.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.armLeftHand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.armRightShoulder.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.armRightHand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

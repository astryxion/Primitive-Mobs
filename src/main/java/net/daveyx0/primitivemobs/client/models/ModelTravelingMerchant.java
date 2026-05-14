package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ModelTravelingMerchant<T extends Entity> extends EntityModel<T> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart armLeftShoulder;
    private final ModelPart armLeftHand;
    private final ModelPart burden1;
    private final ModelPart burden2;
    private final ModelPart armRightShoulder;
    private final ModelPart armRightHand;
    private final ModelPart nose;

    public ModelTravelingMerchant(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.legLeft = root.getChild("leg_left");
        this.legRight = root.getChild("leg_right");
        this.armLeftShoulder = root.getChild("arm_left_shoulder");
        this.armLeftHand = root.getChild("arm_left_hand");
        this.burden1 = root.getChild("burden1");
        this.burden2 = root.getChild("burden2");
        this.armRightShoulder = root.getChild("arm_right_shoulder");
        this.armRightHand = root.getChild("arm_right_hand");
        this.nose = root.getChild("nose");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -9.5F, -5.1F, 8.0F, 10.0F, 8.0F),
            PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(0, 39).mirror().addBox(-5.0F, 0.0F, -4.0F, 8.0F, 17.0F, 6.0F),
            PartPose.offset(1.0F, 0.5F, 0.0F));
        partdefinition.addOrReplaceChild("leg_left",
            CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
            PartPose.offset(2.0F, 12.0F, -1.0F));
        partdefinition.addOrReplaceChild("leg_right",
            CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
            PartPose.offset(-2.0F, 12.0F, -1.0F));
        partdefinition.addOrReplaceChild("arm_left_shoulder",
            CubeListBuilder.create().texOffs(40, 38).mirror().addBox(0.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F),
            PartPose.offsetAndRotation(2.0F, 3.0F, 1.0F, 0.6981317F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("arm_left_hand",
            CubeListBuilder.create().texOffs(44, 22).mirror().addBox(0.0F, -6.0F, -6.0F, 4.0F, 8.0F, 4.0F),
            PartPose.offsetAndRotation(2.0F, 3.0F, 1.0F, 0.6981317F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("burden1",
            CubeListBuilder.create().texOffs(66, 26).mirror().addBox(-8.0F, 0.0F, 0.0F, 16.0F, 6.0F, 6.0F),
            PartPose.offset(0.0F, -8.0F, 3.0F));
        partdefinition.addOrReplaceChild("burden2",
            CubeListBuilder.create().texOffs(66, 38).mirror().addBox(-7.0F, 0.0F, 0.0F, 14.0F, 18.0F, 8.0F),
            PartPose.offset(0.0F, -2.0F, 3.0F));
        partdefinition.addOrReplaceChild("arm_right_shoulder",
            CubeListBuilder.create().texOffs(40, 38).mirror().addBox(-4.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F),
            PartPose.offsetAndRotation(-2.0F, 3.0F, 1.0F, 0.6981317F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("arm_right_hand",
            CubeListBuilder.create().texOffs(44, 22).mirror().addBox(-4.0F, -6.0F, -6.0F, 4.0F, 8.0F, 4.0F),
            PartPose.offsetAndRotation(-2.0F, 3.0F, 1.0F, 0.6981317F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("nose",
            CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-1.0F, -2.5F, -7.0F, 2.0F, 4.0F, 2.0F),
            PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.nose.xRot = this.head.xRot;
        this.nose.yRot = this.head.yRot;
        this.legRight.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.legLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.legRight.yRot = 0.0F;
        this.legLeft.yRot = 0.0F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.armLeftShoulder.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.armLeftHand.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.burden1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.burden2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.armRightShoulder.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.armRightHand.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.nose.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}

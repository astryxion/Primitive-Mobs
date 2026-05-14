package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.entity.monster.EntityTrollager;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelTrollager extends EntityModel<EntityTrollager> {
    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart legLeft;
    public final ModelPart legRight;
    public final ModelPart armLeft;
    public final ModelPart armRight;
    public final ModelPart blockHolder;
    private final ModelPart mouth;
    private final ModelPart nose;

    private static final float BASE_BODY_X = 0.0F;
    private static final float BASE_BODY_Y = 6.5F;
    private static final float BASE_HEAD_X = 0.0F;
    private static final float BASE_HEAD_Y = 5.0F;
    private static final float BASE_ARM_LEFT_X = 8.0F;
    private static final float BASE_ARM_RIGHT_X = -8.0F;

    public ModelTrollager(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.legLeft = root.getChild("leg_left");
        this.legRight = root.getChild("leg_right");
        this.armLeft = root.getChild("arm_left");
        this.armRight = root.getChild("arm_right");
        this.blockHolder = root.getChild("block_holder");
        this.mouth = this.head.getChild("mouth");
        this.nose = this.head.getChild("nose");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition headDef = partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -9.5F, -5.1F, 8.0F, 10.0F, 8.0F),
            PartPose.offset(0.0F, 5.0F, -10.0F));
        headDef.addOrReplaceChild("nose",
            CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-1.0F, -2.5F, -7.0F, 2.0F, 5.0F, 2.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1570796F, 0.0F, 0.0F));
        headDef.addOrReplaceChild("mouth",
            CubeListBuilder.create().texOffs(32, 5).mirror().addBox(-4.5F, -2.0F, -5.5F, 9.0F, 4.0F, 9.0F),
            PartPose.ZERO);
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-8.0F, -10.5F, -6.0F, 16.0F, 21.0F, 12.0F),
            PartPose.offsetAndRotation(0.0F, 6.5F, 0.0F, 0.9599311F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg_left",
            CubeListBuilder.create().texOffs(69, 3).mirror().addBox(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F),
            PartPose.offset(4.0F, 15.0F, 5.0F));
        partdefinition.addOrReplaceChild("leg_right",
            CubeListBuilder.create().texOffs(69, 3).mirror().addBox(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F),
            PartPose.offset(-4.0F, 15.0F, 5.0F));
        partdefinition.addOrReplaceChild("arm_left",
            CubeListBuilder.create().texOffs(57, 24).mirror().addBox(-6.0F, -3.0F, -3.0F, 6.0F, 23.0F, 6.0F),
            PartPose.offsetAndRotation(8.0F, 4.0F, -6.0F, 0.0F, 3.141593F, 0.0F));
        partdefinition.addOrReplaceChild("arm_right",
            CubeListBuilder.create().texOffs(57, 24).mirror().addBox(-6.0F, -3.0F, -3.0F, 6.0F, 23.0F, 6.0F),
            PartPose.offset(-8.0F, 4.0F, -6.0F));
        partdefinition.addOrReplaceChild("block_holder",
            CubeListBuilder.create().texOffs(57, 24).mirror().addBox(-6.0F, -3.0F, -3.0F, 6.0F, 23.0F, 6.0F),
            PartPose.offset(-8.0F, 4.0F, -6.0F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(EntityTrollager entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch / (180F / (float)Math.PI);
        this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.legRight.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount;
        this.legLeft.xRot = -Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount;
        if (entity != null) {
            switch (entity.getAnimationState()) {
                case 0:
                    this.armLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount;
                    this.armRight.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.8F * limbSwingAmount;
                    this.armLeft.zRot = 0.0F;
                    this.armRight.zRot = 0.0F;
                    this.blockHolder.xRot = 0.0F;
                    this.blockHolder.zRot = 0.0F;
                    this.body.xRot = 0.9599311F;
                    this.resetPoseOffsets();
                    break;
                case 1:
                    if (entity.getAnimVar() != 1.0F) {
                        float progress = this.poseProgress(entity, 4.0F);
                        this.armLeft.xRot = this.poseBlend(progress, 0.0F, 3.5F);
                        this.armRight.xRot = this.poseBlend(progress, 0.0F, -3.5F);
                        this.blockHolder.xRot = this.poseBlend(progress, 0.0F, -3.5F);
                        this.armLeft.zRot = this.poseBlend(progress, 0.1F, -0.22F);
                        this.armRight.zRot = this.poseBlend(progress, -0.1F, 0.22F);
                        this.blockHolder.zRot = 0.0F;
                        this.body.xRot = 0.9599311F;
                        this.resetPoseOffsets();
                    }
                    break;
                case 2:
                    if (entity.getAnimVar() != 1.0F) {
                        float progress = this.poseProgress(entity, 8.0F);
                        this.armLeft.xRot = this.poseBlend(progress, 3.5F, 0.0F);
                        this.armRight.xRot = this.poseBlend(progress, -3.5F, 0.0F);
                        this.blockHolder.xRot = this.poseBlend(progress, -3.5F, 0.0F);
                        this.armLeft.zRot = this.poseBlend(progress, -0.22F, 0.1F);
                        this.armRight.zRot = this.poseBlend(progress, 0.22F, -0.1F);
                        this.blockHolder.zRot = 0.0F;
                        this.body.xRot = 0.9599311F;
                        this.resetPoseOffsets();
                        this.mouth.xRot = 0.15F;
                        this.mouth.zRot = 0.0F;
                    }
                    break;
                case 3:
                    if (entity.getAnimVar() != 1.0F) {
                        float progress = this.poseProgress(entity, 3.0F);
                        this.armLeft.xRot = this.poseBlend(progress, 0.0F, 3.5F);
                        this.armRight.xRot = this.poseBlend(progress, 0.0F, -3.5F);
                        this.armLeft.zRot = this.poseBlend(progress, 0.1F, -0.22F);
                        this.armRight.zRot = this.poseBlend(progress, -0.1F, 0.22F);
                        this.body.xRot = 0.9599311F;
                        this.resetPoseOffsets();
                    }
                    break;
                case 4:
                    if (entity.getAnimVar() != 1.0F) {
                        float progress = this.poseProgress(entity, 12.0F);
                        this.armLeft.xRot = this.poseBlend(progress, 3.5F, 0.5F);
                        this.armRight.xRot = this.poseBlend(progress, -3.5F, -0.5F);
                        this.armLeft.zRot = 0.0F;
                        this.armRight.zRot = 0.0F;
                        this.armLeft.x = BASE_ARM_LEFT_X + this.poseBlend(progress, 0.0F, 0.2F) * 16.0F;
                        this.armRight.x = BASE_ARM_RIGHT_X + this.poseBlend(progress, 0.0F, 0.2F) * 16.0F;
                        this.body.xRot = this.poseBlend(progress, 0.9599311F, 1.32645F);
                        this.body.x = BASE_BODY_X + this.poseBlend(progress, 0.0F, 0.2F) * 16.0F;
                        this.head.x = BASE_HEAD_X + this.poseBlend(progress, 0.0F, 0.2F) * 16.0F;
                        this.head.y = BASE_HEAD_Y + this.poseBlend(progress, 0.0F, -0.1F) * 16.0F;
                        this.mouth.xRot = 0.15F;
                        this.mouth.zRot = 0.0F;
                    }
                    break;
                case 5:
                    if (entity.getAnimVar() != 1.0F) {
                        float progress = this.poseProgress(entity, 10.0F);
                        this.armLeft.xRot = this.poseBlend(progress, 0.5F, 0.0F);
                        this.armRight.xRot = this.poseBlend(progress, -0.5F, 0.0F);
                        this.armLeft.zRot = 0.0F;
                        this.armRight.zRot = 0.0F;
                        this.armLeft.x = BASE_ARM_LEFT_X + this.poseBlend(progress, 0.2F, 0.0F) * 16.0F;
                        this.armRight.x = BASE_ARM_RIGHT_X + this.poseBlend(progress, 0.2F, 0.0F) * 16.0F;
                        this.body.xRot = this.poseBlend(progress, 1.32645F, 0.9599311F);
                        this.body.x = BASE_BODY_X + this.poseBlend(progress, 0.2F, 0.0F) * 16.0F;
                        this.head.x = BASE_HEAD_X + this.poseBlend(progress, 0.2F, 0.0F) * 16.0F;
                        this.head.y = BASE_HEAD_Y + this.poseBlend(progress, -0.1F, 0.0F) * 16.0F;
                    }
                    break;
                case 6:
                    if (entity.getAnimVar() != 1.0F) {
                        float progress = this.poseProgress(entity, 22.0F);
                        this.armLeft.xRot = this.poseBlend(progress, 0.0F, 1.5F);
                        this.armRight.xRot = this.poseBlend(progress, 0.0F, -1.5F);
                        this.armLeft.zRot = 0.0F;
                        this.armRight.zRot = 0.0F;
                        this.body.xRot = 0.9599311F;
                        this.resetPoseOffsets();
                    }
                    break;
                case 7:
                    if (entity.getAnimVar() != 1.0F) {
                        float progress = this.poseProgress(entity, 22.0F);
                        this.armLeft.xRot = this.poseBlend(progress, 1.5F, 0.0F);
                        this.armRight.xRot = this.poseBlend(progress, -1.5F, 0.0F);
                        this.armLeft.zRot = 0.0F;
                        this.armRight.zRot = 0.0F;
                        this.body.xRot = 0.9599311F;
                        this.resetPoseOffsets();
                    }
                    break;
            }

            if (entity.getAnimationState() != 2 && entity.getAnimationState() != 4 && !entity.isStone()) {
                this.idleMouth((float)entity.tickCount * 0.5F);
            }
        }
    }

    private float poseProgress(EntityTrollager entity, float speed) {
        return Mth.clamp(entity.getAnimVar() * speed, 0.0F, 1.0F);
    }

    private float poseBlend(float progress, float start, float end) {
        return Mth.lerp(progress, start, end);
    }

    private void resetPoseOffsets() {
        this.body.x = BASE_BODY_X;
        this.armLeft.x = BASE_ARM_LEFT_X;
        this.armRight.x = BASE_ARM_RIGHT_X;
        this.head.x = BASE_HEAD_X;
        this.head.y = BASE_HEAD_Y;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.armLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.armRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    public void idleMouth(float e) {
        this.mouth.x = 0.0F;
        this.mouth.y = 0.0F;
        this.mouth.z = 0.0F;
        this.mouth.xRot = Mth.sin(e) * 4.5F * ((float)Math.PI / 180F);
        this.mouth.yRot = 0.0F;
        this.mouth.zRot = Mth.cos(e) * 2.5F * ((float)Math.PI / 180F);
    }
}

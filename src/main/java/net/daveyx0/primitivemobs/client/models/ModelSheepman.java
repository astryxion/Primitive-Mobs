package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.entity.passive.EntitySheepman;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelSheepman extends EntityModel<EntitySheepman> {
    private final int scale;
    private boolean sheared;
    private final ModelPart head;
    private final ModelPart headWool;
    private final ModelPart body;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart armLeft;
    private final ModelPart armRight;
    private final ModelPart bodyWool;
    private final ModelPart legLeftWool;
    private final ModelPart legRightWool;
    private final ModelPart foldedArms;

    public ModelSheepman(ModelPart root, int scale) {
        this.scale = scale;
        this.head = root.getChild("head");
        this.headWool = root.getChild("head_wool");
        this.body = root.getChild("body");
        this.legLeft = root.getChild("leg_left");
        this.legRight = root.getChild("leg_right");
        this.armLeft = root.getChild("arm_left");
        this.armRight = root.getChild("arm_right");
        this.bodyWool = root.getChild("body_wool");
        this.legLeftWool = root.getChild("leg_left_wool");
        this.legRightWool = root.getChild("leg_right_wool");
        this.foldedArms = root.getChild("folded_arms");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.0F, -6.0F, -6.0F, 6.0F, 6.0F, 8.0F),
            PartPose.offset(0.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("head_wool",
            CubeListBuilder.create().texOffs(0, 14).mirror().addBox(-3.0F, -6.0F, -4.0F, 6.0F, 6.0F, 6.0F),
            PartPose.offset(0.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F),
            PartPose.offset(0.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg_left",
            CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
            PartPose.offset(2.0F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg_right",
            CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
            PartPose.offset(-2.0F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("arm_left",
            CubeListBuilder.create().texOffs(16, 26).mirror().addBox(0.0F, 0.0F, -2.0F, 3.0F, 12.0F, 4.0F),
            PartPose.offset(4.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("arm_right",
            CubeListBuilder.create().texOffs(16, 26).mirror().addBox(-3.0F, 0.0F, -2.0F, 3.0F, 12.0F, 4.0F),
            PartPose.offset(-4.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("body_wool",
            CubeListBuilder.create().texOffs(36, 0).mirror().addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F),
            PartPose.offset(0.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg_left_wool",
            CubeListBuilder.create().texOffs(30, 26).mirror().addBox(2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
            PartPose.offset(-2.0F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg_right_wool",
            CubeListBuilder.create().texOffs(30, 26).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
            PartPose.offset(-2.0F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("folded_arms",
            CubeListBuilder.create().texOffs(20, 52).mirror().addBox(-7.0F, 0.0F, -8.0F, 14.0F, 4.0F, 8.0F),
            PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, ((float)Math.PI / 6F), 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(EntitySheepman entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.sheared = entity.getSheared();
        this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.head.xRot = headPitch / (180F / (float)Math.PI);
        this.headWool.yRot = this.head.yRot;
        this.headWool.xRot = this.head.xRot;
        this.legRight.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.6F * limbSwingAmount;
        this.legLeft.xRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.6F * limbSwingAmount;
        this.legRightWool.xRot = this.legRight.xRot;
        this.legLeftWool.xRot = this.legLeft.xRot;
        this.armRight.xRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.6F * limbSwingAmount;
        this.armLeft.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.6F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        if (this.young) {
            float var8 = 1.5F;
            poseStack.pushPose();
            poseStack.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
            poseStack.translate(0.0F, 28.0F * 0.0625F - 1.0F, 0.0F);
            renderByScale(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            poseStack.popPose();
        } else {
            poseStack.pushPose();
            renderByScale(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            poseStack.popPose();
        }
    }

    private void renderByScale(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        if (this.scale == 0) {
            this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.legLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.legRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            if (this.sheared) {
                this.armLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
                this.armRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            }
        } else {
            this.foldedArms.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            poseStack.scale(1.1F, 1.1F, 1.1F);
            this.headWool.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.legLeftWool.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.legRightWool.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            poseStack.translate(0.0F, -0.05F, 0.0F);
            this.bodyWool.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }
    }
}

package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.entity.passive.EntityFilchLizard;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelFilchLizard extends EntityModel<EntityFilchLizard> {
    public final ModelPart body;
    public final ModelPart head;
    public final ModelPart tail;
    public final ModelPart leg1;
    public final ModelPart leg2;
    public final ModelPart leg3;
    public final ModelPart leg4;
    public final ModelPart foldHead;
    public final ModelPart foldFilch1;
    public final ModelPart foldFilch2;
    private EntityFilchLizard currentEntity;

    public ModelFilchLizard(ModelPart root) {
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.tail = root.getChild("tail");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.foldHead = root.getChild("fold_head");
        this.foldFilch1 = root.getChild("fold_filch1");
        this.foldFilch2 = root.getChild("fold_filch2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(0, 6).addBox(-2.0F, -1.5F, -6.0F, 4.0F, 3.0F, 12.0F),
            PartPose.offset(0.0F, 21.0F, 0.0F));
        partdefinition.addOrReplaceChild("tail",
            CubeListBuilder.create().texOffs(32, 9).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 2.0F, 10.0F),
            PartPose.offset(0.0F, 21.0F, 6.0F));
        partdefinition.addOrReplaceChild("leg1",
            CubeListBuilder.create().texOffs(16, 0).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
            PartPose.offsetAndRotation(2.0F, 22.0F, -4.0F, 0.0F, 0.0F, 0.3839724F));
        partdefinition.addOrReplaceChild("leg2",
            CubeListBuilder.create().texOffs(16, 3).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
            PartPose.offsetAndRotation(-2.0F, 22.0F, -4.0F, 0.0F, 0.0F, -0.3839724F));
        partdefinition.addOrReplaceChild("leg3",
            CubeListBuilder.create().texOffs(16, 0).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
            PartPose.offsetAndRotation(2.0F, 22.0F, 5.0F, 0.0F, 0.0F, 0.3839724F));
        partdefinition.addOrReplaceChild("leg4",
            CubeListBuilder.create().texOffs(16, 3).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
            PartPose.offsetAndRotation(-2.0F, 22.0F, 5.0F, 0.0F, 0.0F, -0.3839724F));
        partdefinition.addOrReplaceChild("fold_filch1",
            CubeListBuilder.create().texOffs(0, 22).addBox(1.0F, -1.5F, 0.0F, 1.0F, 3.0F, 6.0F),
            PartPose.offsetAndRotation(0.0F, 21.0F, -6.0F, 0.0F, 0.0349066F, 0.0F));
        partdefinition.addOrReplaceChild("fold_filch2",
            CubeListBuilder.create().texOffs(14, 22).addBox(-2.0F, -1.5F, 0.0F, 1.0F, 3.0F, 6.0F),
            PartPose.offsetAndRotation(0.0F, 21.0F, -6.0F, 0.0F, -0.0349066F, 0.0F));
        partdefinition.addOrReplaceChild("fold_head",
            CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.5F, -4.0F, 4.0F, 2.0F, 4.0F),
            PartPose.offset(0.0F, 21.0F, -6.0F));
        PartDefinition headDef = partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.5F, -4.0F, 4.0F, 2.0F, 4.0F),
            PartPose.offset(0.0F, 12.0F, -1.0F));
        headDef.addOrReplaceChild("filch1",
            CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, -2.5F, 2.5F, 1.0F, 3.0F, 6.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3665191F, 1.570796F, -0.296706F));
        headDef.addOrReplaceChild("filch2",
            CubeListBuilder.create().texOffs(14, 22).addBox(-1.0F, -2.5F, 2.5F, 1.0F, 3.0F, 6.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3665191F, -1.570796F, 0.296706F));
        headDef.addOrReplaceChild("filch3",
            CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, -2.5F, 2.0F, 1.0F, 3.0F, 6.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.570796F, -0.2617994F));
        headDef.addOrReplaceChild("filch4",
            CubeListBuilder.create().texOffs(14, 22).addBox(-0.5F, -2.5F, 2.0F, 1.0F, 3.0F, 6.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.570796F, 0.2617994F));
        headDef.addOrReplaceChild("filch5",
            CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -2.5F, 1.5F, 1.0F, 3.0F, 6.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3839724F, 1.570796F, -0.2617994F));
        headDef.addOrReplaceChild("filch6",
            CubeListBuilder.create().texOffs(14, 22).addBox(0.0F, -2.5F, 1.5F, 1.0F, 3.0F, 6.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4014257F, -1.570796F, 0.2617994F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(EntityFilchLizard entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.currentEntity = entity;
        if (!entity.getMainHandItem().isEmpty()) {
            this.leg1.x = -2.0F; this.leg1.y = 13.0F; this.leg1.z = -1.0F;
            this.leg1.xRot = 0.0F; this.leg1.yRot = 1.047198F; this.leg1.zRot = 0.6981317F;
            this.leg2.x = 2.0F; this.leg2.y = 13.0F; this.leg2.z = -1.0F;
            this.leg2.xRot = 0.0F; this.leg2.yRot = -1.047198F; this.leg2.zRot = -0.6981317F;
            this.leg3.x = 2.0F; this.leg3.y = 20.0F; this.leg3.z = 5.0F;
            this.leg3.xRot = 0.0F; this.leg3.yRot = 0.0F; this.leg3.zRot = 1.396263F;
            this.leg4.x = -2.0F; this.leg4.y = 20.0F; this.leg4.z = 5.0F;
            this.leg4.xRot = 0.0F; this.leg4.yRot = 0.0F; this.leg4.zRot = -1.396263F;
            this.body.x = 0.0F; this.body.y = 16.0F; this.body.z = 2.0F;
            this.body.xRot = -0.9948377F; this.body.yRot = 0.0F; this.body.zRot = 0.0F;
            this.tail.x = 0.0F; this.tail.y = 20.0F; this.tail.z = 6.0F;
            this.tail.xRot = 0.6806784F; this.tail.yRot = 0.0F; this.tail.zRot = 0.0F;
            this.head.xRot = headPitch / (180F / (float)Math.PI);
            this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
        } else {
            this.leg1.x = 2.0F; this.leg1.y = 22.0F; this.leg1.z = -4.0F;
            this.leg1.xRot = 0.0F; this.leg1.yRot = 0.0F; this.leg1.zRot = 0.3839724F;
            this.leg2.x = -2.0F; this.leg2.y = 22.0F; this.leg2.z = -4.0F;
            this.leg2.xRot = 0.0F; this.leg2.yRot = 0.0F; this.leg2.zRot = -0.3839724F;
            this.leg3.x = 2.0F; this.leg3.y = 22.0F; this.leg3.z = 5.0F;
            this.leg3.xRot = 0.0F; this.leg3.yRot = 0.0F; this.leg3.zRot = 0.3839724F;
            this.leg4.x = -2.0F; this.leg4.y = 22.0F; this.leg4.z = 5.0F;
            this.leg4.xRot = 0.0F; this.leg4.yRot = 0.0F; this.leg4.zRot = -0.3839724F;
            this.body.x = 0.0F; this.body.y = 21.0F; this.body.z = 0.0F;
            this.body.xRot = 0.0F; this.body.yRot = 0.0F; this.body.zRot = 0.0F;
            this.tail.x = 0.0F; this.tail.y = 21.0F; this.tail.z = 6.0F;
            this.tail.xRot = 0.0F; this.tail.yRot = 0.0F; this.tail.zRot = 0.0F;
            this.leg1.yRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount;
            this.leg2.yRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount;
            this.foldHead.xRot = headPitch / (180F / (float)Math.PI);
            this.foldHead.yRot = netHeadYaw / (180F / (float)Math.PI);
        }

        this.leg3.yRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount;
        this.leg4.yRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount;
        this.tail.yRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.2F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.leg4.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        if (this.currentEntity != null) {
            this.renderHead(poseStack, vertexConsumer, packedLight, packedOverlay, color, this.currentEntity);
        }
    }

    public void renderHead(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color, EntityFilchLizard lizard) {
        if (!lizard.getMainHandItem().isEmpty()) {
            this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        } else {
            this.foldHead.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.foldFilch1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.foldFilch2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }
    }
}

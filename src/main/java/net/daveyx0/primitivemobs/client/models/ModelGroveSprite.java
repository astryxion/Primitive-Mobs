package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.daveyx0.primitivemobs.entity.passive.EntityGroveSprite;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelGroveSprite extends EntityModel<EntityGroveSprite> {
    private final ModelPart bipedHead;
    private final ModelPart bipedBody;
    private final ModelPart bipedHeadwear;
    public final ModelPart bipedRightArm;
    private final ModelPart bipedLeftArm;
    private final ModelPart bipedRightLeg;
    private final ModelPart bipedLeftLeg;
    private final ModelPart stem;
    private final ModelPart[] leaf;
    private final boolean isLeafModel;

    public ModelGroveSprite(ModelPart root, boolean isLeafModel) {
        this.isLeafModel = isLeafModel;
        this.bipedHead = root.getChild("head");
        this.bipedBody = root.getChild("body");
        if (!isLeafModel) {
            this.bipedHeadwear = root.getChild("headwear");
            this.bipedRightArm = root.getChild("right_arm");
            this.bipedLeftArm = root.getChild("left_arm");
            this.bipedRightLeg = root.getChild("right_leg");
            this.bipedLeftLeg = root.getChild("left_leg");
            this.stem = root.getChild("stem");
            this.leaf = null;
        } else {
            this.bipedHeadwear = null;
            this.bipedRightArm = null;
            this.bipedLeftArm = null;
            this.bipedRightLeg = null;
            this.bipedLeftLeg = null;
            this.stem = null;
            this.leaf = new ModelPart[3];
            for (int i = 0; i < 3; ++i) {
                this.leaf[i] = root.getChild("leaf" + i);
            }
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F),
            PartPose.offset(0.0F, -1.0F, 0.0F));
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(24, 12).addBox(-2.5F, 0.0F, -2.0F, 5.0F, 6.0F, 4.0F),
            PartPose.offset(0.0F, -0.9375F, 0.0F));
        partdefinition.addOrReplaceChild("headwear",
            CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, 2.0F, 6.0F, 6.0F, 2.0F),
            PartPose.offset(0.0F, -1.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_arm",
            CubeListBuilder.create().texOffs(12, 12).addBox(-2.0F, -1.0F, -1.5F, 3.0F, 5.0F, 3.0F),
            PartPose.offset(-3.5F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_arm",
            CubeListBuilder.create().texOffs(12, 12).mirror().addBox(-1.0F, -1.0F, -1.5F, 3.0F, 5.0F, 3.0F),
            PartPose.offset(3.5F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg",
            CubeListBuilder.create().texOffs(0, 12).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
            PartPose.offset(-1.5F, 5.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg",
            CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
            PartPose.offset(1.5F, 5.0F, 0.0F));
        partdefinition.addOrReplaceChild("stem",
            CubeListBuilder.create().texOffs(24, 8).addBox(-0.5F, -9.0F, 0.0F, 1.0F, 3.0F, 1.0F),
            PartPose.offset(0.0F, -1.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public static LayerDefinition createLeafLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F),
            PartPose.offset(0.0F, -1.0F, 0.0F));
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(24, 12).addBox(-2.5F, 0.0F, -2.0F, 5.0F, 6.0F, 4.0F),
            PartPose.offset(0.0F, -0.9375F, 0.0F));
        for (int i = 0; i < 3; ++i) {
            partdefinition.addOrReplaceChild("leaf" + i,
                CubeListBuilder.create().texOffs(0, 22).addBox(-1.5F, -9.0F, -5.25F, 3.0F, 1.0F, 5.0F),
                PartPose.offset(0.0F, -1.0F, 0.0F));
        }
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(EntityGroveSprite entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.bipedHead.yRot = netHeadYaw / 57.29578F;
        this.bipedHead.xRot = headPitch / 57.29578F;
        if (this.bipedHeadwear != null) {
            this.stem.yRot = this.bipedHeadwear.yRot = this.bipedHead.yRot;
            this.stem.xRot = this.bipedHeadwear.xRot = this.bipedHead.xRot;
            this.bipedLeftArm.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.8F * limbSwingAmount;
            this.bipedRightArm.xRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.8F * limbSwingAmount;
            this.bipedRightLeg.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.8F * limbSwingAmount;
            this.bipedLeftLeg.xRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.8F * limbSwingAmount;
        }

        if (this.bipedRightArm != null && entity != null && entity.isBegging()) {
            this.bipedRightArm.xRot = -1.3F;
        }

        if (this.leaf != null) {
            for (int i = 0; i < 3; ++i) {
                this.leaf[i].yRot = netHeadYaw / 57.29578F;
                this.leaf[i].xRot = headPitch / 57.29578F * 0.5F;
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        if (this.bipedHeadwear != null) {
            this.bipedRightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.bipedLeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.bipedRightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.bipedLeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.bipedHeadwear.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.stem.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }

        if (this.leaf != null) {
            for (int i = 0; i < 3; ++i) {
                poseStack.pushPose();
                poseStack.translate(0.0F, 0.8125F, 0.0F);
                poseStack.mulPose(Axis.YP.rotation(this.leaf[i].yRot));
                poseStack.mulPose(Axis.XP.rotation(this.leaf[i].xRot));
                poseStack.translate(0.0F, -0.8125F, 0.0F);
                this.leaf[i].xRot = 0.0F;
                this.leaf[i].yRot = 2.0F * ((float)i + 1.0F);
                this.leaf[i].render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
                poseStack.popPose();
            }
        }

        this.bipedHead.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.bipedBody.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}

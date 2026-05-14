package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.entity.passive.EntityChameleon;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelChameleon extends EntityModel<EntityChameleon> {
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart tail;
    private final ModelPart head;
    private final ModelPart head2;
    private final ModelPart eyeleft;
    private final ModelPart eyeright;
    private final ModelPart legfrontleft1;
    private final ModelPart legfrontright1;
    private final ModelPart legfrontleft2;
    private final ModelPart legfrontright2;
    private final ModelPart legbackleft1;
    private final ModelPart legbackright1;
    private final ModelPart legbackleft2;
    private final ModelPart legbackright2;
    public final ModelPart tongue1;
    public final ModelPart tongue2;
    private EntityChameleon currentEntity;

    public ModelChameleon(ModelPart root) {
        this.body1 = root.getChild("body1");
        this.body2 = root.getChild("body2");
        this.tail = root.getChild("tail");
        this.head = root.getChild("head");
        this.head2 = root.getChild("head2");
        this.eyeleft = root.getChild("eyeleft");
        this.eyeright = root.getChild("eyeright");
        this.legfrontleft1 = root.getChild("legfrontleft1");
        this.legfrontright1 = root.getChild("legfrontright1");
        this.legfrontleft2 = root.getChild("legfrontleft2");
        this.legfrontright2 = root.getChild("legfrontright2");
        this.legbackleft1 = root.getChild("legbackleft1");
        this.legbackright1 = root.getChild("legbackright1");
        this.legbackleft2 = root.getChild("legbackleft2");
        this.legbackright2 = root.getChild("legbackright2");
        this.tongue1 = root.getChild("tongue1");
        this.tongue2 = root.getChild("tongue2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body1",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -1.0F, 0.0F, 4.0F, 4.0F, 6.0F),
            PartPose.offsetAndRotation(0.0F, 20.0F, -4.0F, 0.1487144F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("body2",
            CubeListBuilder.create().texOffs(0, 11).mirror().addBox(-1.5F, -2.0F, 5.5F, 3.0F, 3.0F, 2.0F),
            PartPose.offsetAndRotation(0.0F, 20.0F, -4.0F, -0.0743572F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("tail",
            CubeListBuilder.create().texOffs(0, 17).mirror().addBox(-1.0F, -1.0F, -0.2F, 2.0F, 4.0F, 4.0F),
            PartPose.offsetAndRotation(0.0F, 20.0F, 3.0F, -0.4461433F, 0.0F, -0.5948578F));
        partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(25, 0).mirror().addBox(-1.5F, -1.5F, -3.5F, 3.0F, 3.0F, 4.0F),
            PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, 0.2974289F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("head2",
            CubeListBuilder.create().texOffs(25, 8).mirror().addBox(-1.0F, -2.0F, -3.0F, 2.0F, 4.0F, 2.0F),
            PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, -0.7063936F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("eyeleft",
            CubeListBuilder.create().texOffs(34, 8).mirror().addBox(1.3F, -0.5F, -2.0F, 1.0F, 1.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, 0.1115358F, 0.2230717F, -0.0743572F));
        partdefinition.addOrReplaceChild("eyeright",
            CubeListBuilder.create().texOffs(34, 11).mirror().addBox(-2.3F, -0.5F, -2.0F, 1.0F, 1.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, 0.1115358F, -0.2230705F, 0.074351F));
        partdefinition.addOrReplaceChild("legfrontleft1",
            CubeListBuilder.create().texOffs(45, 0).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
            PartPose.offsetAndRotation(2.0F, 21.0F, -3.5F, 1.115358F, 0.2974367F, 0.1115513F));
        partdefinition.addOrReplaceChild("legfrontright1",
            CubeListBuilder.create().texOffs(50, 0).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
            PartPose.offsetAndRotation(-2.0F, 21.0F, -3.5F, 1.115358F, -0.297439F, -0.111544F));
        partdefinition.addOrReplaceChild("legfrontleft2",
            CubeListBuilder.create().texOffs(45, 4).mirror().addBox(-0.5F, -0.5F, 2.0F, 1.0F, 3.0F, 1.0F),
            PartPose.offsetAndRotation(2.0F, 21.0F, -3.5F, -0.4635966F, 0.297439F, 0.111544F));
        partdefinition.addOrReplaceChild("legfrontright2",
            CubeListBuilder.create().texOffs(50, 4).mirror().addBox(-0.5F, -0.5F, 2.0F, 1.0F, 3.0F, 1.0F),
            PartPose.offsetAndRotation(-2.0F, 21.0F, -3.5F, -0.4635966F, -0.297439F, -0.111544F));
        partdefinition.addOrReplaceChild("legbackleft1",
            CubeListBuilder.create().texOffs(45, 9).mirror().addBox(-0.7F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
            PartPose.offsetAndRotation(1.0F, 21.0F, 3.0F, -1.152532F, -0.8179311F, -0.1487144F));
        partdefinition.addOrReplaceChild("legbackright1",
            CubeListBuilder.create().texOffs(50, 9).mirror().addBox(-0.3F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
            PartPose.offsetAndRotation(-1.0F, 21.0F, 3.0F, -1.152528F, 0.8179334F, 0.1487195F));
        partdefinition.addOrReplaceChild("legbackleft2",
            CubeListBuilder.create().texOffs(45, 13).mirror().addBox(-0.7F, -0.5F, -3.0F, 1.0F, 3.0F, 1.0F),
            PartPose.offsetAndRotation(1.0F, 21.0F, 3.0F, 0.426418F, -0.8179311F, -0.1487195F));
        partdefinition.addOrReplaceChild("legbackright2",
            CubeListBuilder.create().texOffs(50, 13).mirror().addBox(-0.3F, -0.5F, -3.0F, 1.0F, 3.0F, 1.0F),
            PartPose.offsetAndRotation(-1.0F, 21.0F, 3.0F, 0.426418F, 0.8179311F, 0.1487195F));
        partdefinition.addOrReplaceChild("tongue1",
            CubeListBuilder.create().texOffs(25, 15).mirror().addBox(-0.5F, 0.5F, -7.4F, 1.0F, 0.0F, 4.0F),
            PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, 0.2974216F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("tongue2",
            CubeListBuilder.create().texOffs(25, 15).mirror().addBox(-0.5F, 0.0F, -8.4F, 1.0F, 1.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, 0.2974216F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(EntityChameleon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.currentEntity = entity;
        this.head.xRot = headPitch / (180F / (float)Math.PI) + 0.2974289F;
        this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.head2.xRot = headPitch / (180F / (float)Math.PI) - 0.7063936F;
        this.head2.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.eyeleft.xRot = headPitch / (180F / (float)Math.PI) + 0.1396263F;
        this.eyeleft.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.eyeright.xRot = headPitch / (180F / (float)Math.PI) + 0.1396263F;
        this.eyeright.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.tongue1.xRot = headPitch / (180F / (float)Math.PI) + 0.2974216F;
        this.tongue1.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.tongue2.xRot = headPitch / (180F / (float)Math.PI) + 0.2974216F;
        this.tongue2.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.legfrontleft1.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount + 1.115358F;
        this.legfrontleft2.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount - 0.4635966F;
        this.legfrontright1.xRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount + 1.115358F;
        this.legfrontright2.xRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount - 0.4635966F;
        this.legbackleft1.xRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount - 1.152532F;
        this.legbackleft2.xRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount + 0.426418F;
        this.legbackright1.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount - 1.152532F;
        this.legbackright2.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount + 0.426418F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        int tintedColor = color;
        if (this.currentEntity != null) {
            float[] RGB = this.currentEntity.getSkinRGB();
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
        if (this.young) {
            float var8 = 2.0F;
            poseStack.pushPose();
            poseStack.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
            poseStack.translate(0.0F, 23.0F * 0.0625F, 0.0F);
            renderModel(poseStack, vertexConsumer, packedLight, packedOverlay, tintedColor);
            poseStack.popPose();
        } else {
            renderModel(poseStack, vertexConsumer, packedLight, packedOverlay, tintedColor);
        }
    }

    private void renderModel(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.body1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.body2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.head2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.eyeleft.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.eyeright.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legfrontleft1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legfrontright1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legfrontleft2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legfrontright2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legbackleft1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legbackright1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legbackleft2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legbackright2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}

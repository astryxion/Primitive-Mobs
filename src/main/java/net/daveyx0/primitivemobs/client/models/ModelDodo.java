package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ModelDodo<T extends Entity> extends EntityModel<T> {
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart beak;
    private final ModelPart wing1;
    private final ModelPart wing2;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart feet1;
    private final ModelPart feet2;
    private final ModelPart tail;

    public ModelDodo(ModelPart root) {
        this.body = root.getChild("body");
        this.neck = root.getChild("neck");
        this.beak = root.getChild("beak");
        this.wing1 = root.getChild("wing1");
        this.wing2 = root.getChild("wing2");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.feet1 = root.getChild("feet1");
        this.feet2 = root.getChild("feet2");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(0, 12).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 12.0F),
            PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, -0.1919862F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("neck",
            CubeListBuilder.create().texOffs(29, 0).mirror().addBox(-2.0F, -10.0F, -3.0F, 4.0F, 10.0F, 4.0F),
            PartPose.offsetAndRotation(0.0F, 16.0F, -6.0F, -0.2443461F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("beak",
            CubeListBuilder.create().texOffs(14, 0).mirror().addBox(-1.0F, -10.0F, -3.0F, 2.0F, 3.0F, 5.0F),
            PartPose.offsetAndRotation(0.0F, 16.0F, -6.0F, 0.2792527F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("wing1",
            CubeListBuilder.create().texOffs(0, 14).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 5.0F),
            PartPose.offsetAndRotation(4.0F, 12.0F, -3.0F, -0.1919862F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("wing2",
            CubeListBuilder.create().texOffs(28, 14).mirror().addBox(-1.0F, 0.0F, 0.0F, 1.0F, 5.0F, 5.0F),
            PartPose.offsetAndRotation(-4.0F, 12.0F, -3.0F, -0.1919862F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg1",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F),
            PartPose.offset(2.0F, 20.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg2",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F),
            PartPose.offset(-2.0F, 20.0F, 0.0F));
        partdefinition.addOrReplaceChild("feet1",
            CubeListBuilder.create().texOffs(1, 8).mirror().addBox(-1.5F, 3.0F, -2.0F, 3.0F, 1.0F, 3.0F),
            PartPose.offset(2.0F, 20.0F, 0.0F));
        partdefinition.addOrReplaceChild("feet2",
            CubeListBuilder.create().texOffs(1, 8).mirror().addBox(-1.5F, 3.0F, -2.0F, 3.0F, 1.0F, 3.0F),
            PartPose.offset(-2.0F, 20.0F, 0.0F));
        partdefinition.addOrReplaceChild("tail",
            CubeListBuilder.create().texOffs(4, 0).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F),
            PartPose.offset(0.0F, 13.0F, 5.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.neck.xRot = headPitch / 70.028175F - 0.2443461F;
        this.neck.yRot = netHeadYaw / 70.028175F;
        this.beak.xRot = headPitch / 70.028175F + 0.2792527F;
        this.beak.yRot = netHeadYaw / 70.028175F;
        this.leg2.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.feet1.xRot = this.leg1.xRot;
        this.feet2.xRot = this.leg2.xRot;
        this.wing2.zRot = ageInTicks;
        this.wing1.zRot = -ageInTicks;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        if (this.young) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 5.0F * 0.0625F, 2.0F * 0.0625F);
            this.neck.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.beak.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            poseStack.popPose();
            poseStack.pushPose();
            float f6 = 2.0F;
            poseStack.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
            poseStack.translate(0.0F, 24.0F * 0.0625F, 0.0F);
            this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.wing1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.wing2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.feet1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.feet2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            poseStack.popPose();
        } else {
            this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.neck.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.beak.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.wing1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.wing2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.feet1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.feet2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }
    }
}

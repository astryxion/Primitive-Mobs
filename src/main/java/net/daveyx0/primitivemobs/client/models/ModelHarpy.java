package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.entity.monster.EntityHarpy;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ModelHarpy<T extends Entity> extends EntityModel<T> {
    private final ModelPart body;
    private final ModelPart legLeftOverlay;
    private final ModelPart legRightOverlay;
    private final ModelPart head;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;
    private final ModelPart legRight;
    private final ModelPart legLeft;
    private final ModelPart tail;
    private State state;

    public ModelHarpy(ModelPart root) {
        this.state = State.STANDING;
        this.body = root.getChild("body");
        this.legLeftOverlay = root.getChild("leg_left_overlay");
        this.legRightOverlay = root.getChild("leg_right_overlay");
        this.head = root.getChild("head");
        this.wingLeft = root.getChild("wing_left");
        this.wingRight = root.getChild("wing_right");
        this.legRight = root.getChild("leg_right");
        this.legLeft = root.getChild("leg_left");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-4.0F, -6.0F, -3.0F, 10.0F, 14.0F, 8.0F),
            PartPose.offsetAndRotation(-1.0F, 10.0F, -2.0F, 0.1745329F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg_left_overlay",
            CubeListBuilder.create().texOffs(20, 38).mirror().addBox(-2.5F, -2.0F, -2.5F, 5.0F, 6.0F, 5.0F),
            PartPose.offset(3.0F, 18.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg_right_overlay",
            CubeListBuilder.create().texOffs(0, 38).mirror().addBox(-2.5F, -2.0F, -2.5F, 5.0F, 6.0F, 5.0F),
            PartPose.offset(-3.0F, 18.0F, 0.0F));
        partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F),
            PartPose.offset(0.0F, 7.0F, -4.0F));
        partdefinition.addOrReplaceChild("wing_left",
            CubeListBuilder.create().texOffs(44, 38).mirror().addBox(0.0F, 0.0F, -2.0F, 1.0F, 14.0F, 8.0F),
            PartPose.offsetAndRotation(5.0F, 5.0F, -4.0F, 0.2443461F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("wing_right",
            CubeListBuilder.create().texOffs(44, 16).mirror().addBox(-1.0F, 0.0F, -2.0F, 1.0F, 14.0F, 8.0F),
            PartPose.offsetAndRotation(-5.0F, 5.0F, -4.0F, 0.2443461F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg_right",
            CubeListBuilder.create().texOffs(0, 49).mirror().addBox(-2.0F, 1.0F, -2.0F, 4.0F, 5.0F, 4.0F),
            PartPose.offset(-3.0F, 18.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg_left",
            CubeListBuilder.create().texOffs(16, 49).mirror().addBox(-2.0F, 1.0F, -2.0F, 4.0F, 5.0F, 4.0F),
            PartPose.offset(3.0F, 18.0F, 0.0F));
        partdefinition.addOrReplaceChild("tail",
            CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-4.0F, 0.0F, 0.0F, 8.0F, 10.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, 17.0F, 3.0F, 1.047198F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.body.xRot = 0.2443461F;
        this.legLeft.xRot = 0.0F;
        this.legRight.xRot = 0.0F;
        if (entityIn instanceof EntityHarpy) {
            EntityHarpy entityharpy = (EntityHarpy)entityIn;
            if (entityharpy.isFlying()) {
                this.legLeft.xRot += 0.69813174F;
                this.legRight.xRot += 0.69813174F;
                this.state = State.FLYING;
            } else {
                this.state = State.STANDING;
            }
            this.legLeft.zRot = 0.0F;
            this.legRight.zRot = 0.0F;
        }
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.zRot = 0.0F;
        if (this.state != State.FLYING) {
            if (this.state == State.SITTING) {
                return;
            }
            this.legLeft.xRot += Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.legRight.xRot += Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        }

        float f = ageInTicks * 0.3F;
        this.wingLeft.zRot = -0.0873F - ageInTicks;
        this.wingLeft.y = 5.0F + f;
        this.wingRight.zRot = 0.0873F + ageInTicks;
        this.wingRight.y = 5.0F + f;
        this.legLeftOverlay.xRot = this.legLeft.xRot;
        this.legLeftOverlay.zRot = this.legLeft.zRot;
        this.legRightOverlay.xRot = this.legRight.xRot;
        this.legRightOverlay.zRot = this.legRight.zRot;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legLeftOverlay.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legRightOverlay.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.wingLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.wingRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.legLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    private static enum State {
        FLYING,
        STANDING,
        SITTING;
    }
}

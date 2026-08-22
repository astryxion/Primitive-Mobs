package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.entity.monster.EntityYeti;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ModelYeti extends EntityModel<EntityYeti> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart armLeft;
    public final ModelPart armRight;

    public ModelYeti(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.legLeft = root.getChild("leg_left");
        this.legRight = root.getChild("leg_right");
        this.armLeft = root.getChild("arm_left");
        this.armRight = root.getChild("arm_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -9.5F, -5.0F, 8.0F, 10.0F, 8.0F),
            PartPose.offset(0.0F, -9.0F, -1.0F));
        head.addOrReplaceChild("nose",
            CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-1.0F, -2.5F, -7.0F, 2.0F, 5.0F, 2.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1570796F, 0.0F, 0.0F));
        head.addOrReplaceChild("head_hide",
            CubeListBuilder.create().texOffs(0, 54).mirror().addBox(-4.5F, -10.0F, -5.5F, 9.0F, 11.0F, 9.0F),
            PartPose.ZERO);

        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-8.0F, -10.5F, -6.0F, 16.0F, 21.0F, 12.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 3.5F, 0.1745329F, 0.0F, 0.0F));

        PartDefinition legLeft = partdefinition.addOrReplaceChild("leg_left",
            CubeListBuilder.create().texOffs(69, 3).mirror().addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F),
            PartPose.offset(4.0F, 10.0F, 5.0F));
        legLeft.addOrReplaceChild("leg_left_hide",
            CubeListBuilder.create().texOffs(83, 24).mirror().addBox(-3.5F, 0.0F, -3.5F, 7.0F, 10.0F, 7.0F),
            PartPose.ZERO);

        PartDefinition legRight = partdefinition.addOrReplaceChild("leg_right",
            CubeListBuilder.create().texOffs(69, 3).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F),
            PartPose.offset(-4.0F, 10.0F, 5.0F));
        legRight.addOrReplaceChild("leg_right_hide",
            CubeListBuilder.create().texOffs(83, 24).mirror().addBox(-3.5F, 0.0F, -3.5F, 7.0F, 10.0F, 7.0F),
            PartPose.ZERO);

        PartDefinition armLeft = partdefinition.addOrReplaceChild("arm_left",
            CubeListBuilder.create().texOffs(57, 24).mirror().addBox(-6.0F, -3.0F, -3.0F, 6.0F, 23.0F, 6.0F),
            PartPose.offsetAndRotation(8.0F, -5.0F, 2.0F, 0.0F, (float)Math.PI, 0.0F));
        armLeft.addOrReplaceChild("arm_left_hide",
            CubeListBuilder.create().texOffs(57, 55).mirror().addBox(-6.5F, -3.5F, -3.5F, 7.0F, 17.0F, 7.0F),
            PartPose.ZERO);

        PartDefinition armRight = partdefinition.addOrReplaceChild("arm_right",
            CubeListBuilder.create().texOffs(57, 24).mirror().addBox(-6.0F, -3.0F, -3.0F, 6.0F, 23.0F, 6.0F),
            PartPose.offset(-8.0F, -5.0F, 2.0F));
        armRight.addOrReplaceChild("arm_right_hide",
            CubeListBuilder.create().texOffs(57, 55).mirror().addBox(-6.5F, -3.533333F, -3.5F, 7.0F, 17.0F, 7.0F),
            PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void prepareMobModel(EntityYeti entity, float limbSwing, float limbSwingAmount, float partialTick) {
        int attackTick = entity.getAttackAnimationTick();
        if (attackTick > 0) {
            float smash = 1.5F * Mth.triangleWave((float)attackTick - partialTick, 10.0F);
            this.armRight.xRot = -2.0F + smash;
            this.armLeft.xRot = 2.0F - smash;
        } else {
            float stride = 1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
            this.armRight.xRot = -stride;
            this.armLeft.xRot = -stride;
        }
    }

    @Override
    public void setupAnim(EntityYeti entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        float stride = 1.5F * Mth.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.legRight.xRot = stride;
        this.legLeft.xRot = -stride;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

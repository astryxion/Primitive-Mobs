package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ModelGoblin<T extends LivingEntity> extends HumanoidModel<T> {
    private final ModelPart armor;
    private final ModelPart bag;

    public ModelGoblin(ModelPart root) {
        super(root);
        this.armor = root.getChild("armor");
        this.bag = root.getChild("bag");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition headDef = partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -9.0F, -5.1F, 8.0F, 10.0F, 8.0F),
            PartPose.offset(0.0F, 7.0F, -3.0F));
        headDef.addOrReplaceChild("nose",
            CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-1.0F, -2.0F, -7.0F, 2.0F, 4.0F, 2.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3490659F, 0.0F, 0.0F));
        headDef.addOrReplaceChild("ear1",
            CubeListBuilder.create().texOffs(33, 0).mirror().addBox(0.0F, -9.0F, 0.0F, 1.0F, 3.0F, 5.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4537856F, 0.0F, 0.5759587F));
        headDef.addOrReplaceChild("ear2",
            CubeListBuilder.create().texOffs(46, 0).mirror().addBox(-1.0F, -9.0F, 0.0F, 1.0F, 3.0F, 5.0F),
            PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4537856F, 0.0F, -0.5759587F));
        headDef.addOrReplaceChild("helmet",
            CubeListBuilder.create().texOffs(0, 36).mirror().addBox(-4.5F, -9.5F, -5.5F, 9.0F, 11.0F, 9.0F),
            PartPose.ZERO);
        partdefinition.addOrReplaceChild("hat",
            CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
            PartPose.ZERO);
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(17, 20).mirror().addBox(-5.0F, 0.0F, -4.0F, 10.0F, 10.0F, 6.0F),
            PartPose.offset(0.0F, 6.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg",
            CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.1F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
            PartPose.offset(2.0F, 16.0F, -1.0F));
        partdefinition.addOrReplaceChild("left_leg",
            CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.9F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
            PartPose.offset(-2.0F, 16.0F, -1.0F));
        partdefinition.addOrReplaceChild("left_arm",
            CubeListBuilder.create().texOffs(48, 22).mirror().addBox(-4.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F),
            PartPose.offset(-5.0F, 7.0F, -1.0F));
        partdefinition.addOrReplaceChild("right_arm",
            CubeListBuilder.create().texOffs(48, 22).mirror().addBox(0.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F),
            PartPose.offset(5.0F, 7.0F, -1.0F));
        partdefinition.addOrReplaceChild("armor",
            CubeListBuilder.create().texOffs(37, 36).mirror().addBox(-5.5F, -0.5F, -4.5F, 11.0F, 11.0F, 7.0F),
            PartPose.offset(0.0F, 6.0F, 0.0F));
        partdefinition.addOrReplaceChild("bag",
            CubeListBuilder.create().texOffs(64, 0).mirror().addBox(-8.0F, -8.0F, 0.0F, 16.0F, 16.0F, 16.0F),
            PartPose.offsetAndRotation(0.0F, 8.0F, 2.0F, 0.1396263F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.head.y = 7.0F;
        this.head.z = -3.0F;
        this.body.y = 6.0F;
        this.rightArm.x = 5.0F;
        this.rightArm.y = 7.0F;
        this.rightArm.z = -1.0F;
        this.leftArm.x = -5.0F;
        this.leftArm.y = 7.0F;
        this.leftArm.z = -1.0F;
        this.leftLeg.y = 16.0F;
        this.leftLeg.z = -1.0F;
        this.rightLeg.y = 16.0F;
        this.rightLeg.z = -1.0F;
        this.bag.xRot = Mth.cos(limbSwing * 0.6662F * 1.0F + 0.0F) * 0.1F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.armor.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.bag.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    private float rotate(float p_78172_1_, float p_78172_2_) {
        return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
    }
}

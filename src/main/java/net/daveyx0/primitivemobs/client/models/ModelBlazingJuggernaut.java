package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ModelBlazingJuggernaut<T extends Entity> extends EntityModel<T> {
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightShoulder;
    private final ModelPart leftShoulder;
    private final ModelPart[] sticks = new ModelPart[12];

    public ModelBlazingJuggernaut(ModelPart root) {
        this.head = root.getChild("head");
        this.rightArm = root.getChild("right_arm");
        this.body = root.getChild("body");
        this.leftArm = root.getChild("left_arm");
        this.rightShoulder = root.getChild("right_shoulder");
        this.leftShoulder = root.getChild("left_shoulder");
        for (int i = 0; i < this.sticks.length; ++i) {
            this.sticks[i] = root.getChild("stick" + i);
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head",
            CubeListBuilder.create().texOffs(8, 17).mirror().addBox(-3.0F, -5.0F, -2.5F, 6.0F, 5.0F, 5.0F),
            PartPose.offset(0.0F, -1.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_arm",
            CubeListBuilder.create().texOffs(0, 27).mirror().addBox(5.0F, -4.5F, -2.5F, 3.0F, 9.0F, 4.0F),
            PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -0.0698132F, -0.0349066F));
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-6.0F, -4.5F, -4.0F, 12.0F, 9.0F, 8.0F),
            PartPose.offset(0.0F, 4.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_arm",
            CubeListBuilder.create().texOffs(0, 27).mirror().addBox(-8.0F, -4.5F, -2.5F, 3.0F, 9.0F, 4.0F),
            PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0698132F, 0.0349066F));
        partdefinition.addOrReplaceChild("right_shoulder",
            CubeListBuilder.create().texOffs(30, 17).mirror().addBox(3.5F, -7.5F, -4.5F, 5.0F, 5.0F, 7.0F),
            PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -0.1396263F, 0.0F));
        partdefinition.addOrReplaceChild("left_shoulder",
            CubeListBuilder.create().texOffs(30, 17).mirror().addBox(-8.5F, -7.5F, -4.5F, 5.0F, 5.0F, 7.0F),
            PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.1396263F, 0.0F));
        for (int i = 0; i < 12; ++i) {
            partdefinition.addOrReplaceChild("stick" + i,
                CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F),
                PartPose.ZERO);
        }
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f6 = ageInTicks * (float)Math.PI * -0.1F;

        for (int i = 0; i < 4; ++i) {
            this.sticks[i].y = 6.0F + Mth.cos(((float)(i * 2) + ageInTicks) * 0.25F);
            this.sticks[i].x = Mth.cos(f6) * 9.0F;
            this.sticks[i].z = Mth.sin(f6) * 9.0F;
            ++f6;
        }

        f6 = ((float)Math.PI / 4F) + ageInTicks * (float)Math.PI * 0.03F;

        for (int var12 = 4; var12 < 8; ++var12) {
            this.sticks[var12].y = 5.0F + Mth.cos(((float)(var12 * 2) + ageInTicks) * 0.25F);
            this.sticks[var12].x = Mth.cos(f6) * 11.0F;
            this.sticks[var12].z = Mth.sin(f6) * 11.0F;
            ++f6;
        }

        f6 = 0.47123894F + ageInTicks * (float)Math.PI * -0.05F;

        for (int var13 = 8; var13 < 12; ++var13) {
            this.sticks[var13].y = 4.0F + Mth.cos(((float)var13 * 1.5F + ageInTicks) * 0.5F);
            this.sticks[var13].x = Mth.cos(f6) * 13.0F;
            this.sticks[var13].z = Mth.sin(f6) * 13.0F;
            ++f6;
        }

        this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.head.xRot = headPitch / (180F / (float)Math.PI);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.rightShoulder.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.leftShoulder.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        for (int i = 0; i < this.sticks.length; ++i) {
            this.sticks[i].render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }
    }
}

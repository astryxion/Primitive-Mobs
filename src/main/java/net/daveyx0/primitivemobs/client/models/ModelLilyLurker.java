package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class ModelLilyLurker<T extends Entity> extends EntityModel<T> {
    private final ModelPart body1;
    private final ModelPart fin1;
    private final ModelPart fin2;
    private final ModelPart body2;
    private final ModelPart fin3;
    private final ModelPart fin4;
    public final ModelPart root1;
    private final ModelPart root2;
    private T currentEntity;

    public ModelLilyLurker(ModelPart root) {
        this.body1 = root.getChild("body1");
        this.fin1 = root.getChild("fin1");
        this.fin2 = root.getChild("fin2");
        this.body2 = root.getChild("body2");
        this.fin3 = root.getChild("fin3");
        this.fin4 = root.getChild("fin4");
        this.root1 = root.getChild("root1");
        this.root2 = root.getChild("root2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body1",
            CubeListBuilder.create().texOffs(28, 0).mirror().addBox(-4.0F, 0.0F, -5.0F, 8.0F, 4.0F, 10.0F),
            PartPose.offset(0.0F, 20.0F, 0.0F));
        partdefinition.addOrReplaceChild("fin1",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, -1.5F, 6.0F, 1.0F, 3.0F),
            PartPose.offsetAndRotation(3.0F, 22.0F, -1.0F, 0.0F, -0.6806784F, 0.0F));
        partdefinition.addOrReplaceChild("fin2",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, -1.5F, 6.0F, 1.0F, 3.0F),
            PartPose.offsetAndRotation(-3.0F, 22.0F, -1.0F, 0.0F, -2.460914F, 0.0F));
        partdefinition.addOrReplaceChild("body2",
            CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-3.0F, -1.0F, 0.0F, 6.0F, 3.0F, 2.0F),
            PartPose.offset(0.0F, 22.0F, 5.0F));
        partdefinition.addOrReplaceChild("fin3",
            CubeListBuilder.create().texOffs(0, 4).mirror().addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 5.0F),
            PartPose.offsetAndRotation(0.0F, 22.0F, 7.0F, 0.0F, 0.4712389F, 0.0F));
        partdefinition.addOrReplaceChild("fin4",
            CubeListBuilder.create().texOffs(0, 4).mirror().addBox(-2.0F, 0.0F, 0.0F, 2.0F, 1.0F, 5.0F),
            PartPose.offsetAndRotation(0.0F, 22.0F, 7.0F, 0.0F, -0.4712389F, 0.0F));
        partdefinition.addOrReplaceChild("root1",
            CubeListBuilder.create().texOffs(18, 0).mirror().addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, 21.0F, -2.0F, -0.6981317F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("root2",
            CubeListBuilder.create().texOffs(18, 0).mirror().addBox(-0.5F, -4.0F, -0.5F, 1.0F, 5.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, 16.5F, 1.0F, 0.3316126F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.currentEntity = entity;
        this.fin1.yRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount - 0.6806784F;
        this.fin2.yRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount - 2.460914F;
        this.fin3.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount;
        this.fin4.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.6F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.body1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.fin1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.fin2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.body2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.fin3.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.fin4.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        if (this.currentEntity != null) {
            this.renderRoots(poseStack, vertexConsumer, packedLight, packedOverlay, color, this.currentEntity);
        }
    }

    public void renderRoots(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color, T entity) {
        if (entity != null && entity instanceof Mob) {
            Mob living = (Mob)entity;
            if (!living.getMainHandItem().isEmpty()) {
                this.root1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
                this.root2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            }
        }
    }
}

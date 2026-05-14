package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelVoidEye<T extends Entity> extends EntityModel<T> {
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart eye;
    private final boolean renderBody;

    public ModelVoidEye(ModelPart root, boolean renderBody) {
        this.renderBody = renderBody;
        this.body1 = root.getChild("body1");
        this.body2 = root.getChild("body2");
        this.eye = root.getChild("eye");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body1",
            CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-4.5F, -4.5F, -4.5F, 9.0F, 9.0F, 9.0F),
            PartPose.offset(0.0F, 17.0F, 0.0F));
        partdefinition.addOrReplaceChild("body2",
            CubeListBuilder.create().texOffs(0, 28).mirror().addBox(-6.5F, -6.5F, -6.5F, 13.0F, 13.0F, 19.0F),
            PartPose.offset(0.0F, 17.0F, 0.0F));
        partdefinition.addOrReplaceChild("eye",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F),
            PartPose.offset(0.0F, 17.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.body1.zRot -= 0.0075F;
        this.body2.zRot += 0.0075F;
        this.eye.yRot = netHeadYaw / 57.29578F;
        this.eye.xRot = headPitch / 57.29578F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        if (this.renderBody) {
            this.body1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            this.body2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }
        this.eye.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}

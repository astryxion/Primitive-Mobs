package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelMimic<T extends Entity> extends EntityModel<T> {
    private final ModelPart Top;
    private final ModelPart Lock;
    private final ModelPart Bottom;
    private final ModelPart Teeth;

    public ModelMimic(ModelPart root) {
        this.Top = root.getChild("top");
        this.Lock = root.getChild("lock");
        this.Bottom = root.getChild("bottom");
        this.Teeth = root.getChild("teeth");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("top",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-7.0F, -5.0F, -14.0F, 14.0F, 5.0F, 14.0F),
            PartPose.offset(0.0F, 15.0F, 7.0F));
        partdefinition.addOrReplaceChild("lock",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -2.0F, -15.0F, 2.0F, 4.0F, 1.0F),
            PartPose.offset(0.0F, 15.0F, 7.0F));
        partdefinition.addOrReplaceChild("bottom",
            CubeListBuilder.create().texOffs(0, 19).mirror().addBox(-7.0F, 0.0F, -7.0F, 14.0F, 10.0F, 14.0F),
            PartPose.offset(0.0F, 14.0F, 0.0F));
        partdefinition.addOrReplaceChild("teeth",
            CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-6.5F, -1.0F, -6.5F, 13.0F, 1.0F, 13.0F),
            PartPose.offset(0.0F, 14.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.Top.xRot = ageInTicks;
        this.Lock.xRot = ageInTicks;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.Top.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.Lock.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.Bottom.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.Teeth.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

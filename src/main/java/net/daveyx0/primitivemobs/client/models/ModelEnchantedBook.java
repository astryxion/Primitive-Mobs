package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelEnchantedBook<T extends Entity> extends EntityModel<T> {
    private final ModelPart book1;
    private final ModelPart book2;
    private final ModelPart pages1;
    private final ModelPart pages2;
    private final ModelPart page1;
    private final ModelPart page2;

    public ModelEnchantedBook(ModelPart root) {
        this.book1 = root.getChild("book1");
        this.book2 = root.getChild("book2");
        this.pages1 = root.getChild("pages1");
        this.pages2 = root.getChild("pages2");
        this.page1 = root.getChild("page1");
        this.page2 = root.getChild("page2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("book1",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-6.0F, -5.0F, 0.0F, 6.0F, 10.0F, 0.0F),
            PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 1.570796F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("book2",
            CubeListBuilder.create().texOffs(16, 0).mirror().addBox(0.0F, -5.0F, 0.0F, 6.0F, 10.0F, 0.0F),
            PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 1.570796F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("pages1",
            CubeListBuilder.create().texOffs(0, 10).mirror().addBox(-5.0F, -4.0F, -1.1F, 5.0F, 8.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 1.570796F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("pages2",
            CubeListBuilder.create().texOffs(12, 10).mirror().addBox(0.0F, -4.0F, -1.1F, 5.0F, 8.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 1.570796F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("page1",
            CubeListBuilder.create().texOffs(24, 10).mirror().addBox(0.0F, -4.0F, -1.1F, 5.0F, 8.0F, 0.0F),
            PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 1.570796F, 0.0F, ((float)Math.PI / 6F)));
        partdefinition.addOrReplaceChild("page2",
            CubeListBuilder.create().texOffs(24, 10).mirror().addBox(-5.0F, -4.0F, -1.1F, 5.0F, 8.0F, 0.0F),
            PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 1.570796F, 0.0F, (-(float)Math.PI / 6F)));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.book1.zRot = ageInTicks;
        this.book2.zRot = -ageInTicks;
        this.pages1.zRot = ageInTicks;
        this.pages2.zRot = -ageInTicks;
        this.page1.zRot = ageInTicks + ((float)Math.PI / 6F);
        this.page2.zRot = -ageInTicks - ((float)Math.PI / 6F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.book1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.book2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.pages1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.pages2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.page1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        this.page2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}

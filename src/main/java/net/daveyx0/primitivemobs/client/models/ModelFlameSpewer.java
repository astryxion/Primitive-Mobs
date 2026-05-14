package net.daveyx0.primitivemobs.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.daveyx0.primitivemobs.entity.monster.EntityFlameSpewer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ModelFlameSpewer extends EntityModel<EntityFlameSpewer> {
    private final ModelPart body;
    private final ModelPart mouth;
    private final ModelPart[] tentacles = new ModelPart[8];
    private final float[] offset = new float[8];
    private final boolean renderTentacles;
    private final boolean isLava;

    public ModelFlameSpewer(ModelPart root, boolean isLava, boolean renderTentacles) {
        this.isLava = isLava;
        this.renderTentacles = renderTentacles;
        this.body = root.getChild("body");
        this.mouth = root.getChild("mouth");
        for (int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i] = root.getChild("tentacle" + i);
            this.offset[i] = 0.0F;
        }
    }

    public static LayerDefinition createBodyLayer(boolean isLava) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body",
            CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-7.0F, -16.0F, -7.0F, 14.0F, 16.0F, 14.0F),
            PartPose.offset(0.0F, 21.0F, 0.0F));
        int mouthTexU = isLava ? 16 : 43;
        partdefinition.addOrReplaceChild("mouth",
            CubeListBuilder.create().texOffs(mouthTexU, 0).mirror().addBox(-2.0F, -5.0F, -10.0F, 4.0F, 4.0F, 4.0F),
            PartPose.offsetAndRotation(0.0F, 21.0F, 0.0F, 0.1570796F, 0.0F, 0.0F));
        int tentTexV = isLava ? 16 : 30;
        for (int i = 0; i < 8; ++i) {
            partdefinition.addOrReplaceChild("tentacle" + i,
                CubeListBuilder.create().texOffs(0, tentTexV).mirror().addBox(-2.0F, 0.0F, -26.0F, 4.0F, 4.0F, 24.0F),
                PartPose.offsetAndRotation(0.0F, 18.0F, 0.0F, 0.4537856F, (float)Math.toRadians((double)22.5F + (double)(45 * i)), 0.0F));
        }
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(EntityFlameSpewer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.body.yRot = netHeadYaw / (180F / (float)Math.PI);
        this.mouth.yRot = this.body.yRot;
        if (!this.isLava) {
            for (int i = 0; i < this.tentacles.length; ++i) {
                if (this.offset[i] == 0.0F) {
                    this.offset[i] = entity.getRandom().nextFloat() - entity.getRandom().nextFloat() + 0.01F;
                }
                if (entity.isInDanger()) {
                    idleTentacle(this.tentacles[i], (float)entity.tickCount * 0.75F + this.offset[i], 10.0F);
                } else {
                    idleTentacle(this.tentacles[i], (float)entity.tickCount * 0.15F + this.offset[i], 4.0F);
                }
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        EntityFlameSpewer spewer = null;
        poseStack.pushPose();
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        poseStack.popPose();
        poseStack.pushPose();
        this.mouth.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        poseStack.popPose();
        if (this.renderTentacles) {
            for (int i = 0; i < this.tentacles.length; ++i) {
                this.tentacles[i].render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            }
        }
        Item mob = Items.SHIELD;
    }

    public void renderWithScale(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color, EntityFlameSpewer spewer) {
        poseStack.pushPose();
        float f = 1.0F + spewer.getAttackSignal();
        poseStack.translate(0.0F, (double)(-f) + 0.9, 0.0F);
        poseStack.scale(f, f, f);
        this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        poseStack.popPose();
        poseStack.pushPose();
        float e = Mth.sin((float)spewer.tickCount * 0.5F) * 0.03F + 1.0F;
        poseStack.translate(0.0F, -0.1, 0.0F);
        poseStack.scale(e, e, e);
        this.mouth.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        poseStack.popPose();
        if (this.renderTentacles) {
            for (int i = 0; i < this.tentacles.length; ++i) {
                this.tentacles[i].render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
            }
        }
        Item mob = Items.SHIELD;
    }

    public static void idleTentacle(ModelPart model, float e, float f) {
        model.xRot = Mth.sin(e) * f * ((float)Math.PI / 180F);
    }

    public void setFlameSpewerModelAttributes(ModelFlameSpewer model) {
        for (int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i].xRot = model.tentacles[i].xRot;
        }
    }
}

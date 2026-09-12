package com.mongoose.clanginghowl.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class BloodyBatteryModel extends HumanoidModel<LivingEntity> {

	public BloodyBatteryModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.25F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(1, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.4F))
				.texOffs(13, 18).addBox(0.0F, 9.0F, 2.6F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.5F))
				.texOffs(31, 11).addBox(3.0F, 13.0F, 4.6F, -3.0F, -4.0F, -2.0F, new CubeDeformation(-0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Chestplate_r1 = Body.addOrReplaceChild("Chestplate_r1", CubeListBuilder.create().texOffs(8, 20).mirror().addBox(0.0F, -1.0F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.2F, 8.5F, 3.8F, 0.0F, 0.0F, 0.6981F));

		PartDefinition Chestplate_r2 = Body.addOrReplaceChild("Chestplate_r2", CubeListBuilder.create().texOffs(8, 20).addBox(0.0F, -1.0F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.7F, 8.5F, 3.8F, 0.0F, 0.0F, -0.6981F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		this.body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}
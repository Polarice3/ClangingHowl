package com.mongoose.clanginghowl.client.render.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class XRayGogglesModel extends HumanoidModel<LivingEntity> {

	public XRayGogglesModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.25F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(33, 117).addBox(-5.0F, -6.0F, -6.0F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.2F))
				.texOffs(0, 117).addBox(-5.0F, -6.0F, -6.1F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.3F))
				.texOffs(31, 99).addBox(4.8F, -5.05F, -4.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.1F))
				.texOffs(31, 99).mirror().addBox(-5.8F, -5.05F, -4.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false)
				.texOffs(35, 107).addBox(-4.5F, -4.9F, -0.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		this.head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}
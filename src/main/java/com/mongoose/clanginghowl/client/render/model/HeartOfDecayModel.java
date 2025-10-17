package com.mongoose.clanginghowl.client.render.model;

import com.mongoose.clanginghowl.client.render.animation.HeartOfDecayAnimations;
import com.mongoose.clanginghowl.common.entities.hostiles.HeartOfDecay;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class HeartOfDecayModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart main;
	private final ModelPart body_main;
	private final ModelPart left_leg1;
	private final ModelPart left_claw_1;
	private final ModelPart right_leg1;
	private final ModelPart right_claw_1;
	private final ModelPart left_leg3;
	private final ModelPart left_claw_3;
	private final ModelPart right_leg3;
	private final ModelPart right_claw_3;
	private final ModelPart left_leg2;
	private final ModelPart left_claw_2;
	private final ModelPart right_leg2;
	private final ModelPart right_claw_2;
	private final ModelPart body;
	private final ModelPart torso;
	private final ModelPart tail;
	private final ModelPart tail2;
	private final ModelPart sting;

	public HeartOfDecayModel(ModelPart root) {
		this.root = root;
		this.main = root.getChild("main");
		this.body_main = this.main.getChild("body_main");
		this.left_leg1 = this.body_main.getChild("left_leg1");
		this.left_claw_1 = this.left_leg1.getChild("left_claw_1");
		this.right_leg1 = this.body_main.getChild("right_leg1");
		this.right_claw_1 = this.right_leg1.getChild("right_claw_1");
		this.left_leg3 = this.body_main.getChild("left_leg3");
		this.left_claw_3 = this.left_leg3.getChild("left_claw_3");
		this.right_leg3 = this.body_main.getChild("right_leg3");
		this.right_claw_3 = this.right_leg3.getChild("right_claw_3");
		this.left_leg2 = this.body_main.getChild("left_leg2");
		this.left_claw_2 = this.left_leg2.getChild("left_claw_2");
		this.right_leg2 = this.body_main.getChild("right_leg2");
		this.right_claw_2 = this.right_leg2.getChild("right_claw_2");
		this.body = this.body_main.getChild("body");
		this.torso = this.body.getChild("torso");
		this.tail = this.body.getChild("tail");
		this.tail2 = this.tail.getChild("tail2");
		this.sting = this.tail2.getChild("sting");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 19.7857F, -0.2286F));

		PartDefinition body_main = main.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(-0.075F, 0.9393F, 0.2036F));

		PartDefinition left_leg1 = body_main.addOrReplaceChild("left_leg1", CubeListBuilder.create(), PartPose.offset(3.175F, 0.075F, 0.025F));

		PartDefinition cube_r1 = left_leg1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(26, 6).addBox(-0.1F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition left_claw_1 = left_leg1.addOrReplaceChild("left_claw_1", CubeListBuilder.create(), PartPose.offset(2.5259F, 1.4138F, 0.0F));

		PartDefinition cube_r2 = left_claw_1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(44, 0).addBox(-1.2F, -1.5F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4F, 0.3887F, 0.0F, 0.0F, 0.0F, 0.0087F));

		PartDefinition right_leg1 = body_main.addOrReplaceChild("right_leg1", CubeListBuilder.create(), PartPose.offset(-3.025F, 0.075F, 0.025F));

		PartDefinition cube_r3 = right_leg1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(26, 6).mirror().addBox(-3.9F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		PartDefinition right_claw_1 = right_leg1.addOrReplaceChild("right_claw_1", CubeListBuilder.create(), PartPose.offset(-2.5259F, 1.4138F, 0.0F));

		PartDefinition cube_r4 = right_claw_1.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(44, 0).mirror().addBox(-3.8F, -1.5F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.4F, 0.3887F, 0.0F, 0.0F, 0.0F, -0.0087F));

		PartDefinition left_leg3 = body_main.addOrReplaceChild("left_leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0464F, 0.175F, -2.1282F, 0.0F, 0.6981F, 0.0F));

		PartDefinition cube_r5 = left_leg3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(26, 6).addBox(-0.1F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition left_claw_3 = left_leg3.addOrReplaceChild("left_claw_3", CubeListBuilder.create(), PartPose.offset(2.5259F, 1.4138F, 0.0F));

		PartDefinition cube_r6 = left_claw_3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(44, 0).addBox(-1.2F, -1.5F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4F, 0.3887F, 0.0F, 0.0F, 0.0F, 0.0087F));

		PartDefinition right_leg3 = body_main.addOrReplaceChild("right_leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.8964F, 0.175F, -2.1282F, 0.0F, -0.6981F, 0.0F));

		PartDefinition cube_r7 = right_leg3.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(26, 6).mirror().addBox(-3.9F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		PartDefinition right_claw_3 = right_leg3.addOrReplaceChild("right_claw_3", CubeListBuilder.create(), PartPose.offset(-2.5259F, 1.4138F, 0.0F));

		PartDefinition cube_r8 = right_claw_3.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(44, 0).mirror().addBox(-3.8F, -1.5F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.4F, 0.3887F, 0.0F, 0.0F, 0.0F, -0.0087F));

		PartDefinition left_leg2 = body_main.addOrReplaceChild("left_leg2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.175F, 0.175F, 2.025F, 0.0F, -0.6109F, 0.0F));

		PartDefinition cube_r9 = left_leg2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(26, 6).addBox(-0.1F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition left_claw_2 = left_leg2.addOrReplaceChild("left_claw_2", CubeListBuilder.create(), PartPose.offset(2.5259F, 1.4138F, 0.0F));

		PartDefinition cube_r10 = left_claw_2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(44, 0).addBox(-1.2F, -1.5F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4F, 0.3887F, 0.0F, 0.0F, 0.0F, 0.0087F));

		PartDefinition right_leg2 = body_main.addOrReplaceChild("right_leg2", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.025F, 0.175F, 2.025F, 0.0F, 0.6109F, 0.0F));

		PartDefinition cube_r11 = right_leg2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(26, 6).mirror().addBox(-3.9F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		PartDefinition right_claw_2 = right_leg2.addOrReplaceChild("right_claw_2", CubeListBuilder.create(), PartPose.offset(-2.5259F, 1.4138F, 0.0F));

		PartDefinition cube_r12 = right_claw_2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(44, 0).mirror().addBox(-3.8F, -1.5F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.4F, 0.3887F, 0.0F, 0.0F, 0.0F, -0.0087F));

		PartDefinition body = body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(26, 0).addBox(-1.5F, -0.7F, -6.6F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.075F, -1.025F, -0.075F, 0.0524F, 0.0F, 0.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.5F, -4.0F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -0.2F, 0.6F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(32, 21).addBox(-1.0F, -1.1028F, -0.8561F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(20, 12).addBox(-1.0F, -1.1028F, 0.1439F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6667F, 3.45F, -0.1745F, 0.0F, 0.0F));

		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -0.95F, -0.075F, 2.0F, 2.0F, 8.0F, new CubeDeformation(-0.01F))
		.texOffs(14, 22).addBox(-1.0F, -0.95F, 5.925F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -0.1528F, 7.0189F, 0.1745F, 0.0F, 0.0F));

		PartDefinition sting = tail2.addOrReplaceChild("sting", CubeListBuilder.create().texOffs(0, 22).addBox(-1.5F, -1.6F, 0.2F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.2F))
		.texOffs(20, 21).addBox(0.0F, -4.6F, 1.2F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.05F, 6.725F, 0.2269F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		/*this.body_main.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.body_main.xRot = headPitch * ((float)Math.PI / 180F);*/
		if (entity instanceof HeartOfDecay hod){
			this.animate(hod.idleAnimationState, HeartOfDecayAnimations.IDLE, ageInTicks);
			this.animateWalk(HeartOfDecayAnimations.WALK, limbSwing, limbSwingAmount, 2.5F, 20.0F);
			this.animate(hod.attackAnimationState, HeartOfDecayAnimations.ATTACK, ageInTicks);
			this.animate(hod.spitAnimationState, HeartOfDecayAnimations.SPIT, ageInTicks);
			this.animate(hod.appearAnimationState, HeartOfDecayAnimations.APPEAR, ageInTicks);
		}
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}
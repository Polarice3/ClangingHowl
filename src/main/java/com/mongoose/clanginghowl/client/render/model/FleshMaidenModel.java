package com.mongoose.clanginghowl.client.render.model;

import com.mongoose.clanginghowl.client.render.animation.FleshMaidenAnimations;
import com.mongoose.clanginghowl.common.entities.hostiles.FleshMaiden;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class FleshMaidenModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart main;
	private final ModelPart body_main;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart left_jaw;
	private final ModelPart right_jaw;
	private final ModelPart whip;
	private final ModelPart whip2;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	public FleshMaidenModel(ModelPart root) {
		this.root = root;
		this.main = root.getChild("main");
		this.body_main = this.main.getChild("body_main");
		this.body = this.body_main.getChild("body");
		this.left_arm = this.body.getChild("left_arm");
		this.right_arm = this.body.getChild("right_arm");
		this.left_jaw = this.body.getChild("left_jaw");
		this.right_jaw = this.body.getChild("right_jaw");
		this.whip = this.body.getChild("whip");
		this.whip2 = this.whip.getChild("whip2");
		this.left_leg = this.main.getChild("left_leg");
		this.right_leg = this.main.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body_main = main.addOrReplaceChild("body_main", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -3.5944F, -2.8774F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.4056F, -0.1226F));

		PartDefinition body = body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -13.3943F, 0.1226F, 12.0F, 15.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -3.2F, 0.0F, -0.2269F, 0.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(6.4198F, -10.3799F, 1.5746F, 0.0265F, 0.0024F, 0.1856F));

		PartDefinition cube_r1 = left_arm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(68, 5).addBox(3.0614F, 0.0907F, -15.5804F, 6.0F, 0.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(48, 47).addBox(4.0614F, -1.9093F, -5.5804F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 28).addBox(0.0614F, -1.9093F, -1.5804F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1306F, -0.4016F, 0.7555F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.4198F, -10.3799F, 1.5746F, -0.0048F, 0.1405F, -0.2283F));

		PartDefinition cube_r2 = right_arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(68, 5).mirror().addBox(-9.0614F, 0.0907F, -15.5804F, 6.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(48, 47).mirror().addBox(-8.0614F, -1.9093F, -5.5804F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(44, 28).mirror().addBox(-8.0614F, -1.9093F, -1.5804F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1306F, 0.4016F, -0.7555F));

		PartDefinition left_jaw = body.addOrReplaceChild("left_jaw", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0647F, -5.9757F, 0.0028F, -0.022F, 0.1555F, 0.1554F));

		PartDefinition cube_r3 = left_jaw.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-1, 75).addBox(-3.3931F, -6.5692F, -2.3373F, 7.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8948F, 0.1533F, -1.9408F, 0.0626F, -0.8254F, -0.1243F));

		PartDefinition cube_r4 = left_jaw.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(38, 53).addBox(-3.4955F, -7.6169F, -1.5356F, 3.0F, 15.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(-0.4955F, -7.6169F, -1.5356F, 6.0F, 15.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.4167F, 0.2033F, -4.9339F, 0.0F, -1.0001F, 0.0F));

		PartDefinition right_jaw = body.addOrReplaceChild("right_jaw", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0647F, -5.9757F, 0.0028F, -0.022F, -0.1555F, -0.1554F));

		PartDefinition cube_r5 = right_jaw.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(-1, 75).mirror().addBox(-3.6069F, -6.5692F, -2.3373F, 7.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8948F, 0.1533F, -1.9408F, 0.0626F, 0.8254F, 0.1243F));

		PartDefinition cube_r6 = right_jaw.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(38, 53).mirror().addBox(0.4955F, -7.6169F, -1.5356F, 3.0F, 15.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 19).mirror().addBox(-5.5045F, -7.6169F, -1.5356F, 6.0F, 15.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.4167F, 0.2033F, -4.9339F, 0.0F, 1.0001F, 0.0F));

		PartDefinition whip = body.addOrReplaceChild("whip", CubeListBuilder.create().texOffs(16, 47).addBox(-1.0F, -10.025F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(48, 36).addBox(-4.0F, -10.025F, 0.0F, 8.0F, 11.0F, 0.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -9.7319F, 2.3466F, -3.1241F, 0.0F, 0.0F));

		PartDefinition whip2 = whip.addOrReplaceChild("whip2", CubeListBuilder.create().texOffs(28, 39).addBox(-5.0F, -13.999F, -0.1134F, 10.0F, 14.0F, 0.0F, new CubeDeformation(-0.01F))
		.texOffs(24, 53).addBox(-1.0F, -11.999F, -1.1134F, 2.0F, 12.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -10.0241F, 0.1047F, -3.1241F, 0.0F, 0.0F));

		PartDefinition left_leg = main.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(3.0F, -10.4F, 0.0F));

		PartDefinition cube_r7 = left_leg.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(44, 12).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.4F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition right_leg = main.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-3.0F, -10.4F, 0.0F));

		PartDefinition cube_r8 = right_leg.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(44, 12).mirror().addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.4F, 0.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		if (entity instanceof FleshMaiden maiden){
			this.animate(maiden.idleAnimationState, FleshMaidenAnimations.IDLE, ageInTicks);
			if (maiden.isAggressive()) {
				this.animateWalk(FleshMaidenAnimations.AGGRESSION, limbSwing, limbSwingAmount, 2.5F, 20.0F);
			} else {
				this.animateWalk(FleshMaidenAnimations.WALK, limbSwing, limbSwingAmount, 2.5F, 20.0F);
			}
			this.animate(maiden.attackAnimationState, FleshMaidenAnimations.ATTACK, ageInTicks);
			this.animate(maiden.longAttackAnimationState, FleshMaidenAnimations.LONG_ATTACK, ageInTicks);
			this.animate(maiden.appearAnimationState, FleshMaidenAnimations.APPEAR, ageInTicks);
		}
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}
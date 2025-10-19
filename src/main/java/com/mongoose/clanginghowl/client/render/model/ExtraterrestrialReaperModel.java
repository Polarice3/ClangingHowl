package com.mongoose.clanginghowl.client.render.model;

import com.mongoose.clanginghowl.client.render.animation.ExtraterrestrialReaperAnimations;
import com.mongoose.clanginghowl.common.entities.hostiles.ExReaper;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ExtraterrestrialReaperModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart main;
	private final ModelPart body_main;
	private final ModelPart body_top;
	private final ModelPart body;
	private final ModelPart right_arm;
	private final ModelPart right_claw;
	private final ModelPart left_arm;
	private final ModelPart needle;
	private final ModelPart right_leg;
	private final ModelPart left_leg;

	public ExtraterrestrialReaperModel(ModelPart root) {
		this.root = root;
		this.main = root.getChild("main");
		this.body_main = this.main.getChild("body_main");
		this.body_top = this.body_main.getChild("body_top");
		this.body = this.body_top.getChild("body");
		this.right_arm = this.body_top.getChild("right_arm");
		this.right_claw = this.right_arm.getChild("right_claw");
		this.left_arm = this.body_top.getChild("left_arm");
		this.needle = this.left_arm.getChild("needle");
		this.right_leg = this.main.getChild("right_leg");
		this.left_leg = this.main.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 20.9F, 2.0F));

		PartDefinition body_main = main.addOrReplaceChild("body_main", CubeListBuilder.create().texOffs(48, 8).addBox(-3.0009F, -6.6866F, -2.2132F, 6.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0009F, -11.1134F, 1.2132F));

		PartDefinition body_top = body_main.addOrReplaceChild("body_top", CubeListBuilder.create().texOffs(60, 22).addBox(-1.8017F, -4.3731F, -10.8263F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(38, 8).addBox(-1.8017F, -4.3731F, -10.8263F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0009F, -5.3134F, -0.9869F));

		PartDefinition cube_r1 = body_top.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(18, 32).addBox(-4.6F, -1.4F, -2.2F, 8.0F, 3.0F, 7.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-0.0018F, -1.6731F, -9.3263F, 0.5757F, 0.0169F, -0.0305F));

		PartDefinition cube_r2 = body_top.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 85).addBox(-4.1F, 0.25F, -5.1F, 8.0F, 1.0F, 7.0F, new CubeDeformation(-0.2F))
				.texOffs(18, 18).addBox(-4.1F, -4.75F, -5.1F, 8.0F, 7.0F, 7.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-0.0018F, -4.9231F, -7.8263F, -0.327F, 0.0494F, 0.1856F));

		PartDefinition cube_r3 = body_top.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 49).addBox(0.0F, -10.1F, 5.7F, 0.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0018F, 1.8269F, 0.0737F, 0.8614F, 0.1147F, 0.1076F));

		PartDefinition body = body_top.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(-0.0018F, 0.8269F, 0.0737F));

		PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -9.4F, -3.0F, 11.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.8614F, 0.1147F, 0.1076F));

		PartDefinition right_arm = body_top.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(52, 39).addBox(-1.8F, -1.6667F, -1.5F, 3.0F, 12.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.7018F, -6.7065F, -3.1263F, 0.0F, 0.0F, 0.4014F));

		PartDefinition right_claw = right_arm.addOrReplaceChild("right_claw", CubeListBuilder.create().texOffs(12, 56).addBox(-1.0F, -1.2F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(-0.1F))
				.texOffs(0, 18).addBox(0.0F, -1.2F, -4.0F, 0.0F, 22.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1051F, 9.6527F, -0.1943F, -1.1526F, -0.6592F, -0.302F));

		PartDefinition left_arm = body_top.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 42).addBox(-1.0F, -1.6F, -1.5F, 3.0F, 20.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(20, 56).addBox(-1.0F, 11.6F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(6.4969F, -3.5255F, -5.0312F, -0.0346F, 0.0049F, -0.0524F));

		PartDefinition needle = left_arm.addOrReplaceChild("needle", CubeListBuilder.create().texOffs(97, 1).addBox(0.0F, -1.0F, -0.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 13.4F, 0.0F));

		PartDefinition right_leg = main.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(48, 22).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -9.8F, -0.5F));

		PartDefinition left_leg = main.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(18, 42).addBox(-1.95F, -1.35F, -3.45F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(52, 54).addBox(-1.95F, 4.65F, 0.55F, 4.0F, 9.0F, 3.0F, new CubeDeformation(-0.01F)), PartPose.offset(2.95F, -10.55F, 1.55F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		if (entity instanceof ExReaper reaper){
			this.animate(reaper.idleAnimationState, ExtraterrestrialReaperAnimations.IDLE, ageInTicks);
			if (!reaper.attackAnimationState.isStarted()) {
				this.animateWalk(ExtraterrestrialReaperAnimations.WALK, limbSwing, limbSwingAmount, 2.5F, 20.0F);
			}
			this.animate(reaper.attackAnimationState, ExtraterrestrialReaperAnimations.ATTACK, ageInTicks);
			this.animate(reaper.infectAnimationState, ExtraterrestrialReaperAnimations.INFECTION_ATTACK, ageInTicks);
			this.animate(reaper.appearAnimationState, ExtraterrestrialReaperAnimations.APPEAR, ageInTicks);
		}
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}
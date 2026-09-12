package com.mongoose.clanginghowl.client.render;

import com.mongoose.clanginghowl.utils.MathHelper;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

/** Parameters for early NeoForge enum extension. Do not reference item registries here. */
public final class CHArmPoseParameters {
    private CHArmPoseParameters() {}

    public static final EnumProxy<HumanoidModel.ArmPose> CHAINSAW = new EnumProxy<>(
            HumanoidModel.ArmPose.class, false, (IArmPoseTransformer) (model, entity, arm) -> {
            if (arm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = -MathHelper.modelDegrees(55);
                model.rightArm.yRot = -0.1F + model.head.yRot;
                model.leftArm.xRot = -MathHelper.modelDegrees(50);
                model.leftArm.yRot = 0.1F + model.head.yRot + 0.4F;
                model.leftArm.zRot = MathHelper.modelDegrees(30);
            } else {
                model.leftArm.xRot = -MathHelper.modelDegrees(55);
                model.leftArm.yRot = 0.1F + model.head.yRot;
                model.rightArm.xRot = -MathHelper.modelDegrees(50);
                model.rightArm.yRot = -0.1F + model.head.yRot - 0.4F;
                model.rightArm.zRot = -MathHelper.modelDegrees(30);
            }
        });

    public static final EnumProxy<HumanoidModel.ArmPose> IDLE_SAW = new EnumProxy<>(
            HumanoidModel.ArmPose.class, false, (IArmPoseTransformer) (model, entity, arm) -> {
            if (arm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = -MathHelper.modelDegrees(45);
                model.rightArm.yRot = -0.1F + model.head.yRot;
                model.rightArm.zRot = 0.0F;
                model.leftArm.xRot = -MathHelper.modelDegrees(45);
                model.leftArm.yRot = 0.1F + model.head.yRot + 0.4F;
                model.leftArm.zRot = MathHelper.modelDegrees(30);
            } else {
                model.leftArm.xRot = -MathHelper.modelDegrees(45);
                model.leftArm.yRot = 0.1F + model.head.yRot;
                model.leftArm.zRot = 0.0F;
                model.rightArm.xRot = -MathHelper.modelDegrees(45);
                model.rightArm.yRot = -0.1F + model.head.yRot - 0.4F;
                model.rightArm.zRot = -MathHelper.modelDegrees(30);
            }
        });

    public static final EnumProxy<HumanoidModel.ArmPose> DRILL = new EnumProxy<>(
            HumanoidModel.ArmPose.class, false, (IArmPoseTransformer) (model, entity, arm) -> {
            if (arm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = -MathHelper.modelDegrees(55);
                model.rightArm.yRot = -0.1F + model.head.yRot;
                model.leftArm.xRot = -MathHelper.modelDegrees(50);
                model.leftArm.yRot = 0.1F + model.head.yRot + 0.4F;
                model.leftArm.zRot = MathHelper.modelDegrees(30);
            } else {
                model.leftArm.xRot = -MathHelper.modelDegrees(55);
                model.leftArm.yRot = 0.1F + model.head.yRot;
                model.rightArm.xRot = -MathHelper.modelDegrees(50);
                model.rightArm.yRot = -0.1F + model.head.yRot - 0.4F;
                model.rightArm.zRot = -MathHelper.modelDegrees(30);
            }
        });

    public static final EnumProxy<HumanoidModel.ArmPose> IDLE_DRILL = new EnumProxy<>(
            HumanoidModel.ArmPose.class, false, (IArmPoseTransformer) (model, entity, arm) -> {
            if (arm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = -MathHelper.modelDegrees(45);
                model.rightArm.yRot = -0.1F + model.head.yRot;
                model.rightArm.zRot = 0.0F;
                model.leftArm.xRot = -MathHelper.modelDegrees(45);
                model.leftArm.yRot = 0.1F + model.head.yRot + 0.4F;
                model.leftArm.zRot = MathHelper.modelDegrees(30);
            } else {
                model.leftArm.xRot = -MathHelper.modelDegrees(45);
                model.leftArm.yRot = 0.1F + model.head.yRot;
                model.leftArm.zRot = 0.0F;
                model.rightArm.xRot = -MathHelper.modelDegrees(45);
                model.rightArm.yRot = -0.1F + model.head.yRot - 0.4F;
                model.rightArm.zRot = -MathHelper.modelDegrees(30);
            }
        });

    public static final EnumProxy<HumanoidModel.ArmPose> FLAME = new EnumProxy<>(
            HumanoidModel.ArmPose.class, false, (IArmPoseTransformer) (model, entity, arm) -> {
            if (arm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = -MathHelper.modelDegrees(55) + model.head.xRot;
                model.rightArm.yRot = -0.1F + model.head.yRot;
                model.leftArm.xRot = -MathHelper.modelDegrees(50) + model.head.xRot;
                model.leftArm.yRot = 0.1F + model.head.yRot + 0.4F;
                model.leftArm.zRot = MathHelper.modelDegrees(30);
            } else {
                model.leftArm.xRot = -MathHelper.modelDegrees(55) + model.head.xRot;
                model.leftArm.yRot = 0.1F + model.head.yRot;
                model.rightArm.xRot = -MathHelper.modelDegrees(50) + model.head.xRot;
                model.rightArm.yRot = -0.1F + model.head.yRot - 0.4F;
                model.rightArm.zRot = -MathHelper.modelDegrees(30);
            }
        });

    public static final EnumProxy<HumanoidModel.ArmPose> IDLE_FLAME = new EnumProxy<>(
            HumanoidModel.ArmPose.class, false, (IArmPoseTransformer) (model, entity, arm) -> {
            if (arm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = -MathHelper.modelDegrees(45) + model.head.xRot;
                model.rightArm.yRot = -0.1F + model.head.yRot;
                model.rightArm.zRot = 0.0F;
                model.leftArm.xRot = -MathHelper.modelDegrees(45) + model.head.xRot;
                model.leftArm.yRot = 0.1F + model.head.yRot + 0.4F;
                model.leftArm.zRot = MathHelper.modelDegrees(30);
            } else {
                model.leftArm.xRot = -MathHelper.modelDegrees(45) + model.head.xRot;
                model.leftArm.yRot = 0.1F + model.head.yRot;
                model.leftArm.zRot = 0.0F;
                model.rightArm.xRot = -MathHelper.modelDegrees(45) + model.head.xRot;
                model.rightArm.yRot = -0.1F + model.head.yRot - 0.4F;
                model.rightArm.zRot = -MathHelper.modelDegrees(30);
            }
        });
}

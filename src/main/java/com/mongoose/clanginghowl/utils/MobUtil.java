package com.mongoose.clanginghowl.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class MobUtil {

    public static boolean areAllies(@Nullable Entity entity, @Nullable Entity entity1){
        if (entity != null && entity1 != null) {
            return entity.isAlliedTo(entity1) || entity1.isAlliedTo(entity) || entity == entity1;
        } else {
            return false;
        }
    }

    public static boolean isMoving(LivingEntity livingEntity){
        return livingEntity.onGround() && livingEntity.getDeltaMovement().horizontalDistanceSqr() > (double) 2.5000003E-7F;
    }

    public static BlockHitResult rayTrace(Entity entity, double distance, boolean fluids) {
        return (BlockHitResult) entity.pick(distance, 1.0F, fluids);
    }

    public static boolean isInSunlight(Entity entity){
        if (entity.level().isDay() && !entity.level().isClientSide) {
            float f = entity.getLightLevelDependentMagicValue();
            BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ());
            boolean flag = entity.isInWaterRainOrBubble() || entity.isInPowderSnow || entity.wasInPowderSnow;
            return f > 0.5F && entity.level().getRandom().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !flag && entity.level().canSeeSky(blockpos);
        }

        return false;
    }

}
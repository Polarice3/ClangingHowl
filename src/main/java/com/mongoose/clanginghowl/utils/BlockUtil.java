package com.mongoose.clanginghowl.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;

public class BlockUtil {

    public static Iterable<BlockPos> multiBlockBreak(LivingEntity livingEntity, BlockPos blockPos, int x, int y, int z){
        BlockHitResult blockHitResult = MobUtil.rayTrace(livingEntity, 10, false);
        Direction direction = blockHitResult.getDirection();
        boolean hasX = direction.getStepX() == 0;
        boolean hasY = direction.getStepY() == 0;
        boolean hasZ = direction.getStepZ() == 0;
        Vec3i start = new Vec3i(hasX ? -x : 0, hasY ? -y : 0, hasZ ? -z : 0);
        Vec3i end = new Vec3i(hasX ? x : 0, hasY ? (y * 2) - 1 : 0, hasZ ? z : 0);
        return BlockPos.betweenClosed(
                blockPos.offset(start),
                blockPos.offset(end));
    }

}

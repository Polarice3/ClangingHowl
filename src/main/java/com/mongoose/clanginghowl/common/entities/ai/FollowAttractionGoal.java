package com.mongoose.clanginghowl.common.entities.ai;

import com.mongoose.clanginghowl.common.effects.CHEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.PathType;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class FollowAttractionGoal extends Goal {
    private final Mob mob;
    private final Predicate<LivingEntity> followPredicate;
    @Nullable
    private LivingEntity target;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private float oldWaterCost;
    private final float areaSize;

    public FollowAttractionGoal(Mob mob, double speedModifier, float stopDistance, float areaSize) {
        this.mob = mob;
        this.followPredicate = (livingEntity) -> {
            return livingEntity != null
                    && livingEntity.hasEffect(CHEffects.ATTRACTION)
                    && this.mob.distanceTo(livingEntity) <= areaSize;
        };
        this.speedModifier = speedModifier;
        this.navigation = mob.getNavigation();
        this.stopDistance = stopDistance;
        this.areaSize = areaSize;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!(this.mob.getNavigation() instanceof GroundPathNavigation) && !(this.mob.getNavigation() instanceof FlyingPathNavigation)) {
            return false;
        }
        List<LivingEntity> list = this.mob.level().getEntitiesOfClass(LivingEntity.class, this.mob.getBoundingBox().inflate(this.areaSize), this.followPredicate);
        if (!list.isEmpty()) {
            for(LivingEntity livingEntity : list) {
                if (!livingEntity.isInvisible()) {
                    this.target = livingEntity;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canContinueToUse() {
        return this.target != null && this.target.hasEffect(CHEffects.ATTRACTION) && !this.navigation.isDone() && this.mob.distanceToSqr(this.target) > (double)(this.stopDistance * this.stopDistance);
    }

    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.mob.getPathfindingMalus(PathType.WATER);
        this.mob.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public void stop() {
        this.target = null;
        this.navigation.stop();
        this.mob.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
    }

    public void tick() {
        if (this.target != null && !this.mob.isLeashed()) {
            this.mob.getLookControl().setLookAt(this.target, 10.0F, (float)this.mob.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                double d0 = this.mob.getX() - this.target.getX();
                double d1 = this.mob.getY() - this.target.getY();
                double d2 = this.mob.getZ() - this.target.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 > (double)(this.stopDistance * this.stopDistance)) {
                    this.navigation.moveTo(this.target, this.speedModifier);
                } else {
                    this.navigation.stop();
                }
            }
        }
    }
}

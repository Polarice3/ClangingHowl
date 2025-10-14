package com.mongoose.clanginghowl.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MobUtil {
    public static final Predicate<Entity> LIVING_OR_PART = (entity) -> {
        return entity.isAlive() && (entity instanceof LivingEntity || entity instanceof PartEntity<?> partEntity && partEntity.getParent() instanceof LivingEntity);
    };

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
            return f > 0.5F && !flag && entity.level().canSeeSky(blockpos);
        }

        return false;
    }

    public static void knockBack(Entity knocked, Entity knocker, double xPower, double yPower, double zPower) {
        Vec3 vec3 = new Vec3(knocker.getX() - knocked.getX(), knocker.getY() - knocked.getY(), knocker.getZ() - knocked.getZ()).normalize();
        double pY0 = Math.max(-vec3.y, yPower);
        Vec3 vec31 = new Vec3(-vec3.x * xPower, pY0, -vec3.z * zPower);
        double resist = knocked instanceof LivingEntity livingEntity ? livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0.0D;
        double resist1 = Math.max(0.0D, 1.0D - resist);
        if (knocked instanceof Player player) {
            if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player)) {
                player.hurtMarked = true;
                if (!player.level().isClientSide){
                    player.setOnGround(false);
                }
            }
        }
        knocked.setDeltaMovement(knocked.getDeltaMovement().add(vec31).scale(resist1));
        knocked.hasImpulse = true;
    }

    public static List<Entity> getTargets(Level level, LivingEntity pSource, double pRange, double pRadius) {
        return getTargets(level, pSource, pRange, pRadius, EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(LIVING_OR_PART).and(entity -> !MobUtil.areAllies(entity, pSource)));
    }

    public static List<Entity> getTargets(Level level, LivingEntity pSource, double pRange, double pRadius, Predicate<? super Entity> predicate) {
        List<Entity> list = new ArrayList<>();
        Vec3 srcVec = pSource.getEyePosition();
        Vec3 lookVec = pSource.getViewVector(1.0F);
        double[] lookRange = new double[] {lookVec.x() * pRange, lookVec.y() * pRange, lookVec.z() * pRange};
        Vec3 destVec = srcVec.add(lookRange[0], lookRange[1], lookRange[2]);
        List<Entity> possibleList = level.getEntities(pSource, pSource.getBoundingBox().expandTowards(lookRange[0], lookRange[1], lookRange[2]).inflate(pRadius, pRadius, pRadius),
                predicate);
        double hitDist = 0.0D;

        for (Entity hit : possibleList) {
            if ((hit.isPickable() || hit instanceof ItemEntity || hit instanceof Projectile) && pSource.hasLineOfSight(hit) && hit != pSource) {
                float maxSize = pSource instanceof Mob ? 2.0F : 0.8F;
                float borderSize = Math.max(maxSize, hit.getPickRadius());
                AABB collisionBB = hit.getBoundingBox().inflate(borderSize);
                Optional<Vec3> interceptPos = collisionBB.clip(srcVec, destVec);
                if (collisionBB.contains(srcVec)) {
                    if (0.0D <= hitDist) {
                        list.add(hit);
                        hitDist = 0.0D;
                    }
                } else if (interceptPos.isPresent()) {
                    double possibleDist = srcVec.distanceTo(interceptPos.get());

                    if (possibleDist < hitDist || hitDist == 0.0D) {
                        list.add(hit);
                        hitDist = possibleDist;
                    }
                }
            }
        }
        return list;
    }

}
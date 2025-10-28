package com.mongoose.clanginghowl.utils;

import com.mongoose.clanginghowl.client.particles.BloodSplashParticleOption;
import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.network.CHNetwork;
import com.mongoose.clanginghowl.common.network.server.SInstaLookPacket;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.init.CHTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;

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

    public static void instaLook(Mob mob, Vec3 vec3){
        mob.getLookControl().setLookAt(vec3.x, vec3.y, vec3.z, 200.0F, mob.getMaxHeadXRot());
        double d2 = vec3.x - mob.getX();
        double d1 = vec3.z - mob.getZ();
        float rotate = -((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI);
        mob.setYRot(rotate);
        mob.yBodyRot = rotate;
        mob.yHeadRot = rotate;
    }

    public static void instaLook(Mob looker, Entity target){
        instaLook(looker, target, false);
    }

    public static void instaLook(Mob looker, Entity target, boolean clientSent){
        looker.lookAt(target, 100.0F, 100.0F);
        instaLook(looker, target.position());
        if (clientSent) {
            if (!looker.level().isClientSide) {
                CHNetwork.sendToALL(new SInstaLookPacket(looker, target));
            }
        }
    }

    public static boolean isWalking(LivingEntity livingEntity){
        return livingEntity.onGround() && isMoving(livingEntity);
    }

    public static boolean isMoving(LivingEntity livingEntity){
        return livingEntity.getDeltaMovement().horizontalDistanceSqr() > (double) 2.5000003E-7F;
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

    public static void buffTechnoFlesh(ServerLevel serverLevel, LivingEntity livingEntity) {
        int buff = Mth.floor((float) (serverLevel.getGameTime()) / 24000);
        if (buff >= 1 && buff < 1000) {
            float increase = (0.005F * buff) + 1.0F;
            AttributeInstance health = livingEntity.getAttribute(Attributes.MAX_HEALTH);
            AttributeInstance attack = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE);
            AttributeInstance armor = livingEntity.getAttribute(Attributes.ARMOR);
            if (health != null) {
                health.setBaseValue(health.getBaseValue() * increase);
                livingEntity.heal((float) health.getBaseValue());
            }
            if (attack != null) {
                attack.setBaseValue(attack.getBaseValue() * increase);
            }
            if (armor != null) {
                armor.setBaseValue(armor.getBaseValue() * increase);
            }
            if (CHCapHelper.getTechnoResist(livingEntity) < 65.0F) {
                CHCapHelper.setTechnoResist(livingEntity, CHCapHelper.getTechnoResist(livingEntity) + buff);
            }
        }
    }

    public static boolean isTechnoConvert(LivingEntity livingEntity) {
        return livingEntity.getType().is(CHTags.EntityTypes.TECHNO_CONVERT) || isHoDConvert(livingEntity) || isReaperConvert(livingEntity);
    }

    public static boolean isHoDConvert(LivingEntity livingEntity) {
        return livingEntity.getMaxHealth() <= 25.0D && (livingEntity.getType().is(CHTags.EntityTypes.HOD_CONVERT) || livingEntity instanceof Animal || livingEntity instanceof Spider);
    }

    public static boolean isReaperConvert(LivingEntity livingEntity) {
        return livingEntity.getType().is(CHTags.EntityTypes.REAPER_CONVERT) || livingEntity instanceof AbstractVillager || livingEntity instanceof AbstractIllager || livingEntity instanceof Witch || livingEntity instanceof Zombie;
    }

    public static void convertTechno(Mob original) {
        if (original.level() instanceof ServerLevel serverLevel) {
            Monster convert = null;
            if (MobUtil.isHoDConvert(original)) {
                convert = original.convertTo(CHEntityType.HEART_OF_DECAY.get(), false);
            }
            if (MobUtil.isReaperConvert(original)) {
                if (serverLevel.getRandom().nextBoolean()) {
                    convert = original.convertTo(CHEntityType.FLESH_MAIDEN.get(), false);
                } else {
                    convert = original.convertTo(CHEntityType.EX_REAPER.get(), false);
                }
            }
            if (convert != null) {
                convert.removeAllEffects();
                ForgeEventFactory.onFinalizeSpawn(convert, serverLevel, serverLevel.getCurrentDifficultyAt(convert.blockPosition()), MobSpawnType.CONVERSION, null, null);
                serverLevel.sendParticles(new BloodSplashParticleOption(((float)convert.getBoundingBox().getSize() * 2.0F), 0), convert.getX(), convert.getY() + 1.0D, convert.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                serverLevel.playSound(null, convert.getX(), convert.getY(), convert.getZ(), CHSounds.FLESH_RUPTURE_ENDING.get(), convert.getSoundSource(), 2.0F, 1.0F);
            }
        }
    }

}
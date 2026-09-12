package com.mongoose.clanginghowl.utils;

import com.google.common.collect.Multimap;
import com.mongoose.clanginghowl.client.particles.BloodSplashParticleOption;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.entities.hostiles.ITFlesh;
import com.mongoose.clanginghowl.common.entities.projectiles.BloodTrailProjectile;
import com.mongoose.clanginghowl.common.network.CHNetwork;
import com.mongoose.clanginghowl.common.network.server.SInstaLookPacket;
import com.mongoose.clanginghowl.config.CHConfig;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.init.CHTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;

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

    public static boolean validEntity(Entity entity){
        return EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && entity.isAttackable();
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

    public static void deflectProjectile(Projectile projectile, Entity shooter, LivingEntity victim) {
        if (shooter != null) {
            projectile.hasImpulse = true;
            Vec3 deltaMovement = projectile.getDeltaMovement();
            projectile.setPos(projectile.getX() + deltaMovement.x, projectile.getY() + deltaMovement.y, projectile.getZ() + deltaMovement.z);
            projectile.setOwner(victim);
            if (projectile instanceof AbstractHurtingProjectile projectile1) {
                projectile1.hurtMarked = true;
                double d1 = shooter.getX() - victim.getX();
                double d2 = shooter.getY(0.5D) - victim.getY(0.5D);
                double d3 = shooter.getZ() - victim.getZ();
                Vec3 vec3 = new Vec3(d1, d2, d3);
                projectile1.setDeltaMovement(vec3);
                projectile1.accelerationPower = vec3.length() * 0.1D;
            } else {
                float speed = Mth.sqrt((float) (deltaMovement.x * deltaMovement.x + deltaMovement.y * deltaMovement.y + deltaMovement.z * deltaMovement.z));
                speed = speed < 1.0E-4F ? 0.0F : speed;
                double d0 = shooter.getX() - victim.getX();
                double d1 = shooter.getY(0.3333333333333333D) - (victim.getEyeY() - (double) 0.1F);
                double d2 = shooter.getZ() - victim.getZ();
                double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
                projectile.shoot(d0, d1 + d3 * (double) 0.2F, d2, speed, (float) (14 - victim.level().getDifficulty().getId() * 4));
            }
        }
    }

    public static long technoFleshBuffTime(Level level) {
        long time = level.getGameTime();
        if (CHConfig.TechnoFleshBuffDayTime.get()) {
            time = level.getDayTime();
        }
        if (CHConfig.FixedAdaptationStage.get() > 0) {
            time = MathHelper.minecraftDayToTicks(CHConfig.FixedAdaptationStage.get());
        }
        return time;
    }

    public static void buffTechnoFlesh(ServerLevel serverLevel, LivingEntity livingEntity) {
        long time = serverLevel.getGameTime();
        if (CHConfig.TechnoFleshBuffDayTime.get()) {
            time = serverLevel.getDayTime();
        }
        if (CHConfig.FixedAdaptationStage.get() > 0) {
            time = MathHelper.minecraftDayToTicks(CHConfig.FixedAdaptationStage.get());
        }
        int buff = Mth.floor((float) (time) / 24000);
        if (buff >= 1) {
            float increase = Math.min((0.005F * buff) + 1.0F, 6.0F);
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
            if (livingEntity instanceof ITFlesh flesh) {
                int xpReward = Mth.floor(buff / 10.0D);
                flesh.setXPReward(flesh.getXPReward() + xpReward);
            }
            CHCapHelper.setTechnoResist(livingEntity, Math.min(CHCapHelper.getTechnoResist(livingEntity) + Mth.floor(buff / 2.0F), 50.0F));
        }
    }

    public static boolean isTechnoConvert(LivingEntity livingEntity) {
        if (livingEntity.getType().is(CHTags.EntityTypes.TECHNO_FLESH)) {
            return false;
        }
        return livingEntity.getType().is(CHTags.EntityTypes.TECHNO_CONVERT) || isHoDConvert(livingEntity) || isReaperConvert(livingEntity);
    }

    public static boolean isHoDConvert(LivingEntity livingEntity) {
        return livingEntity.getType().is(CHTags.EntityTypes.HOD_CONVERT) || livingEntity instanceof Animal || livingEntity instanceof Spider;
    }

    public static boolean isReaperConvert(LivingEntity livingEntity) {
        return livingEntity.getType().is(CHTags.EntityTypes.REAPER_CONVERT) || livingEntity instanceof AbstractVillager || livingEntity instanceof AbstractIllager || livingEntity instanceof Witch || livingEntity instanceof Zombie;
    }

    public static boolean isHematomaConvert(LivingEntity livingEntity) {
        if (livingEntity.getType().is(CHTags.EntityTypes.HEMATOMA_CONVERT)) {
            return true;
        }
        return livingEntity.getBbWidth() >= 0.5F && livingEntity.getBbWidth() < 1.2F && livingEntity instanceof Animal;
    }

    public static boolean isProwlerConvert(LivingEntity livingEntity) {
        if (livingEntity.getType().is(CHTags.EntityTypes.PROWLER_CONVERT)) {
            return true;
        }
        return livingEntity.getBbWidth() >= 1.2F && livingEntity instanceof Animal;
    }

    public static boolean canInfect(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            return MobUtil.isTechnoConvert(livingEntity) || MobUtil.isReaperConvert(livingEntity) || MobUtil.isHoDConvert(livingEntity) || MobUtil.isHematomaConvert(livingEntity) || MobUtil.isProwlerConvert(livingEntity) || livingEntity instanceof AbstractHorse || livingEntity instanceof Wolf;
        }
        return false;
    }

    public static void convertTechno(Mob original) {
        if (original.level() instanceof ServerLevel serverLevel) {
            Monster convert = null;
            if (MobUtil.isReaperConvert(original)) {
                if (serverLevel.getRandom().nextBoolean()) {
                    convert = original.convertTo(CHEntityType.FLESH_MAIDEN.get(), false);
                } else {
                    convert = original.convertTo(CHEntityType.EX_REAPER.get(), false);
                }
            } else if (MobUtil.isProwlerConvert(original) || original instanceof AbstractHorse) {
                if ((serverLevel.getRandom().nextBoolean() || original instanceof AbstractHorse)) {
                    convert = original.convertTo(CHEntityType.PROWLER.get(), false);
                } else {
                    convert = original.convertTo(CHEntityType.CARCASS.get(), false);
                }
            } else if (MobUtil.isHematomaConvert(original) || original instanceof Wolf || original instanceof Pig) {
                if ((serverLevel.getRandom().nextBoolean() || original instanceof Wolf) && !(original instanceof Pig)) {
                    convert = original.convertTo(CHEntityType.BLOOD_SPREADER.get(), false);
                } else {
                    convert = original.convertTo(CHEntityType.HEMATOMA.get(), false);
                }
            } else if (MobUtil.isHoDConvert(original)) {
                convert = original.convertTo(CHEntityType.HEART_OF_DECAY.get(), false);
            }
            if (convert != null) {
                convert.removeAllEffects();
                EventHooks.finalizeMobSpawn(convert, serverLevel, serverLevel.getCurrentDifficultyAt(convert.blockPosition()), MobSpawnType.CONVERSION, null);
                serverLevel.sendParticles(new BloodSplashParticleOption(((float)convert.getBoundingBox().getSize() * 2.0F), 0), convert.getX(), convert.getY() + 1.0D, convert.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                for (int i = 0; i < serverLevel.getRandom().nextIntBetweenInclusive(4, 6); ++i) {
                    float randomY = serverLevel.getRandom().nextIntBetweenInclusive(1, 4) / 100.0F;
                    serverLevel.sendParticles(CHParticleTypes.BLOOD_STAIN.get(), convert.getRandomX(0.5D), convert.getY() + randomY, convert.getRandomZ(0.5D), 1, 0, 0, 0, 1);
                }
                serverLevel.playSound(null, convert.getX(), convert.getY(), convert.getZ(), CHSounds.FLESH_RUPTURE_ENDING.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
        }
    }

    public static void moveDownToGround(Entity entity) {
        HitResult rayTrace = rayTrace(entity);
        if (rayTrace.getType() == HitResult.Type.BLOCK) {
            BlockHitResult hitResult = (BlockHitResult) rayTrace;
            if (hitResult.getDirection() == Direction.UP) {
                BlockState hitBlock = entity.level().getBlockState(hitResult.getBlockPos());
                if (hitBlock.getBlock() instanceof SlabBlock && hitBlock.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.BOTTOM) {
                    entity.setPos(entity.getX(), hitResult.getBlockPos().getY() + 1.0625F - 0.5f, entity.getZ());
                } else {
                    entity.setPos(entity.getX(), hitResult.getBlockPos().getY() + 1.0625F, entity.getZ());
                }
                if (entity.level() instanceof ServerLevel serverLevel) {
                    serverLevel.getChunkSource().broadcastAndSend(entity, new ClientboundTeleportEntityPacket(entity));
                }
            }
        }
    }

    private static HitResult rayTrace(Entity entity) {
        Vec3 startPos = new Vec3(entity.getX(), entity.getY(), entity.getZ());
        Vec3 endPos = new Vec3(entity.getX(), entity.level().getMinBuildHeight(), entity.getZ());
        return entity.level().clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
    }

    public static void push(Entity pEntity, Vec3 vec3) {
        push(pEntity, vec3, 1.0D);
    }

    public static void push(Entity pEntity, Vec3 vec3, double reduction) {
        push(pEntity, vec3.x, vec3.y, vec3.z, reduction);
    }

    public static void push(Entity pEntity, double pX, double pY, double pZ) {
        push(pEntity, pX, pY, pZ, 1.0D);
    }

    public static void push(Entity pEntity, double pX, double pY, double pZ, double reduction) {
        if (pEntity instanceof Player player) {
            if (MobUtil.validEntity(player)) {
                player.hurtMarked = true;
                if (!player.level().isClientSide){
                    player.setOnGround(false);
                }
            }
        }
        double resist = 0.0D;
        if (pEntity instanceof LivingEntity living && living.getAttribute(Attributes.KNOCKBACK_RESISTANCE) != null) {
            resist = living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * reduction;
        }
        double resist1 = Math.max(0.0D, 1.0D - resist);
        pEntity.setDeltaMovement(pEntity.getDeltaMovement().add(pX, pY, pZ).scale(resist1));
        pEntity.hasImpulse = true;
    }

    public static boolean hasVisualLineOfSight(Entity looker, Entity target) {
        if (target.level() != looker.level()) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(looker.getX(), looker.getEyeY(), looker.getZ());
            Vec3 vec31 = new Vec3(target.getX(), target.getEyeY(), target.getZ());
            if (vec31.distanceTo(vec3) > 128.0D) {
                return false;
            } else {
                return looker.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, looker)).getType() == HitResult.Type.MISS;
            }
        }
    }

    public static Vec3 calculateViewVector(float p_20172_, float p_20173_) {
        float f = p_20172_ * ((float)Math.PI / 180F);
        float f1 = -p_20173_ * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }

    public static Vec3 getHorizontalLeftLookAngle(Entity entity) {
        return MobUtil.calculateViewVector(0, entity.getYRot() - 90);
    }

    public static Vec3 getHorizontalRightLookAngle(Entity entity) {
        return MobUtil.calculateViewVector(0, entity.getYRot() + 90);
    }

    public static void shootOutBlood(Entity source, boolean clotted) {
        if (source.level() instanceof ServerLevel serverLevel) {
            BloodTrailProjectile arrow = new BloodTrailProjectile(source.getX(), source.getY(), source.getZ(), serverLevel);
            float yaw = serverLevel.getRandom().nextFloat() * -180;
            float pitch = serverLevel.getRandom().nextFloat() * 90 - 75;
            arrow.setClotted(clotted);
            arrow.shootFromRotation(source, yaw, pitch, 0.0F, 0.75F, 0.1F);
            serverLevel.addFreshEntity(arrow);
        }
    }

    //Stolen from L_Ender:https://github.com/lender544/new1.20.1/blob/master/src/main/java/com/github/L_Ender/cataclysm/util/AttributeUtils.java#L70
    public static float originDamage(LivingEntity living, ItemStack itemStack) {
        double totalDamage = living.getAttributeValue(Attributes.ATTACK_DAMAGE);

        if (living.getMainHandItem() == itemStack) {
            return (float) totalDamage;
        }

        ItemStack mainHandStack = living.getMainHandItem();
        if (!mainHandStack.isEmpty()) {
            Multimap<net.minecraft.core.Holder<Attribute>, AttributeModifier> modifiers = ItemHelper.mainHandAttributes(mainHandStack);

            if (modifiers.containsKey(Attributes.ATTACK_DAMAGE)) {
                for (AttributeModifier modifier : modifiers.get(Attributes.ATTACK_DAMAGE)) {
                    if (modifier.operation() == AttributeModifier.Operation.ADD_VALUE) {
                        totalDamage -= modifier.amount();
                    }
                }
            }
        }

        Multimap<net.minecraft.core.Holder<Attribute>, AttributeModifier> shredderModifiers = ItemHelper.mainHandAttributes(itemStack);
        if (shredderModifiers.containsKey(Attributes.ATTACK_DAMAGE)) {
            for (AttributeModifier modifier : shredderModifiers.get(Attributes.ATTACK_DAMAGE)) {
                if (modifier.operation() == AttributeModifier.Operation.ADD_VALUE) {
                    totalDamage += modifier.amount();
                }
            }
        }

        return (float) totalDamage;
    }

}

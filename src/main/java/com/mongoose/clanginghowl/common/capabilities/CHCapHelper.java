package com.mongoose.clanginghowl.common.capabilities;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.client.particles.SmallFireSplashParticleOption;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.curios.JetBoots;
import com.mongoose.clanginghowl.common.items.fuel.IFuel;
import com.mongoose.clanginghowl.common.network.CHNetwork;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.CHCuriosFinder;
import com.mongoose.clanginghowl.utils.CHDamageSource;
import com.mongoose.clanginghowl.utils.MobUtil;
import com.mongoose.clanginghowl.utils.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CHCapHelper {

    public static ICHCap getCapability(LivingEntity player) {
        return player.getData(CHAttachments.STATE);
    }

    public static int getMiningProgress(Player player){
        return getCapability(player).getMiningProgress();
    }

    public static void setMiningProgress(Player player, int progress){
        getCapability(player).setMiningProgress(progress);
    }

    public static void increaseMiningProgress(Player player){
        setMiningProgress(player, getMiningProgress(player) + 1);
    }

    @Nullable
    public static BlockPos getMiningPos(Player player){
        return getCapability(player).getMiningPos();
    }

    public static void setMiningPos(Player player, BlockPos blockPos){
        getCapability(player).setMiningPos(blockPos);
    }

    public static int getShakeTime(LivingEntity livingEntity){
        return getCapability(livingEntity).getShakeTime();
    }

    public static void setShakeTime(LivingEntity livingEntity, int ticks){
        getCapability(livingEntity).setShakeTime(ticks);
        sendCHCapUpdatePacket(livingEntity);
    }

    public static boolean isMoving(LivingEntity livingEntity){
        return getCapability(livingEntity).isMoving();
    }

    public static void setMoving(LivingEntity livingEntity, boolean moving){
        getCapability(livingEntity).setMoving(moving);
    }

    public static float getTechnoResist(LivingEntity livingEntity){
        return getCapability(livingEntity).technoResist();
    }

    public static void setTechnoResist(LivingEntity livingEntity, float resist){
        getCapability(livingEntity).setTechnoResist(resist);
        sendCHCapUpdatePacket(livingEntity);
    }

    public static float getEnlightenedTick(LivingEntity livingEntity){
        return getCapability(livingEntity).getEnlightenedTick();
    }

    public static void setEnlightenedTick(LivingEntity livingEntity, int tick){
        if (livingEntity != null) {
            getCapability(livingEntity).setEnlightenedTick(tick);
            if (!livingEntity.level().isClientSide){
                sendCHCapUpdatePacket(livingEntity);
            }
        }
    }

    public static boolean isEnlightened(LivingEntity livingEntity) {
        if (livingEntity != null) {
            return getCapability(livingEntity).getEnlightenedTick() > 0;
        }
        return false;
    }

    //Air Jumps codes based on Zepalesque's codes: https://github.com/Zepalesque/The-Aether-Redux/blob/1.20.1/src/main/java/net/zepalesque/redux/capability/player/ReduxPlayerCapability.java
    public static int getTicksInAir(Player player){
        return getCapability(player).getTicksInAir();
    }

    public static void setTicksInAir(Player player, int tick){
        getCapability(player).setTicksInAir(tick);
        sendCHCapUpdatePacket(player);
    }

    public static boolean increaseAirJumpCount(Player player){
        ItemStack jetBoots = CHCuriosFinder.findCurio(player, CHItems.JET_BOOTS.get());
        if (!jetBoots.isEmpty() && !player.getCooldowns().isOnCooldown(CHItems.JET_BOOTS.get())) {
            if (!JetBoots.noFuelInInventory(player, jetBoots)) {
                IFuel.decreaseFuel(jetBoots, 20);
                player.getCooldowns().addCooldown(CHItems.JET_BOOTS.get(), 35);
                sendCHCapUpdatePacket(player);
                return true;
            }
        }
        return false;
    }

    public static boolean doubleJump(Player player) {
        if (increaseAirJumpCount(player)) {
            doDoubleJumpMovement(player);
            return true;
        } else {
            return false;
        }
    }

    public static void doDoubleJumpMovement(LivingEntity entity) {
        double xDelta = entity.getDeltaMovement().x() * 1.4D;
        double yDelta = (0.42D + entity.getJumpBoostPower()) * 1.5D;
        double zDelta = entity.getDeltaMovement().z() * 1.4D;
        entity.setDeltaMovement(xDelta, yDelta, zDelta);
        entity.resetFallDistance();
        fireEffects(entity.level(), entity.position(), entity);
        entity.level().playSound(null, entity.position().x, entity.position().y, entity.position().z, CHSounds.FLAMETHROWER_EMISSION.get(), entity.getSoundSource(), 1.0F, 1.0F);
    }

    public static void fireEffects(Level level, Vec3 vec3, LivingEntity owner) {
        if (level instanceof ServerLevel serverLevel) {
            Direction facing = Direction.DOWN;
            ParticleUtil.sendAlwaysVisibleParticles(serverLevel, new SmallFireSplashParticleOption(3, 0), vec3.x, vec3.y, vec3.z, 1, 0, 0, 0, 0);
            for (int i = 0; i < 24; i++) {
                int range = 6;
                double initialVelocity = ((double) range / 10) * 0.5D;
                double velocity = initialVelocity + level.getRandom().nextDouble() * initialVelocity;
                double angle = 0.5D;
                Vec3 randomVec = new Vec3(level.getRandom().nextDouble() * 2.0D * angle - angle, level.getRandom().nextDouble() * 2.0D * angle - angle, level.getRandom().nextDouble() * 2.0D * angle - angle).normalize();
                Vec3 direction = new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ());
                Vec3 result = (direction.normalize().scale(5.0D).add(randomVec)).normalize().scale(velocity);
                Vec3 initial = vec3.offsetRandom(serverLevel.getRandom(), 1.0F);
                ParticleUtil.sendAlwaysVisibleParticles(serverLevel, CHParticleTypes.FLAMETHROWER_FLAME.get(), initial.x, initial.y, initial.z, 0, result.x, result.y, result.z, 1.0F);
            }
            for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, new AABB(vec3, vec3.add(0.0D, facing.getStepY(), 0.0D)).inflate(1.0F))) {
                if (!MobUtil.areAllies(owner, livingEntity) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                    DamageSource damageSource = CHDamageSource.fireStream(owner, owner);
                    if (livingEntity.hurt(damageSource, 2.5F)) {
                        int fireSeconds = 10;
                        livingEntity.igniteForSeconds(fireSeconds);
                    }
                }
            }
        }
    }

    public static boolean isFlashing(LivingEntity livingEntity){
        if (livingEntity != null) {
            return getCapability(livingEntity).getFlashTick() > 0;
        }
        return false;
    }

    public static int getFlashTick(LivingEntity livingEntity){
        if (livingEntity != null) {
            return getCapability(livingEntity).getFlashTick();
        }
        return 0;
    }

    public static void setFlashTick(LivingEntity livingEntity, int tick){
        if (livingEntity != null) {
            getCapability(livingEntity).setFlashTick(tick);
            if (!livingEntity.level().isClientSide) {
                sendCHCapUpdatePacket(livingEntity);
            }
        }
    }

    public static void decreaseFlashTick(LivingEntity livingEntity) {
        if (livingEntity != null) {
            setFlashTick(livingEntity, getFlashTick(livingEntity) - 1);
        }
    }

    public static void sendCHCapUpdatePacket(LivingEntity livingEntity) {
        if (!livingEntity.level().isClientSide()) {
            CHNetwork.sentToTrackingEntityAndPlayer(livingEntity, new CHCapUpdatePacket(livingEntity));
        }
    }

    public static CompoundTag save(CompoundTag tag, ICHCap cap) {
        return CHCapSerialization.save(tag, cap);
    }

    public static ICHCap load(CompoundTag tag, ICHCap cap) {
        return CHCapSerialization.load(tag, cap);
    }
}

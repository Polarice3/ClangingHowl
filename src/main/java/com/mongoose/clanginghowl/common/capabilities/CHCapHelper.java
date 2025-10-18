package com.mongoose.clanginghowl.common.capabilities;

import com.mongoose.clanginghowl.common.network.CHNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class CHCapHelper {

    public static ICHCap getCapability(LivingEntity player) {
        return player.getCapability(CHCapProvider.CAPABILITY).orElse(new CHCapImp());
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

    public static void sendCHCapUpdatePacket(LivingEntity livingEntity) {
        if (!livingEntity.level().isClientSide()) {
            CHNetwork.sentToTrackingEntityAndPlayer(livingEntity, new CHCapUpdatePacket(livingEntity));
        }
    }

    public static CompoundTag save(CompoundTag tag, ICHCap cap) {
        if (cap.getMiningPos() != null) {
            tag.putInt("miningPosX", cap.getMiningPos().getX());
            tag.putInt("miningPosY", cap.getMiningPos().getY());
            tag.putInt("miningPosZ", cap.getMiningPos().getZ());
        }
        if (cap.getMiningProgress() > 0) {
            tag.putInt("miningProgress", cap.getMiningProgress());
        }
        tag.putInt("shakeTime", cap.getShakeTime());
        tag.putBoolean("isMoving", cap.isMoving());
        if (cap.technoResist() > 0.0F) {
            tag.putFloat("technoResist", cap.technoResist());
        }
        return tag;
    }

    public static ICHCap load(CompoundTag tag, ICHCap cap) {
        if (tag.contains("miningPosX") && tag.contains("miningPosY") && tag.contains("miningPosZ")) {
            cap.setMiningPos(new BlockPos(tag.getInt("miningPosX"), tag.getInt("miningPosY"), tag.getInt("miningPosZ")));
        }
        if (tag.contains("miningProgress")) {
            cap.setMiningProgress(tag.getInt("miningProgress"));
        }
        if (tag.contains("shakeTime")){
            cap.setShakeTime(tag.getInt("shakeTime"));
        }
        if (tag.contains("isMoving")){
            cap.setMoving(tag.getBoolean("isMoving"));
        }
        if (tag.contains("technoResist")){
            cap.setTechnoResist(tag.getFloat("technoResist"));
        }
        return cap;
    }
}

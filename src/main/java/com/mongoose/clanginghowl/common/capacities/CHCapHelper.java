package com.mongoose.clanginghowl.common.capacities;

import com.mongoose.clanginghowl.common.network.CHNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class CHCapHelper {

    public static ICHCap getCapability(Player player) {
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

    public static void sendCHCapUpdatePacket(Player player) {
        if (!player.level().isClientSide()) {
            CHNetwork.sendTo(player, new CHCapUpdatePacket(player));
        }
    }

    public static CompoundTag save(CompoundTag tag, ICHCap cap) {
        if (cap.getMiningPos() != null) {
            tag.putInt("miningPosX", cap.getMiningPos().getX());
            tag.putInt("miningPosY", cap.getMiningPos().getY());
            tag.putInt("miningPosZ", cap.getMiningPos().getZ());
        }
        tag.putInt("miningProgress", cap.getMiningProgress());
        return tag;
    }

    public static ICHCap load(CompoundTag tag, ICHCap cap) {
        if (tag.contains("miningPosX") && tag.contains("miningPosY") && tag.contains("miningPosZ")) {
            cap.setMiningPos(new BlockPos(tag.getInt("miningPosX"), tag.getInt("miningPosY"), tag.getInt("miningPosZ")));
        }
        cap.setMiningProgress(tag.getInt("miningProgress"));
        return cap;
    }
}

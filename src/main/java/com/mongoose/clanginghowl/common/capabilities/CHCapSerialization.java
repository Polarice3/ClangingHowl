package com.mongoose.clanginghowl.common.capabilities;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/** Shared full-snapshot format for disk persistence and client synchronization. */
public final class CHCapSerialization {
    private CHCapSerialization() {
    }

    public static CompoundTag save(CompoundTag tag, ICHCap state) {
        Objects.requireNonNull(tag, "tag");
        Objects.requireNonNull(state, "state");
        BlockPos position = state.getMiningPos();
        if (position == null) {
            // Callers may reuse an existing tag; missing state must remove stale coordinates.
            tag.remove("miningPosX");
            tag.remove("miningPosY");
            tag.remove("miningPosZ");
        } else {
            tag.putInt("miningPosX", position.getX());
            tag.putInt("miningPosY", position.getY());
            tag.putInt("miningPosZ", position.getZ());
        }
        tag.putInt("miningProgress", state.getMiningProgress());
        tag.putInt("shakeTime", state.getShakeTime());
        tag.putBoolean("isMoving", state.isMoving());
        tag.putFloat("technoResist", state.technoResist());
        tag.putInt("enlightenedTick", state.getEnlightenedTick());
        tag.putInt("airTick", state.getTicksInAir());
        tag.putInt("flashTick", state.getFlashTick());
        return tag;
    }

    public static <T extends ICHCap> T load(CompoundTag tag, T state) {
        Objects.requireNonNull(tag, "tag");
        Objects.requireNonNull(state, "state");
        if (tag.contains("miningPosX", Tag.TAG_ANY_NUMERIC)
                && tag.contains("miningPosY", Tag.TAG_ANY_NUMERIC)
                && tag.contains("miningPosZ", Tag.TAG_ANY_NUMERIC)) {
            state.setMiningPos(new BlockPos(tag.getInt("miningPosX"),
                    tag.getInt("miningPosY"), tag.getInt("miningPosZ")));
        } else {
            state.setMiningPos(null);
        }
        // These are complete snapshots, not deltas. Missing fields mean their defaults,
        // including old snapshots which omitted zero progress or zero resistance.
        state.setMiningProgress(tag.getInt("miningProgress"));
        state.setShakeTime(tag.getInt("shakeTime"));
        state.setMoving(tag.getBoolean("isMoving"));
        state.setTechnoResist(tag.getFloat("technoResist"));
        state.setEnlightenedTick(tag.getInt("enlightenedTick"));
        state.setTicksInAir(tag.getInt("airTick"));
        state.setFlashTick(tag.getInt("flashTick"));
        return state;
    }
}

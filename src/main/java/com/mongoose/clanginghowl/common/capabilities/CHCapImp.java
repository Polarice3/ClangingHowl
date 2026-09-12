package com.mongoose.clanginghowl.common.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

public class CHCapImp implements ICHCap, INBTSerializable<CompoundTag> {
    private int miningProgress = 0;
    @Nullable
    private BlockPos miningPos = null;
    private int shakeTime = 0;
    private boolean moving = false;
    private float resist = 0;
    private int enlightenedTick = 0;
    private int ticksInAir = 0;
    private int flash = 0;

    @Override
    public int getMiningProgress() {
        return this.miningProgress;
    }

    @Override
    public void setMiningProgress(int tick) {
        this.miningProgress = tick;
    }

    @Override
    public @Nullable BlockPos getMiningPos() {
        return this.miningPos;
    }

    @Override
    public void setMiningPos(@Nullable BlockPos blockPos) {
        this.miningPos = blockPos == null ? null : blockPos.immutable();
    }

    @Override
    public int getShakeTime() {
        return this.shakeTime;
    }

    @Override
    public void setShakeTime(int ticks) {
        this.shakeTime = ticks;
    }

    @Override
    public boolean isMoving() {
        return this.moving;
    }

    @Override
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    @Override
    public float technoResist() {
        return this.resist;
    }

    @Override
    public void setTechnoResist(float resist) {
        this.resist = resist;
    }

    @Override
    public int getEnlightenedTick() {
        return this.enlightenedTick;
    }

    @Override
    public void setEnlightenedTick(int tick) {
        this.enlightenedTick = tick;
    }

    @Override
    public int getTicksInAir() {
        return this.ticksInAir;
    }

    @Override
    public void setTicksInAir(int ticksInAir) {
        this.ticksInAir = ticksInAir;
    }

    @Override
    public int getFlashTick() {
        return this.flash;
    }

    @Override
    public void setFlashTick(int tick) {
        this.flash = tick;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return CHCapSerialization.save(new CompoundTag(), this);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        CHCapSerialization.load(tag, this);
    }
}

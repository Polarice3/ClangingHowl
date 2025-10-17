package com.mongoose.clanginghowl.common.capabilities;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CHCapImp implements ICHCap {
    private int miningProgress = 0;
    @Nullable
    private BlockPos miningPos = null;
    private int shakeTime = 0;
    private boolean moving = false;

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
    public void setMiningPos(BlockPos blockPos) {
        this.miningPos = blockPos;
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
}

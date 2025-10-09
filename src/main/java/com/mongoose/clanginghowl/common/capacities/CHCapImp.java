package com.mongoose.clanginghowl.common.capacities;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CHCapImp implements ICHCap {
    private int miningProgress = 0;
    @Nullable
    private BlockPos miningPos = null;

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
}

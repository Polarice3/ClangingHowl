package com.mongoose.clanginghowl.common.capacities;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface ICHCap {
    int getMiningProgress();
    void setMiningProgress(int tick);
    @Nullable
    BlockPos getMiningPos();
    void setMiningPos(BlockPos blockPos);
}

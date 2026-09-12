package com.mongoose.clanginghowl.common.capabilities;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public interface ICHCap {
    int getMiningProgress();
    void setMiningProgress(int tick);
    @Nullable
    BlockPos getMiningPos();
    void setMiningPos(@Nullable BlockPos blockPos);
    int getShakeTime();
    void setShakeTime(int ticks);
    boolean isMoving();
    void setMoving(boolean moving);
    float technoResist();
    void setTechnoResist(float resist);
    int getEnlightenedTick();
    void setEnlightenedTick(int tick);
    int getTicksInAir();
    void setTicksInAir(int tick);
    int getFlashTick();
    void setFlashTick(int tick);
}

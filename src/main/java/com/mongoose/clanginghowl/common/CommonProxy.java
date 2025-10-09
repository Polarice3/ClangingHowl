package com.mongoose.clanginghowl.common;

import com.mongoose.clanginghowl.init.CHProxy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CommonProxy implements CHProxy {
    @Nullable
    @Override
    public Player getPlayer() {
        return null;
    }

    @Nullable
    @Override
    public Level getLevel() {
        return null;
    }
}

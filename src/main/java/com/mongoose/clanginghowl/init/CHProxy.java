package com.mongoose.clanginghowl.init;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface CHProxy {
    @Nullable
    Player getPlayer();
    @Nullable
    Level getLevel();
}

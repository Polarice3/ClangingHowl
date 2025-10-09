package com.mongoose.clanginghowl.client;

import com.mongoose.clanginghowl.init.CHProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ClientProxy implements CHProxy {
    @Nullable
    @Override
    public Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    @Nullable
    @Override
    public Level getLevel() {
        return Minecraft.getInstance().level;
    }
}

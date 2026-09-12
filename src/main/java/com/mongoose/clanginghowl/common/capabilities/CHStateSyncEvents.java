package com.mongoose.clanginghowl.common.capabilities;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.network.CHNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/** Lifecycle snapshots supplement the existing incremental state notifications. */
@EventBusSubscriber(modid = ClangingHowl.MOD_ID)
public final class CHStateSyncEvents {
    private CHStateSyncEvents() {
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // NeoForge handles non-death attachment copies. The attachment intentionally
            // does not copy on death; an interrupted mining action never survives a clone.
            ICHCap state = CHCapHelper.getCapability(player);
            state.setMiningProgress(0);
            state.setMiningPos(null);
        }
        // Do not send here: the replacement player has not completed respawning yet.
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer player
                && event.getTarget() instanceof LivingEntity living) {
            CHNetwork.sendTo(player, new CHCapUpdatePacket(living));
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        syncSelf(event.getEntity());
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncSelf(event.getEntity());
    }

    @SubscribeEvent
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncSelf(event.getEntity());
    }

    private static void syncSelf(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            CHNetwork.sendTo(serverPlayer, new CHCapUpdatePacket(serverPlayer));
        }
    }
}

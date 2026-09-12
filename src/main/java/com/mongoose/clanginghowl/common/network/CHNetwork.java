package com.mongoose.clanginghowl.common.network;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.capabilities.CHCapUpdatePacket;
import com.mongoose.clanginghowl.common.network.client.CActivateCurioKeyPacket;
import com.mongoose.clanginghowl.common.network.client.CIsMovingPacket;
import com.mongoose.clanginghowl.common.network.client.CJetBootsJumpPacket;
import com.mongoose.clanginghowl.common.network.server.SInstaLookPacket;
import com.mongoose.clanginghowl.common.network.server.SPlayWorldSoundPacket;
import com.mongoose.clanginghowl.common.network.server.SReanimatorDeathPacket;
import com.mongoose.clanginghowl.common.network.server.SSendCHWorldData;
import java.util.Objects;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = ClangingHowl.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class CHNetwork {
    // Bump whenever a payload layout or interpretation changes incompatibly.
    private static final String PROTOCOL_VERSION = "1.21.1-1";

    private CHNetwork() {
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        // The default registrar is mandatory. Do not accept arbitrary peer versions.
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION).executesOn(HandlerThread.MAIN);
        registrar.playToServer(CIsMovingPacket.TYPE, CIsMovingPacket.STREAM_CODEC, CIsMovingPacket::consume);
        registrar.playToServer(CActivateCurioKeyPacket.TYPE, CActivateCurioKeyPacket.STREAM_CODEC, CActivateCurioKeyPacket::consume);
        registrar.playToServer(CJetBootsJumpPacket.TYPE, CJetBootsJumpPacket.STREAM_CODEC, CJetBootsJumpPacket::consume);
        registrar.playToClient(SInstaLookPacket.TYPE, SInstaLookPacket.STREAM_CODEC, SInstaLookPacket::consume);
        registrar.playToClient(SPlayWorldSoundPacket.TYPE, SPlayWorldSoundPacket.STREAM_CODEC, SPlayWorldSoundPacket::consume);
        registrar.playToClient(SSendCHWorldData.TYPE, SSendCHWorldData.STREAM_CODEC, SSendCHWorldData::consume);
        registrar.playToClient(SReanimatorDeathPacket.TYPE, SReanimatorDeathPacket.STREAM_CODEC, SReanimatorDeathPacket::consume);
        registrar.playToClient(CHCapUpdatePacket.TYPE, CHCapUpdatePacket.STREAM_CODEC, CHCapUpdatePacket::consume);
    }

    public static <MSG extends CustomPacketPayload> void sendTo(Player player, MSG msg) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            throw new IllegalArgumentException("Clientbound payload requires a server player");
        }
        PacketDistributor.sendToPlayer(serverPlayer, msg);
    }

    public static <MSG extends CustomPacketPayload> void sendToServer(MSG msg) {
        if (msg instanceof CIsMovingPacket movement) {
            Player player = ClangingHowl.PROXY.getPlayer();
            if (player == null || movement.entityID() != player.getId()) {
                return;
            }
        }
        PacketDistributor.sendToServer(msg);
    }

    public static <MSG extends CustomPacketPayload> void sentToTrackingChunk(LevelChunk chunk, MSG msg) {
        if (!(chunk.getLevel() instanceof ServerLevel level)) {
            throw new IllegalArgumentException("Clientbound payload requires a server chunk");
        }
        PacketDistributor.sendToPlayersTrackingChunk(level, chunk.getPos(), msg);
    }

    public static <MSG extends CustomPacketPayload> void sentToTrackingEntity(Entity entity, MSG msg) {
        PacketDistributor.sendToPlayersTrackingEntity(entity, msg);
    }

    public static <MSG extends CustomPacketPayload> void sentToTrackingEntityAndPlayer(Entity entity, MSG msg) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, msg);
    }

    public static <MSG extends CustomPacketPayload> void sendToALL(MSG msg) {
        PacketDistributor.sendToAllPlayers(msg);
    }

    public static <MSG extends CustomPacketPayload> void sendToClient(ServerPlayer player, MSG msg) {
        PacketDistributor.sendToPlayer(player, msg);
    }

    public static <MSG extends CustomPacketPayload> void sendToClientLevel(MSG message, ResourceKey<Level> dimension) {
        MinecraftServer server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(),
                "Clientbound payload requires a running server");
        ServerLevel level = server.getLevel(dimension);
        if (level != null) {
            PacketDistributor.sendToPlayersInDimension(level, message);
        }
    }
}

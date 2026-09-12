package com.mongoose.clanginghowl.common.network.client;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.server.level.ServerPlayer;

public class CIsMovingPacket implements CustomPacketPayload {
    public static final Type<CIsMovingPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ClangingHowl.MOD_ID, "is_moving"));
    public static final StreamCodec<FriendlyByteBuf, CIsMovingPacket> STREAM_CODEC = StreamCodec.of(
            (buffer, packet) -> encode(packet, buffer), CIsMovingPacket::decode);

    @Override
    public Type<CIsMovingPacket> type() {
        return TYPE;
    }

    private final int entityID;
    private final boolean moving;

    public CIsMovingPacket(int entityID, boolean moving) {
        this.entityID = entityID;
        this.moving = moving;
    }

    public static void encode(CIsMovingPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityID);
        buf.writeBoolean(packet.moving);
    }

    public static CIsMovingPacket decode(FriendlyByteBuf buf) {
        return new CIsMovingPacket(
                buf.readInt(),
                buf.readBoolean()
        );
    }

    public int entityID() {
        return entityID;
    }

    public boolean moving() {
        return moving;
    }

    public static void consume(CIsMovingPacket packet, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player) || packet.entityID != player.getId()) {
            return;
        }
        // A client may only report movement for its own player, never another entity.
        CHCapHelper.setMoving(player, packet.moving);
    }
}

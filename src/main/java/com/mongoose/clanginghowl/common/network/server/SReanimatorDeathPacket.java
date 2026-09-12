package com.mongoose.clanginghowl.common.network.server;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.mongoose.clanginghowl.client.network.CHClientPayloadHandlers;

public class SReanimatorDeathPacket implements CustomPacketPayload {
    public static final Type<SReanimatorDeathPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ClangingHowl.MOD_ID, "reanimator_death"));
    public static final StreamCodec<FriendlyByteBuf, SReanimatorDeathPacket> STREAM_CODEC = StreamCodec.of(
            (buffer, packet) -> encode(packet, buffer), SReanimatorDeathPacket::decode);

    @Override
    public Type<SReanimatorDeathPacket> type() {
        return TYPE;
    }

    public SReanimatorDeathPacket() {
    }

    public static void encode(SReanimatorDeathPacket packet, FriendlyByteBuf buffer) {
    }

    public static SReanimatorDeathPacket decode(FriendlyByteBuf buffer) {
        return new SReanimatorDeathPacket();
    }

    public static void consume(SReanimatorDeathPacket packet, IPayloadContext context) {
        CHClientPayloadHandlers.handle(packet);
    }
}

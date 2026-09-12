package com.mongoose.clanginghowl.common.network.client;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.server.level.ServerPlayer;

/**
 * Based on DoubleJumpPacket from Aether-Redux codes: <a href="https://github.com/Zepalesque/The-Aether-Redux/blob/1.20.1/src/main/java/net/zepalesque/redux/network/packet/DoubleJumpPacket.java">...</a>
 */
public class CJetBootsJumpPacket implements CustomPacketPayload {
    public static final Type<CJetBootsJumpPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ClangingHowl.MOD_ID, "jet_boots_jump"));
    public static final StreamCodec<FriendlyByteBuf, CJetBootsJumpPacket> STREAM_CODEC = StreamCodec.of(
            (buffer, packet) -> encode(packet, buffer), CJetBootsJumpPacket::decode);

    @Override
    public Type<CJetBootsJumpPacket> type() {
        return TYPE;
    }

    public static void encode(CJetBootsJumpPacket packet, FriendlyByteBuf buffer) {
    }

    public static CJetBootsJumpPacket decode(FriendlyByteBuf buffer) {
        return new CJetBootsJumpPacket();
    }

    public static void consume(CJetBootsJumpPacket packet, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            CHCapHelper.doubleJump(player);
        }
    }
}

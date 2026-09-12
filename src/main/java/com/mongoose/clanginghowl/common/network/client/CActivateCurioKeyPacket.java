package com.mongoose.clanginghowl.common.network.client;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.items.curios.IActivatable;
import com.mongoose.clanginghowl.utils.CHCuriosFinder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class CActivateCurioKeyPacket implements CustomPacketPayload {
    public static final Type<CActivateCurioKeyPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ClangingHowl.MOD_ID, "activate_curio_key"));
    public static final StreamCodec<FriendlyByteBuf, CActivateCurioKeyPacket> STREAM_CODEC = StreamCodec.of(
            (buffer, packet) -> encode(packet, buffer), CActivateCurioKeyPacket::decode);

    @Override
    public Type<CActivateCurioKeyPacket> type() {
        return TYPE;
    }

    public static void encode(CActivateCurioKeyPacket packet, FriendlyByteBuf buffer) {
    }

    public static CActivateCurioKeyPacket decode(FriendlyByteBuf buffer) {
        return new CActivateCurioKeyPacket();
    }

    public static void consume(CActivateCurioKeyPacket packet, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack stack = CHCuriosFinder.findCurio(player, itemStack -> itemStack.getItem() instanceof IActivatable);
        if (!stack.isEmpty() && stack.getItem() instanceof IActivatable activatable) {
            activatable.activate(player.level(), player, stack);
        }
    }
}

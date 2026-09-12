package com.mongoose.clanginghowl.common.network.server;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.mongoose.clanginghowl.client.network.CHClientPayloadHandlers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class SInstaLookPacket implements CustomPacketPayload {
    public static final Type<SInstaLookPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ClangingHowl.MOD_ID, "insta_look"));
    public static final StreamCodec<FriendlyByteBuf, SInstaLookPacket> STREAM_CODEC = StreamCodec.of(
            (buffer, packet) -> encode(packet, buffer), SInstaLookPacket::decode);

    @Override
    public Type<SInstaLookPacket> type() {
        return TYPE;
    }

    public int looker;
    public int target;

    public SInstaLookPacket(int lookerId, int targetId){
        this.looker = lookerId;
        this.target = targetId;
    }

    public SInstaLookPacket(Mob looker, Entity target){
        this.looker = looker.getId();
        this.target = target.getId();
    }

    public static void encode(SInstaLookPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.looker);
        buffer.writeInt(packet.target);
    }

    public static SInstaLookPacket decode(FriendlyByteBuf buffer) {
        return new SInstaLookPacket(
                buffer.readInt(),
                buffer.readInt());
    }

    public static void consume(SInstaLookPacket packet, IPayloadContext context) {
        CHClientPayloadHandlers.handle(packet);
    }
}

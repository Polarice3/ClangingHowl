package com.mongoose.clanginghowl.common.capabilities;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.mongoose.clanginghowl.client.network.CHClientPayloadHandlers;
import java.util.Objects;
import io.netty.handler.codec.DecoderException;
import net.minecraft.world.entity.LivingEntity;

public class CHCapUpdatePacket implements CustomPacketPayload {
    public static final Type<CHCapUpdatePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ClangingHowl.MOD_ID, "cap_update"));
    public static final StreamCodec<FriendlyByteBuf, CHCapUpdatePacket> STREAM_CODEC = StreamCodec.of(
            (buffer, packet) -> encode(packet, buffer), CHCapUpdatePacket::decode);

    @Override
    public Type<CHCapUpdatePacket> type() {
        return TYPE;
    }

    private final int entityID;
    private final CompoundTag tag;

    public CHCapUpdatePacket(int id, CompoundTag tag) {
        this.entityID = id;
        this.tag = Objects.requireNonNull(tag, "tag").copy();
    }

    /** Capture the entity's actual attached state, never a detached fallback instance. */
    public CHCapUpdatePacket(LivingEntity living) {
        this(living.getId(), CHCapSerialization.save(new CompoundTag(), CHCapHelper.getCapability(living)));
    }

    public static void encode(CHCapUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityID);
        buffer.writeNbt(packet.tag);
    }

    public static CHCapUpdatePacket decode(FriendlyByteBuf buffer) {
        int entityID = buffer.readInt();
        CompoundTag tag = buffer.readNbt();
        if (tag == null) {
            throw new DecoderException("Missing Clanging Howl capability state");
        }
        return new CHCapUpdatePacket(entityID, tag);
    }

    public int entityID() {
        return entityID;
    }

    public CompoundTag tag() {
        return tag.copy();
    }

    public static void consume(CHCapUpdatePacket packet, IPayloadContext context) {
        CHClientPayloadHandlers.handle(packet);
    }
}

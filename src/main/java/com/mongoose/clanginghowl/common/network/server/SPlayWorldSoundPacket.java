package com.mongoose.clanginghowl.common.network.server;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.mongoose.clanginghowl.client.network.CHClientPayloadHandlers;
import net.minecraft.sounds.SoundEvent;

public class SPlayWorldSoundPacket implements CustomPacketPayload {
    public static final Type<SPlayWorldSoundPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ClangingHowl.MOD_ID, "play_world_sound"));
    public static final StreamCodec<FriendlyByteBuf, SPlayWorldSoundPacket> STREAM_CODEC = StreamCodec.of(
            (buffer, packet) -> encode(packet, buffer), SPlayWorldSoundPacket::decode);

    @Override
    public Type<SPlayWorldSoundPacket> type() {
        return TYPE;
    }

    private final BlockPos blockPos;
    private final SoundEvent soundEvent;
    private final float volume;
    private final float pitch;

    public SPlayWorldSoundPacket(BlockPos blockPos, SoundEvent soundEvent, float volume, float pitch){
        this.blockPos = blockPos.immutable();
        this.soundEvent = soundEvent;
        this.volume = volume;
        this.pitch = pitch;
    }

    public static void encode(SPlayWorldSoundPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.blockPos);
        buffer.writeResourceLocation(packet.soundEvent.getLocation());
        buffer.writeFloat(packet.volume);
        buffer.writeFloat(packet.pitch);
    }

    public static SPlayWorldSoundPacket decode(FriendlyByteBuf buffer) {
        return new SPlayWorldSoundPacket(buffer.readBlockPos(), SoundEvent.createVariableRangeEvent(buffer.readResourceLocation()), buffer.readFloat(), buffer.readFloat());
    }

    public BlockPos blockPos() {
        return blockPos;
    }

    public SoundEvent soundEvent() {
        return soundEvent;
    }

    public float volume() {
        return volume;
    }

    public float pitch() {
        return pitch;
    }

    public static void consume(SPlayWorldSoundPacket packet, IPayloadContext context) {
        CHClientPayloadHandlers.handle(packet);
    }
}

package com.mongoose.clanginghowl.common.network.server;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.mongoose.clanginghowl.client.network.CHClientPayloadHandlers;

public class SSendCHWorldData implements CustomPacketPayload {
    public static final Type<SSendCHWorldData> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ClangingHowl.MOD_ID, "world_data"));
    public static final StreamCodec<FriendlyByteBuf, SSendCHWorldData> STREAM_CODEC = StreamCodec.of(
            (buffer, packet) -> encode(packet, buffer), SSendCHWorldData::decode);

    @Override
    public Type<SSendCHWorldData> type() {
        return TYPE;
    }

    public boolean isMeteorShower;
    public boolean isMeteorFlash;
    public boolean playMeteorExplode;
    public boolean playMeteorMusic;
    public boolean isMeteorMusicFullVolume;
    public int meteorFlashTick;

    public SSendCHWorldData(boolean isMeteorShower, boolean isMeteorFlash, boolean playMeteorExplode, boolean playMeteorMusic, boolean isMeteorMusicFullVolume, int meteorFlashTick){
        this.isMeteorShower = isMeteorShower;
        this.isMeteorFlash = isMeteorFlash;
        this.playMeteorExplode = playMeteorExplode;
        this.playMeteorMusic = playMeteorMusic;
        this.isMeteorMusicFullVolume = isMeteorMusicFullVolume;
        this.meteorFlashTick = meteorFlashTick;
    }

    public static void encode(SSendCHWorldData packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.isMeteorShower);
        buffer.writeBoolean(packet.isMeteorFlash);
        buffer.writeBoolean(packet.playMeteorExplode);
        buffer.writeBoolean(packet.playMeteorMusic);
        buffer.writeBoolean(packet.isMeteorMusicFullVolume);
        buffer.writeInt(packet.meteorFlashTick);
    }

    public static SSendCHWorldData decode(FriendlyByteBuf buffer) {
        return new SSendCHWorldData(
                buffer.readBoolean(),
                buffer.readBoolean(),
                buffer.readBoolean(),
                buffer.readBoolean(),
                buffer.readBoolean(),
                buffer.readInt());
    }

    public static void consume(SSendCHWorldData packet, IPayloadContext context) {
        CHClientPayloadHandlers.handle(packet);
    }

    public static void handleSync(SSendCHWorldData packet) {
        CHClientPayloadHandlers.handle(packet);
    }
}

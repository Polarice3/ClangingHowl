package com.mongoose.clanginghowl.common.network;

import com.mongoose.clanginghowl.common.capabilities.CHCapUpdatePacket;
import com.mongoose.clanginghowl.common.network.client.CActivateCurioKeyPacket;
import com.mongoose.clanginghowl.common.network.client.CIsMovingPacket;
import com.mongoose.clanginghowl.common.network.client.CJetBootsJumpPacket;
import com.mongoose.clanginghowl.common.network.server.SInstaLookPacket;
import com.mongoose.clanginghowl.common.network.server.SPlayWorldSoundPacket;
import com.mongoose.clanginghowl.common.network.server.SReanimatorDeathPacket;
import com.mongoose.clanginghowl.common.network.server.SSendCHWorldData;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Codec tests only. They do not replace integrated/dedicated-server smoke tests. */
class PayloadCodecTest {
    @Test
    void movementRoundTrip() {
        for (int id : new int[]{0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE}) {
            for (boolean moving : new boolean[]{false, true}) {
                var result = roundTrip(CIsMovingPacket.STREAM_CODEC, new CIsMovingPacket(id, moving));
                assertEquals(id, result.entityID());
                assertEquals(moving, result.moving());
            }
        }
    }

    @Test
    void activateCurioHasEmptyBody() {
        assertEmpty(CActivateCurioKeyPacket.STREAM_CODEC, new CActivateCurioKeyPacket());
    }

    @Test
    void jetBootsHasEmptyBody() {
        assertEmpty(CJetBootsJumpPacket.STREAM_CODEC, new CJetBootsJumpPacket());
    }

    @Test
    void reanimatorHasEmptyBody() {
        assertEmpty(SReanimatorDeathPacket.STREAM_CODEC, new SReanimatorDeathPacket());
    }

    @Test
    void instantLookRoundTrip() {
        var result = roundTrip(SInstaLookPacket.STREAM_CODEC, new SInstaLookPacket(42, 9001));
        assertEquals(42, result.looker);
        assertEquals(9001, result.target);
    }

    @Test
    void soundRoundTripAndImmutablePosition() {
        var position = new BlockPos.MutableBlockPos(-13, 71, 900);
        var soundId = ResourceLocation.fromNamespaceAndPath("clanginghowl", "codec_test");
        var packet = new SPlayWorldSoundPacket(position, SoundEvent.createVariableRangeEvent(soundId), 0.25F, 1.5F);
        position.set(0, 0, 0);
        var result = roundTrip(SPlayWorldSoundPacket.STREAM_CODEC, packet);
        assertEquals(new BlockPos(-13, 71, 900), result.blockPos());
        assertEquals(soundId, result.soundEvent().getLocation());
        assertEquals(0.25F, result.volume());
        assertEquals(1.5F, result.pitch());
    }

    @Test
    void allMeteorFlagCombinationsRoundTrip() {
        for (int flags = 0; flags < 32; flags++) {
            var result = roundTrip(SSendCHWorldData.STREAM_CODEC, new SSendCHWorldData(
                    (flags & 1) != 0, (flags & 2) != 0, (flags & 4) != 0,
                    (flags & 8) != 0, (flags & 16) != 0, 12345));
            assertEquals((flags & 1) != 0, result.isMeteorShower);
            assertEquals((flags & 2) != 0, result.isMeteorFlash);
            assertEquals((flags & 4) != 0, result.playMeteorExplode);
            assertEquals((flags & 8) != 0, result.playMeteorMusic);
            assertEquals((flags & 16) != 0, result.isMeteorMusicFullVolume);
            assertEquals(12345, result.meteorFlashTick);
        }
    }

    @Test
    void capabilityTagRoundTripAndDefensiveCopies() {
        var nested = new CompoundTag();
        nested.putBoolean("moving", true);
        var original = new CompoundTag();
        original.putInt("energy", 73);
        original.put("nested", nested);
        var expected = original.copy();
        var packet = new CHCapUpdatePacket(42, original);
        original.putInt("energy", -1);
        nested.putBoolean("moving", false);
        var result = roundTrip(CHCapUpdatePacket.STREAM_CODEC, packet);
        assertEquals(42, result.entityID());
        assertEquals(expected, result.tag());
        var exposed = result.tag();
        exposed.putInt("energy", -2);
        assertEquals(expected, result.tag());
    }

    @Test
    void missingCapabilityTagIsRejectedOnDecode() {
        var buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            buffer.writeInt(42);
            buffer.writeNbt(null);
            assertThrows(DecoderException.class, () -> CHCapUpdatePacket.STREAM_CODEC.decode(buffer));
        } finally {
            buffer.release();
        }
    }

    @Test
    void nullCapabilityTagIsRejectedByConstructor() {
        assertThrows(NullPointerException.class, () -> new CHCapUpdatePacket(42, null));
    }

    @Test
    void truncatedMovementBodyIsRejected() {
        var buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            buffer.writeInt(42);
            assertThrows(IndexOutOfBoundsException.class, () -> CIsMovingPacket.STREAM_CODEC.decode(buffer));
        } finally {
            buffer.release();
        }
    }

    @Test
    void allPayloadIdentifiersAreUniqueAndNamespaced() {
        List<CustomPacketPayload.Type<?>> types = List.of(CIsMovingPacket.TYPE, CActivateCurioKeyPacket.TYPE,
                CJetBootsJumpPacket.TYPE, SInstaLookPacket.TYPE, SPlayWorldSoundPacket.TYPE,
                SSendCHWorldData.TYPE, SReanimatorDeathPacket.TYPE, CHCapUpdatePacket.TYPE);
        assertEquals(8L, types.stream().map(CustomPacketPayload.Type::id).distinct().count());
        assertTrue(types.stream().allMatch(type -> "clanginghowl".equals(type.id().getNamespace())));
    }

    private static <T extends CustomPacketPayload> T roundTrip(StreamCodec<FriendlyByteBuf, T> codec, T value) {
        var buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            codec.encode(buffer, value);
            T result = codec.decode(buffer);
            assertEquals(value.type(), result.type());
            assertEquals(0, buffer.readableBytes(), "Decoder must consume the whole body");
            return result;
        } finally {
            buffer.release();
        }
    }

    private static <T extends CustomPacketPayload> void assertEmpty(StreamCodec<FriendlyByteBuf, T> codec, T value) {
        var buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            codec.encode(buffer, value);
            assertEquals(0, buffer.readableBytes());
            assertEquals(value.type(), codec.decode(buffer).type());
        } finally {
            buffer.release();
        }
    }
}

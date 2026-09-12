package com.mongoose.clanginghowl.common.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Real Minecraft NBT tests. Requires the ModDevGradle unit-test classpath. */
class CHCapSerializationTest {
    private static CHCapImp populated() {
        CHCapImp state = new CHCapImp();
        state.setMiningPos(new BlockPos(12, -64, -37));
        state.setMiningProgress(19);
        state.setShakeTime(8);
        state.setMoving(true);
        state.setTechnoResist(31.5F);
        state.setEnlightenedTick(5);
        state.setTicksInAir(43);
        state.setFlashTick(7);
        return state;
    }

    private static void assertDefault(ICHCap state) {
        assertNull(state.getMiningPos());
        assertEquals(0, state.getMiningProgress());
        assertEquals(0, state.getShakeTime());
        assertFalse(state.isMoving());
        assertEquals(0.0F, state.technoResist());
        assertEquals(0, state.getEnlightenedTick());
        assertEquals(0, state.getTicksInAir());
        assertEquals(0, state.getFlashTick());
    }

    @Test
    void emptySnapshotClearsPreviousState() {
        CHCapImp state = populated();
        CHCapSerialization.load(new CompoundTag(), state);
        assertDefault(state);
    }

    @Test
    void allFieldsRoundTrip() {
        CHCapImp source = populated();
        CHCapImp target = new CHCapImp();
        CompoundTag tag = CHCapSerialization.save(new CompoundTag(), source);
        assertSame(target, CHCapSerialization.load(tag, target));
        assertEquals(source.getMiningPos(), target.getMiningPos());
        assertEquals(source.getMiningProgress(), target.getMiningProgress());
        assertEquals(source.getShakeTime(), target.getShakeTime());
        assertEquals(source.isMoving(), target.isMoving());
        assertEquals(source.technoResist(), target.technoResist());
        assertEquals(source.getEnlightenedTick(), target.getEnlightenedTick());
        assertEquals(source.getTicksInAir(), target.getTicksInAir());
        assertEquals(source.getFlashTick(), target.getFlashTick());
    }

    @Test
    void zeroSnapshotReplacesNonzeroValues() {
        CHCapImp state = populated();
        CompoundTag snapshot = CHCapSerialization.save(new CompoundTag(), new CHCapImp());
        CHCapSerialization.load(snapshot, state);
        assertDefault(state);
    }

    @Test
    void partialPositionDoesNotRetainOldPosition() {
        CHCapImp state = populated();
        CompoundTag tag = new CompoundTag();
        tag.putInt("miningPosX", 4);
        tag.putInt("miningPosY", 5);
        CHCapSerialization.load(tag, state);
        assertNull(state.getMiningPos());
    }

    @Test
    void malformedPositionDoesNotBecomeOrigin() {
        CHCapImp state = populated();
        CompoundTag tag = new CompoundTag();
        tag.putString("miningPosX", "wrong type");
        tag.putInt("miningPosY", 5);
        tag.putInt("miningPosZ", 6);
        CHCapSerialization.load(tag, state);
        assertNull(state.getMiningPos());
    }

    @Test
    void savingIntoReusedTagRemovesOldPositionAndZerosFields() {
        CompoundTag tag = CHCapSerialization.save(new CompoundTag(), populated());
        tag.putString("unrelated", "keep");
        assertSame(tag, CHCapSerialization.save(tag, new CHCapImp()));
        for (String key : new String[] {"miningPosX", "miningPosY", "miningPosZ"}) {
            assertFalse(tag.contains(key));
        }
        assertEquals("keep", tag.getString("unrelated"));
        CHCapImp state = populated();
        CHCapSerialization.load(tag, state);
        assertDefault(state);
    }

    @Test
    void originIsNotConfusedWithMissingPosition() {
        CHCapImp source = new CHCapImp();
        source.setMiningPos(BlockPos.ZERO);
        CHCapImp target = new CHCapImp();
        CHCapSerialization.load(CHCapSerialization.save(new CompoundTag(), source), target);
        assertEquals(BlockPos.ZERO, target.getMiningPos());
    }

    @Test
    void mutablePositionIsCopiedOnAssignment() {
        BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos(1, 2, 3);
        CHCapImp state = new CHCapImp();
        state.setMiningPos(position);
        position.set(4, 5, 6);
        assertEquals(new BlockPos(1, 2, 3), state.getMiningPos());
    }

    @Test
    void stateInstancesDoNotShareMutableValues() {
        CHCapImp first = populated();
        CHCapImp second = new CHCapImp();
        CHCapSerialization.load(CHCapSerialization.save(new CompoundTag(), first), second);
        first.setMiningProgress(0);
        first.setMiningPos(null);
        assertEquals(19, second.getMiningProgress());
        assertEquals(new BlockPos(12, -64, -37), second.getMiningPos());
    }

    @Test
    void serializableAttachmentUsesTheSameSnapshotFormat() {
        CHCapImp source = populated();
        CompoundTag tag = source.serializeNBT(RegistryAccess.EMPTY);
        assertEquals(CHCapSerialization.save(new CompoundTag(), source), tag);
        CHCapImp target = new CHCapImp();
        target.deserializeNBT(RegistryAccess.EMPTY, tag);
        assertEquals(tag, target.serializeNBT(RegistryAccess.EMPTY));
    }

    @Test
    void loadingSnapshotDoesNotModifyInputTag() {
        CompoundTag tag = CHCapSerialization.save(new CompoundTag(), populated());
        CompoundTag before = tag.copy();
        CHCapSerialization.load(tag, new CHCapImp());
        assertEquals(before, tag);
    }

    @Test
    void loadingDefaultsDoesNotRequireClientOrGameRegistries() {
        CHCapImp state = populated();
        state.deserializeNBT(RegistryAccess.EMPTY, new CompoundTag());
        assertDefault(state);
    }
}

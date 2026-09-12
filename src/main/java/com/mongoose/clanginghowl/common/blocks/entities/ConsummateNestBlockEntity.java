package com.mongoose.clanginghowl.common.blocks.entities;

import com.mongoose.clanginghowl.common.blocks.CHBlockStates;
import com.mongoose.clanginghowl.common.blocks.entities.consummate_nest.ConsummateNest;
import com.mongoose.clanginghowl.common.blocks.entities.consummate_nest.ConsummateNestState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ConsummateNestBlockEntity extends BlockEntity implements ConsummateNest.StateAccessor{
    private final ConsummateNest consummateNest;

    public ConsummateNestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CHBlockEntities.CONSUMMATE_NEST.get(), blockPos, blockState);
        this.consummateNest = new ConsummateNest(this);
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(compoundTag, registries);
        this.consummateNest.load(compoundTag.getCompound("ConsummateNestData"));
        if (this.level != null) {
            this.markUpdated();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(compoundTag, registries);
        compoundTag.put("ConsummateNestData", this.consummateNest.save());
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public ConsummateNest getConsummateNest() {
        return this.consummateNest;
    }

    @Override
    public ConsummateNestState getState() {
        return !this.getBlockState().hasProperty(CHBlockStates.CONSUMMATE_NEST_STATE)
                ? ConsummateNestState.INACTIVE
                : this.getBlockState().getValue(CHBlockStates.CONSUMMATE_NEST_STATE);
    }

    @Override
    public void setState(Level level, ConsummateNestState spawnerState) {
        this.setChanged();
        level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(CHBlockStates.CONSUMMATE_NEST_STATE, spawnerState));
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, net.minecraft.core.HolderLookup.Provider registries) {
        if (pkt.getTag() != null) {
            this.loadAdditional(pkt.getTag(), registries);
        }
        super.onDataPacket(net, pkt, registries);
    }

    @Override
    public void markUpdated() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }

    }
}

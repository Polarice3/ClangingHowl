package com.mongoose.clanginghowl.common.blocks.entities;

import com.mongoose.clanginghowl.common.blocks.NerveEndingsBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NerveEndingsBlockEntity extends BlockEntity {
    public int tickTime;

    public NerveEndingsBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(CHBlockEntities.NERVE_ENDINGS.get(), p_155229_, p_155230_);
    }

    public void tick() {
        if (this.level != null) {
            if (!this.level.isClientSide) {
                if (this.getBlockState().getValue(NerveEndingsBlock.TRIGGERED)) {
                    ++this.tickTime;
                    if (this.tickTime >= 500) {
                        this.level.setBlock(this.worldPosition, this.getBlockState().setValue(NerveEndingsBlock.TRIGGERED, false), 3);
                    }
                } else {
                    if (this.tickTime > 0) {
                        this.tickTime = 0;
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries) {
        return this.writeNetwork(super.getUpdateTag(registries));
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, net.minecraft.core.HolderLookup.Provider registries) {
        this.readNetwork(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.readNetwork(tag);
    }

    public void readNetwork(CompoundTag tag) {
        this.tickTime = tag.getInt("CoolTick");
    }

    public CompoundTag writeNetwork(CompoundTag tag) {
        tag.putInt("CoolTick", this.tickTime);
        return tag;
    }

    public void markUpdated() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void loadAdditional(CompoundTag compound, net.minecraft.core.HolderLookup.Provider registries) {
        this.readNetwork(compound);
        super.loadAdditional(compound, registries);
    }

    @Override
    public void saveAdditional(CompoundTag compound, net.minecraft.core.HolderLookup.Provider registries) {
        this.writeNetwork(compound);
        super.saveAdditional(compound, registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}

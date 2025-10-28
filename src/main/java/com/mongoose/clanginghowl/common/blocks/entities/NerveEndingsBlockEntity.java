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
    public CompoundTag getUpdateTag() {
        return this.writeNetwork(super.getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.readNetwork(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.load(tag);
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
    public void load(CompoundTag compound) {
        this.readNetwork(compound);
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        this.writeNetwork(compound);
        super.saveAdditional(compound);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}

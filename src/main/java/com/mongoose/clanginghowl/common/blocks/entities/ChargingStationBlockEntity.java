package com.mongoose.clanginghowl.common.blocks.entities;

import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ChargingStationBlockEntity extends BlockEntity {
    public long lastChangeTime;
    public LazyOptional<ItemStackHandler> itemStackHandler = LazyOptional.of(
            () -> new ItemStackHandler(1) {
                @Override
                public int getSlotLimit(int slot) {
                    return 1;
                }

                @Override
                protected void onContentsChanged(int slot) {
                    if (ChargingStationBlockEntity.this.level != null) {
                        if (!ChargingStationBlockEntity.this.level.isClientSide) {
                            ChargingStationBlockEntity.this.lastChangeTime = ChargingStationBlockEntity.this.level
                                    .getGameTime();
                            boolean flag = !this.stacks.get(0).isEmpty();
                            ChargingStationBlockEntity.this.level.setBlockAndUpdate(ChargingStationBlockEntity.this.getBlockPos(),
                                    ChargingStationBlockEntity.this.getBlockState().setValue(BlockStateProperties.OCCUPIED, flag));
                            ChargingStationBlockEntity.this.markNetworkDirty();
                        }
                    }
                }
            });

    public ChargingStationBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CHBlockEntities.STATIONARY_CHARGING_STATION.get(), blockPos, blockState);
    }

    public ChargingStationBlockEntity(BlockEntityType<?> blockEntity, BlockPos blockPos, BlockState blockState){
        super(blockEntity, blockPos, blockState);
    }

    public void tick() {
        if (this.level != null) {
            IItemHandler handler = this.itemStackHandler.orElseThrow(RuntimeException::new);
            ItemStack tool = handler.getStackInSlot(0);
            if (!tool.isEmpty() && tool.getItem() instanceof IEnergyItem && !IEnergyItem.isFull(tool)) {
                if (this.level.getGameTime() % 20 == 0) {
                    IEnergyItem.powerItem(tool, 4);
                }
            }
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemStackHandler.cast();
        }
        return super.getCapability(cap, direction);
    }

    @Override
    public void load( CompoundTag compound) {
        this.readNetwork(compound);
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        this.writeNetwork(compound);
        super.saveAdditional(compound);
    }

    public void readNetwork(CompoundTag compound) {
        this.itemStackHandler.ifPresent((handler) -> handler.deserializeNBT(compound.getCompound("inventory")));
        this.lastChangeTime = compound.getLong("lastChangeTime");
    }

    public CompoundTag writeNetwork(CompoundTag compound) {
        this.itemStackHandler.ifPresent(handler -> compound.put("inventory", handler.serializeNBT()));
        compound.putLong("lastChangeTime", this.lastChangeTime);
        return compound;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemStackHandler.invalidate();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
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

    public void markNetworkDirty() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
        }
    }

}

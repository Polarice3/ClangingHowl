package com.mongoose.clanginghowl.common.blocks.entities;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ChargingStationBlockEntity extends BlockEntity {
    public long lastChangeTime;
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
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
            };

    public ChargingStationBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CHBlockEntities.STATIONARY_CHARGING_STATION.get(), blockPos, blockState);
    }

    public ChargingStationBlockEntity(BlockEntityType<?> blockEntity, BlockPos blockPos, BlockState blockState){
        super(blockEntity, blockPos, blockState);
    }

    public void tick() {
        if (this.level != null) {
            IItemHandler handler = this.itemStackHandler;
            ItemStack tool = handler.getStackInSlot(0);
            if (!tool.isEmpty() && tool.getItem() instanceof IEnergyItem && !IEnergyItem.isFull(tool)) {
                if (!this.level.isClientSide && this.level.getGameTime() % 20 == 0) {
                    IEnergyItem.powerItem(tool, 4);
                    this.setChanged();
                    this.markNetworkDirty();
                }
                if (this.level instanceof ServerLevel serverLevel) {
                    if (serverLevel.getGameTime() % 5 == 0) {
                        for (int i = 0; i < 8; ++i) {
                            Vec3 vec3 = this.getBlockPos().getCenter().offsetRandom(serverLevel.getRandom(), 0.5F);
                            serverLevel.sendParticles(CHParticleTypes.ENERGY_PARTICLE.get(), vec3.x, vec3.y + 0.5F, vec3.z, 1, 0, 0, 0, 0.0F);
                        }
                    }
                }
            }
        }
    }



    @Override
    public void loadAdditional(CompoundTag compound, net.minecraft.core.HolderLookup.Provider registries) {
        this.readNetwork(compound, registries);
        super.loadAdditional(compound, registries);
    }

    @Override
    public void saveAdditional(CompoundTag compound, net.minecraft.core.HolderLookup.Provider registries) {
        this.writeNetwork(compound, registries);
        super.saveAdditional(compound, registries);
    }

    public void readNetwork(CompoundTag compound, net.minecraft.core.HolderLookup.Provider registries) {
        this.itemStackHandler.deserializeNBT(registries, compound.getCompound("inventory"));
        this.lastChangeTime = compound.getLong("lastChangeTime");
    }

    public CompoundTag writeNetwork(CompoundTag compound, net.minecraft.core.HolderLookup.Provider registries) {
        compound.put("inventory", this.itemStackHandler.serializeNBT(registries));
        compound.putLong("lastChangeTime", this.lastChangeTime);
        return compound;
    }



    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries) {
        return this.writeNetwork(super.getUpdateTag(registries), registries);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, net.minecraft.core.HolderLookup.Provider registries) {
        this.readNetwork(pkt.getTag(), registries);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.readNetwork(tag, registries);
    }

    public void markNetworkDirty() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
        }
    }

}

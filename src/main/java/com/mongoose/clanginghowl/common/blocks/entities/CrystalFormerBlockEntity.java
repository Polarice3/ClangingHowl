package com.mongoose.clanginghowl.common.blocks.entities;

import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.common.blocks.CrystalFormerBlock;
import com.mongoose.clanginghowl.common.blocks.ExEnergyClusterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class CrystalFormerBlockEntity extends BlockEntity {
    public int tickTime;

    public CrystalFormerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(CHBlockEntities.CRYSTAL_FORMER.get(), p_155229_, p_155230_);
    }

    public void tick() {
        if (this.level != null) {
            if (!this.level.isClientSide) {
                if (this.getBlockState().getValue(CrystalFormerBlock.ENABLED)) {
                    for (Entity entity : this.level.getEntitiesOfClass(Entity.class, new AABB(this.worldPosition).inflate(2.0D))) {
                        if (!entity.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                                && entity.canFreeze()
                                && entity.isAttackable()
                                && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)) {
                            int i = entity.getTicksFrozen();
                            int j = 4;
                            entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze() + 5, i + j));
                        }
                    }
                    BlockState above = this.level.getBlockState(this.worldPosition.above());
                    float temperature = this.level.getBiome(this.worldPosition).get().getBaseTemperature();
                    int age = -1;
                    if (above.hasProperty(ExEnergyClusterBlock.AGE)) {
                        age = above.getValue(ExEnergyClusterBlock.AGE);
                    }
                    if ((above.canBeReplaced() && temperature <= 1.5F) || (above.is(CHBlocks.EXTRATERRESTRIAL_ENERGY_CLUSTER.get()) && canGrow(age, temperature))) {
                        if (this.tickTime < 5000) {
                            ++this.tickTime;
                        }
                    } else {
                        this.tickTime = 0;
                    }
                    if (this.tickTime >= 2000) {
                        if (above.is(CHBlocks.EXTRATERRESTRIAL_ENERGY_CLUSTER.get()) && canGrow(age, temperature)) {
                            ExEnergyClusterBlock block = (ExEnergyClusterBlock) above.getBlock();
                            block.growCrystal(above, this.level, this.worldPosition.above());
                            this.tickTime = 0;
                        } else if (above.canBeReplaced() && temperature <= 1.5F) {
                            if (this.tickTime >= 4000) {
                                this.level.setBlock(this.worldPosition.above(), CHBlocks.EXTRATERRESTRIAL_ENERGY_CLUSTER.get().defaultBlockState(), 3);
                                this.tickTime = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean canGrow(int age, float temperature) {
        boolean flag = false;
        if (age == 0) {
            if (temperature <= 1.4F) {
                flag = true;
            }
        } else if (age == 1) {
            if (temperature <= 0.9F) {
                flag = true;
            }
        } else if (age == 2) {
            if (temperature <= 0.4F) {
                flag = true;
            }
        }
        return flag;
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
        this.tickTime = tag.getInt("GrowTick");
    }

    public CompoundTag writeNetwork(CompoundTag tag) {
        tag.putInt("GrowTick", this.tickTime);
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

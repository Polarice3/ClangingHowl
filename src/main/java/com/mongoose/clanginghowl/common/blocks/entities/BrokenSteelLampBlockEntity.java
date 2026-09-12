package com.mongoose.clanginghowl.common.blocks.entities;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.blocks.BrokenSteelLampBlock;
import com.mongoose.clanginghowl.common.blocks.FlameSpewerBlock;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BrokenSteelLampBlockEntity extends BlockEntity {
    public int tickTime;

    public BrokenSteelLampBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(CHBlockEntities.BROKEN_STEEL_LAMP.get(), p_155229_, p_155230_);
    }

    public void tick() {
        if (this.level != null) {
            ++this.tickTime;
            boolean flag = true;
            if (this.tickTime > 100) {
                flag = false;
                if (this.tickTime > 102) {
                    flag = true;
                    if (this.tickTime > 104) {
                        flag = false;
                        if (this.tickTime > 106) {
                            this.tickTime = 0;
                        }
                    }
                }
            }
            if (this.tickTime == 100) {
                this.level.playSound(null, this.getBlockPos(), CHSounds.MALFUNCTIONING_ELECTRICS.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
            }
            if (this.level instanceof ServerLevel serverLevel) {
                if (!flag) {
                    int range = 18;
                    double initialVelocity = ((double) range / 10) * 0.5D;
                    Direction facing = this.getBlockState().getValue(FlameSpewerBlock.FACING);
                    double velocity = initialVelocity + this.level.getRandom().nextDouble() * initialVelocity;
                    double angle = 0.5D;
                    Vec3 randomVec = new Vec3(this.level.getRandom().nextDouble() * 2.0D * angle - angle, this.level.getRandom().nextDouble() * 2.0D * angle - angle, this.level.getRandom().nextDouble() * 2.0D * angle - angle).normalize();
                    Vec3 direction = new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ());
                    Vec3 result = (direction.normalize().scale(1.0D).add(randomVec)).normalize().scale(velocity);
                    Vec3 initial = this.getBlockPos().getCenter().add(facing.getStepX() * 0.05F, facing.getStepY() * 0.05F, facing.getStepZ() * 0.05F).offsetRandom(this.level.getRandom(), 0.25F);
                    ParticleUtil.sendAlwaysVisibleParticles(serverLevel, CHParticleTypes.ELECTRIC_SPARK.get(), initial.x, initial.y, initial.z, 0, result.x, result.y, result.z, 1.0F);
                }
            }
            this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(BrokenSteelLampBlock.ENABLED, flag), 3);
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
        this.tickTime = tag.getInt("TickTime");
    }

    public CompoundTag writeNetwork(CompoundTag tag) {
        tag.putInt("TickTime", this.tickTime);
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

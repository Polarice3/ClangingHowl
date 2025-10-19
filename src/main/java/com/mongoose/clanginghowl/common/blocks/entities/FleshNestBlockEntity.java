package com.mongoose.clanginghowl.common.blocks.entities;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.entities.hostiles.HeartOfDecay;
import com.mongoose.clanginghowl.init.CHSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.List;

public class FleshNestBlockEntity extends BlockEntity {
    public int tickTime;

    public FleshNestBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(CHBlockEntities.TECHNOFLESH_NEST.get(), p_155229_, p_155230_);
    }

    public void tick() {
        if (this.level != null) {
            if (this.level instanceof ServerLevel serverLevel) {
                if (this.level.getDifficulty() != Difficulty.PEACEFUL) {
                    int speed = 1;
                    if (this.isNearPlayer(serverLevel, this.worldPosition)) {
                        speed += 1;
                    }
                    this.tickTime += speed;
                    if (this.tickTime >= 1500) {
                        List<Direction> directions = new ArrayList<>();
                        for (Direction direction : Direction.values()) {
                            BlockPos blockPos = this.worldPosition.relative(direction);
                            if (serverLevel.noCollision(CHEntityType.HEART_OF_DECAY.get().getAABB(blockPos.getX() + 0.5F, blockPos.getY(), blockPos.getZ() + 0.5F))) {
                                directions.add(direction);
                            }
                        }
                        if (!directions.isEmpty()) {
                            Direction direction = Util.getRandom(directions, serverLevel.getRandom());
                            BlockPos blockPos = this.worldPosition.relative(direction);
                            HeartOfDecay hod = new HeartOfDecay(CHEntityType.HEART_OF_DECAY.get(), this.level);
                            hod.setPos(blockPos.getCenter());
                            for(int i = 0; i < 20; ++i) {
                                double d0 = serverLevel.getRandom().nextGaussian() * 0.02D;
                                double d1 = serverLevel.getRandom().nextGaussian() * 0.02D;
                                double d2 = serverLevel.getRandom().nextGaussian() * 0.02D;
                                serverLevel.sendParticles(CHParticleTypes.CRIMSON_POOF.get(), hod.getRandomX(1.0D), hod.getRandomY(), hod.getRandomZ(1.0D), 0, d0, d1, d2, 1.0D);
                            }
                            ForgeEventFactory.onFinalizeSpawn(hod, serverLevel, serverLevel.getCurrentDifficultyAt(this.worldPosition), MobSpawnType.SPAWNER, null, null);
                            if (serverLevel.addFreshEntity(hod)) {
                                serverLevel.playSound(null, hod.getX(), hod.getY(), hod.getZ(), CHSounds.FLESH_TEAR.get(), hod.getSoundSource(), 1.0F, 1.0F);
                            }
                        }
                        this.tickTime = 0;
                    }
                }
            }
        }
    }

    private boolean isNearPlayer(Level p_151344_, BlockPos p_151345_) {
        return p_151344_.hasNearbyAlivePlayer((double)p_151345_.getX() + 0.5D, (double)p_151345_.getY() + 0.5D, (double)p_151345_.getZ() + 0.5D, 16.0D);
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
        this.tickTime = tag.getInt("SpawnTick");
    }

    public CompoundTag writeNetwork(CompoundTag tag) {
        tag.putInt("SpawnTick", this.tickTime);
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

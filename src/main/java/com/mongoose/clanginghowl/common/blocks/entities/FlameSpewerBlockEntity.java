package com.mongoose.clanginghowl.common.blocks.entities;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.blocks.FlameSpewerBlock;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.CHBlockUtil;
import com.mongoose.clanginghowl.utils.CHDamageSource;
import com.mongoose.clanginghowl.utils.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FlameSpewerBlockEntity extends BlockEntity {
    public int tickTime;

    public FlameSpewerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(CHBlockEntities.FLAME_SPEWER.get(), p_155229_, p_155230_);
    }

    public void tick() {
        if (this.level != null) {
            if (this.level instanceof ServerLevel serverLevel) {
                if (this.getBlockState().getValue(FlameSpewerBlock.ENABLED)) {
                    if (this.tickTime == 0) {
                        this.level.playSound(null, this.getBlockPos(), CHSounds.FLAMETHROWER_BURNS.get(), SoundSource.BLOCKS, 0.2F, 1.0F);
                    }
                    ++this.tickTime;
                    int range = 6;
                    double initialVelocity = ((double) range / 10) * 0.5D;
                    Direction facing = this.getBlockState().getValue(FlameSpewerBlock.FACING);
                    BlockPos facingPos = this.getBlockPos().relative(facing);
                    if (this.level.getBlockState(facingPos).getCollisionShape(this.level, facingPos).isEmpty()) {
                        for (int i = 0; i < 6; i++) {
                            double velocity = initialVelocity + this.level.getRandom().nextDouble() * initialVelocity;
                            double angle = 0.5D;
                            Vec3 randomVec = new Vec3(this.level.getRandom().nextDouble() * 2.0D * angle - angle, this.level.getRandom().nextDouble() * 2.0D * angle - angle, this.level.getRandom().nextDouble() * 2.0D * angle - angle).normalize();
                            Vec3 direction = new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ());
                            Vec3 result = (direction.normalize().scale(5.0D).add(randomVec)).normalize().scale(velocity);
                            Vec3 initial = this.getBlockPos().getCenter().add(facing.getStepX() / 2.0F, facing.getStepY() / 2.0F, facing.getStepZ() / 2.0F).offsetRandom(this.level.getRandom(), 0.5F);
                            ParticleUtil.sendAlwaysVisibleParticles(serverLevel, CHParticleTypes.FLAMETHROWER_FLAME.get(), initial.x, initial.y, initial.z, 0, result.x, result.y, result.z, 1.0F);
                        }
                    }

                    List<Entity> entitiesInRange = this.level.getEntitiesOfClass(Entity.class, this.getAABB(range), entity -> !(entity instanceof ItemEntity));
                    for (Entity entity : entitiesInRange) {
                        int distance = this.getDistance(entity, facing);
                        if (CHBlockUtil.emptySpaceBetween(this.level, this.getBlockPos().relative(facing), Math.min(range, distance), facing)) {
                            if (!entity.fireImmune()) {
                                entity.hurt(CHDamageSource.getDamageSource(this.level, CHDamageSource.FIRE_STREAM), 3.5F);
                                entity.igniteForSeconds(15);
                            }
                        }
                    }
                    if (this.tickTime % 20 == 0) {
                        this.level.playSound(null, this.getBlockPos(), CHSounds.FLAMETHROWER_BURNS.get(), SoundSource.BLOCKS, 0.2F, 1.0F);
                    }
                } else {
                    if (this.tickTime > 0) {
                        this.tickTime = 0;
                    }
                }
            }
        }
    }

    private int getDistance(Entity entity, Direction facing) {
        int distance = 1;
        if (facing == Direction.UP) {
            distance = Math.max((int) (entity.getY() - this.getBlockPos().getY()), 1);
        } else if (facing == Direction.DOWN) {
            distance = Math.max((int) (this.getBlockPos().getY() - (entity.getY() + 1)), 1);
        } else if (facing == Direction.EAST) {
            distance = Math.max((int) (entity.getX() - this.getBlockPos().getX()), 1);
        } else if (facing == Direction.WEST) {
            distance = Math.max((int) (this.getBlockPos().getX() - entity.getX()), 1);
        } else if (facing == Direction.SOUTH) {
            distance = Math.max((int) (entity.getZ() - this.getBlockPos().getZ()), 1);
        } else if (facing == Direction.NORTH) {
            distance = Math.max((int) (this.getBlockPos().getZ() - entity.getZ()), 1);
        }
        return distance;
    }

    public AABB getAABB(int range) {
        if (this.level != null) {
            BlockState state = this.level.getBlockState(getBlockPos());
            if (!(state.getBlock() instanceof FlameSpewerBlock)) {
                return new AABB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
            }
            Direction facing = state.getValue(FlameSpewerBlock.FACING);

            BlockPos blockPos2 = this.getBlockPos().relative(facing, range);
            switch (facing) {
                case UP, EAST, SOUTH -> blockPos2 = blockPos2.offset(1, 1, 1);
                case DOWN -> blockPos2 = blockPos2.offset(1, 0, 1);
                case WEST -> blockPos2 = blockPos2.offset(0, 1, 1);
                case NORTH -> blockPos2 = blockPos2.offset(1, 1, 0);
            }
            return AABB.encapsulatingFullBlocks(this.getBlockPos(), blockPos2);
        } else {
            return new AABB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
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

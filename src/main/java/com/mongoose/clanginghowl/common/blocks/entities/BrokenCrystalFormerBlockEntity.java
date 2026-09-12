package com.mongoose.clanginghowl.common.blocks.entities;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.blocks.BrokenCrystalFormerBlock;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.common.blocks.CryoFrostBlock;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.init.CHTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BrokenCrystalFormerBlockEntity extends BlockEntity {
    public int tickTime;

    public BrokenCrystalFormerBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(CHBlockEntities.BROKEN_CRYSTAL_FORMER.get(), p_155229_, p_155230_);
    }

    public void tick() {
        if (this.level != null) {
            ++this.tickTime;
            boolean flag = true;
            if (this.tickTime > 80) {
                flag = false;
                if (this.tickTime > 82) {
                    flag = true;
                    if (this.tickTime > 84) {
                        flag = false;
                        if (this.tickTime > 86) {
                            this.tickTime = 0;
                        }
                    }
                }
            }
            if (this.tickTime == 80) {
                this.level.playSound(null, this.getBlockPos(), CHSounds.MALFUNCTIONING_ELECTRICS.get(), SoundSource.BLOCKS, 0.75F, 1.0F);
            }
            if (!this.level.isClientSide) {
                for (Entity entity : this.level.getEntitiesOfClass(Entity.class, new AABB(this.worldPosition).inflate(4.0D))) {
                    if (!entity.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                            && entity.canFreeze()
                            && entity.isAttackable()
                            && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)) {
                        int i = entity.getTicksFrozen();
                        int j = 4;
                        entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze() + 5, i + j));
                    }
                }
                if (this.level instanceof ServerLevel serverLevel) {
                    if (!flag) {
                        for (int i = 0; i < 4; ++i) {
                            Vec3 vec3 = this.getBlockPos().getCenter();
                            double d0 = vec3.x + ((this.level.getRandom().nextDouble() * this.level.getRandom().nextIntBetweenInclusive(-1, 1)) * 0.5F);
                            double d1 = vec3.y + 0.5D;
                            double d2 = vec3.z + ((this.level.getRandom().nextDouble() * this.level.getRandom().nextIntBetweenInclusive(-1, 1)) * 0.5F);
                            double ySpeed = (this.level.getRandom().nextFloat() * 0.4F + 0.05F) * 2.0D;
                            serverLevel.sendParticles(CHParticleTypes.ELECTRIC_SPARK.get(), d0, d1, d2, 0, 0.0F, ySpeed, 0.0F, 1.0D);
                        }
                    }
                }
            }
            if (this.level.getRandom().nextFloat() <= 0.25F && this.level.getGameTime() % 100 == 0) {
                for (Direction direction : Direction.values()) {
                    BlockPos blockPos = this.getBlockPos().relative(direction);
                    if (this.level.getFluidState(blockPos).isSourceOfType(Fluids.WATER)) {
                        this.level.setBlock(blockPos, Blocks.ICE.defaultBlockState(), 2);
                    }
                }

                for (int h = 0; h < 16; ++h) {
                    int i = this.level.getRandom().nextIntBetweenInclusive(-4, 4);
                    int j = this.level.getRandom().nextIntBetweenInclusive(-4, 4);
                    int k = this.level.getRandom().nextIntBetweenInclusive(-4, 4);
                    BlockPos blockPos = this.getBlockPos().offset(i, j, k);
                    BlockState blockState = this.level.getBlockState(blockPos);
                    CryoFrostBlock frostBlock = (CryoFrostBlock) CHBlocks.CRYOGENIC_FROST.get();

                    if (!blockState.is(CHTags.Blocks.CANNOT_FROST)) {
                        for (Direction direction : Direction.values()) {
                            BlockPos blockPos1 = blockPos.relative(direction);
                            BlockState blockState1 = this.level.getBlockState(blockPos1);

                            if (!blockState1.is(CHTags.Blocks.CANNOT_FROST)) {
                                if (blockState.canBeReplaced() || blockState.isAir() || blockState.is(CHBlocks.CRYOGENIC_FROST.get())) {
                                    BlockState newState = frostBlock.getStateForPlacement(blockState, this.level, blockPos, direction);
                                    if (newState != null) {
                                        this.level.setBlockAndUpdate(blockPos, newState);
                                        this.level.gameEvent(null, GameEvent.BLOCK_CHANGE, blockPos);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(BrokenCrystalFormerBlock.ENABLED, flag), 3);
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

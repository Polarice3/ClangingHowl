package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.common.blocks.entities.FleshNestBlockEntity;
import com.mongoose.clanginghowl.common.blocks.entities.NerveEndingsBlockEntity;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.init.CHTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import javax.annotation.Nullable;

public class NerveEndingsBlock extends BushBlock implements EntityBlock {
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public NerveEndingsBlock() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .strength(0.5F)
                .forceSolidOn()
                .noCollission()
                .ignitedByLava()
                .sound(SoundType.HONEY_BLOCK)
                .pushReaction(PushReaction.DESTROY));
        this.registerDefaultState(this.defaultBlockState().setValue(TRIGGERED, Boolean.FALSE));
    }

    @Override
    protected boolean mayPlaceOn(BlockState p_51042_, BlockGetter p_51043_, BlockPos p_51044_) {
        return Block.isFaceFull(p_51042_.getCollisionShape(p_51043_, p_51044_.below()), Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(TRIGGERED);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_55659_) {
        return this.defaultBlockState().setValue(TRIGGERED, false);
    }

    @Override
    public void entityInside(BlockState p_60495_, Level p_60496_, BlockPos p_60497_, Entity p_60498_) {
        if (p_60496_ instanceof ServerLevel serverLevel) {
            if (!p_60495_.getValue(TRIGGERED)) {
                if (!p_60498_.getType().is(CHTags.EntityTypes.TECHNO_FLESH)) {
                    serverLevel.setBlock(p_60497_, p_60495_.setValue(TRIGGERED, true), 3);
                    serverLevel.playSound(null, p_60497_.getX(), p_60497_.getY(), p_60497_.getZ(), CHSounds.FLESH_TEAR.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    int range = 10;
                    for (int i = -range; i < range; ++i) {
                        for (int j = -range; j < range; ++j) {
                            for (int k = -range; k < range; ++k) {
                                BlockPos blockPos = p_60497_.offset(i, j, k);
                                if (serverLevel.getBlockEntity(blockPos) instanceof FleshNestBlockEntity nestBlock) {
                                    nestBlock.spawn(serverLevel);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new NerveEndingsBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof NerveEndingsBlockEntity block) {
                block.tick();
            }
        };
    }
}

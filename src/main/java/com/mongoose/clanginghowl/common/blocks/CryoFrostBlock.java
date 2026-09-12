package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.init.CHTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class CryoFrostBlock extends MultifaceBlock implements SimpleWaterloggedBlock {
    public static final com.mojang.serialization.MapCodec<CryoFrostBlock> CODEC = simpleCodec(CryoFrostBlock::new);

    @Override
    public com.mojang.serialization.MapCodec<CryoFrostBlock> codec() { return CODEC; }

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public CryoFrostBlock() {
        this(BlockBehaviour.Properties.of()
                .mapColor(MapColor.SNOW)
                .replaceable()
                .forceSolidOff()
                .randomTicks()
                .strength(0.1F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.SNOW)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY));
    }

    public CryoFrostBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    public void randomTick(BlockState p_221355_, ServerLevel p_221356_, BlockPos p_221357_, RandomSource p_221358_) {
        if (p_221356_.getBrightness(LightLayer.BLOCK, p_221357_) > 11 - p_221355_.getLightBlock(p_221356_, p_221357_)) {
            this.melt(p_221356_, p_221357_);
        }

    }

    protected void melt(Level p_54170_, BlockPos p_54171_) {
        p_54170_.removeBlock(p_54171_, false);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_153309_) {
        super.createBlockStateDefinition(p_153309_);
        p_153309_.add(WATERLOGGED);
    }

    public BlockState updateShape(BlockState p_153302_, Direction p_153303_, BlockState p_153304_, LevelAccessor p_153305_, BlockPos p_153306_, BlockPos p_153307_) {
        if (p_153302_.getValue(WATERLOGGED)) {
            p_153305_.scheduleTick(p_153306_, Fluids.WATER, Fluids.WATER.getTickDelay(p_153305_));
        }

        return super.updateShape(p_153302_, p_153303_, p_153304_, p_153305_, p_153306_, p_153307_);
    }

    public boolean canBeReplaced(BlockState p_153299_, BlockPlaceContext p_153300_) {
        return !p_153300_.getItemInHand().is(this.asItem()) || super.canBeReplaced(p_153299_, p_153300_);
    }

    public FluidState getFluidState(BlockState p_153311_) {
        return p_153311_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_153311_);
    }

    public boolean propagatesSkylightDown(BlockState p_181225_, BlockGetter p_181226_, BlockPos p_181227_) {
        return p_181225_.getFluidState().isEmpty();
    }

    public MultifaceSpreader getSpreader() {
        return this.spreader;
    }

    public boolean canSurvive(BlockState p_153888_, LevelReader p_153889_, BlockPos p_153890_) {
        boolean flag = false;

        for (Direction direction : DIRECTIONS) {
            if (hasFace(p_153888_, direction)) {
                BlockPos blockpos = p_153890_.relative(direction);
                if (p_153889_.getBlockState(blockpos).is(CHTags.Blocks.CANNOT_FROST)) {
                    return false;
                }

                flag = true;
            }
        }

        return flag;
    }

    public boolean isValidStateForPlacement(BlockGetter p_221572_, BlockState p_221573_, BlockPos p_221574_, Direction p_221575_) {
        if (p_221573_.is(CHTags.Blocks.CANNOT_FROST)) {
            return false;
        } else {
            return super.isValidStateForPlacement(p_221572_, p_221573_, p_221574_, p_221575_);
        }
    }
}

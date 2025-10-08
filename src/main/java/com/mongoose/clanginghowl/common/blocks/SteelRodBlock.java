package com.mongoose.clanginghowl.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SteelRodBlock extends RodBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final float AABB_MIN = 7.0F;
    protected static final float AABB_MAX = 9.0F;
    protected static final VoxelShape Y_AXIS_AABB = Block.box(AABB_MIN, 0.0D, AABB_MIN, AABB_MAX, 16.0D, AABB_MAX);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(AABB_MIN, AABB_MIN, 0.0D, AABB_MAX, AABB_MAX, 16.0D);
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0D, AABB_MIN, AABB_MIN, 16.0D, AABB_MAX, AABB_MAX);

    public SteelRodBlock(BlockBehaviour.Properties p_53085_) {
        super(p_53085_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, Boolean.FALSE));
    }

    public VoxelShape getShape(BlockState p_154346_, BlockGetter p_154347_, BlockPos p_154348_, CollisionContext p_154349_) {
        return switch (p_154346_.getValue(FACING).getAxis()) {
            default -> X_AXIS_AABB;
            case Z -> Z_AXIS_AABB;
            case Y -> Y_AXIS_AABB;
        };
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_153711_) {
        FluidState fluidstate = p_153711_.getLevel().getFluidState(p_153711_.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(FACING, p_153711_.getClickedFace()).setValue(WATERLOGGED, Boolean.valueOf(flag));
    }

    public BlockState updateShape(BlockState p_153739_, Direction p_153740_, BlockState p_153741_, LevelAccessor p_153742_, BlockPos p_153743_, BlockPos p_153744_) {
        if (p_153739_.getValue(WATERLOGGED)) {
            p_153742_.scheduleTick(p_153743_, Fluids.WATER, Fluids.WATER.getTickDelay(p_153742_));
        }

        return super.updateShape(p_153739_, p_153740_, p_153741_, p_153742_, p_153743_, p_153744_);
    }

    public FluidState getFluidState(BlockState p_153759_) {
        return p_153759_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_153759_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53105_) {
        p_53105_.add(FACING, WATERLOGGED);
    }
}

package com.mongoose.clanginghowl.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ExPebbleBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D);

    public ExPebbleBlock() {
        super(Properties.of()
                .mapColor(MapColor.DEEPSLATE)
                .strength(1.0F, 5.0F)
                .noOcclusion()
                .sound(SoundType.DEEPSLATE));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.FALSE));
    }

    public RenderShape getRenderShape(BlockState p_53840_) {
        return RenderShape.MODEL;
    }

    public boolean useShapeForLightOcclusion(BlockState p_52997_) {
        return true;
    }

    public VoxelShape getShape(BlockState p_52988_, BlockGetter p_52989_, BlockPos p_52990_, CollisionContext p_52991_) {
        return SHAPE;
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_48781_) {
        FluidState fluidstate = p_48781_.getLevel().getFluidState(p_48781_.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(FACING, p_48781_.getHorizontalDirection().getClockWise()).setValue(WATERLOGGED, flag);
    }

    public FluidState getFluidState(BlockState p_153759_) {
        return p_153759_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_153759_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53838_) {
        p_53838_.add(FACING, WATERLOGGED);
    }

    public void tick(BlockState p_220908_, ServerLevel p_220909_, BlockPos p_220910_, RandomSource p_220911_) {
        if (!p_220909_.isLoaded(p_220910_)) {
            return;
        }
        if (!p_220908_.canSurvive(p_220909_, p_220910_)) {
            p_220909_.destroyBlock(p_220910_, true);
        }

    }

    public boolean canSurvive(BlockState p_57499_, LevelReader p_57500_, BlockPos p_57501_) {
        BlockState blockstate = p_57500_.getBlockState(p_57501_.below());
        return blockstate.isFaceSturdy(p_57500_, p_57501_.below(), Direction.UP);
    }

    public BlockState updateShape(BlockState p_51157_, Direction p_51158_, BlockState p_51159_, LevelAccessor p_51160_, BlockPos p_51161_, BlockPos p_51162_) {
        if (!p_51157_.canSurvive(p_51160_, p_51161_)) {
            p_51160_.scheduleTick(p_51161_, this, 1);
        }
        if (p_51157_.getValue(WATERLOGGED)) {
            p_51160_.scheduleTick(p_51161_, Fluids.WATER, Fluids.WATER.getTickDelay(p_51160_));
        }

        return super.updateShape(p_51157_, p_51158_, p_51159_, p_51160_, p_51161_, p_51162_);
    }
}

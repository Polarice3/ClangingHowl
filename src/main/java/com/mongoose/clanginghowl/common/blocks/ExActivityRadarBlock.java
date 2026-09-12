package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.common.blocks.entities.ExActivityRadarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ExActivityRadarBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
    public static final com.mojang.serialization.MapCodec<ExActivityRadarBlock> CODEC = simpleCodec(ExActivityRadarBlock::new);

    @Override
    public com.mojang.serialization.MapCodec<ExActivityRadarBlock> codec() { return CODEC; }

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);

    public ExActivityRadarBlock() {
        this(Properties.of()
                .strength(4.0F, 9.0F)
                .sound(SoundType.COPPER)
                .noOcclusion());
    }

    public ExActivityRadarBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    public RenderShape getRenderShape(BlockState p_53840_) {
        return RenderShape.MODEL;
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_48781_) {
        FluidState fluidstate = p_48781_.getLevel().getFluidState(p_48781_.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(FACING, p_48781_.getHorizontalDirection()).setValue(WATERLOGGED, flag);
    }

    public FluidState getFluidState(BlockState p_153759_) {
        return p_153759_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_153759_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53838_) {
        p_53838_.add(FACING, POWERED, WATERLOGGED);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && state.getBlock() != newState.getBlock()) {
            if (state.getValue(POWERED)) {
                worldIn.updateNeighborsAt(pos, this);
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    public BlockState updateShape(BlockState p_51157_, Direction p_51158_, BlockState p_51159_, LevelAccessor p_51160_, BlockPos p_51161_, BlockPos p_51162_) {
        if (p_51157_.getValue(WATERLOGGED)) {
            p_51160_.scheduleTick(p_51161_, Fluids.WATER, Fluids.WATER.getTickDelay(p_51160_));
        }
        if (p_51160_ instanceof Level level) {
            level.updateNeighborsAt(p_51161_, this);
        }
        return super.updateShape(p_51157_, p_51158_, p_51159_, p_51160_, p_51161_, p_51162_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new ExActivityRadarBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152755_, BlockState p_152756_, BlockEntityType<T> p_152757_) {
        return (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof ExActivityRadarBlockEntity block) {
                block.tick();
            }
        };
    }
}

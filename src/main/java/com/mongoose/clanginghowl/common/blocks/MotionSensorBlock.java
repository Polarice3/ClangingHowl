package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.common.blocks.entities.MotionSensorBlockEntity;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class MotionSensorBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final com.mojang.serialization.MapCodec<MotionSensorBlock> CODEC = simpleCodec(MotionSensorBlock::new);

    @Override
    public com.mojang.serialization.MapCodec<MotionSensorBlock> codec() { return CODEC; }

    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final VoxelShape UP_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 1.0D, 11.0D);
    protected static final VoxelShape DOWN_SHAPE = Block.box(5.0D, 15.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    protected static final VoxelShape NORTH_SHAPE = Block.box(5.0D, 5.0D, 15.0D, 11.0D, 11.0D, 16.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(5.0D, 5.0D, 0.0D, 11.0D, 11.0D, 1.0D);
    protected static final VoxelShape WEST_SHAPE = Block.box(15.0D, 5.0D, 5.0D, 16.0D, 11.0D, 11.0D);
    protected static final VoxelShape EAST_SHAPE = Block.box(0.0D, 5.0D, 5.0D, 1.0D, 11.0D, 11.0D);

    public MotionSensorBlock() {
        this(Properties.of()
                .strength(1.0F, 1.0F)
                .sound(SoundType.COPPER)
                .noOcclusion());
    }

    public MotionSensorBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWER, 0).setValue(POWERED, false).setValue(WATERLOGGED, false));
    }

    public VoxelShape getShape(BlockState p_152021_, BlockGetter p_152022_, BlockPos p_152023_, CollisionContext p_152024_) {
        Direction direction = p_152021_.getValue(FACING);
        switch (direction) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case WEST:
                return WEST_SHAPE;
            case DOWN:
                return DOWN_SHAPE;
            case UP:
            default:
                return UP_SHAPE;
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return state.getValue(POWER);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return state.getValue(POWER);
    }

    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && state.getBlock() != newState.getBlock()) {
            if (state.getValue(POWER) > 0) {
                this.updateRedstoneNeighbors(worldIn, pos);
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        this.updateRedstoneNeighbors((Level) worldIn, currentPos);
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public void updateRedstoneNeighbors(Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(Direction.NORTH), this);
        level.updateNeighborsAt(pos.relative(Direction.SOUTH), this);
        level.updateNeighborsAt(pos.relative(Direction.WEST), this);
        level.updateNeighborsAt(pos.relative(Direction.EAST), this);
        level.updateNeighborsAt(pos.relative(Direction.DOWN), this);
        level.updateNeighborsAt(pos.relative(Direction.UP), this);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        boolean waterlogged = fluidState.getType() == Fluids.WATER;
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace())
                .setValue(WATERLOGGED, waterlogged);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55673_) {
        p_55673_.add(WATERLOGGED, POWER, POWERED, FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new MotionSensorBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152755_, BlockState p_152756_, BlockEntityType<T> p_152757_) {
        return (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof MotionSensorBlockEntity block) {
                block.tick();
            }
        };
    }
}

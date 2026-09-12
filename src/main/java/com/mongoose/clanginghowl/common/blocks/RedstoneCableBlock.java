package com.mongoose.clanginghowl.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class RedstoneCableBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final float AABB_MIN = 5.0F;
    protected static final float AABB_MAX = 11.0F;
    protected static final VoxelShape Y_AXIS_AABB = Block.box(AABB_MIN, 0.0D, AABB_MIN, AABB_MAX, 16.0D, AABB_MAX);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(AABB_MIN, AABB_MIN, 0.0D, AABB_MAX, AABB_MAX, 16.0D);
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0D, AABB_MIN, AABB_MIN, 16.0D, AABB_MAX, AABB_MAX);

    public RedstoneCableBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .strength(0.5F)
                .noOcclusion()
                .sound(SoundType.WOOL));
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y).setValue(POWER, 0).setValue(POWERED, false).setValue(WATERLOGGED, false));
    }

    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return true;
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter level, BlockPos blockPos, Direction direction) {
        if (direction.getAxis() != blockState.getValue(AXIS)) {
            return 0;
        }
        return blockState.getValue(POWER);
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter level, BlockPos blockPos, Direction direction) {
        return getSignal(blockState, level, blockPos, direction);
    }

    private int getExternalPower(Level level, BlockPos blockPos, Direction.Axis axis) {
        int power = 0;
        for (Direction direction : Direction.values()) {
            if (direction.getAxis() != axis) {
                continue;
            }

            BlockPos neighborPos = blockPos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);

            if (neighborState.getBlock() instanceof RedstoneCableBlock) {
                continue;
            }

            int signal = neighborState.getSignal(level, neighborPos, direction.getOpposite());
            power = Math.max(power, signal);
        }
        return power;
    }

    private void propagateNetwork(Level level, BlockPos origin) {
        BlockState originState = level.getBlockState(origin);
        if (!(originState.getBlock() instanceof RedstoneCableBlock)) {
            return;
        }

        Map<BlockPos, BlockState> network = new HashMap<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(origin);
        network.put(origin, originState);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            BlockState currentState = network.get(current);
            Direction.Axis axis = currentState.getValue(AXIS);

            for (Direction direction : Direction.values()) {
                if (direction.getAxis() != axis) {
                    continue;
                }
                BlockPos neighborPos = current.relative(direction);
                if (network.containsKey(neighborPos)) {
                    continue;
                }

                BlockState neighborState = level.getBlockState(neighborPos);
                if (neighborState.getBlock() instanceof RedstoneCableBlock
                        && neighborState.getValue(AXIS) == axis) {
                    network.put(neighborPos, neighborState);
                    queue.add(neighborPos);
                }
            }
        }

        Map<BlockPos, Integer> resolvedPower = new HashMap<>();

        for (BlockPos pos : network.keySet()) {
            BlockState state = network.get(pos);
            int external = getExternalPower(level, pos, state.getValue(AXIS));
            if (external > 0) {
                resolvedPower.put(pos, external);
                queue.add(pos);
            } else {
                resolvedPower.put(pos, 0);
            }
        }

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            BlockState currentState = network.get(current);
            if (currentState == null) {
                continue;
            }

            Direction.Axis axis = currentState.getValue(AXIS);
            int currentPower = resolvedPower.get(current);

            for (Direction direction : Direction.values()) {
                if (direction.getAxis() != axis) {
                    continue;
                }
                BlockPos neighborPos = current.relative(direction);
                if (!network.containsKey(neighborPos)) {
                    continue;
                }

                int existingPower = resolvedPower.getOrDefault(neighborPos, 0);
                if (currentPower > existingPower) {
                    resolvedPower.put(neighborPos, currentPower);
                    queue.add(neighborPos);
                }
            }
        }

        for (Map.Entry<BlockPos, Integer> entry : resolvedPower.entrySet()) {
            BlockPos pos = entry.getKey();
            int power = entry.getValue();
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof RedstoneCableBlock && state.getValue(POWER) != power) {
                level.setBlock(pos, state.setValue(POWER, power).setValue(POWERED, power > 0), 3);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            propagateNetwork(level, blockPos);
        }
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            propagateNetwork(level, blockPos);
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (!isMoving && blockState.getBlock() != newState.getBlock()) {
            Direction.Axis axis = blockState.getValue(AXIS);
            for (Direction direction : Direction.values()) {
                if (direction.getAxis() != axis) {
                    continue;
                }
                BlockPos neighborPos = blockPos.relative(direction);
                BlockState neighborState = level.getBlockState(neighborPos);
                if (neighborState.getBlock() instanceof RedstoneCableBlock) {
                    propagateNetwork(level, neighborPos);
                }
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos blockPos, BlockPos neighborPos) {
        if (blockState.getValue(WATERLOGGED)) {
            level.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(blockState, direction, neighborState, level, blockPos, neighborPos);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return switch (blockState.getValue(AXIS)) {
            case X -> X_AXIS_AABB;
            case Z -> Z_AXIS_AABB;
            default -> Y_AXIS_AABB;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        boolean waterlogged = fluidState.getType() == Fluids.WATER;
        return this.defaultBlockState()
                .setValue(AXIS, context.getClickedFace().getAxis())
                .setValue(WATERLOGGED, waterlogged);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, POWER, POWERED, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, PathComputationType type) {
        return false;
    }
}

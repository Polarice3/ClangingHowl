package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ExEnergyClusterBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 7.0D, 12.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 11.0D, 14.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public ExEnergyClusterBlock() {
        super(Properties.copy(Blocks.AMETHYST_CLUSTER)
                .strength(1.5F, 10.0F)
                .sound(SoundType.SMALL_AMETHYST_BUD)
                .mapColor(MapColor.COLOR_LIGHT_BLUE)
                .forceSolidOn()
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .emissiveRendering((i, d, k) -> true)
                .lightLevel((p_187409_) -> (p_187409_.getValue(AGE) * 2) + 1));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(WATERLOGGED, Boolean.FALSE));
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState p_52297_, BlockGetter p_52298_, BlockPos p_52299_, CollisionContext p_52300_) {
        return SHAPE_BY_AGE[this.getAge(p_52297_)];
    }

    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return true;
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        if (state.getValue(AGE) == 0) {
            return SoundType.SMALL_AMETHYST_BUD;
        } else if (state.getValue(AGE) == 1) {
            return SoundType.MEDIUM_AMETHYST_BUD;
        }
        return SoundType.LARGE_AMETHYST_BUD;
    }

    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 2;
    }

    public int getAge(BlockState p_52306_) {
        return p_52306_.getValue(this.getAgeProperty());
    }

    public BlockState getStateForAge(int p_52290_) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), Integer.valueOf(p_52290_));
    }

    public final boolean isMaxAge(BlockState p_52308_) {
        return this.getAge(p_52308_) >= this.getMaxAge();
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

    public boolean isRandomlyTicking(BlockState p_52288_) {
        return false; //temp
    }

    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (!serverLevel.isLoaded(blockPos)) {
            return;
        }
        if (serverLevel.getBlockState(blockPos.below()).is(CHBlocks.EXTRATERRESTRIAL_STONE.get())) {
            int i = this.getAge(state);
            if (i <= this.getMaxAge()) {
                if (randomSource.nextInt(5) == 0) {
                    if (i == this.getMaxAge()) {
                        if (serverLevel.getBlockState(blockPos.above()).canBeReplaced(new DirectionalPlaceContext(serverLevel, blockPos.above(), Direction.DOWN, ItemStack.EMPTY, Direction.UP))) {
                            HugeExEnergyClusterBlock.placeAt(serverLevel, CHBlocks.HUGE_EXTRATERRESTRIAL_ENERGY_CLUSTER.get().defaultBlockState(), blockPos, 3);
                        }
                    } else {
                        serverLevel.setBlock(blockPos, this.getStateForAge(i + 1), 2);
                    }
                }
            }
        }

    }

    public void growCrystal(BlockState state, Level level, BlockPos blockPos) {
        int i = this.getAge(state);
        if (i <= this.getMaxAge()) {
            if (i == this.getMaxAge()) {
                if (level.getBlockState(blockPos.above()).canBeReplaced(new DirectionalPlaceContext(level, blockPos.above(), Direction.DOWN, ItemStack.EMPTY, Direction.UP))) {
                    HugeExEnergyClusterBlock.placeAt(level, CHBlocks.HUGE_EXTRATERRESTRIAL_ENERGY_CLUSTER.get().defaultBlockState(), blockPos, 3);
                }
            } else {
                level.setBlock(blockPos, this.getStateForAge(i + 1), 2);
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        if (state.getValue(AGE) == 1) {
            return new ItemStack(CHBlocks.MEDIUM_EX_ENERGY_CLUSTER.get());
        } else if (state.getValue(AGE) == 2) {
            return new ItemStack(CHBlocks.LARGE_EX_ENERGY_CLUSTER.get());
        }
        return super.getCloneItemStack(state, target, level, pos, player);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_153711_) {
        FluidState fluidstate = p_153711_.getLevel().getFluidState(p_153711_.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, flag);
    }

    public FluidState getFluidState(BlockState p_153759_) {
        return p_153759_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_153759_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_52286_) {
        p_52286_.add(AGE, WATERLOGGED);
    }

    @Override
    public void animateTick(BlockState p_220827_, Level p_220828_, BlockPos p_220829_, RandomSource p_220830_) {
        super.animateTick(p_220827_, p_220828_, p_220829_, p_220830_);
        if (p_220828_.getGameTime() % p_220828_.getRandom().nextIntBetweenInclusive(20, 40) == 0) {
            Direction direction = Direction.getRandom(p_220830_);
            Direction.Axis axis = direction.getAxis();
            double x = axis == Direction.Axis.X ? 0.5 + 0.5625 * (double) direction.getStepX() : (double) p_220830_.nextFloat();
            double y = axis == Direction.Axis.Y ? 0.5 + 0.5625 * (double) direction.getStepY() : (double) p_220830_.nextFloat();
            double z = axis == Direction.Axis.Z ? 0.5 + 0.5625 * (double) direction.getStepZ() : (double) p_220830_.nextFloat();
            p_220828_.addParticle(CHParticleTypes.CRYSTAL_LUSTER.get(), (double) p_220829_.getX() + x, (double) p_220829_.getY() + y, (double) p_220829_.getZ() + z, 0, 0, 0);
        }
    }
}

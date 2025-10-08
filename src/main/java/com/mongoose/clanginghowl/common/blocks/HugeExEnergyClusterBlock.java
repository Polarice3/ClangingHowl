package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class HugeExEnergyClusterBlock extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public HugeExEnergyClusterBlock() {
        super(Properties.copy(Blocks.AMETHYST_CLUSTER)
                .strength(1.5F, 10.0F)
                .sound(SoundType.AMETHYST_CLUSTER)
                .mapColor(MapColor.COLOR_LIGHT_BLUE)
                .forceSolidOn()
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .emissiveRendering((i, d, k) -> true)
                .lightLevel((p_187409_) -> 10));
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        if (p_60555_.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return Block.box(0.0F, 0.0F, 0.0F, 16.0F, 12.0F, 16.0F);
        }
        return super.getShape(p_60555_, p_60556_, p_60557_, p_60558_);
    }

    public BlockState updateShape(BlockState p_52894_, Direction p_52895_, BlockState p_52896_, LevelAccessor p_52897_, BlockPos p_52898_, BlockPos p_52899_) {
        DoubleBlockHalf doubleblockhalf = p_52894_.getValue(HALF);
        if (p_52895_.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (p_52895_ == Direction.UP) || p_52896_.is(this) && p_52896_.getValue(HALF) != doubleblockhalf) {
            return doubleblockhalf == DoubleBlockHalf.LOWER && p_52895_ == Direction.DOWN && !p_52894_.canSurvive(p_52897_, p_52898_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_52894_, p_52895_, p_52896_, p_52897_, p_52898_, p_52899_);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_52863_) {
        BlockPos blockpos = p_52863_.getClickedPos();
        Level level = p_52863_.getLevel();
        return blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(p_52863_) ? super.getStateForPlacement(p_52863_) : null;
    }

    public void setPlacedBy(Level p_52872_, BlockPos p_52873_, BlockState p_52874_, LivingEntity p_52875_, ItemStack p_52876_) {
        BlockPos blockpos = p_52873_.above();
        p_52872_.setBlock(blockpos, copyWaterloggedFrom(p_52872_, blockpos, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER)), 3);
    }

    public boolean canSurvive(BlockState p_52887_, LevelReader p_52888_, BlockPos p_52889_) {
        if (p_52887_.getValue(HALF) != DoubleBlockHalf.UPPER) {
            BlockState blockstate = p_52888_.getBlockState(p_52889_.below());
            return blockstate.isFaceSturdy(p_52888_, p_52889_.below(), Direction.UP);
        } else {
            BlockState blockstate = p_52888_.getBlockState(p_52889_.below());
            if (p_52887_.getBlock() != this) {
                return super.canSurvive(p_52887_, p_52888_, p_52889_);
            }
            return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    public static void placeAt(LevelAccessor p_153174_, BlockState p_153175_, BlockPos p_153176_, int p_153177_) {
        BlockPos blockpos = p_153176_.above();
        p_153174_.setBlock(p_153176_, copyWaterloggedFrom(p_153174_, p_153176_, p_153175_.setValue(HALF, DoubleBlockHalf.LOWER)), p_153177_);
        p_153174_.setBlock(blockpos, copyWaterloggedFrom(p_153174_, blockpos, p_153175_.setValue(HALF, DoubleBlockHalf.UPPER)), p_153177_);
    }

    public static BlockState copyWaterloggedFrom(LevelReader p_182454_, BlockPos p_182455_, BlockState p_182456_) {
        return p_182456_.hasProperty(BlockStateProperties.WATERLOGGED) ? p_182456_.setValue(BlockStateProperties.WATERLOGGED, p_182454_.isWaterAt(p_182455_)) : p_182456_;
    }

    public void playerWillDestroy(Level p_52878_, BlockPos p_52879_, BlockState p_52880_, Player p_52881_) {
        if (!p_52878_.isClientSide) {
            if (p_52881_.isCreative() || EnchantmentHelper.hasSilkTouch(p_52881_.getMainHandItem())) {
                preventCreativeDropFromBottomPart(p_52878_, p_52879_, p_52880_, p_52881_);
                if (!p_52881_.isCreative()) {
                    dropResources(p_52880_, p_52878_, p_52879_, null, p_52881_, p_52881_.getMainHandItem());
                }
            } else {
                dropResources(p_52880_, p_52878_, p_52879_, null, p_52881_, p_52881_.getMainHandItem());
            }
        }

        super.playerWillDestroy(p_52878_, p_52879_, p_52880_, p_52881_);
    }

    public void playerDestroy(Level p_52865_, Player p_52866_, BlockPos p_52867_, BlockState p_52868_, @Nullable BlockEntity p_52869_, ItemStack p_52870_) {
        super.playerDestroy(p_52865_, p_52866_, p_52867_, Blocks.AIR.defaultBlockState(), p_52869_, p_52870_);
    }

    protected static void preventCreativeDropFromBottomPart(Level p_52904_, BlockPos p_52905_, BlockState p_52906_, Player p_52907_) {
        DoubleBlockHalf doubleblockhalf = p_52906_.getValue(HALF);
        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
            BlockPos blockpos = p_52905_.below();
            BlockState blockstate = p_52904_.getBlockState(blockpos);
            if (blockstate.is(p_52906_.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockstate1 = blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                p_52904_.setBlock(blockpos, blockstate1, 35);
                p_52904_.levelEvent(p_52907_, 2001, blockpos, Block.getId(blockstate));
            }
        }

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_52901_) {
        p_52901_.add(HALF);
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

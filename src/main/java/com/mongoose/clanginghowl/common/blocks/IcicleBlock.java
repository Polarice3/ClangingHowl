package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.utils.CHDamageSource;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class IcicleBlock extends Block implements Fallable, SimpleWaterloggedBlock {
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    public IcicleBlock() {
        super(Properties.copy(Blocks.PACKED_ICE));
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(CHBlocks.CRYOGENIC_ICICLE.get().asItem()) && pState.is(CHBlocks.CRYOGENIC_ICICLE.get())) {
            BlockState blockState = CHBlocks.BIG_CRYOGENIC_ICICLE.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, pState.getValue(BlockStateProperties.WATERLOGGED));
            if (!pPlayer.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            SoundType soundtype = blockState.getSoundType(pLevel, pPos, pPlayer);
            pLevel.playSound(pPlayer, pPos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            pLevel.gameEvent(GameEvent.BLOCK_PLACE, pPos, GameEvent.Context.of(pPlayer, blockState));
            pLevel.setBlockAndUpdate(pPos, blockState);
            blockState.getBlock().setPlacedBy(pLevel, pPos, blockState, pPlayer, itemstack);
            if (pPlayer instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)pPlayer, pPos, itemstack);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_153358_) {
        p_153358_.add(WATERLOGGED);
    }

    public FluidState getFluidState(BlockState p_153360_) {
        return p_153360_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_153360_);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_153340_) {
        BlockState blockstate = super.getStateForPlacement(p_153340_);
        if (blockstate != null) {
            FluidState fluidstate = p_153340_.getLevel().getFluidState(p_153340_.getClickedPos());
            return blockstate.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
        } else {
            return null;
        }
    }

    public boolean canSurvive(BlockState p_153347_, LevelReader p_153348_, BlockPos p_153349_) {
        BlockPos blockpos = p_153349_.above();
        BlockState blockstate = p_153348_.getBlockState(blockpos);
        return blockstate.isFaceSturdy(p_153348_, blockpos, Direction.DOWN);
    }

    public void tick(BlockState p_221865_, ServerLevel p_221866_, BlockPos p_221867_, RandomSource p_221868_) {
        if (!this.canSurvive(p_221865_, p_221866_, p_221867_)) {
            this.spawnFallingStalactite(p_221865_, p_221866_, p_221867_);
        }
    }

    public VoxelShape getShape(BlockState p_153342_, BlockGetter p_153343_, BlockPos p_153344_, CollisionContext p_153345_) {
        return SHAPE;
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if (direction == Direction.UP && !this.canSurvive(blockState, levelAccessor, blockPos)) {
            levelAccessor.scheduleTick(blockPos, this, 1);
        } else {
            if (blockState.getValue(WATERLOGGED)) {
                levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
            }
        }
        return super.updateShape(blockState, direction, blockState1, levelAccessor, blockPos, blockPos1);
    }

    public void animateTick(BlockState p_221870_, Level p_221871_, BlockPos p_221872_, RandomSource p_221873_) {
        if (p_221871_.getBiome(p_221872_).get().getBaseTemperature() > 0.5F && isTip(p_221870_)) {
            float f = p_221873_.nextFloat();
            if (!(f > 0.12F)) {
                spawnDripParticle(p_221871_, p_221872_, p_221870_);
            }
        }
    }

    private static void spawnDripParticle(Level p_154072_, BlockPos p_154073_, BlockState p_154074_) {
        Vec3 vec3 = p_154074_.getOffset(p_154072_, p_154073_);
        double d0 = 0.0625D;
        double d1 = (double)p_154073_.getX() + 0.5D + vec3.x;
        double d2 = (double)((float)(p_154073_.getY() + 1) - 0.6875F) - 0.0625D;
        double d3 = (double)p_154073_.getZ() + 0.5D + vec3.z;
        ParticleOptions particleoptions = ParticleTypes.DRIPPING_DRIPSTONE_WATER;
        p_154072_.addParticle(particleoptions, d1, d2, d3, 0.0D, 0.0D, 0.0D);
    }

    public void spawnFallingStalactite(BlockState p_154098_, ServerLevel p_154099_, BlockPos p_154100_) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = p_154100_.mutable();

        for(BlockState blockstate = p_154098_; blockstate.getBlock() instanceof IcicleBlock; blockstate = p_154099_.getBlockState(blockpos$mutableblockpos)) {
            FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(p_154099_, blockpos$mutableblockpos, blockstate);
            if (isTip(blockstate)) {
                fallingblockentity.setHurtsEntities(4.0F, 40);
                fallingblockentity.disableDrop();
                break;
            }

            blockpos$mutableblockpos.move(Direction.DOWN);
        }

    }

    public static boolean isTip(BlockState p_154154_) {
        if (!(p_154154_.getBlock() instanceof IcicleBlock)) {
            return false;
        } else {
            return !p_154154_.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) || p_154154_.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER;
        }
    }

    public void onBrokenAfterFall(Level p_154059_, BlockPos p_154060_, FallingBlockEntity p_154061_) {
        if (isTip(p_154061_.getBlockState())) {
            p_154059_.levelEvent(2001, p_154060_, Block.getId(this.defaultBlockState()));
            Vec3 vec3 = p_154061_.getBoundingBox().getCenter();
            p_154059_.gameEvent(p_154061_, GameEvent.BLOCK_DESTROY, vec3);
        }
    }

    public DamageSource getFallDamageSource(Entity p_254432_) {
        return CHDamageSource.getDamageSource(p_254432_.level(), CHDamageSource.ICICLE);
    }
}

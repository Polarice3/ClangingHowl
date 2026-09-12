package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.client.particles.FieryExplosionParticleOption;
import com.mongoose.clanginghowl.client.particles.SmallFireSplashParticleOption;
import com.mongoose.clanginghowl.utils.FakeExplosion;
import com.mongoose.clanginghowl.utils.MobUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;

public class BlazeFuelCylinderBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final com.mojang.serialization.MapCodec<BlazeFuelCylinderBlock> CODEC = simpleCodec(BlazeFuelCylinderBlock::new);

    @Override
    public com.mojang.serialization.MapCodec<BlazeFuelCylinderBlock> codec() { return CODEC; }

    public static final IntegerProperty CYLINDERS = IntegerProperty.create("cylinders", 1, 3);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    protected static final VoxelShape ONE_AABB = Block.box(4.5D, 0.0D, 4.5D, 11.5D, 16.0D, 11.5D);
    protected static final VoxelShape TWO_AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    protected static final VoxelShape THREE_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public BlazeFuelCylinderBlock() {
        this(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_ORANGE)
                .strength(3.0F, 0.0F)
                .sound(SoundType.COPPER)
                .noOcclusion()
                .ignitedByLava());
    }

    public BlazeFuelCylinderBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(CYLINDERS, 1)
                .setValue(WATERLOGGED, false)
                .setValue(TRIGGERED, false));
    }

    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
        return true;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_56089_) {
        BlockState blockstate = p_56089_.getLevel().getBlockState(p_56089_.getClickedPos());
        if (blockstate.is(this)) {
            return blockstate.setValue(CYLINDERS, Math.min(3, blockstate.getValue(CYLINDERS) + 1));
        } else {
            FluidState fluidstate = p_56089_.getLevel().getFluidState(p_56089_.getClickedPos());
            boolean flag = fluidstate.getType() == Fluids.WATER;
            return this.defaultBlockState().setValue(FACING, p_56089_.getHorizontalDirection()).setValue(WATERLOGGED, flag);
        }
    }

    public FluidState getFluidState(BlockState p_153759_) {
        return p_153759_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_153759_);
    }

    public BlockState updateShape(BlockState p_51157_, Direction p_51158_, BlockState p_51159_, LevelAccessor p_51160_, BlockPos p_51161_, BlockPos p_51162_) {
        if (p_51157_.getValue(WATERLOGGED)) {
            p_51160_.scheduleTick(p_51161_, Fluids.WATER, Fluids.WATER.getTickDelay(p_51160_));
        }

        return super.updateShape(p_51157_, p_51158_, p_51159_, p_51160_, p_51161_, p_51162_);
    }

    public VoxelShape getShape(BlockState p_56122_, BlockGetter p_56123_, BlockPos p_56124_, CollisionContext p_56125_) {
        return switch (p_56122_.getValue(CYLINDERS)) {
            default -> ONE_AABB;
            case 2 -> TWO_AABB;
            case 3 -> THREE_AABB;
        };
    }

    public RenderShape getRenderShape(BlockState p_53840_) {
        return RenderShape.MODEL;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_56120_) {
        p_56120_.add(FACING, CYLINDERS, WATERLOGGED, TRIGGERED);
    }

    public boolean canBeReplaced(BlockState p_56101_, BlockPlaceContext p_56102_) {
        return !p_56102_.isSecondaryUseActive() && p_56102_.getItemInHand().is(this.asItem()) && p_56101_.getValue(CYLINDERS) < 3 || super.canBeReplaced(p_56101_, p_56102_);
    }

    public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        explode(world, pos, igniter);
    }

    public static void explode(Level level, BlockPos blockPos, @Nullable LivingEntity exploder) {
        BlockState blockState = level.getBlockState(blockPos);
        if (level instanceof ServerLevel serverLevel) {
            float damage = 10.0F;
            float radius = 4.0F;
            if (blockState.hasProperty(CYLINDERS)) {
                damage *= blockState.getValue(CYLINDERS);
            }
            serverLevel.setBlock(blockPos, blockState.setValue(TRIGGERED, true), 3);
            Vec3 vec3 = blockPos.getCenter();
            serverLevel.playSound(null, vec3.x(), vec3.y(), vec3.z(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.0F);
            serverLevel.sendParticles(new SmallFireSplashParticleOption(radius, 0), vec3.x(), vec3.y(), vec3.z(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            serverLevel.sendParticles(new FieryExplosionParticleOption(radius, 0), vec3.x(), vec3.y(), vec3.z(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            new FakeExplosion(serverLevel, null, serverLevel.damageSources().explosion(exploder, exploder), vec3.x(), vec3.y(), vec3.z(), radius / 2, damage) {
                @Override
                public void explodeHurt(Entity target, DamageSource damageSource, double x, double y, double z, double seen, float actualDamage) {
                    if (target.hurt(damageSource, actualDamage)) {
                        if (target instanceof LivingEntity livingEntity) {
                            livingEntity.igniteForSeconds(15);
                        }
                    }
                    double d11 = seen;
                    if (target instanceof LivingEntity) {
                        d11 = (seen * (1.0D - ((LivingEntity) target).getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.EXPLOSION_KNOCKBACK_RESISTANCE)));
                    }

                    if (target instanceof LivingEntity) {
                        MobUtil.push(target, x * d11, y * d11, z * d11);
                    }
                }

                @Override
                public Optional<Float> getBlockExplosionResistance(BlockGetter p_46100_, BlockPos p_46101_, BlockState p_46102_, FluidState p_46103_) {
                    if (p_46102_.is(CHBlocks.FLAME_SPEWER.get())) {
                        return Optional.of(Math.max(0.0F, p_46103_.getExplosionResistance()));
                    }
                    return super.getBlockExplosionResistance(p_46100_, p_46101_, p_46102_, p_46103_);
                }

                @Override
                public void explodeBlocks(Level level, Entity source) {
                    for (BlockPos blockpos2 : this.toBlow) {
                        BlockState blockState1 = level.getBlockState(blockpos2);
                        if (blockState1.is(CHBlocks.BLAZE_FUEL_CYLINDER_BLOCK.get())) {
                            if (blockState1.hasProperty(BlazeFuelCylinderBlock.TRIGGERED) && !blockState1.getValue(BlazeFuelCylinderBlock.TRIGGERED)) {
                                BlazeFuelCylinderBlock.explode(level, blockpos2, source instanceof LivingEntity livingEntity ? livingEntity : null);
                            }
                        }
                        if (blockState1.is(CHBlocks.FLAME_SPEWER.get())) {
                            if (blockState1.hasProperty(FlameSpewerBlock.TRIGGERED) && !blockState1.getValue(FlameSpewerBlock.TRIGGERED)) {
                                FlameSpewerBlock.explode(level, blockpos2, source instanceof LivingEntity livingEntity ? livingEntity : null);
                            }
                        }
                    }
                    Util.shuffle(this.toBlow, level.getRandom());
                    for (BlockPos blockpos2 : this.toBlow) {
                        if (level.getBlockState(blockpos2).isAir() && level.getBlockState(blockpos2.below()).isSolidRender(level, blockpos2.below())) {
                            level.setBlockAndUpdate(blockpos2, BaseFireBlock.getState(level, blockpos2));
                        }
                    }
                }
            };
            level.removeBlock(blockPos, false);
        }
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        explode(level, pos, explosion.getDirectSourceEntity() instanceof LivingEntity livingEntity ? livingEntity : null);
    }

    @Override
    protected net.minecraft.world.ItemInteractionResult useItemOn(net.minecraft.world.item.ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return switch (interact(state, level, pos, player, hand, hit)) {
            case SUCCESS, SUCCESS_NO_ITEM_USED -> net.minecraft.world.ItemInteractionResult.SUCCESS;
            case CONSUME -> net.minecraft.world.ItemInteractionResult.CONSUME;
            case CONSUME_PARTIAL -> net.minecraft.world.ItemInteractionResult.CONSUME_PARTIAL;
            case FAIL -> net.minecraft.world.ItemInteractionResult.FAIL;
            case PASS -> net.minecraft.world.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return interact(state, level, pos, player, InteractionHand.MAIN_HAND, hit);
    }

    private InteractionResult interact(BlockState p_57450_, Level p_57451_, BlockPos p_57452_, Player p_57453_, InteractionHand p_57454_, BlockHitResult p_57455_) {
        ItemStack itemstack = p_57453_.getItemInHand(p_57454_);
        if (!itemstack.is(Items.FLINT_AND_STEEL) && !itemstack.is(Items.FIRE_CHARGE)) {
            return InteractionResult.PASS;
        } else {
            onCaughtFire(p_57450_, p_57451_, p_57452_, p_57455_.getDirection(), p_57453_);
            Item item = itemstack.getItem();
            if (!p_57453_.isCreative()) {
                if (itemstack.is(Items.FLINT_AND_STEEL)) {
                    itemstack.hurtAndBreak(1, p_57453_, net.minecraft.world.entity.LivingEntity.getSlotForHand(p_57454_));
                } else {
                    itemstack.shrink(1);
                }
            }

            p_57453_.awardStat(Stats.ITEM_USED.get(item));
            return InteractionResult.sidedSuccess(p_57451_.isClientSide);
        }
    }

    public void onProjectileHit(Level p_57429_, BlockState p_57430_, BlockHitResult p_57431_, Projectile p_57432_) {
        if (!p_57429_.isClientSide) {
            BlockPos blockpos = p_57431_.getBlockPos();
            Entity entity = p_57432_.getOwner();
            if (p_57432_.isOnFire() && p_57432_.mayInteract(p_57429_, blockpos)) {
                onCaughtFire(p_57430_, p_57429_, blockpos, null, entity instanceof LivingEntity ? (LivingEntity)entity : null);
            }
        }

    }

    public boolean dropFromExplosion(Explosion p_57427_) {
        return false;
    }
}

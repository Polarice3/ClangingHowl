package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.client.particles.FieryExplosionParticleOption;
import com.mongoose.clanginghowl.client.particles.SmallFireSplashParticleOption;
import com.mongoose.clanginghowl.common.blocks.entities.FlameSpewerBlockEntity;
import com.mongoose.clanginghowl.utils.FakeExplosion;
import com.mongoose.clanginghowl.utils.MobUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

public class FlameSpewerBlock extends BaseEntityBlock {
    public static final com.mojang.serialization.MapCodec<FlameSpewerBlock> CODEC = simpleCodec(FlameSpewerBlock::new);

    @Override
    public com.mojang.serialization.MapCodec<FlameSpewerBlock> codec() { return CODEC; }

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public FlameSpewerBlock() {
        this(BlockBehaviour.Properties.of()
                .sound(SoundType.NETHERITE_BLOCK)
                .mapColor(MapColor.COLOR_GRAY)
                .requiresCorrectToolForDrops()
                .strength(2.0F, 0.0F));
    }

    public FlameSpewerBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(ENABLED, false).setValue(TRIGGERED, false));
    }

    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.MODEL;
    }

    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(ENABLED, false).setValue(TRIGGERED, false);
    }

    public void neighborChanged(BlockState p_55666_, Level p_55667_, BlockPos p_55668_, Block p_55669_, BlockPos p_55670_, boolean p_55671_) {
        if (!p_55667_.isClientSide) {
            if (p_55667_.hasNeighborSignal(p_55668_)) {
                p_55667_.setBlock(p_55668_, p_55666_.cycle(ENABLED), 2);
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55673_) {
        p_55673_.add(FACING, ENABLED, TRIGGERED);
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        explode(level, pos, explosion.getDirectSourceEntity() instanceof LivingEntity livingEntity ? livingEntity : null);
    }

    public static void explode(Level level, BlockPos blockPos, @Nullable LivingEntity exploder) {
        BlockState blockState = level.getBlockState(blockPos);
        if (level instanceof ServerLevel serverLevel) {
            float damage = 10.0F;
            float radius = 4.0F;
            serverLevel.setBlock(blockPos, blockState.setValue(TRIGGERED, true), 3);
            Vec3 vec3 = blockPos.getCenter();
            serverLevel.playSound(null, vec3.x(), vec3.y(), vec3.z(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.0F);
            serverLevel.sendParticles(new SmallFireSplashParticleOption(radius, 0), vec3.x(), vec3.y(), vec3.z(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            serverLevel.sendParticles(new FieryExplosionParticleOption(radius, 0), vec3.x(), vec3.y(), vec3.z(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            new FakeExplosion(serverLevel, null, serverLevel.damageSources().explosion(exploder, exploder), vec3.x(), vec3.y(), vec3.z(), radius, damage) {
                @Override
                public void explodeHurt(Entity target, DamageSource damageSource, double x, double y, double z, double seen, float actualDamage) {
                    if (target.hurt(damageSource, actualDamage)){
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new FlameSpewerBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152755_, BlockState p_152756_, BlockEntityType<T> p_152757_) {
        return (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof FlameSpewerBlockEntity block) {
                block.tick();
            }
        };
    }
}

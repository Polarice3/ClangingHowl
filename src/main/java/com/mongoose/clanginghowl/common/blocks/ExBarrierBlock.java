package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.common.blocks.entities.ExBarrierBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class ExBarrierBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final com.mojang.serialization.MapCodec<ExBarrierBlock> CODEC = simpleCodec(ExBarrierBlock::new);

    @Override
    public com.mojang.serialization.MapCodec<ExBarrierBlock> codec() { return CODEC; }

    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public ExBarrierBlock() {
        this(Properties.of()
                .strength(4.0F, 9.0F)
                .sound(SoundType.COPPER));
    }

    public ExBarrierBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ENABLED, true).setValue(FACING, Direction.NORTH));
    }

    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.MODEL;
    }

    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_48781_) {
        return this.defaultBlockState().setValue(ENABLED, true).setValue(FACING, p_48781_.getHorizontalDirection());
    }

    public void neighborChanged(BlockState p_55666_, Level p_55667_, BlockPos p_55668_, Block p_55669_, BlockPos p_55670_, boolean p_55671_) {
        if (!p_55667_.isClientSide) {
            if (p_55667_.hasNeighborSignal(p_55668_)) {
                p_55667_.setBlock(p_55668_, p_55666_.cycle(ENABLED), 2);
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53838_) {
        p_53838_.add(FACING, ENABLED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new ExBarrierBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152755_, BlockState p_152756_, BlockEntityType<T> p_152757_) {
        return (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof ExBarrierBlockEntity block) {
                block.tick();
            }
        };
    }
}

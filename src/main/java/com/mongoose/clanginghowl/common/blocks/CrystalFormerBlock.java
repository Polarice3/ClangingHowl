package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.common.blocks.entities.CrystalFormerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nullable;

public class CrystalFormerBlock extends BaseEntityBlock {
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public CrystalFormerBlock() {
        super(Properties.of()
                .strength(4.0F, 9.0F)
                .sound(SoundType.COPPER)
                .lightLevel(l -> l.getValue(ENABLED) ? 2 : 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(ENABLED, true));
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_55659_) {
        return this.defaultBlockState().setValue(ENABLED, true);
    }

    public void neighborChanged(BlockState p_55666_, Level p_55667_, BlockPos p_55668_, Block p_55669_, BlockPos p_55670_, boolean p_55671_) {
        if (!p_55667_.isClientSide) {
            if (p_55667_.hasNeighborSignal(p_55668_)) {
                p_55667_.setBlock(p_55668_, p_55666_.cycle(ENABLED), 2);
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55673_) {
        p_55673_.add(ENABLED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new CrystalFormerBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152755_, BlockState p_152756_, BlockEntityType<T> p_152757_) {
        return (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof CrystalFormerBlockEntity block) {
                block.tick();
            }
        };
    }
}

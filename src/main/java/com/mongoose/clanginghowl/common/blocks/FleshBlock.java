package com.mongoose.clanginghowl.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;

public class FleshBlock extends Block {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;

    public FleshBlock() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .strength(2.0F)
                .sound(SoundType.HONEY_BLOCK));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0));
    }

    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @javax.annotation.Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (!pLevel.isClientSide) {
            int type = 0;
            if (pLevel.getRandom().nextFloat() <= 0.25F) {
                type = pLevel.getRandom().nextIntBetweenInclusive(1, 4);
            }
            pLevel.setBlock(pPos, pState.setValue(AGE, type), 11);
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int type = 0;
        if (!context.getLevel().isClientSide) {
            if (context.getLevel().getRandom().nextFloat() <= 0.25F) {
                type = context.getLevel().getRandom().nextIntBetweenInclusive(1, 4);
            }
        }
        return this.defaultBlockState().setValue(AGE, 0);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53071_) {
        p_53071_.add(AGE);
    }
}

package com.mongoose.clanginghowl.common.blocks;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;

public class FleshBlock extends Block {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_5;

    public FleshBlock() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .strength(2.0F)
                .ignitedByLava()
                .sound(SoundType.HONEY_BLOCK));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (!context.getLevel().isClientSide) {
            int type = 0;
            if (context.getLevel().getRandom().nextFloat() <= 0.25F) {
                type = context.getLevel().getRandom().nextIntBetweenInclusive(1, 5);
            }
            return this.defaultBlockState().setValue(AGE, type);
        } else {
            return this.defaultBlockState();
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53071_) {
        p_53071_.add(AGE);
    }
}

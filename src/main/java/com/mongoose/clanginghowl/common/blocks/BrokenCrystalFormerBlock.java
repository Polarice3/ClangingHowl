package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.common.blocks.entities.BrokenCrystalFormerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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

public class BrokenCrystalFormerBlock extends BaseEntityBlock {
    public static final com.mojang.serialization.MapCodec<BrokenCrystalFormerBlock> CODEC = simpleCodec(BrokenCrystalFormerBlock::new);

    @Override
    public com.mojang.serialization.MapCodec<BrokenCrystalFormerBlock> codec() { return CODEC; }

    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public BrokenCrystalFormerBlock() {
        this(Properties.of()
                .strength(4.0F, 9.0F)
                .sound(SoundType.COPPER));
    }

    public BrokenCrystalFormerBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ENABLED, true));
    }

    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.MODEL;
    }

    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
        return 1.0F;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_55659_) {
        return this.defaultBlockState().setValue(ENABLED, true);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55673_) {
        p_55673_.add(ENABLED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BrokenCrystalFormerBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152755_, BlockState p_152756_, BlockEntityType<T> p_152757_) {
        return (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof BrokenCrystalFormerBlockEntity block) {
                block.tick();
            }
        };
    }
}

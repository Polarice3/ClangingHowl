package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.common.blocks.entities.FleshNestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class FleshNestBlock extends BaseEntityBlock {
    public static final com.mojang.serialization.MapCodec<FleshNestBlock> CODEC = simpleCodec(FleshNestBlock::new);

    @Override
    public com.mojang.serialization.MapCodec<FleshNestBlock> codec() { return CODEC; }


    public FleshNestBlock() {
        this(Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .strength(10.0F)
                .ignitedByLava()
                .sound(SoundType.HONEY_BLOCK));
    }

    public FleshNestBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.level.LevelAccessor world, BlockPos pos, BlockEntity blockEntity, net.minecraft.world.entity.Entity breaker, net.minecraft.world.item.ItemStack tool) {
        return com.mongoose.clanginghowl.common.enchantments.CHEnchantments.level(tool, net.minecraft.world.item.enchantment.Enchantments.SILK_TOUCH) == 0 ? 10 : 0;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new FleshNestBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return (world, pos, state, blockEntity) -> {
            if (blockEntity instanceof FleshNestBlockEntity block) {
                block.tick();
            }
        };
    }
}

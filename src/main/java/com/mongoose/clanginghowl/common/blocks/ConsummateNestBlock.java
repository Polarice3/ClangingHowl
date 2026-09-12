package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.common.blocks.entities.CHBlockEntities;
import com.mongoose.clanginghowl.common.blocks.entities.ConsummateNestBlockEntity;
import com.mongoose.clanginghowl.common.blocks.entities.consummate_nest.ConsummateNestState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class ConsummateNestBlock extends BaseEntityBlock {
    public static final com.mojang.serialization.MapCodec<ConsummateNestBlock> CODEC = simpleCodec(ConsummateNestBlock::new);

    @Override
    public com.mojang.serialization.MapCodec<ConsummateNestBlock> codec() { return CODEC; }

    public static final EnumProperty<ConsummateNestState> STATE = CHBlockStates.CONSUMMATE_NEST_STATE;

    public ConsummateNestBlock() {
        this(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .lightLevel(l -> l.getValue(ConsummateNestBlock.STATE).lightLevel())
                .strength(50.0F)
                .sound(SoundType.METAL)
                .noOcclusion());
    }

    public ConsummateNestBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STATE, ConsummateNestState.INACTIVE));
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level instanceof ServerLevel serverLevel) {
            if (level.getBlockEntity(pos) instanceof ConsummateNestBlockEntity be)
                be.getConsummateNest().activate(serverLevel, pos);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.level.LevelAccessor world, BlockPos pos, BlockEntity blockEntity, net.minecraft.world.entity.Entity breaker, net.minecraft.world.item.ItemStack tool) {
        return 15 + world.getRandom().nextInt(15) + world.getRandom().nextInt(15);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ConsummateNestBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level instanceof ServerLevel serverLevel
                ? createTickerHelper(type, CHBlockEntities.CONSUMMATE_NEST.get(), (level1, blockPos, state, block) -> block.getConsummateNest().tickServer(serverLevel, blockPos))
                : createTickerHelper(type, CHBlockEntities.CONSUMMATE_NEST.get(), (level1, blockPos, state, block) -> block.getConsummateNest().tickClient(level1, blockPos));
    }

}

package com.mongoose.clanginghowl.common.blocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HangingRootsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HangingFleshBlock extends HangingRootsBlock {

    public HangingFleshBlock() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .strength(0.5F)
                .forceSolidOn()
                .noCollission()
                .ignitedByLava()
                .sound(SoundType.HONEY_BLOCK)
                .pushReaction(PushReaction.DESTROY));
    }

    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(CHBlocks.HANGING_TECHNOFLESH.get().asItem()) && pState.is(CHBlocks.HANGING_TECHNOFLESH.get())) {
            BlockState blockState = CHBlocks.BIG_HANGING_TECHNOFLESH.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, pState.getValue(BlockStateProperties.WATERLOGGED));
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
}

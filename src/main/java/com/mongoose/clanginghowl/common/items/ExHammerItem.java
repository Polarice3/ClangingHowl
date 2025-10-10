package com.mongoose.clanginghowl.common.items;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nonnull;

public class ExHammerItem extends SwordItem {
    public ExHammerItem() {
        super(CHTiers.EXTRATERRESTRIAL, 6, -3.1F, new Item.Properties().durability(1000));
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        if (container.getDamageValue() <= container.getMaxDamage()) {
            container.setDamageValue(itemStack.getDamageValue() + 1);
        } else {
            container = ItemStack.EMPTY;
        }
        return container;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
    }

    public InteractionResult useOn(UseOnContext p_40529_) {
        Level level = p_40529_.getLevel();
        BlockPos blockpos = p_40529_.getClickedPos();
        Player player = p_40529_.getPlayer();
        BlockState blockstate = level.getBlockState(blockpos);
        ItemStack itemstack = p_40529_.getItemInHand();
        BlockState result = null;
        if (blockstate.is(CHBlocks.STEEL_PLATE_BLOCK.get())) {
            result = CHBlocks.DAMAGED_STEEL_PLATE_BLOCK.get().defaultBlockState();
        }
        if (blockstate.is(CHBlocks.CARVED_STEEL_PLATE_BLOCK.get())) {
            result = CHBlocks.DAMAGED_CARVED_STEEL_PLATE_BLOCK.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockstate.getValue(RotatedPillarBlock.AXIS));
        }
        if (result != null) {
            level.playSound(player, blockpos, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
            }

            level.setBlock(blockpos, result, 11);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, result));
            ParticleUtils.spawnParticlesOnBlockFaces(level, blockpos, CHParticleTypes.BREAKDOWN_SMOKE.get(), UniformInt.of(1, 3));
            if (player != null) {
                itemstack.hurtAndBreak(1, player, (p_150686_) -> {
                    p_150686_.broadcastBreakEvent(p_40529_.getHand());
                });
                player.swing(p_40529_.getHand());
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}

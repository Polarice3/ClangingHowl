package com.mongoose.clanginghowl.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.client.particles.RotationParticleOption;
import com.mongoose.clanginghowl.common.blocks.*;
import com.mongoose.clanginghowl.init.CHTags;
import com.mongoose.clanginghowl.utils.ItemHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class WrenchItem extends Item {
    private final Multimap<net.minecraft.core.Holder<Attribute>, AttributeModifier> wrenchAttributes;

    public WrenchItem() {
        super(new Properties()
                .durability(1200));
        ImmutableMultimap.Builder<net.minecraft.core.Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 6.0D, AttributeModifier.Operation.ADD_VALUE));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.6F, AttributeModifier.Operation.ADD_VALUE));
        this.wrenchAttributes = builder.build();
    }

    public InteractionResult useOn(UseOnContext p_40529_) {
        Level level = p_40529_.getLevel();
        BlockPos blockpos = p_40529_.getClickedPos();
        Player player = p_40529_.getPlayer();
        BlockState blockstate = level.getBlockState(blockpos);
        ItemStack itemstack = p_40529_.getItemInHand();
        BlockState result = null;
        if (blockstate.is(CHBlocks.DAMAGED_STEEL_PLATE_BLOCK.get())) {
            result = CHBlocks.STEEL_PLATE_BLOCK.get().defaultBlockState();
        }
        if (blockstate.is(CHBlocks.DAMAGED_CARVED_STEEL_PLATE_BLOCK.get())) {
            result = CHBlocks.CARVED_STEEL_PLATE_BLOCK.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockstate.getValue(RotatedPillarBlock.AXIS));
        }
        if (blockstate.is(CHBlocks.STEEL_LAMP.get())) {
            result = blockstate.cycle(SteelLampBlock.ENABLED);
        }
        if (blockstate.is(CHBlocks.BROKEN_STEEL_LAMP.get())) {
            result = CHBlocks.STEEL_LAMP.get().defaultBlockState().setValue(SteelLampBlock.FACING, blockstate.getValue(SteelLampBlock.FACING));
        }
        if (blockstate.is(CHBlocks.REDSTONE_STEEL_LAMP.get())) {
            result = blockstate.setValue(RedstoneSteelLampBlock.TRIGGER, 1).cycle(RedstoneSteelLampBlock.ENABLED);
        }
        if (blockstate.is(CHBlocks.BROKEN_CRYSTAL_FORMER.get())) {
            result = CHBlocks.CRYSTAL_FORMER.get().defaultBlockState();
        }
        if (blockstate.is(CHBlocks.CRYSTAL_FORMER.get())) {
            result = blockstate.cycle(CrystalFormerBlock.ENABLED);
        }
        if (blockstate.is(CHBlocks.FLAME_SPEWER.get())) {
            result = blockstate.cycle(FlameSpewerBlock.ENABLED);
        }
        if (blockstate.is(CHBlocks.BARRIER_OF_EXTRATERRESTRIAL_ACTIVITY.get())) {
            result = blockstate.cycle(ExBarrierBlock.ENABLED);
        }
        if (blockstate.is(CHBlocks.STEEL_BRIDGE.get())) {
            result = blockstate.cycle(SteelBridgeBlock.ALTERNATE);
        }
        if (blockstate.getBlock() instanceof StairBlock && blockstate.hasProperty(StairBlock.FACING)) {
            result = blockstate.cycle(StairBlock.FACING);
        }
        if (result != null) {
            Vec3 vec3 = blockpos.getCenter();
            if (blockstate.hasProperty(CHBlockStates.ALTERNATE)) {
                level.playSound(player, blockpos, SoundEvents.COPPER_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.addParticle(new RotationParticleOption(1.0F, 0), vec3.x, vec3.y, vec3.z, 0.0F, 0.0F, 0.0F);
            } else if (blockstate.hasProperty(BlockStateProperties.ENABLED) && !blockstate.is(CHTags.Blocks.BROKEN)) {
                level.playSound(player, blockpos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 1.0F, 1.0F);
            } else if (blockstate.hasProperty(StairBlock.FACING) && !blockstate.is(CHTags.Blocks.BROKEN)) {
                if (player != null) {
                    player.getCooldowns().addCooldown(this, 5);
                }
                level.playSound(player, blockpos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.addParticle(new RotationParticleOption(1.0F, 0), vec3.x, vec3.y, vec3.z, 0.0F, 0.0F, 0.0F);
            } else {
                level.playSound(player, blockpos, SoundEvents.IRON_GOLEM_REPAIR, SoundSource.BLOCKS, 1.0F, 1.0F);
                ItemHelper.hurtAndBreak(itemstack, 1, player);
                ParticleUtils.spawnParticlesOnBlockFaces(level, blockpos, CHParticleTypes.REPAIR.get(), UniformInt.of(2, 4));
            }
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
            }

            level.setBlock(blockpos, result, 11);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, result));

            if (player != null) {
                player.swing(p_40529_.getHand());
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.getCooldowns().isOnCooldown(this)) {
            if (target instanceof IronGolem) {
                if (target.getHealth() < target.getMaxHealth()) {
                    target.heal(40);
                    target.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, 1.0F);
                    ItemHelper.hurtAndBreak(itemStack, 5, player);
                    player.getCooldowns().addCooldown(this, 20);
                    if (target.level() instanceof ServerLevel serverLevel) {
                        for (int i = 0; i < 5; ++i) {
                            double d0 = serverLevel.random.nextGaussian() * 0.02D;
                            double d1 = serverLevel.random.nextGaussian() * 0.02D;
                            double d2 = serverLevel.random.nextGaussian() * 0.02D;
                            serverLevel.sendParticles(CHParticleTypes.REPAIR.get(), target.getRandomX(0.5D), target.getRandomY(), target.getRandomZ(0.5D), 0, d0, d1, d2, 0.5F);
                        }
                    }
                    player.swing(hand);
                }
            }
        }
        return super.interactLivingEntity(itemStack, player, target, hand);
    }

    @Override
    public net.minecraft.world.item.component.ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack itemStack) {
        return com.mongoose.clanginghowl.utils.ItemHelper.mainHandAttributes(this.wrenchAttributes);
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
    }

    public void addInformationAfterShift(List<Component> tooltip) {
        tooltip.add(Component.translatable("info.clanginghowl.item.wrench.0"));
        tooltip.add(Component.translatable("info.clanginghowl.item.wrench.1"));
    }

}

package com.mongoose.clanginghowl.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.client.particles.RotationParticleOption;
import com.mongoose.clanginghowl.common.blocks.CHBlockStates;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.common.blocks.CrystalFormerBlock;
import com.mongoose.clanginghowl.common.blocks.SteelBridgeBlock;
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
    private final Multimap<Attribute, AttributeModifier> wrenchAttributes;

    public WrenchItem() {
        super(new Properties()
                .durability(1200));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 6.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.6F, AttributeModifier.Operation.ADDITION));
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
        if (blockstate.is(CHBlocks.CRYSTAL_FORMER.get())) {
            result = blockstate.cycle(CrystalFormerBlock.ENABLED);
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
            } else if (blockstate.hasProperty(BlockStateProperties.ENABLED)) {
                level.playSound(player, blockpos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 1.0F, 1.0F);
            } else if (blockstate.hasProperty(StairBlock.FACING)) {
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

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.wrenchAttributes : super.getAttributeModifiers(equipmentSlot, itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
    }

    public void addInformationAfterShift(List<Component> tooltip) {
        tooltip.add(Component.translatable("info.clanginghowl.item.wrench.0"));
        tooltip.add(Component.translatable("info.clanginghowl.item.wrench.1"));
    }

}

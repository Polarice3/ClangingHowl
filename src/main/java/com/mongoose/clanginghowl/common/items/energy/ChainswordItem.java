package com.mongoose.clanginghowl.common.items.energy;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class ChainswordItem extends EnergyItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    private final Multimap<Attribute, AttributeModifier> dischargedModifiers;

    public ChainswordItem() {
        super(new Properties().stacksTo(1));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 6.5F, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -(4.0F - 1.6F), AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder2 = ImmutableMultimap.builder();
        builder2.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 6.5F * 0.2F, AttributeModifier.Operation.ADDITION));
        builder2.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -(4.0F - 1.6F), AttributeModifier.Operation.ADDITION));
        this.dischargedModifiers = builder2.build();
    }

    @Override
    public int getMaxEnergy() {
        return 1500;
    }

    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        if (!IEnergyItem.isEmpty(itemStack)) {
            if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(attacker)) {
                this.consumeEnergy(itemStack);
            }
            attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), CHSounds.CHAINSAW_BLOW.get(), attacker.getSoundSource(), 1.0F, 1.0F);
        } else if (attacker instanceof Player player) {
            player.displayClientMessage(Component.translatable("info.clanginghowl.energy.empty"), true);
        }
        return true;
    }

    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if (blockState.getDestroySpeed(level, blockPos) != 0.0F) {
            if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                this.consumeEnergy(itemStack);
            }
        }

        return true;
    }

    public boolean canAttackBlock(BlockState p_43291_, Level p_43292_, BlockPos p_43293_, Player p_43294_) {
        return !p_43294_.isCreative();
    }

    public float getDestroySpeed(ItemStack p_43288_, BlockState p_43289_) {
        if (p_43289_.is(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            return p_43289_.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment.category == EnchantmentCategory.WEAPON) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public boolean isCorrectToolForDrops(BlockState p_43298_) {
        return p_43298_.is(Blocks.COBWEB);
    }

    public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        return Items.DIAMOND_SWORD.use(p_40672_, p_40673_, p_40674_);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            if (IEnergyItem.isEmpty(stack)) {
                return this.dischargedModifiers;
            } else {
                return this.defaultModifiers;
            }
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        this.addEnergyText(stack, worldIn, tooltip, flagIn);
    }

    public void addInformationAfterShift(List<Component> tooltip) {
        tooltip.add(Component.translatable("info.clanginghowl.item.chainsword.0"));
        tooltip.add(Component.translatable("info.clanginghowl.item.chainsword.1"));
        tooltip.add(Component.translatable("info.clanginghowl.item.chainsword.2"));
    }
}

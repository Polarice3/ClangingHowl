package com.mongoose.clanginghowl.common.items.curios;

import com.mongoose.clanginghowl.utils.CHItemData;
import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import com.mongoose.clanginghowl.common.items.energy.BatteryItem;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import com.mongoose.clanginghowl.utils.MobUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class CuriosEnergyItem extends CHCurioItem implements IEnergyItem {

    public CuriosEnergyItem() {
        super();
    }

    public CuriosEnergyItem(Properties p_41383_) {
        super(p_41383_);
    }

    public ItemStack getPowerlessItem(){
        ItemStack itemStack = new ItemStack(this);
        IEnergyItem.setEnergy(itemStack, 0);
        IEnergyItem.setMaxEnergyAmount(itemStack, this.getMaxEnergy());
        return itemStack;
    }

    public ItemStack getPoweredItem(){
        ItemStack itemStack = new ItemStack(this);
        IEnergyItem.setEnergy(itemStack, this.getMaxEnergy());
        IEnergyItem.setMaxEnergyAmount(itemStack, this.getMaxEnergy());
        return itemStack;
    }

    public int getBarColor(ItemStack stack) {
        float f = Math.max(0.0F, 1.0F - amountColor(stack));
        return Mth.color(1.0F, 0.88F * f, 0.045F);
    }

    public float amountColor(ItemStack stack){
        if (CHItemData.hasData(stack)) {
            int energy = CHItemData.getInt(stack, ENERGY_AMOUNT);
            int maxEnergy = CHItemData.getInt(stack, MAX_ENERGY_AMOUNT);
            return 1.0F - ((float) energy / maxEnergy);
        } else {
            return 1.0F;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return CHItemData.hasData(stack) && !IEnergyItem.isFull(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack){
        if (CHItemData.hasData(stack)) {
            int energy = CHItemData.getInt(stack, ENERGY_AMOUNT);
            int maxEnergy = CHItemData.getInt(stack, MAX_ENERGY_AMOUNT);
            return Math.round((energy * 13.0F / maxEnergy));
        } else {
            return 0;
        }
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        IEnergyItem.setEnergy(pStack, this.getMaxEnergy());
        IEnergyItem.setMaxEnergyAmount(pStack, this.getMaxEnergy());
        super.onCraftedBy(pStack, pLevel, pPlayer);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        this.setTagTick(stack);
        if (!worldIn.isClientSide) {
            if (com.mongoose.clanginghowl.common.enchantments.CHEnchantments.level(stack, CHEnchantments.ECOLOGICAL_ENERGY) > 0) {
                if (MobUtil.isInSunlight(entityIn)) {
                    if (entityIn.tickCount % 20 == 0) {
                        IEnergyItem.powerItem(stack, 2);
                    }
                }
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
    }

    public void addEnergyText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (CHItemData.hasData(stack)) {
            tooltip.add(Component.empty());
            int energy = CHItemData.getInt(stack, ENERGY_AMOUNT);
            int maxEnergy = CHItemData.getInt(stack, MAX_ENERGY_AMOUNT);
            if (stack.getItem() instanceof BatteryItem) {
                tooltip.add(Component.translatable("info.clanginghowl.battery.amount").append(Component.literal(" ")).append(Component.translatable("info.clanginghowl.battery.number", energy, maxEnergy).withStyle(ChatFormatting.GRAY)));
            } else {
                tooltip.add(Component.translatable("info.clanginghowl.energy.amount").append(Component.literal(" ")).append(Component.translatable("info.clanginghowl.energy.number", energy, maxEnergy).withStyle(ChatFormatting.GRAY)));
            }
        }
    }
}

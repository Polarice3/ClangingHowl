package com.mongoose.clanginghowl.common.items.energy;

import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IEnergyItem {
    String ENERGY_AMOUNT = "Energy";
    String MAX_ENERGY_AMOUNT = "Max Energy";

    int getMaxEnergy();

    default int getConsumption(ItemStack itemStack) {
        return 4;
    }

    default void consumeEnergy(ItemStack itemStack) {
        int amount = this.getConsumption(itemStack) - itemStack.getEnchantmentLevel(CHEnchantments.ENERGY_EFFICIENCY.get());
        IEnergyItem.decreaseEnergy(itemStack, amount);
    }

    default void setTagTick(ItemStack stack){
        if (stack.getTag() == null){
            CompoundTag compound = stack.getOrCreateTag();
            compound.putInt(ENERGY_AMOUNT, 0);
            compound.putInt(MAX_ENERGY_AMOUNT, this.getMaxEnergy());
        }
        if (!stack.getTag().contains(MAX_ENERGY_AMOUNT)){
            CompoundTag compound = stack.getOrCreateTag();
            compound.putInt(MAX_ENERGY_AMOUNT, this.getMaxEnergy());
        }
        if (stack.getTag().getInt(ENERGY_AMOUNT) > stack.getTag().getInt(MAX_ENERGY_AMOUNT)){
            stack.getTag().putInt(ENERGY_AMOUNT, stack.getTag().getInt(MAX_ENERGY_AMOUNT));
        }
        if (stack.getTag().getInt(ENERGY_AMOUNT) < 0){
            stack.getTag().putInt(ENERGY_AMOUNT, 0);
        }
    }

    static boolean isFull(ItemStack itemStack) {
        if (itemStack.getTag() == null){
            return false;
        }
        int energy = itemStack.getTag().getInt(ENERGY_AMOUNT);
        int maxEnergy = itemStack.getTag().getInt(MAX_ENERGY_AMOUNT);
        return energy >= maxEnergy;
    }

    static boolean isEmpty(ItemStack itemStack) {
        if (itemStack.getTag() == null){
            return true;
        }
        int energy = itemStack.getTag().getInt(ENERGY_AMOUNT);
        return energy <= 0;
    }

    static int currentEnergy(ItemStack itemStack){
        if (itemStack.getTag() != null){
            return itemStack.getTag().getInt(ENERGY_AMOUNT);
        } else {
            return 0;
        }
    }

    static int maximumEnergy(ItemStack itemStack){
        if (itemStack.getTag() != null){
            return itemStack.getTag().getInt(MAX_ENERGY_AMOUNT);
        } else {
            return 0;
        }
    }

    static void setEnergy(ItemStack itemStack, int energy){
        if (!(itemStack.getItem() instanceof IEnergyItem)) {
            return;
        }
        itemStack.getOrCreateTag().putInt(ENERGY_AMOUNT, energy);
    }

    static void setMaxEnergyAmount(ItemStack itemStack, int energy){
        if (!(itemStack.getItem() instanceof IEnergyItem)) {
            return;
        }
        itemStack.getOrCreateTag().putInt(MAX_ENERGY_AMOUNT, energy);
    }

    static void powerItem(ItemStack itemStack, int energy) {
        if (!(itemStack.getItem() instanceof IEnergyItem) || itemStack.getTag() == null) {
            return;
        }
        int currentEnergy = itemStack.getTag().getInt(ENERGY_AMOUNT);
        if (!isFull(itemStack)) {
            int finalCount = Math.min(currentEnergy + energy, maximumEnergy(itemStack));
            itemStack.getOrCreateTag().putInt(ENERGY_AMOUNT, finalCount);
        }
    }

    static void decreaseEnergy(ItemStack itemStack, int energy) {
        if (!(itemStack.getItem() instanceof IEnergyItem) || itemStack.getTag() == null) {
            return;
        }
        int currentEnergy = itemStack.getTag().getInt(ENERGY_AMOUNT);
        if (!isEmpty(itemStack)) {
            int finalCount = Math.max(currentEnergy - energy, 0);
            itemStack.getOrCreateTag().putInt(ENERGY_AMOUNT, finalCount);
        }
    }

    static void chargeEnergy(ItemStack charging, ItemStack battery) {
        if (!isEmpty(battery)) {
            powerItem(charging, 1);
            decreaseEnergy(battery, 1);
            if (isEmpty(battery)) {
                battery.shrink(1);
            }
        }
    }
}

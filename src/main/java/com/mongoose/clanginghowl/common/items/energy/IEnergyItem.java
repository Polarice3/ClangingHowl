package com.mongoose.clanginghowl.common.items.energy;

import com.mongoose.clanginghowl.utils.CHItemData;
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

    default boolean canCharge() {
        return true;
    }

    default void consumeEnergy(ItemStack itemStack) {
        int amount = this.getConsumption(itemStack) - com.mongoose.clanginghowl.common.enchantments.CHEnchantments.level(itemStack, CHEnchantments.ENERGY_EFFICIENCY);
        IEnergyItem.decreaseEnergy(itemStack, amount);
    }

    default void setTagTick(ItemStack stack){
        if (!CHItemData.hasData(stack)){

            CHItemData.putInt(stack, ENERGY_AMOUNT, 0);
            CHItemData.putInt(stack, MAX_ENERGY_AMOUNT, this.getMaxEnergy());
        }
        if (!CHItemData.contains(stack, MAX_ENERGY_AMOUNT)){

            CHItemData.putInt(stack, MAX_ENERGY_AMOUNT, this.getMaxEnergy());
        }
        if (CHItemData.getInt(stack, ENERGY_AMOUNT) > CHItemData.getInt(stack, MAX_ENERGY_AMOUNT)){
            CHItemData.putInt(stack, ENERGY_AMOUNT, CHItemData.getInt(stack, MAX_ENERGY_AMOUNT));
        }
        if (CHItemData.getInt(stack, ENERGY_AMOUNT) < 0){
            CHItemData.putInt(stack, ENERGY_AMOUNT, 0);
        }
    }

    static boolean isFull(ItemStack itemStack) {
        if (!CHItemData.hasData(itemStack)){
            return false;
        }
        int energy = CHItemData.getInt(itemStack, ENERGY_AMOUNT);
        int maxEnergy = CHItemData.getInt(itemStack, MAX_ENERGY_AMOUNT);
        return energy >= maxEnergy;
    }

    static boolean isEmpty(ItemStack itemStack) {
        if (!CHItemData.hasData(itemStack)){
            return true;
        }
        int energy = CHItemData.getInt(itemStack, ENERGY_AMOUNT);
        return energy <= 0;
    }

    static int currentEnergy(ItemStack itemStack){
        if (CHItemData.hasData(itemStack)){
            return CHItemData.getInt(itemStack, ENERGY_AMOUNT);
        } else {
            return 0;
        }
    }

    static int maximumEnergy(ItemStack itemStack){
        if (CHItemData.hasData(itemStack)){
            return CHItemData.getInt(itemStack, MAX_ENERGY_AMOUNT);
        } else {
            return 0;
        }
    }

    static void setEnergy(ItemStack itemStack, int energy){
        if (!(itemStack.getItem() instanceof IEnergyItem)) {
            return;
        }
        CHItemData.putInt(itemStack, ENERGY_AMOUNT, energy);
    }

    static void setMaxEnergyAmount(ItemStack itemStack, int energy){
        if (!(itemStack.getItem() instanceof IEnergyItem)) {
            return;
        }
        CHItemData.putInt(itemStack, MAX_ENERGY_AMOUNT, energy);
    }

    static void powerItem(ItemStack itemStack, int energy) {
        if (!(itemStack.getItem() instanceof IEnergyItem) || !CHItemData.hasData(itemStack)) {
            return;
        }
        int currentEnergy = CHItemData.getInt(itemStack, ENERGY_AMOUNT);
        if (!isFull(itemStack)) {
            int finalCount = Math.min(currentEnergy + energy, maximumEnergy(itemStack));
            CHItemData.putInt(itemStack, ENERGY_AMOUNT, finalCount);
        }
    }

    static void decreaseEnergy(ItemStack itemStack, int energy) {
        if (!(itemStack.getItem() instanceof IEnergyItem) || !CHItemData.hasData(itemStack)) {
            return;
        }
        int currentEnergy = CHItemData.getInt(itemStack, ENERGY_AMOUNT);
        if (!isEmpty(itemStack)) {
            int finalCount = Math.max(currentEnergy - energy, 0);
            CHItemData.putInt(itemStack, ENERGY_AMOUNT, finalCount);
        }
    }

    static void chargeEnergy(ItemStack charging, ItemStack battery) {
        if (!isEmpty(battery)) {
            powerItem(charging, 1);
            decreaseEnergy(battery, 1);
            if (isEmpty(battery) && !BatteryItem.isPersistant(battery)) {
                battery.shrink(1);
            }
        }
    }
}

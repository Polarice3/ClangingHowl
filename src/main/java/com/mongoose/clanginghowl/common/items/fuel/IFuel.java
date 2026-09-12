package com.mongoose.clanginghowl.common.items.fuel;

import com.mongoose.clanginghowl.utils.CHItemData;
import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IFuel {
    String FUEL_AMOUNT = "Fuel";
    String MAX_FUEL_AMOUNT = "Max Fuel";

    int getMaxFuel();

    default int getConsumption(ItemStack itemStack) {
        return 5;
    }

    default void consumeFuel(ItemStack itemStack) {
        int amount = this.getConsumption(itemStack) - com.mongoose.clanginghowl.common.enchantments.CHEnchantments.level(itemStack, CHEnchantments.FUEL_SAVING);
        IFuel.decreaseFuel(itemStack, amount);
    }

    default void setTagTick(ItemStack stack){
        if (!CHItemData.hasData(stack)){

            CHItemData.putInt(stack, FUEL_AMOUNT, 0);
            CHItemData.putInt(stack, MAX_FUEL_AMOUNT, this.getMaxFuel());
        }
        if (!CHItemData.contains(stack, MAX_FUEL_AMOUNT)){

            CHItemData.putInt(stack, MAX_FUEL_AMOUNT, this.getMaxFuel());
        }
        if (CHItemData.getInt(stack, FUEL_AMOUNT) > CHItemData.getInt(stack, MAX_FUEL_AMOUNT)){
            CHItemData.putInt(stack, FUEL_AMOUNT, CHItemData.getInt(stack, MAX_FUEL_AMOUNT));
        }
        if (CHItemData.getInt(stack, FUEL_AMOUNT) < 0){
            CHItemData.putInt(stack, FUEL_AMOUNT, 0);
        }
    }

    static boolean isFull(ItemStack itemStack) {
        if (!CHItemData.hasData(itemStack)){
            return false;
        }
        int fuel = CHItemData.getInt(itemStack, FUEL_AMOUNT);
        int maxFuel = CHItemData.getInt(itemStack, MAX_FUEL_AMOUNT);
        return fuel >= maxFuel;
    }

    static boolean isEmpty(ItemStack itemStack) {
        if (!CHItemData.hasData(itemStack)){
            return true;
        }
        int fuel = CHItemData.getInt(itemStack, FUEL_AMOUNT);
        return fuel <= 0;
    }

    static int currentFuel(ItemStack itemStack){
        if (CHItemData.hasData(itemStack)){
            return CHItemData.getInt(itemStack, FUEL_AMOUNT);
        } else {
            return 0;
        }
    }

    static int maximumFuel(ItemStack itemStack){
        if (CHItemData.hasData(itemStack)){
            return CHItemData.getInt(itemStack, MAX_FUEL_AMOUNT);
        } else {
            return 0;
        }
    }

    static void setFuel(ItemStack itemStack, int fuel){
        if (!(itemStack.getItem() instanceof IFuel)) {
            return;
        }
        CHItemData.putInt(itemStack, FUEL_AMOUNT, fuel);
    }

    static void setMaxFuelAmount(ItemStack itemStack, int fuel){
        if (!(itemStack.getItem() instanceof IFuel)) {
            return;
        }
        CHItemData.putInt(itemStack, MAX_FUEL_AMOUNT, fuel);
    }

    static void fillUpItem(ItemStack itemStack, int fuel) {
        if (!(itemStack.getItem() instanceof IFuel) || !CHItemData.hasData(itemStack)) {
            return;
        }
        int currentEnergy = CHItemData.getInt(itemStack, FUEL_AMOUNT);
        if (!isFull(itemStack)) {
            int finalCount = Math.min(currentEnergy + fuel, maximumFuel(itemStack));
            CHItemData.putInt(itemStack, FUEL_AMOUNT, finalCount);
        }
    }

    static void decreaseFuel(ItemStack itemStack, int fuel) {
        if (!(itemStack.getItem() instanceof IFuel) || !CHItemData.hasData(itemStack)) {
            return;
        }
        int currentFuel = CHItemData.getInt(itemStack, FUEL_AMOUNT);
        if (!isEmpty(itemStack)) {
            int finalCount = Math.max(currentFuel - fuel, 0);
            CHItemData.putInt(itemStack, FUEL_AMOUNT, finalCount);
        }
    }

    default void addFuelText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (CHItemData.hasData(stack)) {
            tooltip.add(Component.empty());
            int energy = CHItemData.getInt(stack, FUEL_AMOUNT);
            int maxEnergy = CHItemData.getInt(stack, MAX_FUEL_AMOUNT);
            tooltip.add(Component.translatable("info.clanginghowl.fuel.amount").append(Component.literal(" ")).append(Component.translatable("info.clanginghowl.energy.number", energy, maxEnergy).withStyle(ChatFormatting.GRAY)));
        }
    }
}

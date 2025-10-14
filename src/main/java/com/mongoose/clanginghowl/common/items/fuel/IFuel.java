package com.mongoose.clanginghowl.common.items.fuel;

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
        int amount = this.getConsumption(itemStack) - itemStack.getEnchantmentLevel(CHEnchantments.FUEL_SAVING.get());
        IFuel.decreaseFuel(itemStack, amount);
    }

    default void setTagTick(ItemStack stack){
        if (stack.getTag() == null){
            CompoundTag compound = stack.getOrCreateTag();
            compound.putInt(FUEL_AMOUNT, 0);
            compound.putInt(MAX_FUEL_AMOUNT, this.getMaxFuel());
        }
        if (!stack.getTag().contains(MAX_FUEL_AMOUNT)){
            CompoundTag compound = stack.getOrCreateTag();
            compound.putInt(MAX_FUEL_AMOUNT, this.getMaxFuel());
        }
        if (stack.getTag().getInt(FUEL_AMOUNT) > stack.getTag().getInt(MAX_FUEL_AMOUNT)){
            stack.getTag().putInt(FUEL_AMOUNT, stack.getTag().getInt(MAX_FUEL_AMOUNT));
        }
        if (stack.getTag().getInt(FUEL_AMOUNT) < 0){
            stack.getTag().putInt(FUEL_AMOUNT, 0);
        }
    }

    static boolean isFull(ItemStack itemStack) {
        if (itemStack.getTag() == null){
            return false;
        }
        int fuel = itemStack.getTag().getInt(FUEL_AMOUNT);
        int maxFuel = itemStack.getTag().getInt(MAX_FUEL_AMOUNT);
        return fuel >= maxFuel;
    }

    static boolean isEmpty(ItemStack itemStack) {
        if (itemStack.getTag() == null){
            return true;
        }
        int fuel = itemStack.getTag().getInt(FUEL_AMOUNT);
        return fuel <= 0;
    }

    static int currentFuel(ItemStack itemStack){
        if (itemStack.getTag() != null){
            return itemStack.getTag().getInt(FUEL_AMOUNT);
        } else {
            return 0;
        }
    }

    static int maximumFuel(ItemStack itemStack){
        if (itemStack.getTag() != null){
            return itemStack.getTag().getInt(MAX_FUEL_AMOUNT);
        } else {
            return 0;
        }
    }

    static void setFuel(ItemStack itemStack, int fuel){
        if (!(itemStack.getItem() instanceof IFuel)) {
            return;
        }
        itemStack.getOrCreateTag().putInt(FUEL_AMOUNT, fuel);
    }

    static void setMaxFuelAmount(ItemStack itemStack, int fuel){
        if (!(itemStack.getItem() instanceof IFuel)) {
            return;
        }
        itemStack.getOrCreateTag().putInt(MAX_FUEL_AMOUNT, fuel);
    }

    static void fillUpItem(ItemStack itemStack, int fuel) {
        if (!(itemStack.getItem() instanceof IFuel) || itemStack.getTag() == null) {
            return;
        }
        int currentEnergy = itemStack.getTag().getInt(FUEL_AMOUNT);
        if (!isFull(itemStack)) {
            int finalCount = Math.min(currentEnergy + fuel, maximumFuel(itemStack));
            itemStack.getOrCreateTag().putInt(FUEL_AMOUNT, finalCount);
        }
    }

    static void decreaseFuel(ItemStack itemStack, int fuel) {
        if (!(itemStack.getItem() instanceof IFuel) || itemStack.getTag() == null) {
            return;
        }
        int currentFuel = itemStack.getTag().getInt(FUEL_AMOUNT);
        if (!isEmpty(itemStack)) {
            int finalCount = Math.max(currentFuel - fuel, 0);
            itemStack.getOrCreateTag().putInt(FUEL_AMOUNT, finalCount);
        }
    }

    default void addFuelText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.getTag() != null) {
            tooltip.add(Component.empty());
            int energy = stack.getTag().getInt(FUEL_AMOUNT);
            int maxEnergy = stack.getTag().getInt(MAX_FUEL_AMOUNT);
            tooltip.add(Component.translatable("info.clanginghowl.fuel.amount").append(Component.literal(" ")).append(Component.translatable("info.clanginghowl.energy.number", energy, maxEnergy).withStyle(ChatFormatting.GRAY)));
        }
    }
}

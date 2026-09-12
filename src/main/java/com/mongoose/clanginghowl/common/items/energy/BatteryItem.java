package com.mongoose.clanginghowl.common.items.energy;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatteryItem extends EnergyItem {
    public int maxBattery;
    public boolean canCharge;
    public boolean persistant;

    public BatteryItem(Properties properties, int maxBattery, boolean canCharge, boolean persistant) {
        super(properties);
        this.maxBattery = maxBattery;
        this.canCharge = canCharge;
        this.persistant = persistant;
    }

    public BatteryItem(Properties properties, int maxBattery) {
        this(properties, maxBattery, false, false);
    }

    @Override
    public boolean canCharge() {
        return this.canCharge;
    }

    public boolean isPersistant() {
        return this.persistant;
    }

    public static boolean isPersistant(ItemStack itemStack) {
        return itemStack.getItem() instanceof BatteryItem batteryItem && batteryItem.isPersistant();
    }

    @Override
    public int getMaxEnergy() {
        return this.maxBattery;
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        this.addEnergyText(stack, tooltipContext, tooltip, flagIn);
    }
}

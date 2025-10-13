package com.mongoose.clanginghowl.common.items.energy;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatteryItem extends EnergyItem {
    public int maxBattery;

    public BatteryItem(Properties properties, int maxBattery) {
        super(properties);
        this.maxBattery = maxBattery;
    }

    @Override
    public int getMaxEnergy() {
        return this.maxBattery;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        this.addEnergyText(stack, worldIn, tooltip, flagIn);
    }
}

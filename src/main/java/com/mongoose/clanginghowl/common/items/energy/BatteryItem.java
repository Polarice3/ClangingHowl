package com.mongoose.clanginghowl.common.items.energy;

import net.minecraft.world.item.Item;

public class BatteryItem extends EnergyItem {
    public int maxBattery;

    public BatteryItem(int maxBattery) {
        super(new Item.Properties().stacksTo(1));
        this.maxBattery = maxBattery;
    }

    @Override
    public int getMaxEnergy() {
        return this.maxBattery;
    }
}

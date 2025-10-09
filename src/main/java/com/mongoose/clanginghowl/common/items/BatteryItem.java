package com.mongoose.clanginghowl.common.items;

import net.minecraft.world.item.Item;

public class BatteryItem extends Item implements IEnergyItem {
    public BatteryItem() {
        super(new Item.Properties());
    }

    @Override
    public int getMaxEnergy() {
        return 0;
    }
}

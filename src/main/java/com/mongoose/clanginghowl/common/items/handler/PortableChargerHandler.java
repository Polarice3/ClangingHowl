package com.mongoose.clanginghowl.common.items.handler;

import com.mongoose.clanginghowl.common.items.energy.BatteryItem;
import com.mongoose.clanginghowl.common.items.energy.PortableChargerItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ComponentItemHandler;

/** Inventory contents are saved and synchronized by the stack's container component. */
public final class PortableChargerHandler extends ComponentItemHandler {
    public PortableChargerHandler(ItemStack stack) {
        super(stack, DataComponents.CONTAINER, 6);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof BatteryItem;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 6;
    }

    public static PortableChargerHandler get(ItemStack stack) {
        if (!(stack.getItem() instanceof PortableChargerItem)) {
            throw new IllegalArgumentException("Expected a portable charger stack");
        }
        return new PortableChargerHandler(stack);
    }
}

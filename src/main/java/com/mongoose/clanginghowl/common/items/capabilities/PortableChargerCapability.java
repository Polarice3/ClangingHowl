package com.mongoose.clanginghowl.common.items.capabilities;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.blocks.entities.CHBlockEntities;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.handler.PortableChargerHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/** Expose the component inventory and charging station to automation. */
@EventBusSubscriber(modid = ClangingHowl.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class PortableChargerCapability {
    private PortableChargerCapability() {}

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.ItemHandler.ITEM,
                (stack, ignored) -> PortableChargerHandler.get(stack), CHItems.PORTABLE_CHARGER.get());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                CHBlockEntities.STATIONARY_CHARGING_STATION.get(), (station, side) -> station.itemStackHandler);
    }
}

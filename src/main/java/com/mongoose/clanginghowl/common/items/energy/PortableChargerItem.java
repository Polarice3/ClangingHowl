package com.mongoose.clanginghowl.common.items.energy;

import com.mongoose.clanginghowl.client.inventory.menu.PortableChargerMenu;
import com.mongoose.clanginghowl.common.items.handler.PortableChargerHandler;
import com.mongoose.clanginghowl.utils.CHCuriosFinder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import java.util.List;

public class PortableChargerItem extends Item {
    public PortableChargerItem() {
        super(new Properties().setNoRepair().rarity(Rarity.RARE).stacksTo(1)
                .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (level.isClientSide || !(entity instanceof Player player)) return;

        PortableChargerHandler handler = PortableChargerHandler.get(stack);
        for (int batterySlot = 0; batterySlot < handler.getSlots(); ++batterySlot) {
            // Component inventories return snapshots. Mutate a copy and explicitly write it back.
            ItemStack battery = handler.getStackInSlot(batterySlot).copy();
            if (!(battery.getItem() instanceof BatteryItem batteryItem)) continue;
            if (IEnergyItem.isEmpty(battery)) {
                if (!batteryItem.isPersistant()) {
                    battery.shrink(1);
                    handler.setStackInSlot(batterySlot, battery);
                }
                continue;
            }
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                charge(player.getInventory().getItem(i), battery);
            }
            for (ItemStack curio : CHCuriosFinder.getCurioList(player, item -> item instanceof IEnergyItem)) {
                charge(curio, battery);
            }
            handler.setStackInSlot(batterySlot, battery);
            break;
        }
    }

    private static void charge(ItemStack target, ItemStack battery) {
        if (!target.isEmpty() && target.getItem() instanceof IEnergyItem item
                && item.canCharge() && !IEnergyItem.isFull(target)) {
            IEnergyItem.chargeEnergy(target, battery);
        }
    }

    public static boolean hasBatteries(ItemStack stack) {
        PortableChargerHandler handler = PortableChargerHandler.get(stack);
        for (int i = 0; i < handler.getSlots(); ++i) {
            ItemStack battery = handler.getStackInSlot(i);
            if (battery.getItem() instanceof BatteryItem && !IEnergyItem.isEmpty(battery)) return true;
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer) {
            SimpleMenuProvider provider = new SimpleMenuProvider(
                    (id, inventory, owner) -> new PortableChargerMenu(id, inventory,
                            PortableChargerHandler.get(stack), stack), getName(stack));
            serverPlayer.openMenu(provider, buffer -> {});
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("info.clanginghowl.item.charger.0"));
        tooltip.add(Component.translatable("info.clanginghowl.item.charger.1"));
    }
}

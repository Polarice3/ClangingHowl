package com.mongoose.clanginghowl.client.inventory.menu;

import com.mongoose.clanginghowl.common.items.handler.PortableChargerHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class PortableChargerMenu extends AbstractContainerMenu {
    private final ItemStack stack;

    public static PortableChargerMenu createContainerClientSide(int id, Inventory inventory, FriendlyByteBuf buffer) {
        return new PortableChargerMenu(id, inventory, new PortableChargerHandler(ItemStack.EMPTY), ItemStack.EMPTY);
    }

    public PortableChargerMenu(int id, Inventory playerInventory, PortableChargerHandler handler, ItemStack stack) {
        super(CHMenuTypes.PORTABLE_CHARGER.get(), id);
        this.stack = stack;
        for (int i = 0; i < 3; i++) {
            addSlot(new SlotItemHandler(handler, i, 80 - 18 + i * 18, 37));
        }
        for (int i = 0; i < 3; i++) {
            addSlot(new SlotItemHandler(handler, 3 + i, 80 - 18 + i * 18, 55));
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < 6) {
                if (!this.moveItemStackTo(itemstack1, 6, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 6, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return !stack.isEmpty();
    }
}

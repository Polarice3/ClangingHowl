package com.mongoose.clanginghowl.common.items.handler;

import com.mongoose.clanginghowl.common.items.energy.BatteryItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class PortableChargerHandler extends ItemStackHandler {
    private final ItemStack itemStack;

    public PortableChargerHandler(ItemStack itemStack) {
        super(6);
        this.itemStack = itemStack;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof BatteryItem;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 6;
    }

    public NonNullList<ItemStack> getContents(){
        return stacks;
    }

    @Override
    protected void onContentsChanged(int slot) {
        CompoundTag nbt = itemStack.getOrCreateTag();
        nbt.putBoolean("ch-dirty", !nbt.getBoolean("ch-dirty"));
    }

    public static PortableChargerHandler get(ItemStack stack) {
        IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .orElseThrow(() -> new IllegalArgumentException("ItemStack is missing item capability"));
        return (PortableChargerHandler) handler;
    }
}

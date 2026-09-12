package com.mongoose.clanginghowl.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/** Scalar custom data with copy-on-write updates and allocation-free read access. */
public final class CHItemData {
    private CHItemData() {}

    public static boolean hasData(ItemStack stack) {
        return stack.has(DataComponents.CUSTOM_DATA);
    }

    public static boolean contains(ItemStack stack, String key) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains(key);
    }

    // This reference never leaves the helper and is used only for scalar reads.
    @SuppressWarnings("deprecation")
    private static CompoundTag read(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).getUnsafe();
    }

    public static int getInt(ItemStack stack, String key) {
        return read(stack).getInt(key);
    }

    public static boolean getBoolean(ItemStack stack, String key) {
        return read(stack).getBoolean(key);
    }

    public static void putInt(ItemStack stack, String key, int value) {
        if (read(stack).contains(key, Tag.TAG_INT) && getInt(stack, key) == value) return;
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(key, value));
    }

    public static void putBoolean(ItemStack stack, String key, boolean value) {
        if (read(stack).contains(key, Tag.TAG_BYTE) && getBoolean(stack, key) == value) return;
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(key, value));
    }
}

package com.mongoose.clanginghowl.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class NapalmStreamEnchantment extends Enchantment {
    public NapalmStreamEnchantment(Rarity p_44676_, EquipmentSlot... p_44678_) {
        super(p_44676_, CHEnchantments.FUEL, p_44678_);
    }

    public int getMinCost(int p_45000_) {
        return 10 + 20 * (p_45000_ - 1);
    }

    public int getMaxCost(int p_45002_) {
        return super.getMinCost(p_45002_) + 50;
    }

    public int getMaxLevel() {
        return 3;
    }
}

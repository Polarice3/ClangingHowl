package com.mongoose.clanginghowl.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class FuelTreasureEnchantment extends Enchantment {

    public FuelTreasureEnchantment(Rarity p_44676_, EquipmentSlot... p_44678_) {
        super(p_44676_, CHEnchantments.FUEL, p_44678_);
    }

    public int getMinCost(int p_45102_) {
        return p_45102_ * 25;
    }

    public int getMaxCost(int p_45105_) {
        return this.getMinCost(p_45105_) + 50;
    }

    public boolean isTreasureOnly() {
        return true;
    }
}

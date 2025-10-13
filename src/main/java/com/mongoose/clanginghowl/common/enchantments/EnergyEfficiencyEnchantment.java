package com.mongoose.clanginghowl.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnergyEfficiencyEnchantment extends Enchantment {

    public EnergyEfficiencyEnchantment(Rarity p_44676_, EquipmentSlot... p_44678_) {
        super(p_44676_, CHEnchantments.ENERGY, p_44678_);
    }

    public int getMinCost(int p_44652_) {
        return 5 + (p_44652_ - 1) * 8;
    }

    public int getMaxCost(int p_44660_) {
        return super.getMinCost(p_44660_) + 50;
    }

    public int getMaxLevel() {
        return 3;
    }
}

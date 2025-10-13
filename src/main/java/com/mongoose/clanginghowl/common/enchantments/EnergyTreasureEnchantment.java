package com.mongoose.clanginghowl.common.enchantments;

import com.mongoose.clanginghowl.common.items.energy.ChainsawItem;
import com.mongoose.clanginghowl.common.items.energy.ChainswordItem;
import com.mongoose.clanginghowl.common.items.energy.DrillItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class EnergyTreasureEnchantment extends Enchantment {

    public EnergyTreasureEnchantment(Rarity p_44676_, EquipmentSlot... p_44678_) {
        super(p_44676_, CHEnchantments.ENERGY, p_44678_);
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

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        if (this == CHEnchantments.TUNNEL_DRILLER.get()) {
            return enchantment != Enchantments.BLOCK_FORTUNE && enchantment != Enchantments.SILK_TOUCH;
        }
        return super.checkCompatibility(enchantment);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        if (this == CHEnchantments.TUNNEL_DRILLER.get()) {
            return stack.getItem() instanceof DrillItem;
        } else if (this == CHEnchantments.OVERDRIVE.get()) {
            return stack.getItem() instanceof ChainsawItem;
        } else if (this == CHEnchantments.KILLER_CHARGE.get()) {
            return stack.getItem() instanceof ChainswordItem;
        }
        return super.canApplyAtEnchantingTable(stack);
    }
}

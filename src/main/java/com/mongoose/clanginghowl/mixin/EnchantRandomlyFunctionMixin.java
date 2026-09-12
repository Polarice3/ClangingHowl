package com.mongoose.clanginghowl.mixin;

import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import net.minecraft.core.Holder;
import java.util.ArrayList;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(EnchantRandomlyFunction.class)
public class EnchantRandomlyFunctionMixin {
    @org.spongepowered.asm.mixin.Shadow @org.spongepowered.asm.mixin.Final
    private java.util.Optional<net.minecraft.core.HolderSet<Enchantment>> options;

    @ModifyVariable(method = "run", at = @At("STORE"))
    private List<Holder<Enchantment>> filterEnchants(List<Holder<Enchantment>> enchantments) {
        if (this.options.isPresent()) return enchantments;
        List<Holder<Enchantment>> allowed = new ArrayList<>(enchantments);
        allowed.removeIf(CHEnchantments::excludedFromRandomLoot);
        return allowed;
    }
}

package com.mongoose.clanginghowl.common.enchantments;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.List;

/** Enchantments are world/datapack registry entries in Minecraft 1.21.1. */
public final class CHEnchantments {
    private CHEnchantments() {}
    public static final ResourceKey<Enchantment> ENERGY_EFFICIENCY = key("energy_efficiency");
    public static final ResourceKey<Enchantment> ECOLOGICAL_ENERGY = key("ecological_energy");
    public static final ResourceKey<Enchantment> TUNNEL_DRILLER = key("tunnel_driller");
    public static final ResourceKey<Enchantment> OVERDRIVE = key("overdrive");
    public static final ResourceKey<Enchantment> FULL_POWER = key("full_power");
    public static final ResourceKey<Enchantment> KILLER_CHARGE = key("killer_charge");
    public static final ResourceKey<Enchantment> EXCEEDING_THE_LIMIT = key("exceeding_the_limit");
    public static final ResourceKey<Enchantment> FUEL_SAVING = key("fuel_saving");
    public static final ResourceKey<Enchantment> NAPALM_STREAM = key("napalm_stream");
    public static final ResourceKey<Enchantment> CHAIN_BURN = key("chain_burn");
    public static final ResourceKey<Enchantment> FUEL_BURST = key("fuel_burst");
    public static final ResourceKey<Enchantment> SOUL_BURNER = key("soul_burner");
    public static final List<ResourceKey<Enchantment>> ALL = List.of(
            ENERGY_EFFICIENCY, ECOLOGICAL_ENERGY, TUNNEL_DRILLER, OVERDRIVE, FULL_POWER, KILLER_CHARGE, EXCEEDING_THE_LIMIT, FUEL_SAVING, NAPALM_STREAM, CHAIN_BURN, FUEL_BURST, SOUL_BURNER);

    private static ResourceKey<Enchantment> key(String path) {
        return ResourceKey.create(Registries.ENCHANTMENT, ClangingHowl.location(path));
    }

    /** Resolve by the holders already on the stack, without a global registry cache. */
    public static int level(ItemStack stack, ResourceKey<Enchantment> key) {
        for (Holder<Enchantment> holder : stack.getEnchantments().keySet()) {
            if (holder.is(key)) return stack.getEnchantmentLevel(holder);
        }
        return 0;
    }

    public static boolean excludedFromRandomLoot(Holder<Enchantment> holder) {
        return holder.is(ECOLOGICAL_ENERGY) || holder.is(TUNNEL_DRILLER) || holder.is(OVERDRIVE) || holder.is(FULL_POWER) || holder.is(KILLER_CHARGE) || holder.is(EXCEEDING_THE_LIMIT);
    }
}

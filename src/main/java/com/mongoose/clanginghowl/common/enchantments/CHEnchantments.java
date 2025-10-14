package com.mongoose.clanginghowl.common.enchantments;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.items.energy.BatteryItem;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import com.mongoose.clanginghowl.common.items.fuel.IFuel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEnchantments {
    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ClangingHowl.MOD_ID);

    public static final EnchantmentCategory ENERGY = EnchantmentCategory.create("ch_energy", (item) -> (item instanceof IEnergyItem && !(item instanceof BatteryItem)));
    public static final EnchantmentCategory FUEL = EnchantmentCategory.create("ch_fuel", (item) -> item instanceof IFuel);

    public static final RegistryObject<Enchantment> ENERGY_EFFICIENCY = ENCHANTMENTS.register("energy_efficiency",
            () -> new EnergyEfficiencyEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));

    public static final RegistryObject<Enchantment> ECOLOGICAL_ENERGY = ENCHANTMENTS.register("ecological_energy",
            () -> new EnergyTreasureEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

    public static final RegistryObject<Enchantment> TUNNEL_DRILLER = ENCHANTMENTS.register("tunnel_driller",
            () -> new EnergyTreasureEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

    public static final RegistryObject<Enchantment> OVERDRIVE = ENCHANTMENTS.register("overdrive",
            () -> new EnergyTreasureEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

    public static final RegistryObject<Enchantment> KILLER_CHARGE = ENCHANTMENTS.register("killer_charge",
            () -> new EnergyTreasureEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

    public static final RegistryObject<Enchantment> FUEL_SAVING = ENCHANTMENTS.register("fuel_saving",
            () -> new FuelSavingEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));

    public static final RegistryObject<Enchantment> NAPALM_STREAM = ENCHANTMENTS.register("napalm_stream",
            () -> new NapalmStreamEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND));

    public static final RegistryObject<Enchantment> CHAIN_BURN = ENCHANTMENTS.register("chain_burn",
            () -> new FuelTreasureEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

    public static final RegistryObject<Enchantment> FUEL_BURST = ENCHANTMENTS.register("fuel_burst",
            () -> new FuelTreasureEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

}

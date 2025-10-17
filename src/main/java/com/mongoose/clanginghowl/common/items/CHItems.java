package com.mongoose.clanginghowl.common.items;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.items.energy.*;
import com.mongoose.clanginghowl.common.items.fuel.FlamethrowerItem;
import com.mongoose.clanginghowl.common.items.fuel.IFuel;
import com.mongoose.clanginghowl.utils.ItemHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

public class CHItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ClangingHowl.MOD_ID);

    public static void init(){
        CHItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Item> EXTRATERRESTRIAL_STEEL = ITEMS.register("extraterrestrial_steel",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PIECE_OF_EXTRATERRESTRIAL_STEEL = ITEMS.register("piece_of_extraterrestrial_steel",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EXTRATERRESTRIAL_STEEL_INGOT = ITEMS.register("extraterrestrial_steel_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EXTRATERRESTRIAL_STEEL_NUGGET = ITEMS.register("extraterrestrial_steel_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EXTRATERRESTRIAL_STEEL_PLATE = ITEMS.register("extraterrestrial_steel_plate",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EXTRATERRESTRIAL_ENERGY_CRYSTAL = ITEMS.register("extraterrestrial_energy_crystal",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BATTERY_PANEL = ITEMS.register("battery_panel",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CRYOGENIC_FUEL = ITEMS.register("cryogenic_fuel",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLAZE_FUEL = ITEMS.register("blaze_fuel",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLAZE_FUEL_CYLINDER = ITEMS.register("blaze_fuel_cylinder",
            () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRILL_BIT = ITEMS.register("drill_bit",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHAINSAW_TEETH = ITEMS.register("chainsaw_teeth",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REDSTONE_WIRE = ITEMS.register("redstone_wire",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_DIODE = ITEMS.register("diamond_diode",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));

    //Batteries
    public static final RegistryObject<EnergyItem> ENERGY_BATTERY = ITEMS.register("energy_battery",
            () -> new BatteryItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), 2000));
    public static final RegistryObject<EnergyItem> ENERGY_INTENSIVE_BATTERY = ITEMS.register("energy_intensive_battery",
            () -> new BatteryItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 7000));

    //Tools
    public static final RegistryObject<Item> EXTRATERRESTRIAL_SWORD = ITEMS.register("extraterrestrial_sword", () -> new SwordItem(CHTiers.EXTRATERRESTRIAL, 3, -2.4F, new Item.Properties()){
        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        }

        public void addInformationAfterShift(List<Component> tooltip) {
            tooltip.add(Component.translatable("info.clanginghowl.item.extraterrestrial"));
        }
    });
    public static final RegistryObject<Item> EXTRATERRESTRIAL_SHOVEL = ITEMS.register("extraterrestrial_shovel", () -> new ShovelItem(CHTiers.EXTRATERRESTRIAL, 1.5F, -3.0F, new Item.Properties()){
        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        }

        public void addInformationAfterShift(List<Component> tooltip) {
            tooltip.add(Component.translatable("info.clanginghowl.item.extraterrestrial"));
        }
    });
    public static final RegistryObject<Item> EXTRATERRESTRIAL_PICKAXE = ITEMS.register("extraterrestrial_pickaxe", () -> new PickaxeItem(CHTiers.EXTRATERRESTRIAL, 1, -2.8F, new Item.Properties()){
        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        }

        public void addInformationAfterShift(List<Component> tooltip) {
            tooltip.add(Component.translatable("info.clanginghowl.item.extraterrestrial"));
        }
    });
    public static final RegistryObject<Item> EXTRATERRESTRIAL_AXE = ITEMS.register("extraterrestrial_axe", () -> new AxeItem(CHTiers.EXTRATERRESTRIAL, 5.0F, -3.0F, new Item.Properties()){
        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        }

        public void addInformationAfterShift(List<Component> tooltip) {
            tooltip.add(Component.translatable("info.clanginghowl.item.extraterrestrial"));
        }
    });
    public static final RegistryObject<Item> EXTRATERRESTRIAL_HOE = ITEMS.register("extraterrestrial_hoe", () -> new HoeItem(CHTiers.EXTRATERRESTRIAL, -3, 0.0F, new Item.Properties()){
        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        }

        public void addInformationAfterShift(List<Component> tooltip) {
            tooltip.add(Component.translatable("info.clanginghowl.item.extraterrestrial"));
        }
    });
    public static final RegistryObject<Item> EXTRATERRESTRIAL_HAMMER = ITEMS.register("extraterrestrial_hammer", ExHammerItem::new);

    public static final RegistryObject<Item>  INDUSTRIAL_ADJUSTABLE_WRENCH = ITEMS.register("industrial_adjustable_wrench", WrenchItem::new);
    public static final RegistryObject<Item> BLAZE_BURNER = ITEMS.register("blaze_burner", BlazeBurnerItem::new);
    public static final RegistryObject<DrillItem>  ADVANCED_HAND_DRILL = ITEMS.register("advanced_hand_drill", DrillItem::new);
    public static final RegistryObject<ChainsawItem>  ADVANCED_CHAINSAW = ITEMS.register("advanced_chainsaw", ChainsawItem::new);
    public static final RegistryObject<ChainswordItem>  ADVANCED_CHAINSWORD = ITEMS.register("advanced_chainsword", ChainswordItem::new);
    public static final RegistryObject<FlamethrowerItem>  FLAMETHROWER = ITEMS.register("flamethrower", FlamethrowerItem::new);

    public static final RegistryObject<Item>  PORTABLE_CHARGER = ITEMS.register("portable_charger", PortableChargerItem::new);

    public static final RegistryObject<ForgeSpawnEggItem> HEART_OF_DECAY_SPAWN_EGG = ITEMS.register("heart_of_decay_spawn_egg",
            () -> new ForgeSpawnEggItem(CHEntityType.HEART_OF_DECAY, 0x161f2c, 0xc362d9, new Item.Properties()));

    public static final RegistryObject<ForgeSpawnEggItem> EX_REAPER_SPAWN_EGG = ITEMS.register("extraterrestrial_reaper_spawn_egg",
            () -> new ForgeSpawnEggItem(CHEntityType.EX_REAPER, 0x621224, 0xbc636b, new Item.Properties()));

    public static boolean shouldSkipCreativeModTab(Item item) {
        return item instanceof EnergyItem || item instanceof IFuel;
    }
}

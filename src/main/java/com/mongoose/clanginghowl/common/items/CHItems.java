package com.mongoose.clanginghowl.common.items;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
    public static final RegistryObject<Item> ENERGY_BATTERY = ITEMS.register("energy_battery",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ENERGY_INTENSIVE_BATTERY = ITEMS.register("energy_intensive_battery",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BATTERY_PANEL = ITEMS.register("battery_panel",
            () -> new Item(new Item.Properties()));

    //Tools
    public static final RegistryObject<Item> EXTRATERRESTRIAL_SWORD = ITEMS.register("extraterrestrial_sword", () -> new SwordItem(CHTiers.EXTRATERRESTRIAL, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> EXTRATERRESTRIAL_SHOVEL = ITEMS.register("extraterrestrial_shovel", () -> new ShovelItem(CHTiers.EXTRATERRESTRIAL, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> EXTRATERRESTRIAL_PICKAXE = ITEMS.register("extraterrestrial_pickaxe", () -> new PickaxeItem(CHTiers.EXTRATERRESTRIAL, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> EXTRATERRESTRIAL_AXE = ITEMS.register("extraterrestrial_axe", () -> new AxeItem(CHTiers.EXTRATERRESTRIAL, 5.0F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> EXTRATERRESTRIAL_HOE = ITEMS.register("extraterrestrial_hoe", () -> new HoeItem(CHTiers.EXTRATERRESTRIAL, -3, 0.0F, new Item.Properties()));
    public static final RegistryObject<Item> EXTRATERRESTRIAL_HAMMER = ITEMS.register("extraterrestrial_hammer", ExHammerItem::new);
}

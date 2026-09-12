package com.mongoose.clanginghowl.common.items;

import net.minecraft.core.registries.BuiltInRegistries;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.items.curios.*;
import com.mongoose.clanginghowl.common.items.energy.*;
import com.mongoose.clanginghowl.common.items.fuel.FlamethrowerItem;
import com.mongoose.clanginghowl.common.items.fuel.IFuel;
import com.mongoose.clanginghowl.utils.ItemHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.List;

public class CHItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, ClangingHowl.MOD_ID);

    public static void init(IEventBus modEventBus){
        CHItems.ITEMS.register(modEventBus);
    }

    public static final DeferredHolder<Item, Item> EXTRATERRESTRIAL_STEEL = ITEMS.register("extraterrestrial_steel",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PIECE_OF_EXTRATERRESTRIAL_STEEL = ITEMS.register("piece_of_extraterrestrial_steel",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EXTRATERRESTRIAL_STEEL_INGOT = ITEMS.register("extraterrestrial_steel_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EXTRATERRESTRIAL_STEEL_NUGGET = ITEMS.register("extraterrestrial_steel_nugget",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EXTRATERRESTRIAL_STEEL_PLATE = ITEMS.register("extraterrestrial_steel_plate",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> EXTRATERRESTRIAL_ENERGY_CRYSTAL = ITEMS.register("extraterrestrial_energy_crystal",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BATTERY_PANEL = ITEMS.register("battery_panel",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> CRYOGENIC_FUEL = ITEMS.register("cryogenic_fuel",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BLAZE_FUEL = ITEMS.register("blaze_fuel",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BLAZE_FUEL_CYLINDER = ITEMS.register("blaze_fuel_cylinder",
            () -> new BlockItem(CHBlocks.BLAZE_FUEL_CYLINDER_BLOCK.get(), new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> DRILL_BIT = ITEMS.register("drill_bit",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CHAINSAW_TEETH = ITEMS.register("chainsaw_teeth",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> REDSTONE_WIRE = ITEMS.register("redstone_wire",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> DIAMOND_DIODE = ITEMS.register("diamond_diode",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> CHUNK_OF_TECHNOFLESH = ITEMS.register("chunk_of_technoflesh",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> TECHNO_OPTICS = ITEMS.register("techno_optics",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> TECHNO_HEART = ITEMS.register("techno_heart",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> HEMATOMA_LUMP = ITEMS.register("hematoma_lump",
            () -> new CHBaseItem(new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().effect(() -> new MobEffectInstance(CHEffects.ATTRACTION, 300), 1.0F).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 300), 1.0F).alwaysEdible().nutrition(1).saturationModifier(1.0F).build())));
    public static final DeferredHolder<Item, Item> ENERGY_FIBER = ITEMS.register("energy_fiber",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ADVANCED_ELECTRONICS = ITEMS.register("advanced_electronics",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> NETHERRACK_SHAVINGS = ITEMS.register("netherrack_shavings",
            () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FIREPROOF_STEEL_COATING = ITEMS.register("fireproof_steel_coating",
            () -> new Item(new Item.Properties().fireResistant()));

    //Batteries
    public static final DeferredHolder<Item, EnergyItem> SMALL_ENERGY_BATTERY = ITEMS.register("small_energy_battery",
            () -> new BatteryItem(new Item.Properties().rarity(Rarity.UNCOMMON), 500));
    public static final DeferredHolder<Item, EnergyItem> ENERGY_BATTERY = ITEMS.register("energy_battery",
            () -> new BatteryItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), 2000));
    public static final DeferredHolder<Item, EnergyItem> ENERGY_INTENSIVE_BATTERY = ITEMS.register("energy_intensive_battery",
            () -> new BatteryItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 7000));
    public static final DeferredHolder<Item, EnergyItem> ADVANCED_ENERGY_BATTERY = ITEMS.register("advanced_energy_battery",
            () -> new BatteryItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC), 12000, true, true));

    //Tools
    public static final DeferredHolder<Item, Item> EXTRATERRESTRIAL_SWORD = ITEMS.register("extraterrestrial_sword", () -> new SwordItem(CHTiers.EXTRATERRESTRIAL, new Item.Properties().attributes(SwordItem.createAttributes(CHTiers.EXTRATERRESTRIAL, 3, -2.4F))){
        @Override
        public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
            ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        }

        public void addInformationAfterShift(List<Component> tooltip) {
            tooltip.add(Component.translatable("info.clanginghowl.item.extraterrestrial"));
        }
    });
    public static final DeferredHolder<Item, Item> EXTRATERRESTRIAL_SHOVEL = ITEMS.register("extraterrestrial_shovel", () -> new ShovelItem(CHTiers.EXTRATERRESTRIAL, new Item.Properties().attributes(DiggerItem.createAttributes(CHTiers.EXTRATERRESTRIAL, 1.5F, -3.0F))){
        @Override
        public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
            ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        }

        public void addInformationAfterShift(List<Component> tooltip) {
            tooltip.add(Component.translatable("info.clanginghowl.item.extraterrestrial"));
        }
    });
    public static final DeferredHolder<Item, Item> EXTRATERRESTRIAL_PICKAXE = ITEMS.register("extraterrestrial_pickaxe", () -> new PickaxeItem(CHTiers.EXTRATERRESTRIAL, new Item.Properties().attributes(DiggerItem.createAttributes(CHTiers.EXTRATERRESTRIAL, 1, -2.8F))){
        @Override
        public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
            ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        }

        public void addInformationAfterShift(List<Component> tooltip) {
            tooltip.add(Component.translatable("info.clanginghowl.item.extraterrestrial"));
        }
    });
    public static final DeferredHolder<Item, Item> EXTRATERRESTRIAL_AXE = ITEMS.register("extraterrestrial_axe", () -> new AxeItem(CHTiers.EXTRATERRESTRIAL, new Item.Properties().attributes(DiggerItem.createAttributes(CHTiers.EXTRATERRESTRIAL, 5.0F, -3.0F))){
        @Override
        public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
            ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        }

        public void addInformationAfterShift(List<Component> tooltip) {
            tooltip.add(Component.translatable("info.clanginghowl.item.extraterrestrial"));
        }
    });
    public static final DeferredHolder<Item, Item> EXTRATERRESTRIAL_HOE = ITEMS.register("extraterrestrial_hoe", () -> new HoeItem(CHTiers.EXTRATERRESTRIAL, new Item.Properties().attributes(DiggerItem.createAttributes(CHTiers.EXTRATERRESTRIAL, -3, 0.0F))){
        @Override
        public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
            ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        }

        public void addInformationAfterShift(List<Component> tooltip) {
            tooltip.add(Component.translatable("info.clanginghowl.item.extraterrestrial"));
        }
    });
    public static final DeferredHolder<Item, Item> EXTRATERRESTRIAL_HAMMER = ITEMS.register("extraterrestrial_hammer", ExHammerItem::new);

    public static final DeferredHolder<Item, Item>  INDUSTRIAL_ADJUSTABLE_WRENCH = ITEMS.register("industrial_adjustable_wrench", WrenchItem::new);
    public static final DeferredHolder<Item, Item> BLAZE_BURNER = ITEMS.register("blaze_burner", BlazeBurnerItem::new);
    public static final DeferredHolder<Item, Item> ATTRACTION_DEVICE = ITEMS.register("attraction_device", AttractionDevice::new);
    public static final DeferredHolder<Item, DrillItem>  ADVANCED_HAND_DRILL = ITEMS.register("advanced_hand_drill", DrillItem::new);
    public static final DeferredHolder<Item, ChainsawItem>  ADVANCED_CHAINSAW = ITEMS.register("advanced_chainsaw", ChainsawItem::new);
    public static final DeferredHolder<Item, ChainswordItem>  ADVANCED_CHAINSWORD = ITEMS.register("advanced_chainsword", ChainswordItem::new);
    public static final DeferredHolder<Item, FlamethrowerItem>  FLAMETHROWER = ITEMS.register("flamethrower", FlamethrowerItem::new);

    public static final DeferredHolder<Item, Item> PORTABLE_CHARGER = ITEMS.register("portable_charger", PortableChargerItem::new);

    //Curios
    public static final DeferredHolder<Item, CuriosEnergyItem> X_RAY_GOGGLES = ITEMS.register("x_ray_goggles", XRayGoggles::new);
    public static final DeferredHolder<Item, CuriosEnergyItem> ENERGY_BARRIER_GENERATOR = ITEMS.register("energy_barrier_generator", EnergyBarrierGenerator::new);
    public static final DeferredHolder<Item, CuriosEnergyItem> TENDON_STRENGTHENER = ITEMS.register("tendon_strengthener", TendonStrengthener::new);
    public static final DeferredHolder<Item, CuriosEnergyItem> ENERGY_GLOVE = ITEMS.register("energy_glove", EnergyGlove::new);
    public static final DeferredHolder<Item, CuriosFuelItem> JET_BOOTS = ITEMS.register("jet_boots", JetBoots::new);
    public static final DeferredHolder<Item, CHCurioItem> BLOODY_BATTERY = ITEMS.register("bloody_battery", () -> new CHCurioItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, CuriosEnergyItem> REANIMATOR = ITEMS.register("reanimator", Reanimator::new);

    //Spawn Eggs
    public static final DeferredHolder<Item, DeferredSpawnEggItem> HEART_OF_DECAY_SPAWN_EGG = ITEMS.register("heart_of_decay_spawn_egg",
            () -> new DeferredSpawnEggItem(CHEntityType.HEART_OF_DECAY, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> EX_REAPER_SPAWN_EGG = ITEMS.register("extraterrestrial_reaper_spawn_egg",
            () -> new DeferredSpawnEggItem(CHEntityType.EX_REAPER, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> FLESH_MAIDEN_SPAWN_EGG = ITEMS.register("flesh_maiden_spawn_egg",
            () -> new DeferredSpawnEggItem(CHEntityType.FLESH_MAIDEN, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> HEMATOMA_SPAWN_EGG = ITEMS.register("hematoma_spawn_egg",
            () -> new DeferredSpawnEggItem(CHEntityType.HEMATOMA, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> BLOOD_SPREADER_SPAWN_EGG = ITEMS.register("blood_spreader_spawn_egg",
            () -> new DeferredSpawnEggItem(CHEntityType.BLOOD_SPREADER, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> PROWLER_SPAWN_EGG = ITEMS.register("prowler_spawn_egg",
            () -> new DeferredSpawnEggItem(CHEntityType.PROWLER, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> CARCASS_SPAWN_EGG = ITEMS.register("carcass_spawn_egg",
            () -> new DeferredSpawnEggItem(CHEntityType.CARCASS, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    //Dummy
    public static final DeferredHolder<Item, Item> REANIMATION = ITEMS.register("reanimation", DummyItem::new);

    public static boolean shouldSkipCreativeModTab(Item item) {
        return item instanceof EnergyItem || item instanceof IFuel || item instanceof CHCurioItem || item instanceof DummyItem;
    }
}

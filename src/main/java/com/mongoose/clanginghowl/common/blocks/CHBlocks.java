package com.mongoose.clanginghowl.common.blocks;

import net.minecraft.core.registries.BuiltInRegistries;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.items.CHBlockItem;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.ExEnergyClusterItem;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CHBlocks {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, ClangingHowl.MOD_ID);
    public static final Map<ResourceLocation, BlockLootSetting> BLOCK_LOOT = new HashMap<>();

    public static void init(IEventBus modEventBus){
        CHBlocks.BLOCKS.register(modEventBus);
    }

    //Extraterrestrial Stone
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_STONE = register("extraterrestrial_stone", ExStoneBlock::new);
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_STONE_STAIRS = registerStairs("extraterrestrial_stone_stairs",
            EXTRATERRESTRIAL_STONE);
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_STONE_SLAB = registerSlabs("extraterrestrial_stone_slab",
            EXTRATERRESTRIAL_STONE);
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_STONE_WALL = registerWalls("extraterrestrial_stone_wall",
            EXTRATERRESTRIAL_STONE);

    public static final DeferredHolder<Block, Block> SMOOTH_EXTRATERRESTRIAL_STONE = register("smooth_extraterrestrial_stone", SmoothExStoneBlock::new);
    public static final DeferredHolder<Block, Block> SMOOTH_EXTRATERRESTRIAL_STONE_STAIRS = registerStairs("smooth_extraterrestrial_stone_stairs",
            SMOOTH_EXTRATERRESTRIAL_STONE);
    public static final DeferredHolder<Block, Block> SMOOTH_EXTRATERRESTRIAL_STONE_SLAB = registerSlabs("smooth_extraterrestrial_stone_slab",
            SMOOTH_EXTRATERRESTRIAL_STONE);
    public static final DeferredHolder<Block, Block> SMOOTH_EXTRATERRESTRIAL_STONE_WALL = registerWalls("smooth_extraterrestrial_stone_wall",
            SMOOTH_EXTRATERRESTRIAL_STONE);

    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_STONE_BRICKS = register("extraterrestrial_stone_bricks", ExStoneBricksBlock::new);
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_STONE_BRICK_STAIRS = registerStairs("extraterrestrial_stone_brick_stairs",
            EXTRATERRESTRIAL_STONE_BRICKS);
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_STONE_BRICK_SLAB = registerSlabs("extraterrestrial_stone_brick_slab",
            EXTRATERRESTRIAL_STONE_BRICKS);
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_STONE_BRICK_WALL = registerWalls("extraterrestrial_stone_brick_wall",
            EXTRATERRESTRIAL_STONE_BRICKS);

    public static final DeferredHolder<Block, Block> CARVED_EXTRATERRESTRIAL_STONE_BRICKS = register("carved_extraterrestrial_stone_bricks", ExStoneBricksBlock::new);
    public static final DeferredHolder<Block, Block> CHARGED_EXTRATERRESTRIAL_STONE_BRICKS = register("charged_extraterrestrial_stone_bricks", ExStoneBricksBlock::new);

    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_COLUMN = register("extraterrestrial_column", () -> pillar(smoothExStoneProperties()));

    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_PEBBLE = register("extraterrestrial_pebble", ExPebbleBlock::new);
    public static final DeferredHolder<Block, Block> BURNISHED_EXTRATERRESTRIAL_STONE = register("burnished_extraterrestrial_stone", ExStoneBlock::new);
    public static final DeferredHolder<Block, Block> INCANDESCENT_EXTRATERRESTRIAL_STONE = register("incandescent_extraterrestrial_stone", () -> new MagmaBlock(exStoneProperties().lightLevel(l -> 3).hasPostProcess(CHBlocks::always).emissiveRendering(CHBlocks::always)));

    //Ores
    public static final DeferredHolder<Block, Block> METEORITE_STEEL_ORE = register("meteorite_steel_ore", () -> new DropExperienceBlock(UniformInt.of(3, 7), exStoneProperties()), true, LootTableType.EMPTY);
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_STEEL_ORE = register("extraterrestrial_steel_ore", () -> new DropExperienceBlock(UniformInt.of(3, 7), exSteelOreProperties()), true, LootTableType.EMPTY);

    //Energy Clusters
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_ENERGY_CLUSTER = registerCluster("extraterrestrial_energy_cluster", ExEnergyClusterBlock::new);
    public static final DeferredHolder<Block, Block> HUGE_EXTRATERRESTRIAL_ENERGY_CLUSTER = register("huge_extraterrestrial_energy_cluster", HugeExEnergyClusterBlock::new, true, LootTableType.EMPTY);
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_ENERGY_CRYSTAL_BLOCK = register("extraterrestrial_energy_crystal_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).sound(SoundType.AMETHYST).strength(1.5F, 6.0F)));

    //Steel
    public static final DeferredHolder<Block, Block> RAW_EXTRATERRESTRIAL_STEEL_BLOCK = register("raw_extraterrestrial_steel_block", () -> new ExSteelBlock(Blocks.STONE));
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_STEEL_BLOCK = register("extraterrestrial_steel_block", () -> new ExSteelBlock(Blocks.NETHERITE_BLOCK));
    public static final DeferredHolder<Block, Block> STEEL_PLATE_BLOCK = register("steel_plate_block", ExSteelPlateBlock::new);
    public static final DeferredHolder<Block, Block> DAMAGED_STEEL_PLATE_BLOCK = register("damaged_steel_plate_block", ExSteelPlateBlock::new);
    public static final DeferredHolder<Block, Block> CORRUGATED_STEEL_BLOCK = register("corrugated_steel_block", ExSteelPlateBlock::new);
    public static final DeferredHolder<Block, Block> CARVED_STEEL_PLATE_BLOCK = register("carved_steel_plate_block", () -> pillar(exSteelPlateProperties()));
    public static final DeferredHolder<Block, Block> CARVED_STEEL_PLATE_STAIRS = registerStairs("carved_steel_plate_stairs",
            CARVED_STEEL_PLATE_BLOCK);
    public static final DeferredHolder<Block, Block> CARVED_STEEL_PLATE_SLAB = registerSlabs("carved_steel_plate_slab",
            CARVED_STEEL_PLATE_BLOCK);
    public static final DeferredHolder<Block, Block> DAMAGED_CARVED_STEEL_PLATE_BLOCK = register("damaged_carved_steel_plate_block", () -> pillar(exSteelPlateProperties()));
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_STEEL_GRATE = register("extraterrestrial_steel_grate", GrateBlock::new);
    public static final DeferredHolder<Block, IronBarsBlock> EXTRATERRESTRIAL_STEEL_GRATE_PANEL = register("extraterrestrial_steel_grate_panel", () -> new IronBarsBlock(exSteelPlateProperties().noOcclusion()));
    public static final DeferredHolder<Block, Block> STEEL_BRIDGE = register("steel_bridge", SteelBridgeBlock::new);
    public static final DeferredHolder<Block, Block> STEEL_BRIDGE_SLAB = register("steel_bridge_slab", () -> new SlabBlock(exSteelPlateProperties().noOcclusion()){
        public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
            return 1.0F;
        }

        public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) {
            return true;
        }
    });
    public static final DeferredHolder<Block, Block> STEEL_ROD = register("steel_rod", () -> new SteelRodBlock(exSteelPlateProperties().noOcclusion()));
    public static final DeferredHolder<Block, Block> STEEL_DOOR = register("steel_door",
            () -> new DoorBlock(CHBlockSetType.EX_STEEL, Block.Properties.of()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 10.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .isRedstoneConductor((i, d, k) -> false)));
    public static final DeferredHolder<Block, Block> STEEL_TRAPDOOR = register("steel_trapdoor",
            () -> new TrapDoorBlock(CHBlockSetType.EX_STEEL, Block.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 10.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    //Lamp
    public static final DeferredHolder<Block, Block> STEEL_LAMP = register("steel_lamp", SteelLampBlock::new);
    public static final DeferredHolder<Block, Block> BROKEN_STEEL_LAMP = register("broken_steel_lamp", BrokenSteelLampBlock::new);
    public static final DeferredHolder<Block, Block> REDSTONE_STEEL_LAMP = register("redstone_steel_lamp", RedstoneSteelLampBlock::new);

    //Fireproof
    public static final DeferredHolder<Block, Block> FIREPROOF_STEEL_BLOCK = fireproof("fireproof_steel_block", FireproofSteelBlock::new);
    public static final DeferredHolder<Block, Block> CARVED_FIREPROOF_STEEL_BLOCK = fireproof("carved_fireproof_steel_block", FireproofSteelBlock::new);
    public static final DeferredHolder<Block, Block> CARVED_FIREPROOF_STEEL_STAIRS = fireproofStairs("carved_fireproof_steel_stairs",
            CARVED_FIREPROOF_STEEL_BLOCK);
    public static final DeferredHolder<Block, Block> CARVED_FIREPROOF_STEEL_SLAB = fireproofSlabs("carved_fireproof_steel_slab",
            CARVED_FIREPROOF_STEEL_BLOCK);
    public static final DeferredHolder<Block, Block> FIREPROOF_STEEL_DOOR = fireproof("fireproof_steel_door",
            () -> new DoorBlock(CHBlockSetType.FIREPROOF_STEEL, Block.Properties.of()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 35.0F)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion()
                    .isRedstoneConductor((i, d, k) -> false)));
    public static final DeferredHolder<Block, Block> FIREPROOF_STEEL_TRAPDOOR = fireproof("fireproof_steel_trapdoor",
            () -> new TrapDoorBlock(CHBlockSetType.FIREPROOF_STEEL, Block.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 35.0F)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion()));
    public static final DeferredHolder<Block, Block> BLAZE_FUEL_CYLINDER_BLOCK = register("blaze_fuel_cylinder", BlazeFuelCylinderBlock::new, false, LootTableType.EMPTY);

    //Calcite
    public static final DeferredHolder<Block, Block> CALCITE_TILES = register("calcite_tiles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE)));
    public static final DeferredHolder<Block, Block> CALCITE_TILE_STAIRS = registerStairs("calcite_tile_stairs",
            CALCITE_TILES);
    public static final DeferredHolder<Block, Block> CALCITE_TILE_SLAB = registerSlabs("calcite_tile_slab",
            CALCITE_TILES);

    public static final DeferredHolder<Block, Block> CRACKED_CALCITE_TILES = register("cracked_calcite_tiles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CALCITE)));

    //Ice
    public static final DeferredHolder<Block, Block> CRYOGENIC_ICICLE = register("cryogenic_icicle", IcicleBlock::new);
    public static final DeferredHolder<Block, Block> BIG_CRYOGENIC_ICICLE = register("big_cryogenic_icicle", BigIcicleBlock::new, false, LootTableType.EMPTY);
    public static final DeferredHolder<Block, Block> CRYOGENIC_FROST = register("cryogenic_frost", CryoFrostBlock::new, true, LootTableType.EMPTY);

    //Redstone
//    public static final DeferredHolder<Block, Block> REDSTONE_CABLE = register("redstone_cable", RedstoneCableBlock::new);
//    public static final DeferredHolder<Block, Block> REDSTONE_CABLE_DISTRIBUTOR = fireproof("redstone_cable_distributor", RedstoneCableDistributorBlock::new);

    //Flesh
    public static final DeferredHolder<Block, Block> TECHNOFLESH_BLOCK = register("technoflesh_block", FleshBlock::new);
    public static final DeferredHolder<Block, Block> TECHNOFLESH_SLAB = registerSlabs("technoflesh_slab",
            TECHNOFLESH_BLOCK);
    public static final DeferredHolder<Block, Block> FROZEN_TECHNOFLESH_BLOCK = register("frozen_technoflesh", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PACKED_ICE)));
    public static final DeferredHolder<Block, Block> TUBULAR_TECHNOFLESH = register("tubular_technoflesh", () -> pillar(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_RED)
            .strength(2.0F)
            .ignitedByLava()
            .sound(SoundType.HONEY_BLOCK)));
    public static final DeferredHolder<Block, Block> TECHNOFLESH_MEMBRANE = register("technoflesh_membrane", FleshMembraneBlock::new, true, LootTableType.EMPTY);
    public static final DeferredHolder<Block, Block> HANGING_TECHNOFLESH = register("hanging_technoflesh", HangingFleshBlock::new, true, LootTableType.EMPTY);
    public static final DeferredHolder<Block, Block> BIG_HANGING_TECHNOFLESH = register("big_hanging_technoflesh", BigHangingFleshBlock::new, false, LootTableType.EMPTY);
    public static final DeferredHolder<Block, Block> NERVE_ENDINGS = register("nerve_endings", NerveEndingsBlock::new, true, LootTableType.EMPTY);
    public static final DeferredHolder<Block, Block> TECHNOFLESH_NEST = register("technoflesh_nest", FleshNestBlock::new, true, LootTableType.EMPTY);
    public static final DeferredHolder<Block, Block> CONSUMMATE_NEST = register("consummate_nest", ConsummateNestBlock::new, true, LootTableType.EMPTY);

    //Tech
    public static final DeferredHolder<Block, Block> CRYSTAL_FORMER = register("crystal_former", CrystalFormerBlock::new);
    public static final DeferredHolder<Block, Block> STATIONARY_CHARGING_STATION = register("stationary_charging_station", ChargingStationBlock::new);
    public static final DeferredHolder<Block, Block> BARRIER_OF_EXTRATERRESTRIAL_ACTIVITY = register("barrier_of_extraterrestrial_activity", ExBarrierBlock::new);
    public static final DeferredHolder<Block, Block> BROKEN_CRYSTAL_FORMER = register("broken_crystal_former", BrokenCrystalFormerBlock::new);
    public static final DeferredHolder<Block, Block> FLAME_SPEWER = register("flame_spewer", FlameSpewerBlock::new);
    public static final DeferredHolder<Block, Block> MOTION_SENSOR = register("motion_sensor", MotionSensorBlock::new);
    public static final DeferredHolder<Block, Block> EXTRATERRESTRIAL_ACTIVITY_RADAR = register("extraterrestrial_activity_radar", ExActivityRadarBlock::new);

    private static RotatedPillarBlock pillar(BlockBehaviour.Properties properties) {
        return new RotatedPillarBlock(properties);
    }

    public static <T extends Block> DeferredHolder<Block, Block> registerSlabs(final String string, final DeferredHolder<Block, T> block){
        return register(string, () -> new SlabBlock(Block.Properties.ofFullCopy(block.get())), true);
    }

    public static <T extends Block> DeferredHolder<Block, Block> registerStairs(final String name, final DeferredHolder<Block, T> block){
        return register(name, () -> new StairBlock(block.get().defaultBlockState(), Block.Properties.ofFullCopy(block.get())));
    }

    public static <T extends Block> DeferredHolder<Block, Block> fireproofSlabs(final String string, final DeferredHolder<Block, T> block){
        return fireproof(string, () -> new SlabBlock(Block.Properties.ofFullCopy(block.get())), true);
    }

    public static <T extends Block> DeferredHolder<Block, Block> fireproofStairs(final String name, final DeferredHolder<Block, T> block){
        return fireproof(name, () -> new StairBlock(block.get().defaultBlockState(), Block.Properties.ofFullCopy(block.get())));
    }

    public static <T extends Block> DeferredHolder<Block, Block> registerWalls(final String name, final DeferredHolder<Block, T> block){
        return register(name, () -> new WallBlock(Block.Properties.ofFullCopy(block.get())));
    }

    public static <T extends Block> DeferredHolder<Block, T> register(final String string, final Supplier<? extends T> sup){
        return register(string, sup, true);
    }

    public static <T extends Block> DeferredHolder<Block, T> register(final String string, final Supplier<? extends T> sup, boolean blockItemDefault){
        return register(string, sup, blockItemDefault, LootTableType.DROP);
    }

    public static <T extends Block> DeferredHolder<Block, T> register(final String string, final Supplier<? extends T> sup, boolean blockItemDefault, LootTableType lootTableType) {
        DeferredHolder<Block, T> block = BLOCKS.register(string, sup);
        BLOCK_LOOT.put(block.getId(), new BlockLootSetting(blockItemDefault, lootTableType));
        if (blockItemDefault) {
            CHItems.ITEMS.register(string,
                    () -> new CHBlockItem(block.get(), new Item.Properties()));
        }
        return block;
    }

    public static <T extends Block> DeferredHolder<Block, T> fireproof(final String string, final Supplier<? extends T> sup){
        return fireproof(string, sup, true);
    }

    public static <T extends Block> DeferredHolder<Block, T> fireproof(final String string, final Supplier<? extends T> sup, boolean blockItemDefault){
        return fireproof(string, sup, blockItemDefault, LootTableType.DROP);
    }

    public static <T extends Block> DeferredHolder<Block, T> fireproof(final String string, final Supplier<? extends T> sup, boolean blockItemDefault, LootTableType lootTableType) {
        DeferredHolder<Block, T> block = BLOCKS.register(string, sup);
        BLOCK_LOOT.put(block.getId(), new BlockLootSetting(blockItemDefault, lootTableType));
        if (blockItemDefault) {
            CHItems.ITEMS.register(string,
                    () -> new CHBlockItem(block.get(), new Item.Properties().fireResistant()));
        }
        return block;
    }

    private static boolean always(BlockState p_50775_, BlockGetter p_50776_, BlockPos p_50777_) {
        return true;
    }

    public static DeferredHolder<Item, BlockItem> SMALL_EX_ENERGY_CLUSTER;
    public static DeferredHolder<Item, BlockItem> MEDIUM_EX_ENERGY_CLUSTER;
    public static DeferredHolder<Item, BlockItem> LARGE_EX_ENERGY_CLUSTER;

    public static <T extends Block> DeferredHolder<Block, T> registerCluster(final String string, final Supplier<? extends T> sup) {
        DeferredHolder<Block, T> block = BLOCKS.register(string, sup);
        BLOCK_LOOT.put(block.getId(), new BlockLootSetting(false, LootTableType.EMPTY));
        SMALL_EX_ENERGY_CLUSTER = CHItems.ITEMS.register("small_" + string,
                () -> new ExEnergyClusterItem(0));
        MEDIUM_EX_ENERGY_CLUSTER = CHItems.ITEMS.register("medium_" + string,
                () -> new ExEnergyClusterItem(1));
        LARGE_EX_ENERGY_CLUSTER = CHItems.ITEMS.register("large_" + string,
                () -> new ExEnergyClusterItem(2));
        return block;
    }

    public static BlockBehaviour.Properties exStoneProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)
                .strength(3.0F, 35.0F);
    }

    public static class SmoothExStoneBlock extends Block {
        public SmoothExStoneBlock() {
            super(smoothExStoneProperties());
        }
    }

    public static BlockBehaviour.Properties smoothExStoneProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE)
                .strength(3.0F, 35.0F);
    }

    public static class ExStoneBricksBlock extends Block {
        public ExStoneBricksBlock() {
            super(exStoneBricksProperties());
        }
    }

    public static BlockBehaviour.Properties exStoneBricksProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS)
                .strength(3.0F, 35.0F);
    }

    public static BlockBehaviour.Properties exSteelOreProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                .strength(1.5F, 6.0F);
    }

    public static class ExSteelBlock extends Block {
        public ExSteelBlock(Block block) {
            super(exSteelBlockProperties(block));
        }
    }

    public static BlockBehaviour.Properties exSteelBlockProperties(Block block) {
        return BlockBehaviour.Properties.ofFullCopy(block)
                .mapColor(MapColor.COLOR_LIGHT_GRAY)
                .strength(4.0F, 40.0F);
    }

    public static class ExSteelPlateBlock extends Block {
        public ExSteelPlateBlock() {
            super(exSteelPlateProperties());
        }
    }

    public static BlockBehaviour.Properties exSteelPlateProperties() {
        return BlockBehaviour.Properties.of()
                .sound(SoundType.COPPER)
                .mapColor(MapColor.COLOR_GRAY)
                .requiresCorrectToolForDrops()
                .strength(2.0F, 10.0F);
    }

    public static class FireproofSteelBlock extends Block {
        public FireproofSteelBlock() {
            super(fireproofSteelProperties());
        }
    }

    public static BlockBehaviour.Properties fireproofSteelProperties() {
        return BlockBehaviour.Properties.of()
                .sound(SoundType.NETHERITE_BLOCK)
                .mapColor(MapColor.COLOR_GRAY)
                .requiresCorrectToolForDrops()
                .strength(2.0F, 35.0F);
    }

    /**
     * Based on @klikli-dev's Block Loot Generator
     */
    public enum LootTableType {
        EMPTY,
        DROP
    }

    public static class BlockLootSetting {
        public boolean generateDefaultBlockItem;
        public LootTableType lootTableType;

        public BlockLootSetting(boolean generateDefaultBlockItem,
                                LootTableType lootTableType) {
            this.generateDefaultBlockItem = generateDefaultBlockItem;
            this.lootTableType = lootTableType;
        }
    }
}

package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.ExEnergyClusterItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CHBlocks {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ClangingHowl.MOD_ID);
    public static final Map<ResourceLocation, BlockLootSetting> BLOCK_LOOT = new HashMap<>();

    public static void init(){
        CHBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Extraterrestrial Stone
    public static final RegistryObject<Block> EXTRATERRESTRIAL_STONE = register("extraterrestrial_stone", ExStoneBlock::new);
    public static final RegistryObject<Block> EXTRATERRESTRIAL_STONE_STAIRS = registerStairs("extraterrestrial_stone_stairs",
            EXTRATERRESTRIAL_STONE);
    public static final RegistryObject<Block> EXTRATERRESTRIAL_STONE_SLAB = registerSlabs("extraterrestrial_stone_slab",
            EXTRATERRESTRIAL_STONE);
    public static final RegistryObject<Block> EXTRATERRESTRIAL_STONE_WALL = registerWalls("extraterrestrial_stone_wall",
            EXTRATERRESTRIAL_STONE);

    public static final RegistryObject<Block> SMOOTH_EXTRATERRESTRIAL_STONE = register("smooth_extraterrestrial_stone", SmoothExStoneBlock::new);
    public static final RegistryObject<Block> SMOOTH_EXTRATERRESTRIAL_STONE_STAIRS = registerStairs("smooth_extraterrestrial_stone_stairs",
            SMOOTH_EXTRATERRESTRIAL_STONE);
    public static final RegistryObject<Block> SMOOTH_EXTRATERRESTRIAL_STONE_SLAB = registerSlabs("smooth_extraterrestrial_stone_slab",
            SMOOTH_EXTRATERRESTRIAL_STONE);
    public static final RegistryObject<Block> SMOOTH_EXTRATERRESTRIAL_STONE_WALL = registerWalls("smooth_extraterrestrial_stone_wall",
            SMOOTH_EXTRATERRESTRIAL_STONE);

    public static final RegistryObject<Block> EXTRATERRESTRIAL_STONE_BRICKS = register("extraterrestrial_stone_bricks", ExStoneBricksBlock::new);
    public static final RegistryObject<Block> EXTRATERRESTRIAL_STONE_BRICK_STAIRS = registerStairs("extraterrestrial_stone_brick_stairs",
            EXTRATERRESTRIAL_STONE_BRICKS);
    public static final RegistryObject<Block> EXTRATERRESTRIAL_STONE_BRICK_SLAB = registerSlabs("extraterrestrial_stone_brick_slab",
            EXTRATERRESTRIAL_STONE_BRICKS);
    public static final RegistryObject<Block> EXTRATERRESTRIAL_STONE_BRICK_WALL = registerWalls("extraterrestrial_stone_brick_wall",
            EXTRATERRESTRIAL_STONE_BRICKS);

    public static final RegistryObject<Block> CARVED_EXTRATERRESTRIAL_STONE_BRICKS = register("carved_extraterrestrial_stone_bricks", ExStoneBricksBlock::new);

    public static final RegistryObject<Block> EXTRATERRESTRIAL_COLUMN = register("extraterrestrial_column", () -> pillar(smoothExStoneProperties()));

    public static final RegistryObject<Block> EXTRATERRESTRIAL_PEBBLE = register("extraterrestrial_pebble", ExPebbleBlock::new);
    public static final RegistryObject<Block> INCANDESCENT_EXTRATERRESTRIAL_STONE = register("incandescent_extraterrestrial_stone", () -> new MagmaBlock(exStoneProperties().lightLevel(l -> 3)));

    //Ores
    public static final RegistryObject<Block> METEORITE_STEEL_ORE = register("meteorite_steel_ore", () -> new DropExperienceBlock(exStoneProperties(), UniformInt.of(3, 7)), true, LootTableType.EMPTY);
    public static final RegistryObject<Block> EXTRATERRESTRIAL_STEEL_ORE = register("extraterrestrial_steel_ore", () -> new DropExperienceBlock(exSteelOreProperties(), UniformInt.of(3, 7)), true, LootTableType.EMPTY);

    //Energy Clusters
    public static final RegistryObject<Block> EXTRATERRESTRIAL_ENERGY_CLUSTER = registerCluster("extraterrestrial_energy_cluster", ExEnergyClusterBlock::new);
    public static final RegistryObject<Block> HUGE_EXTRATERRESTRIAL_ENERGY_CLUSTER = register("huge_extraterrestrial_energy_cluster", HugeExEnergyClusterBlock::new, true, LootTableType.EMPTY);

    //Steel
    public static final RegistryObject<Block> RAW_EXTRATERRESTRIAL_STEEL_BLOCK = register("raw_extraterrestrial_steel_block", () -> new ExSteelBlock(Blocks.STONE));
    public static final RegistryObject<Block> EXTRATERRESTRIAL_STEEL_BLOCK = register("extraterrestrial_steel_block", () -> new ExSteelBlock(Blocks.NETHERITE_BLOCK));
    public static final RegistryObject<Block> STEEL_PLATE_BLOCK = register("steel_plate_block", ExSteelPlateBlock::new);
    public static final RegistryObject<Block> DAMAGED_STEEL_PLATE_BLOCK = register("damaged_steel_plate_block", ExSteelPlateBlock::new);
    public static final RegistryObject<Block> CARVED_STEEL_PLATE_BLOCK = register("carved_steel_plate_block", () -> pillar(exSteelPlateProperties()));
    public static final RegistryObject<Block> CARVED_STEEL_PLATE_STAIRS = registerStairs("carved_steel_plate_stairs",
            CARVED_STEEL_PLATE_BLOCK);
    public static final RegistryObject<Block> CARVED_STEEL_PLATE_SLAB = registerSlabs("carved_steel_plate_slab",
            CARVED_STEEL_PLATE_BLOCK);
    public static final RegistryObject<Block> DAMAGED_CARVED_STEEL_PLATE_BLOCK = register("damaged_carved_steel_plate_block", () -> pillar(exSteelPlateProperties()));
    public static final RegistryObject<Block> EXTRATERRESTRIAL_STEEL_GRATE = register("extraterrestrial_steel_grate", GrateBlock::new);
    public static final RegistryObject<Block> STEEL_ROD = register("steel_rod", () -> new SteelRodBlock(exSteelPlateProperties().noOcclusion()));
    public static final RegistryObject<Block> STEEL_DOOR = register("steel_door",
            () -> new DoorBlock(Block.Properties.of()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 10.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .isRedstoneConductor((i, d, k) -> false), CHBlockSetType.EX_STEEL));
    public static final RegistryObject<Block> STEEL_TRAPDOOR = register("steel_trapdoor",
            () -> new TrapDoorBlock(Block.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 10.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion(),
                    CHBlockSetType.EX_STEEL));

    private static RotatedPillarBlock pillar(BlockBehaviour.Properties properties) {
        return new RotatedPillarBlock(properties);
    }

    public static <T extends Block> RegistryObject<Block> registerSlabs(final String string, final RegistryObject<T> block){
        return register(string, () -> new SlabBlock(Block.Properties.copy(block.get())), true);
    }

    public static <T extends Block> RegistryObject<Block> registerStairs(final String name, final RegistryObject<T> block){
        return register(name, () -> new StairBlock(() -> block.get().defaultBlockState(), Block.Properties.copy(block.get())));
    }

    public static <T extends Block> RegistryObject<Block> registerWalls(final String name, final RegistryObject<T> block){
        return register(name, () -> new WallBlock(Block.Properties.copy(block.get())));
    }

    public static <T extends Block> RegistryObject<T> register(final String string, final Supplier<? extends T> sup){
        return register(string, sup, true);
    }

    public static <T extends Block> RegistryObject<T> register(final String string, final Supplier<? extends T> sup, boolean blockItemDefault){
        return register(string, sup, blockItemDefault, LootTableType.DROP);
    }

    public static <T extends Block> RegistryObject<T> register(final String string, final Supplier<? extends T> sup, boolean blockItemDefault, LootTableType lootTableType) {
        RegistryObject<T> block = BLOCKS.register(string, sup);
        BLOCK_LOOT.put(block.getId(), new BlockLootSetting(blockItemDefault, lootTableType));
        if (blockItemDefault) {
            CHItems.ITEMS.register(string,
                    () -> new BlockItem(block.get(), new Item.Properties()));
        }
        return block;
    }

    public static RegistryObject<BlockItem> SMALL_EX_ENERGY_CLUSTER;
    public static RegistryObject<BlockItem> MEDIUM_EX_ENERGY_CLUSTER;
    public static RegistryObject<BlockItem> LARGE_EX_ENERGY_CLUSTER;

    public static <T extends Block> RegistryObject<T> registerCluster(final String string, final Supplier<? extends T> sup) {
        RegistryObject<T> block = BLOCKS.register(string, sup);
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
        return BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                .strength(3.0F, 35.0F);
    }

    public static class SmoothExStoneBlock extends Block {
        public SmoothExStoneBlock() {
            super(smoothExStoneProperties());
        }
    }

    public static BlockBehaviour.Properties smoothExStoneProperties() {
        return BlockBehaviour.Properties.copy(Blocks.POLISHED_DEEPSLATE)
                .strength(3.0F, 35.0F);
    }

    public static class ExStoneBricksBlock extends Block {
        public ExStoneBricksBlock() {
            super(exStoneBricksProperties());
        }
    }

    public static BlockBehaviour.Properties exStoneBricksProperties() {
        return BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS)
                .strength(3.0F, 35.0F);
    }

    public static BlockBehaviour.Properties exSteelOreProperties() {
        return BlockBehaviour.Properties.copy(Blocks.STONE)
                .strength(1.5F, 6.0F);
    }

    public static class ExSteelBlock extends Block {
        public ExSteelBlock(Block block) {
            super(exSteelBlockProperties(block));
        }
    }

    public static BlockBehaviour.Properties exSteelBlockProperties(Block block) {
        return BlockBehaviour.Properties.copy(block)
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

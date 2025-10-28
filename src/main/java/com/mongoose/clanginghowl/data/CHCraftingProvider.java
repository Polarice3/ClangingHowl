package com.mongoose.clanginghowl.data;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.common.items.CHItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CHCraftingProvider extends RecipeProvider {
    public CHCraftingProvider(PackOutput p_248933_) {
        super(p_248933_);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CHBlocks.CARVED_EXTRATERRESTRIAL_STONE_BRICKS.get())
                .pattern("#")
                .pattern("#")
                .define('#', CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_SLAB.get())
                .unlockedBy("has_item", has(CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_SLAB.get()))
                .showNotification(false)
                .save(consumer, loc("ex_stone/carved_extraterrestrial_stone_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CHBlocks.EXTRATERRESTRIAL_COLUMN.get(), 2)
                .pattern("#")
                .pattern("#")
                .define('#', CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE.get())
                .unlockedBy("has_item", has(CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE.get()))
                .showNotification(false)
                .save(consumer, loc("ex_stone/extraterrestrial_column"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CHBlocks.STEEL_BRIDGE.get(), 6)
                .pattern("###")
                .pattern("$$$")
                .pattern("###")
                .define('#', CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get())
                .define('$', CHBlocks.STEEL_ROD.get())
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/steel_bridge"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CHBlocks.STEEL_ROD.get(), 4)
                .pattern("#")
                .pattern("#")
                .define('#', CHItems.EXTRATERRESTRIAL_STEEL_NUGGET.get())
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_NUGGET.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/steel_rod"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CHBlocks.STEEL_DOOR.get(), 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get())
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/steel_door"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CHBlocks.STEEL_TRAPDOOR.get(), 4)
                .pattern("###")
                .pattern("###")
                .define('#', CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get())
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/steel_trapdoor"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, CHBlocks.CRYSTAL_FORMER.get())
                .pattern("EEE")
                .pattern("PGP")
                .pattern("CFC")
                .define('E', CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get())
                .define('P', CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get())
                .define('G', CHBlocks.EXTRATERRESTRIAL_STEEL_GRATE.get())
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('F', CHItems.CRYOGENIC_FUEL.get())
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get()))
                .showNotification(false)
                .save(consumer, loc("crystal_former"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, CHBlocks.STATIONARY_CHARGING_STATION.get())
                .pattern("LDL")
                .pattern("CBC")
                .pattern("PPP")
                .define('D', CHItems.DIAMOND_DIODE.get())
                .define('P', CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get())
                .define('L', Items.LIGHTNING_ROD)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('B', CHItems.BATTERY_PANEL.get())
                .unlockedBy("has_item", has(CHItems.BATTERY_PANEL.get()))
                .showNotification(false)
                .save(consumer, loc("stationary_charging_station"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CHItems.EXTRATERRESTRIAL_SWORD.get())
                .pattern("#")
                .pattern("#")
                .pattern("R")
                .define('#', CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get())
                .define('R', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/tools/extraterrestrial_sword"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CHItems.EXTRATERRESTRIAL_SHOVEL.get())
                .pattern("#")
                .pattern("S")
                .pattern("R")
                .define('#', CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get())
                .define('S', CHBlocks.STEEL_ROD.get())
                .define('R', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/tools/extraterrestrial_shovel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CHItems.EXTRATERRESTRIAL_PICKAXE.get())
                .pattern("###")
                .pattern(" S ")
                .pattern(" R ")
                .define('#', CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get())
                .define('S', CHBlocks.STEEL_ROD.get())
                .define('R', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/tools/extraterrestrial_pickaxe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CHItems.EXTRATERRESTRIAL_AXE.get())
                .pattern("##")
                .pattern("#S")
                .pattern(" R")
                .define('#', CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get())
                .define('S', CHBlocks.STEEL_ROD.get())
                .define('R', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/tools/extraterrestrial_axe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CHItems.EXTRATERRESTRIAL_HOE.get())
                .pattern("##")
                .pattern(" S")
                .pattern(" R")
                .define('#', CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get())
                .define('S', CHBlocks.STEEL_ROD.get())
                .define('R', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/tools/extraterrestrial_hoe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CHItems.EXTRATERRESTRIAL_HAMMER.get())
                .pattern("###")
                .pattern("#S#")
                .pattern(" R ")
                .define('#', CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get())
                .define('S', CHBlocks.STEEL_ROD.get())
                .define('R', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/tools/extraterrestrial_hammer"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CHItems.INDUSTRIAL_ADJUSTABLE_WRENCH.get())
                .pattern("PC")
                .pattern("PB")
                .pattern(" R")
                .define('P', CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get())
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('B', CHItems.ENERGY_BATTERY.get())
                .define('R', CHBlocks.STEEL_ROD.get())
                .unlockedBy("has_item", has(CHItems.ENERGY_BATTERY.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/tools/industrial_adjustable_wrench"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CHItems.ENERGY_BATTERY.get())
                .pattern("SCS")
                .pattern("SES")
                .pattern("SCS")
                .define('S', CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get())
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('E', CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get())
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get()))
                .showNotification(false)
                .save(consumer, loc("energy_battery"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CHItems.ENERGY_INTENSIVE_BATTERY.get())
                .pattern("SCS")
                .pattern("SES")
                .pattern("SCS")
                .define('S', CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get())
                .define('C', CHItems.ENERGY_BATTERY.get())
                .define('E', CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get())
                .unlockedBy("has_item", has(CHItems.ENERGY_BATTERY.get()))
                .showNotification(false)
                .save(consumer, loc("energy_intensive_battery"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CHItems.BATTERY_PANEL.get())
                .pattern("DRR")
                .pattern("BPC")
                .define('D', CHItems.DIAMOND_DIODE.get())
                .define('R', CHItems.REDSTONE_WIRE.get())
                .define('B', CHItems.ENERGY_BATTERY.get())
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('P', CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get())
                .unlockedBy("has_item", has(CHItems.ENERGY_BATTERY.get()))
                .showNotification(false)
                .save(consumer, loc("battery_panel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CHItems.CRYOGENIC_FUEL.get(), 4)
                .pattern("CB")
                .pattern("BB")
                .define('C', CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get())
                .define('B', Items.BLUE_ICE)
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get()))
                .showNotification(false)
                .save(consumer, loc("cryogenic_fuel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CHItems.BLAZE_FUEL.get(), 4)
                .pattern("CB")
                .pattern("BB")
                .define('C', CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get())
                .define('B', Items.BLAZE_POWDER)
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get()))
                .showNotification(false)
                .save(consumer, loc("blaze_fuel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CHItems.BLAZE_FUEL_CYLINDER.get())
                .pattern(" C ")
                .pattern("PFP")
                .pattern("PPP")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('F', CHItems.BLAZE_FUEL.get())
                .define('P', CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get())
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get()))
                .showNotification(false)
                .save(consumer, loc("blaze_fuel_cylinder"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CHItems.BLAZE_BURNER.get())
                .pattern("FC ")
                .pattern("PBP")
                .pattern(" P ")
                .define('P', CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get())
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('B', CHItems.BLAZE_FUEL.get())
                .define('F', Items.FLINT_AND_STEEL)
                .unlockedBy("has_item", has(CHItems.BLAZE_FUEL.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/tools/blaze_burner"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CHItems.FLAMETHROWER.get())
                .pattern("P  ")
                .pattern("BPC")
                .pattern(" FC")
                .define('P', CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get())
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('B', CHItems.BLAZE_BURNER.get())
                .define('F', CHItems.BLAZE_FUEL_CYLINDER.get())
                .unlockedBy("has_item", has(CHItems.BLAZE_FUEL_CYLINDER.get()))
                .showNotification(false)
                .save(consumer, loc("ex_steel/tools/flamethrower"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CHItems.DRILL_BIT.get())
                .pattern(" PC")
                .pattern("NPP")
                .pattern("NN ")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('N', CHItems.EXTRATERRESTRIAL_STEEL_NUGGET.get())
                .define('P', CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get())
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get()))
                .showNotification(false)
                .save(consumer, loc("drill_bit"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CHItems.ADVANCED_HAND_DRILL.get())
                .pattern(" CP")
                .pattern("CBP")
                .pattern("D  ")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('P', CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get())
                .define('B', CHItems.BATTERY_PANEL.get())
                .define('D', CHItems.DRILL_BIT.get())
                .unlockedBy("has_item", has(CHItems.DRILL_BIT.get()))
                .showNotification(false)
                .save(consumer, loc("advanced_hand_drill"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CHItems.CHAINSAW_TEETH.get())
                .pattern(" NN")
                .pattern("N N")
                .pattern("NN ")
                .define('N', CHItems.EXTRATERRESTRIAL_STEEL_NUGGET.get())
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_NUGGET.get()))
                .showNotification(false)
                .save(consumer, loc("chainsaw_teeth"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CHItems.ADVANCED_CHAINSAW.get())
                .pattern(" CB")
                .pattern("TPC")
                .pattern("TT ")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('B', CHItems.BATTERY_PANEL.get())
                .define('P', CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get())
                .define('T', CHItems.CHAINSAW_TEETH.get())
                .unlockedBy("has_item", has(CHItems.CHAINSAW_TEETH.get()))
                .showNotification(false)
                .save(consumer, loc("advanced_chainsaw"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CHItems.ADVANCED_CHAINSWORD.get())
                .pattern("TI ")
                .pattern("TIB")
                .pattern("CS ")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('B', CHItems.BATTERY_PANEL.get())
                .define('I', CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get())
                .define('T', CHItems.CHAINSAW_TEETH.get())
                .define('S', CHBlocks.STEEL_ROD.get())
                .unlockedBy("has_item", has(CHItems.CHAINSAW_TEETH.get()))
                .showNotification(false)
                .save(consumer, loc("advanced_chainsword"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CHItems.REDSTONE_WIRE.get(), 3)
                .pattern(" RC")
                .pattern("RCR")
                .pattern("CR ")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_item", has(Items.REDSTONE))
                .showNotification(false)
                .save(consumer, loc("redstone_wire"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CHItems.DIAMOND_DIODE.get(), 2)
                .pattern("D")
                .pattern("C")
                .pattern("N")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('N', CHItems.EXTRATERRESTRIAL_STEEL_NUGGET.get())
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_NUGGET.get()))
                .showNotification(false)
                .save(consumer, loc("diamond_diode"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CHItems.PORTABLE_CHARGER.get())
                .pattern("RCR")
                .pattern("BDB")
                .pattern("PPP")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('R', CHItems.REDSTONE_WIRE.get())
                .define('D', CHItems.DIAMOND_DIODE.get())
                .define('B', CHItems.ENERGY_INTENSIVE_BATTERY.get())
                .define('P', CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get())
                .unlockedBy("has_item", has(CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get()))
                .showNotification(false)
                .save(consumer, loc("portable_charger"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CHBlocks.TECHNOFLESH_MEMBRANE.get(), 4)
                .pattern("FF")
                .define('F', CHItems.CHUNK_OF_TECHNOFLESH.get())
                .unlockedBy("has_item", has(CHItems.CHUNK_OF_TECHNOFLESH.get()))
                .showNotification(false)
                .save(consumer, loc("technoflesh_membrane"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CHBlocks.HANGING_TECHNOFLESH.get(), 6)
                .pattern("F F")
                .pattern(" F ")
                .define('F', CHItems.CHUNK_OF_TECHNOFLESH.get())
                .unlockedBy("has_item", has(CHItems.CHUNK_OF_TECHNOFLESH.get()))
                .showNotification(false)
                .save(consumer, loc("hanging_technoflesh"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CHBlocks.TUBULAR_TECHNOFLESH.get(), 2)
                .pattern("F")
                .pattern("F")
                .define('F', CHBlocks.TECHNOFLESH_BLOCK.get())
                .unlockedBy("has_item", has(CHBlocks.TECHNOFLESH_BLOCK.get()))
                .showNotification(false)
                .save(consumer, loc("tubular_technoflesh"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CHBlocks.CRYOGENIC_ICICLE.get(), 4)
                .pattern("F")
                .pattern("F")
                .define('F', CHItems.CRYOGENIC_FUEL.get())
                .unlockedBy("has_item", has(CHItems.CRYOGENIC_FUEL.get()))
                .showNotification(false)
                .save(consumer, loc("cryogenic_icicle"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, CHBlocks.FROZEN_TECHNOFLESH_BLOCK.get())
                .requires(CHItems.CHUNK_OF_TECHNOFLESH.get())
                .requires(Blocks.PACKED_ICE)
                .unlockedBy("has_item", has(CHItems.CHUNK_OF_TECHNOFLESH.get()))
                .save(consumer, loc("frozen_technoflesh"));

        smelting(consumer, "ex_stone/smooth_extraterrestrial_stone", CHBlocks.EXTRATERRESTRIAL_STONE.get(), CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE.get());
        smelting(consumer, "calcite/cracked_calcite_tiles", CHBlocks.CALCITE_TILES.get(), CHBlocks.CRACKED_CALCITE_TILES.get());

        blasting(consumer, "ex_steel/extraterrestrial_steel_ingot_blasting", CHItems.EXTRATERRESTRIAL_STEEL.get(), CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get(), 400);
        blasting(consumer, "ex_steel/extraterrestrial_steel_nugget_blasting", CHItems.PIECE_OF_EXTRATERRESTRIAL_STEEL.get(), CHItems.EXTRATERRESTRIAL_STEEL_NUGGET.get(), 50);
//        blasting(consumer, "ex_steel/extraterrestrial_steel_block_blasting", CHBlocks.RAW_EXTRATERRESTRIAL_STEEL_BLOCK.get(), CHBlocks.EXTRATERRESTRIAL_STEEL_BLOCK.get(), 3600);

        oneToOne(consumer, "ex_stone/extraterrestrial_pebble_from_stone", CHBlocks.EXTRATERRESTRIAL_STONE.get(), CHBlocks.EXTRATERRESTRIAL_PEBBLE.get(), 4);
        oneToOne(consumer, "ex_steel/piece_of_extraterrestrial_steel_from_steel", CHItems.EXTRATERRESTRIAL_STEEL.get(), CHItems.PIECE_OF_EXTRATERRESTRIAL_STEEL.get(), 9);
        oneToOne(consumer, "ex_steel/extraterrestrial_steel_nugget_from_ingot", CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get(), CHItems.EXTRATERRESTRIAL_STEEL_NUGGET.get(), 9);
        oneToOne(consumer, "ex_steel/extraterrestrial_steel_from_block", CHBlocks.RAW_EXTRATERRESTRIAL_STEEL_BLOCK.get(), CHItems.EXTRATERRESTRIAL_STEEL.get(), 9);
        oneToOne(consumer, "ex_steel/extraterrestrial_steel_ingot_from_block", CHBlocks.EXTRATERRESTRIAL_STEEL_BLOCK.get(), CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get(), 9);

        fullGrid(consumer, "ex_steel/extraterrestrial_steel_from_pieces", CHItems.PIECE_OF_EXTRATERRESTRIAL_STEEL.get(), CHItems.EXTRATERRESTRIAL_STEEL.get());
        fullGrid(consumer, "ex_steel/extraterrestrial_steel_ingot_from_nuggets", CHItems.EXTRATERRESTRIAL_STEEL_NUGGET.get(), CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get());
        fullGrid(consumer, "ex_steel/raw_extraterrestrial_steel_block", CHItems.EXTRATERRESTRIAL_STEEL.get(), CHBlocks.RAW_EXTRATERRESTRIAL_STEEL_BLOCK.get());
        fullGrid(consumer, "ex_steel/extraterrestrial_steel_block", CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get(), CHBlocks.EXTRATERRESTRIAL_STEEL_BLOCK.get());

        twoByTwo(consumer, "ex_stone/extraterrestrial_stone_from_pebbles", CHBlocks.EXTRATERRESTRIAL_PEBBLE.get(), CHBlocks.EXTRATERRESTRIAL_STONE.get(), 1);
        twoByTwo(consumer, "ex_stone/extraterrestrial_stone_bricks", CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE.get(), CHBlocks.EXTRATERRESTRIAL_STONE_BRICKS.get());
        twoByTwo(consumer, "ex_steel/steel_plate_block", CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get(), CHBlocks.STEEL_PLATE_BLOCK.get(), 2);
        twoByTwo(consumer, "ex_steel/carved_steel_plate_block", CHBlocks.STEEL_PLATE_BLOCK.get(), CHBlocks.CARVED_STEEL_PLATE_BLOCK.get());
        twoByTwo(consumer, "ex_steel/extraterrestrial_steel_grate", CHBlocks.STEEL_ROD.get(), CHBlocks.EXTRATERRESTRIAL_STEEL_GRATE.get(), 1);
        twoByTwo(consumer, "flesh/technoflesh_block", CHItems.CHUNK_OF_TECHNOFLESH.get(), CHBlocks.TECHNOFLESH_BLOCK.get(), 1);

        slabBlock(consumer, "ex_stone/extraterrestrial_stone", CHBlocks.EXTRATERRESTRIAL_STONE, CHBlocks.EXTRATERRESTRIAL_STONE_SLAB);
        slabBlock(consumer, "ex_stone/smooth_extraterrestrial_stone", CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE, CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_SLAB);
        slabBlock(consumer, "ex_stone/extraterrestrial_stone_bricks", CHBlocks.EXTRATERRESTRIAL_STONE_BRICKS, CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_SLAB);
        slabBlock(consumer, "ex_steel/carved_steel_plate", CHBlocks.CARVED_STEEL_PLATE_BLOCK, CHBlocks.CARVED_STEEL_PLATE_SLAB);
        slabBlock(consumer, "calcite/calcite_tiles", CHBlocks.CALCITE_TILES, CHBlocks.CALCITE_TILE_SLAB);
        slabBlock(consumer, "flesh/technoflesh_slab", CHBlocks.TECHNOFLESH_BLOCK, CHBlocks.TECHNOFLESH_SLAB);

        stairsBlock(consumer, "ex_stone/extraterrestrial_stone", CHBlocks.EXTRATERRESTRIAL_STONE, CHBlocks.EXTRATERRESTRIAL_STONE_STAIRS);
        stairsBlock(consumer, "ex_stone/smooth_extraterrestrial_stone", CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE, CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_STAIRS);
        stairsBlock(consumer, "ex_stone/extraterrestrial_stone_bricks", CHBlocks.EXTRATERRESTRIAL_STONE_BRICKS, CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_STAIRS);
        stairsBlock(consumer, "ex_steel/carved_steel_plate", CHBlocks.CARVED_STEEL_PLATE_BLOCK, CHBlocks.CARVED_STEEL_PLATE_STAIRS);
        stairsBlock(consumer, "calcite/calcite_tiles", CHBlocks.CALCITE_TILES, CHBlocks.CALCITE_TILE_STAIRS);

        wallBlock(consumer, "ex_stone/extraterrestrial_stone", CHBlocks.EXTRATERRESTRIAL_STONE, CHBlocks.EXTRATERRESTRIAL_STONE_WALL);
        wallBlock(consumer, "ex_stone/smooth_extraterrestrial_stone", CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE, CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_WALL);
        wallBlock(consumer, "ex_stone/extraterrestrial_stone_bricks", CHBlocks.EXTRATERRESTRIAL_STONE_BRICKS, CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_WALL);

        stoneCutting(consumer, "ex_stone", CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE, CHBlocks.EXTRATERRESTRIAL_COLUMN);

        stoneCutting(consumer, "ex_stone", CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE, CHBlocks.CARVED_EXTRATERRESTRIAL_STONE_BRICKS);
        stoneCutting(consumer, "ex_stone", CHBlocks.EXTRATERRESTRIAL_STONE_BRICKS, CHBlocks.CARVED_EXTRATERRESTRIAL_STONE_BRICKS);
        stoneCutting(consumer, "ex_steel", CHBlocks.STEEL_PLATE_BLOCK, CHBlocks.CARVED_STEEL_PLATE_BLOCK);

        stoneCutting(consumer, "ex_stone", CHBlocks.EXTRATERRESTRIAL_STONE, CHBlocks.EXTRATERRESTRIAL_STONE_SLAB, 2);
        stoneCutting(consumer, "ex_stone", CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE, CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_SLAB, 2);
        stoneCutting(consumer, "ex_stone", CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE, CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_SLAB, 2);
        stoneCutting(consumer, "ex_stone", CHBlocks.EXTRATERRESTRIAL_STONE_BRICKS, CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_SLAB, 2);
        stoneCutting(consumer, "ex_steel", CHBlocks.CARVED_STEEL_PLATE_BLOCK, CHBlocks.CARVED_STEEL_PLATE_SLAB, 2);

        stoneCutting(consumer, "ex_stone", CHBlocks.EXTRATERRESTRIAL_STONE, CHBlocks.EXTRATERRESTRIAL_STONE_STAIRS);
        stoneCutting(consumer, "ex_stone", CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE, CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_STAIRS);
        stoneCutting(consumer, "ex_stone", CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE, CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_STAIRS);
        stoneCutting(consumer, "ex_stone", CHBlocks.EXTRATERRESTRIAL_STONE_BRICKS, CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_STAIRS);
        stoneCutting(consumer, "ex_steel", CHBlocks.CARVED_STEEL_PLATE_BLOCK, CHBlocks.CARVED_STEEL_PLATE_STAIRS);

        stoneCutting(consumer, "ex_stone", CHBlocks.EXTRATERRESTRIAL_STONE, CHBlocks.EXTRATERRESTRIAL_STONE_WALL);
        stoneCutting(consumer, "ex_stone", CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE, CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_WALL);
        stoneCutting(consumer, "ex_stone", CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE, CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_WALL);
        stoneCutting(consumer, "ex_stone", CHBlocks.EXTRATERRESTRIAL_STONE_BRICKS, CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_WALL);

        stoneCutting(consumer, "ex_steel", CHBlocks.STEEL_PLATE_BLOCK, CHBlocks.EXTRATERRESTRIAL_STEEL_GRATE, 4);

        stoneCutting(consumer, "calcite", () -> Blocks.CALCITE, CHBlocks.CALCITE_TILES);
        stoneCutting(consumer, "calcite", CHBlocks.CALCITE_TILES, CHBlocks.CALCITE_TILE_SLAB, 2);
        stoneCutting(consumer, "calcite", CHBlocks.CALCITE_TILES, CHBlocks.CALCITE_TILE_STAIRS);

        hammerTime(consumer, "ex_steel/extraterrestrial_steel_plate", CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get(), CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get(), 4);
    }

    protected final void hammerTime(Consumer<FinishedRecipe> consumer, String name, ItemLike input, ItemLike output, int amount) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, amount)
                .requires(CHItems.EXTRATERRESTRIAL_HAMMER.get())
                .requires(input)
                .unlockedBy("has_item", has(input))
                .save(consumer, loc(name));
    }

    protected final void oneToOne(Consumer<FinishedRecipe> consumer, String name, ItemLike input, ItemLike output, int amount) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, amount)
                .requires(input)
                .unlockedBy("has_item", has(input))
                .save(consumer, loc(name));
    }

    protected final void twoByTwo(Consumer<FinishedRecipe> consumer, String name, ItemLike input, ItemLike output) {
        twoByTwo(consumer, name, input, output, 4);
    }

    protected final void twoByTwo(Consumer<FinishedRecipe> consumer, String name, ItemLike input, ItemLike output, int amount) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, amount)
                .pattern("##")
                .pattern("##")
                .define('#', input)
                .unlockedBy("has_item", has(input))
                .showNotification(false)
                .save(consumer, loc(name));
    }

    protected final void stairsBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> input, Supplier<? extends Block> output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', input.get())
                .unlockedBy("has_item", has(input.get()))
                .showNotification(false)
                .save(consumer, loc(name + "_stairs"));
    }

    protected final void slabBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> input, Supplier<? extends Block> output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output.get(), 6)
                .pattern("###")
                .define('#', input.get())
                .unlockedBy("has_item", has(input.get()))
                .showNotification(false)
                .save(consumer, loc(name + "_slab"));
    }

    protected final void wallBlock(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends Block> input, Supplier<? extends Block> output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output.get(), 6)
                .pattern("###")
                .pattern("###")
                .define('#', input.get())
                .unlockedBy("has_item", has(input.get()))
                .showNotification(false)
                .save(consumer, loc(name + "_wall"));
    }

    protected final void fullGrid(Consumer<FinishedRecipe> consumer, String name, ItemLike input, ItemLike output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, output, 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', input)
                .unlockedBy("has_item", has(input))
                .showNotification(false)
                .save(consumer, loc(name));
    }

    protected final ResourceLocation loc(String name) {
        return ClangingHowl.location(name);
    }

    public final void stoneCutting(Consumer<FinishedRecipe> consumer, String folder, Supplier<? extends Block> input, Supplier<? extends Block> output) {
        stoneCutting(consumer, folder, input, output, 1);
    }

    public final void stoneCutting(Consumer<FinishedRecipe> consumer, String folder, Supplier<? extends Block> input, Supplier<? extends Block> output, int amount) {
        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(input.get()), RecipeCategory.BUILDING_BLOCKS, output.get(), amount)
                .unlockedBy("has_item", has(input.get()))
                .save(consumer, loc(folder + "/" + getConversionRecipeName(input.get(), output.get()) + "_stonecutting"));
    }

    public final void smelting(Consumer<FinishedRecipe> consumer, String name, ItemLike input, ItemLike output){
        SimpleCookingRecipeBuilder
                .smelting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output, 0.35F, 200)
                .unlockedBy("has_item", has(input))
                .save(consumer, loc(name + "_smelting"));
    }

    public final void blasting(Consumer<FinishedRecipe> consumer, String name, ItemLike input, ItemLike output){
        blasting(consumer, name, input, output, 200);
    }

    public final void blasting(Consumer<FinishedRecipe> consumer, String name, ItemLike input, ItemLike output, int time){
        blasting(consumer, name, input, output, 0.35F, time);
    }

    public final void blasting(Consumer<FinishedRecipe> consumer, String name, ItemLike input, ItemLike output, float exp, int time){
        SimpleCookingRecipeBuilder
                .blasting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, output, exp, time)
                .unlockedBy("has_item", has(input))
                .save(consumer, loc(name + "_smelting"));
    }
}

package com.mongoose.clanginghowl.data;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.init.CHTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CHBlockTagsProvider extends IntrinsicHolderTagsProvider<Block> {

    public CHBlockTagsProvider(PackOutput p_256095_, CompletableFuture<HolderLookup.Provider> p_256572_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256095_, Registries.BLOCK, p_256572_, (p_256665_) -> {
            return p_256665_.builtInRegistryHolder().key();
        }, ClangingHowl.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_255894_) {
        this.tag(CHTags.Blocks.EX_STONE_BLOCKS)
                .addTag(CHTags.Blocks.EX_STONE)
                .addTag(CHTags.Blocks.SMOOTH_EX_STONE)
                .addTag(CHTags.Blocks.EX_STONE_BRICKS)
                .add(CHBlocks.EXTRATERRESTRIAL_COLUMN.get())
                .replace(false);
        this.tag(CHTags.Blocks.EX_STONE)
                .add(CHBlocks.EXTRATERRESTRIAL_STONE.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_SLAB.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_STAIRS.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_WALL.get())
                .add(CHBlocks.EXTRATERRESTRIAL_PEBBLE.get())
                .add(CHBlocks.INCANDESCENT_EXTRATERRESTRIAL_STONE.get())
                .replace(false);
        this.tag(CHTags.Blocks.SMOOTH_EX_STONE)
                .add(CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE.get())
                .add(CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_SLAB.get())
                .add(CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_STAIRS.get())
                .add(CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_WALL.get())
                .replace(false);
        this.tag(CHTags.Blocks.EX_STONE_BRICKS)
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_BRICKS.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_SLAB.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_STAIRS.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_WALL.get())
                .add(CHBlocks.CARVED_EXTRATERRESTRIAL_STONE_BRICKS.get())
                .replace(false);
        this.tag(CHTags.Blocks.EX_STEEL_BLOCKS)
                .addTag(CHTags.Blocks.CARVED_STEEL)
                .add(CHBlocks.RAW_EXTRATERRESTRIAL_STEEL_BLOCK.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STEEL_BLOCK.get())
                .add(CHBlocks.STEEL_PLATE_BLOCK.get())
                .add(CHBlocks.DAMAGED_STEEL_PLATE_BLOCK.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STEEL_GRATE.get())
                .add(CHBlocks.STEEL_BRIDGE.get())
                .add(CHBlocks.STEEL_ROD.get())
                .add(CHBlocks.STEEL_DOOR.get())
                .add(CHBlocks.STEEL_TRAPDOOR.get())
                .replace(false);
        this.tag(CHTags.Blocks.CARVED_STEEL)
                .add(CHBlocks.CARVED_STEEL_PLATE_BLOCK.get())
                .add(CHBlocks.CARVED_STEEL_PLATE_STAIRS.get())
                .add(CHBlocks.CARVED_STEEL_PLATE_SLAB.get())
                .add(CHBlocks.DAMAGED_CARVED_STEEL_PLATE_BLOCK.get())
                .replace(false);
        this.tag(CHTags.Blocks.CALCITE)
                .add(CHBlocks.CALCITE_TILES.get())
                .add(CHBlocks.CALCITE_TILE_STAIRS.get())
                .add(CHBlocks.CALCITE_TILE_SLAB.get())
                .add(CHBlocks.CRACKED_CALCITE_TILES.get())
                .replace(false);
        this.tag(CHTags.Blocks.ENERGY_CLUSTER)
                .add(CHBlocks.EXTRATERRESTRIAL_ENERGY_CLUSTER.get())
                .add(CHBlocks.HUGE_EXTRATERRESTRIAL_ENERGY_CLUSTER.get())
                .replace(false);
        this.tag(CHTags.Blocks.TECHNOFLESH)
                .add(CHBlocks.TECHNOFLESH_BLOCK.get())
                .add(CHBlocks.TECHNOFLESH_SLAB.get())
                .add(CHBlocks.FROZEN_TECHNOFLESH_BLOCK.get())
                .add(CHBlocks.TECHNOFLESH_MEMBRANE.get())
                .add(CHBlocks.HANGING_TECHNOFLESH.get())
                .add(CHBlocks.BIG_HANGING_TECHNOFLESH.get())
                .add(CHBlocks.NERVE_ENDINGS.get())
                .add(CHBlocks.TECHNOFLESH_NEST.get())
                .replace(false);
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(CHTags.Blocks.EX_STONE_BLOCKS)
                .addTag(CHTags.Blocks.EX_STEEL_BLOCKS)
                .addTag(CHTags.Blocks.CALCITE)
                .addTag(CHTags.Blocks.ENERGY_CLUSTER)
                .add(CHBlocks.METEORITE_STEEL_ORE.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STEEL_ORE.get())
                .add(CHBlocks.CRYSTAL_FORMER.get())
                .add(CHBlocks.STATIONARY_CHARGING_STATION.get())
                .replace(false);
        this.tag(BlockTags.MINEABLE_WITH_HOE)
                .addTag(CHTags.Blocks.TECHNOFLESH)
                .replace(false);
        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(CHBlocks.METEORITE_STEEL_ORE.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STEEL_ORE.get())
                .replace(false);
        this.tag(BlockTags.DOORS)
                .add(CHBlocks.STEEL_DOOR.get())
                .replace(false);
        this.tag(BlockTags.TRAPDOORS)
                .add(CHBlocks.STEEL_TRAPDOOR.get())
                .replace(false);
        this.tag(BlockTags.STAIRS)
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_STAIRS.get())
                .add(CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_STAIRS.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_STAIRS.get())
                .add(CHBlocks.CARVED_STEEL_PLATE_STAIRS.get())
                .add(CHBlocks.CALCITE_TILE_STAIRS.get())
                .replace(false);
        this.tag(BlockTags.SLABS)
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_SLAB.get())
                .add(CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_SLAB.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_SLAB.get())
                .add(CHBlocks.CARVED_STEEL_PLATE_SLAB.get())
                .add(CHBlocks.CALCITE_TILE_SLAB.get())
                .add(CHBlocks.TECHNOFLESH_SLAB.get())
                .replace(false);
        this.tag(BlockTags.WALLS)
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_WALL.get())
                .add(CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_WALL.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_WALL.get())
                .replace(false);
        this.tag(BlockTags.INFINIBURN_OVERWORLD)
                .add(CHBlocks.INCANDESCENT_EXTRATERRESTRIAL_STONE.get())
                .replace(false);
        this.tag(Tags.Blocks.STONE)
                .add(CHBlocks.EXTRATERRESTRIAL_STONE.get())
                .replace(false);
        this.tag(Tags.Blocks.ORES)
                .add(CHBlocks.METEORITE_STEEL_ORE.get())
                .add(CHBlocks.EXTRATERRESTRIAL_STEEL_ORE.get())
                .replace(false);
    }
}

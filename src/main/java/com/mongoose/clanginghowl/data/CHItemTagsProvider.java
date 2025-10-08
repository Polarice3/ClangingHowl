package com.mongoose.clanginghowl.data;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.common.items.CHItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CHItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {

    public CHItemTagsProvider(PackOutput p_256095_, CompletableFuture<HolderLookup.Provider> p_256572_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256095_, Registries.ITEM, p_256572_, (p_256665_) -> {
            return p_256665_.builtInRegistryHolder().key();
        }, ClangingHowl.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_255894_) {
        this.tag(ItemTags.SWORDS)
                .add(CHItems.EXTRATERRESTRIAL_SWORD.get())
                .add(CHItems.EXTRATERRESTRIAL_HAMMER.get())
                .replace(false);
        this.tag(ItemTags.SHOVELS)
                .add(CHItems.EXTRATERRESTRIAL_SHOVEL.get())
                .replace(false);
        this.tag(ItemTags.PICKAXES)
                .add(CHItems.EXTRATERRESTRIAL_PICKAXE.get())
                .replace(false);
        this.tag(ItemTags.AXES)
                .add(CHItems.EXTRATERRESTRIAL_AXE.get())
                .replace(false);
        this.tag(ItemTags.HOES)
                .add(CHItems.EXTRATERRESTRIAL_HOE.get())
                .replace(false);
        this.tag(ItemTags.STAIRS)
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_STAIRS.get().asItem())
                .add(CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_STAIRS.get().asItem())
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_STAIRS.get().asItem())
                .add(CHBlocks.CARVED_STEEL_PLATE_STAIRS.get().asItem())
                .add(CHBlocks.CALCITE_TILE_STAIRS.get().asItem())
                .replace(false);
        this.tag(ItemTags.SLABS)
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_SLAB.get().asItem())
                .add(CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_SLAB.get().asItem())
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_SLAB.get().asItem())
                .add(CHBlocks.CARVED_STEEL_PLATE_SLAB.get().asItem())
                .add(CHBlocks.CALCITE_TILE_SLAB.get().asItem())
                .replace(false);
        this.tag(ItemTags.WALLS)
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_WALL.get().asItem())
                .add(CHBlocks.SMOOTH_EXTRATERRESTRIAL_STONE_WALL.get().asItem())
                .add(CHBlocks.EXTRATERRESTRIAL_STONE_BRICK_WALL.get().asItem())
                .replace(false);
        this.tag(Tags.Items.INGOTS)
                .add(CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get())
                .replace(false);
        this.tag(Tags.Items.NUGGETS)
                .add(CHItems.EXTRATERRESTRIAL_STEEL_NUGGET.get())
                .replace(false);
        this.tag(Tags.Items.TOOLS)
                .add(CHItems.INDUSTRIAL_ADJUSTABLE_WRENCH.get())
                .replace(false);
    }
}

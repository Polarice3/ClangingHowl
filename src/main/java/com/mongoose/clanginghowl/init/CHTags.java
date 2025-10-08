package com.mongoose.clanginghowl.init;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CHTags {

    public static class Blocks {
        public static final TagKey<Block> EX_STONE_BLOCKS = tag("ex_stone_blocks");
        public static final TagKey<Block> EX_STONE = tag("ex_stone");
        public static final TagKey<Block> SMOOTH_EX_STONE = tag("smooth_ex_stone");
        public static final TagKey<Block> EX_STONE_BRICKS = tag("ex_stone_bricks");
        public static final TagKey<Block> EX_STEEL_BLOCKS = tag("ex_steel_blocks");
        public static final TagKey<Block> CARVED_STEEL = tag("carved_steel");
        public static final TagKey<Block> CALCITE = tag("calcite");
        public static final TagKey<Block> ENERGY_CLUSTER = tag("energy_cluster");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ClangingHowl.location(name));
        }
    }
}

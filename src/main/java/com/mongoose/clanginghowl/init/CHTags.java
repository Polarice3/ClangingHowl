package com.mongoose.clanginghowl.init;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

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

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> TECHNO_FLESH = tag("techno_flesh");

        public static final TagKey<EntityType<?>> TECHNO_CONVERT = tag("techno_convert");
        public static final TagKey<EntityType<?>> HOD_CONVERT = tag("hod_convert");
        public static final TagKey<EntityType<?>> REAPER_CONVERT = tag("reaper_convert");

        private static TagKey<EntityType<?>> tag(String name) {
            return create(ClangingHowl.location(name));
        }

        private static TagKey<EntityType<?>> create(ResourceLocation p_215874_) {
            return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), p_215874_);
        }
    }
}

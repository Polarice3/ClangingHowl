package com.mongoose.clanginghowl.init;

import net.minecraft.core.registries.BuiltInRegistries;
import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;

public class CHTags {

    public static class Blocks {
        public static final TagKey<Block> EX_STONE_BLOCKS = tag("ex_stone_blocks");
        public static final TagKey<Block> EX_STONE = tag("ex_stone");
        public static final TagKey<Block> SMOOTH_EX_STONE = tag("smooth_ex_stone");
        public static final TagKey<Block> EX_STONE_BRICKS = tag("ex_stone_bricks");
        public static final TagKey<Block> EX_STEEL_BLOCKS = tag("ex_steel_blocks");
        public static final TagKey<Block> LAMPS = tag("lamps");
        public static final TagKey<Block> FIREPROOF_STEEL_BLOCKS = tag("fireproof_steel_blocks");
        public static final TagKey<Block> CARVED_STEEL = tag("carved_steel");
        public static final TagKey<Block> CARVED_FIREPROOF_STEEL = tag("carved_fireproof_steel");
        public static final TagKey<Block> CALCITE = tag("calcite");
        public static final TagKey<Block> ENERGY_CLUSTER = tag("energy_cluster");
        public static final TagKey<Block> TECHNOFLESH = tag("technoflesh");
        public static final TagKey<Block> CANNOT_FROST = tag("cannot_frost");
        public static final TagKey<Block> BROKEN = tag("broken");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ClangingHowl.location(name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> TECHNO_FLESH = tag("techno_flesh");

        public static final TagKey<EntityType<?>> TECHNO_CONVERT = tag("techno_convert");
        public static final TagKey<EntityType<?>> HOD_CONVERT = tag("hod_convert");
        public static final TagKey<EntityType<?>> REAPER_CONVERT = tag("reaper_convert");
        public static final TagKey<EntityType<?>> HEMATOMA_CONVERT = tag("hematoma_convert");
        public static final TagKey<EntityType<?>> PROWLER_CONVERT = tag("prowler_convert");

        private static TagKey<EntityType<?>> tag(String name) {
            return create(ClangingHowl.location(name));
        }

        private static TagKey<EntityType<?>> create(ResourceLocation p_215874_) {
            return TagKey.create(Registries.ENTITY_TYPE, p_215874_);
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> COMMON_BLACKLIST = tag("mob_spawn/common_blacklist");
        public static final TagKey<Biome> HOD_SPAWN = tag("mob_spawn/heart_of_decay");
        public static final TagKey<Biome> EX_REAPER_SPAWN = tag("mob_spawn/ex_reaper");
        public static final TagKey<Biome> FLESH_MAIDEN_SPAWN = tag("mob_spawn/flesh_maiden");
        public static final TagKey<Biome> HEMATOMA_SPAWN = tag("mob_spawn/hematoma");
        public static final TagKey<Biome> BLOOD_SPREADER_SPAWN = tag("mob_spawn/blood_spreader");
        public static final TagKey<Biome> PROWLER_SPAWN = tag("mob_spawn/prowler");
        public static final TagKey<Biome> CARCASS_SPAWN = tag("mob_spawn/carcass");

        private static TagKey<Biome> tag(String name) {
            return create(ClangingHowl.location(name));
        }

        private static TagKey<Biome> create(ResourceLocation p_215874_) {
            return TagKey.create(Registries.BIOME, p_215874_);
        }
    }
}

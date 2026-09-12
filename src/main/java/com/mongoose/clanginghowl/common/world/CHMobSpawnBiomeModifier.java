package com.mongoose.clanginghowl.common.world;

import com.mojang.serialization.MapCodec;
import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Based of codes by @AlexModGuy
 */
public class CHMobSpawnBiomeModifier implements BiomeModifier {
    public static final MapCodec<CHMobSpawnBiomeModifier> CODEC = MapCodec.unit(CHMobSpawnBiomeModifier::new);

    public CHMobSpawnBiomeModifier() {
    }

    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            CHLevelRegistry.addBiomeSpawns(biome, builder);
        }
    }

    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }

    public static MapCodec<CHMobSpawnBiomeModifier> makeCodec() {
        return CODEC;
    }
}

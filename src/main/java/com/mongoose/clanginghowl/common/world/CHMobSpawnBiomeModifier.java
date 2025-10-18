package com.mongoose.clanginghowl.common.world;

import com.mojang.serialization.Codec;
import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Based of codes by @AlexModGuy
 */
public class CHMobSpawnBiomeModifier implements BiomeModifier {
    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation(ClangingHowl.MOD_ID, "mob_spawns"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ClangingHowl.MOD_ID);

    public CHMobSpawnBiomeModifier() {
    }

    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            CHLevelRegistry.addBiomeSpawns(biome, builder);
        }
    }

    public Codec<? extends BiomeModifier> codec() {
        return (Codec)SERIALIZER.get();
    }

    public static Codec<CHMobSpawnBiomeModifier> makeCodec() {
        return Codec.unit(CHMobSpawnBiomeModifier::new);
    }
}

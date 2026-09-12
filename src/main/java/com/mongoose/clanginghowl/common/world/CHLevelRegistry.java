package com.mongoose.clanginghowl.common.world;

import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.config.CHConfig;
import com.mongoose.clanginghowl.init.CHTags;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

public class CHLevelRegistry {

    public static void addBiomeSpawns(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (!biome.is(CHTags.Biomes.COMMON_BLACKLIST) && !biome.is(biomeResourceKey -> biomeResourceKey.registry().getNamespace().contains("alexscaves"))){
            if (biome.is(CHTags.Biomes.HOD_SPAWN) && CHConfig.HoDSpawnWeight.get() > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(CHEntityType.HEART_OF_DECAY.get(), CHConfig.HoDSpawnWeight.get(), CHConfig.HoDSpawnMinCount.get(), CHConfig.HoDSpawnMaxCount.get()));
            }
            if (biome.is(CHTags.Biomes.EX_REAPER_SPAWN) && CHConfig.ExReaperSpawnWeight.get() > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(CHEntityType.EX_REAPER.get(), CHConfig.ExReaperSpawnWeight.get(), CHConfig.ExReaperSpawnMinCount.get(), CHConfig.ExReaperSpawnMaxCount.get()));
            }
            if (biome.is(CHTags.Biomes.FLESH_MAIDEN_SPAWN) && CHConfig.FleshMaidenSpawnWeight.get() > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(CHEntityType.FLESH_MAIDEN.get(), CHConfig.FleshMaidenSpawnWeight.get(), CHConfig.FleshMaidenSpawnMinCount.get(), CHConfig.FleshMaidenSpawnMaxCount.get()));
            }
            if (biome.is(CHTags.Biomes.HEMATOMA_SPAWN) && CHConfig.HematomaSpawnWeight.get() > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(CHEntityType.HEMATOMA.get(), CHConfig.HematomaSpawnWeight.get(), CHConfig.HematomaSpawnMinCount.get(), CHConfig.HematomaSpawnMaxCount.get()));
            }
            if (biome.is(CHTags.Biomes.BLOOD_SPREADER_SPAWN) && CHConfig.BloodSpreaderSpawnWeight.get() > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(CHEntityType.BLOOD_SPREADER.get(), CHConfig.BloodSpreaderSpawnWeight.get(), CHConfig.BloodSpreaderSpawnMinCount.get(), CHConfig.BloodSpreaderSpawnMaxCount.get()));
            }
            if (biome.is(CHTags.Biomes.PROWLER_SPAWN) && CHConfig.ProwlerSpawnWeight.get() > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(CHEntityType.PROWLER.get(), CHConfig.ProwlerSpawnWeight.get(), CHConfig.ProwlerSpawnMinCount.get(), CHConfig.ProwlerSpawnMaxCount.get()));
            }
            if (biome.is(CHTags.Biomes.CARCASS_SPAWN) && CHConfig.CarcassSpawnWeight.get() > 0){
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(CHEntityType.CARCASS.get(), CHConfig.CarcassSpawnWeight.get(), CHConfig.CarcassSpawnMinCount.get(), CHConfig.CarcassSpawnMaxCount.get()));
            }
        }
    }

    public static boolean startName(ResourceKey<Biome> biomeResourceKey, String string){
        return biomeResourceKey.registry().getNamespace().startsWith(string);
    }

    public static boolean containsName(ResourceKey<Biome> biomeResourceKey, String string){
        return biomeResourceKey.registry().getNamespace().contains(string);
    }
}

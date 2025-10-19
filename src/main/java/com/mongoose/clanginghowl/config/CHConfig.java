package com.mongoose.clanginghowl.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class CHConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> TechnoFleshBuff;

    public static final ForgeConfigSpec.ConfigValue<Integer> HoDSpawnWeight;
    public static final ForgeConfigSpec.ConfigValue<Integer> HoDSpawnMinCount;
    public static final ForgeConfigSpec.ConfigValue<Integer> HoDSpawnMaxCount;
    public static final ForgeConfigSpec.ConfigValue<Integer> HoDDaySpawn;

    public static final ForgeConfigSpec.ConfigValue<Integer> ExReaperSpawnWeight;
    public static final ForgeConfigSpec.ConfigValue<Integer> ExReaperSpawnMinCount;
    public static final ForgeConfigSpec.ConfigValue<Integer> ExReaperSpawnMaxCount;
    public static final ForgeConfigSpec.ConfigValue<Integer> ExReaperDaySpawn;

    public static final ForgeConfigSpec.ConfigValue<Integer> FleshMaidenSpawnWeight;
    public static final ForgeConfigSpec.ConfigValue<Integer> FleshMaidenSpawnMinCount;
    public static final ForgeConfigSpec.ConfigValue<Integer> FleshMaidenSpawnMaxCount;
    public static final ForgeConfigSpec.ConfigValue<Integer> FleshMaidenDaySpawn;

    static {
        BUILDER.push("General");
        TechnoFleshBuff = BUILDER.comment("Whether Technoflesh creatures gain buffs as game time goes by, Default: true")
                        .define("TechnoFleshBuff", true);
        BUILDER.pop();
        BUILDER.push("Mob Spawn");
            BUILDER.push("Heart of Decay");
            HoDSpawnWeight = BUILDER.comment("Spawn Weight for Heart of Decay, Default: 15")
                    .defineInRange("HoDSpawnWeight", 15, 0, Integer.MAX_VALUE);
            HoDSpawnMinCount = BUILDER.comment("Spawn minimum group count for Heart of Decay, Default: 1")
                    .defineInRange("HoDSpawnMinCount", 1, 1, Integer.MAX_VALUE);
            HoDSpawnMaxCount = BUILDER.comment("Spawn maximum group count for Heart of Decay, must be equal or higher than min count, Default: 3")
                    .defineInRange("HoDSpawnMaxCount", 3, 1, Integer.MAX_VALUE);
            HoDDaySpawn = BUILDER.comment("How many days until Heart of Decay can spawn, set to -1 to disable, Default: 0")
                    .defineInRange("HoDDaySpawn", 0, -1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Ex Reaper");
            ExReaperSpawnWeight = BUILDER.comment("Spawn Weight for Ex Reaper, Default: 10")
                    .defineInRange("ExReaperSpawnWeight", 15, 0, Integer.MAX_VALUE);
            ExReaperSpawnMinCount = BUILDER.comment("Spawn minimum group count for Ex Reaper, Default: 1")
                    .defineInRange("ExReaperSpawnMinCount", 1, 1, Integer.MAX_VALUE);
            ExReaperSpawnMaxCount = BUILDER.comment("Spawn maximum group count for Ex Reaper, must be equal or higher than min count, Default: 2")
                    .defineInRange("ExReaperSpawnMaxCount", 2, 1, Integer.MAX_VALUE);
            ExReaperDaySpawn = BUILDER.comment("How many days until Ex Reaper can spawn, set to -1 to disable, Default: 15")
                    .defineInRange("ExReaperDaySpawn", 15, -1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Flesh Maiden");
            FleshMaidenSpawnWeight = BUILDER.comment("Spawn Weight for Flesh Maiden, Default: 10")
                    .defineInRange("FleshMaidenSpawnWeight", 15, 0, Integer.MAX_VALUE);
            FleshMaidenSpawnMinCount = BUILDER.comment("Spawn minimum group count for Flesh Maiden, Default: 1")
                    .defineInRange("FleshMaidenSpawnMinCount", 1, 1, Integer.MAX_VALUE);
            FleshMaidenSpawnMaxCount = BUILDER.comment("Spawn maximum group count for Flesh Maiden, must be equal or higher than min count, Default: 2")
                    .defineInRange("FleshMaidenSpawnMaxCount", 2, 1, Integer.MAX_VALUE);
            FleshMaidenDaySpawn = BUILDER.comment("How many days until Flesh Maiden can spawn, set to -1 to disable, Default: 15")
                    .defineInRange("FleshMaidenDaySpawn", 15, -1, Integer.MAX_VALUE);
            BUILDER.pop();
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path))
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        file.load();
        config.setConfig(file);
    }
}

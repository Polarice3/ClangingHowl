package com.mongoose.clanginghowl.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.io.File;

public class CHConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Boolean> TechnoFleshBuff;
    public static final ModConfigSpec.ConfigValue<Boolean> TechnoFleshBuffDayTime;
    public static final ModConfigSpec.ConfigValue<Boolean> MeteorShower;
    public static final ModConfigSpec.ConfigValue<Boolean> MeteorShowerFlash;
    public static final ModConfigSpec.ConfigValue<Boolean> CameraShake;

    public static final ModConfigSpec.ConfigValue<Integer> FixedAdaptationStage;
    public static final ModConfigSpec.ConfigValue<Integer> MeteorShowerDays;
    public static final ModConfigSpec.ConfigValue<Integer> MeteorShowerDuration;

    public static final ModConfigSpec.ConfigValue<Integer> HoDSpawnWeight;
    public static final ModConfigSpec.ConfigValue<Integer> HoDSpawnMinCount;
    public static final ModConfigSpec.ConfigValue<Integer> HoDSpawnMaxCount;
    public static final ModConfigSpec.ConfigValue<Integer> HoDDaySpawn;

    public static final ModConfigSpec.ConfigValue<Integer> ExReaperSpawnWeight;
    public static final ModConfigSpec.ConfigValue<Integer> ExReaperSpawnMinCount;
    public static final ModConfigSpec.ConfigValue<Integer> ExReaperSpawnMaxCount;
    public static final ModConfigSpec.ConfigValue<Integer> ExReaperDaySpawn;

    public static final ModConfigSpec.ConfigValue<Integer> FleshMaidenSpawnWeight;
    public static final ModConfigSpec.ConfigValue<Integer> FleshMaidenSpawnMinCount;
    public static final ModConfigSpec.ConfigValue<Integer> FleshMaidenSpawnMaxCount;
    public static final ModConfigSpec.ConfigValue<Integer> FleshMaidenDaySpawn;

    public static final ModConfigSpec.ConfigValue<Integer> HematomaSpawnWeight;
    public static final ModConfigSpec.ConfigValue<Integer> HematomaSpawnMinCount;
    public static final ModConfigSpec.ConfigValue<Integer> HematomaSpawnMaxCount;
    public static final ModConfigSpec.ConfigValue<Integer> HematomaDaySpawn;

    public static final ModConfigSpec.ConfigValue<Integer> BloodSpreaderSpawnWeight;
    public static final ModConfigSpec.ConfigValue<Integer> BloodSpreaderSpawnMinCount;
    public static final ModConfigSpec.ConfigValue<Integer> BloodSpreaderSpawnMaxCount;
    public static final ModConfigSpec.ConfigValue<Integer> BloodSpreaderDaySpawn;

    public static final ModConfigSpec.ConfigValue<Integer> ProwlerSpawnWeight;
    public static final ModConfigSpec.ConfigValue<Integer> ProwlerSpawnMinCount;
    public static final ModConfigSpec.ConfigValue<Integer> ProwlerSpawnMaxCount;
    public static final ModConfigSpec.ConfigValue<Integer> ProwlerDaySpawn;

    public static final ModConfigSpec.ConfigValue<Integer> CarcassSpawnWeight;
    public static final ModConfigSpec.ConfigValue<Integer> CarcassSpawnMinCount;
    public static final ModConfigSpec.ConfigValue<Integer> CarcassSpawnMaxCount;
    public static final ModConfigSpec.ConfigValue<Integer> CarcassDaySpawn;

    static {
        BUILDER.push("General");
        TechnoFleshBuff = BUILDER.comment("Whether Technoflesh creatures gain buffs as game time goes by, Default: true")
                        .define("TechnoFleshBuff", true);
        TechnoFleshBuffDayTime = BUILDER.comment("If 'TechnoFleshBuff' is set to true, whether it gains buffs via minecraft days instead of game time, Default: false")
                .define("TechnoFleshBuffDayTime", false);
        FixedAdaptationStage = BUILDER.comment("If set above 0 and 'TechnoFleshBuff' is set to true, Technoflesh creatures will always be buffed as though the 'TechnoFleshBuffDayTime' is enabled and the input amount of days have passed, set to 0 to disable, Default: 0")
                .defineInRange("FixedAdaptationStage", 0, 0, Integer.MAX_VALUE);
        MeteorShower = BUILDER.comment("Whether Meteor Showers can occur, Default: true")
                .define("MeteorShower", true);
        MeteorShowerFlash = BUILDER.comment("If 'MeteorShower' is enabled, whether Meteor Showers make flashes, Default: true")
                .define("MeteorShowerFlash", true);
        MeteorShowerDays = BUILDER.comment("If 'MeteorShower' is enabled, Meteor Showers will occur ever defined days, Default: 15")
                .defineInRange("MeteorShowerDays", 15, 1, Integer.MAX_VALUE);
        MeteorShowerDuration = BUILDER.comment("If 'MeteorShower' is enabled, how many seconds do Meteor Showers lasts, Default: 180")
                .defineInRange("MeteorShowerDuration", 180, 1, 550);
        CameraShake = BUILDER.comment("Players camera can shake. Default: true")
                .define("CameraShake", true);
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
            ExReaperSpawnWeight = BUILDER.comment("Spawn Weight for Ex Reaper, Default: 15")
                    .defineInRange("ExReaperSpawnWeight", 15, 0, Integer.MAX_VALUE);
            ExReaperSpawnMinCount = BUILDER.comment("Spawn minimum group count for Ex Reaper, Default: 1")
                    .defineInRange("ExReaperSpawnMinCount", 1, 1, Integer.MAX_VALUE);
            ExReaperSpawnMaxCount = BUILDER.comment("Spawn maximum group count for Ex Reaper, must be equal or higher than min count, Default: 2")
                    .defineInRange("ExReaperSpawnMaxCount", 2, 1, Integer.MAX_VALUE);
            ExReaperDaySpawn = BUILDER.comment("How many days until Ex Reaper can spawn, set to -1 to disable, Default: 15")
                    .defineInRange("ExReaperDaySpawn", 15, -1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Flesh Maiden");
            FleshMaidenSpawnWeight = BUILDER.comment("Spawn Weight for Flesh Maiden, Default: 15")
                    .defineInRange("FleshMaidenSpawnWeight", 15, 0, Integer.MAX_VALUE);
            FleshMaidenSpawnMinCount = BUILDER.comment("Spawn minimum group count for Flesh Maiden, Default: 1")
                    .defineInRange("FleshMaidenSpawnMinCount", 1, 1, Integer.MAX_VALUE);
            FleshMaidenSpawnMaxCount = BUILDER.comment("Spawn maximum group count for Flesh Maiden, must be equal or higher than min count, Default: 2")
                    .defineInRange("FleshMaidenSpawnMaxCount", 2, 1, Integer.MAX_VALUE);
            FleshMaidenDaySpawn = BUILDER.comment("How many days until Flesh Maiden can spawn, set to -1 to disable, Default: 15")
                    .defineInRange("FleshMaidenDaySpawn", 15, -1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Hematoma");
            HematomaSpawnWeight = BUILDER.comment("Spawn Weight for Hematoma, Default: 19")
                    .defineInRange("HematomaSpawnWeight", 19, 0, Integer.MAX_VALUE);
            HematomaSpawnMinCount = BUILDER.comment("Spawn minimum group count for Hematoma, Default: 1")
                    .defineInRange("HematomaSpawnMinCount", 1, 1, Integer.MAX_VALUE);
            HematomaSpawnMaxCount = BUILDER.comment("Spawn maximum group count for Hematoma, must be equal or higher than min count, Default: 2")
                    .defineInRange("HematomaSpawnMaxCount", 2, 1, Integer.MAX_VALUE);
            HematomaDaySpawn = BUILDER.comment("How many days until Hematoma can spawn, set to -1 to disable, Default: 15")
                    .defineInRange("HematomaDaySpawn", 15, -1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Blood Spreader");
            BloodSpreaderSpawnWeight = BUILDER.comment("Spawn Weight for Blood Spreader, Default: 15")
                    .defineInRange("BloodSpreaderSpawnWeight", 15, 0, Integer.MAX_VALUE);
            BloodSpreaderSpawnMinCount = BUILDER.comment("Spawn minimum group count for Blood Spreader, Default: 1")
                    .defineInRange("BloodSpreaderSpawnMinCount", 1, 1, Integer.MAX_VALUE);
            BloodSpreaderSpawnMaxCount = BUILDER.comment("Spawn maximum group count for Blood Spreader, must be equal or higher than min count, Default: 3")
                    .defineInRange("BloodSpreaderSpawnMaxCount", 3, 1, Integer.MAX_VALUE);
            BloodSpreaderDaySpawn = BUILDER.comment("How many days until Blood Spreader can spawn, set to -1 to disable, Default: 15")
                    .defineInRange("BloodSpreaderDaySpawn", 15, -1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Prowler");
            ProwlerSpawnWeight = BUILDER.comment("Spawn Weight for Prowler, Default: 10")
                    .defineInRange("ProwlerSpawnWeight", 10, 0, Integer.MAX_VALUE);
            ProwlerSpawnMinCount = BUILDER.comment("Spawn minimum group count for Prowler, Default: 1")
                    .defineInRange("ProwlerSpawnMinCount", 1, 1, Integer.MAX_VALUE);
            ProwlerSpawnMaxCount = BUILDER.comment("Spawn maximum group count for Prowler, must be equal or higher than min count, Default: 1")
                    .defineInRange("ProwlerSpawnMaxCount", 1, 1, Integer.MAX_VALUE);
            ProwlerDaySpawn = BUILDER.comment("How many days until Prowler can spawn, set to -1 to disable, Default: 15")
                    .defineInRange("ProwlerDaySpawn", 15, -1, Integer.MAX_VALUE);
            BUILDER.pop();
            BUILDER.push("Carcass");
            CarcassSpawnWeight = BUILDER.comment("Spawn Weight for Carcass, Default: 10")
                    .defineInRange("CarcassSpawnWeight", 10, 0, Integer.MAX_VALUE);
            CarcassSpawnMinCount = BUILDER.comment("Spawn minimum group count for Carcass, Default: 1")
                    .defineInRange("CarcassSpawnMinCount", 1, 1, Integer.MAX_VALUE);
            CarcassSpawnMaxCount = BUILDER.comment("Spawn maximum group count for Carcass, must be equal or higher than min count, Default: 1")
                    .defineInRange("CarcassSpawnMaxCount", 1, 1, Integer.MAX_VALUE);
            CarcassDaySpawn = BUILDER.comment("How many days until Carcass can spawn, set to -1 to disable, Default: 15")
                    .defineInRange("CarcassDaySpawn", 15, -1, Integer.MAX_VALUE);
            BUILDER.pop();
        BUILDER.pop();
        SPEC = BUILDER.build();
    }


}

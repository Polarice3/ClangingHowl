package com.mongoose.clanginghowl.common.blocks.entities.consummate_nest;

import com.mongoose.clanginghowl.common.entities.CHEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;

import java.util.*;

public class ConsummateNestData {
    public static final int COOLDOWN_TICKS = 30_000;
    public static final int EJECT_INTERVAL = 30;

    public int currentWave = 0;
    public int ticksSinceWaveStart = 0;
    public int ticksToNextWave = 0;
    public int ejectTimes = 0;
    public int hasEjected = 0;
    public long cooldownEnd = 0L;
    public final Set<UUID> livingMobs = new HashSet<>();
    public Optional<ResourceLocation> ejectingLootTable = Optional.empty();

    public static final List<List<EntityType<?>>> WAVE_POOLS = List.of(
            List.of(CHEntityType.HEART_OF_DECAY.get(), CHEntityType.HEMATOMA.get(), CHEntityType.EX_REAPER.get()),
            List.of(CHEntityType.EX_REAPER.get(), CHEntityType.FLESH_MAIDEN.get(), CHEntityType.BLOOD_SPREADER.get(), CHEntityType.HEMATOMA.get()),
            List.of(CHEntityType.FLESH_MAIDEN.get(), CHEntityType.BLOOD_SPREADER.get(), CHEntityType.EX_REAPER.get()),
            List.of(CHEntityType.FLESH_MAIDEN.get(), CHEntityType.BLOOD_SPREADER.get()),
            List.of(CHEntityType.FLESH_MAIDEN.get(), CHEntityType.BLOOD_SPREADER.get())
    );

    public static final int[] WAVE_COUNTS = {4, 5, 5, 4, 3};

    public boolean isReadyToEjectItems(ServerLevel serverLevel, float interval) {
        return (serverLevel.getGameTime() - (this.cooldownEnd - COOLDOWN_TICKS)) % interval == 0;
    }

    public boolean isCooldownFinished(ServerLevel serverLevel) {
        return serverLevel.getGameTime() >= this.cooldownEnd;
    }

    public void reset() {
        this.currentWave = 0;
        this.ticksSinceWaveStart = 0;
        this.ticksToNextWave = 0;
        this.ejectTimes = 0;
        this.hasEjected = 0;
        this.cooldownEnd = 0L;
        this.livingMobs.clear();
        this.ejectingLootTable = Optional.empty();
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("CurrentWave", this.currentWave);
        tag.putInt("TicksSinceWaveStart", this.ticksSinceWaveStart);
        tag.putInt("TicksToNextWave", this.ticksToNextWave);
        tag.putInt("EjectTimes", this.ejectTimes);
        tag.putInt("HasEjected", this.hasEjected);
        tag.putLong("CooldownEnd", this.cooldownEnd);
        ListTag mobList = new ListTag();
        for (UUID uuid : this.livingMobs) {
            CompoundTag uuidTag = new CompoundTag();
            uuidTag.putUUID("UUID", uuid);
            mobList.add(uuidTag);
        }
        tag.put("LivingMobs", mobList);
        this.ejectingLootTable.ifPresent(loc -> tag.putString("EjectingLootTable", loc.toString()));
        return tag;
    }

    public void load(CompoundTag tag) {
        this.currentWave = tag.getInt("CurrentWave");
        this.ticksSinceWaveStart = tag.getInt("TicksSinceWaveStart");
        this.ticksToNextWave = tag.getInt("TicksToNextWave");
        this.ejectTimes = tag.getInt("EjectTimes");
        this.hasEjected = tag.getInt("HasEjected");
        this.cooldownEnd = tag.getLong("CooldownEnd");
        this.livingMobs.clear();
        ListTag mobList = tag.getList("LivingMobs", 10);
        for (int i = 0; i < mobList.size(); i++) {
            this.livingMobs.add(mobList.getCompound(i).getUUID("UUID"));
        }
        this.ejectingLootTable = tag.contains("EjectingLootTable")
                ? Optional.of(ResourceLocation.parse(tag.getString("EjectingLootTable")))
                : Optional.empty();
    }
}

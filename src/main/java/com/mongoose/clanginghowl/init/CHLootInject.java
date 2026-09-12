package com.mongoose.clanginghowl.init;

import net.neoforged.fml.common.EventBusSubscriber;
import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.List;

@EventBusSubscriber(modid = ClangingHowl.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CHLootInject {

    private static final List<String> CHEST_TABLES = List.of("bastion_bridge", "bastion_treasure", "bastion_other", "nether_bridge");

    @SubscribeEvent
    public static void InjectLootTables(LootTableLoadEvent evt) {
        String chestsPrefix = "minecraft:chests/";
        String name = evt.getName().toString();

        if ((name.startsWith(chestsPrefix) && CHEST_TABLES.contains(name.substring(chestsPrefix.length())))) {
            String file = name.substring("minecraft:".length());
            evt.getTable().addPool(getInjectPool(file));
        }
    }

    private static LootPool getInjectPool(String entryName) {
        return LootPool.lootPool().add(getInjectEntry(entryName)).name("clanginghowl_inject_pool").build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name) {
        return NestedLootTable.lootTableReference(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE, ClangingHowl.location("inject/" + name)));
    }
}

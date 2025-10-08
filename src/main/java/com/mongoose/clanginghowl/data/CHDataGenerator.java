package com.mongoose.clanginghowl.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CHDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        generator.addProvider(event.includeServer(),
                new LootTableProvider(generator.getPackOutput(), Set.of(), List.of(
                        new LootTableProvider.SubProviderEntry(CHBlockLootProvider::new, LootContextParamSets.BLOCK)
                )));
        generator.addProvider(event.includeServer(), new CHCraftingProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new CHBlockStateProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new CHItemModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        DatapackBuiltinEntriesProvider datapackProvider = new CHRegisteryDataProvider(generator.getPackOutput(), provider);
        CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), datapackProvider);
        generator.addProvider(event.includeServer(), new CHItemTagsProvider(generator.getPackOutput(), lookupProvider, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new CHBlockTagsProvider(generator.getPackOutput(), lookupProvider, event.getExistingFileHelper()));
    }
}

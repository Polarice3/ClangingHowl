package com.mongoose.clanginghowl.compat;

import com.google.common.collect.ImmutableMap;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.compat.curios.CuriosIntegration;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class CHCompat {
    private static final Map<String, Supplier<CHCompatable>> MODULE_TYPES = ImmutableMap.<String, Supplier<CHCompatable>>builder()
            .put("curios", CuriosIntegration::new)
            .build();
    private static final Map<String, CHCompatable> MODULES = new HashMap<>();

    public static void setup(FMLCommonSetupEvent event) {
        populateModules(ModList.get()::isLoaded);
        MODULES.values().forEach(c -> c.setup(event));
    }

    private static void populateModules(Predicate<String> isLoaded) {
        for (Map.Entry<String, Supplier<CHCompatable>> entry : MODULE_TYPES.entrySet()) {
            String id = entry.getKey();
            if (isLoaded.test(id)) {
                MODULES.put(id, entry.getValue().get());
                ClangingHowl.LOGGER.info("Loading compat module for mod " + id);
            }
        }
    }
}

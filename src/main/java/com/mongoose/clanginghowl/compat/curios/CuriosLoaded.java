package com.mongoose.clanginghowl.compat.curios;

import net.neoforged.fml.ModList;

public enum CuriosLoaded {
    CURIOS("curios");
    private final boolean loaded;

    CuriosLoaded(String modid) {
        this.loaded = ModList.get() != null && ModList.get().getModContainerById(modid).isPresent();
    }

    public boolean isLoaded() {
        return this.loaded;
    }

}

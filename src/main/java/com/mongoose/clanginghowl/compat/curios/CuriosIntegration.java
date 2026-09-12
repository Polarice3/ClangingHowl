package com.mongoose.clanginghowl.compat.curios;

import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.curios.CHCurioItem;
import com.mongoose.clanginghowl.compat.CHCompatable;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosIntegration implements CHCompatable {
    @Override
    public void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> CHItems.ITEMS.getEntries().forEach(holder -> {
            if (holder.get() instanceof CHCurioItem item) {
                CuriosApi.registerCurio(item, item);
            }
        }));
    }
}

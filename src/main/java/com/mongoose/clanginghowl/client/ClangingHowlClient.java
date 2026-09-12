package com.mongoose.clanginghowl.client;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.init.ClientSideInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/** Loaded only on the physical client; common entrypoint bytecode remains server-safe. */
@Mod(value = ClangingHowl.MOD_ID, dist = Dist.CLIENT)
public final class ClangingHowlClient {
    public ClangingHowlClient(IEventBus modEventBus) {
        ClangingHowl.PROXY = new ClientProxy();
        new ClientSideInit().init(modEventBus);
    }
}

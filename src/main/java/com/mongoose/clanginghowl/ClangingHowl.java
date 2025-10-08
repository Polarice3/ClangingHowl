package com.mongoose.clanginghowl;

import com.mojang.logging.LogUtils;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.init.CHCreativeTab;
import com.mongoose.clanginghowl.init.ClientSideInit;
import com.mongoose.clanginghowl.init.SidedInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ClangingHowl.MOD_ID)
public class ClangingHowl {
    public static final String MOD_ID = "clanginghowl";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static SidedInit SIDED_INIT = DistExecutor.unsafeRunForDist(() -> ClientSideInit::new, () -> SidedInit::new);

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public ClangingHowl() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CHParticleTypes.PARTICLE_TYPES.register(modEventBus);
        CHCreativeTab.CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        CHItems.init();
        CHBlocks.init();
        SIDED_INIT.init();
    }
}

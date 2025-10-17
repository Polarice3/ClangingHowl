package com.mongoose.clanginghowl;

import com.mojang.logging.LogUtils;
import com.mongoose.clanginghowl.client.ClientProxy;
import com.mongoose.clanginghowl.client.inventory.menu.CHMenuTypes;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.CommonProxy;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.common.blocks.entities.CHBlockEntities;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.entities.hostiles.ExReaper;
import com.mongoose.clanginghowl.common.entities.hostiles.HeartOfDecay;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.network.CHNetwork;
import com.mongoose.clanginghowl.init.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ClangingHowl.MOD_ID)
public class ClangingHowl {
    public static final String MOD_ID = "clanginghowl";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static CHProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public static SidedInit SIDED_INIT = DistExecutor.unsafeRunForDist(() -> ClientSideInit::new, () -> SidedInit::new);

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public ClangingHowl() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CHBlockEntities.BLOCK_ENTITY.register(modEventBus);
        CHEntityType.ENTITY_TYPE.register(modEventBus);
        CHParticleTypes.PARTICLE_TYPES.register(modEventBus);
        CHMenuTypes.MENU_TYPE.register(modEventBus);
        CHEnchantments.ENCHANTMENTS.register(modEventBus);
        CHCreativeTab.CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupEntityAttributeCreation);

        MinecraftForge.EVENT_BUS.register(this);
        CHItems.init();
        CHBlocks.init();
        CHEffects.init();
        CHSounds.init();
        SIDED_INIT.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        CHNetwork.init();
    }

    private void setupEntityAttributeCreation(final EntityAttributeCreationEvent event) {
        event.put(CHEntityType.HEART_OF_DECAY.get(), HeartOfDecay.createAttributes().build());
        event.put(CHEntityType.EX_REAPER.get(), ExReaper.createAttributes().build());
    }
}

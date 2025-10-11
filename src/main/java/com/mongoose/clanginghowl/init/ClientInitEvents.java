package com.mongoose.clanginghowl.init;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.gui.screen.inventory.PortableChargerScreen;
import com.mongoose.clanginghowl.client.inventory.menu.CHMenuTypes;
import com.mongoose.clanginghowl.client.render.block.CHBlockEntityRenderer;
import com.mongoose.clanginghowl.common.blocks.entities.CHBlockEntities;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.energy.PortableChargerItem;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ClangingHowl.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInitEvents {

    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event){
        MenuScreens.register(CHMenuTypes.PORTABLE_CHARGER.get(), PortableChargerScreen::new);
        event.enqueueWork(() -> {
            ItemProperties.register(CHItems.PORTABLE_CHARGER.get(), new ResourceLocation("active")
                    , (stack, world, living, seed) -> PortableChargerItem.hasBatteries(stack) ? 1.0F : 0.0F);
        });
    }

    @SubscribeEvent
    public static void onRegisterRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CHBlockEntities.CRYSTAL_FORMER.get(), CHBlockEntityRenderer::new);
    }
}

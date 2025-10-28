package com.mongoose.clanginghowl.init;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.gui.screen.inventory.PortableChargerScreen;
import com.mongoose.clanginghowl.client.inventory.menu.CHMenuTypes;
import com.mongoose.clanginghowl.client.render.*;
import com.mongoose.clanginghowl.client.render.block.CHBlockEntityRenderer;
import com.mongoose.clanginghowl.client.render.block.ChargingStationRenderer;
import com.mongoose.clanginghowl.client.render.model.ExtraterrestrialReaperModel;
import com.mongoose.clanginghowl.client.render.model.FleshMaidenModel;
import com.mongoose.clanginghowl.client.render.model.HeartOfDecayModel;
import com.mongoose.clanginghowl.client.render.model.SpitProjectileModel;
import com.mongoose.clanginghowl.common.blocks.entities.CHBlockEntities;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
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
            ItemProperties.register(CHItems.ADVANCED_CHAINSWORD.get(), new ResourceLocation("active")
                    , (stack, world, living, seed) -> !IEnergyItem.isEmpty(stack) ? 1.0F : 0.0F);
            ItemProperties.register(CHItems.PORTABLE_CHARGER.get(), new ResourceLocation("active")
                    , (stack, world, living, seed) -> PortableChargerItem.hasBatteries(stack) ? 1.0F : 0.0F);
        });
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CHModelLayer.SPIT, SpitProjectileModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.HEART_OF_DECAY, HeartOfDecayModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.EXTRATERRESTRIAL_REAPER, ExtraterrestrialReaperModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.FLESH_MAIDEN, FleshMaidenModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CHBlockEntities.CRYSTAL_FORMER.get(), CHBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CHBlockEntities.STATIONARY_CHARGING_STATION.get(), ChargingStationRenderer::new);
        event.registerBlockEntityRenderer(CHBlockEntities.NERVE_ENDINGS.get(), CHBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CHBlockEntities.TECHNOFLESH_NEST.get(), CHBlockEntityRenderer::new);
        event.registerEntityRenderer(CHEntityType.SPIT_PROJECTILE.get(), SpitProjectileRenderer::new);
        event.registerEntityRenderer(CHEntityType.HEART_OF_DECAY.get(), HeartOfDecayRenderer::new);
        event.registerEntityRenderer(CHEntityType.EX_REAPER.get(), ExReaperRenderer::new);
        event.registerEntityRenderer(CHEntityType.FLESH_MAIDEN.get(), FleshMaidenRenderer::new);
    }
}

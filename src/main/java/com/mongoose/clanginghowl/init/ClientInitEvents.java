package com.mongoose.clanginghowl.init;

import net.neoforged.fml.common.EventBusSubscriber;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.gui.overlay.OverheatOverlay;
import com.mongoose.clanginghowl.client.gui.overlay.XRayOverlay;
import com.mongoose.clanginghowl.client.gui.screen.inventory.PortableChargerScreen;
import com.mongoose.clanginghowl.client.inventory.menu.CHMenuTypes;
import com.mongoose.clanginghowl.client.render.*;
import com.mongoose.clanginghowl.client.render.block.CHBlockEntityRenderer;
import com.mongoose.clanginghowl.client.render.block.ChargingStationRenderer;
import com.mongoose.clanginghowl.client.render.model.*;
import com.mongoose.clanginghowl.common.blocks.entities.CHBlockEntities;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.curios.XRayGoggles;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import com.mongoose.clanginghowl.common.items.energy.PortableChargerItem;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = ClangingHowl.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInitEvents {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(CHMenuTypes.PORTABLE_CHARGER.get(), PortableChargerScreen::new);
    }

    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event){
        CuriosRenderer.register();
        CHKeybindings.init();
        event.enqueueWork(() -> {
            ItemProperties.register(CHItems.ADVANCED_ENERGY_BATTERY.get(), ResourceLocation.parse("active")
                    , (stack, world, living, seed) -> !IEnergyItem.isEmpty(stack) ? 1.0F : 0.0F);
            ItemProperties.register(CHItems.ADVANCED_CHAINSWORD.get(), ResourceLocation.parse("active")
                    , (stack, world, living, seed) -> !IEnergyItem.isEmpty(stack) ? 1.0F : 0.0F);
            ItemProperties.register(CHItems.PORTABLE_CHARGER.get(), ResourceLocation.parse("active")
                    , (stack, world, living, seed) -> PortableChargerItem.hasBatteries(stack) ? 1.0F : 0.0F);
            ItemProperties.register(CHItems.X_RAY_GOGGLES.get(), ResourceLocation.parse("active")
                    , (stack, world, living, seed) -> XRayGoggles.isActivated(stack) ? 1.0F : 0.0F);
        });
    }

    @SubscribeEvent
    public static void registerGUI(final RegisterGuiLayersEvent event){
        event.registerAbove(VanillaGuiLayers.EXPERIENCE_BAR, ClangingHowl.location("overheat_overlay"), OverheatOverlay.OVERLAY);
        event.registerAbove(VanillaGuiLayers.TAB_LIST, ClangingHowl.location("x_ray_overlay"), XRayOverlay.OVERLAY);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CHModelLayer.X_RAY_GOGGLES, XRayGogglesModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.ENERGY_BARRIER_GENERATOR, EnergyBarrierGeneratorModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.TENDON_STRENGTHENER, TendonStrengthenerModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.ENERGY_GLOVE, EnergyGloveModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.JET_BOOTS, JetBootsModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.BLOODY_BATTERY, BloodyBatteryModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.REANIMATOR, ReanimatorModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.SPIT, SpitProjectileModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.SMALL_METEORITE, SmallMeteoriteModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.HEART_OF_DECAY, HeartOfDecayModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.EXTRATERRESTRIAL_REAPER, ExtraterrestrialReaperModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.FLESH_MAIDEN, FleshMaidenModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.HEMATOMA, HematomaModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.BLOOD_SPREADER, BloodSpreaderModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.BLOOD_CLOT, BloodClotModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.PROWLER, ProwlerModel::createBodyLayer);
        event.registerLayerDefinition(CHModelLayer.CARCASS, CarcassModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CHBlockEntities.CRYSTAL_FORMER.get(), CHBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CHBlockEntities.STATIONARY_CHARGING_STATION.get(), ChargingStationRenderer::new);
        event.registerBlockEntityRenderer(CHBlockEntities.BARRIER_OF_EXTRATERRESTRIAL_ACTIVITY.get(), CHBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CHBlockEntities.BROKEN_CRYSTAL_FORMER.get(), CHBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CHBlockEntities.FLAME_SPEWER.get(), CHBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CHBlockEntities.BROKEN_STEEL_LAMP.get(), CHBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CHBlockEntities.MOTION_SENSOR.get(), CHBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CHBlockEntities.NERVE_ENDINGS.get(), CHBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CHBlockEntities.TECHNOFLESH_NEST.get(), CHBlockEntityRenderer::new);
        event.registerEntityRenderer(CHEntityType.SPIT_PROJECTILE.get(), SpitProjectileRenderer::new);
        event.registerEntityRenderer(CHEntityType.SMALL_METEORITE.get(), SmallMeteoriteRenderer::new);
        event.registerEntityRenderer(CHEntityType.BLOOD_TRAIL.get(), NoProjectileRenderer::new);
        event.registerEntityRenderer(CHEntityType.HEART_OF_DECAY.get(), HeartOfDecayRenderer::new);
        event.registerEntityRenderer(CHEntityType.EX_REAPER.get(), ExReaperRenderer::new);
        event.registerEntityRenderer(CHEntityType.FLESH_MAIDEN.get(), FleshMaidenRenderer::new);
        event.registerEntityRenderer(CHEntityType.HEMATOMA.get(), HematomaRenderer::new);
        event.registerEntityRenderer(CHEntityType.BLOOD_SPREADER.get(), BloodSpreaderRenderer::new);
        event.registerEntityRenderer(CHEntityType.BLOODY_COPY.get(), BloodyCopyRenderer::new);
        event.registerEntityRenderer(CHEntityType.BLOOD_CLOT.get(), BloodClotRenderer::new);
        event.registerEntityRenderer(CHEntityType.PROWLER.get(), ProwlerRenderer::new);
        event.registerEntityRenderer(CHEntityType.CARCASS.get(), CarcassRenderer::new);
        event.registerEntityRenderer(CHEntityType.CAMERA_SHAKE.get(), NoopRenderer::new);
    }
}

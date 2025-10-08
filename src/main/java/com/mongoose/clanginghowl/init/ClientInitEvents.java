package com.mongoose.clanginghowl.init;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.render.block.CHBlockEntityRenderer;
import com.mongoose.clanginghowl.common.blocks.entities.CHBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ClangingHowl.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInitEvents {

    @SubscribeEvent
    public static void onRegisterRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CHBlockEntities.CRYSTAL_FORMER.get(), CHBlockEntityRenderer::new);
    }
}

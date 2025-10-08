package com.mongoose.clanginghowl.init;

import com.mongoose.clanginghowl.client.particles.BreakdownSmokeParticle;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.client.particles.CrystalLusterParticle;
import com.mongoose.clanginghowl.client.particles.RepairParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
public class ClientSideInit extends SidedInit {

    public void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupParticles);
    }

    public void setupParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(CHParticleTypes.CRYSTAL_LUSTER.get(), CrystalLusterParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.REPAIR.get(), RepairParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.BREAKDOWN_SMOKE.get(), BreakdownSmokeParticle.Provider::new);
    }
}

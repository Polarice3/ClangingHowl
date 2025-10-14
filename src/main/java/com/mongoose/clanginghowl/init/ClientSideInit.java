package com.mongoose.clanginghowl.init;

import com.mongoose.clanginghowl.client.particles.*;
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
        event.registerSpriteSet(CHParticleTypes.OVERDRIVE_FIRE.get(), OverdriveParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.FLAMETHROWER_FLAME.get(), FlamethrowerFlameParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.HORIZONTAL_ELECTRICAL_SPLASH.get(), ElectricSplashParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.VERTICAL_ELECTRIC_SHOCK.get(), ElectricShockParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.FIERY_EXPLOSION.get(), FieryExplosionParticle.Provider::new);
    }
}

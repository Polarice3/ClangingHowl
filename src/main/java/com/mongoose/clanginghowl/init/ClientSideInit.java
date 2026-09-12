package com.mongoose.clanginghowl.init;

import com.mongoose.clanginghowl.client.particles.*;
import net.minecraft.client.particle.ExplodeParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.bus.api.IEventBus;

@OnlyIn(Dist.CLIENT)
public class ClientSideInit extends SidedInit {

    public void init(IEventBus modEventBus) {
        modEventBus.addListener(this::setupParticles);
    }

    public void setupParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(CHParticleTypes.CRYSTAL_LUSTER.get(), CrystalLusterParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.REPAIR.get(), RepairParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.BREAKDOWN_SMOKE.get(), BreakdownSmokeParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.OVERDRIVE_FIRE.get(), OverdriveParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.FLAMETHROWER_FLAME.get(), FlamethrowerFlameParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.FLAMETHROWER_SOUL_FLAME.get(), FlamethrowerFlameParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.FLAMETHROWER_BURST.get(), FlamethrowerFlameParticle.BurstProvider::new);
        event.registerSpriteSet(CHParticleTypes.HORIZONTAL_ELECTRICAL_SPLASH.get(), ElectricSplashParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.VERTICAL_ELECTRIC_SHOCK.get(), ElectricShockParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.FIERY_EXPLOSION.get(), FieryExplosionParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.SPIT.get(), SpitParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.INFECTION.get(), InfectionParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.BLOOD_SPLASH.get(), BloodSplashParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.CRIMSON_POOF.get(), ExplodeParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.ENERGY_DISSOLUTION.get(), EnergyDissolutionParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.ENERGY_PARTICLE.get(), EnergyParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.BLOODY_ENERGY.get(), EnergyParticle.BloodyProvider::new);
        event.registerSpriteSet(CHParticleTypes.BIG_BLOODY_ENERGY.get(), EnergyParticle.BigBloodyProvider::new);
        event.registerSpriteSet(CHParticleTypes.GAS_SURGE.get(), GasSurgeParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.ELECTRIC_SPARK.get(), ElectricSparkParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.ROTATION.get(), RotationParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.METEORITE_TRAIL.get(), MeteoriteTrailParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.METEORITE_SPLIT.get(), MeteoriteSplitParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.NEUROTOXIN.get(), NeurotoxinParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.BLOOD_STAIN.get(), BloodStainParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.BLOODY_PROJECTILE.get(), BloodyProjectileParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.ATTRACTION_CLOUD.get(), AttractionCloudParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.ATTRACTION_SMOKE.get(), AttractionSmokeParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.QUAKE_SMOKE.get(), QuakeSmokeParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.ENERGETIC_EMERGENCE.get(), EnergeticEmergenceParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.SMALL_FIRE_SPLASH.get(), SmallFireSplashParticle.Provider::new);
        event.registerSpriteSet(CHParticleTypes.SHOCK_WAVE.get(), ShockWaveParticle.Provider::new);
    }
}

package com.mongoose.clanginghowl.client.particles;

import com.mojang.serialization.Codec;
import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHParticleTypes {
    public static DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ClangingHowl.MOD_ID);

    public static final RegistryObject<SimpleParticleType> CRYSTAL_LUSTER = PARTICLE_TYPES.register("crystal_luster",
            () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> REPAIR = PARTICLE_TYPES.register("repair",
            () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> BREAKDOWN_SMOKE = PARTICLE_TYPES.register("breakdown_smoke",
            () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> OVERDRIVE_FIRE = PARTICLE_TYPES.register("overdrive_fire",
            () -> new SimpleParticleType(false));

    public static final RegistryObject<ParticleType<ElectricSplashParticleOption>> HORIZONTAL_ELECTRICAL_SPLASH = PARTICLE_TYPES.register("horizontal_electrical_splash",
            () -> new ParticleType<>(false, ElectricSplashParticleOption.DESERIALIZER) {
                @Override
                public Codec<ElectricSplashParticleOption> codec() {
                    return ElectricSplashParticleOption.CODEC;
                }
            });

    public static final RegistryObject<ParticleType<ElectricShockParticleOption>> VERTICAL_ELECTRIC_SHOCK = PARTICLE_TYPES.register("vertical_electric_shock",
            () -> new ParticleType<>(false, ElectricShockParticleOption.DESERIALIZER) {
                @Override
                public Codec<ElectricShockParticleOption> codec() {
                    return ElectricShockParticleOption.CODEC;
                }
            });
}

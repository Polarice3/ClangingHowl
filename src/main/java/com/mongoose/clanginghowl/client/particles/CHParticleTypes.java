package com.mongoose.clanginghowl.client.particles;

import net.minecraft.core.registries.BuiltInRegistries;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CHParticleTypes {
    public static DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ClangingHowl.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CRYSTAL_LUSTER = PARTICLE_TYPES.register("crystal_luster",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> REPAIR = PARTICLE_TYPES.register("repair",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BREAKDOWN_SMOKE = PARTICLE_TYPES.register("breakdown_smoke",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> OVERDRIVE_FIRE = PARTICLE_TYPES.register("overdrive_fire",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLAMETHROWER_FLAME = PARTICLE_TYPES.register("flamethrower_flame",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLAMETHROWER_SOUL_FLAME = PARTICLE_TYPES.register("flamethrower_soul_flame",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLAMETHROWER_BURST = PARTICLE_TYPES.register("flamethrower_burst",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPIT = PARTICLE_TYPES.register("extraterrestrial_spit",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> INFECTION = PARTICLE_TYPES.register("sign_of_infection",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> CRIMSON_POOF = PARTICLE_TYPES.register("crimson_poof",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ENERGY_DISSOLUTION = PARTICLE_TYPES.register("energy_dissolution",
            () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ENERGY_PARTICLE = PARTICLE_TYPES.register("energy_particle",
            () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOODY_ENERGY = PARTICLE_TYPES.register("bloody_energy",
            () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIG_BLOODY_ENERGY = PARTICLE_TYPES.register("big_bloody_energy",
            () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GAS_SURGE = PARTICLE_TYPES.register("gas_surge",
            () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ELECTRIC_SPARK = PARTICLE_TYPES.register("electric_spark",
            () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> METEORITE_TRAIL = PARTICLE_TYPES.register("meteorite_trail",
            () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> METEORITE_SPLIT = PARTICLE_TYPES.register("meteorite_split",
            () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> NEUROTOXIN = PARTICLE_TYPES.register("neurotoxin",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOD_STAIN = PARTICLE_TYPES.register("blood_stain",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOODY_PROJECTILE = PARTICLE_TYPES.register("bloody_projectile",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ATTRACTION_CLOUD = PARTICLE_TYPES.register("attraction_cloud",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ATTRACTION_SMOKE = PARTICLE_TYPES.register("attraction_smoke",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> QUAKE_SMOKE = PARTICLE_TYPES.register("quake_smoke",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ENERGETIC_EMERGENCE = PARTICLE_TYPES.register("energetic_emergence",
            () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, ParticleType<ElectricSplashParticleOption>> HORIZONTAL_ELECTRICAL_SPLASH = PARTICLE_TYPES.register("horizontal_electrical_splash",
            () -> new ParticleType<>(true) {
                @Override
                public MapCodec<ElectricSplashParticleOption> codec() {
                    return ElectricSplashParticleOption.CODEC;
                }
                @Override
                public StreamCodec<FriendlyByteBuf, ElectricSplashParticleOption> streamCodec() {
                    return ElectricSplashParticleOption.STREAM_CODEC;
                }
            });

    public static final DeferredHolder<ParticleType<?>, ParticleType<ElectricShockParticleOption>> VERTICAL_ELECTRIC_SHOCK = PARTICLE_TYPES.register("vertical_electric_shock",
            () -> new ParticleType<>(false) {
                @Override
                public MapCodec<ElectricShockParticleOption> codec() {
                    return ElectricShockParticleOption.CODEC;
                }
                @Override
                public StreamCodec<FriendlyByteBuf, ElectricShockParticleOption> streamCodec() {
                    return ElectricShockParticleOption.STREAM_CODEC;
                }
            });

    public static final DeferredHolder<ParticleType<?>, ParticleType<FieryExplosionParticleOption>> FIERY_EXPLOSION = PARTICLE_TYPES.register("fiery_explosion",
            () -> new ParticleType<>(false) {
                @Override
                public MapCodec<FieryExplosionParticleOption> codec() {
                    return FieryExplosionParticleOption.CODEC;
                }
                @Override
                public StreamCodec<FriendlyByteBuf, FieryExplosionParticleOption> streamCodec() {
                    return FieryExplosionParticleOption.STREAM_CODEC;
                }
            });

    public static final DeferredHolder<ParticleType<?>, ParticleType<BloodSplashParticleOption>> BLOOD_SPLASH = PARTICLE_TYPES.register("blood_splash",
            () -> new ParticleType<>(false) {
                @Override
                public MapCodec<BloodSplashParticleOption> codec() {
                    return BloodSplashParticleOption.CODEC;
                }
                @Override
                public StreamCodec<FriendlyByteBuf, BloodSplashParticleOption> streamCodec() {
                    return BloodSplashParticleOption.STREAM_CODEC;
                }
            });

    public static final DeferredHolder<ParticleType<?>, ParticleType<RotationParticleOption>> ROTATION = PARTICLE_TYPES.register("rotation",
            () -> new ParticleType<>(false) {
                @Override
                public MapCodec<RotationParticleOption> codec() {
                    return RotationParticleOption.CODEC;
                }
                @Override
                public StreamCodec<FriendlyByteBuf, RotationParticleOption> streamCodec() {
                    return RotationParticleOption.STREAM_CODEC;
                }
            });

    public static final DeferredHolder<ParticleType<?>, ParticleType<SmallFireSplashParticleOption>> SMALL_FIRE_SPLASH = PARTICLE_TYPES.register("small_fire_splash",
            () -> new ParticleType<>(true) {
                @Override
                public MapCodec<SmallFireSplashParticleOption> codec() {
                    return SmallFireSplashParticleOption.CODEC;
                }
                @Override
                public StreamCodec<FriendlyByteBuf, SmallFireSplashParticleOption> streamCodec() {
                    return SmallFireSplashParticleOption.STREAM_CODEC;
                }
            });

    public static final DeferredHolder<ParticleType<?>, ParticleType<ShockWaveParticleOption>> SHOCK_WAVE = PARTICLE_TYPES.register("shock_wave",
            () -> new ParticleType<>(true) {
                @Override
                public MapCodec<ShockWaveParticleOption> codec() {
                    return ShockWaveParticleOption.CODEC;
                }
                @Override
                public StreamCodec<FriendlyByteBuf, ShockWaveParticleOption> streamCodec() {
                    return ShockWaveParticleOption.STREAM_CODEC;
                }
            });
}

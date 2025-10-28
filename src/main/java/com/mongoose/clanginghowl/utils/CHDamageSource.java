package com.mongoose.clanginghowl.utils;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CHDamageSource extends DamageSource {
    public static ResourceKey<DamageType> LIGHTNING = create("lightning");
    public static ResourceKey<DamageType> FIRE_STREAM = create("fire_stream");
    public static ResourceKey<DamageType> NEUROTOXIN = create("neurotoxin");
    public static ResourceKey<DamageType> ICICLE = create("icicle");

    public CHDamageSource(Holder<DamageType> p_270906_, @Nullable Entity p_270796_, @Nullable Entity p_270459_, @Nullable Vec3 p_270623_) {
        super(p_270906_, p_270796_, p_270459_, p_270623_);
    }

    public static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ClangingHowl.location(name));
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type) {
        return getEntityDamageSource(level, type, null);
    }

    public static DamageSource source(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, @Nullable Entity indirectAttacker){
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), attacker, indirectAttacker);
    }

    public static DamageSource noKnockbackDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, @Nullable Entity indirectAttacker) {
        return new NoKnockBackDamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), attacker, indirectAttacker);
    }

    public static DamageSource getEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker) {
        return indirectEntityDamageSource(level, type, attacker, attacker);
    }

    public static DamageSource indirectEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, @Nullable Entity indirectAttacker){
        return source(level, type, attacker, indirectAttacker);
    }

    public static DamageSource lightning(Entity pSource, @Nullable Entity pIndirectEntity) {
        return CHDamageSource.indirectEntityDamageSource(pSource.level(), LIGHTNING, pSource, pIndirectEntity);
    }

    public static DamageSource fireStream(Entity pSource, @Nullable Entity pIndirectEntity){
        return noKnockbackDamageSource(pSource.level(), FIRE_STREAM, pSource, pIndirectEntity);
    }

    public static boolean physicalAttacks(DamageSource source){
        return source.getDirectEntity() != null && source.getDirectEntity() instanceof LivingEntity
                && (source.getMsgId().equals("mob")
                || source.getMsgId().equals("sting")
                || source.getMsgId().equals("player"));
    }

    public static String source(String source){
        return "clanginghowl." + source;
    }

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(LIGHTNING, new DamageType("clanginghowl.lightning", 0.0F));
        context.register(FIRE_STREAM, new DamageType("clanginghowl.fire_stream", 0.0F, DamageEffects.BURNING));
        context.register(NEUROTOXIN, new DamageType("clanginghowl.neurotoxin", 0.0F));
        context.register(ICICLE, new DamageType("clanginghowl.icicle", 0.0F, DamageEffects.FREEZING));
    }
}

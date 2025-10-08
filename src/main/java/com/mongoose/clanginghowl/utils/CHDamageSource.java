package com.mongoose.clanginghowl.utils;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CHDamageSource extends DamageSource {
    public CHDamageSource(Holder<DamageType> p_270906_, @Nullable Entity p_270796_, @Nullable Entity p_270459_, @Nullable Vec3 p_270623_) {
        super(p_270906_, p_270796_, p_270459_, p_270623_);
    }

    public static boolean physicalAttacks(DamageSource source){
        return source.getDirectEntity() != null && source.getDirectEntity() instanceof LivingEntity
                && (source.getMsgId().equals("mob")
                || source.getMsgId().equals("sting")
                || source.getMsgId().equals("player"));
    }
}

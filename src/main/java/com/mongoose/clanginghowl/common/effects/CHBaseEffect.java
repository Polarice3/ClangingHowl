package com.mongoose.clanginghowl.common.effects;

import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.utils.CHDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class CHBaseEffect extends MobEffect {
    public CHBaseEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplify) {
        super.applyEffectTick(livingEntity, amplify);
        if (this == CHEffects.NEUROTOXIN.get()) {
            if (livingEntity.tickCount % 20 == 0 && CHCapHelper.isMoving(livingEntity)) {
                livingEntity.hurt(CHDamageSource.getDamageSource(livingEntity.level(), CHDamageSource.NEUROTOXIN), 1.0F + amplify);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}

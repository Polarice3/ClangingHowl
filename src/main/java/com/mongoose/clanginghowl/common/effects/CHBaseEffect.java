package com.mongoose.clanginghowl.common.effects;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.utils.CHDamageSource;
import com.mongoose.clanginghowl.utils.MobUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class CHBaseEffect extends MobEffect {
    public CHBaseEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplify) {
        super.applyEffectTick(livingEntity, amplify);
        if (this == CHEffects.NEUROTOXIN.get()) {
            if (livingEntity.tickCount % 20 == 0) {
                if (livingEntity.level() instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 8; ++i) {
                        serverLevel.sendParticles(CHParticleTypes.NEUROTOXIN.get(), livingEntity.getRandomX(0.5D), livingEntity.getY() + 0.5D, livingEntity.getRandomZ(0.5D), 1, 0.0D, 0.5D, 0.0D, 0);
                    }
                }
                // Player movement reports belong to the authenticated player only.
                // Mobs must not depend on client reports to receive this effect.
                boolean moving = livingEntity instanceof Player
                        ? CHCapHelper.isMoving(livingEntity) : MobUtil.isMoving(livingEntity);
                if (moving) {
                    livingEntity.hurt(CHDamageSource.getDamageSource(livingEntity.level(), CHDamageSource.NEUROTOXIN), 1.0F + amplify);
                }
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_19455_, int p_19456_) {
        return true;
    }
}

package com.mongoose.clanginghowl.mixin;

import com.mongoose.clanginghowl.common.effects.CHEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin {

    @Shadow public abstract MobEffect getEffect();

    @Inject(method = "endsWithin(I)Z", at = @At("HEAD"), cancellable = true)
    public void endsWithin(int p_268088_, CallbackInfoReturnable<Boolean> cir){
        if (this.getEffect() == CHEffects.OVERDRIVE.get()) {
            cir.setReturnValue(false);
        }
    }
}

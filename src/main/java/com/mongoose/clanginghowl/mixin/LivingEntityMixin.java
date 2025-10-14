package com.mongoose.clanginghowl.mixin;

import com.mongoose.clanginghowl.utils.NoKnockBackDamageSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "isDamageSourceBlocked", at = @At("HEAD"), cancellable = true)
    public void isDamageSourceBlocked(DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (damageSource instanceof NoKnockBackDamageSource damageSource1){
            if (!damageSource1.is(DamageTypeTags.BYPASSES_SHIELD) && livingEntity.isBlocking()) {
                Vec3 vec32 = damageSource1.getSourcePosition();
                if (vec32 != null) {
                    Vec3 vec3 = livingEntity.getViewVector(1.0F);
                    Vec3 vec31 = vec32.vectorTo(livingEntity.position()).normalize();
                    vec31 = new Vec3(vec31.x, 0.0D, vec31.z);
                    if (vec31.dot(vec3) < 0.0D) {
                        callbackInfoReturnable.setReturnValue(true);
                    }
                }
            }
        }
    }
}

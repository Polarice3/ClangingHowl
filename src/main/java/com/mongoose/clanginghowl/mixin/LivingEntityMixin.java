package com.mongoose.clanginghowl.mixin;

import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import com.mongoose.clanginghowl.utils.CHCuriosFinder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract boolean hasEffect(net.minecraft.core.Holder<MobEffect> p_21024_);

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "getJumpPower()F", at = @At("RETURN"), cancellable = true)
    protected void getJumpPower(CallbackInfoReturnable<Float> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (CHCuriosFinder.hasCurio(livingEntity, CHItems.TENDON_STRENGTHENER.get())) {
            ItemStack itemStack = CHCuriosFinder.findCurio(livingEntity, CHItems.TENDON_STRENGTHENER.get());
            if (!itemStack.isEmpty() && !IEnergyItem.isEmpty(itemStack)) {
                if (livingEntity.isSprinting()) {
                    IEnergyItem.decreaseEnergy(itemStack, 4);
                    cir.setReturnValue(cir.getReturnValueF() * 1.5F);
                }
            }
        }
    }
}

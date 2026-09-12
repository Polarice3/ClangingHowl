package com.mongoose.clanginghowl.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("lastHurtByMobTimestamp")
    void clanginghowl$setLastHurtByMobTimestamp(int timestamp);
}

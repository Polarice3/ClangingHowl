package com.mongoose.clanginghowl.common.items;

import net.minecraft.core.registries.BuiltInRegistries;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CHPotions {
    public static DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, ClangingHowl.MOD_ID);

    public static void init(IEventBus modEventBus){
        CHPotions.POTIONS.register(modEventBus);
    }

    public static final DeferredHolder<Potion, Potion> ATTRACTION = POTIONS.register("attraction", () -> new Potion(new MobEffectInstance(CHEffects.ATTRACTION, 1800)));
    public static final DeferredHolder<Potion, Potion> LONG_ATTRACTION = POTIONS.register("long_attraction", () -> new Potion("attraction", new MobEffectInstance(CHEffects.ATTRACTION, 3600)));
}

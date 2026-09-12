package com.mongoose.clanginghowl.common.effects;

import net.minecraft.core.registries.BuiltInRegistries;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.utils.CHUUIDUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CHEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ClangingHowl.MOD_ID);

    public static void init(IEventBus modEventBus){
        EFFECTS.register(modEventBus);
    }

    public static final DeferredHolder<MobEffect, MobEffect> COSMIC_IRRADIATION = EFFECTS.register("cosmic_irradiation",
            () -> new CHBaseEffect(MobEffectCategory.HARMFUL, 0xa583df)
                    .addAttributeModifier(Attributes.MAX_HEALTH, com.mongoose.clanginghowl.ClangingHowl.location("effect.clanginghowl.cosmic_irradiation.health"),
                            -0.15D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, com.mongoose.clanginghowl.ClangingHowl.location("effect.clanginghowl.cosmic_irradiation.attack"),
                            -0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, MobEffect> SAWING_UP_HEALTH = EFFECTS.register("sawing_up_health",
            () -> new CHBaseEffect(MobEffectCategory.HARMFUL, 0x831f33)
                    .addAttributeModifier(Attributes.MAX_HEALTH, com.mongoose.clanginghowl.ClangingHowl.location("effect.clanginghowl.sawing_up_health.health"),
                            -0.05D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, MobEffect> WEAKENED_DEFENSE = EFFECTS.register("weakened_defense",
            () -> new CHBaseEffect(MobEffectCategory.HARMFUL, 0x831f33)
                    .addAttributeModifier(Attributes.ARMOR, com.mongoose.clanginghowl.ClangingHowl.location("effect.clanginghowl.weakened_defense.armor"),
                            -0.4D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, MobEffect> DEEP_BURN = EFFECTS.register("deep_burn",
            () -> new CHBaseEffect(MobEffectCategory.HARMFUL, 0));

    public static final DeferredHolder<MobEffect, MobEffect> INTERNAL_HEAT = EFFECTS.register("internal_heat",
            () -> new CHBaseEffect(MobEffectCategory.HARMFUL, 0xe76900));

    public static final DeferredHolder<MobEffect, MobEffect> NEUROTOXIN = EFFECTS.register("neurotoxin",
            () -> new CHBaseEffect(MobEffectCategory.HARMFUL, 0xca3bbe));

    public static final DeferredHolder<MobEffect, MobEffect> BEYOND_FLESH = EFFECTS.register("beyond_flesh",
            () -> new CHBaseEffect(MobEffectCategory.HARMFUL, 0));

    public static final DeferredHolder<MobEffect, MobEffect> ATTRACTION = EFFECTS.register("attraction",
            () -> new CHBaseEffect(MobEffectCategory.NEUTRAL, 0xe36f6f));

    public static final DeferredHolder<MobEffect, MobEffect> OVERDRIVE = EFFECTS.register("overdrive",
            () -> new CHBaseEffect(MobEffectCategory.BENEFICIAL, 0)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, com.mongoose.clanginghowl.ClangingHowl.location("effect.clanginghowl.overdrive.movement"),
                            0.45D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, com.mongoose.clanginghowl.ClangingHowl.location("effect.clanginghowl.overdrive.attack"),
                            4.0D, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<MobEffect, MobEffect> ENLIGHTENED = EFFECTS.register("enlightened",
            () -> new CHBaseEffect(MobEffectCategory.HARMFUL, 0xb9e25b));
}

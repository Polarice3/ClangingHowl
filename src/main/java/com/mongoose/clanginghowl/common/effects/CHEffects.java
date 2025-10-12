package com.mongoose.clanginghowl.common.effects;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.utils.CHUUIDUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ClangingHowl.MOD_ID);

    public static void init(){
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<MobEffect> COSMIC_IRRADIATION = EFFECTS.register("cosmic_irradiation",
            () -> new CHBaseEffect(MobEffectCategory.HARMFUL, 0xa583df)
                    .addAttributeModifier(Attributes.MAX_HEALTH, CHUUIDUtil.uuidString("effect.clanginghowl.cosmic_irradiation.health"),
                            -0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, CHUUIDUtil.uuidString("effect.clanginghowl.cosmic_irradiation.attack"),
                            -0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<MobEffect> SAWING_UP_HEALTH = EFFECTS.register("sawing_up_health",
            () -> new CHBaseEffect(MobEffectCategory.HARMFUL, 0x831f33)
                    .addAttributeModifier(Attributes.MAX_HEALTH, CHUUIDUtil.uuidString("effect.clanginghowl.sawing_up_health.health"),
                            -0.05D, AttributeModifier.Operation.MULTIPLY_TOTAL));

}

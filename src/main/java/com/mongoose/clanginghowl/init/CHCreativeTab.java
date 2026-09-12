package com.mongoose.clanginghowl.init;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import com.mongoose.clanginghowl.common.items.CHItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CHCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ClangingHowl.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(ClangingHowl.MOD_ID, () -> CreativeModeTab.builder()
            .icon(() -> CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.clanginghowl"))
            .displayItems((parameters, output) -> {
                output.accept(CHItems.ENERGY_BATTERY.get().getPoweredItem());
                output.accept(CHItems.SMALL_ENERGY_BATTERY.get().getPoweredItem());
                output.accept(CHItems.ENERGY_INTENSIVE_BATTERY.get().getPoweredItem());
                output.accept(CHItems.ADVANCED_ENERGY_BATTERY.get().getPowerlessItem());
                output.accept(CHItems.ADVANCED_ENERGY_BATTERY.get().getPoweredItem());
                output.accept(CHItems.ADVANCED_HAND_DRILL.get().getPowerlessItem());
                output.accept(CHItems.ADVANCED_HAND_DRILL.get().getPoweredItem());
                output.accept(CHItems.ADVANCED_CHAINSAW.get().getPowerlessItem());
                output.accept(CHItems.ADVANCED_CHAINSAW.get().getPoweredItem());
                output.accept(CHItems.ADVANCED_CHAINSWORD.get().getPowerlessItem());
                output.accept(CHItems.ADVANCED_CHAINSWORD.get().getPoweredItem());
                output.accept(CHItems.FLAMETHROWER.get().getPowerlessItem());
                output.accept(CHItems.FLAMETHROWER.get().getPoweredItem());
                output.accept(CHItems.X_RAY_GOGGLES.get().getPowerlessItem());
                output.accept(CHItems.X_RAY_GOGGLES.get().getPoweredItem());
                output.accept(CHItems.ENERGY_BARRIER_GENERATOR.get().getPowerlessItem());
                output.accept(CHItems.ENERGY_BARRIER_GENERATOR.get().getPoweredItem());
                output.accept(CHItems.TENDON_STRENGTHENER.get().getPowerlessItem());
                output.accept(CHItems.TENDON_STRENGTHENER.get().getPoweredItem());
                output.accept(CHItems.ENERGY_GLOVE.get().getPowerlessItem());
                output.accept(CHItems.ENERGY_GLOVE.get().getPoweredItem());
                output.accept(CHItems.JET_BOOTS.get().getPowerlessItem());
                output.accept(CHItems.JET_BOOTS.get().getPoweredItem());
                output.accept(CHItems.BLOODY_BATTERY.get());
                output.accept(CHItems.REANIMATOR.get().getPowerlessItem());
                output.accept(CHItems.REANIMATOR.get().getPoweredItem());
                CHItems.ITEMS.getEntries().forEach(i -> {
                    if (i.isBound()) {
                        if (!CHItems.shouldSkipCreativeModTab(i.get())) {
                            output.accept(i.get());
                        }
                    }
                });
                var enchantments = parameters.holders().lookupOrThrow(Registries.ENCHANTMENT);
                CHEnchantments.ALL.forEach(key -> {
                    var holder = enchantments.getOrThrow(key);
                    for (int level = holder.value().getMinLevel(); level <= holder.value().getMaxLevel(); ++level) {
                        output.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(holder, level)));
                    }
                });
            }).build());
}

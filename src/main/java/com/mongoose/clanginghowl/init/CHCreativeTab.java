package com.mongoose.clanginghowl.init;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import com.mongoose.clanginghowl.common.items.CHItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CHCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ClangingHowl.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(ClangingHowl.MOD_ID, () -> CreativeModeTab.builder()
            .icon(() -> CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.clanginghowl"))
            .displayItems((parameters, output) -> {
                output.accept(CHItems.ENERGY_BATTERY.get().getPoweredItem());
                output.accept(CHItems.ENERGY_INTENSIVE_BATTERY.get().getPoweredItem());
                output.accept(CHItems.ADVANCED_HAND_DRILL.get().getPowerlessItem());
                output.accept(CHItems.ADVANCED_HAND_DRILL.get().getPoweredItem());
                output.accept(CHItems.ADVANCED_CHAINSAW.get().getPowerlessItem());
                output.accept(CHItems.ADVANCED_CHAINSAW.get().getPoweredItem());
                output.accept(CHItems.ADVANCED_CHAINSWORD.get().getPowerlessItem());
                output.accept(CHItems.ADVANCED_CHAINSWORD.get().getPoweredItem());
                output.accept(CHItems.FLAMETHROWER.get().getPowerlessItem());
                output.accept(CHItems.FLAMETHROWER.get().getPoweredItem());
                CHItems.ITEMS.getEntries().forEach(i -> {
                    if (i.isPresent()) {
                        if (!CHItems.shouldSkipCreativeModTab(i.get())) {
                            output.accept(i.get());
                        }
                    }
                });
                CHEnchantments.ENCHANTMENTS.getEntries().forEach(i -> {
                    if (i.isPresent()) {
                        for (int j = i.get().getMinLevel(); j <= i.get().getMaxLevel(); ++j) {
                            output.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(i.get(), j)));
                        }
                    }
                });
            }).build());
}

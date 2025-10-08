package com.mongoose.clanginghowl.init;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.items.CHItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CHCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ClangingHowl.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(ClangingHowl.MOD_ID, () -> CreativeModeTab.builder()
            .icon(() -> CHItems.EXTRATERRESTRIAL_STEEL.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.clanginghowl"))
            .displayItems((parameters, output) -> {
                CHItems.ITEMS.getEntries().forEach(i -> {
                    if (i.isPresent()) {
                        output.accept(i.get());
                    }
                });
            }).build());
}

package com.mongoose.clanginghowl.client.inventory.menu;

import net.minecraft.core.registries.BuiltInRegistries;
import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CHMenuTypes {
    public static DeferredRegister<MenuType<?>> MENU_TYPE = DeferredRegister.create(BuiltInRegistries.MENU, ClangingHowl.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<PortableChargerMenu>> PORTABLE_CHARGER = MENU_TYPE.register("portable_charger",
            () -> IMenuTypeExtension.create(PortableChargerMenu::createContainerClientSide));
}

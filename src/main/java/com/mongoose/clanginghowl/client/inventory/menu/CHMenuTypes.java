package com.mongoose.clanginghowl.client.inventory.menu;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHMenuTypes {
    public static DeferredRegister<MenuType<?>> MENU_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ClangingHowl.MOD_ID);

    public static final RegistryObject<MenuType<PortableChargerMenu>> PORTABLE_CHARGER = MENU_TYPE.register("portable_charger",
            () -> IForgeMenuType.create(PortableChargerMenu::createContainerClientSide));
}

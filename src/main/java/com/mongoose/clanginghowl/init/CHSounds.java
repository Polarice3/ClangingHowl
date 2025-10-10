package com.mongoose.clanginghowl.init;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHSounds {
    public static DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ClangingHowl.MOD_ID);

    public static void init(){
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SoundEvent> DRILLING = create("drilling");

    public static final RegistryObject<SoundEvent> CHAINSAW_IDLE = create("chainsaw_idle");
    public static final RegistryObject<SoundEvent> CHAINSAW_CUT = create("chainsaw_cut");
    public static final RegistryObject<SoundEvent> CHAINSAW_DISCHARGED = create("chainsaw_discharged");

    public static final RegistryObject<SoundEvent> DISCHARGED = create("discharged");

    static RegistryObject<SoundEvent> create(String name) {
        SoundEvent event = SoundEvent.createVariableRangeEvent(ClangingHowl.location(name));
        return SOUNDS.register(name, () -> event);
    }
}

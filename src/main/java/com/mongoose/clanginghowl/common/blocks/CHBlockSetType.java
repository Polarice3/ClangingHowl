package com.mongoose.clanginghowl.common.blocks;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class CHBlockSetType {
    public static final BlockSetType EX_STEEL =
            BlockSetType.register(new BlockSetType(ClangingHowl.location("extraterrestrial_steel").toString(),
                    true,
                    SoundType.METAL,
                    SoundEvents.IRON_DOOR_CLOSE,
                    SoundEvents.IRON_DOOR_OPEN,
                    SoundEvents.IRON_TRAPDOOR_CLOSE,
                    SoundEvents.IRON_TRAPDOOR_OPEN,
                    SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF,
                    SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON,
                    SoundEvents.STONE_BUTTON_CLICK_OFF,
                    SoundEvents.STONE_BUTTON_CLICK_ON));
}

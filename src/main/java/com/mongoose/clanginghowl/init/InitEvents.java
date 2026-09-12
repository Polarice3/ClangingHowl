package com.mongoose.clanginghowl.init;

import com.mojang.brigadier.CommandDispatcher;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.commands.CHCommands;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = ClangingHowl.MOD_ID)
public class InitEvents {
    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        CHCommands.register(commandDispatcher, event.getBuildContext());
    }
}

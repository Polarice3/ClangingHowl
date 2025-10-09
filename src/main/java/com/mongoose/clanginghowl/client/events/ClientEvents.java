package com.mongoose.clanginghowl.client.events;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.audio.ItemLoopSound;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.init.CHSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.sounds.SoundManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ClangingHowl.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Start event){
        if (event.getEntity().level() instanceof ClientLevel){
            Minecraft minecraft = Minecraft.getInstance();
            SoundManager soundHandler = minecraft.getSoundManager();
            if (event.getItem().is(CHItems.ADVANCED_HAND_DRILL.get())){
                soundHandler.play(new ItemLoopSound(CHSounds.DRILLING.get(), event.getEntity()));
            }
        }
    }
}

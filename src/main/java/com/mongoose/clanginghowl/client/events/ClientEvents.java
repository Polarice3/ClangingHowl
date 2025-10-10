package com.mongoose.clanginghowl.client.events;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.audio.ItemIdleSound;
import com.mongoose.clanginghowl.client.audio.ItemLoopSound;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.ChainsawItem;
import com.mongoose.clanginghowl.common.items.IEnergyItem;
import com.mongoose.clanginghowl.init.CHSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ClangingHowl.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (event.getLevel() instanceof ClientLevel){
            Minecraft minecraft = Minecraft.getInstance();
            SoundManager soundHandler = minecraft.getSoundManager();
            if (entity instanceof LivingEntity livingEntity) {
                soundHandler.play(new ItemIdleSound(CHSounds.CHAINSAW_IDLE.get(), livingEntity, CHItems.ADVANCED_CHAINSAW.get()));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHoldItem(LivingEvent.LivingTickEvent event) {
        Entity entity = event.getEntity();
        if (entity.level() instanceof ClientLevel) {
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity.isHolding(itemStack -> itemStack.getItem() instanceof ChainsawItem && !IEnergyItem.isEmpty(itemStack))) {
                    playItemIdleLoop(CHSounds.CHAINSAW_IDLE.get(), livingEntity, CHItems.ADVANCED_CHAINSAW.get());
                }
            }
        }
    }

    public static AbstractTickableSoundInstance ITEM_TICK;

    public static void playItemIdleLoop(SoundEvent soundEvent, LivingEntity livingEntity, Item item){
        Minecraft minecraft = Minecraft.getInstance();
        if (soundEvent != null && livingEntity.isAlive()) {
            if (ITEM_TICK == null) {
                ITEM_TICK = new ItemIdleSound(soundEvent, livingEntity, item);
            }
        } else {
            ITEM_TICK = null;
        }
        if (ITEM_TICK != null && !minecraft.getSoundManager().isActive(ITEM_TICK)) {
            Minecraft.getInstance().getSoundManager().play(ITEM_TICK);
        }
    }

    @SubscribeEvent
    public static void onItemUse(LivingEntityUseItemEvent.Start event){
        if (event.getEntity().level() instanceof ClientLevel){
            Minecraft minecraft = Minecraft.getInstance();
            SoundManager soundHandler = minecraft.getSoundManager();
            if (event.getItem().is(CHItems.ADVANCED_HAND_DRILL.get())){
                soundHandler.play(new ItemLoopSound(CHSounds.DRILLING.get(), event.getEntity()));
            }
            if (event.getItem().is(CHItems.ADVANCED_CHAINSAW.get())){
                soundHandler.play(new ItemLoopSound(CHSounds.CHAINSAW_CUT.get(), event.getEntity()));
            }
        }
    }
}

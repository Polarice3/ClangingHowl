package com.mongoose.clanginghowl.client.events;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.audio.ItemIdleSound;
import com.mongoose.clanginghowl.client.audio.ItemLoopSound;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.energy.ChainsawItem;
import com.mongoose.clanginghowl.common.items.energy.ChainswordItem;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import com.mongoose.clanginghowl.init.CHSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ClangingHowl.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onEntityHoldItem(LivingEvent.LivingTickEvent event) {
        Entity entity = event.getEntity();
        if (entity.level() instanceof ClientLevel) {
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity.isHolding(itemStack -> itemStack.getItem() instanceof ChainsawItem && !IEnergyItem.isEmpty(itemStack))) {
                    playItemIdleLoop(CHSounds.CHAINSAW_IDLE.get(), livingEntity, CHItems.ADVANCED_CHAINSAW.get(), 0.4F, 1.0F);
                } else if (livingEntity.isHolding(itemStack -> itemStack.getItem() instanceof ChainswordItem && !IEnergyItem.isEmpty(itemStack))) {
                    playItemIdleLoop(CHSounds.CHAINSAW_IDLE.get(), livingEntity, CHItems.ADVANCED_CHAINSWORD.get(), 0.3F, 1.0F);
                }
            }
        }
    }

    public static AbstractTickableSoundInstance ITEM_TICK;

    public static void playItemIdleLoop(SoundEvent soundEvent, LivingEntity livingEntity, Item item, float volume, float pitch){
        Minecraft minecraft = Minecraft.getInstance();
        if (soundEvent != null && livingEntity.isAlive()) {
            if (ITEM_TICK == null) {
                ITEM_TICK = new ItemIdleSound(soundEvent, livingEntity, item, volume, pitch);
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

    @SubscribeEvent
    public static void updateInputEvent(MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        Input input = event.getInput();
        if (player instanceof LocalPlayer localPlayer) {
            if (localPlayer.isUsingItem() && !localPlayer.isPassenger()) {
                if (localPlayer.getUseItem().is(itemHolder -> itemHolder.get() instanceof IEnergyItem)) {
                    input.leftImpulse *= 5.0F;
                    input.forwardImpulse *= 5.0F;
                }
            }
        }
    }
}

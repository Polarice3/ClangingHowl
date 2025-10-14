package com.mongoose.clanginghowl.client.audio;

import com.mongoose.clanginghowl.client.events.ClientEvents;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemIdleSound extends AbstractTickableSoundInstance {
    protected final Item item;

    public ItemIdleSound(SoundEvent soundEvent, LivingEntity entity, Item item) {
        this(soundEvent, entity, item, 0.4F, 1.0F);
    }

    public ItemIdleSound(SoundEvent soundEvent, LivingEntity entity, Item item, float volume, float pitch) {
        super(soundEvent, entity.getSoundSource(), SoundInstance.createUnseededRandom());
        this.item = item;
        this.x = (float)entity.getX();
        this.y = (float)entity.getY();
        this.z = (float)entity.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = volume;
        this.pitch = pitch;
    }

    public boolean canPlaySound() {
        return ClientEvents.ITEM_TICK == this;
    }

    @Override
    public boolean isStopped() {
        boolean flag = super.isStopped();
        if (flag) {
            if (ClientEvents.ITEM_TICK == this) {
                ClientEvents.ITEM_TICK = null;
            }
        }
        return flag;
    }

    public void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player != null) {
            if (player.isRemoved()
                    || !player.isAlive()
                    || !player.isHolding(this.item)){
                ClientEvents.ITEM_TICK = null;
                this.stop();
            } else {
                this.x = player.getX();
                this.y = player.getY();
                this.z = player.getZ();
                if (this.item instanceof IEnergyItem) {
                    ItemStack itemStack = ItemStack.EMPTY;
                    if (player.getMainHandItem().is(this.item)) {
                        itemStack = player.getMainHandItem();
                    } else if (player.getOffhandItem().is(this.item)) {
                        itemStack = player.getOffhandItem();
                    }
                    boolean flag;
                    if (!itemStack.isEmpty()) {
                        flag = IEnergyItem.isEmpty(itemStack);
                    } else {
                        flag = true;
                    }
                    if (flag) {
                        ClientEvents.ITEM_TICK = null;
                        this.stop();
                    }
                }
            }
        } else {
            ClientEvents.ITEM_TICK = null;
            this.stop();
        }

    }
}

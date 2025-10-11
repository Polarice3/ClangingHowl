package com.mongoose.clanginghowl.client.audio;

import com.mongoose.clanginghowl.client.events.ClientEvents;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemIdleSound extends AbstractTickableSoundInstance {
    protected final LivingEntity entity;
    protected final Item item;

    public ItemIdleSound(SoundEvent soundEvent, LivingEntity entity, Item item) {
        super(soundEvent, entity.getSoundSource(), SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.item = item;
        this.x = (float)entity.getX();
        this.y = (float)entity.getY();
        this.z = (float)entity.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 0.4F;
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
        if (this.entity.isRemoved()
                || !this.entity.isAlive()
                || !this.entity.isHolding(this.item)){
            ClientEvents.ITEM_TICK = null;
            this.stop();
        } else {
            this.x = this.entity.getX();
            this.y = this.entity.getY();
            this.z = this.entity.getZ();
            if (this.item instanceof IEnergyItem) {
                ItemStack itemStack = ItemStack.EMPTY;
                if (this.entity.getMainHandItem().is(this.item)) {
                    itemStack = this.entity.getMainHandItem();
                } else if (this.entity.getOffhandItem().is(this.item)) {
                    itemStack = this.entity.getOffhandItem();
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
    }
}

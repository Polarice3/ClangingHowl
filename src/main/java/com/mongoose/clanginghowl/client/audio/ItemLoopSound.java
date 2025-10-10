package com.mongoose.clanginghowl.client.audio;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;

public class ItemLoopSound extends AbstractTickableSoundInstance {
    protected final LivingEntity entity;

    public ItemLoopSound(SoundEvent soundEvent, LivingEntity entity) {
        super(soundEvent, entity.getSoundSource(), SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.x = (float)entity.getX();
        this.y = (float)entity.getY();
        this.z = (float)entity.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 0.4F;
    }

    public void tick() {
        if (this.entity.isRemoved() || !this.entity.isAlive() || !this.entity.isUsingItem()){
            this.stop();
        } else {
            this.x = this.entity.getX();
            this.y = this.entity.getY();
            this.z = this.entity.getZ();
        }
    }
}

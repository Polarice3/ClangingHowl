package com.mongoose.clanginghowl.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ExplodeParticle;
import net.minecraft.client.particle.SpriteSet;

public class EnergyDissolutionParticle extends ExplodeParticle {
    public EnergyDissolutionParticle(ClientLevel p_106576_, double p_106577_, double p_106578_, double p_106579_, double p_106580_, double p_106581_, double p_106582_, SpriteSet p_106583_) {
        super(p_106576_, p_106577_, p_106578_, p_106579_, p_106580_, p_106581_, p_106582_, p_106583_);
    }

    @Override
    public net.minecraft.world.phys.AABB getRenderBoundingBox(float partialTick) {
        return net.minecraft.world.phys.AABB.INFINITE;
    }
}

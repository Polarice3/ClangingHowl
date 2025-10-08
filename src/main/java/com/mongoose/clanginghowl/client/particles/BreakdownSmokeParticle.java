package com.mongoose.clanginghowl.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class BreakdownSmokeParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    protected BreakdownSmokeParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.scale(3.0F);
        this.setSize(0.25F, 0.25F);
        this.lifetime = 14;
        this.gravity = 3.0E-6F;
        this.xd = 0.0F;
        this.yd = 0.1F;
        this.zd = 0.0F;
        this.setSpriteFromAge(spriteSet);
    }

    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSprite(this.spriteSet.get((this.age / 2) % 10 + 1, 10));
        }
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet p_105899_) {
            this.sprites = p_105899_;
        }

        public Particle createParticle(SimpleParticleType p_105910_, ClientLevel p_105911_, double p_105912_, double p_105913_, double p_105914_, double p_105915_, double p_105916_, double p_105917_) {
            return new BreakdownSmokeParticle(p_105911_, p_105912_, p_105913_, p_105914_, p_105915_, p_105916_, p_105917_, this.sprites);
        }
    }
}

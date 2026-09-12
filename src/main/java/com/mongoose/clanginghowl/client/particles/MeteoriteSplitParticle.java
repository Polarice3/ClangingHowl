package com.mongoose.clanginghowl.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

public class MeteoriteSplitParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected MeteoriteSplitParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D);
        this.quadSize *= 10.0F;
        this.lifetime = 8;
        this.gravity = 0.0F;
        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;
        this.sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
    }

    public int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (++this.age >= this.lifetime) {
            this.remove();
        } else {
            this.setSprite(this.sprites.get((this.age / 2) % 5 + 1, 5));
        }
    }

    @Override
    public net.minecraft.world.phys.AABB getRenderBoundingBox(float partialTick) {
        return net.minecraft.world.phys.AABB.INFINITE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new MeteoriteSplitParticle(worldIn, x, y, z, this.spriteSet);
        }
    }
}

package com.mongoose.clanginghowl.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

public class MeteoriteTrailParticle extends TextureSheetParticle {
    private final float rotSpeed;
    private final SpriteSet sprites;

    protected MeteoriteTrailParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D);
        this.quadSize *= 1.6F;
        this.lifetime = 19;
        this.gravity = 0.0F;
        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;
        this.sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
        this.rotSpeed = ((float)Math.random() - 0.5F) * 0.1F;
        this.roll = (float)Math.random() * ((float)Math.PI * 2F);
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
            this.setSprite(this.sprites.get((this.age / 3) % 7 + 1, 7));
        }
        this.oRoll = this.roll;
        this.roll += (float)Math.PI * this.rotSpeed * 2.0F;
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
            return new MeteoriteTrailParticle(worldIn, x, y, z, this.spriteSet);
        }
    }
}

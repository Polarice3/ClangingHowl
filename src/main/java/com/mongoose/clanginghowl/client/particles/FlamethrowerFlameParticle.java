package com.mongoose.clanginghowl.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

public class FlamethrowerFlameParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    protected FlamethrowerFlameParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(0.2f, 0.2f);
        this.quadSize *= 1.6F;
        this.lifetime = 20; //Equation: (Total Frames * Interval) - 2
        this.friction = 0.96F;
        this.gravity = -0.1F;
        this.hasPhysics = false;
        this.xd = this.xd * (double)0.01F + vx;
        this.yd = this.yd * (double)0.01F + vy;
        this.zd = this.zd * (double)0.01F + vz;
        this.setSpriteFromAge(spriteSet);
    }

    public int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void move(double p_106817_, double p_106818_, double p_106819_) {
        this.setBoundingBox(this.getBoundingBox().move(p_106817_, p_106818_, p_106819_));
        this.setLocationFromBoundingbox();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            this.xd += this.random.nextFloat() / 500.0F * (float)(this.random.nextBoolean() ? 1 : -1);
            this.zd += this.random.nextFloat() / 500.0F * (float)(this.random.nextBoolean() ? 1 : -1);
            this.setSprite(this.spriteSet.get((this.age / 2) % 11 + 1, 11));
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlamethrowerFlameParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}

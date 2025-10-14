package com.mongoose.clanginghowl.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class FlamethrowerFlameParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    public boolean burst = false;

    protected FlamethrowerFlameParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, boolean burst, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(0.2f, 0.2f);
        this.quadSize *= 2.0F;
        this.lifetime = 20; //Equation: (Total Frames * Interval) - 2
        this.friction = 0.96F;
        this.gravity = -0.1F;
        this.hasPhysics = true;
        this.burst = burst;
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

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            if (!this.burst) {
                this.xd += this.random.nextFloat() / 500.0F * (float)(this.random.nextBoolean() ? 1 : -1);
                this.zd += this.random.nextFloat() / 500.0F * (float)(this.random.nextBoolean() ? 1 : -1);
            }
            this.setSprite(this.spriteSet.get((this.age / 2) % 11 + 1, 11));
            if (this.level.isWaterAt(BlockPos.containing(this.getPos()))) {
                this.level.addParticle(ParticleTypes.BUBBLE, this.x, this.y, this.z, this.xd, this.yd, this.zd);
                this.remove();
            }
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlamethrowerFlameParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, false, this.spriteSet);
        }
    }

    public static class BurstProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public BurstProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlamethrowerFlameParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, true, this.spriteSet);
        }
    }
}

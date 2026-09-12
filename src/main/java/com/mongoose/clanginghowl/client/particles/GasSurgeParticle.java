package com.mongoose.clanginghowl.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

public class GasSurgeParticle extends TextureSheetParticle {
   private final SpriteSet sprites;

   protected GasSurgeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
      super(level, x, y, z, xSpeed, ySpeed, zSpeed);
      this.quadSize = 2.0F;
      this.lifetime = 6;
      this.gravity = 0.0F;
      this.sprites = spriteSet;
      this.setSpriteFromAge(spriteSet);
   }

   public float getQuadSize(float p_234003_) {
      return this.quadSize;
   }

   public int getLightColor(float p_106921_) {
      return LightTexture.FULL_BRIGHT;
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      } else {
         this.setSpriteFromAge(this.sprites);
      }
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
         return new GasSurgeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
      }
   }
}
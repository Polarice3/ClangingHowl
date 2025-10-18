package com.mongoose.clanginghowl.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class BloodSplashParticle extends TextureSheetParticle {
   private int speed;
   private final SpriteSet spriteSet;

   public BloodSplashParticle(ClientLevel p_233976_, double p_233977_, double p_233978_, double p_233979_, SpriteSet spriteSet) {
      super(p_233976_, p_233977_, p_233978_, p_233979_, 0.0D, 0.0D, 0.0D);
      this.quadSize = 10.0F;
      this.lifetime = 9;
      this.gravity = 0.0F;
      this.xd = 0.0D;
      this.yd = 0.0D;
      this.zd = 0.0D;
      this.spriteSet = spriteSet;
      this.setSpriteFromAge(spriteSet);
   }

   public float getQuadSize(float p_234003_) {
      return this.quadSize;
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_LIT;
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      this.age += this.speed;
      if (this.age++ >= this.lifetime) {
         this.remove();
      } else {
         this.setSpriteFromAge(this.spriteSet);
      }
   }

   public static class Provider implements ParticleProvider<BloodSplashParticleOption> {
      private final SpriteSet sprite;

      public Provider(SpriteSet p_234008_) {
         this.sprite = p_234008_;
      }

      public Particle createParticle(BloodSplashParticleOption p_234019_, ClientLevel p_234020_, double p_234021_, double p_234022_, double p_234023_, double p_234024_, double p_234025_, double p_234026_) {
         BloodSplashParticle explodeParticle = new BloodSplashParticle(p_234020_, p_234021_, p_234022_, p_234023_, sprite);
         explodeParticle.speed = p_234019_.getSpeed();
         explodeParticle.quadSize = p_234019_.getSize();
         explodeParticle.setAlpha(1.0F);
         return explodeParticle;
      }
   }
}
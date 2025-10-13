package com.mongoose.clanginghowl.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;

public class ElectricShockParticle extends TextureSheetParticle {
   private int speed;
   private final SpriteSet sprites;

   protected ElectricShockParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet) {
      super(level, x, y, z, 0.0D, 0.0D, 0.0D);
      this.quadSize = 10.0F;
      this.lifetime = 9;
      this.gravity = 0.0F;
      this.xd = 0.0D;
      this.yd = 0.0D;
      this.zd = 0.0D;
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
      this.age += this.speed;
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
   public boolean shouldCull() {
      return false;
   }

   public static class Provider implements ParticleProvider<ElectricShockParticleOption> {
      private final SpriteSet sprite;

      public Provider(SpriteSet p_234008_) {
         this.sprite = p_234008_;
      }

      public Particle createParticle(ElectricShockParticleOption p_234019_, ClientLevel p_234020_, double p_234021_, double p_234022_, double p_234023_, double p_234024_, double p_234025_, double p_234026_) {
         ElectricShockParticle shockwaveParticle = new ElectricShockParticle(p_234020_, p_234021_, p_234022_, p_234023_, sprite);
         shockwaveParticle.speed = p_234019_.getSpeed();
         shockwaveParticle.quadSize = p_234019_.getSize();
         shockwaveParticle.setAlpha(1.0F);
         return shockwaveParticle;
      }
   }
}
package com.mongoose.clanginghowl.client.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;

public class BloodSplashParticleOption implements ParticleOptions {
   public static final Codec<BloodSplashParticleOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
           Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
           Codec.INT.fieldOf("speed").forGetter(d -> d.speed)
   ).apply(instance, BloodSplashParticleOption::new));
   public static final Deserializer<BloodSplashParticleOption> DESERIALIZER = new Deserializer<>() {
      public BloodSplashParticleOption fromCommand(ParticleType<BloodSplashParticleOption> p_235961_, StringReader p_235962_) throws CommandSyntaxException {
         p_235962_.expect(' ');
         float s = p_235962_.readFloat();
         p_235962_.expect(' ');
         int s2 = p_235962_.readInt();
         return new BloodSplashParticleOption(s, s2);
      }

      public BloodSplashParticleOption fromNetwork(ParticleType<BloodSplashParticleOption> p_235964_, FriendlyByteBuf p_235965_) {
         return new BloodSplashParticleOption(p_235965_.readFloat(), p_235965_.readInt());
      }
   };
   private final float size;
   private final int speed;

   public BloodSplashParticleOption() {
      this.size = 10;
      this.speed = 0;
   }

   public BloodSplashParticleOption(float size, int speed) {
      this.size = size;
      this.speed = speed;
   }

   public void writeToNetwork(FriendlyByteBuf p_235956_) {
      p_235956_.writeFloat(this.size);
      p_235956_.writeInt(this.speed);
   }

   public String writeToString() {
      return String.format(Locale.ROOT, "%s %.2f %s",
              BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.size, this.speed);
   }

   public ParticleType<BloodSplashParticleOption> getType() {
      return CHParticleTypes.BLOOD_SPLASH.get();
   }

   public float getSize(){
      return this.size;
   }

   public int getSpeed(){
      return this.speed;
   }
}
package com.mongoose.clanginghowl.client.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;

public class ElectricSplashParticleOption implements ParticleOptions {
   public static final MapCodec<ElectricSplashParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
           Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
           Codec.INT.fieldOf("speed").forGetter(d -> d.speed)
   ).apply(instance, ElectricSplashParticleOption::new));
   public static final StreamCodec<FriendlyByteBuf, ElectricSplashParticleOption> STREAM_CODEC = StreamCodec.of(
           (buffer, option) -> option.writeToNetwork(buffer),
           buffer -> new ElectricSplashParticleOption(buffer.readFloat(), buffer.readInt()));
   private final float size;
   private final int speed;

   public ElectricSplashParticleOption() {
      this.size = 10;
      this.speed = 0;
   }

   public ElectricSplashParticleOption(float size, int speed) {
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

   public ParticleType<ElectricSplashParticleOption> getType() {
      return CHParticleTypes.HORIZONTAL_ELECTRICAL_SPLASH.get();
   }

   public float getSize(){
      return this.size;
   }

   public int getSpeed(){
      return this.speed;
   }
}

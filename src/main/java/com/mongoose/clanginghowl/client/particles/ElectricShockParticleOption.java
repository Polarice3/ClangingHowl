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

public class ElectricShockParticleOption implements ParticleOptions {
    public static final Codec<ElectricShockParticleOption> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
            Codec.INT.fieldOf("speed").forGetter(d -> d.speed)
    ).apply(instance, ElectricShockParticleOption::new));
    public static final Deserializer<ElectricShockParticleOption> DESERIALIZER = new Deserializer<>() {
        public ElectricShockParticleOption fromCommand(ParticleType<ElectricShockParticleOption> p_235961_, StringReader p_235962_) throws CommandSyntaxException {
            p_235962_.expect(' ');
            float s = p_235962_.readFloat();
            p_235962_.expect(' ');
            int s2 = p_235962_.readInt();
            return new ElectricShockParticleOption(s, s2);
        }

        public ElectricShockParticleOption fromNetwork(ParticleType<ElectricShockParticleOption> p_235964_, FriendlyByteBuf p_235965_) {
            return new ElectricShockParticleOption(p_235965_.readFloat(), p_235965_.readInt());
        }
    };
    private final float size;
    private final int speed;

    public ElectricShockParticleOption(float size, int speed) {
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

    public ParticleType<ElectricShockParticleOption> getType() {
        return CHParticleTypes.VERTICAL_ELECTRIC_SHOCK.get();
    }

    public float getSize(){
        return this.size;
    }

    public int getSpeed(){
        return this.speed;
    }
}

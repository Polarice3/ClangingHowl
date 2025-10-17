package com.mongoose.clanginghowl.common.capabilities;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CHCapUpdatePacket {
    private final int entityID;
    private CompoundTag tag;

    public CHCapUpdatePacket(int id, CompoundTag tag) {
        this.entityID = id;
        this.tag = tag;
    }

    public CHCapUpdatePacket(LivingEntity living) {
        this.entityID = living.getId();
        living.getCapability(CHCapProvider.CAPABILITY, null).ifPresent((soulEnergy) -> {
            this.tag = CHCapHelper.save(new CompoundTag(), soulEnergy);
        });
    }

    public static void encode(CHCapUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityID);
        buffer.writeNbt(packet.tag);
    }

    public static CHCapUpdatePacket decode(FriendlyByteBuf buffer) {
        return new CHCapUpdatePacket(buffer.readInt(), buffer.readNbt());
    }

    public static void consume(CHCapUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                Level level = ClangingHowl.PROXY.getLevel();
                if (level instanceof ClientLevel clientLevel) {
                    Entity entity = clientLevel.getEntity(packet.entityID);
                    if (entity != null) {
                        entity.getCapability(CHCapProvider.CAPABILITY).ifPresent((misc) -> {
                            CHCapHelper.load(packet.tag, misc);
                        });
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

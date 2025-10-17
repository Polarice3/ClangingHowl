package com.mongoose.clanginghowl.common.network.client;

import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CIsMovingPacket {
    private final int entityID;
    private boolean moving;

    public CIsMovingPacket(int entityID, boolean moving) {
        this.entityID = entityID;
        this.moving = moving;
    }

    public static void encode(CIsMovingPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityID);
        buf.writeBoolean(packet.moving);
    }

    public static CIsMovingPacket decode(FriendlyByteBuf buf) {
        return new CIsMovingPacket(
                buf.readInt(),
                buf.readBoolean()
        );
    }

    public static void consume(CIsMovingPacket packet, Supplier<NetworkEvent.Context> ctx) {
        if (packet != null) {
            ctx.get().setPacketHandled(true);
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    Entity entity = player.level().getEntity(packet.entityID);
                    if (entity instanceof LivingEntity livingEntity) {
                        CHCapHelper.setMoving(livingEntity, packet.moving);
                    }
                }
            });
        }
    }
}

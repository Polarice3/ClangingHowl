package com.mongoose.clanginghowl.common.capabilities;

import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CHCapUpdatePacket {
    private CompoundTag tag;

    public CHCapUpdatePacket(CompoundTag tag) {
        this.tag = tag;
    }

    public CHCapUpdatePacket(Player player) {
        player.getCapability(CHCapProvider.CAPABILITY, null).ifPresent((soulEnergy) -> {
            this.tag = CHCapHelper.save(new CompoundTag(), soulEnergy);
        });
    }

    public static void encode(CHCapUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeNbt(packet.tag);
    }

    public static CHCapUpdatePacket decode(FriendlyByteBuf buffer) {
        return new CHCapUpdatePacket(buffer.readNbt());
    }

    public static void consume(CHCapUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                Player player = ClangingHowl.PROXY.getPlayer();
                if (player != null) {
                    player.getCapability(CHCapProvider.CAPABILITY).ifPresent((soulEnergy) -> {
                        CHCapHelper.load(packet.tag, soulEnergy);
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

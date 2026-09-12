package com.mongoose.clanginghowl.client.network;

import com.mongoose.clanginghowl.client.world.CHClientWorld;
import com.mongoose.clanginghowl.client.world.ICHClientWorld;
import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.common.capabilities.CHCapUpdatePacket;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.network.server.SInstaLookPacket;
import com.mongoose.clanginghowl.common.network.server.SPlayWorldSoundPacket;
import com.mongoose.clanginghowl.common.network.server.SReanimatorDeathPacket;
import com.mongoose.clanginghowl.common.network.server.SSendCHWorldData;
import com.mongoose.clanginghowl.utils.MobUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/** Invoked only by clientbound play handlers, already on the main game thread. */
@OnlyIn(Dist.CLIENT)
public final class CHClientPayloadHandlers {
    private CHClientPayloadHandlers() {
    }

    public static void handle(SInstaLookPacket packet) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        Entity looker = level.getEntity(packet.looker);
        Entity target = level.getEntity(packet.target);
        if (looker instanceof Mob mob && target != null) {
            MobUtil.instaLook(mob, target);
        }
    }

    public static void handle(SPlayWorldSoundPacket packet) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            level.playLocalSound(packet.blockPos(), packet.soundEvent(), SoundSource.NEUTRAL,
                    packet.volume(), packet.pitch(), false);
        }
    }

    public static void handle(SSendCHWorldData packet) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        // ClientLevelMixin is required; a broken mixin must remain an observable failure.
        CHClientWorld world = ((ICHClientWorld) level).getCHClientWorld();
        world.setMeteorShower(packet.isMeteorShower);
        world.setMeteorFlash(packet.isMeteorFlash);
        world.setPlayMeteorExplode(packet.playMeteorExplode);
        world.setPlayMeteorMusic(packet.playMeteorMusic);
        world.setMeteorMusicFullVolume(packet.isMeteorMusicFullVolume);
        world.setMeteorFlashTick(packet.meteorFlashTick);
    }

    public static void handle(SReanimatorDeathPacket packet) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player != null) {
            player.level().playLocalSound(player.getX(), player.getY(), player.getZ(),
                    SoundEvents.TOTEM_USE, player.getSoundSource(), 0.25F, 1.0F, false);
            minecraft.gameRenderer.displayItemActivation(new ItemStack(CHItems.REANIMATION.get()));
        }
    }

    public static void handle(CHCapUpdatePacket packet) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        Entity entity = level.getEntity(packet.entityID());
        if (entity instanceof LivingEntity living) {
            CHCapHelper.load(packet.tag(), CHCapHelper.getCapability(living));
        }
    }
}

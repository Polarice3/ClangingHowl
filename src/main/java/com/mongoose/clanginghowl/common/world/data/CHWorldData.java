package com.mongoose.clanginghowl.common.world.data;

import com.mongoose.clanginghowl.common.entities.projectiles.SmallMeteorite;
import com.mongoose.clanginghowl.common.entities.utils.CameraShake;
import com.mongoose.clanginghowl.common.network.CHNetwork;
import com.mongoose.clanginghowl.common.network.server.SPlayWorldSoundPacket;
import com.mongoose.clanginghowl.common.network.server.SSendCHWorldData;
import com.mongoose.clanginghowl.config.CHConfig;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CHWorldData extends SavedData {
    private static final int SYNC_INTERVAL = 60;
    private static final int FLASH_TIME = 8;
    private static final int PLAY_EXPLODE_TIME = 9;
    private static final int START_METEOR_TIME = 10;
    public static final String IDENTIFIER = "ch_world_data";
    private int timeSinceSync;
    public boolean startMeteorShower;
    public boolean isMeteorShower;
    public boolean isMeteorFlash;
    public boolean playMeteorExplode;
    public boolean playMeteorMusic;
    public boolean isMeteorMusicFullVolume;
    public boolean forceMeteor;
    public boolean forceStop;
    public int lastStop;
    public int meteorShowerTime;
    public int meteorFlashTick;
    public ServerLevel serverLevel;

    public CHWorldData(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
        this.setDirty();
    }

    public CHWorldData(ServerLevel serverLevel, CompoundTag compoundTag) {
        this(serverLevel);
        this.startMeteorShower = compoundTag.getBoolean("StartMeteorShower");
        this.isMeteorShower = compoundTag.getBoolean("MeteorShower");
        this.isMeteorFlash = compoundTag.getBoolean("MeteorFlash");
        this.playMeteorExplode = compoundTag.getBoolean("MeteorExplode");
        this.playMeteorMusic = compoundTag.getBoolean("MeteorMusic");
        this.isMeteorMusicFullVolume = compoundTag.getBoolean("MeteorMusicFullVolume");
        this.forceMeteor = compoundTag.getBoolean("ForceMeteor");
        this.forceStop = compoundTag.getBoolean("ForceStop");
        this.lastStop = compoundTag.getInt("LastStop");
        this.meteorShowerTime = compoundTag.getInt("MeteorShowerTime");
        this.meteorFlashTick = compoundTag.getInt("MeteorFlashTick");
    }

    public void tick() {
        ++this.timeSinceSync;
        if (this.timeSinceSync > SYNC_INTERVAL) {
            this.syncToClients();
        }
        if (this.serverLevel != null) {
            if (CHConfig.MeteorShower.get() || (this.isForceMeteor())) {
                if (this.serverLevel.dimension() == Level.OVERWORLD) {
                    if (this.isForceStop()) {
                        //Prevent ForceStop from continuing if time was set to Day 0.
                        if (this.serverLevel.getDayTime() < 24000 || this.getLastStop() != MathHelper.ticksToMinecraftDay(this.serverLevel.getDayTime())) {
                            this.setForceStop(false);
                            this.setLastStop(0);
                            this.setMeteorShowerTime(0);
                        }
                    }
                    boolean canStart = this.isForceMeteor() || this.serverLevel.getDayTime() >= MathHelper.minecraftDayToTicks(CHConfig.MeteorShowerDays.get());
                    //Allows Meteor Showers to occur if time was forcibly set in the middle of configured day.
                    if (canStart && this.serverLevel.getDayTime() % MathHelper.minecraftDayToTicks(CHConfig.MeteorShowerDays.get()) < 24000) {
                        //Stops Meteor Showers from reoccurring if it's stopped through duration or command.
                        if ((this.isForceMeteor() || (!this.startMeteorShower() && this.getMeteorShowerTime() <= 0)) && !this.isForceStop()) {
                            this.setStartMeteorShower(true);
                        }
                        if (this.serverLevel.isDay()) {
                            if (this.getMeteorShowerTime() > 0) {
                                this.setMeteorShowerTime(0);
                            }
                        }
                    } else {
                        if (this.getMeteorShowerTime() > 0) {
                            this.setMeteorShowerTime(0);
                        }
                    }
                    List<ServerPlayer> affected = this.serverLevel.getServer().getPlayerList().getPlayers().stream().filter(serverPlayer -> serverPlayer.level().dimension() == Level.OVERWORLD).toList();
                    if (this.isMeteorShower()) {
                        this.setMeteorShowerTime(this.getMeteorShowerTime() + 1);
                        if (this.getMeteorShowerTime() >= MathHelper.secondsToTicks(START_METEOR_TIME)) {
                            if (!affected.isEmpty()) {
                                for (ServerPlayer player : affected) {
                                    double x = player.getX() + this.serverLevel.getRandom().nextIntBetweenInclusive(-50, 50);
                                    double z = player.getZ() + this.serverLevel.getRandom().nextIntBetweenInclusive(-50, 50);
                                    BlockPos blockPos = BlockPos.containing(x, player.getY(), z);
                                    double heightMap = this.serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos).getY() + 80.0D;
                                    Vec3 vec3 = new Vec3(x, heightMap, z);
                                    float chance = player.distanceToSqr(vec3) <= Mth.square(96.0F) ? 0.25F : 0.05F;
                                    if (player.getRandom().nextFloat() <= chance) {
                                        if (this.serverLevel.isLoaded(BlockPos.containing(vec3))) {
                                            double xd = player.getRandom().nextIntBetweenInclusive(-1, 1) * (player.getRandom().nextDouble() * 256.0D);
                                            double zd = player.getRandom().nextIntBetweenInclusive(-1, 1) * (player.getRandom().nextDouble() * 256.0D);
                                            SmallMeteorite smallMeteorite = new SmallMeteorite(vec3.x, vec3.y, vec3.z, xd, -900.0D, zd, this.serverLevel);
                                            this.serverLevel.addFreshEntity(smallMeteorite);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (this.serverLevel.isNight() && this.serverLevel.getDayTime() % 24000 >= 12000) {
                        if (this.startMeteorShower()) {
                            if (!this.isMeteorShower()) {
                                if (!canStart) {
                                    this.setStartMeteorShower(false);
                                } else {
                                    this.setMeteorShower(true);
                                    for (ServerPlayer player : affected) {
                                        player.displayClientMessage(Component.translatable("info.clanginghowl.event.meteor_shower.start"), true);
                                        CHNetwork.sentToTrackingEntityAndPlayer(player, new SPlayWorldSoundPacket(player.blockPosition(), CHSounds.METEOR_SHOWER_FLY.get(), 16.0F, 1.0F));
                                    }
                                }
                            }
                            if (this.getMeteorShowerTime() >= MathHelper.secondsToTicks(PLAY_EXPLODE_TIME) + 10 && this.getMeteorShowerTime() < MathHelper.secondsToTicks(PLAY_EXPLODE_TIME) + 12) {
                                if (!affected.isEmpty()) {
                                    for (ServerPlayer player : affected) {
                                        CameraShake.cameraShake(player.level(), player.position(), 20.0F, 0.5F, 0, 20);
                                    }
                                }
                            }
                            if (this.getMeteorShowerTime() == MathHelper.secondsToTicks(PLAY_EXPLODE_TIME) + 10) {
                                this.setPlayMeteorExplode(true);
                            } else {
                                if (this.playMeteorExplode()) {
                                    this.setPlayMeteorExplode(false);
                                }
                            }
                            if (this.getMeteorShowerTime() >= MathHelper.secondsToTicks(PLAY_EXPLODE_TIME + 5)) {
                                if (!this.playMeteorMusic()) {
                                    this.setPlayMeteorMusic(true);
                                    this.setMeteorMusicFullVolume(false);
                                }
                            }
                            if (this.isMeteorFlash()) {
                                if (this.getMeteorShowerTime() >= MathHelper.secondsToTicks(FLASH_TIME) + 220) {
                                    this.setMeteorFlash(false);
                                }
                                int i = this.getMeteorFlashTick() < 100 ? 5 : 1;
                                this.setMeteorFlashTick(this.getMeteorFlashTick() + i);
                            } else {
                                if (this.getMeteorShowerTime() >= MathHelper.secondsToTicks(FLASH_TIME) && this.getMeteorShowerTime() < MathHelper.secondsToTicks(FLASH_TIME) + 200) {
                                    this.setMeteorFlash(true);
                                }
                            }
                        }
                    }
                    if (this.getMeteorShowerTime() >= MathHelper.secondsToTicks(CHConfig.MeteorShowerDuration.get()) || (this.serverLevel.isDay() && this.serverLevel.getDayTime() % 24000 <= 12000) || !canStart) {
                        if (this.isMeteorShower()) {
                            this.stop();
                        }
                    }
                }
            } else {
                if (this.startMeteorShower()) {
                    this.setStartMeteorShower(false);
                }
                if (this.isMeteorShower()) {
                    this.setMeteorShower(false);
                }
                if (this.isMeteorFlash()) {
                    this.setMeteorFlash(false);
                }
                if (this.playMeteorExplode()) {
                    this.setPlayMeteorExplode(false);
                }
                if (this.playMeteorExplode()) {
                    this.setPlayMeteorMusic(false);
                    this.setMeteorMusicFullVolume(false);
                }
                if (this.isForceStop()) {
                    this.setForceStop(false);
                }
                if (this.getLastStop() > 0) {
                    this.setLastStop(0);
                }
                if (this.getMeteorShowerTime() > 0) {
                    this.setMeteorShowerTime(0);
                }
                if (this.getMeteorFlashTick() > 0) {
                    this.setMeteorFlashTick(0);
                }
            }
        }
    }

    public void stop() {
        List<ServerPlayer> affected = this.serverLevel.getServer().getPlayerList().getPlayers().stream().filter(serverPlayer -> serverPlayer.level().dimension() == Level.OVERWORLD).toList();
        if (this.isMeteorShower()) {
            if (!affected.isEmpty()) {
                for (ServerPlayer player : affected) {
                    player.displayClientMessage(Component.translatable("info.clanginghowl.event.meteor_shower.stop"), true);
                }
            }
        }
        this.setStartMeteorShower(false);
        this.setMeteorShower(false);
        this.setPlayMeteorExplode(false);
        this.setPlayMeteorMusic(false);
        this.setMeteorMusicFullVolume(false);
        this.setMeteorFlash(false);
        this.setMeteorFlashTick(0);
        if (this.isForceMeteor()) {
            this.setForceMeteor(false);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound, net.minecraft.core.HolderLookup.Provider registries) {
        compound.putBoolean("StartMeteorShower", this.startMeteorShower);
        compound.putBoolean("MeteorShower", this.isMeteorShower);
        compound.putBoolean("MeteorFlash", this.isMeteorFlash);
        compound.putBoolean("MeteorExplode", this.playMeteorExplode);
        compound.putBoolean("MeteorMusic", this.playMeteorMusic);
        compound.putBoolean("MeteorMusicFullVolume", this.isMeteorMusicFullVolume);
        compound.putBoolean("ForceMeteor", this.forceMeteor);
        compound.putBoolean("ForceStop", this.forceStop);
        compound.putInt("LastStop", this.lastStop);
        compound.putInt("MeteorShowerTime", this.meteorShowerTime);
        compound.putInt("MeteorFlashTick", this.meteorFlashTick);
        return compound;
    }

    private void syncToClients() {
        if (this.serverLevel != null) {
            CHNetwork.sendToClientLevel(new SSendCHWorldData(this.isMeteorShower(), this.isMeteorFlash(), this.playMeteorExplode(), this.playMeteorMusic(), this.isMeteorMusicFullVolume(), this.getMeteorFlashTick()), this.serverLevel.dimension());
            this.timeSinceSync = 0;
            this.setDirty();
        }
    }

    public ServerLevel getServerLevel() {
        return this.serverLevel;
    }

    public boolean startMeteorShower() {
        return this.startMeteorShower;
    }

    public void setStartMeteorShower(boolean start) {
        this.startMeteorShower = start;
    }

    public boolean isMeteorShower() {
        return this.isMeteorShower;
    }

    public void setMeteorShower(boolean meteorShower) {
        this.isMeteorShower = meteorShower;
        this.syncToClients();
    }

    public boolean isMeteorFlash() {
        return this.isMeteorFlash;
    }

    public void setMeteorFlash(boolean meteorFlash) {
        this.isMeteorFlash = meteorFlash;
        this.syncToClients();
    }

    public boolean playMeteorExplode() {
        return this.playMeteorExplode;
    }

    public void setPlayMeteorExplode(boolean meteorExplode) {
        this.playMeteorExplode = meteorExplode;
        this.syncToClients();
    }

    public boolean playMeteorMusic() {
        return this.playMeteorMusic;
    }

    public void setPlayMeteorMusic(boolean meteorMusic) {
        this.playMeteorMusic = meteorMusic;
        this.syncToClients();
    }

    public boolean isMeteorMusicFullVolume() {
        return this.isMeteorMusicFullVolume;
    }

    public void setMeteorMusicFullVolume(boolean meteorMusic) {
        this.isMeteorMusicFullVolume = meteorMusic;
        this.syncToClients();
    }

    public boolean isForceMeteor() {
        return this.forceMeteor;
    }

    public void setForceMeteor(boolean forceMeteor) {
        this.forceMeteor = forceMeteor;
        if (forceMeteor) {
            this.setMeteorShowerTime(0);
        }
    }

    public boolean isForceStop() {
        return this.forceStop;
    }

    public void setForceStop(boolean forceStop) {
        this.forceStop = forceStop;
    }

    public int getLastStop() {
        return this.lastStop;
    }

    public void setLastStop(int lastStop) {
        this.lastStop = lastStop;
    }

    public int getMeteorShowerTime() {
        return this.meteorShowerTime;
    }

    public void setMeteorShowerTime(int meteorShowerTime) {
        this.meteorShowerTime = meteorShowerTime;
    }

    public int getMeteorFlashTick() {
        return this.meteorFlashTick;
    }

    public void setMeteorFlashTick(int meteorFlashTick) {
        this.meteorFlashTick = meteorFlashTick;
        this.syncToClients();
    }
}

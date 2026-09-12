package com.mongoose.clanginghowl.mixin;

import com.mongoose.clanginghowl.common.world.data.CHWorldData;
import com.mongoose.clanginghowl.common.world.data.ICHWorldData;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

//Stolen from @yungnickyoung codes:https://github.com/YUNG-GANG/YUNGs-Cave-Biomes/blob/1.20.1/Common/src/main/java/com/yungnickyoung/minecraft/yungscavebiomes/mixin/lost_caves/ServerLevelMixin.java
@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements ICHWorldData {
    @Unique
    private CHWorldData clangingHowl$chWorldData;

    @Shadow
    public abstract DimensionDataStorage getDataStorage();

    protected ServerLevelMixin(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);
    }

    @Unique
    public CHWorldData getCHWorldData() {
        return clangingHowl$chWorldData;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void clanginghowl_worldinit(MinecraftServer p_214999_, Executor p_215000_, LevelStorageSource.LevelStorageAccess p_215001_, ServerLevelData p_215002_, ResourceKey p_215003_, LevelStem p_215004_, ChunkProgressListener p_215005_, boolean p_215006_, long p_215007_, List p_215008_, boolean p_215009_, RandomSequences p_288977_, CallbackInfo ci) {
        this.clangingHowl$chWorldData = this.getDataStorage().computeIfAbsent(
                new net.minecraft.world.level.saveddata.SavedData.Factory<>(
                        () -> new CHWorldData((ServerLevel) (Object) this),
                        (compoundTag, registries) -> new CHWorldData((ServerLevel) (Object) this, compoundTag), null),
                CHWorldData.IDENTIFIER);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void clanginghowl_worldtick(BooleanSupplier p_8794_, CallbackInfo ci) {
        this.clangingHowl$chWorldData.tick();
    }
}

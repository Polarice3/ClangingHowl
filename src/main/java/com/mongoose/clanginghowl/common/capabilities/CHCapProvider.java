package com.mongoose.clanginghowl.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class CHCapProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    public static Capability<ICHCap> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    ICHCap instance = new CHCapImp();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CAPABILITY ? LazyOptional.of(() -> (T) instance) : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return CHCapHelper.save(new CompoundTag(), instance);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        CHCapHelper.load(nbt, instance);
    }
}

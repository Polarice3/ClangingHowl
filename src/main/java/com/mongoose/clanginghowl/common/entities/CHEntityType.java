package com.mongoose.clanginghowl.common.entities;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.entities.hostiles.ExReaper;
import com.mongoose.clanginghowl.common.entities.hostiles.HeartOfDecay;
import com.mongoose.clanginghowl.common.entities.projectiles.SpitProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ClangingHowl.MOD_ID);

    public static final RegistryObject<EntityType<SpitProjectile>> SPIT_PROJECTILE = register("spit_projectile",
            EntityType.Builder.<SpitProjectile>of(SpitProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(8)
                    .updateInterval(1));

    public static final RegistryObject<EntityType<HeartOfDecay>> HEART_OF_DECAY = register("heart_of_decay",
            EntityType.Builder.of(HeartOfDecay::new, MobCategory.MONSTER)
                    .sized(0.9F, 0.7F)
                    .clientTrackingRange(8));

    public static final RegistryObject<EntityType<ExReaper>> EX_REAPER = register("extraterrestrial_reaper",
            EntityType.Builder.of(ExReaper::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String p_20635_, EntityType.Builder<T> p_20636_) {
        return ENTITY_TYPE.register(p_20635_, () -> p_20636_.build(ClangingHowl.location(p_20635_).toString()));
    }
}

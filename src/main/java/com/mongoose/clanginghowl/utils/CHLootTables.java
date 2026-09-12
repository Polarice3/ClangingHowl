package com.mongoose.clanginghowl.utils;

import com.google.common.collect.Sets;
import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collections;
import java.util.Set;

public class CHLootTables {
    private static final Set<ResourceLocation> LOCATIONS = Sets.newHashSet();
    private static final Set<ResourceLocation> IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);
    public static final ResourceLocation EMPTY = ResourceLocation.parse("empty");

    public static final ResourceLocation CONSUMMATE_NEST_LOOT = register("gameplay/consummate_nest_loot");

    private static ResourceLocation register(String pId) {
        return register(ClangingHowl.location(pId));
    }

    private static ResourceLocation register(ResourceLocation pId) {
        if (LOCATIONS.add(pId)) {
            return pId;
        } else {
            throw new IllegalArgumentException(pId + " is already a registered built-in loot table");
        }
    }

    public static LootParams.Builder createLootParams(LivingEntity target, boolean checkPlayerKill, DamageSource source) {
        LootParams.Builder lootcontext$builder = (new LootParams.Builder((ServerLevel) target.level())).withParameter(LootContextParams.THIS_ENTITY, target).withParameter(LootContextParams.ORIGIN, target.position()).withParameter(LootContextParams.DAMAGE_SOURCE, source).withOptionalParameter(LootContextParams.ATTACKING_ENTITY, source.getEntity()).withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, source.getDirectEntity());
        if (checkPlayerKill && target.getKillCredit() instanceof Player player) {
            lootcontext$builder = lootcontext$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
        }

        return lootcontext$builder;
    }

    public static Set<ResourceLocation> all() {
        return IMMUTABLE_LOCATIONS;
    }

}

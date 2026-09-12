package com.mongoose.clanginghowl.common.blocks.entities.consummate_nest;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.client.particles.ShockWaveParticleOption;
import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.CHLootTables;
import com.mongoose.clanginghowl.utils.ParticleUtil;
import com.mongoose.clanginghowl.utils.PlayerDetector;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//Based codes from deobfuscated codes by @mahtomedi:https://github.com/mahtomedi/minecraft/blob/main/src/main/java/net/minecraft/world/level/block/entity/trialspawner/TrialSpawner.java
public class ConsummateNest {
    private static final int MAX_SPAWN_DISTANCE_SQR = Mth.square(47);
    private final ConsummateNestData data = new ConsummateNestData();
    private final StateAccessor stateAccessor;

    public ConsummateNest(StateAccessor stateAccessor) {
        this.stateAccessor = stateAccessor;
    }

    public void tickClient(Level level, BlockPos blockPos) {
        this.getState().emitParticles(level, blockPos);
        if (this.getState() == ConsummateNestState.ACTIVE) {
            RandomSource random = level.getRandom();
            if (random.nextFloat() <= 0.02F) {
                level.playLocalSound(blockPos, CHSounds.CONSUMMATE_NEST_AMBIENT.get(), SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
            }
        }
    }

    public void tickServer(ServerLevel serverLevel, BlockPos blockPos) {
        ConsummateNestState state = getState();

        this.data.livingMobs.removeIf(uuid -> shouldMobBeUntracked(serverLevel, blockPos, uuid));

        switch (state) {
            case INACTIVE -> {
                if (this.playerNearby(serverLevel, blockPos, 5)) {
                    this.activate(serverLevel, blockPos);
                }
            }
            case ACTIVE -> {
                this.data.ticksSinceWaveStart++;
                boolean allDead = this.data.livingMobs.isEmpty();
                boolean timeout = this.data.ticksSinceWaveStart >= 1000 && this.playerNearby(serverLevel, blockPos, 7);

                if (allDead || timeout) {
                    if (allDead) {
                        ++this.data.ticksToNextWave;
                    }
                    if (this.data.ticksToNextWave >= 40 || timeout) {
                        if (this.data.currentWave >= 5) {
                            if (allDead) {
                                this.data.ejectTimes = serverLevel.getRandom().nextIntBetweenInclusive(5, 10);
                                this.setState(serverLevel, ConsummateNestState.EJECTING_REWARD);
                                this.data.cooldownEnd = serverLevel.getGameTime() + ConsummateNestData.COOLDOWN_TICKS;
                                serverLevel.playSound(null, blockPos, CHSounds.CONSUMMATE_NEST_OPEN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                addEjectItemParticles(serverLevel, blockPos, serverLevel.getRandom());
                            }
                        } else {
                            this.data.ticksToNextWave = 0;
                            this.startWave(serverLevel, blockPos, this.data.currentWave + 1);
                        }
                    }
                }
            }
            case EJECTING_REWARD -> {
                if (this.data.isReadyToEjectItems(serverLevel, ConsummateNestData.EJECT_INTERVAL)) {
                    if (this.data.hasEjected >= this.data.ejectTimes) {
                        serverLevel.playSound(null, blockPos, CHSounds.CONSUMMATE_NEST_CLOSE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                        this.data.ejectingLootTable = Optional.empty();
                        this.setState(serverLevel, ConsummateNestState.COOLDOWN);
                    } else {
                        if (this.data.hasEjected == 0) {
                            serverLevel.addFreshEntity(new ExperienceOrb(serverLevel, blockPos.getX() + 0.5D, blockPos.getY() + 1.2D, blockPos.getZ() + 0.5D, 100 + serverLevel.getRandom().nextInt(100)));
                        }
                        if (this.data.ejectingLootTable.isEmpty()) {
                            this.data.ejectingLootTable = Optional.of(CHLootTables.CONSUMMATE_NEST_LOOT);
                        }
                        this.data.ejectingLootTable.ifPresent(loc -> ejectReward(serverLevel, blockPos, loc));
                        this.data.hasEjected += 1;
                    }
                }
            }
            case COOLDOWN -> {
                if (this.data.isCooldownFinished(serverLevel)) {
                    this.data.reset();
                    this.setState(serverLevel, ConsummateNestState.INACTIVE);
                }
            }
        }
    }

    public void activate(ServerLevel serverLevel, BlockPos blockPos) {
        if (this.getState() == ConsummateNestState.INACTIVE) {
            this.setState(serverLevel, ConsummateNestState.ACTIVE);
            addDetectPlayerParticles(serverLevel, blockPos);
            this.startWave(serverLevel, blockPos, 1);
        }
    }

    private void startWave(ServerLevel serverLevel, BlockPos blockPos, int wave) {
        this.data.currentWave = wave;
        this.data.ticksSinceWaveStart = 0;
        this.data.livingMobs.clear();

        List<EntityType<?>> pool = ConsummateNestData.WAVE_POOLS.get(wave - 1);
        int count = ConsummateNestData.WAVE_COUNTS[wave - 1];

        for (int i = 0; i < count; i++) {
            EntityType<?> type = pool.get(serverLevel.getRandom().nextInt(pool.size()));
            this.spawnMob(serverLevel, blockPos, type).ifPresent(this.data.livingMobs::add);
        }

        if (wave == 4) {
            EntityType<?> special = serverLevel.getRandom().nextBoolean() ? CHEntityType.PROWLER.get() : CHEntityType.CARCASS.get();
            this.spawnMob(serverLevel, blockPos, special).ifPresent(this.data.livingMobs::add);
        }

        if (wave == 5) {
            for (int i = 0; i < 2; i++) {
                EntityType<?> special = serverLevel.getRandom().nextBoolean() ? CHEntityType.PROWLER.get() : CHEntityType.CARCASS.get();
                this.spawnMob(serverLevel, blockPos, special).ifPresent(this.data.livingMobs::add);
            }
        }

        addSpawnParticles(serverLevel, blockPos, serverLevel.getRandom());
        serverLevel.playSound(null, blockPos, CHSounds.CONSUMMATE_NEST_SPAWN.get(), SoundSource.BLOCKS, 1.0F, (serverLevel.getRandom().nextFloat() - serverLevel.getRandom().nextFloat()) * 0.2F + 1.0F);
    }

    private Optional<UUID> spawnMob(ServerLevel serverLevel, BlockPos blockPos, EntityType<?> type) {
        RandomSource random = serverLevel.getRandom();
        double x = blockPos.getX();
        double y = blockPos.getY() + 1;
        double z = blockPos.getZ();
        for (int i = 0; i < 32; ++i) {
            double x1 = blockPos.getX() + (random.nextDouble() - random.nextDouble()) * 4 + 0.5D;
            double y1 = blockPos.getY() + random.nextInt(3) - 1;
            double z1 = blockPos.getZ() + (random.nextDouble() - random.nextDouble()) * 4 + 0.5D;
            if (i == 31 || (serverLevel.noCollision(type.getDimensions().makeBoundingBox(x1, y1, z1)) && inLineOfSight(serverLevel, blockPos.getCenter(), new Vec3(x1, y1, z1)))) {
                x = x1;
                y = y1;
                z = z1;
                break;
            }
        }

        Entity entity = type.create(serverLevel);
        if (entity == null) {
            return Optional.empty();
        }

        entity.moveTo(x, y, z, random.nextFloat() * 360F, 0);
        if (entity instanceof Mob mob) {
            if (!mob.checkSpawnObstruction(serverLevel)) {
                for (int i = 0; i < 32; ++i) {
                    double x1 = blockPos.getX() + (random.nextDouble() - random.nextDouble()) * 4 + 0.5D;
                    double y1 = blockPos.getY() + random.nextInt(3) - 1;
                    double z1 = blockPos.getZ() + (random.nextDouble() - random.nextDouble()) * 4 + 0.5D;
                    mob.moveTo(x1, y1, z1, random.nextFloat() * 360F, 0);
                    if (mob.checkSpawnObstruction(serverLevel)) {
                        x = x1;
                        y = y1;
                        z = z1;
                        break;
                    } else if (i == 31) {
                        return Optional.empty();
                    }
                }
            }
            EventHooks.finalizeMobSpawn(mob, serverLevel, serverLevel.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.SPAWNER, null);
            mob.setPersistenceRequired();
            mob.restrictTo(blockPos, 16);
        }
        if (entity instanceof LivingEntity livingEntity) {
            CHCapHelper.setFlashTick(livingEntity, 15);
        }

        if (!serverLevel.tryAddFreshEntityWithPassengers(entity)) {
            return Optional.empty();
        }

        addSpawnParticles(serverLevel, BlockPos.containing(x, y, z), random);
        serverLevel.gameEvent(entity, GameEvent.ENTITY_PLACE, BlockPos.containing(x, y, z));
        return Optional.of(entity.getUUID());
    }

    public void ejectReward(ServerLevel serverLevel, BlockPos blockPos, ResourceLocation lootTableId) {
        LootTable table = serverLevel.getServer().reloadableRegistries().getLootTable(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE, lootTableId));
        LootParams params = new LootParams.Builder(serverLevel).create(LootContextParamSets.EMPTY);
        ObjectArrayList<ItemStack> items = table.getRandomItems(params);
        if (!items.isEmpty()) {
            for (ItemStack stack : items) {
                DefaultDispenseItemBehavior.spawnItem(serverLevel, stack, 2, Direction.UP,
                        Vec3.atBottomCenterOf(blockPos).relative(Direction.UP, 1.2));
            }
            serverLevel.playSound(null, blockPos, CHSounds.CONSUMMATE_NEST_EJECT_ITEM.get(), SoundSource.BLOCKS, 1.0F, (serverLevel.getRandom().nextFloat() - serverLevel.getRandom().nextFloat()) * 0.2F + 1.0F);
            addEjectItemParticles(serverLevel, blockPos, serverLevel.getRandom());
        }
    }

    public static void addDetectPlayerParticles(ServerLevel serverLevel, BlockPos blockPos) {
        Vec3 vec3 = blockPos.getCenter();
        ParticleUtil.sendAlwaysVisibleParticles(serverLevel, new ShockWaveParticleOption(4.0F, 0), vec3.x, vec3.y - 0.25F, vec3.z, 1, 0, 0, 0, 0);
    }

    public static void addSpawnParticles(ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        for (int i = 0; i < 20; i++) {
            double x = blockPos.getX() + 0.5 + (random.nextDouble() - 0.5) * 2;
            double y = blockPos.getY() + 0.5 + (random.nextDouble() - 0.5) * 2;
            double z = blockPos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 2;
            serverLevel.sendParticles(CHParticleTypes.ENERGETIC_EMERGENCE.get(), x, y, z, 1, 0, 0, 0, 0);
        }
    }

    public static void addEjectItemParticles(ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        for (int i = 0; i < 20; i++) {
            double x = blockPos.getX() + 0.4 + random.nextDouble() * 0.2;
            double y = blockPos.getY() + 0.4 + random.nextDouble() * 0.2;
            double z = blockPos.getZ() + 0.4 + random.nextDouble() * 0.2;
            double dx = random.nextGaussian() * 0.02;
            double dy = random.nextGaussian() * 0.02;
            double dz = random.nextGaussian() * 0.02;
            serverLevel.sendParticles(CHParticleTypes.BIG_BLOODY_ENERGY.get(), x, y, z, 0, dx, dy, dz * 0.25, 0.5F);
            serverLevel.sendParticles(ParticleTypes.SMOKE, x, y, z, 0, dx, dy, dz, 0.5F);
        }
    }

    private boolean playerNearby(ServerLevel serverLevel, BlockPos blockPos, int distance) {
        return !PlayerDetector.NO_CREATIVE_PLAYERS.detect(serverLevel, PlayerDetector.EntitySelector.SELECT_FROM_LEVEL, blockPos, distance, true).isEmpty();
    }

    private static boolean shouldMobBeUntracked(ServerLevel serverLevel, BlockPos blockPos, UUID uuid) {
        Entity entity = serverLevel.getEntity(uuid);
        return entity == null || !entity.isAlive()
                || !entity.level().dimension().equals(serverLevel.dimension())
                || entity.blockPosition().distSqr(blockPos) > MAX_SPAWN_DISTANCE_SQR;
    }

    public List<Entity> getEntities(ServerLevel serverLevel) {
        List<Entity> entities = new ArrayList<>();
        if (!this.data.livingMobs.isEmpty()) {
            for (UUID uuid : this.data.livingMobs) {
                Entity entity = serverLevel.getEntity(uuid);
                entities.add(entity);
            }
        }
        return entities;
    }

    private static boolean inLineOfSight(Level level, Vec3 from, Vec3 to) {
        BlockHitResult hit = level.clip(new ClipContext(to, from, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, (net.minecraft.world.entity.Entity) null));
        return hit.getBlockPos().equals(BlockPos.containing(from)) || hit.getType() == HitResult.Type.MISS;
    }

    public ConsummateNestState getState() {
        return stateAccessor.getState();
    }

    public void setState(Level level, ConsummateNestState state) {
        stateAccessor.setState(level, state);
    }
    public ConsummateNestData getData() {
        return this.data;
    }

    public CompoundTag save() {
        return this.data.save();
    }

    public void load(CompoundTag tag) {
        this.data.load(tag);
    }

    public interface StateAccessor {

        ConsummateNestState getState();

        void setState(Level level, ConsummateNestState state);

        void markUpdated();
    }
}

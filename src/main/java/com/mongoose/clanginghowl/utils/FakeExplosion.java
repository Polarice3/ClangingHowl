package com.mongoose.clanginghowl.utils;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FakeExplosion {
    public ObjectArrayList<BlockPos> toBlow = new ObjectArrayList<>();

    public FakeExplosion(Level level, Entity source, DamageSource damageSource, double x, double y, double z, float radius) {
        this(level, source, damageSource, x, y, z, radius, 0);
    }

    public FakeExplosion(Level level, Entity source, DamageSource damageSource, BlockPos blockPos, float radius, float damage){
        this(level, source, damageSource, blockPos.getX(), blockPos.getY(), blockPos.getZ(), radius, damage);
    }

    public FakeExplosion(Level level, Entity source, DamageSource damageSource, double x, double y, double z, float radius, float damage){
        level.gameEvent(source, GameEvent.EXPLODE, new Vec3(x, y, z));
        this.collectBlocksToBlow(level, x, y, z, radius);

        // Descriptor for entity-specific immunity hooks; it is never exploded a second time.
        net.minecraft.world.level.Explosion descriptor = new net.minecraft.world.level.Explosion(
                level, source, damageSource, null, x, y, z, radius, false,
                net.minecraft.world.level.Explosion.BlockInteraction.DESTROY,
                net.minecraft.core.particles.ParticleTypes.EXPLOSION,
                net.minecraft.core.particles.ParticleTypes.EXPLOSION_EMITTER,
                net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE);
        Vec3 vec3 = new Vec3(x, y, z);
        for (Entity entity : explosionRangeEntities(level, source, x, y, z, radius)) {
            double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / (double) radius;
            if (d12 <= 1.0D) {
                double d5 = entity.getX() - x;
                double d7 = entity.getEyeY() - y;
                double d9 = entity.getZ() - z;
                double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                if (d13 != 0.0D) {
                    d5 /= d13;
                    d7 /= d13;
                    d9 /= d13;
                    double d14 = (double) getSeenPercent(vec3, entity);
                    double d10 = (1.0D - d12) * d14;
                    float actualDamage = damage == 0 ? (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) radius + 1.0D)) : damage;
                    Entity trueSource = null;
                    boolean hurt = true;
                    if (entity instanceof ItemEntity) {
                        hurt = false;
                    } else if (damageSource.is(DamageTypeTags.IS_EXPLOSION) && entity.ignoreExplosion(descriptor)){
                        hurt = false;
                    } else if (damageSource.getEntity() != null){
                        trueSource = damageSource.getEntity();
                    } else if (source != null){
                        trueSource = source;
                    }
                    if (trueSource != null) {
                        if (MobUtil.areAllies(trueSource, entity) || entity == trueSource){
                            hurt = false;
                        }
                    }
                    if (hurt) {
                        this.explodeHurt(entity, damageSource, d5, d7, d9, d10, actualDamage);
                    }
                }
            }
        }
        if (!this.toBlow.isEmpty()) {
            this.explodeBlocks(level, source);
        }
    }

    public Optional<Float> getBlockExplosionResistance(BlockGetter p_46100_, BlockPos p_46101_, BlockState p_46102_, FluidState p_46103_) {
        return p_46102_.isAir() && p_46103_.isEmpty() ? Optional.empty() : Optional.of(Math.max(p_46102_.getBlock().getExplosionResistance(), p_46103_.getExplosionResistance()));
    }

    public void collectBlocksToBlow(Level level, double x, double y, double z, float radius) {
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;

        for(int j = 0; j < i; ++j) {
            for(int k = 0; k < i; ++k) {
                for(int l = 0; l < i; ++l) {
                    if (j == 0 || j == (i - 1) || k == 0 || k == (i - 1) || l == 0 || l == (i - 1)) {
                        double d0 = (double)((float)j / (i - 1) * 2.0F - 1.0F);
                        double d1 = (double)((float)k / (i - 1) * 2.0F - 1.0F);
                        double d2 = (double)((float)l / (i - 1) * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = radius * (0.7F + level.random.nextFloat() * 0.6F);
                        double d4 = x;
                        double d6 = y;
                        double d8 = z;

                        for(float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = BlockPos.containing(d4, d6, d8);
                            BlockState blockstate = level.getBlockState(blockpos);
                            FluidState fluidstate = level.getFluidState(blockpos);
                            if (!level.isInWorldBounds(blockpos)) {
                                break;
                            }

                            Optional<Float> optional = this.getBlockExplosionResistance(level, blockpos, blockstate, fluidstate);
                            if (optional.isPresent()) {
                                f -= (optional.get() + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F) {
                                set.add(blockpos);
                            }

                            d4 += d0 * (double)0.3F;
                            d6 += d1 * (double)0.3F;
                            d8 += d2 * (double)0.3F;
                        }
                    }
                }
            }
        }

        this.toBlow.addAll(set);
    }

    public void explodeHurt(Entity target, DamageSource damageSource, double x, double y, double z, double seen, float actualDamage){
        target.hurt(damageSource, actualDamage);
        double d11 = seen;
        if (target instanceof LivingEntity) {
            d11 = (seen * (1.0D - ((LivingEntity) target).getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.EXPLOSION_KNOCKBACK_RESISTANCE)));
        }

        if (target instanceof LivingEntity) {
            MobUtil.push(target, x * d11, y * d11, z * d11);
        }
    }

    public void explodeBlocks(Level level, Entity source) {
    }

    public static List<Entity> explosionRangeEntities(Level level, Entity source, BlockPos blockPos, float range){
        return explosionRangeEntities(level, source, blockPos.getX(), blockPos.getY(), blockPos.getZ(), range);
    }

    public static List<Entity> explosionRangeEntities(Level level, Entity source, double x, double y, double z, float radius){
        int k1 = Mth.floor(x - (double) radius - 1.0D);
        int l1 = Mth.floor(x + (double) radius + 1.0D);
        int i2 = Mth.floor(y - (double) radius - 1.0D);
        int i1 = Mth.floor(y + (double) radius + 1.0D);
        int j2 = Mth.floor(z - (double) radius - 1.0D);
        int j1 = Mth.floor(z + (double) radius + 1.0D);
        return level.getEntities(source, new AABB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
    }

    public static float getSeenPercent(Vec3 vector, Entity target) {
        AABB aabb = target.getBoundingBox();
        double d0 = 1.0D / ((aabb.maxX - aabb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((aabb.maxY - aabb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((aabb.maxZ - aabb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
        if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
            int i = 0;
            int j = 0;

            for(double d5 = 0.0D; d5 <= 1.0D; d5 += d0) {
                for(double d6 = 0.0D; d6 <= 1.0D; d6 += d1) {
                    for(double d7 = 0.0D; d7 <= 1.0D; d7 += d2) {
                        double d8 = Mth.lerp(d5, aabb.minX, aabb.maxX);
                        double d9 = Mth.lerp(d6, aabb.minY, aabb.maxY);
                        double d10 = Mth.lerp(d7, aabb.minZ, aabb.maxZ);
                        Vec3 vec3 = new Vec3(d8 + d3, d9, d10 + d4);
                        if (target.level().clip(new ClipContext(vec3, vector, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, target)).getType() == HitResult.Type.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float)i / (float)j;
        } else {
            return 0.0F;
        }
    }
}

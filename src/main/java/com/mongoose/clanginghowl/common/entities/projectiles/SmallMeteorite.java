package com.mongoose.clanginghowl.common.entities.projectiles;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.client.particles.SmallFireSplashParticleOption;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.entities.hostiles.HeartOfDecay;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.MobUtil;
import com.mongoose.clanginghowl.utils.ParticleUtil;
import com.mongoose.clanginghowl.utils.TrailEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.event.EventHooks;

public class SmallMeteorite extends AbstractHurtingProjectile {
    public TrailEffect trail = new TrailEffect(0.4F, 6.0F);

    public SmallMeteorite(EntityType<? extends AbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    public SmallMeteorite(double p_36818_, double p_36819_, double p_36820_, double p_36821_, double p_36822_, double p_36823_, Level p_36824_) {
        super(CHEntityType.SMALL_METEORITE.get(), p_36818_, p_36819_, p_36820_, new Vec3(p_36821_, p_36822_, p_36823_), p_36824_);
    }

    public SmallMeteorite(LivingEntity p_36827_, double p_36828_, double p_36829_, double p_36830_, Level p_36831_) {
        super(CHEntityType.SMALL_METEORITE.get(), p_36827_, new Vec3(p_36828_, p_36829_, p_36830_), p_36831_);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() - vec3.x;
            double d1 = this.getY() - vec3.y;
            double d2 = this.getZ() - vec3.z;
            this.level().addAlwaysVisibleParticle(CHParticleTypes.METEORITE_TRAIL.get(), true, d0, d1 + 0.15D, d2, 0.0D, 0.0D, 0.0D);
            if (this.tickCount > 5) {
                Vec3 oldPos = new Vec3(xOld, yOld + getBbHeight() / 1.5F, zOld);
                this.trail.update(oldPos);
            }
        }
    }

    protected void onHitEntity(EntityHitResult p_37386_) {
        super.onHitEntity(p_37386_);
        if (!this.level().isClientSide) {
            Entity entity = p_37386_.getEntity();
            Entity entity1 = this.getOwner();
            if (entity.hurt(this.damageSources().explosion(this, entity1), this.level().getRandom().nextIntBetweenInclusive(3, 5))) {
                if (entity1 instanceof LivingEntity) {
                    net.minecraft.world.item.enchantment.EnchantmentHelper.doPostAttackEffects((ServerLevel) this.level(), entity, this.damageSources().explosion(this, entity1));
                }
            }

        }
    }

    protected void onHitBlock(BlockHitResult p_37384_) {
        super.onHitBlock(p_37384_);
        if (this.level() instanceof ServerLevel serverLevel) {
            float random = serverLevel.getRandom().nextFloat();
            if (random <= 0.01F && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                HeartOfDecay hod = new HeartOfDecay(CHEntityType.HEART_OF_DECAY.get(), serverLevel);
                hod.setPos(this.position().add(0.0D, 1.0D, 0.0D));
                EventHooks.finalizeMobSpawn(hod, serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.SPAWNER, null);
                hod.heal(hod.getMaxHealth());
                serverLevel.addFreshEntity(hod);
            } else if (serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                if (random <= 0.07F) {
                    ItemEntity itemEntity = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), new ItemStack(CHItems.PIECE_OF_EXTRATERRESTRIAL_STEEL.get()));
                    itemEntity.setExtendedLifetime();
                    serverLevel.addFreshEntity(itemEntity);
                } else if (random <= 0.15F) {
                    ItemEntity itemEntity = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), new ItemStack(CHBlocks.EXTRATERRESTRIAL_PEBBLE.get()));
                    itemEntity.setExtendedLifetime();
                    serverLevel.addFreshEntity(itemEntity);
                }
            }
        }
    }

    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            Vec3 vec3 = hitResult.getLocation();
            AABB aabb = new AABB(BlockPos.containing(vec3));
            this.playSound(CHSounds.SMALL_METEORITE_LANDING.get(), 2.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            for (LivingEntity livingEntity : this.level().getEntitiesOfClass(LivingEntity.class, aabb.inflate(3.0F), living -> living.hasLineOfSight(this))) {
                if (!MobUtil.areAllies(this, livingEntity)) {
                    livingEntity.hurt(this.damageSources().explosion(this, this), 3.0F);
                }
            }
            if (this.level() instanceof ServerLevel serverLevel) {
                ParticleUtil.sendAlwaysVisibleParticles(serverLevel, CHParticleTypes.METEORITE_SPLIT.get(), vec3.x, vec3.y, vec3.z, 1, 0, 0, 0, 0);
                ParticleUtil.sendAlwaysVisibleParticles(serverLevel, new SmallFireSplashParticleOption(3, 0), vec3.x, vec3.y + 0.25F, vec3.z, 1, 0, 0, 0, 0);
            }
            this.discard();
        }

    }

    protected boolean shouldBurn() {
        return false;
    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource p_37381_, float p_37382_) {
        return false;
    }
}

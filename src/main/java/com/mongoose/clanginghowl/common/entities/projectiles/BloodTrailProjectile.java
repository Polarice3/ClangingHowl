package com.mongoose.clanginghowl.common.entities.projectiles;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.entities.hostiles.BloodClot;
import com.mongoose.clanginghowl.utils.MobUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BloodTrailProjectile extends ThrowableProjectile {
    private static final EntityDataAccessor<Boolean> CLOT = SynchedEntityData.defineId(BloodTrailProjectile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> INFECT = SynchedEntityData.defineId(BloodTrailProjectile.class, EntityDataSerializers.BOOLEAN);

    public BloodTrailProjectile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);
    }

    public BloodTrailProjectile(double p_37457_, double p_37458_, double p_37459_, Level p_37460_) {
        super(CHEntityType.BLOOD_TRAIL.get(), p_37457_, p_37458_, p_37459_, p_37460_);
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        builder.define(CLOT, false);
        builder.define(INFECT, false);
    }

    public boolean isClotted() {
        return this.entityData.get(CLOT);
    }

    public void setClotted(boolean p_37630_) {
        this.entityData.set(CLOT, p_37630_);
    }

    public boolean isInfectious() {
        return this.entityData.get(INFECT);
    }

    public void setInfectious(boolean p_37630_) {
        this.entityData.set(INFECT, p_37630_);
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            Vec3 vec3 = pResult.getLocation();
            if (pResult instanceof EntityHitResult entityHitResult) {
                vec3 = entityHitResult.getEntity().position();
            }
            if (this.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < serverLevel.getRandom().nextIntBetweenInclusive(1, 3); ++i) {
                    float randomY = serverLevel.getRandom().nextIntBetweenInclusive(1, 4) / 100.0F;
                    serverLevel.sendParticles(CHParticleTypes.BLOOD_STAIN.get(), vec3.x, vec3.y + randomY, vec3.z, 1, 0, 0, 0, 1);
                }
                if (this.isClotted()) {
                    BloodClot bloodClot = new BloodClot(CHEntityType.BLOOD_CLOT.get(), this.level());
                    bloodClot.setPos(vec3);
                    this.level().addFreshEntity(bloodClot);
                }
                if (this.isInfectious()) {
                    if (pResult instanceof EntityHitResult result) {
                        Entity entity = result.getEntity();
                        DamageSource damageSource = this.damageSources().mobProjectile(this, this.getOwner() instanceof LivingEntity owner ? owner : null);
                        if (entity instanceof Player || !MobUtil.canInfect(entity)) {
                            entity.hurt(damageSource, 5.0F);
                        } else if (entity instanceof LivingEntity livingEntity) {
                            livingEntity.addEffect(new MobEffectInstance(CHEffects.BEYOND_FLESH, 300, 0, false, false));
                        }
                    }
                }
            }
            this.level().playSound(null, vec3.x, vec3.y, vec3.z, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.NEUTRAL, 0.3F, 1.0F);
            this.discard();
        }
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.level().isLoaded(this.blockPosition())) {
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            this.level().addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
        }
        if (this.tickCount > 100) {
            this.discard();
        }
    }

    protected ParticleOptions getTrailParticle() {
        return CHParticleTypes.BLOODY_PROJECTILE.get();
    }

    protected double getDefaultGravity() {
        return 0.07F;
    }

    public boolean hurt(DamageSource p_37381_, float p_37382_) {
        return false;
    }

    @Override
    public void makeStuckInBlock(BlockState p_20006_, Vec3 p_20007_) {
    }

    @Override
    protected boolean canHitEntity(Entity p_37250_) {
        Entity entity = this.getOwner();
        if (entity != null) {
            if (p_37250_ == entity) {
                return false;
            }
        }
        return super.canHitEntity(p_37250_);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(net.minecraft.server.level.ServerEntity serverEntity) {
        return super.getAddEntityPacket(serverEntity);
    }
}

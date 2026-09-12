package com.mongoose.clanginghowl.common.entities.hostiles;

import com.mongoose.clanginghowl.client.particles.BloodSplashParticleOption;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.utils.ParticleUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

public class BloodClot extends TFleshMonster {
    public AnimationState idleAnimationState = new AnimationState();
    public int ageTick = 0;

    public BloodClot(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 2.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putInt("AgeTick", this.ageTick);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        if (p_21450_.contains("AgeTick")) {
            this.ageTick = p_21450_.getInt("AgeTick");
        }
    }

    @Override
    public boolean canAlert() {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity p_20303_) {
        return false;
    }

    @Override
    public void push(Entity p_21294_) {
    }

    @Override
    protected void doPush(Entity p_20971_) {
    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33814_) {
        return SoundEvents.HONEY_BLOCK_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HONEY_BLOCK_BREAK;
    }

    @Override
    public void die(DamageSource p_21014_) {
        super.die(p_21014_);
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new BloodSplashParticleOption((float) (this.getBoundingBox().getSize() * 1.5F), 0), this.getX(), this.getY() + 0.5D, this.getZ(), 1, 0, 0, 0, 0);
        }
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        ++this.ageTick;
        if (this.level().isClientSide) {
            this.idleAnimationState.startIfStopped(this.tickCount);
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.onGround()) {
                if (this.ageTick % 20 == 0) {
                    float randomY = serverLevel.getRandom().nextIntBetweenInclusive(1, 4) / 100.0F;
                    serverLevel.sendParticles(CHParticleTypes.BLOOD_STAIN.get(), this.getRandomX(0.5D), this.getY() + randomY, this.getRandomZ(0.5D), 1, 0, 0, 0, 1);
                }
            }
            if (this.ageTick >= 500) {
                ParticleUtil.addParticlesAroundMiddleSelf(serverLevel, CHParticleTypes.CRIMSON_POOF.get(), this);
                this.discard();
            }
        }
    }

    public void spawnBloodyCopy(@Nullable LivingEntity target) {
        if (this.level() instanceof ServerLevel serverLevel) {
            BloodyCopy bloodyCopy = new BloodyCopy(CHEntityType.BLOODY_COPY.get(), this.level());
            EventHooks.finalizeMobSpawn(bloodyCopy, serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null);
            bloodyCopy.setPos(this.position());
            bloodyCopy.setHealth(bloodyCopy.getMaxHealth() / 2.0F);
            if (target != null) {
                bloodyCopy.setLastHurtByMob(target);
            }
            this.level().addFreshEntity(bloodyCopy);
            this.discard();
        }
    }
}

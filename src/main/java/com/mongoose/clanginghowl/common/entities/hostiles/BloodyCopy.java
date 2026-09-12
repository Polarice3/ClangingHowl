package com.mongoose.clanginghowl.common.entities.hostiles;

import com.mongoose.clanginghowl.client.particles.BloodSplashParticleOption;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.entities.ai.AvoidTargetGoal;
import com.mongoose.clanginghowl.common.entities.projectiles.BloodTrailProjectile;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.CHUUIDUtil;
import com.mongoose.clanginghowl.utils.MathHelper;
import com.mongoose.clanginghowl.utils.MobUtil;
import com.mongoose.clanginghowl.utils.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BloodyCopy extends TFleshMonster {
    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(BloodyCopy.class, EntityDataSerializers.INT);
    public static AttributeModifier APPEAR_SPEED_MODIFIER = new AttributeModifier(com.mongoose.clanginghowl.ClangingHowl.location("entity.clanginghowl.bloody_copy.immobile"), -1.0D, AttributeModifier.Operation.ADD_VALUE);
    public static String IDLE = "idle";
    public static String ATTACK = "attack";
    public static String APPEAR = "appear";
    public int isAppearTick;
    public int attackTick;
    public int retreatTick;
    public int bloodCooldown;
    public boolean stepAlt;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState appearAnimationState = new AnimationState();

    public BloodyCopy(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new AppearingGoal(this));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidTargetGoal<>(this, LivingEntity.class, 8, 1.0D, 1.2D){
            @Override
            public boolean canUse() {
                return BloodyCopy.this.retreatTick > 0 && super.canUse();
            }
        });
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.3F, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ARMOR, 3.5D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3D);
    }

    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIM_STATE, 0);
    }

    @Override
    public boolean canAlert() {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33814_) {
        return CHSounds.TECHNO_FLESH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CHSounds.FLESH_TEAR.get();
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.WOLF_STEP;
    }

    protected void playStepSound(BlockPos p_34316_, BlockState p_34317_) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    public void die(DamageSource cause) {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new BloodSplashParticleOption((float) (this.getBoundingBox().getSize()), 0), this.getX(), this.getY() + 1.0D, this.getZ(), 1, 0, 0, 0, 0);
            ParticleUtil.addParticlesAroundMiddleSelf(serverLevel, CHParticleTypes.CRIMSON_POOF.get(), this);
            for (int i = 0; i < serverLevel.getRandom().nextIntBetweenInclusive(1, 3); ++i) {
                float randomY = serverLevel.getRandom().nextIntBetweenInclusive(1, 4) / 100.0F;
                serverLevel.sendParticles(CHParticleTypes.BLOOD_STAIN.get(), this.getRandomX(0.5D), this.getY() + randomY, this.getRandomZ(0.5D), 1, 0, 0, 0, 1);
            }
            for (int i = 0; i < 2 + this.level().random.nextInt(2); ++i){
                Projectile arrow = new BloodTrailProjectile(this.getX(), this.getY(), this.getZ(), this.level());
                float yaw = this.random.nextFloat() * 360;
                float pitch = this.random.nextFloat() * 90 - 75;
                arrow.shootFromRotation(this, yaw, pitch, 0.0F, 0.75F, 0.1F);
                this.level().addFreshEntity(arrow);
            }
        }
        super.die(cause);
        this.remove(RemovalReason.KILLED);
    }

    public void setAnimationState(String input) {
        this.setAnimationState(this.getAnimationState(input));
    }

    public void setAnimationState(int id) {
        this.entityData.set(ANIM_STATE, id);
    }

    public int getAnimationState(String animation) {
        if (Objects.equals(animation, IDLE)){
            return 0;
        } else if (Objects.equals(animation, ATTACK)){
            return 1;
        } else if (Objects.equals(animation, APPEAR)){
            return 2;
        } else {
            return 0;
        }
    }

    public void stopMostAnimation(AnimationState exception){
        for (AnimationState state : this.getAnimations()){
            if (state != exception){
                state.stop();
            }
        }
    }

    public void stopAllAnimation(){
        for (AnimationState state : this.getAnimations()){
            state.stop();
        }
    }

    public int getCurrentAnimation(){
        return this.entityData.get(ANIM_STATE);
    }

    public boolean isCurrentAnimation(String animation) {
        return this.getCurrentAnimation() == this.getAnimationState(animation);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_219422_) {
        if (ANIM_STATE.equals(p_219422_)) {
            if (this.level().isClientSide){
                switch (this.entityData.get(ANIM_STATE)){
                    case 0:
                        this.stopAllAnimation();
                        break;
                    case 1:
                        this.stopAllAnimation();
                        this.attackAnimationState.startIfStopped(this.tickCount);
                        break;
                    case 2:
                        this.stopAllAnimation();
                        this.appearAnimationState.startIfStopped(this.tickCount);
                        break;
                }
            }
        }

        super.onSyncedDataUpdated(p_219422_);
    }

    public List<AnimationState> getAnimations(){
        List<AnimationState> animationStates = new ArrayList<>();
        animationStates.add(this.attackAnimationState);
        animationStates.add(this.appearAnimationState);
        return animationStates;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(net.minecraft.server.level.ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity, this.hasPose(Pose.EMERGING) ? 1 : 0);
    }

    public void recreateFromPacket(ClientboundAddEntityPacket p_219420_) {
        super.recreateFromPacket(p_219420_);
        if (p_219420_.getData() == 1) {
            this.setPose(Pose.EMERGING);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        if (pReason == MobSpawnType.MOB_SUMMONED) {
            this.setPose(Pose.EMERGING);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    @Override
    public void swing(InteractionHand p_21007_) {
        super.swing(p_21007_);
        this.attackTick = 10;
        this.setAnimationState(ATTACK);
    }

    public boolean isMeleeAttacking() {
        return this.attackTick > 0;
    }

    public boolean isAppearing() {
        return this.hasPose(Pose.EMERGING);
    }

    public void tick() {
        super.tick();
        if (this.hasPose(Pose.EMERGING)){
            ++this.isAppearTick;
            if (this.isAppearTick == 1) {
                if (this.level() instanceof ServerLevel serverLevel) {
                    this.playSound(CHSounds.FLESH_RUPTURE_BEGINNING.get());
                    serverLevel.sendParticles(new BloodSplashParticleOption((float) (this.getBoundingBox().getSize()), 0), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
                    ParticleUtil.addParticlesAroundMiddleSelf(serverLevel, CHParticleTypes.CRIMSON_POOF.get(), this);
                    for (int i = 0; i < serverLevel.getRandom().nextIntBetweenInclusive(4, 6); ++i) {
                        serverLevel.sendParticles(CHParticleTypes.BLOOD_STAIN.get(), this.getRandomX(0.5D), this.getY() + 0.1F, this.getRandomZ(0.5D), 1, 0, 0, 0, 1);
                    }
                }
            }
            if (this.isAppearTick > 50){
                this.setAnimationState(IDLE);
                this.setPose(Pose.STANDING);
            } else {
                this.setAnimationState(APPEAR);
            }
        }
        if (this.level().isClientSide) {
            this.idleAnimationState.animateWhen(!this.walkAnimation.isMoving() && this.isCurrentAnimation(IDLE) && !this.isAppearing(), this.tickCount);
        }
        if (!this.level().isClientSide) {
            if (this.attackTick > 0) {
                --this.attackTick;
            } else if (this.isCurrentAnimation(ATTACK)) {
                this.setAnimationState(IDLE);
                if (this.retreatTick <= 0) {
                    this.retreatTick = MathHelper.secondsToTicks(1);
                }
            }
            if (this.retreatTick > 0){
                --this.retreatTick;
            }
            if (this.bloodCooldown > 0){
                --this.bloodCooldown;
            }
            AttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (this.isCurrentAnimation(APPEAR)) {
                if (modifiableattributeinstance != null) {
                    modifiableattributeinstance.removeModifier(APPEAR_SPEED_MODIFIER);
                    modifiableattributeinstance.addTransientModifier(APPEAR_SPEED_MODIFIER);
                }
            } else {
                if (modifiableattributeinstance != null) {
                    if (modifiableattributeinstance.hasModifier(APPEAR_SPEED_MODIFIER.id())) {
                        modifiableattributeinstance.removeModifier(APPEAR_SPEED_MODIFIER);
                    }
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (p_21016_.getEntity() != null) {
            if (this.level().getRandom().nextFloat() <= 0.15F) {
                if (this.bloodCooldown <= 0) {
                    MobUtil.shootOutBlood(this, true);
                    this.bloodCooldown = 10;
                }
            }
        }
        return super.hurt(p_21016_, p_21017_);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean flag = super.doHurtTarget(target);
        if (flag) {
            if (this.level().getRandom().nextFloat() <= 0.15F) {
                MobUtil.shootOutBlood(target, false);
            }
        }
        return flag;
    }

    public static class AppearingGoal extends Goal {
        public BloodyCopy bloodyCopy;

        public AppearingGoal(BloodyCopy bloodyCopy) {
            this.bloodyCopy = bloodyCopy;
        }

        @Override
        public boolean canUse() {
            return this.bloodyCopy.isAppearing();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public void tick() {
            super.tick();
            this.bloodyCopy.getNavigation().stop();
            this.bloodyCopy.getMoveControl().strafe(0.0F, 0.0F);
        }
    }
}

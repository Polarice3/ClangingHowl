package com.mongoose.clanginghowl.common.entities.hostiles;

import com.mongoose.clanginghowl.common.entities.ai.AvoidTargetGoal;
import com.mongoose.clanginghowl.config.CHConfig;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.CHUUIDUtil;
import com.mongoose.clanginghowl.utils.MathHelper;
import com.mongoose.clanginghowl.utils.MobUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BloodSpreader extends TFleshMonster {
    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(BloodSpreader.class, EntityDataSerializers.INT);
    public static AttributeModifier ROAR_SPEED_MODIFIER = new AttributeModifier(com.mongoose.clanginghowl.ClangingHowl.location("entity.clanginghowl.blood_spreader.immobile"), -1.0D, AttributeModifier.Operation.ADD_VALUE);
    public static String IDLE = "idle";
    public static String ATTACK = "attack";
    public static String ROAR = "roar";
    public static String APPEAR = "appear";
    public int isAppearTick;
    public int attackTick;
    public int retreatTick;
    public int bloodCooldown;
    public int bloodToken;
    public boolean stepAlt;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState roarAnimationState = new AnimationState();
    public AnimationState appearAnimationState = new AnimationState();

    public BloodSpreader(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward = 7;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RoarGoal(this));
        this.goalSelector.addGoal(2, new AvoidTargetGoal<>(this, LivingEntity.class, 8, 1.0D, 1.2D){
            @Override
            public boolean canUse() {
                return BloodSpreader.this.retreatTick > 0 && super.canUse();
            }
        });
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.3F, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
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
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putInt("BloodToken", this.bloodToken);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        if (p_21450_.contains("BloodToken")) {
            this.bloodToken = p_21450_.getInt("BloodToken");
        }
    }

    public static boolean checkBloodSpreaderSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource randomSource) {
        if (CHConfig.BloodSpreaderDaySpawn.get() >= 0 && levelAccessor.dayTime() >= MathHelper.minecraftDayToTicks(CHConfig.BloodSpreaderDaySpawn.get())) {
            return levelAccessor.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(levelAccessor, blockPos, randomSource) && checkMobSpawnRules(entityType, levelAccessor, spawnType, blockPos, randomSource);
        }
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return CHSounds.TECHNO_FLESH_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33814_) {
        return CHSounds.TECHNO_FLESH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CHSounds.TECHNO_FLESH_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.WOLF_STEP;
    }

    protected void playStepSound(BlockPos p_34316_, BlockState p_34317_) {
        SoundEvent stepSound = this.getStepSound();
        if (!this.stepAlt) {
            stepSound = CHSounds.HEMATOMA_STEP.get();
            this.stepAlt = true;
        } else {
            this.stepAlt = false;
        }
        this.playSound(stepSound, 0.15F, 1.0F);
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
        } else if (Objects.equals(animation, ROAR)){
            return 2;
        } else if (Objects.equals(animation, APPEAR)){
            return 3;
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
                        this.roarAnimationState.startIfStopped(this.tickCount);
                        break;
                    case 3:
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
        animationStates.add(this.roarAnimationState);
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
        this.setPose(Pose.EMERGING);
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
            if (this.isAppearTick > MathHelper.secondsToTicks(0.5F)){
                this.setAnimationState(IDLE);
                this.setPose(Pose.STANDING);
            } else {
                this.setAnimationState(APPEAR);
            }
        }
        if (this.level().isClientSide) {
            this.idleAnimationState.animateWhen(!this.walkAnimation.isMoving() && this.isCurrentAnimation(IDLE), this.tickCount);
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
            if (this.isCurrentAnimation(ROAR)) {
                if (modifiableattributeinstance != null) {
                    modifiableattributeinstance.removeModifier(ROAR_SPEED_MODIFIER);
                    modifiableattributeinstance.addTransientModifier(ROAR_SPEED_MODIFIER);
                }
            } else {
                if (modifiableattributeinstance != null) {
                    if (modifiableattributeinstance.hasModifier(ROAR_SPEED_MODIFIER.id())) {
                        modifiableattributeinstance.removeModifier(ROAR_SPEED_MODIFIER);
                    }
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (p_21016_.getEntity() != null && this.isAlive()) {
            if (this.bloodCooldown <= 0) {
                MobUtil.shootOutBlood(this, true);
                ++this.bloodToken;
                this.bloodCooldown = 10;
            }
        }
        return super.hurt(p_21016_, p_21017_);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean flag = super.doHurtTarget(target);
        if (flag && this.isAlive()) {
            if (this.level().getRandom().nextBoolean()) {
                MobUtil.shootOutBlood(target, true);
                ++this.bloodToken;
            }
        }
        return flag;
    }

    public static class RoarGoal extends Goal {
        public BloodSpreader bloodSpreader;
        public int roarTick;

        public RoarGoal(BloodSpreader bloodSpreader) {
            this.bloodSpreader = bloodSpreader;
        }

        @Override
        public boolean canUse() {
            if (this.bloodSpreader.isAppearing()) {
                return false;
            }
            if (this.bloodSpreader.bloodToken < 5) {
                return false;
            }
            return !this.getList().isEmpty();
        }

        @Override
        public boolean canContinueToUse() {
            return this.bloodSpreader.isAlive() && this.roarTick < MathHelper.secondsToTicks(1.5F);
        }

        public List<BloodClot> getList() {
            return this.bloodSpreader.level().getEntitiesOfClass(BloodClot.class, this.bloodSpreader.getBoundingBox().inflate(15.0D));
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
        public void start() {
            super.start();
            this.bloodSpreader.getNavigation().stop();
            this.bloodSpreader.setAnimationState(ROAR);
            this.roarTick = 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.bloodSpreader.setAnimationState(IDLE);
            this.bloodSpreader.bloodToken = 0;
            this.roarTick = 0;
        }

        @Override
        public void tick() {
            super.tick();
            ++this.roarTick;
            if (!this.bloodSpreader.isCurrentAnimation(ROAR)) {
                this.bloodSpreader.setAnimationState(ROAR);
            }
            this.bloodSpreader.getNavigation().stop();
            if (this.roarTick == 10) {
                this.bloodSpreader.playSound(CHSounds.HOWL_OF_TECHNOFLESH.get(), 2.0F, 1.0F);
                if (!this.getList().isEmpty()) {
                    for (BloodClot bloodClot : this.getList()) {
                        bloodClot.spawnBloodyCopy(this.bloodSpreader.getTarget());
                    }
                }
            }
        }
    }
}

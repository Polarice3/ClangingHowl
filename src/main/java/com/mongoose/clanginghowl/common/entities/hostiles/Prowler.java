package com.mongoose.clanginghowl.common.entities.hostiles;

import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.common.effects.CHEffects;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

public class Prowler extends TFleshMonster {
    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(Prowler.class, EntityDataSerializers.INT);
    public static AttributeModifier INVISIBLE_SPEED_MODIFIER = new AttributeModifier(com.mongoose.clanginghowl.ClangingHowl.location("entity.clanginghowl.prowler.invisible"), 0.15D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    public static String IDLE = "idle";
    public static String ATTACK = "attack";
    public static String APPEAR = "appear";
    public int beforeAttackTick;
    public int isAppearTick;
    public int attackTick;
    public int retreatTick;
    public int retaliateTick;
    public int weakenDefense;
    public int becomeInvisible;
    public float accumulatedDamage;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState appearAnimationState = new AnimationState();

    public Prowler(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward = 14;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidTargetGoal<>(this, LivingEntity.class, 16, 1.0D, 1.2D){
            @Override
            public boolean canUse() {
                return Prowler.this.retreatTick > 0 && Prowler.this.retaliateTick <= 0 && super.canUse();
            }
        });
        this.goalSelector.addGoal(4, new com.mongoose.clanginghowl.common.entities.ai.ModMeleeAttackGoal(this, 1.1F, true) {
            @Override
            protected void checkAndPerformAttack(LivingEntity p_25557_, double p_25558_) {
                if (this.mob instanceof Prowler prowler) {
                    double d0 = this.getAttackReachSqr(p_25557_);
                    if (p_25558_ <= d0 && this.getTicksUntilNextAttack() <= 0) {
                        if (prowler.isInvisible()) {
                            prowler.removeEffect(MobEffects.INVISIBILITY);
                            prowler.beforeAttackTick = 2;
                            prowler.weakenDefense = MathHelper.secondsToTicks(4);
                        } else if (prowler.beforeAttackTick <= 0) {
                            this.resetAttackCooldown();
                            this.mob.swing(InteractionHand.MAIN_HAND);
                            this.mob.doHurtTarget(p_25557_);
                        }
                    }
                }
            }

            @Override
            protected double getAttackReachSqr(LivingEntity p_25556_) {
                return super.getAttackReachSqr(p_25556_) - 0.4D;
            }
        });
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 70.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.29D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.8D);
    }

    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIM_STATE, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        if (p_21450_.contains("AccumulatedDamage")) {
            this.accumulatedDamage = p_21450_.getFloat("AccumulatedDamage");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putFloat("AccumulatedDamage", this.accumulatedDamage);
    }

    public static boolean checkProwlerSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource randomSource) {
        if (CHConfig.ProwlerDaySpawn.get() >= 0 && levelAccessor.dayTime() >= MathHelper.minecraftDayToTicks(CHConfig.ProwlerDaySpawn.get())) {
            return levelAccessor.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(levelAccessor, blockPos, randomSource) && checkMobSpawnRules(entityType, levelAccessor, spawnType, blockPos, randomSource);
        }
        return false;
    }

    public boolean hasLineOfSight(Entity p_149755_) {
        if (CHCapHelper.isFlashing(this)) {
            return false;
        }
        return super.hasLineOfSight(p_149755_);
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
        return CHSounds.FLESH_MAIDEN_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return CHSounds.PROWLER_STEP.get();
    }

    protected void playStepSound(BlockPos p_34316_, BlockState p_34317_) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
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
        this.setPose(Pose.EMERGING);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    @Override
    public void swing(InteractionHand p_21007_) {
        super.swing(p_21007_);
        this.attackTick = 15;
        this.setAnimationState(ATTACK);
    }

    @Override
    public void setTarget(@Nullable LivingEntity p_21544_) {
        if (p_21544_ != null && this.getTarget() != p_21544_) {
            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, -1, 0, false, false));
        }
        super.setTarget(p_21544_);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (!this.level().isClientSide) {
            if (entityIn instanceof LivingEntity target) {
                if (flag) {
                    if (this.weakenDefense > 0) {
                        this.playSound(CHSounds.TECHNO_FLESH_ATTACK.get(), 2.0F, this.getVoicePitch());
                        target.addEffect(new MobEffectInstance(CHEffects.WEAKENED_DEFENSE, 500));
                        this.weakenDefense = 0;
                    }
                    int amp = 0;
                    if (CHConfig.TechnoFleshBuff.get()) {
                        if (MobUtil.technoFleshBuffTime(this.level()) >= MathHelper.minecraftDayToTicks(40)) {
                            amp = 1;
                        }
                    }
                    target.addEffect(new MobEffectInstance(CHEffects.NEUROTOXIN, 200, amp));
                } else if (target.isBlocking()) {
                    if (this.weakenDefense > 0) {
                        if (target instanceof Player player) {
                            player.disableShield();
                            this.weakenDefense = 0;
                        }
                    }
                }
            }
        }

        return flag;
    }

    public boolean isAppearing() {
        return this.hasPose(Pose.EMERGING);
    }

    @Override
    public double getVisibilityPercent(@Nullable Entity p_20969_) {
        if (this.isInvisible()) {
            return 0.01D;
        }
        return super.getVisibilityPercent(p_20969_);
    }

    public int invisibleTime() {
        return 20;
    }

    public void tick() {
        super.tick();
        if (this.hasPose(Pose.EMERGING)){
            ++this.isAppearTick;
            if (this.isAppearTick > MathHelper.secondsToTicks(0.75F)){
                this.setAnimationState(IDLE);
                this.setPose(Pose.STANDING);
            } else {
                this.setAnimationState(APPEAR);
            }
        }
        if (this.level().isClientSide) {
            this.idleAnimationState.animateWhen(!this.walkAnimation.isMoving() && this.isCurrentAnimation(IDLE), this.tickCount);
            if (this.isInvisible()) {
                if (this.becomeInvisible < this.invisibleTime()) {
                    ++this.becomeInvisible;
                }
            } else {
                this.becomeInvisible = 0;
            }
        }
        if (!this.level().isClientSide) {
            if (this.attackTick > 0) {
                --this.attackTick;
            } else if (this.isCurrentAnimation(ATTACK)) {
                this.setAnimationState(IDLE);
            }
            if (this.weakenDefense > 0) {
                --this.weakenDefense;
            }
            if (this.beforeAttackTick > 0) {
                --this.beforeAttackTick;
            }
            if (this.retreatTick > 0) {
                if (this.retreatTick == MathHelper.secondsToTicks(4.5F) && this.isAlive()) {
                    this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, -1, 0, false, false));
                }
                if (this.retaliateTick <= 0) {
                    --this.retreatTick;
                }
            }
            if (this.retaliateTick > 0) {
                --this.retaliateTick;
            }
            if (!this.hasEffect(MobEffects.INVISIBILITY)) {
                if (this.getTarget() != null && this.isAlive()) {
                    if (this.retreatTick <= 0) {
                        if (this.getTarget().distanceTo(this) >= 16.0F) {
                            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, -1, 0, false, false));
                        }
                    }
                }
            } else if (this.isDeadOrDying() || this.getTarget() == null || !this.getTarget().isAlive() || this.retreatTick > MathHelper.secondsToTicks(4.5F)) {
                this.removeEffect(MobEffects.INVISIBILITY);
            }
            AttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (modifiableattributeinstance != null) {
                if (this.isInvisible()) {
                    modifiableattributeinstance.removeModifier(INVISIBLE_SPEED_MODIFIER);
                    modifiableattributeinstance.addTransientModifier(INVISIBLE_SPEED_MODIFIER);
                } else {
                    if (modifiableattributeinstance.hasModifier(INVISIBLE_SPEED_MODIFIER.id())) {
                        modifiableattributeinstance.removeModifier(INVISIBLE_SPEED_MODIFIER);
                    }
                }
            }
        }
    }
}

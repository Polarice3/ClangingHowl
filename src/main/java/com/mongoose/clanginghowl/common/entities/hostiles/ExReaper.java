package com.mongoose.clanginghowl.common.entities.hostiles;

import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.config.CHConfig;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.init.CHTags;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExReaper extends Monster {
    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(ExReaper.class, EntityDataSerializers.INT);
    public static String IDLE = "idle";
    public static String ATTACK = "attack";
    public static String INFECT = "infect";
    public static String APPEAR = "appear";
    public int isAppearTick;
    public int attackTick;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState infectAnimationState = new AnimationState();
    public AnimationState appearAnimationState = new AnimationState();

    public ExReaper(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward = 6;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false){
            @Override
            public boolean canUse() {
                return super.canUse() && ExReaper.this.attackTick <= 0;
            }
        });
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, livingEntity -> MobUtil.isReaperConvert(livingEntity) && !livingEntity.hasEffect(CHEffects.BEYOND_FLESH.get())));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ARMOR, 7.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIM_STATE, 0);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (entity == null) {
            return false;
        } else if (entity == this) {
            return true;
        } else if (super.isAlliedTo(entity)) {
            return true;
        } else if (entity.getType().is(CHTags.EntityTypes.TECHNO_FLESH)) {
            return this.getTeam() == null && entity.getTeam() == null;
        } else {
            return false;
        }
    }

    public static boolean checkExReaperSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource randomSource) {
        if (CHConfig.ExReaperDaySpawn.get() >= 0 && levelAccessor.dayTime() >= CHConfig.ExReaperDaySpawn.get()) {
            return levelAccessor.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(levelAccessor, blockPos, randomSource) && checkMobSpawnRules(entityType, levelAccessor, spawnType, blockPos, randomSource);
        }
        return false;
    }

    @Override
    public boolean canAttack(LivingEntity p_21171_) {
        return super.canAttack(p_21171_) && !p_21171_.hasEffect(CHEffects.BEYOND_FLESH.get());
    }

    protected SoundEvent getAmbientSound() {
        return CHSounds.TECHNO_FLESH_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource p_34327_) {
        return CHSounds.TECHNO_FLESH_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CHSounds.TECHNO_FLESH_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return CHSounds.TECHNO_FLESH_STEP.get();
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
        } else if (Objects.equals(animation, INFECT)){
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
                        this.infectAnimationState.startIfStopped(this.tickCount);
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
        animationStates.add(this.infectAnimationState);
        animationStates.add(this.appearAnimationState);
        return animationStates;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.hasPose(Pose.EMERGING) ? 1 : 0);
    }

    public void recreateFromPacket(ClientboundAddEntityPacket p_219420_) {
        super.recreateFromPacket(p_219420_);
        if (p_219420_.getData() == 1) {
            this.setPose(Pose.EMERGING);
        }

    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.setPose(Pose.EMERGING);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
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
                this.getNavigation().stop();
                this.moveControl.strafe(0.0F, 0.0F);
                --this.attackTick;
            } else if (this.isCurrentAnimation(ATTACK) || this.isCurrentAnimation(INFECT)) {
                this.setAnimationState(IDLE);
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);

        if (!this.level().isClientSide) {
            if (flag) {
                boolean infect = false;
                if (entityIn instanceof Mob mob) {
                    if (MobUtil.isReaperConvert(mob) && mob.getTarget() != this) {
                        infect = true;
                    }
                }
                this.attackTick = 10;
                if (!infect) {
                    this.setAnimationState(ATTACK);
                } else {
                    this.playSound(CHSounds.INJECT.get(), 1.0F, 1.0F);
                    this.setAnimationState(INFECT);
                    LivingEntity livingEntity = (LivingEntity) entityIn;
                    int time = 300;
                    if (livingEntity instanceof AbstractVillager) {
                        time = 12000;
                    } else if (livingEntity instanceof AbstractIllager) {
                        time = 500;
                    }
                    livingEntity.addEffect(new MobEffectInstance(CHEffects.BEYOND_FLESH.get(), time, 0, false, false));
                    this.setTarget(null);
                    if (livingEntity instanceof Mob mob) {
                        mob.setTarget(null);
                        mob.setLastHurtByMob(null);
                    }
                }
            }
        }
        return flag;
    }
}

package com.mongoose.clanginghowl.common.entities.hostiles;

import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.entities.projectiles.SpitProjectile;
import com.mongoose.clanginghowl.config.CHConfig;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.init.CHTags;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class HeartOfDecay extends Spider implements RangedAttackMob {
    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(HeartOfDecay.class, EntityDataSerializers.INT);
    public static AttributeModifier SHOOT_SPEED_MODIFIER = new AttributeModifier(CHUUIDUtil.createUUID("entity.clanginghowl.heart_of_decay.immobile"), "Shooting speed penalty", -1.0D, AttributeModifier.Operation.ADDITION);
    public static String IDLE = "idle";
    public static String ATTACK = "attack";
    public static String SPIT = "spit";
    public static String APPEAR = "appear";
    public int isAppearTick;
    public int attackTick;
    public int spitCool;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState spitAnimationState = new AnimationState();
    public AnimationState appearAnimationState = new AnimationState();

    public HeartOfDecay(EntityType<? extends Spider> p_33786_, Level p_33787_) {
        super(p_33786_, p_33787_);
        this.xpReward = 4;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25F) {
            protected boolean shouldPanic() {
                return this.mob.isOnFire();
            }
        });
        this.goalSelector.addGoal(2, new WebShootGoal<>(this));
        this.goalSelector.addGoal(4, new HoDAttackGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new HoDTargetGoal<>(this, Player.class));
        this.targetSelector.addGoal(2, new HoDTargetGoal<>(this, LivingEntity.class, livingEntity -> MobUtil.isTechnoConvert(livingEntity) && !livingEntity.hasEffect(CHEffects.BEYOND_FLESH.get())));
        this.targetSelector.addGoal(3, new HoDTargetGoal<>(this, IronGolem.class));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 14.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIM_STATE, 0);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("SpitCool", this.spitCool);
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("SpitCool")){
            this.spitCool = pCompound.getInt("SpitCool");
        }
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

    public static boolean checkHoDSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource randomSource) {
        if (CHConfig.HoDDaySpawn.get() >= 0 && levelAccessor.dayTime() >= CHConfig.HoDDaySpawn.get()) {
            return levelAccessor.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(levelAccessor, blockPos, randomSource) && checkMobSpawnRules(entityType, levelAccessor, spawnType, blockPos, randomSource);
        }
        return false;
    }

    @Override
    public boolean canAttack(LivingEntity p_21171_) {
        return super.canAttack(p_21171_) && !p_21171_.hasEffect(CHEffects.BEYOND_FLESH.get());
    }

    @Override
    public void playAmbientSound() {
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33814_) {
        return CHSounds.TECHNO_FLESH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CHSounds.HEART_OF_DECAY_DEATH.get();
    }

    protected PathNavigation createNavigation(Level p_33802_) {
        return new WallClimberNavigation(this, p_33802_);
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
        } else if (Objects.equals(animation, SPIT)){
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
                        this.spitAnimationState.startIfStopped(this.tickCount);
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
        animationStates.add(this.spitAnimationState);
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

    @Override
    protected float getStandingEyeHeight(Pose p_33799_, EntityDimensions p_33800_) {
        return p_33800_.height * 0.85F;
    }

    public boolean isMeleeAttacking() {
        return this.attackTick > 0;
    }

    public boolean isAppearing() {
        return this.hasPose(Pose.EMERGING);
    }

    public void shootSpit() {
        SpitProjectile snowball = new SpitProjectile(this, this.level());
        Vec3 vector3d = this.getViewVector(1.0F);
        snowball.setPos(this.getX() + vector3d.x / 2,
                this.getEyeY() - 0.2,
                this.getZ() + vector3d.z / 2);
        if (this.getTarget() != null) {
            double d0 = this.getTarget().getX() - this.getX();
            double d1 = this.getTarget().getY() - this.getY();
            double d2 = this.getTarget().getZ() - this.getZ();
            double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
            snowball.shoot(d0, d1 + 0.25D, d2, 1.6F, 1.0F);
        } else {
            snowball.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0F, 1.6F, 1.0F);
        }
        this.playSound(SoundEvents.LLAMA_SPIT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(snowball);
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
            if (this.spitCool > 0) {
                --this.spitCool;
            }
            if (this.attackTick > 0) {
                --this.attackTick;
            } else if (this.isCurrentAnimation(ATTACK)) {
                this.setAnimationState(IDLE);
            }

            AttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (this.isCurrentAnimation(SPIT)) {
                if (modifiableattributeinstance != null) {
                    modifiableattributeinstance.removeModifier(SHOOT_SPEED_MODIFIER);
                    modifiableattributeinstance.addTransientModifier(SHOOT_SPEED_MODIFIER);
                }
            } else {
                if (modifiableattributeinstance != null) {
                    if (modifiableattributeinstance.hasModifier(SHOOT_SPEED_MODIFIER)) {
                        modifiableattributeinstance.removeModifier(SHOOT_SPEED_MODIFIER);
                    }
                }
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);

        if (!this.level().isClientSide) {
            if (flag) {
                this.attackTick = 10;
                this.setAnimationState(ATTACK);
                if (entityIn instanceof LivingEntity livingEntity) {
                    if (MobUtil.isTechnoConvert(livingEntity)) {
                        this.playSound(CHSounds.INFECT.get(), 1.0F, 1.0F);
                        int time = 1000;
                        if (livingEntity instanceof Zombie) {
                            time = 300;
                        } else if (livingEntity instanceof AbstractVillager) {
                            time = 12000;
                        } else if (livingEntity instanceof AbstractIllager) {
                            time = 500;
                        }
                        livingEntity.addEffect(new MobEffectInstance(CHEffects.BEYOND_FLESH.get(), time, 0, false, false));
                        this.discard();
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public void performRangedAttack(LivingEntity p_33317_, float p_33318_) {
        this.shootSpit();
    }

    static class HoDAttackGoal extends MeleeAttackGoal {
        public HeartOfDecay heartOfDecay;

        public HoDAttackGoal(HeartOfDecay p_33822_) {
            super(p_33822_, 1.1F, true);
            this.heartOfDecay = p_33822_;
        }

        @Override
        public boolean canUse() {
            return super.canUse()
                    && !this.mob.isVehicle()
                    && !this.heartOfDecay.isAppearing()
                    && !this.heartOfDecay.isOnFire()
                    && this.mob.getTarget() != null;
        }

        public boolean canContinueToUse() {
            if (this.mob.isOnFire()) {
                this.mob.setTarget(null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }

        protected double getAttackReachSqr(LivingEntity p_33825_) {
            return (double)(4.0F + p_33825_.getBbWidth());
        }
    }

    public static class WebShootGoal<T extends HeartOfDecay> extends Goal {
        private final T mob;
        public int attackTick;
        @Nullable
        private LivingEntity target;

        public WebShootGoal(T mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null
                    && livingentity.isAlive()
                    && !MobUtil.isTechnoConvert(livingentity)
                    && !this.mob.isOnFire()
                    && !this.mob.isAppearing()
                    && !this.mob.isMeleeAttacking()
                    && this.mob.spitCool <= 0
                    && this.mob.hasLineOfSight(livingentity)) {
                this.target = livingentity;
                return this.mob.distanceTo(livingentity) > 4.0F && this.mob.distanceTo(livingentity) <= 12.0F;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.attackTick < 20;
        }

        @Override
        public void start() {
            super.start();
            this.mob.getNavigation().stop();
            this.mob.shootSpit();
            this.mob.setAnimationState(SPIT);
        }

        @Override
        public void stop() {
            this.target = null;
            this.attackTick = 0;
            this.mob.spitCool = 160;
            this.mob.setAnimationState(IDLE);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            ++this.attackTick;
            this.mob.getNavigation().stop();
            this.mob.moveControl.strafe(0.0F, 0.0F);
            if (this.target != null) {
                MobUtil.instaLook(this.mob, this.target);
            }
        }
    }

    static class HoDTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public HoDTargetGoal(HeartOfDecay p_33832_, Class<T> p_33833_) {
            super(p_33832_, p_33833_, true);
        }

        public HoDTargetGoal(HeartOfDecay p_199891_, Class<T> p_199892_, Predicate<LivingEntity> p_199894_) {
            super(p_199891_, p_199892_, 10, true, false, p_199894_);
        }

        public boolean canUse() {
            return !this.mob.isOnFire() && super.canUse();
        }
    }
}

package com.mongoose.clanginghowl.common.entities.hostiles;

import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.client.particles.ShockWaveParticleOption;
import com.mongoose.clanginghowl.common.entities.ai.ModMeleeAttackGoal;
import com.mongoose.clanginghowl.common.entities.projectiles.BloodTrailProjectile;
import com.mongoose.clanginghowl.config.CHConfig;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.init.CHTags;
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
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class Carcass extends TFleshMonster {
    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(Carcass.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(Carcass.class, EntityDataSerializers.INT);
    public static AttributeModifier SPECIAL_SPEED_MODIFIER = new AttributeModifier(com.mongoose.clanginghowl.ClangingHowl.location("entity.clanginghowl.carcass.special_attack"), -1.0D, AttributeModifier.Operation.ADD_VALUE);
    private final DynamicGameEventListener<GameEventListener> gameEventListener;
    public static String IDLE = "idle";
    public static String ATTACK = "attack";
    public static String QUAKE = "quake";
    public static String SPIT = "spit";
    public static String APPEAR = "appear";
    public static String GROWTH = "growth";
    public int isAppearTick;
    public int attackTick;
    public int growthTick;
    public int spitCooldown;
    public int quakeCooldown;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState quakeAnimationState = new AnimationState();
    public AnimationState spitAnimationState = new AnimationState();
    public AnimationState appearAnimationState = new AnimationState();
    public AnimationState growthAnimationState = new AnimationState();

    public Carcass(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward = 24;
        this.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.6D);
        this.gameEventListener = new DynamicGameEventListener<>(new GameEventListener() {
            public PositionSource getListenerSource() {
                return new BlockPositionSource(Carcass.this.blockPosition());
            }

            public int getListenerRadius() {
                return 10;
            }

            public GameEventListener.DeliveryMode getDeliveryMode() {
                return GameEventListener.DeliveryMode.BY_DISTANCE;
            }

            public boolean handleGameEvent(ServerLevel serverLevel, net.minecraft.core.Holder<GameEvent> p_282184_, GameEvent.Context p_283014_, Vec3 p_282350_) {
                if (!Carcass.this.isRemoved()) {
                    if (p_282184_ == GameEvent.ENTITY_DIE) {
                        Entity sourceEntity = p_283014_.sourceEntity();
                        if (sourceEntity != null) {
                            if (sourceEntity.getType().is(CHTags.EntityTypes.TECHNO_FLESH) && Carcass.this.growthTick <= 0 && !Carcass.this.isCurrentAnimation(GROWTH)) {
                                Carcass.this.setAnimationState(GROWTH);
                                Carcass.this.growthTick = 20;
                                return true;
                            }
                        }
                    }

                }
                return false;
            }
        });
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SpitGoal(this));
        this.goalSelector.addGoal(1, new QuakeGoal(this));
        this.goalSelector.addGoal(4, new ModMeleeAttackGoal(this, 1.2F, true) {
            @Override
            protected int getAttackInterval() {
                return super.getAttackInterval() + this.adjustedTickDelay(5);
            }

            protected double getAttackReachSqr(LivingEntity p_33377_) {
                return 4.0F + p_33377_.getBbWidth();
            }
        });
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.ARMOR, 6.0D);
    }

    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIM_STATE, 0);
        builder.define(ID_SIZE, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putInt("SpitCoolDown", this.spitCooldown);
        p_21484_.putInt("QuakeCoolDown", this.quakeCooldown);
        p_21484_.putInt("Size", this.getCarcassSize());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        if (p_21450_.contains("SpitCoolDown")) {
            this.spitCooldown = p_21450_.getInt("SpitCoolDown");
        }
        if (p_21450_.contains("QuakeCoolDown")) {
            this.quakeCooldown = p_21450_.getInt("QuakeCoolDown");
        }
        this.setCarcassSize(p_21450_.getInt("Size"));
    }

    public void setCarcassSize(int p_33109_) {
        this.entityData.set(ID_SIZE, Mth.clamp(p_33109_, 0, 5));
    }

    private void updateSizeInfo() {
        this.refreshDimensions();
        AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (health != null) {
            health.setBaseValue(80.0D + (this.getCarcassSize() * 15.0D));
            this.heal(15.0F);
        }
        if (attack != null) {
            attack.setBaseValue(8.0D + this.getCarcassSize());
        }
    }

    public int getCarcassSize() {
        return this.entityData.get(ID_SIZE);
    }

    public EntityDimensions getDefaultDimensions(Pose p_33113_) {
        int i = this.getCarcassSize();
        EntityDimensions entitydimensions = super.getDefaultDimensions(p_33113_);
        return entitydimensions.scale((0.1F * i) + 0.9F);
    }

    public static boolean checkCarcassSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource randomSource) {
        if (CHConfig.CarcassDaySpawn.get() >= 0 && levelAccessor.dayTime() >= MathHelper.minecraftDayToTicks(CHConfig.CarcassDaySpawn.get())) {
            return levelAccessor.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(levelAccessor, blockPos, randomSource) && checkMobSpawnRules(entityType, levelAccessor, spawnType, blockPos, randomSource);
        }
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return CHSounds.TECHNO_FLESH_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource p_34327_) {
        return CHSounds.TECHNO_FLESH_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CHSounds.FLESH_MAIDEN_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return CHSounds.CARCASS_STEP.get();
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
        } else if (Objects.equals(animation, QUAKE)){
            return 2;
        } else if (Objects.equals(animation, SPIT)){
            return 3;
        } else if (Objects.equals(animation, APPEAR)){
            return 4;
        } else if (Objects.equals(animation, GROWTH)){
            return 5;
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
        if (ID_SIZE.equals(p_219422_)) {
            this.updateSizeInfo();
        }
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
                        this.quakeAnimationState.startIfStopped(this.tickCount);
                        break;
                    case 3:
                        this.stopAllAnimation();
                        this.spitAnimationState.startIfStopped(this.tickCount);
                        break;
                    case 4:
                        this.stopAllAnimation();
                        this.appearAnimationState.startIfStopped(this.tickCount);
                        break;
                    case 5:
                        this.stopAllAnimation();
                        this.growthAnimationState.startIfStopped(this.tickCount);
                        break;
                }
            }
        }

        super.onSyncedDataUpdated(p_219422_);
    }

    public List<AnimationState> getAnimations(){
        List<AnimationState> animationStates = new ArrayList<>();
        animationStates.add(this.attackAnimationState);
        animationStates.add(this.quakeAnimationState);
        animationStates.add(this.spitAnimationState);
        animationStates.add(this.appearAnimationState);
        animationStates.add(this.growthAnimationState);
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

    public boolean isAppearing() {
        return this.hasPose(Pose.EMERGING);
    }

    public void increaseSize() {
        if (this.getCarcassSize() < 5) {
            this.setCarcassSize(this.getCarcassSize() + 1);
            this.playSound(CHSounds.FLESH_TEAR.get(), 1.0F, 1.0F);
            if (this.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 16; ++i) {
                    ParticleUtil.addParticlesAroundMiddleSelf(serverLevel, CHParticleTypes.CRIMSON_POOF.get(), this);
                }
            }
        } else {
            this.heal(10.0F);
        }
    }

    @Override
    public void swing(InteractionHand p_21007_) {
        super.swing(p_21007_);
        this.attackTick = 20;
        this.setAnimationState(ATTACK);
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
        }
        if (!this.level().isClientSide) {
            if (this.attackTick > 0) {
                --this.attackTick;
            } else if (this.isCurrentAnimation(ATTACK)) {
                this.setAnimationState(IDLE);
            }
            if (this.spitCooldown > 0){
                --this.spitCooldown;
            }
            if (this.quakeCooldown > 0){
                --this.quakeCooldown;
            }
            if (this.growthTick > 0) {
                --this.growthTick;
                if (this.growthTick == 5) {
                    this.increaseSize();
                }
            } else if (this.isCurrentAnimation(GROWTH)) {
                this.setAnimationState(IDLE);
            }
            AttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (this.isCurrentAnimation(SPIT) || this.isCurrentAnimation(QUAKE) || this.isCurrentAnimation(ATTACK) || this.isCurrentAnimation(GROWTH)) {
                if (modifiableattributeinstance != null) {
                    modifiableattributeinstance.removeModifier(SPECIAL_SPEED_MODIFIER);
                    modifiableattributeinstance.addTransientModifier(SPECIAL_SPEED_MODIFIER);
                }
            } else {
                if (modifiableattributeinstance != null) {
                    if (modifiableattributeinstance.hasModifier(SPECIAL_SPEED_MODIFIER.id())) {
                        modifiableattributeinstance.removeModifier(SPECIAL_SPEED_MODIFIER);
                    }
                }
            }
        }
    }

    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> p_218348_) {
        Level level = this.level();
        if (level instanceof ServerLevel serverlevel) {
            p_218348_.accept(this.gameEventListener, serverlevel);
        }

    }

    public static class SpitGoal extends Goal {
        public Carcass carcass;
        public LivingEntity target;
        public int spitTick;
        public static double DISTANCE = 16.0D;

        public SpitGoal(Carcass carcass) {
            this.carcass = carcass;
        }

        @Override
        public boolean canUse() {
            if (this.carcass.isAppearing()) {
                return false;
            }
            if (this.carcass.spitCooldown > 0) {
                return false;
            }
            if (!this.carcass.isCurrentAnimation(IDLE)) {
                return false;
            }
            if (this.carcass.getTarget() != null && this.carcass.getTarget().isAlive() && this.carcass.hasLineOfSight(this.carcass.getTarget())) {
                this.target = this.carcass.getTarget();
            } else if (this.otherTarget() != null) {
                this.target = this.otherTarget();
            }
            return this.target != null && this.carcass.distanceTo(this.target) > 4.0D && this.carcass.distanceTo(this.target) <= DISTANCE;
        }

        @Override
        public boolean canContinueToUse() {
            return this.carcass.isAlive() && this.spitTick < MathHelper.secondsToTicks(1.4167F);
        }

        public LivingEntity otherTarget() {
            return this.carcass.level().getNearestEntity(LivingEntity.class, TargetingConditions.forCombat().selector(MobUtil::canInfect), this.carcass, this.carcass.getX(), this.carcass.getY(), this.carcass.getZ(), this.carcass.getBoundingBox().inflate(DISTANCE));
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
            this.carcass.getNavigation().stop();
            this.carcass.setAnimationState(SPIT);
            if (this.target != null) {
                MobUtil.instaLook(this.carcass, this.target);
            }
            this.spitTick = 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.target = null;
            this.carcass.setAnimationState(IDLE);
            this.carcass.spitCooldown = 300;
            this.spitTick = 0;
        }

        @Override
        public void tick() {
            super.tick();
            ++this.spitTick;
            if (!this.carcass.isCurrentAnimation(SPIT)) {
                this.carcass.setAnimationState(SPIT);
            }
            if (this.target != null) {
                MobUtil.instaLook(this.carcass, this.target);
            }
            this.carcass.getNavigation().stop();
            if (this.spitTick == 10) {
                this.carcass.playSound(CHSounds.CARCASS_SPIT.get(), 1.0F, 1.0F);
                for (int i = 0; i < 8; ++i) {
                    BloodTrailProjectile arrow = new BloodTrailProjectile(carcass.getX(), carcass.getEyeY() - (double)0.1F, carcass.getZ(), this.carcass.level());
                    arrow.setOwner(this.carcass);
                    arrow.setInfectious(true);
                    arrow.shootFromRotation(this.carcass, this.carcass.getXRot(), this.carcass.getYRot(), 0.0F, 1.0F, 30.0F);
                    this.carcass.level().addFreshEntity(arrow);
                }
            }
        }
    }

    public static class QuakeGoal extends Goal {
        public Carcass carcass;
        public int quakeTick;
        public static float RANGE = 4.5F;

        public QuakeGoal(Carcass carcass) {
            this.carcass = carcass;
        }

        @Override
        public boolean canUse() {
            if (this.carcass.isAppearing()) {
                return false;
            }
            if (this.carcass.quakeCooldown > 0) {
                return false;
            }
            if (!this.carcass.isCurrentAnimation(IDLE)) {
                return false;
            }
            if (this.carcass.getHealth() > this.carcass.getMaxHealth() * 0.9F) {
                return false;
            }
            return this.carcass.getTarget() != null && this.carcass.getTarget().isAlive() && this.carcass.hasLineOfSight(this.carcass.getTarget()) && this.carcass.getTarget().distanceTo(this.carcass) <= RANGE;
        }

        @Override
        public boolean canContinueToUse() {
            return this.carcass.isAlive() && this.quakeTick < MathHelper.secondsToTicks(1.5F);
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
            this.carcass.getNavigation().stop();
            this.carcass.setAnimationState(QUAKE);
            this.carcass.playSound(CHSounds.CARCASS_QUAKE.get(), 1.0F, 1.0F);
            if (this.carcass.getTarget() != null) {
                MobUtil.instaLook(this.carcass, this.carcass.getTarget());
            }
            this.quakeTick = 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.carcass.setAnimationState(IDLE);
            this.carcass.quakeCooldown = 160;
            this.quakeTick = 0;
        }

        @Override
        public void tick() {
            super.tick();
            ++this.quakeTick;
            if (!this.carcass.isCurrentAnimation(QUAKE)) {
                this.carcass.setAnimationState(QUAKE);
            }
            this.carcass.getNavigation().stop();
            if (this.quakeTick == 20) {
                if (this.carcass.level() instanceof ServerLevel serverLevel) {
                    ParticleUtil.sendAlwaysVisibleParticles(serverLevel, new ShockWaveParticleOption(RANGE, 0), this.carcass.getX(), this.carcass.getY() + 0.25F, this.carcass.getZ(), 1, 0, 0, 0, 0);
                    ParticleUtil.quakeCloud(serverLevel, this.carcass);
                }
                for (LivingEntity livingEntity : this.carcass.level().getEntitiesOfClass(LivingEntity.class, this.carcass.getBoundingBox().inflate(RANGE, RANGE / 2, RANGE), l -> !MobUtil.areAllies(this.carcass, l) && !l.getType().is(CHTags.EntityTypes.TECHNO_FLESH))) {
                    livingEntity.hurt(this.carcass.damageSources().mobAttack(this.carcass), 8.0F + this.carcass.getCarcassSize());
                    this.strongKnockback(livingEntity);
                    if (livingEntity.isBlocking()) {
                        if (livingEntity instanceof Player player) {
                            player.getCooldowns().addCooldown(player.getUseItem().getItem(), 200);
                            player.stopUsingItem();
                            player.level().broadcastEntityEvent(player, (byte)30);
                        }
                    }
                }
            }
        }

        private void strongKnockback(Entity p_33340_) {
            double d0 = p_33340_.getX() - this.carcass.getX();
            double d1 = p_33340_.getZ() - this.carcass.getZ();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
            p_33340_.push(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
        }
    }
}

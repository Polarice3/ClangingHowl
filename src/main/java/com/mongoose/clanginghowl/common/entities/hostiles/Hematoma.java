package com.mongoose.clanginghowl.common.entities.hostiles;

import com.mongoose.clanginghowl.client.particles.BloodSplashParticleOption;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.entities.projectiles.BloodTrailProjectile;
import com.mongoose.clanginghowl.config.CHConfig;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.init.CHTags;
import com.mongoose.clanginghowl.utils.*;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hematoma extends TFleshMonster {
    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(Hematoma.class, EntityDataSerializers.INT);
    public static AttributeModifier SPREAD_SPEED_MODIFIER = new AttributeModifier(com.mongoose.clanginghowl.ClangingHowl.location("entity.clanginghowl.hematoma.immobile"), -1.0D, AttributeModifier.Operation.ADD_VALUE);
    public static String IDLE = "idle";
    public static String ATTACK = "attack";
    public static String SPREAD = "spread";
    public static String APPEAR = "appear";
    public static String DEATH = "death";
    public int isAppearTick;
    public int attackTick;
    public int customDeathTime = 0;
    public DamageSource deathBlow = this.damageSources().generic();
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState attackAnimationState = new AnimationState();
    public AnimationState spreadAnimationState = new AnimationState();
    public AnimationState appearAnimationState = new AnimationState();
    public AnimationState deathAnimationState = new AnimationState();

    public Hematoma(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.xpReward = 5;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SpreadGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.1F, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.26D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D);
    }

    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIM_STATE, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putShort("DeathTime", (short)this.customDeathTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        this.customDeathTime = p_21450_.getShort("DeathTime");
    }

    @Override
    public boolean canAlert() {
        return false;
    }

    public static boolean checkHematomaSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource randomSource) {
        if (CHConfig.HematomaDaySpawn.get() >= 0 && levelAccessor.dayTime() >= MathHelper.minecraftDayToTicks(CHConfig.HematomaDaySpawn.get())) {
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
        return null;
    }

    @Override
    protected void playStepSound(BlockPos p_20135_, BlockState p_20136_) {
        this.playSound(CHSounds.HEMATOMA_STEP.get(), 0.15F, 1.0F);
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
        } else if (Objects.equals(animation, SPREAD)){
            return 2;
        } else if (Objects.equals(animation, APPEAR)){
            return 3;
        } else if (Objects.equals(animation, DEATH)){
            return 4;
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
                        this.spreadAnimationState.startIfStopped(this.tickCount);
                        break;
                    case 3:
                        this.stopAllAnimation();
                        this.appearAnimationState.startIfStopped(this.tickCount);
                        break;
                    case 4:
                        this.stopAllAnimation();
                        this.deathAnimationState.startIfStopped(this.tickCount);
                        break;
                }
            }
        }

        super.onSyncedDataUpdated(p_219422_);
    }

    public List<AnimationState> getAnimations(){
        List<AnimationState> animationStates = new ArrayList<>();
        animationStates.add(this.attackAnimationState);
        animationStates.add(this.spreadAnimationState);
        animationStates.add(this.appearAnimationState);
        return animationStates;
    }

    protected void tickDeath() {
        ++this.customDeathTime;
        if (this.getKillCredit() instanceof Player){
            this.lastHurtByPlayerTime = 100;
        } else {
            ((com.mongoose.clanginghowl.mixin.LivingEntityAccessor) this).clanginghowl$setLastHurtByMobTimestamp(100);
        }
        if (this.customDeathTime >= 30) {
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new BloodSplashParticleOption((float) (this.getBoundingBox().getSize() * 2.0F), 0), this.getX(), this.getY() + 1.0D, this.getZ(), 1, 0, 0, 0, 0);
                ParticleUtil.addParticlesAroundMiddleSelf(serverLevel, CHParticleTypes.CRIMSON_POOF.get(), this);
                for (int i = 0; i < serverLevel.getRandom().nextIntBetweenInclusive(4, 6); ++i) {
                    float randomY = serverLevel.getRandom().nextIntBetweenInclusive(1, 4) / 100.0F;
                    serverLevel.sendParticles(CHParticleTypes.BLOOD_STAIN.get(), this.getRandomX(0.5D), this.getY() + randomY, this.getRandomZ(0.5D), 1, 0, 0, 0, 1);
                }
                for (int i = 0; i < 8 + this.level().random.nextInt(8); ++i){
                    Projectile arrow = new BloodTrailProjectile(this.getX(), this.getY(), this.getZ(), this.level());
                    float yaw = this.random.nextFloat() * 360;
                    float pitch = this.random.nextFloat() * 90 - 75;
                    arrow.shootFromRotation(this, yaw, pitch, 0.0F, 0.75F, 0.1F);
                    this.level().addFreshEntity(arrow);
                }
                new FakeExplosion(serverLevel, this, serverLevel.damageSources().explosion(this, this), this.getX(), this.getY(), this.getZ(), 4.0F, 5.0F) {
                    @Override
                    public void explodeHurt(Entity target, DamageSource damageSource, double x, double y, double z, double seen, float actualDamage) {
                        boolean infectType = target instanceof Animal || target instanceof Zombie || target instanceof Spider || target instanceof AbstractVillager;
                        if (infectType) {
                            actualDamage = 2.0F;
                        }
                        if (!target.getType().is(CHTags.EntityTypes.TECHNO_FLESH)) {
                            if (target.hurt(damageSource, actualDamage)) {
                                if (infectType) {
                                    LivingEntity livingEntity = (LivingEntity) target;
                                    livingEntity.addEffect(new MobEffectInstance(CHEffects.BEYOND_FLESH, 300, 0, false, false));
                                }
                            }
                            double d11 = seen;
                            if (target instanceof LivingEntity) {
                                d11 = (seen * (1.0D - ((LivingEntity) target).getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.EXPLOSION_KNOCKBACK_RESISTANCE)));
                            }

                            if (target instanceof LivingEntity) {
                                MobUtil.push(target, x * d11, y * d11, z * d11);
                            }
                        }
                    }
                };
            }
            this.playSound(CHSounds.FLESH_RUPTURE_ENDING.get());
            this.die(this.deathBlow);
            this.remove(RemovalReason.KILLED);
        }
    }

    public void die(DamageSource cause) {
        if (this.customDeathTime > 0) {
            super.die(cause);
        } else {
            this.setAnimationState(DEATH);
            this.playSound(CHSounds.FLESH_RUPTURE_BEGINNING.get());
            this.deathBlow = cause;
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isDeadOrDying()) {
            this.deathBlow = pSource;
        }
        return super.hurt(pSource, pAmount);
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
            }
            AttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (this.isCurrentAnimation(SPREAD)) {
                if (modifiableattributeinstance != null) {
                    modifiableattributeinstance.removeModifier(SPREAD_SPEED_MODIFIER);
                    modifiableattributeinstance.addTransientModifier(SPREAD_SPEED_MODIFIER);
                }
            } else {
                if (modifiableattributeinstance != null) {
                    if (modifiableattributeinstance.hasModifier(SPREAD_SPEED_MODIFIER.id())) {
                        modifiableattributeinstance.removeModifier(SPREAD_SPEED_MODIFIER);
                    }
                }
            }
        }
    }

    @Override
    public void swing(InteractionHand p_21007_) {
        super.swing(p_21007_);
        this.attackTick = 10;
        this.setAnimationState(ATTACK);
    }

    public static class SpreadGoal extends Goal {
        public Hematoma hematoma;
        public int spreadTick;

        public SpreadGoal(Hematoma hematoma) {
            this.hematoma = hematoma;
        }

        @Override
        public boolean canUse() {
            if (this.hematoma.isAppearing()) {
                return false;
            }
            if (this.hematoma.hasEffect(CHEffects.ATTRACTION)) {
                return false;
            }
            List<Mob> list = this.hematoma.level().getEntitiesOfClass(Mob.class, this.hematoma.getBoundingBox().inflate(16.0D), mob -> !mob.getType().is(CHTags.EntityTypes.TECHNO_FLESH) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(mob) && (mob instanceof Animal || mob instanceof Enemy));
            return !list.isEmpty();
        }

        @Override
        public boolean canContinueToUse() {
            return this.hematoma.isAlive() && this.spreadTick < 30;
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
            this.hematoma.getNavigation().stop();
            this.hematoma.setAnimationState(SPREAD);
            this.spreadTick = 0;
        }

        @Override
        public void stop() {
            super.stop();
            this.hematoma.setAnimationState(IDLE);
            this.spreadTick = 0;
        }

        @Override
        public void tick() {
            super.tick();
            ++this.spreadTick;
            if (!this.hematoma.isCurrentAnimation(SPREAD)) {
                this.hematoma.setAnimationState(SPREAD);
            }
            this.hematoma.getNavigation().stop();
            if (this.spreadTick == 10) {
                this.hematoma.playSound(CHSounds.SMOKE_RELEASE.get());
                if (this.hematoma.level() instanceof ServerLevel serverLevel) {
                    ParticleUtil.attractionCloud(serverLevel, this.hematoma);
                }
                this.hematoma.addEffect(new MobEffectInstance(CHEffects.ATTRACTION, 500, 0, false, false));
            }
        }
    }
}

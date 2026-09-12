package com.mongoose.clanginghowl.common.events;

import net.neoforged.fml.common.EventBusSubscriber;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.particles.*;
import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.common.entities.ai.FollowAttractionGoal;
import com.mongoose.clanginghowl.common.entities.hostiles.HeartOfDecay;
import com.mongoose.clanginghowl.common.entities.hostiles.Prowler;
import com.mongoose.clanginghowl.common.items.BlazeBurnerItem;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.CHTiers;
import com.mongoose.clanginghowl.common.items.curios.EnergyBarrierGenerator;
import com.mongoose.clanginghowl.common.items.curios.XRayGoggles;
import com.mongoose.clanginghowl.common.items.energy.ChainswordItem;
import com.mongoose.clanginghowl.common.items.energy.EnergyItem;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import com.mongoose.clanginghowl.common.items.fuel.IFuel;
import com.mongoose.clanginghowl.common.network.CHNetwork;
import com.mongoose.clanginghowl.common.network.client.CIsMovingPacket;
import com.mongoose.clanginghowl.common.network.server.SReanimatorDeathPacket;
import com.mongoose.clanginghowl.common.world.data.ICHWorldData;
import com.mongoose.clanginghowl.config.CHConfig;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.init.CHTags;
import com.mongoose.clanginghowl.utils.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

@EventBusSubscriber(modid = ClangingHowl.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CHEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        Level world = event.getLevel();
        if (entity instanceof LivingEntity && !world.isClientSide()) {
            if (entity instanceof Mob mob) {
                if (mob instanceof Animal) {
                    mob.goalSelector.addGoal(0, new FollowAttractionGoal(mob, 1.0F, 3.0F, 20.0F));
                }
                if (mob instanceof Enemy && !(mob instanceof Creeper) && !mob.getType().is(CHTags.EntityTypes.TECHNO_FLESH)) {
                    mob.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(mob, LivingEntity.class, true, (livingEntity -> livingEntity.hasEffect(CHEffects.ATTRACTION))) {
                        @Override
                        protected double getFollowDistance() {
                            return 20.0D;
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void TickEvent(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
        if (CHCapHelper.getShakeTime(livingEntity) > 0) {
            CHCapHelper.setShakeTime(livingEntity, CHCapHelper.getShakeTime(livingEntity) - 1);
        }
        if (CHCapHelper.getTechnoResist(livingEntity) > 50.0F) {
            CHCapHelper.setTechnoResist(livingEntity, 50.0F);
        }
        if (CHCapHelper.isFlashing(livingEntity)){
            CHCapHelper.decreaseFlashTick(livingEntity);
        }
        if (livingEntity instanceof Player player) {
            if (player.onGround()) {
                if (CHCapHelper.getTicksInAir(player) > 0) {
                    CHCapHelper.setTicksInAir(player, 0);
                }
                if (player.getCooldowns().isOnCooldown(CHItems.JET_BOOTS.get())) {
                    player.getCooldowns().removeCooldown(CHItems.JET_BOOTS.get());
                }
            } else {
                CHCapHelper.setTicksInAir(player, CHCapHelper.getTicksInAir(player) + 1);
            }
        }
        if (livingEntity.level().isClientSide) {
            CHNetwork.sendToServer(new CIsMovingPacket(livingEntity.getId(), MobUtil.isMoving(livingEntity)));
        } else {
            if (livingEntity.hasEffect(CHEffects.ENLIGHTENED)) {
                CHCapHelper.setEnlightenedTick(livingEntity, 5);
            } else if (CHCapHelper.isEnlightened(livingEntity)) {
                CHCapHelper.setEnlightenedTick(livingEntity, 0);
            }
        }
        if (livingEntity.hasEffect(CHEffects.OVERDRIVE)) {
            if (MobUtil.isWalking(livingEntity)) {
                livingEntity.level().addParticle(CHParticleTypes.OVERDRIVE_FIRE.get(), livingEntity.getX(), livingEntity.getY() + 0.25F, livingEntity.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
        if (livingEntity.hasEffect(CHEffects.BEYOND_FLESH)) {
            MobEffectInstance instance = livingEntity.getEffect(CHEffects.BEYOND_FLESH);
            if (instance != null) {
                int duration = instance.getDuration();
                if (livingEntity.level() instanceof ServerLevel serverLevel) {
                    if (duration % 400 == 0 || (duration <= 100 && duration % 5 == 0)) {
                        for (int i = 0; i < serverLevel.getRandom().nextIntBetweenInclusive(1, 3); ++i) {
                            serverLevel.sendParticles(CHParticleTypes.INFECTION.get(), livingEntity.getRandomX(0.5D), livingEntity.getRandomY() + 0.5D, livingEntity.getRandomZ(0.5D), 1, 0.0D, 0.0D, 0.0D, 0);
                        }
                    }
                    if (livingEntity instanceof Mob mob && mob.isAlive()) {
                        if (duration <= 100) {
                            if (CHCapHelper.getShakeTime(mob) == 0) {
                                serverLevel.playSound(null, mob.getX(), mob.getY(), mob.getZ(), CHSounds.FLESH_RUPTURE_BEGINNING.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                            }
                            CHCapHelper.setShakeTime(mob, 20);
                            mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 1, false, false));
                        }

                        if (duration <= 60) {
                            MobUtil.convertTechno(mob);
                        }
                    }
                }
             }
        }

        AttributeInstance movement = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        boolean hasTendon = CHCuriosFinder.hasCurio(livingEntity, CHItems.TENDON_STRENGTHENER.get());

        AttributeModifier attributemodifier = new AttributeModifier(com.mongoose.clanginghowl.ClangingHowl.location("item.tendon.movement_speed"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        AttributeModifier attributemodifier2 = new AttributeModifier(com.mongoose.clanginghowl.ClangingHowl.location("item.tendon.sprint_speed"), 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        if (movement != null) {
            boolean removeModifier = false;
            if (hasTendon) {
                ItemStack itemStack = CHCuriosFinder.findCurio(livingEntity, CHItems.TENDON_STRENGTHENER.get());
                if (!itemStack.isEmpty() && !IEnergyItem.isEmpty(itemStack)) {
                    if (!movement.hasModifier(attributemodifier.id())) {
                        movement.addPermanentModifier(attributemodifier);
                    }
                    if (livingEntity.isSprinting()) {
                        if (!movement.hasModifier(attributemodifier2.id())) {
                            movement.addPermanentModifier(attributemodifier2);
                        }
                    } else {
                        if (movement.hasModifier(attributemodifier2.id())){
                            movement.removeModifier(attributemodifier2);
                        }
                    }
                } else {
                    removeModifier = true;
                }
            } else {
                removeModifier = true;
            }
            if (removeModifier) {
                if (movement.hasModifier(attributemodifier.id())){
                    movement.removeModifier(attributemodifier);
                }
                if (movement.hasModifier(attributemodifier2.id())){
                    movement.removeModifier(attributemodifier2);
                }
            }
        }
        if (livingEntity instanceof Mob mob) {
            if (mob.getTarget() instanceof Prowler prowler) {
                if (prowler.isInvisible()) {
                    mob.setTarget(null);
                }
            }
        }
    }

    private static final String NO_KNOCKBACK_TAG = "clanginghowl:no_knockback";

    @SubscribeEvent
    public static void AttackEvent(LivingIncomingDamageEvent event){
        LivingEntity victim = event.getEntity();
        if (event.getSource() instanceof NoKnockBackDamageSource){
            if (!victim.level().isClientSide) {
                CompoundTag tag = victim.getPersistentData();
                tag.putInt(NO_KNOCKBACK_TAG, victim.tickCount);
            }
        }
    }

    @SubscribeEvent
    public static void KnockBackEvents(LivingKnockBackEvent event){
        LivingEntity knocked = event.getEntity();
        if (!knocked.level().isClientSide) {
            CompoundTag tag = knocked.getPersistentData();
            if (tag.contains(NO_KNOCKBACK_TAG)) {
                int stampedTick = tag.getInt(NO_KNOCKBACK_TAG);
                if (knocked.tickCount - stampedTick <= 1) {
                    event.setCanceled(true);
                }
                tag.remove(NO_KNOCKBACK_TAG);
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvent(LivingIncomingDamageEvent event){
        LivingEntity victim = event.getEntity();
        Entity directEntity = event.getSource().getDirectEntity();
        if (event.getAmount() > 0.0F) {
            if (CHCapHelper.getTechnoResist(victim) > 0.0F && !victim.isOnFire() && !event.getSource().is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                float percent = CHCapHelper.getTechnoResist(victim) / 100.0F;
                float calc = Math.min(0.35F, 1.0F - percent);
                event.setAmount(event.getAmount() * calc);
            }
            if (event.getSource().is(CHDamageSource.ICICLE)) {
                if (victim.canFreeze()) {
                    victim.setTicksFrozen(140 + (160 * 2));
                }
            }
            if (event.getSource().is(DamageTypeTags.IS_FALL)) {
                if (CHCuriosFinder.hasCurio(victim, CHItems.JET_BOOTS.get())) {
                    event.setAmount(event.getAmount() * 0.35F);
                }
            }
            if (!victim.level().isClientSide) {
                if (CHCuriosFinder.hasCurio(victim, CHItems.ENERGY_BARRIER_GENERATOR.get())) {
                    ItemStack curio = CHCuriosFinder.findCurio(victim, CHItems.ENERGY_BARRIER_GENERATOR.get());
                    if (!curio.isEmpty() && !EnergyBarrierGenerator.isDischarged(curio) && !IEnergyItem.isEmpty(curio)) {
                        int energyConsume = Mth.floor(10 * event.getAmount());
                        if (IEnergyItem.currentEnergy(curio) > energyConsume) {
                            IEnergyItem.decreaseEnergy(curio, energyConsume);
                            victim.level().playSound(null, victim.getX(), victim.getY(), victim.getZ(), CHSounds.ENERGY_BARRIER_IMPACT.get(), victim.getSoundSource(), 2.0F, 1.0F);
                            event.setAmount(event.getAmount() * 0.5F);
                        } else {
                            IEnergyItem.setEnergy(curio, 0);
                            victim.level().playSound(null, victim.getX(), victim.getY(), victim.getZ(), CHSounds.DISCHARGED.get(), victim.getSoundSource(), 2.0F, 1.0F);
                            EnergyBarrierGenerator.setDischarged(curio, true);
                        }
                    }
                }
            }
            if (directEntity instanceof LivingEntity livingAttacker) {
                if (CHDamageSource.physicalAttacks(event.getSource())) {
                    if (victim.hasEffect(CHEffects.ENLIGHTENED)) {
                        ItemStack itemStack = CHCuriosFinder.findCurio(livingAttacker, CHItems.X_RAY_GOGGLES.get());
                        if (!itemStack.isEmpty() && XRayGoggles.isActivated(itemStack)) {
                            MobEffectInstance instance = victim.getEffect(CHEffects.ENLIGHTENED);
                            if (instance != null) {
                                float amp = instance.getAmplifier() + 1;
                                amp *= 1.3F;
                                event.setAmount(event.getAmount() * amp);
                            }
                        }
                    }
                    if (livingAttacker.getMainHandItem().getItem() instanceof BlazeBurnerItem) {
                        if (!livingAttacker.fireImmune() && !livingAttacker.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                            victim.setRemainingFireTicks(120);
                            ItemHelper.hurtAndBreak(livingAttacker.getMainHandItem(), 1, livingAttacker);
                        }
                    }
                    if (livingAttacker.getMainHandItem().getItem() instanceof TieredItem weapon) {
                        if (weapon.getTier() == CHTiers.EXTRATERRESTRIAL) {
                            victim.addEffect(new MobEffectInstance(CHEffects.COSMIC_IRRADIATION, 400));
                            if (victim.getType().is(CHTags.EntityTypes.TECHNO_FLESH)) {
                                event.setAmount(event.getAmount() + 4.0F);
                            }
                        }
                    }
                    if (livingAttacker.getMainHandItem().getItem() instanceof ChainswordItem) {
                        if (victim.getType().is(CHTags.EntityTypes.TECHNO_FLESH)) {
                            event.setAmount(event.getAmount() + 7.0F);
                        }
                    }
                }
            }
            if (victim.hasEffect(CHEffects.DEEP_BURN)) {
                MobEffectInstance mobEffectInstance = victim.getEffect(CHEffects.DEEP_BURN);
                if (mobEffectInstance != null){
                    if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                        event.setAmount(event.getAmount() * 1.6F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Pre event) {
        LivingEntity victim = event.getEntity();
        Entity directEntity = event.getSource().getDirectEntity();
        if (event.getNewDamage() > 0.0F) {
            if (!victim.level().isClientSide) {
                Player bloodEnergy = null;
                if (victim instanceof Player player) {
                    if (CHCuriosFinder.hasCurio(player, CHItems.BLOODY_BATTERY.get())) {
                        ItemStack curio = CHCuriosFinder.findCurio(player, CHItems.BLOODY_BATTERY.get());
                        if (!curio.isEmpty()) {
                            float amount = event.getNewDamage() * 1.15F;
                            amount = Math.min(amount, player.getHealth());
                            bloodEnergy = player;
                            EnergyUtil.chargeAllItems(player, Mth.floor(amount));
                            event.setNewDamage(amount);
                        }
                    }
                } else if (directEntity instanceof Player player) {
                    if (CHCuriosFinder.hasCurio(player, CHItems.BLOODY_BATTERY.get())) {
                        ItemStack curio = CHCuriosFinder.findCurio(player, CHItems.BLOODY_BATTERY.get());
                        if (!curio.isEmpty()) {
                            bloodEnergy = player;
                            EnergyUtil.chargeAllItems(player, Mth.floor(event.getNewDamage()));
                        }
                    }
                }
                if (bloodEnergy != null) {
                    if (bloodEnergy.level() instanceof ServerLevel serverLevel) {
                        for (int i = 0; i < 4; ++i) {
                            serverLevel.sendParticles(CHParticleTypes.BLOODY_ENERGY.get(), bloodEnergy.getRandomX(0.5F), bloodEnergy.getRandomY(), bloodEnergy.getRandomZ(0.5F), 1, 0, 0, 0, 0.0F);
                        }
                    }
                }
                if (victim instanceof Prowler prowler) {
                    if (event.getSource().getEntity() != null) {
                        float amount = event.getNewDamage();
                        if (prowler.retreatTick <= 0) {
                            prowler.accumulatedDamage += amount;
                            if (prowler.accumulatedDamage >= (prowler.getMaxHealth() * 0.2F)) {
                                prowler.accumulatedDamage -= (prowler.getMaxHealth() * 0.2F);
                                if (amount >= (prowler.getMaxHealth() * 0.2F)) {
                                    prowler.retaliateTick = MathHelper.secondsToTicks(1);
                                }
                                prowler.retreatTick = MathHelper.secondsToTicks(5);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSpawn(FinalizeSpawnEvent event) {
        if (event.getEntity().getType().is(CHTags.EntityTypes.TECHNO_FLESH) && CHConfig.TechnoFleshBuff.get()) {
            MobUtil.buffTechnoFlesh(event.getLevel().getLevel(), event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Entity directEntity = event.getSource().getDirectEntity();
        if (CHDamageSource.physicalAttacks(event.getSource())) {
            if (directEntity instanceof LivingEntity livingAttacker) {
                if (CHDamageSource.physicalAttacks(event.getSource())) {
                    if (com.mongoose.clanginghowl.common.enchantments.CHEnchantments.level(livingAttacker.getMainHandItem(), CHEnchantments.KILLER_CHARGE) > 0) {
                        for (LivingEntity livingEntity : livingAttacker.level().getEntitiesOfClass(LivingEntity.class, victim.getBoundingBox().inflate(4.0D))) {
                            if (!MobUtil.areAllies(livingAttacker, livingEntity) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                                if (livingEntity.hurt(CHDamageSource.lightning(livingAttacker, livingAttacker), 7.0F)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
                                }
                            }
                        }
                        livingAttacker.level().playSound(null, victim.getX(), victim.getY(), victim.getZ(), CHSounds.ELECTRIC_SHOCK.get(), livingAttacker.getSoundSource(), 1.0F, 1.0F);
                        if (livingAttacker.level() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(new ElectricShockParticleOption(2.0F, 0), victim.getX(), victim.getY() + 2.0D, victim.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                            serverLevel.sendParticles(new ElectricSplashParticleOption(4.0F, 0), victim.getX(), victim.getY() + 0.5D, victim.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                        if (livingAttacker instanceof Player player) {
                            EnergyUtil.chargeAllItems(player, 40);
                        }
                    }
                }
            }
        }
        if (victim.hasEffect(CHEffects.INTERNAL_HEAT)) {
            victim.level().playSound(null, victim.getX(), victim.getY(), victim.getZ(), SoundEvents.GENERIC_EXPLODE, victim.getSoundSource(), 1.0F, 1.0F);
            if (victim.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(new SmallFireSplashParticleOption(4.0F, 0), victim.getX(), victim.getY() + 1.0D, victim.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                serverLevel.sendParticles(new FieryExplosionParticleOption(4.0F, 0), victim.getX(), victim.getY() + 1.0D, victim.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
            for (LivingEntity livingEntity : victim.level().getEntitiesOfClass(LivingEntity.class, victim.getBoundingBox().inflate(4.0D))) {
                if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)
                        && !(livingEntity instanceof Player)
                        && !(livingEntity instanceof OwnableEntity ownable
                        && ownable.getOwner() instanceof Player)) {
                    if (livingEntity.hurt(victim.level().damageSources().inFire(), 5.0F)){
                        livingEntity.addEffect(new MobEffectInstance(CHEffects.INTERNAL_HEAT, 500));
                        livingEntity.igniteForSeconds(15);
                    }
                }
            }
        }
        if (victim.level() instanceof ServerLevel serverLevel) {
            if (victim instanceof Player player) {
                if (CHCuriosFinder.hasCurio(player, CHItems.REANIMATOR.get())) {
                    ItemStack reanimator = CHCuriosFinder.findCurio(player, CHItems.REANIMATOR.get());
                    if (!reanimator.isEmpty() && IEnergyItem.isFull(reanimator) && !player.getCooldowns().isOnCooldown(CHItems.REANIMATOR.get())) {
                        player.setHealth(player.getMaxHealth() / 2);
                        player.removeAllEffects();
                        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
                        for (LivingEntity target : player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(4.0D))) {
                            if (!MobUtil.areAllies(player, target) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
                                if (target.hurt(CHDamageSource.lightning(player, player), 10.0F)){
                                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
                                }
                            }
                        }
                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), CHSounds.ELECTRIC_SHOCK.get(), player.getSoundSource(), 1.0F, 1.0F);
                        serverLevel.sendParticles(new ElectricShockParticleOption(2.0F, 0), player.getX(), player.getY() + 2.0D, player.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        serverLevel.sendParticles(new ElectricSplashParticleOption(4.0F, 0), player.getX(), player.getY() + 0.5D, player.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        player.getCooldowns().addCooldown(CHItems.REANIMATOR.get(), 500);
                        CHNetwork.sendTo(player, new SReanimatorDeathPacket());
                        IEnergyItem.decreaseEnergy(reanimator, 1000);
                        event.setCanceled(true);
                    }
                }
            }
            if (victim.hasEffect(CHEffects.BEYOND_FLESH) && !victim.isOnFire()) {
                serverLevel.sendParticles(new BloodSplashParticleOption(1.0F, 0), victim.getX(), victim.getY() + 1.0D, victim.getZ(), 1, 0, 0, 0, 0);
                for (int i = 0; i < serverLevel.getRandom().nextIntBetweenInclusive(4, 6); ++i) {
                    serverLevel.sendParticles(CHParticleTypes.BLOOD_STAIN.get(), victim.getRandomX(0.5D), victim.getY() + 0.1F, victim.getRandomZ(0.5D), 1, 0, 0, 0, 1);
                }
                HeartOfDecay heartOfDecay = new HeartOfDecay(CHEntityType.HEART_OF_DECAY.get(), victim.level());
                heartOfDecay.setPos(victim.position().add(0.0D, 1.0D, 0.0D));
                EventHooks.finalizeMobSpawn(heartOfDecay, serverLevel, serverLevel.getCurrentDifficultyAt(victim.blockPosition()), MobSpawnType.TRIGGERED, null);
                heartOfDecay.playSound(CHSounds.FLESH_TEAR.get(), 1.0F, 1.0F);
                serverLevel.addFreshEntity(heartOfDecay);
            }
            if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                for (LivingEntity livingEntity : victim.level().getEntitiesOfClass(LivingEntity.class, victim.getBoundingBox().inflate(4.0D), living -> living.isHolding(CHItems.FLAMETHROWER.get()))) {
                    ItemStack itemStack = livingEntity.getMainHandItem();
                    if (!itemStack.is(CHItems.FLAMETHROWER.get())) {
                        itemStack = livingEntity.getOffhandItem();
                    }
                    if (itemStack.is(CHItems.FLAMETHROWER.get())) {
                        if (com.mongoose.clanginghowl.common.enchantments.CHEnchantments.level(itemStack, CHEnchantments.SOUL_BURNER) > 0) {
                            IFuel.fillUpItem(itemStack, 10);
                        }
                    }
                }
            }
        }
        if (!event.isCanceled()){
            CHCapHelper.setShakeTime(victim, 0);
        }
    }

    @SubscribeEvent
    public static void onCritical(CriticalHitEvent event) {
        if (event.isCriticalHit()) {
            Player player = event.getEntity();
            if (event.getTarget() instanceof LivingEntity target) {
                ItemStack itemStack = event.getEntity().getMainHandItem();
                if (itemStack.getItem() instanceof ChainswordItem && !IEnergyItem.isEmpty(itemStack)) {
                    if (!target.hasEffect(CHEffects.SAWING_UP_HEALTH)) {
                        target.addEffect(new MobEffectInstance(CHEffects.SAWING_UP_HEALTH, 500));
                    } else {
                        int maxAmp = 4;
                        if (com.mongoose.clanginghowl.common.enchantments.CHEnchantments.level(itemStack, CHEnchantments.EXCEEDING_THE_LIMIT) > 0) {
                            maxAmp = 9;
                        }
                        EffectsUtil.amplifyEffect(target, CHEffects.SAWING_UP_HEALTH, 500, maxAmp);
                    }
                }
                if (CHCuriosFinder.hasCurio(player, CHItems.ENERGY_GLOVE.get())) {
                    ItemStack itemStack1 = CHCuriosFinder.findCurio(player, CHItems.ENERGY_GLOVE.get());
                    if (!itemStack1.isEmpty() && !IEnergyItem.isEmpty(itemStack1) && !player.getCooldowns().isOnCooldown(CHItems.ENERGY_GLOVE.get())) {
                        for (LivingEntity livingEntity : player.level().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(4.0D))) {
                            if (!MobUtil.areAllies(player, livingEntity) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                                if (livingEntity.hurt(CHDamageSource.lightning(player, player), 10.0F)){
                                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
                                }
                            }
                        }
                        player.level().playSound(null, target.getX(), target.getY(), target.getZ(), CHSounds.ELECTRIC_SHOCK.get(), player.getSoundSource(), 1.0F, 1.0F);
                        if (player.level() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(new ElectricShockParticleOption(2.0F, 0), target.getX(), target.getY() + 2.0D, target.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                            serverLevel.sendParticles(new ElectricSplashParticleOption(4.0F, 0), target.getX(), target.getY() + 0.5D, target.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                        if (!player.level().isClientSide) {
                            EnergyUtil.chargeAllItems(player, 20, itemStack2 -> itemStack2.is(CHItems.ENERGY_GLOVE.get()));
                            IEnergyItem.decreaseEnergy(itemStack1, 80);
                            player.getCooldowns().addCooldown(CHItems.ENERGY_GLOVE.get(), 100);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        if (event.getEntity().hasEffect(CHEffects.SAWING_UP_HEALTH)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event){
        Player player = event.getEntity();
        Level world = player.level();
        if (!world.isClientSide) {
            if (!player.isUsingItem() || !(player.getUseItem().getItem() instanceof EnergyItem)) {
                EnergyItem.resetMiningProgress(world, player);
            }
        }
    }

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel() instanceof ServerLevel) {
            if (event.getTarget() instanceof Mob mob && mob.isAlive() && !(mob instanceof Enemy)) {
                if (mob.hasEffect(CHEffects.BEYOND_FLESH)) {
                    MobEffectInstance instance = mob.getEffect(CHEffects.BEYOND_FLESH);
                    if (instance != null) {
                        int duration = instance.getDuration();
                        if (duration > 100) {
                            mob.removeEffect(CHEffects.BEYOND_FLESH);
                            mob.addEffect(new MobEffectInstance(CHEffects.BEYOND_FLESH, 99, 0, false, false));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLightningStrike(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack itemStack = player.getInventory().getItem(i);
                if (itemStack.getItem() instanceof IEnergyItem) {
                    if (com.mongoose.clanginghowl.common.enchantments.CHEnchantments.level(itemStack, CHEnchantments.ECOLOGICAL_ENERGY) > 0) {
                        IEnergyItem.powerItem(itemStack, 400);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void FurnaceBurnItems(FurnaceFuelBurnTimeEvent event){
        if (!event.getItemStack().isEmpty()){
            ItemStack itemStack = event.getItemStack();
            if (itemStack.is(CHItems.BLAZE_FUEL.get())) {
                event.setBurnTime(3200);
            }
        }
    }

    @SubscribeEvent
    public static void PotionApplyEvents(MobEffectEvent.Applicable event) {
        LivingEntity livingEntity = event.getEntity();
        MobEffectInstance instance = event.getEffectInstance();
        if (instance.getEffect() == CHEffects.OVERDRIVE) {
            if (livingEntity.level() instanceof ServerLevel serverLevel) {
                if (livingEntity.isAlive() && !livingEntity.hasEffect(CHEffects.OVERDRIVE)) {
                    serverLevel.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), CHSounds.CHAINSAW_OVERDRIVE.get(), livingEntity.getSoundSource(), 1.0F, 1.0F);
                }
            }
        }
        if (instance.getEffect() == CHEffects.BEYOND_FLESH) {
            if (livingEntity.level() instanceof ServerLevel serverLevel) {
                if (livingEntity.isAlive() && !livingEntity.hasEffect(CHEffects.BEYOND_FLESH)) {
                    for(int i = 0; i < 20; ++i) {
                        double d0 = serverLevel.getRandom().nextGaussian() * 0.02D;
                        double d1 = serverLevel.getRandom().nextGaussian() * 0.02D;
                        double d2 = serverLevel.getRandom().nextGaussian() * 0.02D;
                        serverLevel.sendParticles(CHParticleTypes.CRIMSON_POOF.get(), livingEntity.getRandomX(1.0D), livingEntity.getRandomY(), livingEntity.getRandomZ(1.0D), 0, d0, d1, d2, 1.0D);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void SleepEvents(CanPlayerSleepEvent event){
        if (event.getEntity() != null) {
            if (!event.getEntity().level().isClientSide) {
                if (event.getEntity().level() instanceof ICHWorldData data) {
                    if (data.getCHWorldData().isMeteorShower()) {
                        event.getEntity().displayClientMessage(Component.translatable("info.clanginghowl.bed.meteor_shower"), true);
                        event.setProblem(Player.BedSleepingProblem.OTHER_PROBLEM);
                    }
                }
            }
        }
    }
}

package com.mongoose.clanginghowl.common.events;

import com.google.common.collect.ImmutableList;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.client.particles.ElectricShockParticleOption;
import com.mongoose.clanginghowl.client.particles.ElectricSplashParticleOption;
import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.common.capabilities.CHCapProvider;
import com.mongoose.clanginghowl.common.capabilities.ICHCap;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import com.mongoose.clanginghowl.common.items.BlazeBurnerItem;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.CHTiers;
import com.mongoose.clanginghowl.common.items.energy.BatteryItem;
import com.mongoose.clanginghowl.common.items.energy.ChainswordItem;
import com.mongoose.clanginghowl.common.items.energy.EnergyItem;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.CHDamageSource;
import com.mongoose.clanginghowl.utils.EffectsUtil;
import com.mongoose.clanginghowl.utils.MobUtil;
import com.mongoose.clanginghowl.utils.NoKnockBackDamageSource;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = ClangingHowl.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CHEvents {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        Player original = event.getOriginal();

        original.reviveCaps();

        ICHCap capability3 = CHCapHelper.getCapability(original);
        player.getCapability(CHCapProvider.CAPABILITY)
                .ifPresent(cap ->
                        cap.setMiningProgress(0));
        player.getCapability(CHCapProvider.CAPABILITY)
                .ifPresent(cap ->
                        cap.setMiningPos(null));
    }

    @SubscribeEvent
    public static void TickEvent(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity.hasEffect(CHEffects.OVERDRIVE.get())) {
            if (MobUtil.isMoving(livingEntity)) {
                livingEntity.level().addParticle(CHParticleTypes.OVERDRIVE_FIRE.get(), livingEntity.getX(), livingEntity.getY() + 0.25F, livingEntity.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @SubscribeEvent
    public static void AttackEvent(LivingAttackEvent event){
        LivingEntity victim = event.getEntity();
        Entity source = event.getSource().getEntity();
        Entity direct = event.getSource().getDirectEntity();
        if (event.getSource() instanceof NoKnockBackDamageSource damageSource){
            if (damageSource.getOwner() != null) {
                if (damageSource.getOwner() instanceof LivingEntity && !damageSource.is(DamageTypeTags.NO_ANGER)) {
                    victim.setLastHurtByMob((LivingEntity) damageSource.getOwner());
                }
                if (damageSource.getOwner() instanceof Player player) {
                    victim.lastHurtByPlayer = player;
                    victim.lastHurtByPlayerTime = 100;
                }
                if (damageSource.getOwner() instanceof ServerPlayer) {
                    CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer) damageSource.getOwner(), victim, event.getSource(), event.getAmount(), event.getAmount(), false);
                }
                if (damageSource.getOwner() instanceof OwnableEntity owned){
                    if (owned.getOwner() instanceof Player player) {
                        victim.lastHurtByPlayer = player;
                        victim.lastHurtByPlayerTime = 100;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvent(LivingHurtEvent event){
        LivingEntity victim = event.getEntity();
        Entity directEntity = event.getSource().getDirectEntity();
        if (event.getAmount() > 0.0F) {
            if (directEntity instanceof LivingEntity livingAttacker) {
                if (CHDamageSource.physicalAttacks(event.getSource())) {
                    if (livingAttacker.getMainHandItem().getItem() instanceof BlazeBurnerItem) {
                        if (!livingAttacker.fireImmune() && !livingAttacker.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                            victim.setRemainingFireTicks(120);
                        }
                    }
                    if (livingAttacker.getMainHandItem().getItem() instanceof TieredItem weapon) {
                        if (weapon.getTier() == CHTiers.EXTRATERRESTRIAL) {
                            victim.addEffect(new MobEffectInstance(CHEffects.COSMIC_IRRADIATION.get(), 400));
                        }
                    }
                }
            }
            if (victim.hasEffect(CHEffects.DEEP_BURN.get())) {
                MobEffectInstance mobEffectInstance = victim.getEffect(CHEffects.DEEP_BURN.get());
                if (mobEffectInstance != null){
                    if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                        event.setAmount(event.getAmount() * 1.6F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Entity directEntity = event.getSource().getDirectEntity();
        if (CHDamageSource.physicalAttacks(event.getSource())) {
            if (directEntity instanceof LivingEntity livingAttacker) {
                if (CHDamageSource.physicalAttacks(event.getSource())) {
                    if (livingAttacker.getMainHandItem().getEnchantmentLevel(CHEnchantments.KILLER_CHARGE.get()) > 0) {
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
                            if (!player.level().isClientSide) {
                                Inventory inventory = player.getInventory();
                                List<NonNullList<ItemStack>> compartments = ImmutableList.of(inventory.items, inventory.armor, inventory.offhand);
                                for (List<ItemStack> list : compartments) {
                                    for (ItemStack itemStack : list) {
                                        if (!itemStack.isEmpty()) {
                                            if (itemStack.getItem() instanceof IEnergyItem && !(itemStack.getItem() instanceof BatteryItem) && !IEnergyItem.isFull(itemStack)) {
                                                IEnergyItem.powerItem(itemStack, 40);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCritical(CriticalHitEvent event) {
        if (event.isVanillaCritical() || event.getResult() == Event.Result.ALLOW) {
            if (event.getTarget() instanceof LivingEntity target) {
                ItemStack itemStack = event.getEntity().getMainHandItem();
                if (itemStack.getItem() instanceof ChainswordItem && !IEnergyItem.isEmpty(itemStack)) {
                    if (!target.hasEffect(CHEffects.SAWING_UP_HEALTH.get())) {
                        target.addEffect(new MobEffectInstance(CHEffects.SAWING_UP_HEALTH.get(), 500));
                    } else {
                        EffectsUtil.amplifyEffect(target, CHEffects.SAWING_UP_HEALTH.get(), 500);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        if (event.getEntity().hasEffect(CHEffects.SAWING_UP_HEALTH.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        Player player = event.player;
        Level world = player.level();
        if (event.phase == TickEvent.Phase.END) {
            if (!player.isUsingItem() || !(player.getUseItem().getItem() instanceof EnergyItem)) {
                EnergyItem.resetMiningProgress(world, player);
            }
        }
    }

    @SubscribeEvent
    public static void onLightningStrike(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack itemStack = player.getInventory().getItem(i);
                if (itemStack.getItem() instanceof IEnergyItem) {
                    if (itemStack.getEnchantmentLevel(CHEnchantments.ECOLOGICAL_ENERGY.get()) > 0) {
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
        if (instance.getEffect() == CHEffects.OVERDRIVE.get()) {
            if (livingEntity.level() instanceof ServerLevel serverLevel) {
                if (livingEntity.isAlive() && !livingEntity.hasEffect(CHEffects.OVERDRIVE.get())) {
                    serverLevel.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), CHSounds.CHAINSAW_OVERDRIVE.get(), livingEntity.getSoundSource(), 1.0F, 1.0F);
                }
            }
        }
    }
}

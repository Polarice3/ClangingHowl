package com.mongoose.clanginghowl.common.items.fuel;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.common.effects.CHEffects;
import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.*;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FlamethrowerItem extends Item implements IFuel{

    public FlamethrowerItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public int getMaxFuel() {
        return 1600;
    }

    public ItemStack getPowerlessItem(){
        ItemStack itemStack = new ItemStack(this);
        IFuel.setFuel(itemStack, 0);
        IFuel.setMaxFuelAmount(itemStack, this.getMaxFuel());
        return itemStack;
    }

    public ItemStack getPoweredItem(){
        ItemStack itemStack = new ItemStack(this);
        IFuel.setFuel(itemStack, this.getMaxFuel());
        IFuel.setMaxFuelAmount(itemStack, this.getMaxFuel());
        return itemStack;
    }

    public int getBarColor(ItemStack stack) {
        float f = Math.max(0.0F, 1.0F - amountColor(stack));
        return Mth.color(1.0F, 0.88F * f, 0.045F);
    }

    public float amountColor(ItemStack stack){
        if (stack.getTag() != null) {
            int energy = stack.getTag().getInt(FUEL_AMOUNT);
            int maxEnergy = stack.getTag().getInt(MAX_FUEL_AMOUNT);
            return 1.0F - ((float) energy / maxEnergy);
        } else {
            return 1.0F;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getTag() != null && !IFuel.isFull(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack){
        if (stack.getTag() != null) {
            int energy = stack.getTag().getInt(FUEL_AMOUNT);
            int maxEnergy = stack.getTag().getInt(MAX_FUEL_AMOUNT);
            return Math.round((energy * 13.0F / maxEnergy));
        } else {
            return 0;
        }
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        IFuel.setFuel(pStack, 0);
        IFuel.setMaxFuelAmount(pStack, this.getMaxFuel());
        super.onCraftedBy(pStack, pLevel, pPlayer);
    }

    @Override
    public int getConsumption(ItemStack itemStack) {
        int increase = 0;
        if (itemStack.getEnchantmentLevel(CHEnchantments.NAPALM_STREAM.get()) > 0) {
            increase += itemStack.getEnchantmentLevel(CHEnchantments.NAPALM_STREAM.get());
        }
        if (this.isFuelBurst(itemStack)) {
            increase += 5;
        }
        return IFuel.super.getConsumption(itemStack) + increase;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        this.setTagTick(stack);
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public int getUseDuration(ItemStack p_40680_) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack p_40678_) {
        return UseAnim.CUSTOM;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 10;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public boolean isFuelBurst(ItemStack stack) {
        return stack.getEnchantmentLevel(CHEnchantments.FUEL_BURST.get()) > 0;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticks) {
        super.onUseTick(level, livingEntity, itemStack, ticks);
        int range = 6;
        if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
            if (ticks % 20 == 0) {
                this.consumeFuel(itemStack);
            }
        }
        if (this.noFuelInInventory(livingEntity, itemStack)) {
            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), CHSounds.DISCHARGED.get(), livingEntity.getSoundSource(), 0.4F, 1.0F);
            livingEntity.stopUsingItem();
        }
        if (!level.isClientSide) {
            float flameRange = range * ((float) Math.PI / 180.0F);
            for (int i = 0; i < 3; i++) {
                Vec3 cast = livingEntity.getLookAngle().normalize().xRot(level.getRandom().nextFloat() * flameRange * 2 - flameRange).yRot(level.getRandom().nextFloat() * flameRange * 2 - flameRange);
                HitResult hitResult = level.clip(new ClipContext(livingEntity.getEyePosition(), livingEntity.getEyePosition().add(cast.scale(10)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, livingEntity));
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    Vec3 pos = hitResult.getLocation().subtract(cast.scale(0.5D));
                    BlockPos blockPos = BlockPos.containing(pos.x, pos.y, pos.z);
                    if (level.getBlockState(blockPos).isAir() ||
                            (CHBlockUtil.canBeReplaced(level, blockPos)
                                    && level.getFluidState(blockPos).isEmpty()
                                    && Blocks.FIRE.defaultBlockState().canSurvive(level, blockPos))) {
                        level.setBlockAndUpdate(blockPos, BaseFireBlock.getState(level, blockPos));
                    }
                }
                for (Entity target : getBreathTarget(livingEntity, range)) {
                    if (target != null && !target.isUnderWater()) {
                        DamageSource damageSource = CHDamageSource.fireStream(livingEntity, livingEntity);
                        int enchantment = itemStack.getEnchantmentLevel(CHEnchantments.NAPALM_STREAM.get());
                        if (target.hurt(damageSource, 1.5F + enchantment)){
                            if (target instanceof LivingEntity livingEntity1) {
                                livingEntity1.addEffect(new MobEffectInstance(CHEffects.DEEP_BURN.get(), 500));
                            }
                            int fireSeconds = 10;
                            fireSeconds += enchantment * 5;
                            target.setSecondsOnFire(fireSeconds);
                            if (itemStack.getEnchantmentLevel(CHEnchantments.CHAIN_BURN.get()) > 0) {
                                if (target instanceof LivingEntity livingEntity1) {
                                    livingEntity1.addEffect(new MobEffectInstance(CHEffects.INTERNAL_HEAT.get(), 500));
                                }
                            }
                        }
                    }
                }
            }
        }
        this.dragonBreathAttack(CHParticleTypes.FLAMETHROWER_FLAME.get(), livingEntity, ((double) range / 10) * 0.5D);
    }

    public List<Entity> getBreathTarget(LivingEntity livingEntity, double range) {
        return MobUtil.getTargets(livingEntity.level(), livingEntity, range, 3.0D);
    }

    public List<Entity> getBreathTarget(LivingEntity livingEntity, double range, Predicate<Entity> predicate) {
        return MobUtil.getTargets(livingEntity.level(), livingEntity, range, 3.0D, predicate);
    }

    public void dragonBreathAttack(ParticleOptions particleOptions, LivingEntity entityLiving, double pVelocity){
        this.dragonBreathAttack(particleOptions, entityLiving, 10, pVelocity);
    }

    public void dragonBreathAttack(ParticleOptions particleOptions, LivingEntity entityLiving, int pParticleAmount, double pVelocity){
        Vec3 look = entityLiving.getLookAngle();

        double dist = 2.0D;
        double px = entityLiving.getX() + look.x * dist;
        double py = entityLiving.getEyeY() + look.y * dist;
        double pz = entityLiving.getZ() + look.z * dist;

        double velocity = pVelocity + entityLiving.getRandom().nextDouble() * pVelocity;
        for (int i = 0; i < pParticleAmount; i++) {
            double offset = 0.15D;
            double dx = entityLiving.getRandom().nextDouble() * 2.0D * offset - offset;
            double dy = entityLiving.getRandom().nextDouble() * 2.0D * offset - offset;
            double dz = entityLiving.getRandom().nextDouble() * 2.0D * offset - offset;

            double angle = 0.5D;
            Vec3 randomVec = new Vec3(entityLiving.getRandom().nextDouble() * 2.0D * angle - angle, entityLiving.getRandom().nextDouble() * 2.0D * angle - angle, entityLiving.getRandom().nextDouble() * 2.0D * angle - angle).normalize();
            Vec3 result = (look.normalize().scale(3.0D).add(randomVec)).normalize().scale(velocity);
            BlockPos blockPos = BlockPos.containing(px + dx, py + dy, pz + dz);
            if (entityLiving.level().isWaterAt(blockPos)) {
                particleOptions = ParticleTypes.BUBBLE;
            }
            if (entityLiving.level() instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(particleOptions, px + dx, (py + dy) - 0.2D, pz + dz, 0, result.x, result.y, result.z, 1.0F);
            } else {
                entityLiving.level().addAlwaysVisibleParticle(particleOptions, px + dx, (py + dy) - 0.2D, pz + dz, result.x, result.y, result.z);
            }
        }
    }

    public boolean noFuelInInventory(LivingEntity livingEntity, ItemStack itemStack) {
        if (IFuel.isEmpty(itemStack)) {
            if (livingEntity instanceof Player player) {
                Inventory inventory = player.getInventory();
                List<NonNullList<ItemStack>> compartments = ImmutableList.of(inventory.items, inventory.armor, inventory.offhand);
                for (List<ItemStack> list : compartments) {
                    for (ItemStack itemStack1 : list) {
                        if (!itemStack1.isEmpty()) {
                            if (itemStack1.is(CHItems.BLAZE_FUEL_CYLINDER.get())) {
                                itemStack1.shrink(1);
                                IFuel.fillUpItem(itemStack, this.getMaxFuel());
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return IFuel.isEmpty(itemStack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!this.noFuelInInventory(player, itemStack)) {
            if (this.isFuelBurst(itemStack)) {
                this.consumeFuel(itemStack);
                int range = 6;
                if (!level.isClientSide) {
                    for (Entity target : getBreathTarget(player, range, EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(entity -> !MobUtil.areAllies(entity, player)))) {
                        if (target != null && !target.isUnderWater()) {
                            DamageSource damageSource = CHDamageSource.fireStream(player, player);
                            int enchantment = itemStack.getEnchantmentLevel(CHEnchantments.NAPALM_STREAM.get());
                            if (target instanceof LivingEntity livingEntity1) {
                                if (target.hurt(damageSource, 1.5F + enchantment)) {
                                    livingEntity1.addEffect(new MobEffectInstance(CHEffects.DEEP_BURN.get(), 500));
                                    int fireSeconds = 10;
                                    fireSeconds += enchantment * 5;
                                    target.setSecondsOnFire(fireSeconds);
                                }
                            }
                            MobUtil.knockBack(target, player, 3.0D, 0.2D, 3.0D);
                        }
                    }
                }
                this.dragonBreathAttack(CHParticleTypes.FLAMETHROWER_BURST.get(), player, 30, 0.1F + ((double) range / 10));
                player.getCooldowns().addCooldown(this, 35);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), CHSounds.FLAMETHROWER_ACTIVATION.get(), player.getSoundSource(), 1.0F, 1.0F);
            } else {
                player.startUsingItem(hand);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), CHSounds.FLAMETHROWER_ACTIVATION.get(), player.getSoundSource(), 0.4F, 1.0F);
            }
            return InteractionResultHolder.consume(itemStack);
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), CHSounds.DISCHARGED.get(), player.getSoundSource(), 0.4F, 1.0F);
            player.displayClientMessage(Component.translatable("info.clanginghowl.fuel.empty"), true);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        this.addFuelText(stack, worldIn, tooltip, flagIn);
    }

    public void addInformationAfterShift(List<Component> tooltip) {
        tooltip.add(Component.translatable("info.clanginghowl.item.flamethrower.0"));
        tooltip.add(Component.translatable("info.clanginghowl.item.flamethrower.1"));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new FlameClient());
    }

    public static class FlameClient implements IClientItemExtensions{
        private static final HumanoidModel.ArmPose FLAME = HumanoidModel.ArmPose.create("CH_FLAME", false, (model, entity, arm) -> {
            if (arm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = -MathHelper.modelDegrees(55) + model.head.xRot;
                model.rightArm.yRot = -0.1F + model.head.yRot;
                model.leftArm.xRot = -MathHelper.modelDegrees(50) + model.head.xRot;
                model.leftArm.yRot = 0.1F + model.head.yRot + 0.4F;
                model.leftArm.zRot = MathHelper.modelDegrees(30);
            } else {
                model.leftArm.xRot = -MathHelper.modelDegrees(55) + model.head.xRot;
                model.leftArm.yRot = 0.1F + model.head.yRot;
                model.rightArm.xRot = -MathHelper.modelDegrees(50) + model.head.xRot;
                model.rightArm.yRot = -0.1F + model.head.yRot - 0.4F;
                model.rightArm.zRot = -MathHelper.modelDegrees(30);
            }
        });

        private static final HumanoidModel.ArmPose IDLE_FLAME = HumanoidModel.ArmPose.create("CH_IDLE_FLAME", false, (model, entity, arm) -> {
            if (arm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = -MathHelper.modelDegrees(45) + model.head.xRot;
                model.rightArm.yRot = -0.1F + model.head.yRot;
                model.rightArm.zRot = 0.0F;
                model.leftArm.xRot = -MathHelper.modelDegrees(45) + model.head.xRot;
                model.leftArm.yRot = 0.1F + model.head.yRot + 0.4F;
                model.leftArm.zRot = MathHelper.modelDegrees(30);
            } else {
                model.leftArm.xRot = -MathHelper.modelDegrees(45) + model.head.xRot;
                model.leftArm.yRot = 0.1F + model.head.yRot;
                model.leftArm.zRot = 0.0F;
                model.rightArm.xRot = -MathHelper.modelDegrees(45) + model.head.xRot;
                model.rightArm.yRot = -0.1F + model.head.yRot - 0.4F;
                model.rightArm.zRot = -MathHelper.modelDegrees(30);
            }
        });

        @Override
        public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof FlamethrowerItem) {
                if (entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0) {
                    return FLAME;
                }
                return IDLE_FLAME;
            }
            return HumanoidModel.ArmPose.EMPTY;
        }

        @Override
        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            if (player.isUsingItem()) {
                applyItemArmTransform(poseStack, arm, equipProcess);
                poseStack.translate(0.0F, 0.0F, 0.0F);
                poseStack.mulPose(Axis.XP.rotationDegrees(1.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees((float)i * 35.3F));
                poseStack.mulPose(Axis.ZP.rotationDegrees((float)i * -9.785F));
                float f8 = (float)itemInHand.getUseDuration() - ((float)player.getUseItemRemainingTicks() - partialTick + 1.0F);
                float f12 = f8 / 20.0F;
                f12 = (f12 * f12 + f12 * 2.0F) / 3.0F;
                if (f12 > 1.0F) {
                    f12 = 1.0F;
                }

                if (f12 > 0.1F) {
                    float f15 = Mth.sin((f8 - 0.1F) * 1.3F);
                    float f18 = f12 - 0.1F;
                    float f20 = f15 * f18;
                    poseStack.translate(f20 * 0.0F, f20 * 0.004F, (double)(f20 * 0.0F));
                }

                poseStack.translate(f12 * 0.0F, f12 * 0.0F, (double)(f12 * 0.04F));
                poseStack.scale(1.0F, 1.0F, 1.0F);
                poseStack.mulPose(Axis.YN.rotationDegrees((float)i * 45.0F));
            } else {
                float f5 = -0.4F * Mth.sin(Mth.sqrt(swingProcess) * (float)Math.PI);
                float f6 = 0.2F * Mth.sin(Mth.sqrt(swingProcess) * ((float)Math.PI * 2F));
                float f10 = -0.2F * Mth.sin(swingProcess * (float)Math.PI);
                poseStack.translate((double)((float)i * f5), (double)f6, (double)f10);
                this.applyItemArmTransform(poseStack, arm, equipProcess);
                this.applyItemArmAttackTransform(poseStack, arm, swingProcess);
            }
            return true;
        }

        private void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float equipProcess) {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            poseStack.translate((double)((float)i * 0.56F), (double)(-0.52F + equipProcess * -0.6F), (double)-0.72F);
        }

        private void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm humanoidArm, float swingProcess) {
            int i = humanoidArm == HumanoidArm.RIGHT ? 1 : -1;
            float f = Mth.sin(swingProcess * swingProcess * (float)Math.PI);
            poseStack.mulPose(Axis.YP.rotationDegrees((float)i * (45.0F + f * -20.0F)));
            float f1 = Mth.sin(Mth.sqrt(swingProcess) * (float)Math.PI);
            poseStack.mulPose(Axis.ZP.rotationDegrees((float)i * f1 * -20.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(f1 * -80.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees((float)i * -45.0F));
        }
    }
}

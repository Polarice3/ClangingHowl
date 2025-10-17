package com.mongoose.clanginghowl.common.items.energy;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mongoose.clanginghowl.client.particles.CHParticleTypes;
import com.mongoose.clanginghowl.client.render.item.AdvancedHandDrillRenderer;
import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.common.enchantments.CHEnchantments;
import com.mongoose.clanginghowl.init.CHSounds;
import com.mongoose.clanginghowl.utils.CHBlockUtil;
import com.mongoose.clanginghowl.utils.ItemHelper;
import com.mongoose.clanginghowl.utils.MathHelper;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.level.BlockEvent;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class DrillItem extends EnergyItem implements GeoItem {
    private static final RawAnimation DRILLING = RawAnimation.begin().thenLoop("drilling");
    public AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final Multimap<Attribute, AttributeModifier> attributes;

    public DrillItem() {
        super(new Properties());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 5.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -(4.0F - 1.0F), AttributeModifier.Operation.ADDITION));
        this.attributes = builder.build();
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributes : super.getAttributeModifiers(equipmentSlot, itemStack);
    }

    @Override
    public int getMaxEnergy() {
        return 3000;
    }

    @Override
    public int getConsumption(ItemStack itemStack) {
        if (itemStack.getEnchantmentLevel(CHEnchantments.TUNNEL_DRILLER.get()) > 0) {
            return super.getConsumption(itemStack) + 5;
        }
        return super.getConsumption(itemStack);
    }

    public int getUseDuration(ItemStack p_40680_) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack p_40678_) {
        return UseAnim.CUSTOM;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.SILK_TOUCH
            || enchantment == Enchantments.BLOCK_FORTUNE
            || enchantment == Enchantments.BLOCK_EFFICIENCY) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticks) {
        super.onUseTick(level, livingEntity, itemStack, ticks);
        triggerAnim(livingEntity, GeoItem.getId(itemStack), "controller", "drilling");
        if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
            if (ticks % 20 == 0) {
                this.consumeEnergy(itemStack);
            }
        }
        if (ticks % 15 == 0) {
            double d0 = level.random.nextGaussian() * 0.02D;
            double d1 = level.random.nextGaussian() * 0.02D;
            double d2 = level.random.nextGaussian() * 0.02D;
            level.addParticle(CHParticleTypes.BREAKDOWN_SMOKE.get(), livingEntity.getRandomX(0.5D), livingEntity.getRandomY(), livingEntity.getRandomZ(0.5D), d0, d1, d2);
        }
        if (IEnergyItem.isEmpty(itemStack)) {
            level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), CHSounds.DISCHARGED.get(), livingEntity.getSoundSource(), 0.4F, 1.0F);
            livingEntity.stopUsingItem();
        }
        if (level instanceof ServerLevel serverLevel) {
            boolean hitting = false;
            if (ticks % 10 == 0) {
                double hitRange = 2.5D;
                Vec3 srcVec = livingEntity.getEyePosition();
                Vec3 lookVec = livingEntity.getViewVector(1.0F);
                Vec3 destVec = srcVec.add(lookVec.x() * hitRange, lookVec.y() * hitRange, lookVec.z() * hitRange);
                float scale = 1.0F;
                List<Entity> possibleList = level.getEntities(livingEntity, livingEntity.getBoundingBox().expandTowards(lookVec.x() * hitRange, lookVec.y() * hitRange, lookVec.z() * hitRange).inflate(scale, scale, scale));
                for (Entity entity : possibleList) {
                    if (entity instanceof LivingEntity) {
                        float borderSize = 0.5F;
                        AABB collisionBB = entity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
                        Optional<Vec3> interceptPos = collisionBB.clip(srcVec, destVec);
                        if (collisionBB.contains(srcVec)) {
                            hitting = true;
                        } else if (interceptPos.isPresent()) {
                            hitting = true;
                        }

                        if (hitting) {
                            float extraDamage = 0.0F;
                            if (entity instanceof Mob mob) {
                                extraDamage = EnchantmentHelper.getDamageBonus(itemStack, mob.getMobType());
                            }
                            if (entity.hurt(livingEntity.damageSources().mobAttack(livingEntity), 1.0F + extraDamage)) {
                                int j = EnchantmentHelper.getFireAspect(livingEntity);
                                if (j > 0 && !entity.isOnFire()) {
                                    entity.setSecondsOnFire(j * 4);
                                }
                            }
                        }
                    }
                }
            }
            double range = 2.5D;
            if (livingEntity.getAttribute(ForgeMod.BLOCK_REACH.get()) != null) {
                range = livingEntity.getAttributeValue(ForgeMod.BLOCK_REACH.get());
            }
            BlockHitResult blockHitResult = this.blockResult(serverLevel, livingEntity, range);
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = serverLevel.getBlockState(blockPos);
            if (!hitting) {
                float toolSpeed = this.getBreakSpeed(blockPos, livingEntity, EnchantmentHelper.getBlockEfficiency(livingEntity));
                if (livingEntity instanceof Player player) {
                    if (this.canMineBlock(serverLevel, player, blockPos, blockState)) {
                        if (toolSpeed > 0.0F && itemStack.getEnchantmentLevel(CHEnchantments.TUNNEL_DRILLER.get()) > 0) {
                            toolSpeed = Math.max(1.0F, toolSpeed - 2.0F);
                        }
                        if (!(blockState.is(BlockTags.MINEABLE_WITH_PICKAXE) || blockState.is(BlockTags.MINEABLE_WITH_SHOVEL))) {
                            toolSpeed = 1.0F;
                        }
                        float hardness = this.getDestroySpeed(blockState, toolSpeed, serverLevel, blockPos);
                        SoundType soundtype = blockState.getSoundType(serverLevel, blockPos, null);
                        BlockPos miningPos = CHCapHelper.getMiningPos(player);
                        if (miningPos != null) {
                            if (miningPos.getX() != blockPos.getX() || miningPos.getY() != blockPos.getY() || miningPos.getZ() != blockPos.getZ()) {
                                CHCapHelper.setMiningProgress(player, 0);
                            }
                        }
                        CHCapHelper.increaseMiningProgress(player);
                        CHCapHelper.setMiningPos(player, blockPos);
                        int i = CHCapHelper.getMiningProgress(player);
                        float progress = hardness * (float)(i + 1);
                        int j = (int) (progress * 10);
                        if (hardness >= 0.0F) {
                            destroyBlockProgress(serverLevel, player.getId(), blockPos, j);
                        }
                        if (CHCapHelper.getMiningProgress(player) % 4 == 0) {
                            serverLevel.playSound(null, blockPos, soundtype.getHitSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        }
                        if (hardness == 0.0F) {
                            progress = 1.0F;
                        }
                        if (progress >= 1.0F) {
                            ItemStack tempTool = new ItemStack(Items.DIAMOND_PICKAXE);
                            int silk = itemStack.getEnchantmentLevel(Enchantments.SILK_TOUCH);
                            int fortune = itemStack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);

                            if (silk > 0) {
                                tempTool.enchant(Enchantments.SILK_TOUCH, silk);
                            } else if (fortune > 0) {
                                tempTool.enchant(Enchantments.BLOCK_FORTUNE, fortune);
                            }
                            this.breakBlocks(serverLevel, blockState, blockPos, player, soundtype, silk, fortune, tempTool);
                            if (!player.isCrouching() && itemStack.getEnchantmentLevel(CHEnchantments.TUNNEL_DRILLER.get()) > 0) {
                                for (BlockPos blockPos1 : CHBlockUtil.multiBlockBreak(player, blockPos, 1, 1, 1)) {
                                    if (!CHBlockUtil.areSamePos(blockPos, blockPos1)) {
                                        BlockState blockState1 = level.getBlockState(blockPos1);
                                        if (blockState1.is(BlockTags.MINEABLE_WITH_PICKAXE) || blockState1.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                                            this.breakBlocks(serverLevel, blockState1, blockPos1, player, soundtype, 0, 0, tempTool);
                                        }
                                    }
                                }
                            }

                            CHCapHelper.setMiningProgress(player, 0);
                            CHCapHelper.setMiningPos(player, null);
                            destroyBlockProgress(serverLevel, player.getId(), blockPos, -1);
                        }
                    }
                }
            } else {
                if (livingEntity instanceof Player player) {
                    CHCapHelper.setMiningProgress(player, 0);
                    CHCapHelper.setMiningPos(player, null);
                    destroyBlockProgress(serverLevel, player.getId(), blockPos, -1);
                }
            }
        }
    }

    public void breakBlocks(ServerLevel serverLevel, BlockState blockState, BlockPos blockPos, Player player, SoundType soundtype, int silk, int fortune, ItemStack tempTool) {
        BlockEvent.BreakEvent breakEvent = this.fixForgeEventBreakBlock(blockState, player, serverLevel, blockPos, silk, fortune);
        MinecraftForge.EVENT_BUS.post(breakEvent);
        if (breakEvent.isCanceled()) {
            return;
        }

        if (TierSortingRegistry.isCorrectTierForDrops(Tiers.DIAMOND, blockState)) {
            List<ItemStack> drops = Block.getDrops(blockState, serverLevel, blockPos, null, player, tempTool);

            int exp = blockState.getExpDrop(serverLevel, serverLevel.random, blockPos, fortune, silk);
            for (ItemStack drop : drops) {
                if (drop != null) {
                    Block.popResource(serverLevel, blockPos, drop);
                }
            }
            if (exp > 0) {
                blockState.getBlock().popExperience(serverLevel, blockPos, exp);
            }
        }

        serverLevel.playSound(null, blockPos, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

        serverLevel.removeBlockEntity(blockPos);
        serverLevel.levelEvent(2001, blockPos, Block.getId(blockState));
        serverLevel.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());

        player.awardStat(Stats.BLOCK_MINED.get(blockState.getBlock()));
    }

    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        super.onStopUsing(stack, entity, count);
        stopTriggeredAnim(entity, GeoItem.getId(stack), "controller", "drilling");
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate).triggerableAnim("drilling", DRILLING));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtils.getCurrentTick();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        this.addEnergyText(stack, worldIn, tooltip, flagIn);
    }

    public void addInformationAfterShift(List<Component> tooltip) {
        tooltip.add(Component.translatable("info.clanginghowl.item.drill.0"));
        tooltip.add(Component.translatable("info.clanginghowl.item.drill.1"));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new DrillClient());
    }

    public static class DrillClient implements IClientItemExtensions{
        private AdvancedHandDrillRenderer renderer;

        private static final HumanoidModel.ArmPose DRILL = HumanoidModel.ArmPose.create("CH_DRILL", false, (model, entity, arm) -> {
            if (arm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = -MathHelper.modelDegrees(55);
                model.rightArm.yRot = -0.1F + model.head.yRot;
                model.leftArm.xRot = -MathHelper.modelDegrees(50);
                model.leftArm.yRot = 0.1F + model.head.yRot + 0.4F;
                model.leftArm.zRot = MathHelper.modelDegrees(30);
            } else {
                model.leftArm.xRot = -MathHelper.modelDegrees(55);
                model.leftArm.yRot = 0.1F + model.head.yRot;
                model.rightArm.xRot = -MathHelper.modelDegrees(50);
                model.rightArm.yRot = -0.1F + model.head.yRot - 0.4F;
                model.rightArm.zRot = -MathHelper.modelDegrees(30);
            }
        });

        private static final HumanoidModel.ArmPose IDLE_DRILL = HumanoidModel.ArmPose.create("CH_IDLE_DRILL", false, (model, entity, arm) -> {
            if (arm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = -MathHelper.modelDegrees(45);
                model.rightArm.yRot = -0.1F + model.head.yRot;
                model.rightArm.zRot = 0.0F;
                model.leftArm.xRot = -MathHelper.modelDegrees(45);
                model.leftArm.yRot = 0.1F + model.head.yRot + 0.4F;
                model.leftArm.zRot = MathHelper.modelDegrees(30);
            } else {
                model.leftArm.xRot = -MathHelper.modelDegrees(45);
                model.leftArm.yRot = 0.1F + model.head.yRot;
                model.leftArm.zRot = 0.0F;
                model.rightArm.xRot = -MathHelper.modelDegrees(45);
                model.rightArm.yRot = -0.1F + model.head.yRot - 0.4F;
                model.rightArm.zRot = -MathHelper.modelDegrees(30);
            }
        });

        @Override
        public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof DrillItem) {
                if (entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0) {
                    return DRILL;
                }
                return IDLE_DRILL;
            }
            return HumanoidModel.ArmPose.EMPTY;
        }

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            if (this.renderer == null) {
                this.renderer = new AdvancedHandDrillRenderer();
            }
            return this.renderer;
        }

        @Override
        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            if (player.isUsingItem()) {
                applyItemArmTransform(poseStack, arm, equipProcess);
                poseStack.translate((float)i * -0.1F, 0.0F, (double)0.15731531F);
                poseStack.mulPose(Axis.XP.rotationDegrees(-13.935F));
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
                poseStack.translate((float)i * f5, f6, (double)f10);
                this.applyItemArmTransform(poseStack, arm, equipProcess);
                this.applyItemArmAttackTransform(poseStack, arm, swingProcess);
            }
            return true;
        }

        private void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float equipProcess) {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            poseStack.translate((float)i * 0.56F, -0.52F + equipProcess * -0.6F, (double)-0.72F);
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

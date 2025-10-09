package com.mongoose.clanginghowl.common.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mongoose.clanginghowl.client.render.item.AdvancedHandDrillRenderer;
import com.mongoose.clanginghowl.common.capacities.CHCapHelper;
import com.mongoose.clanginghowl.utils.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class DrillItem extends Item implements IEnergyItem, GeoItem {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation DRILLING = RawAnimation.begin().thenLoop("drilling");
    public AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public DrillItem() {
        super(new Properties());
    }

    @Override
    public int getMaxEnergy() {
        return 3000;
    }

    public ItemStack getPowerlessDrill(){
        ItemStack itemStack = new ItemStack(this);
        IEnergyItem.setEnergy(itemStack, 0);
        IEnergyItem.setMaxEnergyAmount(itemStack, this.getMaxEnergy());
        return itemStack;
    }

    public ItemStack getPoweredDrill(){
        ItemStack itemStack = new ItemStack(this);
        IEnergyItem.setEnergy(itemStack, this.getMaxEnergy());
        IEnergyItem.setMaxEnergyAmount(itemStack, this.getMaxEnergy());
        return itemStack;
    }

    public int getBarColor(ItemStack stack) {
        float f = Math.max(0.0F, 1.0F - amountColor(stack));
        return Mth.color(1.0F, 0.88F * f, 0.045F);
    }


    public float amountColor(ItemStack stack){
        if (stack.getTag() != null) {
            int energy = stack.getTag().getInt(ENERGY_AMOUNT);
            int maxEnergy = stack.getTag().getInt(MAX_ENERGY_AMOUNT);
            return 1.0F - ((float) energy / maxEnergy);
        } else {
            return 1.0F;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getTag() != null;
    }

    @Override
    public int getBarWidth(ItemStack stack){
        if (stack.getTag() != null) {
            int energy = stack.getTag().getInt(ENERGY_AMOUNT);
            int maxEnergy = stack.getTag().getInt(MAX_ENERGY_AMOUNT);
            return Math.round((energy * 13.0F / maxEnergy));
        } else {
            return 0;
        }
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        IEnergyItem.setEnergy(pStack, 0);
        IEnergyItem.setMaxEnergyAmount(pStack, this.getMaxEnergy());
        super.onCraftedBy(pStack, pLevel, pPlayer);
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
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment.category == EnchantmentCategory.VANISHABLE
            || enchantment.category == EnchantmentCategory.DIGGER
            || enchantment.category == EnchantmentCategory.WEAPON) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticks) {
        super.onUseTick(level, livingEntity, itemStack, ticks);
        triggerAnim(livingEntity, GeoItem.getId(itemStack), "controller", "drilling");
        if (ticks % 20 == 0) {
            IEnergyItem.decreaseEnergy(itemStack, 3);
        }
        if (IEnergyItem.isEmpty(itemStack)) {
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
                            if (entity.hurt(livingEntity.damageSources().mobAttack(livingEntity), 3.0F + extraDamage)) {
                                int j = EnchantmentHelper.getFireAspect(livingEntity);
                                if (j > 0 && !entity.isOnFire()) {
                                    entity.setSecondsOnFire(j * 4);
                                }
                            }
                        }
                    }
                }
            }
            if (!hitting) {
                double range = 2.5D;
                if (livingEntity.getAttribute(ForgeMod.BLOCK_REACH.get()) != null) {
                    range = livingEntity.getAttributeValue(ForgeMod.BLOCK_REACH.get());
                }
                BlockHitResult blockHitResult = this.blockResult(serverLevel, livingEntity, range);
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = serverLevel.getBlockState(blockPos);
                float hardness = getHardness(blockPos, livingEntity, EnchantmentHelper.getBlockEfficiency(livingEntity));
                hardness = (float) Math.floor(hardness);
                if (hardness == 0) {
                    hardness = 1;
                }
                if (livingEntity instanceof Player player) {
                    if (canMineBlock(serverLevel, player, blockPos, blockState)) {
                        if (!TierSortingRegistry.isCorrectTierForDrops(Tiers.DIAMOND, blockState)) {
                            hardness = blockState.getDestroySpeed(serverLevel, blockPos) * 5;
                        } else if (!blockState.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                            hardness = blockState.getDestroySpeed(serverLevel, blockPos) * 5;
                        }

                        SoundType soundtype = blockState.getSoundType(serverLevel, blockPos, null);
                        BlockPos miningPos = CHCapHelper.getMiningPos(player);
                        if (miningPos != null) {
                            if (miningPos.getX() != blockPos.getX() || miningPos.getY() != blockPos.getY() || miningPos.getZ() != blockPos.getZ()) {
                                CHCapHelper.setMiningProgress(player, 0);
                            }
                        }
                        CHCapHelper.increaseMiningProgress(player);
                        CHCapHelper.setMiningPos(player, blockPos);
                        destroyBlockProgress(serverLevel, player.getId(), blockPos, (int) ((CHCapHelper.getMiningProgress(player) / hardness) * 10));
                        if (CHCapHelper.getMiningProgress(player) % 4 == 0) {
                            serverLevel.playSound(null, blockPos, soundtype.getHitSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        }
                        if (CHCapHelper.getMiningProgress(player) >= hardness) {
                            ItemStack tempTool = new ItemStack(Items.DIAMOND_PICKAXE);
                            int silk = itemStack.getEnchantmentLevel(Enchantments.SILK_TOUCH);
                            int fortune = itemStack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);

                            if (silk > 0) {
                                tempTool.enchant(Enchantments.SILK_TOUCH, silk);
                            } else if (fortune > 0) {
                                tempTool.enchant(Enchantments.BLOCK_FORTUNE, fortune);
                            }
                            BlockEvent.BreakEvent breakEvent = fixForgeEventBreakBlock(blockState, player, serverLevel, blockPos, silk, fortune);
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
                            CHCapHelper.setMiningProgress(player, 0);
                            CHCapHelper.setMiningPos(player, null);
                            destroyBlockProgress(serverLevel, player.getId(), blockPos, -1);
                        }
                    }
                }
            }
        }
    }

    public BlockHitResult blockResult(Level worldIn, LivingEntity caster, double range) {
        float f = caster.getXRot();
        float f1 = caster.getYRot();
        Vec3 vector3d = caster.getEyePosition(1.0F);
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3 vector3d1 = vector3d.add((double)f6 * range, (double)f5 * range, (double)f7 * range);
        return worldIn.clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, caster));
    }

    public static void resetMiningProgress(Level level, Player player){
        if (level instanceof ServerLevel serverLevel) {
            if (CHCapHelper.getMiningProgress(player) > 0) {
                CHCapHelper.setMiningProgress(player, 0);
            }
            if (CHCapHelper.getMiningPos(player) != null) {
                destroyBlockProgress(serverLevel, player.getId(), CHCapHelper.getMiningPos(player), -1);
                CHCapHelper.setMiningPos(player, null);
            }
        }
    }

    public static void destroyBlockProgress(ServerLevel serverLevel, int p_8612_, BlockPos p_8613_, int p_8614_) {
        for(ServerPlayer serverplayer : serverLevel.getServer().getPlayerList().getPlayers()) {
            if (serverplayer != null && serverplayer.level() == serverLevel) {
                double d0 = (double)p_8613_.getX() - serverplayer.getX();
                double d1 = (double)p_8613_.getY() - serverplayer.getY();
                double d2 = (double)p_8613_.getZ() - serverplayer.getZ();
                if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D) {
                    serverplayer.connection.send(new ClientboundBlockDestructionPacket(p_8612_, p_8613_, p_8614_));
                }
            }
        }

    }

    public static boolean canMineBlock(Level world, Player player, BlockPos pos, BlockState state) {
        if (!player.mayBuild() || !world.mayInteract(player, pos)) {
            return false;
        }

        return isValid(pos, world) && !MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, pos, state, player));
    }

    private static boolean isValid(BlockPos pos, Level world) {
        BlockState state = world.getBlockState(pos);

        if ((!state.getFluidState().isEmpty() && !state.hasProperty(BlockStateProperties.WATERLOGGED)) || world.isEmptyBlock(pos)) {
            return false;
        }

        if (state.getDestroySpeed(world, pos) < 0) {
            return false;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null) {
            return false;
        }

        return !(state.getBlock() instanceof DoorBlock);
    }

    private static float getHardness(BlockPos blockPos, LivingEntity player, int efficiency) {
        float hardness = 0;
        float toolSpeed = Tiers.GOLD.getSpeed();
        if (efficiency > 0) {
            toolSpeed += (efficiency * efficiency + 1);
        }

        if (MobEffectUtil.hasDigSpeed(player)) {
            toolSpeed *= 1.0F + (float)(MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
        }

        MobEffectInstance fatigue = player.getEffect(MobEffects.DIG_SLOWDOWN);
        if (fatigue != null) {
            float f1 = switch (fatigue.getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };

            toolSpeed *= f1;
        }

        Level world = player.level();
        BlockState state = world.getBlockState(blockPos);

        if (player instanceof Player player1) {
            toolSpeed = net.minecraftforge.event.ForgeEventFactory.getBreakSpeed(player1, state, toolSpeed, blockPos);
        }

        hardness += (state.getDestroySpeed(world, blockPos) * 30) / toolSpeed;

        return hardness;
    }

    private static BlockEvent.BreakEvent fixForgeEventBreakBlock(BlockState state, Player player, Level world, BlockPos pos, int silk, int fortune) {
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, player);
        if (state != null) {
            event.setExpToDrop(state.getExpDrop(world, world.random, pos, fortune, silk));
        }

        return event;
    }

    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        super.onStopUsing(stack, entity, count);
        stopTriggeredAnim(entity, GeoItem.getId(stack), "controller", "drilling");
    }

    public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        ItemStack itemstack = p_40673_.getItemInHand(p_40674_);
        if (!IEnergyItem.isEmpty(itemstack)) {
            p_40673_.startUsingItem(p_40674_);
            return InteractionResultHolder.consume(itemstack);
        } else {
            p_40673_.displayClientMessage(Component.translatable("info.clanginghowl.energy.empty"), true);
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null) {
            int energy = stack.getTag().getInt(ENERGY_AMOUNT);
            int maxEnergy = stack.getTag().getInt(MAX_ENERGY_AMOUNT);
            tooltip.add(Component.translatable("info.clanginghowl.energy.amount", energy, maxEnergy));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, state -> {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }).triggerableAnim("drilling", DRILLING));
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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new DrillClient());
    }

    public static class DrillClient implements IClientItemExtensions{
        private AdvancedHandDrillRenderer renderer;

        private static final HumanoidModel.ArmPose DRILL = HumanoidModel.ArmPose.create("GOETY_SPELL", false, (model, entity, arm) -> {
            float f5 = entity.walkAnimation.position(Minecraft.getInstance().getPartialTick());
            if (arm == HumanoidArm.RIGHT) {
                model.rightArm.xRot -= MathHelper.modelDegrees(45);
                model.rightArm.zRot = Mth.cos(f5 * 0.6662F) * 0.25F;
                model.leftArm.xRot += MathHelper.modelDegrees(25);
            } else {
                model.leftArm.xRot -= MathHelper.modelDegrees(45);
                model.leftArm.zRot = -Mth.cos(f5 * 0.6662F) * 0.25F;
                model.rightArm.xRot += MathHelper.modelDegrees(25);
            }
        });

        @Override
        public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof DrillItem) {
                if (entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0) {
                    return DRILL;
                }
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
                poseStack.translate((double)((float)i * -0.2785682F), (double)0.18344387F, (double)0.15731531F);
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
                    poseStack.translate((double)(f20 * 0.0F), (double)(f20 * 0.004F), (double)(f20 * 0.0F));
                }

                poseStack.translate((double)(f12 * 0.0F), (double)(f12 * 0.0F), (double)(f12 * 0.04F));
                poseStack.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);
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

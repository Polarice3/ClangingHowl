package com.mongoose.clanginghowl.common.items;

import com.mongoose.clanginghowl.common.capacities.CHCapHelper;
import com.mongoose.clanginghowl.init.CHSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class EnergyItem extends Item implements IEnergyItem {

    public EnergyItem(Properties p_41383_) {
        super(p_41383_);
    }

    public ItemStack getPowerlessItem(){
        ItemStack itemStack = new ItemStack(this);
        IEnergyItem.setEnergy(itemStack, 0);
        IEnergyItem.setMaxEnergyAmount(itemStack, this.getMaxEnergy());
        return itemStack;
    }

    public ItemStack getPoweredItem(){
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
        return stack.getTag() != null && !IEnergyItem.isFull(stack);
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

    public float getDestroySpeed(BlockState blockState, float toolSpeed, BlockGetter blockGetter, BlockPos blockPos) {
        float f = blockState.getDestroySpeed(blockGetter, blockPos);
        if (f <= 0.0F) {
            return 0.0F;
        } else {
            int i = TierSortingRegistry.isCorrectTierForDrops(Tiers.DIAMOND, blockState) ? 30 : 100;
            return toolSpeed / f / (float)i;
        }
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

    public boolean canMineBlock(Level world, Player player, BlockPos pos, BlockState state) {
        if (!player.mayBuild() || !world.mayInteract(player, pos)) {
            return false;
        }

        return this.isValid(pos, world) && !MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, pos, state, player));
    }

    public boolean isValid(BlockPos pos, Level world) {
        return !world.isEmptyBlock(pos);
    }

    public float getBreakSpeed(BlockPos blockPos, LivingEntity player, int efficiency) {
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

        return toolSpeed;
    }

    public BlockEvent.BreakEvent fixForgeEventBreakBlock(BlockState state, Player player, Level world, BlockPos pos, int silk, int fortune) {
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, player);
        if (state != null) {
            event.setExpToDrop(state.getExpDrop(world, world.random, pos, fortune, silk));
        }

        return event;
    }

    public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        ItemStack itemstack = p_40673_.getItemInHand(p_40674_);
        if (!IEnergyItem.isEmpty(itemstack)) {
            p_40673_.startUsingItem(p_40674_);
            return InteractionResultHolder.consume(itemstack);
        } else {
            p_40672_.playSound(null, p_40673_.getX(), p_40673_.getY(), p_40673_.getZ(), CHSounds.DISCHARGED.get(), p_40673_.getSoundSource(), 0.4F, 1.0F);
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
}

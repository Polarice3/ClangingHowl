package com.mongoose.clanginghowl.common.items.curios;

import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import com.mongoose.clanginghowl.utils.CHCuriosFinder;
import com.mongoose.clanginghowl.utils.ItemHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class TendonStrengthener extends CuriosEnergyItem {

    public TendonStrengthener() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public int getMaxEnergy() {
        return 2000;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (entityIn instanceof LivingEntity wearer && CHCuriosFinder.findCurio(wearer, this) == stack) {
            if (!IEnergyItem.isEmpty(stack)) {
                if (!worldIn.isClientSide) {
                    if (wearer.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                        wearer.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                    }
                }
                if (wearer.isSprinting()) {
                    if (wearer.tickCount % 20 == 0) {
                        IEnergyItem.decreaseEnergy(stack, 2);
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        this.addEnergyText(stack, tooltipContext, tooltip, flagIn);
    }

    public void addInformationAfterShift(List<Component> tooltip) {
        tooltip.add(Component.translatable("info.clanginghowl.item.tendon.0"));
        tooltip.add(Component.translatable("info.clanginghowl.item.tendon.1"));
        tooltip.add(Component.translatable("info.clanginghowl.item.tendon.2"));
        tooltip.add(Component.translatable("info.clanginghowl.item.tendon.3"));
        tooltip.add(Component.translatable("info.clanginghowl.item.tendon.4"));
    }
}

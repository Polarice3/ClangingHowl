package com.mongoose.clanginghowl.common.items.curios;

import com.mongoose.clanginghowl.utils.ItemHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class Reanimator extends CuriosEnergyItem {

    @Override
    public int getMaxEnergy() {
        return 1000;
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        this.addEnergyText(stack, tooltipContext, tooltip, flagIn);
    }

    public void addInformationAfterShift(List<Component> tooltip) {
        tooltip.add(Component.translatable("info.clanginghowl.item.reanimator.0"));
        tooltip.add(Component.translatable("info.clanginghowl.item.reanimator.1"));
        tooltip.add(Component.translatable("info.clanginghowl.item.reanimator.2"));
    }
}

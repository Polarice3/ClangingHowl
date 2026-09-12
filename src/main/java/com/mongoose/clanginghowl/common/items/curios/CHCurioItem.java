package com.mongoose.clanginghowl.common.items.curios;

import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.utils.ItemHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

public class CHCurioItem extends Item implements ICurioItem {

    public CHCurioItem() {
        this(new Properties().stacksTo(1));
    }

    public CHCurioItem(Properties properties){
        super(properties);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        if (stack.is(CHItems.BLOODY_BATTERY.get())) {
            ItemHelper.addOnShift(tooltip, () -> addBloodyBatteryInfo(tooltip));
        }
    }

    public void addBloodyBatteryInfo(List<Component> tooltip) {
        tooltip.add(Component.translatable("info.clanginghowl.item.battery.0"));
        tooltip.add(Component.translatable("info.clanginghowl.item.battery.1"));
        tooltip.add(Component.translatable("info.clanginghowl.item.battery.2"));
    }
}

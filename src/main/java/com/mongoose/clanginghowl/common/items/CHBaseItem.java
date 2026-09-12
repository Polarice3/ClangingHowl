package com.mongoose.clanginghowl.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class CHBaseItem extends Item {
    public CHBaseItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable net.minecraft.world.item.Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.is(CHItems.HEMATOMA_LUMP.get())){
            tooltip.add(Component.translatable("info.clanginghowl.item.hematoma_lump"));
        }
    }
}

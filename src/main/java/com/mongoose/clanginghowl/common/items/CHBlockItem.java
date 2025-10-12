package com.mongoose.clanginghowl.common.items;

import com.mongoose.clanginghowl.common.blocks.CrystalFormerBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class CHBlockItem extends BlockItem {
    public CHBlockItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getItem() instanceof CHBlockItem base){
            if (base.getBlock() instanceof CrystalFormerBlock){
                tooltip.add(Component.translatable("info.clanginghowl.block.former.0"));
                tooltip.add(Component.translatable("info.clanginghowl.block.former.1"));
                tooltip.add(Component.translatable("info.clanginghowl.block.former.2"));
            }
        }
    }
}

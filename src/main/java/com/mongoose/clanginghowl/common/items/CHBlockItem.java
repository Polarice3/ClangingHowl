package com.mongoose.clanginghowl.common.items;

import com.mongoose.clanginghowl.common.blocks.*;
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
    public void appendHoverText(@NotNull ItemStack stack, @Nullable net.minecraft.world.item.Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getItem() instanceof CHBlockItem base){
            if (base.getBlock() instanceof CrystalFormerBlock){
                tooltip.add(Component.translatable("info.clanginghowl.block.former.0"));
                tooltip.add(Component.translatable("info.clanginghowl.block.former.1"));
                tooltip.add(Component.translatable("info.clanginghowl.block.former.2"));
            }
            if (base.getBlock() instanceof ChargingStationBlock){
                tooltip.add(Component.translatable("info.clanginghowl.block.charger"));
            }
            if (base.getBlock() instanceof ExBarrierBlock){
                tooltip.add(Component.translatable("info.clanginghowl.block.barrier"));
            }
            if (base.getBlock() instanceof BrokenCrystalFormerBlock){
                tooltip.add(Component.translatable("info.clanginghowl.block.broken_former"));
            }
            if (base.getBlock() instanceof FlameSpewerBlock){
                tooltip.add(Component.translatable("info.clanginghowl.block.flame_spewer.1"));
                tooltip.add(Component.translatable("info.clanginghowl.block.flame_spewer.2"));
            }
            if (base.getBlock() instanceof MotionSensorBlock){
                tooltip.add(Component.translatable("info.clanginghowl.block.sensor"));
            }
            if (base.getBlock() instanceof ExActivityRadarBlock){
                tooltip.add(Component.translatable("info.clanginghowl.block.radar"));
            }
        }
    }
}

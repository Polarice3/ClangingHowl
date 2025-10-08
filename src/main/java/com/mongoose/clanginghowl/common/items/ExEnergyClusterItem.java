package com.mongoose.clanginghowl.common.items;

import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ExEnergyClusterItem extends BlockItem {
    public int age;

    public ExEnergyClusterItem(int age) {
        super(CHBlocks.EXTRATERRESTRIAL_ENERGY_CLUSTER.get(), new Item.Properties());
        this.age = age;
    }

    public @NotNull String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext p_40613_) {
        BlockState blockstate = this.getBlock().getStateForPlacement(p_40613_);
        if (blockstate != null) {
            if (blockstate.hasProperty(BlockStateProperties.AGE_3)) {
                blockstate = blockstate.setValue(BlockStateProperties.AGE_3, Math.min(this.age, 3));
            }
        }
        return blockstate != null && this.canPlace(p_40613_, blockstate) ? blockstate : null;
    }
}

package com.mongoose.clanginghowl.common.blocks.entities;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.blocks.CHBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CHBlockEntities {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ClangingHowl.MOD_ID);

    public static final RegistryObject<BlockEntityType<CrystalFormerBlockEntity>> CRYSTAL_FORMER = BLOCK_ENTITY.register("crystal_former",
            () -> BlockEntityType.Builder.of(CrystalFormerBlockEntity::new, CHBlocks.CRYSTAL_FORMER.get()).build(null));

}

package com.mongoose.clanginghowl.data;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.entities.CHEntityType;
import com.mongoose.clanginghowl.init.CHTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CHEntityTypeTagsProvider extends IntrinsicHolderTagsProvider<EntityType<?>> {

    public CHEntityTypeTagsProvider(PackOutput p_256095_, CompletableFuture<HolderLookup.Provider> p_256572_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_256095_, Registries.ENTITY_TYPE, p_256572_, (p_256665_) -> p_256665_.builtInRegistryHolder().key(), ClangingHowl.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_255894_) {
        this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(
                CHEntityType.SPIT_PROJECTILE.get());
        this.tag(CHTags.EntityTypes.TECHNO_FLESH).add(
                CHEntityType.HEART_OF_DECAY.get(),
                CHEntityType.EX_REAPER.get());
    }
}

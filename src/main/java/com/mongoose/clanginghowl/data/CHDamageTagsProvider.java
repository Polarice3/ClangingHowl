package com.mongoose.clanginghowl.data;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.utils.CHDamageSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CHDamageTagsProvider extends TagsProvider<DamageType> {

    public CHDamageTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.DAMAGE_TYPE, future, ClangingHowl.MOD_ID, helper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.DAMAGES_HELMET).add(CHDamageSource.ICICLE);
        this.tag(DamageTypeTags.BYPASSES_SHIELD).add(CHDamageSource.ICICLE);
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(CHDamageSource.NEUROTOXIN);
        this.tag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS).add(CHDamageSource.NEUROTOXIN);
        this.tag(DamageTypeTags.WITCH_RESISTANT_TO).add(CHDamageSource.NEUROTOXIN);
        this.tag(DamageTypeTags.IS_LIGHTNING).add(CHDamageSource.LIGHTNING);
        this.tag(DamageTypeTags.IS_FIRE).add(CHDamageSource.FIRE_STREAM);
    }
}

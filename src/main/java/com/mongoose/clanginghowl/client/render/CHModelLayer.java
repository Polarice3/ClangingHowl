package com.mongoose.clanginghowl.client.render;

import com.google.common.collect.Sets;
import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.client.model.geom.ModelLayerLocation;

import java.util.Set;

public class CHModelLayer {
    private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();

    public static final ModelLayerLocation SPIT = register("spit");

    public static final ModelLayerLocation HEART_OF_DECAY = register("heart_of_decay");
    public static final ModelLayerLocation EXTRATERRESTRIAL_REAPER = register("extraterrestrial_reaper");

    private static ModelLayerLocation register(String p_171294_) {
        return register(p_171294_, "main");
    }

    private static ModelLayerLocation register(String p_171296_, String p_171297_) {
        ModelLayerLocation modellayerlocation = createLocation(p_171296_, p_171297_);
        if (!ALL_MODELS.add(modellayerlocation)) {
            throw new IllegalStateException("Duplicate registration for " + modellayerlocation);
        } else {
            return modellayerlocation;
        }
    }

    private static ModelLayerLocation createLocation(String p_171301_, String p_171302_) {
        return new ModelLayerLocation(ClangingHowl.location(p_171301_), p_171302_);
    }

    private static ModelLayerLocation registerInnerArmor(String p_171299_) {
        return register(p_171299_, "inner_armor");
    }

    private static ModelLayerLocation registerOuterArmor(String p_171304_) {
        return register(p_171304_, "outer_armor");
    }
}

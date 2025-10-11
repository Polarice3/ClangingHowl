package com.mongoose.clanginghowl.client.render.model;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.items.energy.ChainsawItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AdvancedChainsawModel extends GeoModel<ChainsawItem> {
    @Override
    public ResourceLocation getModelResource(ChainsawItem item) {
        return ClangingHowl.location("geo/advanced_chainsaw.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ChainsawItem item) {
        return ClangingHowl.location("textures/item/advanced_chainsaw_model.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ChainsawItem item) {
        return ClangingHowl.location("animations/advanced_chainsaw.animation.json");
    }
}

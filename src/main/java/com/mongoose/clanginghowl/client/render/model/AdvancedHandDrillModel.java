package com.mongoose.clanginghowl.client.render.model;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.items.energy.DrillItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AdvancedHandDrillModel extends GeoModel<DrillItem> {
    @Override
    public ResourceLocation getModelResource(DrillItem drillItem) {
        return ClangingHowl.location("geo/advanced_hand_drill.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DrillItem drillItem) {
        return ClangingHowl.location("textures/item/advanced_hand_drill_model.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DrillItem drillItem) {
        return ClangingHowl.location("animations/advanced_hand_drill.animation.json");
    }
}

package com.mongoose.clanginghowl.client.render.item;

import com.mongoose.clanginghowl.client.render.model.AdvancedHandDrillModel;
import com.mongoose.clanginghowl.common.items.energy.DrillItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AdvancedHandDrillRenderer extends GeoItemRenderer<DrillItem> {
    public AdvancedHandDrillRenderer() {
        super(new AdvancedHandDrillModel());
    }
}

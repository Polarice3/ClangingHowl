package com.mongoose.clanginghowl.client.render.item;

import com.mongoose.clanginghowl.client.render.model.AdvancedChainsawModel;
import com.mongoose.clanginghowl.common.items.ChainsawItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AdvancedChainsawRenderer extends GeoItemRenderer<ChainsawItem> {
    public AdvancedChainsawRenderer() {
        super(new AdvancedChainsawModel());
    }
}

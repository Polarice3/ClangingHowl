package com.mongoose.clanginghowl.client.gui.screen.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.client.inventory.menu.PortableChargerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PortableChargerScreen extends AbstractContainerScreen<PortableChargerMenu> {
    private static final ResourceLocation GUI_TEXTURES = ClangingHowl.location("textures/gui/menu/portable_charger.png");

    public PortableChargerScreen(PortableChargerMenu p_i51097_1_, Inventory p_i51097_2_, Component p_i51097_3_) {
        super(p_i51097_1_, p_i51097_2_, p_i51097_3_);
    }

    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void render(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            int i = (this.width - this.imageWidth) / 2;
            int j = (this.height - this.imageHeight) / 2;
            matrixStack.blit(GUI_TEXTURES, i, j, 0, 0, this.imageWidth, this.imageHeight);
        }
    }

    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {
    }
}

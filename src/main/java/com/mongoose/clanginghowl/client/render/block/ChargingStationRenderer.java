package com.mongoose.clanginghowl.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mongoose.clanginghowl.common.blocks.entities.ChargingStationBlockEntity;
import com.mongoose.clanginghowl.common.items.energy.DrillItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ChargingStationRenderer implements BlockEntityRenderer<ChargingStationBlockEntity> {
    public ChargingStationRenderer(BlockEntityRendererProvider.Context p_i226007_1_) {
    }

    public void render(ChargingStationBlockEntity pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        pBlockEntity.itemStackHandler.ifPresent(handler -> {
            ItemStack stack = handler.getStackInSlot(0);
            Minecraft minecraft = Minecraft.getInstance();
            if (!stack.isEmpty()) {
                pMatrixStack.pushPose();
                pMatrixStack.translate(0.5F, 1.1F, 0.5F);
                if (stack.getItem() instanceof DrillItem) {
                    pMatrixStack.scale(0.45F, 0.45F, 0.45F);
                } else {
                    pMatrixStack.scale(0.75F, 0.75F, 0.75F);
                }
                if (minecraft.level != null){
                    pMatrixStack.mulPose(Axis.YP.rotationDegrees(3 * (minecraft.level.getGameTime() % 360 + pPartialTicks)));
                }
                minecraft.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, pCombinedLight, pCombinedOverlay, pMatrixStack, pBuffer, pBlockEntity.getLevel(), 0);
                pMatrixStack.popPose();
            }
        });
    }
}

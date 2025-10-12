package com.mongoose.clanginghowl.utils;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemHelper {

    public static <T extends LivingEntity> void hurtAndRemove(ItemStack stack, int pAmount, T pEntity) {
        if (!pEntity.level().isClientSide && (!(pEntity instanceof Player) || !((Player)pEntity).getAbilities().instabuild)) {
            if (stack.isDamageableItem()) {
                if (stack.hurt(pAmount, pEntity.getRandom(), pEntity instanceof ServerPlayer ? (ServerPlayer)pEntity : null)) {
                    stack.shrink(1);
                    stack.setDamageValue(0);
                }
            }
        }
    }

    public static <T extends LivingEntity> void hurtAndBreak(ItemStack itemStack, int pAmount, T pEntity) {
        itemStack.hurtAndBreak(pAmount, pEntity, (p_220045_0_) -> p_220045_0_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    }

    public static boolean armorSet(LivingEntity living, ArmorMaterial material){
        int i = 0;
        if (living.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem helmet && helmet.getMaterial() == material) {
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR) {
                    if (living.getItemBySlot(equipmentSlot).getItem() instanceof ArmorItem armorItem) {
                        if (armorItem.getMaterial() == material) {
                            ++i;
                        }
                    }
                }
            }
        }
        return i >= 4;
    }

    public static ItemEntity itemEntityDrop(LivingEntity livingEntity, ItemStack itemStack){
        return new ItemEntity(livingEntity.level(), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), itemStack);
    }

    public static void addItemEntity(Level level, BlockPos blockPos, ItemStack itemStack){
        double d0 = (double) (level.random.nextFloat() * 0.5F) + 0.25D;
        double d1 = (double) (level.random.nextFloat() * 0.5F) + 0.25D;
        double d2 = (double) (level.random.nextFloat() * 0.5F) + 0.25D;
        ItemEntity itementity = new ItemEntity(level, (double) blockPos.getX() + d0, (double) blockPos.getY() + d1, (double) blockPos.getZ() + d2, itemStack);
        itementity.setDefaultPickUpDelay();
        level.addFreshEntity(itementity);
    }

    //Stolen from @Vazkii: https://github.com/VazkiiMods/Botania/blob/1.20.x/Xplat/src/main/java/vazkii/botania/client/gui/TooltipHandler.java
    public static Component getShiftInfoTooltip() {
        return Component.translatable("info.clanginghowl.item.tooltip");
    }

    public static void addOnShift(List<Component> tooltip, Runnable lambda) {
        if (Screen.hasShiftDown()) {
            lambda.run();
        } else {
            tooltip.add(getShiftInfoTooltip());
        }
    }
}

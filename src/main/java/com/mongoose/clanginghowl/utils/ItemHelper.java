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
    public static net.minecraft.world.item.component.ItemAttributeModifiers mainHandAttributes(
            com.google.common.collect.Multimap<net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute>, net.minecraft.world.entity.ai.attributes.AttributeModifier> modifiers) {
        var builder = net.minecraft.world.item.component.ItemAttributeModifiers.builder();
        modifiers.forEach((attribute, modifier) -> builder.add(attribute, modifier, net.minecraft.world.entity.EquipmentSlotGroup.MAINHAND));
        return builder.build();
    }

    public static com.google.common.collect.Multimap<net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute>, net.minecraft.world.entity.ai.attributes.AttributeModifier> mainHandAttributes(ItemStack stack) {
        com.google.common.collect.Multimap<net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute>, net.minecraft.world.entity.ai.attributes.AttributeModifier> result = com.google.common.collect.HashMultimap.create();
        stack.getAttributeModifiers().forEach(EquipmentSlot.MAINHAND, result::put);
        return result;
    }


    public static <T extends LivingEntity> void hurtAndRemove(ItemStack stack, int pAmount, T pEntity) {
        if (!pEntity.level().isClientSide && (!(pEntity instanceof Player) || !((Player)pEntity).getAbilities().instabuild)) {
            if (stack.isDamageableItem()) {
                // This helper intentionally does not emit a hand/armor break animation.
                stack.hurtAndBreak(pAmount, (net.minecraft.server.level.ServerLevel) pEntity.level(), pEntity, brokenItem -> {});
            }
        }
    }

    public static <T extends LivingEntity> void hurtAndBreak(ItemStack itemStack, int pAmount, T pEntity) {
        itemStack.hurtAndBreak(pAmount, pEntity, EquipmentSlot.MAINHAND);
    }

    public static boolean armorSet(LivingEntity living, ArmorMaterial material){
        int i = 0;
        if (living.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorItem helmet && helmet.getMaterial().value() == material) {
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                if (equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                    if (living.getItemBySlot(equipmentSlot).getItem() instanceof ArmorItem armorItem) {
                        if (armorItem.getMaterial().value() == material) {
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

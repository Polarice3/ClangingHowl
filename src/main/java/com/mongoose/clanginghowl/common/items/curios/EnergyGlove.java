package com.mongoose.clanginghowl.common.items.curios;

import com.mongoose.clanginghowl.utils.CHItemData;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mongoose.clanginghowl.common.items.energy.IEnergyItem;
import com.mongoose.clanginghowl.utils.CHCuriosFinder;
import com.mongoose.clanginghowl.utils.CHUUIDUtil;
import com.mongoose.clanginghowl.utils.ItemHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EnergyGlove extends CuriosEnergyItem {
    private static final String DISCHARGED = "Discharged";
    private static final String DISCHARGING = "Discharging";

    public EnergyGlove() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public int getMaxEnergy() {
        return 1500;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (entityIn instanceof LivingEntity wearer && CHCuriosFinder.findCurio(wearer, this) == stack) {
            if (isDischarged(stack)) {
                if (getDischarging(stack) < 80) {
                    setDischarging(stack, getDischarging(stack) + 1);
                } else {
                    setDischarged(stack, false);
                }
            } else {
                if (getDischarging(stack) > 0) {
                    setDischarging(stack, 0);
                }
            }
        }
    }

    public static void setDischarged(ItemStack stack, boolean discharged){
        if (CHItemData.hasData(stack)) {
            CHItemData.putBoolean(stack, DISCHARGED, discharged);
        } else {

            CHItemData.putBoolean(stack, DISCHARGED, discharged);
        }
    }

    public static boolean isDischarged(ItemStack stack) {
        if (CHItemData.hasData(stack)) {
            return CHItemData.getBoolean(stack, DISCHARGED);
        } else {
            return false;
        }
    }

    public static void setDischarging(ItemStack stack, int discharging){
        if (CHItemData.hasData(stack)) {
            CHItemData.putInt(stack, DISCHARGING, discharging);
        } else {

            CHItemData.putInt(stack, DISCHARGING, discharging);
        }
    }

    public static int getDischarging(ItemStack stack) {
        if (CHItemData.hasData(stack)) {
            return CHItemData.getInt(stack, DISCHARGING);
        } else {
            return 0;
        }
    }

    @Override
    public Multimap<net.minecraft.core.Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext,
                                                                        net.minecraft.resources.ResourceLocation id, ItemStack stack) {
        Multimap<net.minecraft.core.Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
        map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(com.mongoose.clanginghowl.ClangingHowl.location("item.clanginghowl.energy_glove"), 1.0F, AttributeModifier.Operation.ADD_VALUE));
        if (IEnergyItem.isEmpty(stack)) {
            map = super.getAttributeModifiers(slotContext, id, stack);
        }
        return map;
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        ItemHelper.addOnShift(tooltip, () -> addInformationAfterShift(tooltip));
        this.addEnergyText(stack, tooltipContext, tooltip, flagIn);
    }

    public void addInformationAfterShift(List<Component> tooltip) {
        tooltip.add(Component.translatable("info.clanginghowl.item.glove.0"));
        tooltip.add(Component.translatable("info.clanginghowl.item.glove.1"));
        tooltip.add(Component.translatable("info.clanginghowl.item.glove.2"));
        tooltip.add(Component.translatable("info.clanginghowl.item.glove.3"));
    }
}

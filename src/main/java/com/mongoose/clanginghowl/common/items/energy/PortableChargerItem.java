package com.mongoose.clanginghowl.common.items.energy;

import com.google.common.collect.ImmutableList;
import com.mongoose.clanginghowl.client.inventory.menu.PortableChargerMenu;
import com.mongoose.clanginghowl.common.items.capabilities.PortableChargerCapability;
import com.mongoose.clanginghowl.common.items.handler.PortableChargerHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class PortableChargerItem extends Item {
    private static final String HAS_BATTERIES = "HasBatteries";

    public PortableChargerItem() {
        super(new Properties()
                .setNoRepair()
                .rarity(Rarity.RARE)
                .stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof Player player) {
            if (stack.getTag() == null) {
                stack.getOrCreateTag().putBoolean(HAS_BATTERIES, false);
            }
            PortableChargerHandler handler = PortableChargerHandler.get(stack);
            ItemStack battery = ItemStack.EMPTY;
            for (ItemStack itemStack : handler.getContents()) {
                if (itemStack.getItem() instanceof BatteryItem) {
                    if (!IEnergyItem.isEmpty(itemStack)) {
                        battery = itemStack;
                        break;
                    } else {
                        itemStack.shrink(1);
                    }
                }
            }
            if (!battery.isEmpty()) {
                if (!worldIn.isClientSide) {
                    Inventory inventory = player.getInventory();
                    List<NonNullList<ItemStack>> compartments = ImmutableList.of(inventory.items, inventory.armor, inventory.offhand);
                    for (List<ItemStack> list : compartments) {
                        for (ItemStack itemStack : list) {
                            if (!itemStack.isEmpty()) {
                                if (itemStack.getItem() instanceof IEnergyItem && !(itemStack.getItem() instanceof BatteryItem) && !IEnergyItem.isFull(itemStack)) {
                                    IEnergyItem.chargeEnergy(itemStack, battery);
                                }
                            }
                        }
                    }
                }
                if (stack.getTag() != null) {
                    stack.getTag().putBoolean(HAS_BATTERIES, true);
                }
            } else {
                if (stack.getTag() != null) {
                    stack.getTag().putBoolean(HAS_BATTERIES, false);
                }
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public static boolean hasBatteries(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().getBoolean(HAS_BATTERIES);
    }

    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (!worldIn.isClientSide) {
            SimpleMenuProvider provider = new SimpleMenuProvider(
                    (id, inventory, player) -> new PortableChargerMenu(id, inventory, PortableChargerHandler.get(itemstack), itemstack), getName(itemstack));
            NetworkHooks.openScreen((ServerPlayer) playerIn, provider, (buffer) -> {});
        }
        return InteractionResultHolder.success(itemstack);
    }

    /**
     * Found Creative Server Bug fix from @mraof's Minestuck Music Player Weapon code.
     */
    private static IItemHandler getItemHandler(ItemStack itemStack) {
        return itemStack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(() ->
                new IllegalArgumentException("Expected an item handler for the Magic Focus item, but " + itemStack + " does not expose an item handler."));
    }

    public CompoundTag getShareTag(ItemStack stack) {
        IItemHandler iitemHandler = getItemHandler(stack);
        CompoundTag nbt = stack.getTag() != null ? stack.getTag() : new CompoundTag();
        if(iitemHandler instanceof ItemStackHandler itemHandler) {
            nbt.put("cap", itemHandler.serializeNBT());
        }
        return nbt;
    }

    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        if(nbt == null) {
            stack.setTag(null);
        } else {
            IItemHandler iitemHandler = getItemHandler(stack);
            if(iitemHandler instanceof ItemStackHandler itemHandler) {
                itemHandler.deserializeNBT(nbt.getCompound("cap"));
            }
            stack.setTag(nbt);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
    }

    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundTag nbt) {
        return new PortableChargerCapability(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("info.clanginghowl.item.charger.0"));
        tooltip.add(Component.translatable("info.clanginghowl.item.charger.1"));
    }
}

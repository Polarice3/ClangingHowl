package com.mongoose.clanginghowl.utils;

import com.mongoose.clanginghowl.common.capabilities.CHCapHelper;
import com.mongoose.clanginghowl.common.items.CHItems;
import com.mongoose.clanginghowl.common.items.curios.XRayGoggles;
import com.mongoose.clanginghowl.compat.curios.CuriosLoaded;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CHCuriosFinder {

    public static ItemStack findCurio(LivingEntity livingEntity, Predicate<ItemStack> filter){
        ItemStack foundStack = ItemStack.EMPTY;
        if (livingEntity instanceof Player) {
            if (CuriosLoaded.CURIOS.isLoaded()) {
                Optional<SlotResult> slotResult = CuriosApi.getCuriosInventory(livingEntity).map(inv -> inv.findFirstCurio(filter))
                        .orElse(Optional.empty());
                if (slotResult.isPresent()) {
                    foundStack = slotResult.get().stack();
                }
            }
        }

        return foundStack;
    }

    public static boolean hasCurio(LivingEntity livingEntity, Predicate<ItemStack> filter){
        return !findCurio(livingEntity, filter).isEmpty();
    }

    public static boolean hasCurio(LivingEntity livingEntity, Item item){
        return !findCurio(livingEntity, item).isEmpty();
    }

    public static ItemStack findCurio(LivingEntity livingEntity, Item item){
        ItemStack foundStack = ItemStack.EMPTY;
        if (livingEntity instanceof Player) {
            if (CuriosLoaded.CURIOS.isLoaded()) {
                Optional<SlotResult> slotResult = CuriosApi.getCuriosInventory(livingEntity).map(inv -> inv.findFirstCurio(item))
                        .orElse(Optional.empty());
                if (slotResult.isPresent()) {
                    foundStack = slotResult.get().stack();
                }
            }
        }

        return foundStack;
    }

    public static ItemStack findCurioInAll(Player playerEntity, Item item){
        ItemStack foundStack = ItemStack.EMPTY;
        if (CuriosLoaded.CURIOS.isLoaded()) {
            Optional<SlotResult> slotResult = CuriosApi.getCuriosInventory(playerEntity).map(inv -> inv.findFirstCurio(item))
                    .orElse(Optional.empty());
            if (slotResult.isPresent()) {
                foundStack = slotResult.get().stack();
            }
        }

        if (playerEntity.getOffhandItem().is(item)){
            foundStack = playerEntity.getOffhandItem();
        } else {
            for (int i = 0; i <= 9; i++) {
                ItemStack itemStack = playerEntity.getInventory().getItem(i);
                if (!itemStack.isEmpty() && itemStack.is(item)) {
                    foundStack = itemStack;
                    break;
                }
            }
        }
        return foundStack;
    }

    public static boolean noHeadWear(LivingEntity livingEntity){
        if (livingEntity != null) {
            if (CuriosLoaded.CURIOS.isLoaded()) {
                return CuriosApi.getCuriosInventory(livingEntity).map(inv -> inv.findCurios("head").isEmpty()).orElse(false);
            }
        }
        return true;
    }

    public static List<ItemStack> getCurioList(LivingEntity livingEntity, Predicate<Item> predicate) {
        List<ItemStack> list = new ArrayList<>();
        if (livingEntity != null) {
            if (CuriosLoaded.CURIOS.isLoaded()) {
                if (CuriosApi.getCuriosInventory(livingEntity).isPresent()) {
                    Optional<ICuriosItemHandler> optional = CuriosApi.getCuriosInventory(livingEntity);
                    if (optional.isPresent()) {
                        Optional<ICuriosItemHandler> optional1 = optional;
                        if (optional1.isPresent()) {
                            for (int i = 0; i < optional1.get().getSlots(); ++i) {
                                ItemStack itemStack = optional1.get().getEquippedCurios().getStackInSlot(i);
                                if (predicate.test(itemStack.getItem())) {
                                    list.add(itemStack);
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    public static boolean onXRayGoggles(Player player, LivingEntity livingEntity) {
        if (CHCapHelper.isEnlightened(livingEntity)) {
            ItemStack curio = CHCuriosFinder.findCurio(player, CHItems.X_RAY_GOGGLES.get());
            return !curio.isEmpty() && XRayGoggles.isActivated(curio);
        }
        return false;
    }
}

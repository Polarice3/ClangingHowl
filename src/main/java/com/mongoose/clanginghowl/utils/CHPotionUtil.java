package com.mongoose.clanginghowl.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;

public class CHPotionUtil extends BrewingRecipe {

    private final ItemStack inputStack;

    public CHPotionUtil(ItemStack inputStack, Ingredient ingredient, ItemStack output) {
        super(Ingredient.of(inputStack), ingredient, output);
        this.inputStack = inputStack;
    }

    @Override
    public boolean isInput(ItemStack stack) {
        return super.isInput(stack) && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).equals(inputStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));
    }

    public static ItemStack setPotion(Holder<Potion> pPotion) {
        return PotionContents.createItemStack(Items.POTION, pPotion);
    }

    public static ItemStack setSplashPotion(Holder<Potion> pPotion) {
        return PotionContents.createItemStack(Items.SPLASH_POTION, pPotion);
    }

    public static ItemStack setLingeringPotion(Holder<Potion> pPotion) {
        return PotionContents.createItemStack(Items.LINGERING_POTION, pPotion);
    }
}

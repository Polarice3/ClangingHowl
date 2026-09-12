package com.mongoose.clanginghowl.common.crafting;

import net.minecraft.core.registries.BuiltInRegistries;
import com.mongoose.clanginghowl.ClangingHowl;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CHRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(
            BuiltInRegistries.RECIPE_SERIALIZER, ClangingHowl.MOD_ID);

    public static void init(IEventBus modEventBus){
        RECIPE_SERIALIZERS.register(modEventBus);
    }

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HammerRepairRecipe>> HAMMER_REPAIR = RECIPE_SERIALIZERS.register("hammer_repair",
            () -> new SimpleCraftingRecipeSerializer<>(HammerRepairRecipe::new));
}

package com.mongoose.clanginghowl.data;

import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.items.CHItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class CHItemModelProvider extends ItemModelProvider {
    public static final String GENERATED = "item/generated";
    public static final String HANDHELD = "item/handheld";

    public CHItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ClangingHowl.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.itemGeneratedModel(CHItems.EXTRATERRESTRIAL_STEEL.get());
        this.itemGeneratedModel(CHItems.PIECE_OF_EXTRATERRESTRIAL_STEEL.get());
        this.itemGeneratedModel(CHItems.EXTRATERRESTRIAL_STEEL_INGOT.get());
        this.itemGeneratedModel(CHItems.EXTRATERRESTRIAL_STEEL_NUGGET.get());
        this.itemGeneratedModel(CHItems.EXTRATERRESTRIAL_STEEL_PLATE.get());
        this.itemGeneratedModel(CHItems.EXTRATERRESTRIAL_ENERGY_CRYSTAL.get());
        this.itemGeneratedModel(CHItems.ENERGY_BATTERY.get());
        this.itemGeneratedModel(CHItems.ENERGY_INTENSIVE_BATTERY.get());
        this.itemGeneratedModel(CHItems.BATTERY_PANEL.get());
        this.itemGeneratedModel(CHItems.CHUNK_OF_TECHNO_FLESH.get());

        this.itemHandheldModel(CHItems.EXTRATERRESTRIAL_SWORD.get());
        this.itemHandheldModel(CHItems.EXTRATERRESTRIAL_SHOVEL.get());
        this.itemHandheldModel(CHItems.EXTRATERRESTRIAL_PICKAXE.get());
        this.itemHandheldModel(CHItems.EXTRATERRESTRIAL_AXE.get());
        this.itemHandheldModel(CHItems.EXTRATERRESTRIAL_HOE.get());

        for (Item item : ForgeRegistries.ITEMS) {
            if (ForgeRegistries.ITEMS.getKey(item) != null) {
                ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(item);
                if (resourceLocation != null) {
                    if (item instanceof SpawnEggItem && resourceLocation.getNamespace().equals(ClangingHowl.MOD_ID)) {
                        getBuilder(resourceLocation.getPath())
                                .parent(getExistingFile(new ResourceLocation("item/template_spawn_egg")));
                    }
                }
            }
        }
    }

    public void itemHandheldModel(Item item) {
        withExistingParent(itemName(item), HANDHELD).texture("layer0", resourceItem(itemName(item)));
    }

    public void itemHandheldModel(Item item, ResourceLocation texture) {
        withExistingParent(itemName(item), HANDHELD).texture("layer0", texture);
    }

    public void itemGeneratedModel(Item item) {
        withExistingParent(itemName(item), GENERATED).texture("layer0", resourceItem(itemName(item)));
    }

    public void itemGeneratedModel(Item item, ResourceLocation texture) {
        withExistingParent(itemName(item), GENERATED).texture("layer0", texture);
    }


    public ResourceLocation resourceItem(String path) {
        return ClangingHowl.location("item/" + path);
    }

    private String itemName(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }
}

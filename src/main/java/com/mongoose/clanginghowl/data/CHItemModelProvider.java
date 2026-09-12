package com.mongoose.clanginghowl.data;

import net.minecraft.core.registries.BuiltInRegistries;
import com.mongoose.clanginghowl.ClangingHowl;
import com.mongoose.clanginghowl.common.items.CHItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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
        this.itemGeneratedModel(CHItems.SMALL_ENERGY_BATTERY.get());
        this.itemGeneratedModel(CHItems.ENERGY_BATTERY.get());
        this.itemGeneratedModel(CHItems.ENERGY_INTENSIVE_BATTERY.get());
        this.itemGeneratedModel(CHItems.BATTERY_PANEL.get());
        this.itemGeneratedModel(CHItems.CHUNK_OF_TECHNOFLESH.get());
        this.itemGeneratedModel(CHItems.ENERGY_FIBER.get());
        this.itemGeneratedModel(CHItems.NETHERRACK_SHAVINGS.get());
        this.itemGeneratedModel(CHItems.FIREPROOF_STEEL_COATING.get());
        this.itemGeneratedModel(CHItems.ADVANCED_ELECTRONICS.get());
        this.itemGeneratedModel(CHItems.TECHNO_OPTICS.get());
        this.itemGeneratedModel(CHItems.TECHNO_HEART.get());
        this.itemGeneratedModel(CHItems.HEMATOMA_LUMP.get());

        this.itemHandheldModel(CHItems.EXTRATERRESTRIAL_SWORD.get());
        this.itemHandheldModel(CHItems.EXTRATERRESTRIAL_SHOVEL.get());
        this.itemHandheldModel(CHItems.EXTRATERRESTRIAL_PICKAXE.get());
        this.itemHandheldModel(CHItems.EXTRATERRESTRIAL_AXE.get());
        this.itemHandheldModel(CHItems.EXTRATERRESTRIAL_HOE.get());

        this.itemGeneratedModel(CHItems.ATTRACTION_DEVICE.get());

        this.itemGeneratedModel(CHItems.ENERGY_BARRIER_GENERATOR.get());
        this.itemGeneratedModel(CHItems.TENDON_STRENGTHENER.get());
        this.itemGeneratedModel(CHItems.ENERGY_GLOVE.get());
        this.itemGeneratedModel(CHItems.JET_BOOTS.get());
        this.itemGeneratedModel(CHItems.BLOODY_BATTERY.get());
        this.itemGeneratedModel(CHItems.REANIMATOR.get());

        this.itemGeneratedModel(CHItems.REANIMATION.get());

        /*for (Item item : BuiltInRegistries.ITEM) {
            if (BuiltInRegistries.ITEM.getKey(item) != null) {
                ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(item);
                if (resourceLocation != null) {
                    if (item instanceof SpawnEggItem && resourceLocation.getNamespace().equals(ClangingHowl.MOD_ID)) {
                        getBuilder(resourceLocation.getPath())
                                .parent(getExistingFile(ResourceLocation.parse("item/template_spawn_egg")));
                    }
                }
            }
        }*/
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
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }
}

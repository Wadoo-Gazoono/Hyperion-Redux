package com.wadoo.hyperion.server.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.server.item.HyperionMobBucket;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

import static com.wadoo.hyperion.server.registry.CreativeTabHandler.addToTab;

public class ItemHandler {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Hyperion.MODID);

    public static final RegistryObject<Item> AGRALITE_SHEET = addToTab(ITEMS.register("agralite_sheet", () -> new Item(new Item.Properties().stacksTo(64).fireResistant())));
    public static final RegistryObject<Item> MECHANICAL_INNARDS = addToTab(ITEMS.register("mechanical_innards", () -> new Item(new Item.Properties().stacksTo(64).fireResistant())));

    public static final RegistryObject<Item> CAPSLING_BUCKET = ITEMS.register("capsling_bucket",() -> new HyperionMobBucket(EntityHandler.CAPSLING, Fluids.LAVA, (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> CAPSLING_SPAWN_EGG = ITEMS.register("capsling_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.CAPSLING, 0x737170, 0x515054, new Item.Properties()));
    public static final RegistryObject<Item> GRUSK_SPAWN_EGG = ITEMS.register("grusk_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.GRUSK, 0x575757, 0x141210, new Item.Properties()));

    public static final List<RegistryObject<? extends Item>> HYPERION_ITEMS = List.of(
        CAPSLING_SPAWN_EGG, GRUSK_SPAWN_EGG,
            AGRALITE_SHEET, CAPSLING_BUCKET, MECHANICAL_INNARDS
    );

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeTabHandler.HYPERION_TAB.getKey()) {
            event.accept((Supplier<? extends ItemLike>) HYPERION_ITEMS.stream().map(item -> item.get().getDefaultInstance()).toList());
        }
    }
}
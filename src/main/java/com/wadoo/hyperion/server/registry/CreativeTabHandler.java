package com.wadoo.hyperion.server.registry;


import com.wadoo.hyperion.Hyperion;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CreativeTabHandler {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Hyperion.MODID);

    public static final List<Supplier<? extends ItemLike>> ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> HYPERION_TAB = TABS.register("hyperion_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.hyperion_tab"))
                    .icon(ItemHandler.AGRALITE_SHEET.get()::getDefaultInstance)
                    .displayItems((displayParams, output) ->
                            ItemHandler.HYPERION_ITEMS.forEach(itemLike -> output.accept(itemLike.get())))
                    .build()
    );

    public static <T extends Item> RegistryObject<T> addToTab(RegistryObject<T> itemLike) {
        ITEMS.add(itemLike);
        return itemLike;
    }

}
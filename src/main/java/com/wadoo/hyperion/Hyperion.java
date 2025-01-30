package com.wadoo.hyperion;

import com.mojang.logging.LogUtils;
import com.wadoo.hyperion.client.ClientProxy;
import com.wadoo.hyperion.server.ServerEvents;
import com.wadoo.hyperion.server.network.ServerProxy;
import com.wadoo.hyperion.server.registry.CapabilityHandler;
import com.wadoo.hyperion.server.registry.CreativeTabHandler;
import com.wadoo.hyperion.server.registry.EntityHandler;
import com.wadoo.hyperion.server.registry.ItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Hyperion.MODID)
public class Hyperion
{
    public static final String MODID = "hyperion";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static SimpleChannel NETWORK;
    public static ServerProxy PROXY;

    public Hyperion(FMLJavaModLoadingContext context)
    {

        PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);


        IEventBus bus = context.getModEventBus();
        bus.addListener(this::commonSetup);
        bus.addListener(ServerEvents::registerEntityAttributes);
        MinecraftForge.EVENT_BUS.register(this);

        EntityHandler.ENTITIES.register(bus);
        ItemHandler.ITEMS.register(bus);
        CreativeTabHandler.TABS.register(bus);
        bus.addListener(CapabilityHandler::registerCapabilities);

        MinecraftForge.EVENT_BUS.register(new ServerEvents());
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, CapabilityHandler::attachEntityCapability);
    }



    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }
}

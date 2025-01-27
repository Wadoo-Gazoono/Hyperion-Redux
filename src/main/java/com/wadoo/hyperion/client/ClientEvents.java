package com.wadoo.hyperion.client;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.client.render.entity.CapslingRenderer;
import com.wadoo.hyperion.server.registry.EntityHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Hyperion.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityHandler.CAPSLING.get(), CapslingRenderer::new);
    }
}
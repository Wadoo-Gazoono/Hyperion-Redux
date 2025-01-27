package com.wadoo.hyperion.server;

import com.wadoo.hyperion.server.entity.capsling.CapslingEntity;
import com.wadoo.hyperion.server.registry.EntityHandler;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

public class ServerEvents {
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityHandler.CAPSLING.get(), CapslingEntity.createAttributes().build());
    }
}

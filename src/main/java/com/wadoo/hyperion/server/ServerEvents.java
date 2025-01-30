package com.wadoo.hyperion.server;

import com.wadoo.hyperion.server.capability.AbilityCapability;
import com.wadoo.hyperion.server.entity.HyperionMob;
import com.wadoo.hyperion.server.entity.capsling.CapslingEntity;
import com.wadoo.hyperion.server.entity.grusk.GruskEntity;
import com.wadoo.hyperion.server.registry.AbilityHandler;
import com.wadoo.hyperion.server.registry.CapabilityHandler;
import com.wadoo.hyperion.server.registry.EntityHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerEvents {
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityHandler.CAPSLING.get(), CapslingEntity.createAttributes().build());
        event.put(EntityHandler.GRUSK.get(), GruskEntity.createAttributes().build());

    }

}

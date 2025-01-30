package com.wadoo.hyperion.server.network.message;

import com.wadoo.hyperion.server.capability.AbilityCapability;
import com.wadoo.hyperion.server.registry.CapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessageUseAbility {
    private int entityID;
    private int index;

    public MessageUseAbility() {

    }

    public MessageUseAbility(int entityID, int index) {
        this.entityID = entityID;
        this.index = index;
    }

    public static void serialize(final MessageUseAbility message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.index);
    }

    public static MessageUseAbility deserialize(final FriendlyByteBuf buf) {
        final MessageUseAbility message = new MessageUseAbility();
        message.entityID = buf.readVarInt();
        message.index = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageUseAbility, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageUseAbility message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                LivingEntity entity = (LivingEntity) Minecraft.getInstance().level.getEntity(message.entityID);
                if (entity != null) {
                    AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.ABILITY_CAPABILITY);
                    if (abilityCapability != null) {
                        abilityCapability.activateAbility(entity, abilityCapability.getAbilityTypesOnEntity(entity)[message.index]);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
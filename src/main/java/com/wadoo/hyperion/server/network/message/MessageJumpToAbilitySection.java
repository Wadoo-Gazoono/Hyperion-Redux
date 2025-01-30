package com.wadoo.hyperion.server.network.message;

import com.wadoo.hyperion.server.ability.Ability;
import com.wadoo.hyperion.server.ability.AbilityType;
import com.wadoo.hyperion.server.capability.AbilityCapability;
import com.wadoo.hyperion.server.registry.CapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessageJumpToAbilitySection {
    public static class MessageJumpToAbilitySectionServerToClient {
        private int entityID;
        private int index;
        private int sectionIndex;

        public MessageJumpToAbilitySectionServerToClient() {

        }

        public MessageJumpToAbilitySectionServerToClient(int entityID, int index, int sectionIndex) {
            this.entityID = entityID;
            this.index = index;
            this.sectionIndex = sectionIndex;
        }

        public static void serialize(final MessageJumpToAbilitySectionServerToClient message, final FriendlyByteBuf buf) {
            buf.writeVarInt(message.entityID);
            buf.writeVarInt(message.index);
            buf.writeVarInt(message.sectionIndex);
        }

        public static MessageJumpToAbilitySectionServerToClient deserialize(final FriendlyByteBuf buf) {
            final MessageJumpToAbilitySectionServerToClient message = new MessageJumpToAbilitySectionServerToClient();
            message.entityID = buf.readVarInt();
            message.index = buf.readVarInt();
            message.sectionIndex = buf.readVarInt();
            return message;
        }

        public static class Handler implements BiConsumer<MessageJumpToAbilitySectionServerToClient, Supplier<NetworkEvent.Context>> {
            @Override
            public void accept(final MessageJumpToAbilitySectionServerToClient message, final Supplier<NetworkEvent.Context> contextSupplier) {
                final NetworkEvent.Context context = contextSupplier.get();
                context.enqueueWork(() -> {
                    LivingEntity entity = (LivingEntity) Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity != null) {
                        AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(entity, CapabilityHandler.ABILITY_CAPABILITY);
                        if (abilityCapability != null) {
                            AbilityType<?, ?> abilityType = abilityCapability.getAbilityTypesOnEntity(entity)[message.index];
                            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
                            if (instance.isUsing()) instance.jumpToSection(message.sectionIndex);
                        }
                    }
                });
                context.setPacketHandled(true);
            }
        }
    }

    public static class MessageJumpToAbilitySectionClientToServer {
        private int index;
        private int sectionIndex;

        public MessageJumpToAbilitySectionClientToServer() {

        }

        public MessageJumpToAbilitySectionClientToServer(int index, int sectionIndex) {
            this.index = index;
            this.sectionIndex = sectionIndex;
        }

        public static void serialize(final MessageJumpToAbilitySectionClientToServer message, final FriendlyByteBuf buf) {
            buf.writeVarInt(message.index);
            buf.writeVarInt(message.sectionIndex);
        }

        public static MessageJumpToAbilitySectionClientToServer deserialize(final FriendlyByteBuf buf) {
            final MessageJumpToAbilitySectionClientToServer message = new MessageJumpToAbilitySectionClientToServer();
            message.index = buf.readVarInt();
            message.sectionIndex = buf.readVarInt();
            return message;
        }

        public static class Handler implements BiConsumer<MessageJumpToAbilitySectionClientToServer, Supplier<NetworkEvent.Context>> {
            @Override
            public void accept(final MessageJumpToAbilitySectionClientToServer message, final Supplier<NetworkEvent.Context> contextSupplier) {
                final NetworkEvent.Context context = contextSupplier.get();
                final ServerPlayer player = context.getSender();
                context.enqueueWork(() -> {
                    AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(player, CapabilityHandler.ABILITY_CAPABILITY);
                    if (abilityCapability != null) {
                        AbilityType<?, ?> abilityType = abilityCapability.getAbilityTypesOnEntity(player)[message.index];
                        Ability instance = abilityCapability.getAbilityMap().get(abilityType);
                        if (instance.isUsing()) instance.jumpToSection(message.sectionIndex);
                    }
                });
                context.setPacketHandled(true);
            }
        }
    }
}
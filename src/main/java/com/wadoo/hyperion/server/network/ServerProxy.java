package com.wadoo.hyperion.server.network;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.server.network.message.MessageInterruptAbility;
import com.wadoo.hyperion.server.network.message.MessageJumpToAbilitySection;
import com.wadoo.hyperion.server.network.message.MessageUseAbility;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ServerProxy {
    private int nextMessageId;

    public void init(final IEventBus modbus) {
    }

    public void onLateInit(final IEventBus modbus) {
    }



    public Player getPlayer() {
        return null;
    }

    public void initNetwork() {
        final String version = "1";
        Hyperion.NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Hyperion.MODID, "net"))
                .networkProtocolVersion(() -> version)
                .clientAcceptedVersions(version::equals)
                .serverAcceptedVersions(version::equals)
                .simpleChannel();
        this.registerMessage(MessageUseAbility.class, MessageUseAbility::serialize, MessageUseAbility::deserialize, new MessageUseAbility.Handler());
        this.registerMessage(MessageInterruptAbility.class, MessageInterruptAbility::serialize, MessageInterruptAbility::deserialize, new MessageInterruptAbility.Handler());
        this.registerMessage(MessageJumpToAbilitySection.MessageJumpToAbilitySectionServerToClient.class, MessageJumpToAbilitySection.MessageJumpToAbilitySectionServerToClient::serialize, MessageJumpToAbilitySection.MessageJumpToAbilitySectionServerToClient::deserialize, new MessageJumpToAbilitySection.MessageJumpToAbilitySectionServerToClient.Handler());
        this.registerMessage(MessageJumpToAbilitySection.MessageJumpToAbilitySectionClientToServer.class, MessageJumpToAbilitySection.MessageJumpToAbilitySectionClientToServer::serialize, MessageJumpToAbilitySection.MessageJumpToAbilitySectionClientToServer::deserialize, new MessageJumpToAbilitySection.MessageJumpToAbilitySectionClientToServer.Handler());
   }

    private <MSG> void registerMessage(final Class<MSG> clazz, final BiConsumer<MSG, FriendlyByteBuf> encoder, final Function<FriendlyByteBuf, MSG> decoder, final BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer) {
        Hyperion.NETWORK.registerMessage(this.nextMessageId++, clazz, encoder, decoder, consumer);
    }

    public void setTPS(float tickRate) {
    }


}
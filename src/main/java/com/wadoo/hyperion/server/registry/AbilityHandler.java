package com.wadoo.hyperion.server.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.server.ability.Ability;
import com.wadoo.hyperion.server.ability.AbilityType;
import com.wadoo.hyperion.server.capability.AbilityCapability;
import com.wadoo.hyperion.server.network.message.MessageInterruptAbility;
import com.wadoo.hyperion.server.network.message.MessageJumpToAbilitySection;
import com.wadoo.hyperion.server.network.message.MessageUseAbility;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;

public enum AbilityHandler {
    INSTANCE;

    //public static final AbilityType<Player, FireballAbility> FIREBALL_ABILITY = new AbilityType<>("fireball", FireballAbility::new);



    @Nullable
    public AbilityCapability.IAbilityCapability getAbilityCapability(LivingEntity entity) {
        return CapabilityHandler.getCapability(entity, CapabilityHandler.ABILITY_CAPABILITY);
    }

    @Nullable
    public Ability getAbility(LivingEntity entity, AbilityType<?, ?> abilityType) {
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            return abilityCapability.getAbilityMap().get(abilityType);
        }
        return null;
    }

    public <T extends LivingEntity> void sendAbilityMessage(T entity, AbilityType<?, ?> abilityType) {
        if (entity.level().isClientSide) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance != null && instance.canUse()) {
                abilityCapability.activateAbility(entity, abilityType);
                Hyperion.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageUseAbility(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType)));
            }
        }
    }

    public <T extends LivingEntity> void sendInterruptAbilityMessage(T entity, AbilityType<?, ?> abilityType) {
        if (entity.level().isClientSide) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance.isUsing()) {
                instance.interrupt();
                Hyperion.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageInterruptAbility(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType)));
            }
        }
    }



    public <T extends LivingEntity> void sendJumpToSectionMessage(T entity, AbilityType<?, ?> abilityType, int sectionIndex) {
        if (entity.level().isClientSide) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance.isUsing()) {
                instance.jumpToSection(sectionIndex);
                Hyperion.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageJumpToAbilitySection.MessageJumpToAbilitySectionServerToClient(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType), sectionIndex));
            }
        }
    }

    public <T extends Player> void sendClientToServerJumpToSectionMessage(T entity, AbilityType<?, ?> abilityType, int sectionIndex) {
        if (!(entity.level().isClientSide && entity instanceof LocalPlayer)) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability instance = abilityCapability.getAbilityMap().get(abilityType);
            if (instance.isUsing()) {
                instance.jumpToSection(sectionIndex);
                Hyperion.NETWORK.sendToServer(new MessageJumpToAbilitySection.MessageJumpToAbilitySectionServerToClient(entity.getId(), ArrayUtils.indexOf(abilityCapability.getAbilityTypesOnEntity(entity), abilityType), sectionIndex));
            }
        }
    }
}
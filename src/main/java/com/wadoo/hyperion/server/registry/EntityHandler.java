package com.wadoo.hyperion.server.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.server.entity.capsling.CapslingEntity;
import com.wadoo.hyperion.server.entity.grusk.GruskEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityHandler {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Hyperion.MODID);

    public static final RegistryObject<EntityType<CapslingEntity>> CAPSLING = create("capsling", EntityType.Builder.of(CapslingEntity::new, MobCategory.CREATURE).sized(0.45f, 1.0f).fireImmune());
    public static final RegistryObject<EntityType<GruskEntity>> GRUSK = create("grusk", EntityType.Builder.of(GruskEntity::new, MobCategory.MONSTER).sized(1f, 1.6f).fireImmune());

    private static <T extends Entity> RegistryObject<EntityType<T>> create(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(Hyperion.MODID + "." + name));
    }


}
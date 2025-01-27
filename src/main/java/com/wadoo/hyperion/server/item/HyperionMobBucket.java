package com.wadoo.hyperion.server.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class HyperionMobBucket extends MobBucketItem {
    public HyperionMobBucket(Supplier<? extends EntityType<?>> entity, Fluid fluid, Item.Properties properties) {
        super(entity, () -> fluid, () -> SoundEvents.BUCKET_EMPTY_FISH, properties.stacksTo(1));
    }
}
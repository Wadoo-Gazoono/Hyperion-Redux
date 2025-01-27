package com.wadoo.hyperion.server.entity.capsling;

import com.wadoo.hyperion.server.entity.capsling.CapslingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

class CapslingTemptGoal extends TemptGoal {
    public CapslingEntity entity;
    public CapslingTemptGoal(CapslingEntity mob, double speed, Ingredient ingredient, boolean canScare) {
        super((PathfinderMob) mob, speed, ingredient, canScare);
        this.entity = mob;
    }

    @Override
    public boolean canUse() {
        if(this.entity.getMainHandItem().is(Items.AIR)) {
            return super.canUse();
        }
        return false;
    }

    @Override
    public void start() {
        super.start();
        this.entity.setOpen(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setOpen(false);
    }
}

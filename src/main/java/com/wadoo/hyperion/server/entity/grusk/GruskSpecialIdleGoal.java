package com.wadoo.hyperion.server.entity.grusk;


import com.wadoo.hyperion.server.ai.AnimatedGoal;
import com.wadoo.hyperion.server.entity.HyperionMob;

class GruskSpecialIdleGoal extends AnimatedGoal {
    public GruskEntity grusk;

    public GruskSpecialIdleGoal(HyperionMob entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
        this.grusk = (GruskEntity) entity;
    }


    @Override
    public boolean canUse() {
        return this.entity.onGround() && super.canUse() && !grusk.isDisturbed();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        grusk.doingNothingTicks = 0;
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        if (grusk.isDisturbed()) stop();
        entity.freeze(false);
    }
}
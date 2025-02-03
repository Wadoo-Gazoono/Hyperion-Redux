package com.wadoo.hyperion.server.entity.grusk;


import com.wadoo.hyperion.server.ai.AnimatedGoal;
import com.wadoo.hyperion.server.entity.HyperionMob;

class GruskRoarGoal extends AnimatedGoal {
    public GruskEntity grusk;

    public GruskRoarGoal(HyperionMob entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
        this.grusk = (GruskEntity) entity;
    }


    @Override
    public boolean canUse() {
        return this.entity.onGround() && super.canUse() && grusk.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && grusk.getTarget() != null;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        grusk.setRoarCD(grusk.getRandom().nextInt(300,400));
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.freeze(true);
    }
}
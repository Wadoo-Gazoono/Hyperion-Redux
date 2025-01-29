package com.wadoo.hyperion.server.ai;

import com.wadoo.hyperion.server.entity.HyperionMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class AnimatedGoal extends Goal {
    public HyperionMob entity;
    public int state;
    public String animation;

    //Set to -1 for Looping Animation
    public int animLength;

    public int timer = 0;

    public AnimatedGoal(HyperionMob entity, int state, String animation, int animLength){
        this.entity = entity;
        this.state = state;
        this.animation = animation;
        this.animLength = animLength;
    }

    @Override
    public boolean canUse() {
        return entity.getAnimation() == this.state && this.entity.getTransition() == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        super.start();
        this.timer = 0;
        this.entity.triggerAnim("controller", this.animation);
    }

    @Override
    public void tick() {
        super.tick();
        if(animLength > 0) {
            if (this.timer <= this.animLength) {
                timer++;
                doEffects(timer);
            } else {
                stop();
            }
        }
        else{
            timer++;
            doEffects(timer);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAnimation(0);
        this.entity.unfreeze();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void doEffects(int currentTick){

    }
}
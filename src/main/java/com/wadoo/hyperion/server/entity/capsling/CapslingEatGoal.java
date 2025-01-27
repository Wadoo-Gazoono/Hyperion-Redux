package com.wadoo.hyperion.server.entity.capsling;


import com.wadoo.hyperion.server.ai.AnimatedGoal;
import com.wadoo.hyperion.server.entity.HyperionMob;
import com.wadoo.hyperion.server.entity.capsling.CapslingEntity;
import com.wadoo.hyperion.server.registry.ItemHandler;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

class CapslingEatGoal extends AnimatedGoal {
    public CapslingEntity capsling;

    public CapslingEatGoal(HyperionMob entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
        this.capsling = (CapslingEntity) entity;
    }


    @Override
    public boolean canUse() {
        return this.entity.onGround() && super.canUse() && capsling.getEatCooldown() == 0;
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
        capsling.setEatTimer(0);
        capsling.setEatCooldown(entity.getRandom().nextInt(40));
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.freeze(false);

        if (currentTick < 111)capsling.setEatTimer(Math.max(0, currentTick - 31));
        if (currentTick == 31){
            this.entity.triggerAnim("controller", "eat_loop");
        }
        if (currentTick %  5 == 0 && currentTick > 31){
            this.entity.playSound(SoundEvents.FIRE_AMBIENT,1,1);
        }
        if (currentTick == 111){
            capsling.setEatTimer(0);
            this.entity.triggerAnim("controller", "eat_end");
            this.entity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            this.entity.level().addFreshEntity(new ItemEntity(this.entity.level(),this.entity.getX(),this.entity.getY(),this.entity.getZ(),new ItemStack(ItemHandler.AGRALITE_SHEET.get(),1)));
        }
    }
}
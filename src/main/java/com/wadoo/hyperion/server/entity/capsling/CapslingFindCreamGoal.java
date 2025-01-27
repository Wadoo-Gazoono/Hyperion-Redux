package com.wadoo.hyperion.server.entity.capsling;

import com.wadoo.hyperion.server.entity.capsling.CapslingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;

import java.util.List;

class FindMagmaCreamGoal extends Goal {
    public CapslingEntity entity;
    public ItemEntity wantedItem;
    public FindMagmaCreamGoal(CapslingEntity entity){
        this.entity = entity;
    }


    @Override
    public boolean canUse() {
        List<ItemEntity> list = this.entity.level().getEntitiesOfClass(ItemEntity.class, this.entity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), CapslingEntity.ALLOWED_ITEMS);
        if(!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getItem().is(Items.MAGMA_CREAM)) {
                    return this.entity.getMainHandItem().is(Items.AIR);
                }
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        List<ItemEntity> list = this.entity.level().getEntitiesOfClass(ItemEntity.class, this.entity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), CapslingEntity.ALLOWED_ITEMS);
        if(!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getItem().is(Items.MAGMA_CREAM)) {
                    return this.entity.getMainHandItem().is(Items.AIR) && this.entity.getAnimation() == 0;
                }
            }
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

    @Override
    public void tick() {
        super.tick();
        List<ItemEntity> list = this.entity.level().getEntitiesOfClass(ItemEntity.class, this.entity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), CapslingEntity.ALLOWED_ITEMS);
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getItem().is(Items.MAGMA_CREAM)){
                wantedItem = list.get(i);
            }
        }



        if(wantedItem != null){
            this.entity.getNavigation().moveTo(wantedItem,1.2f);
            if(this.entity.distanceToSqr(wantedItem) <= 1.85d){
                this.entity.getNavigation().moveTo(wantedItem,1.2f);
                //this.entity.playSound(SoundEvents.STRIDER_EAT);
                this.entity.setItemInHand(InteractionHand.MAIN_HAND,wantedItem.getItem());
                this.wantedItem.getItem().shrink(1);
            }
        }
    }
}
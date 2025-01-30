package com.wadoo.hyperion.client.model.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.server.entity.capsling.CapslingEntity;
import com.wadoo.hyperion.server.entity.grusk.GruskEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GruskModel extends DefaultedEntityGeoModel<GruskEntity> {

    public GruskModel() {
        super(new ResourceLocation(Hyperion.MODID, "grusk"));
    }

    @Override
    public ResourceLocation getModelResource(GruskEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "geo/entity/grusk/grusk.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GruskEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "textures/entity/grusk/grusk.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GruskEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "animations/entity/grusk/grusk.animation.json");
    }

    @Override
    public RenderType getRenderType(GruskEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

    @Override
    public void setCustomAnimations(GruskEntity animatable, long instanceId, AnimationState<GruskEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);



        CoreGeoBone head = getAnimationProcessor().getBone("headRoot");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null) {
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
        }
    }
}
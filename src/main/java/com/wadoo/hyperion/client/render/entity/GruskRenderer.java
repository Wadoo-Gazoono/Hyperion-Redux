package com.wadoo.hyperion.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wadoo.hyperion.client.layer.entity.capsling.CapslingGlowLayer;
import com.wadoo.hyperion.client.layer.entity.capsling.CapslingLeadLayer;
import com.wadoo.hyperion.client.layer.entity.capsling.GruskGlowLayer;
import com.wadoo.hyperion.client.model.entity.CapslingModel;
import com.wadoo.hyperion.client.model.entity.GruskModel;
import com.wadoo.hyperion.server.entity.capsling.CapslingEntity;
import com.wadoo.hyperion.server.entity.grusk.GruskEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import javax.annotation.Nullable;

public class GruskRenderer extends GeoEntityRenderer<GruskEntity> {

    public GruskRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GruskModel());
        this.shadowRadius = 0.67788F;
        addRenderLayer(new GruskGlowLayer<>(this));

    }

    @Override
    protected float getDeathMaxRotation(GruskEntity animatable) {
        return 0f;
    }

    @Override
    public void renderRecursively(PoseStack poseStack, GruskEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }


}
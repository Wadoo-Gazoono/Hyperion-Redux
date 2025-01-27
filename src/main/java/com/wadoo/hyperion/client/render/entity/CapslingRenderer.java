package com.wadoo.hyperion.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wadoo.hyperion.client.layer.entity.capsling.CapslingGlowLayer;
import com.wadoo.hyperion.client.model.entity.CapslingModel;
import com.wadoo.hyperion.server.entity.capsling.CapslingEntity;
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

public class CapslingRenderer extends GeoEntityRenderer<CapslingEntity> {

    public CapslingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CapslingModel());
        this.shadowRadius = 0.16788F;

        addRenderLayer(new CapslingGlowLayer<>(this));
        this.addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, CapslingEntity animatable) {
                return bone.getName().equals("item") ? animatable.getItemBySlot(EquipmentSlot.MAINHAND) : null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, CapslingEntity animatable) {
                return ItemDisplayContext.GROUND;
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, CapslingEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.mulPose(Axis.YP.rotationDegrees(0));
                poseStack.mulPose(Axis.ZP.rotationDegrees(0));
                poseStack.scale(0.6f,0.6f,0.6f);
                poseStack.translate(0.0D, -0.1D, 0.0D);
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight,
                        packedOverlay);
            }

        });
    }

    @Override
    public void renderRecursively(PoseStack poseStack, CapslingEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }


}
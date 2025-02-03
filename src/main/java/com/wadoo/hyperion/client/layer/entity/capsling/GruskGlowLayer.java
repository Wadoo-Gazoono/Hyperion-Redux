package com.wadoo.hyperion.client.layer.entity.capsling;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.server.entity.capsling.CapslingEntity;
import com.wadoo.hyperion.server.entity.grusk.GruskEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GruskGlowLayer<T extends GruskEntity> extends GeoRenderLayer<GruskEntity> {
    private ResourceLocation texture = new ResourceLocation(Hyperion.MODID, "textures/entity/grusk/grusk_eyes.png");
    public GruskGlowLayer(GeoRenderer<GruskEntity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, GruskEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.dragonExplosionAlpha(texture);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
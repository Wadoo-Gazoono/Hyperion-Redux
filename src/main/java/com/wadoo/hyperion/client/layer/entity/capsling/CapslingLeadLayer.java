package com.wadoo.hyperion.client.layer.entity.capsling;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.server.entity.capsling.CapslingEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class CapslingLeadLayer<T extends CapslingEntity> extends GeoRenderLayer<CapslingEntity> {
    private ResourceLocation texture = new ResourceLocation(Hyperion.MODID, "textures/entity/capsling/capsling_lead.png");
    public CapslingLeadLayer(GeoRenderer<CapslingEntity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, CapslingEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (!animatable.isLeashed()) return;
        RenderType armorRenderType = RenderType.entityCutout(texture);
        poseStack.scale(1.1f,1f,1.1f);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
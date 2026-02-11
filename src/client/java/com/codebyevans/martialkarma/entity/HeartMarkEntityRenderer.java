package com.codebyevans.martialkarma.entity;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class HeartMarkEntityRenderer extends EntityRenderer<HeartMarkEntity> {

    public HeartMarkEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(HeartMarkEntity entity) {
        return Identifier.of("minecraft", "textures/item/ender_pearl.png");

    }

}

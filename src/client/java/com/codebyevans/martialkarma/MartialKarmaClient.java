package com.codebyevans.martialkarma;

import com.codebyevans.martialkarma.client.KarmaHudOverlay;
import com.codebyevans.martialkarma.client.KeyInputHandler;
import com.codebyevans.martialkarma.client.ModKeybindings;
import com.codebyevans.martialkarma.entity.HeartMarkEntityRenderer;
import com.codebyevans.martialkarma.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class MartialKarmaClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register(new KarmaHudOverlay());
		ModKeybindings.register();
		KeyInputHandler.register();

		EntityRendererRegistry.register(
				ModEntities.HEART_MARK,
				HeartMarkEntityRenderer::new
		);
	}
}
package com.codebyevans.martialkarma.client;

import com.codebyevans.martialkarma.PlayerKarma;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class KarmaHudOverlay implements HudRenderCallback {

    // Puedes crear tus propias texturas o usar colores
    private static final Identifier KARMA_BAR_EMPTY = Identifier.of("minecraft", "textures/gui/bars.png");

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) return;

        PlayerEntity player = client.player;
        int karmaPoints = PlayerKarma.getKarmaPoints(player);
        int maxKarma = PlayerKarma.getMaxKarmaPoints(player);

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int barWidth = 81;
        int barHeight = 9;

        // Posición de la barra (arriba a la izquierda, debajo de la salud)
        int x = screenWidth / 2 - barWidth / -10;  // Centrado horizontalmente
        int y = screenHeight - 51;


        // Dibujar fondo de la barra (negro semi-transparente)
        drawContext.fill(x - 1, y - 1, x + barWidth + 1, y + barHeight + 1, 0x80000000);

        // Calcular el ancho de la barra llena
        int filledWidth = (int)((float)karmaPoints / maxKarma * barWidth);

        // Dibujar la barra vacía (gris oscuro)
        drawContext.fill(x, y, x + barWidth, y + barHeight, 0xFF333333);

        // Dibujar la barra llena (color morado/mágico)
        drawContext.fill(x, y, x + filledWidth, y + barHeight, 0xFF9933FF);

        // Dibujar texto con los puntos
        String text = karmaPoints + " / " + maxKarma;
        drawContext.drawText(
                client.textRenderer,
                text,
                x + barWidth / 2 - client.textRenderer.getWidth(text) / 2,
                y + 1,
                0xFFFFFF,
                true
        );
    }
}
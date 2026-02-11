package com.codebyevans.martialkarma.client;

import com.codebyevans.martialkarma.networking.HeartMarkPacketClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class KeyInputHandler {

    private static boolean wasHeartMarkPressed = false;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            boolean isHeartMarkPressed = ModKeybindings.heartMarkKey.isPressed();

            if (isHeartMarkPressed && !wasHeartMarkPressed) {
                HeartMarkPacketClient.send();
            }

            wasHeartMarkPressed = isHeartMarkPressed;
        });
    }
}
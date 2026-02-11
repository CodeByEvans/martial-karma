// src/client/java/com/codebyevans/martialkarma/networking/HeartMarkPacketClient.java
package com.codebyevans.martialkarma.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class HeartMarkPacketClient {

    public static void send() {
        // Usa la clase main
        ClientPlayNetworking.send(new HeartMarkPacket());
    }
}

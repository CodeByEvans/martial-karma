package com.codebyevans.martialkarma.networking;

import com.codebyevans.martialkarma.KarmaType;
import com.codebyevans.martialkarma.PlayerKarma;
import com.codebyevans.martialkarma.ability.AbilityData;
import com.codebyevans.martialkarma.entity.HeartMarkEntity;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public record HeartMarkPacket() implements CustomPayload {

    public static final CustomPayload.Id<HeartMarkPacket> ID =
            new CustomPayload.Id<>(Identifier.of("martialkarma", "heart_mark"));

    public static final PacketCodec<RegistryByteBuf, HeartMarkPacket> CODEC =
            PacketCodec.unit(new HeartMarkPacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(ID, CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ID, (payload, context) -> {
            context.server().execute(() -> {

                var player = context.player();
                KarmaType karma = PlayerKarma.getKarma(player);

                if (karma != KarmaType.DEATH) {
                    player.sendMessage(Text.literal("§cSolo los usuarios con Karma DEATH pueden usar esta habilidad"), true);
                    return;
                }

                AbilityData abilityData = PlayerKarma.getAbilityData(player);

                if (abilityData.getHeartMarkLevel() == 0) {
                    player.sendMessage(Text.literal("§cNecesitas desbloquear esta habilidad primero"), true);
                    return;
                }

                long currentTime = System.currentTimeMillis();

                if (abilityData.isOnCooldown(currentTime)) {
                    int remaining = abilityData.getRemainingCooldown(currentTime);
                    player.sendMessage(Text.literal("§cCooldown: " + remaining + " segundos restantes"), true);
                    return;
                }

                if (!PlayerKarma.consumeKarmaPoints(player, 50)) {
                    player.sendMessage(Text.literal("§cNo tienes suficiente Karma (necesitas 50)"), true);
                    return;
                }

                float currentHealth = player.getHealth();
                float healthCost = currentHealth * 0.15f;

                if (currentHealth - healthCost <= 1.0f) {
                    player.sendMessage(Text.literal("§cNo tienes suficiente vida para usar esta habilidad"), true);
                    PlayerKarma.addKarmaPoints(player, 50);
                    return;
                }

                player.setHealth(currentHealth - healthCost);

                HeartMarkEntity projectile = new HeartMarkEntity(
                        player.getWorld(),
                        player,
                        abilityData.getHeartMarkLevel()
                );

                player.getWorld().spawnEntity(projectile);

                abilityData.setLastHeartMarkUse(currentTime);

                String levelText = switch (abilityData.getHeartMarkLevel()) {
                    case 1 -> "§5Marca en el Corazón I §7(30% daño)";
                    case 2 -> "§5Marca en el Corazón II §7(40% daño)";
                    case 3 -> "§5Marca en el Corazón III §7(50% daño)";
                    default -> "§5Marca en el Corazón";
                };

                player.sendMessage(Text.literal(levelText + " §5lanzada!"), true);
            });
        });
    }
}

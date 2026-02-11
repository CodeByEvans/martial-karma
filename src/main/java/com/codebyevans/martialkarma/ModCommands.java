package com.codebyevans.martialkarma;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerEntity;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ModCommands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            // Comando /setkarma <tipo>
            dispatcher.register(literal("setkarma")
                    .then(argument("tipo", StringArgumentType.word())
                            .executes(context -> {
                                ServerCommandSource source = context.getSource();
                                PlayerEntity player = source.getPlayerOrThrow();
                                String tipo = StringArgumentType.getString(context, "tipo").trim().toUpperCase();

                                try {
                                    KarmaType kt = KarmaType.valueOf(tipo);
                                    PlayerKarma.setKarmaType(player, kt);
                                    player.sendMessage(Text.literal("Karma seteado a " + kt.name()), false);
                                    return 1;
                                } catch (IllegalArgumentException e) {
                                    player.sendMessage(Text.literal("Tipo de Karma inválido!"), false);
                                    return 0;
                                }
                            })
                    )
            );

            // Comando /getkarma
            dispatcher.register(literal("getkarma")
                    .executes(context -> {
                        PlayerEntity player = context.getSource().getPlayerOrThrow();
                        KarmaType kt = PlayerKarma.getKarma(player);
                        player.sendMessage(Text.literal("Tu Karma actual es " + kt.name()), false);
                        return 1;
                    })
            );
        });
    }
}
package com.codebyevans.martialkarma.command;

import com.codebyevans.martialkarma.PlayerKarma;
import com.codebyevans.martialkarma.ability.AbilityData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class AbilityCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("heartmark")
                        .then(CommandManager.literal("level")
                                .then(CommandManager.argument("level", IntegerArgumentType.integer(0, 3))
                                        .executes(context -> {
                                            var player = context.getSource().getPlayer();
                                            int level = IntegerArgumentType.getInteger(context, "level");

                                            AbilityData data = PlayerKarma.getAbilityData(player);
                                            data.setHeartMarkLevel(level);

                                            player.sendMessage(Text.literal("§aHeart Mark nivel establecido a: " + level));
                                            return 1;
                                        })
                                )
                        )
        );
    }
}
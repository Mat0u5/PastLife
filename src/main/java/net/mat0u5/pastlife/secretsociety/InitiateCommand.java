package net.mat0u5.pastlife.secretsociety;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class InitiateCommand  {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                literal("initiate")
                        .executes(context -> execute(context.getSource()))
        );
    }

    public static int execute(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) return -1;

        if (!SecretSociety.members.contains(player.getUuid())) {
            source.sendError(Text.of("§cYou are not a Member, you cannot use this command."));
            return -1;
        }

        if (!SecretSociety.yetToInitiate.contains(player.getUuid())) {
            source.sendError(Text.of("§cYou have already been initiated."));
            source.sendFeedback(() -> Text.of("§7Find the other members with the secret word: §d\""+SecretSociety.secretWord+"\""), false);
            return -1;
        }

        SecretSociety.initiatePlayer(player);

        return 1;
    }
}

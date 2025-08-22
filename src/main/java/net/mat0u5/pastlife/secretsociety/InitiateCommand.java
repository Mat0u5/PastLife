package net.mat0u5.pastlife.secretsociety;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.literal;

public class InitiateCommand  {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean b) {
        dispatcher.register(
                literal("initiate")
                        .executes(context -> execute(context.getSource()))
        );
    }

    public static int execute(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        if (!SecretSociety.members.contains(player.getUuid())) {
            source.sendError(new LiteralText("§cYou are not a Member, you cannot use this command."));
            return -1;
        }

        if (!SecretSociety.yetToInitiate.contains(player.getUuid())) {
            source.sendError(new LiteralText("§cYou have already been initiated."));
            source.sendFeedback(new LiteralText("§7Find the other members with the secret word: §d\""+SecretSociety.secretWord+"\""), false);
            return -1;
        }

        SecretSociety.initiatePlayer(player);

        return 1;
    }
}

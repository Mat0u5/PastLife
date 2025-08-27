package net.mat0u5.pastlife.secretsociety;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SecretSocietyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean b) {
        dispatcher.register(
            literal("society")
                .then(literal("begin")
                    .requires(PlayerUtils::isAdmin)
                    .executes(context -> begin(context.getSource()))
                    .then(argument("secret_word", StringArgumentType.string())
                        .executes(context -> beginWord(context.getSource(), StringArgumentType.getString(context, "secret_word")))
                    )
                )
                .then(literal("success")
                    .executes(context -> success(context.getSource()))
                )
                .then(literal("fail")
                    .executes(context -> fail(context.getSource()))
                )
        );
    }

    public static int begin(ServerCommandSource source) {
        SecretSociety.beginSociety(source.getMinecraftServer());
        return 1;
    }

    public static int beginWord(ServerCommandSource source, String word) {
        SecretSociety.beginSociety(source.getMinecraftServer(), word);
        return 1;
    }

    public static int success(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();

        if (!SecretSociety.members.contains(player.getUuid())) {
            source.sendError(new LiteralText("§cYou are not a Member, you cannot use this command."));
            return -1;
        }

        if (SecretSociety.yetToInitiate.contains(player.getUuid())) {
            source.sendError(new LiteralText("§cYou have not been initiated."));
            return -1;
        }

        if (SecretSociety.ended) {
            source.sendError(new LiteralText("§cThe society has already ended."));
            return -1;
        }

        Main.log(player.getEntityName()+" ran the '/society success' command");
        SecretSociety.end(source.getMinecraftServer(), true);
        return 1;
    }

    public static int fail(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();

        if (!SecretSociety.members.contains(player.getUuid())) {
            source.sendError(new LiteralText("§cYou are not a Member, you cannot use this command."));
            return -1;
        }

        if (SecretSociety.yetToInitiate.contains(player.getUuid())) {
            source.sendError(new LiteralText("§cYou have not been initiated."));
            return -1;
        }

        if (SecretSociety.ended) {
            source.sendError(new LiteralText("§cThe society has already ended."));
            return -1;
        }

        Main.log(player.getEntityName()+" ran the '/society fail' command");
        SecretSociety.end(source.getMinecraftServer(), false);
        return 1;
    }
}

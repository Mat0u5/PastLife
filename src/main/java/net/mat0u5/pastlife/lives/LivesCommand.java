package net.mat0u5.pastlife.lives;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LivesCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean b) {
        dispatcher.register(
            literal("lives")
            .executes(context -> showLives(context.getSource()))
            .then(literal("get")
                    .requires(PlayerUtils::isAdmin)
                    .then(argument("player", EntityArgumentType.player())
                            .executes(context -> getLives(
                                    context.getSource(), EntityArgumentType.getPlayer(context, "player")
                            ))
                    )
            )
            .then(literal("set")
                    .requires(PlayerUtils::isAdmin)
                    .then(argument("player", EntityArgumentType.player())
                            .then(argument("amount", IntegerArgumentType.integer())
                                    .executes(context -> setLives(
                                            context.getSource(), EntityArgumentType.getPlayer(context, "player"), IntegerArgumentType.getInteger(context, "amount")
                                    ))
                            )
                    )
            )
            .then(literal("add")
                    .requires(PlayerUtils::isAdmin)
                    .then(argument("player", EntityArgumentType.player())
                            .then(argument("amount", IntegerArgumentType.integer())
                                    .executes(context -> addLives(
                                            context.getSource(), EntityArgumentType.getPlayer(context, "player"), IntegerArgumentType.getInteger(context, "amount")
                                    ))
                            )
                    )
            )
            .then(literal("remove")
                    .requires(PlayerUtils::isAdmin)
                    .then(argument("player", EntityArgumentType.player())
                            .then(argument("amount", IntegerArgumentType.integer())
                                    .executes(context -> removeLives(
                                            context.getSource(), EntityArgumentType.getPlayer(context, "player"), IntegerArgumentType.getInteger(context, "amount")
                                    ))
                            )
                    )
            )
        );
    }

    public static int showLives(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();

        int lives = Main.livesManager.getLives(player);
        source.sendFeedback(Text.of("You have " + lives + " " + (lives == 1 ? "life" : "lives") + "."), false);
        return 1;
    }

    public static int getLives(ServerCommandSource source, ServerPlayerEntity target) {
        int lives = Main.livesManager.getLives(target);
        source.sendFeedback(Text.of(target.getEntityName() + " has " + lives + " " + (lives == 1 ? "life" : "lives") + "."), false);
        return 1;
    }

    public static int setLives(ServerCommandSource source, ServerPlayerEntity target, int amount) {
        Main.livesManager.setLives(target, amount);
        sendAmountOfLives(source, target);
        return 1;
    }

    public static int addLives(ServerCommandSource source, ServerPlayerEntity target, int amount) {
        Main.livesManager.addLives(target, amount);
        sendAmountOfLives(source, target);
        return 1;
    }

    public static int removeLives(ServerCommandSource source, ServerPlayerEntity target, int amount) {
        Main.livesManager.addLives(target, -amount);
        sendAmountOfLives(source, target);
        return 1;
    }

    public static void sendAmountOfLives(ServerCommandSource source, ServerPlayerEntity target) {
        int lives = Main.livesManager.getLives(target);
        source.sendFeedback(Text.of(target.getEntityName() + " now has " + lives + " " + (lives == 1 ? "life" : "lives") + "."), true);
    }
}

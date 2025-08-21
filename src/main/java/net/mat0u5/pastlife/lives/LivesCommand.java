package net.mat0u5.pastlife.lives;

import net.mat0u5.pastlife.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.exception.InvalidNumberException;
import net.minecraft.server.command.exception.PlayerNotFoundException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LivesCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "lives";
    }

    @Override
    public String getUsage(CommandSource source) {
        return "/lives usage:\n§7For admins: '/lives <get|set|add|remove> <player> [amount]'\n§7For players: '/lives'";
    }

    @Override
    public boolean canUse(MinecraftServer server, CommandSource source) {
        return true;
    }

    @Override
    public void run(MinecraftServer server, CommandSource source, String[] args) throws PlayerNotFoundException, InvalidNumberException {
        ServerPlayerEntity player = asPlayer(source);
        if (args.length == 0) {
            int lives = Main.livesManager.getLives(player);
            source.sendMessage(new LiteralText("You have " + lives + " " + (lives == 1 ? "life" : "lives") + "."));
            return;
        }

        if (!server.getPlayerManager().isOp(player.getGameProfile())) {
            source.sendMessage(new LiteralText("§cYou do not have permission to use this command."));
            return;
        }


        if (args.length < 2) {
            sendUsageInfo(source);
            return;
        }

        String playerName = args[1];
        ServerPlayerEntity serverPlayer = server.getPlayerManager().get(playerName);
        if (serverPlayer == null) {
            source.sendMessage(new LiteralText("Player not found: " + playerName));
            return;
        }
        playerName = serverPlayer.getName();

        if (args[0].equalsIgnoreCase("get")) {
            int lives = Main.livesManager.getLives(serverPlayer);
            source.sendMessage(new LiteralText(playerName + " has " + lives + " " + (lives == 1 ? "life" : "lives") + "."));
            return;
        }
        if (args.length >= 3) {
            int amount = parseInt(args[2], 0);
            if (args[0].equalsIgnoreCase("set")) {
                Main.livesManager.setLives(serverPlayer, amount);
            }
            else if (args[0].equalsIgnoreCase("add")) {
                Main.livesManager.addLives(serverPlayer, amount);
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                Main.livesManager.addLives(serverPlayer, -amount);
            }
            else {
                sendUsageInfo(source);
                return;
            }

            int lives = Main.livesManager.getLives(serverPlayer);
            source.sendMessage(new LiteralText(playerName + " now has " + lives + " " + (lives == 1 ? "life" : "lives") + "."));
            return;
        }
        sendUsageInfo(source);
    }

    public void sendUsageInfo(CommandSource source) {
        source.sendMessage(new LiteralText("§cInvalid usage."));
        source.sendMessage(new LiteralText(getUsage(source)));
    }

    @Override
    public List<String> getSuggestions(MinecraftServer server, CommandSource source, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return suggestMatching(args, new String[]{"get", "set", "add", "remove"});
        }
        else if (args.length == 2 && !args[1].equalsIgnoreCase("get")) {
            return suggestMatching(args, server.getPlayerNames());
        }
        return null;
    }

    @Override
    public boolean hasTargetSelectorAt(String[] args, int index) {
        return index == 1;
    }
}

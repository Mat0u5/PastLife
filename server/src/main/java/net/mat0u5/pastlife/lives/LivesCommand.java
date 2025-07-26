package net.mat0u5.pastlife.lives;

import net.mat0u5.pastlife.Main;
import net.minecraft.command.source.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

public class LivesCommand {
    public static void handleCommand(MinecraftServer server, String command, CommandSource source) {
        int response = commandLogic(server, command, source);
        if (response != 1) {
            source.sendMessage("Invalid usage. Use: /lives <get|set|add|remove> <player> [amount]");
        }
    }

    public static int commandLogic(MinecraftServer server, String command, CommandSource source) {
        if (!command.startsWith("/lives ")) {
            return -1;
        }
        String[] args = command.split(" ");
        if (args.length < 3) {
            return -1;
        }

        String playerName = args[2];
        ServerPlayerEntity serverPlayer = server.playerManager.get(playerName);
        if (serverPlayer == null) {
            source.sendMessage("Player not found: " + playerName);
            return -1;
        }
        playerName = serverPlayer.name;

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("get")) {
                int lives = Main.livesManager.getLives(serverPlayer);
                source.sendMessage( playerName + " has " + lives + " " + (lives == 1 ? "life" : "lives") + ".");
                return 1;
            }
        }
        else if (args.length >= 4) {
            try {
                int amount = Integer.parseInt(args[3]);
                if (args[1].equalsIgnoreCase("set")) {
                    Main.livesManager.setLives(serverPlayer, amount);
                }
                else if (args[1].equalsIgnoreCase("add")) {
                    Main.livesManager.addLives(serverPlayer, amount);
                }
                else if (args[1].equalsIgnoreCase("remove")) {
                    Main.livesManager.addLives(serverPlayer, -amount);
                }
                else {
                    return -1;
                }
            }catch(Exception e) {
                source.sendMessage("Invalid number format for amount: " + args[3]);
                return -1;
            }

            int lives = Main.livesManager.getLives(serverPlayer);
            source.sendMessage( playerName + " now has " + lives + " " + (lives == 1 ? "life" : "lives") + ".");
            return 1;
        }
        return -1;
    }
}

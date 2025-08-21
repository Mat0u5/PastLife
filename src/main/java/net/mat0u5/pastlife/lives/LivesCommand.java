package net.mat0u5.pastlife.lives;

import net.mat0u5.pastlife.Main;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LivesCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "lives";
    }

    @Override
    public String getUsage(CommandSource source) {
        return "Use: /lives <get|set|add|remove> <player> [amount]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void run(CommandSource source, String[] args) {
        ServerPlayerEntity player = asPlayer(source);
        MinecraftServer server = MinecraftServer.getInstance();
        if (!args[0].equalsIgnoreCase("lives")) return;
        if (args.length == 1) {
            int lives = Main.livesManager.getLives(player);
            source.sendMessage("You have " + lives + " " + (lives == 1 ? "life" : "lives") + ".");
            return;
        }

        if (args.length < 3) {
            return;
        }

        String playerName = args[2];
        ServerPlayerEntity serverPlayer = server.getPlayerManager().get(playerName);
        if (serverPlayer == null) {
            source.sendMessage("Player not found: " + playerName);
            return;
        }
        playerName = serverPlayer.name;

        if (args[1].equalsIgnoreCase("get")) {
            int lives = Main.livesManager.getLives(serverPlayer);
            source.sendMessage(playerName + " has " + lives + " " + (lives == 1 ? "life" : "lives") + ".");
            return;
        }
        if (args.length >= 4) {
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
                    return;
                }
            }catch(Exception e) {
                source.sendMessage("Invalid number format for amount: " + args[3]);
                return;
            }

            int lives = Main.livesManager.getLives(serverPlayer);
            source.sendMessage( playerName + " now has " + lives + " " + (lives == 1 ? "life" : "lives") + ".");
            return;
        }
    }

    @Override
    public List getSuggestions(CommandSource source, String[] args) {
        if (args.length == 1) {
            return suggestMatching(args, new String[]{"get", "set", "add", "remove"});
        }
        else if (args.length == 2 && !args[1].equalsIgnoreCase("get")) {
            return suggestMatching(args, MinecraftServer.getInstance().getPlayerNames());
        }
        return null;
    }

    @Override
    public boolean hasTargetSelectorAt(int index) {
        return index == 1;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (o instanceof Command) {
            return this.getName().compareTo(((Command) o).getName());
        }
        return 0;
    }
}

package net.mat0u5.pastlife.secretsociety;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.boogeyman.BoogeymanManager;
import net.minecraft.command.source.CommandSource;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

public class SecretSocietyCommand {
    public static void handleCommand(MinecraftServer server, ServerPlayerEntity player, String command, ServerPlayNetworkHandler networkHandler) {
        int response = commandLogic(server, player, command, networkHandler);
        if (response != 1) {
            networkHandler.sendPacket(new ChatMessagePacket("§cInvalid usage."));
            networkHandler.sendMessage("For admins: '/society begin' or '/society begin <secret_word>'");
            networkHandler.sendMessage("For members: '/initiate' and '/society success|fail'");
        }
    }

    public static int commandLogic(MinecraftServer server, ServerPlayerEntity player, String command, CommandSource source) {
        if (command.equalsIgnoreCase("/initiate")) {
            if (SecretSociety.members.contains(player.name)) {
                if (SecretSociety.yetToInitiate.contains(player.name)) {
                    SecretSociety.initiatePlayer(player);
                }
                else {
                    source.sendMessage("§cYou have already been initiated.");
                    source.sendMessage("§7Find the other members with the secret word: §d\""+SecretSociety.secretWord+"\"");
                }
            }
            else {
                source.sendMessage("§cYou are not a Member, you cannot use this command.");
            }
            return 1;
        }
        if (command.equalsIgnoreCase("/society begin")) {
            if (server.playerManager.isOp(player.name)) {
                SecretSociety.beginSociety(server);
            }
            else {
                source.sendMessage("§cYou do not have permission to use this command.");
            }
            return 1;
        }
        if (command.startsWith("/society begin ")) {
            String word = command.replaceFirst("/society begin ","");
            if (server.playerManager.isOp(player.name)) {
                SecretSociety.beginSociety(server, word);
            }
            else {
                source.sendMessage("§cYou do not have permission to use this command.");
            }
            return 1;
        }
        if (command.equalsIgnoreCase("/society success")) {
            if (SecretSociety.members.contains(player.name)) {
                if (SecretSociety.yetToInitiate.contains(player.name)) {
                    source.sendMessage("§cYou have not been initiated.");
                }
                else {
                    if (SecretSociety.ended) {
                        source.sendMessage("§cThe society has already ended.");
                    }
                    else {
                        Main.log(player.name+" ran the '/society success' command");
                        SecretSociety.end(server, true);
                    }
                }
            }
            else {
                source.sendMessage("§cYou are not a Member, you cannot use this command.");
            }
            return 1;
        }
        if (command.equalsIgnoreCase("/society fail")) {
            if (SecretSociety.members.contains(player.name)) {
                if (SecretSociety.yetToInitiate.contains(player.name)) {
                    source.sendMessage("§cYou have not been initiated.");
                }
                else {
                    if (SecretSociety.ended) {
                        source.sendMessage("§cThe society has already ended.");
                    }
                    else {
                        Main.log(player.name+" ran the '/society fail' command");
                        SecretSociety.end(server, false);
                    }
                }
            }
            else {
                source.sendMessage("§cYou are not a Member, you cannot use this command.");
            }
            return 1;
        }
        return -1;
    }
}

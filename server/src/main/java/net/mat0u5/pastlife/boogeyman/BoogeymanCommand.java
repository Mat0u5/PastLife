package net.mat0u5.pastlife.boogeyman;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.packets.TitlePacket;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.minecraft.command.source.CommandSource;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;

public class BoogeymanCommand {
    public static void handleCommand(MinecraftServer server, ServerPlayerEntity player, String command, ServerPlayNetworkHandler networkHandler) {
        int response = commandLogic(server, player, command, networkHandler);
        if (response != 1) {
            networkHandler.sendPacket(new ChatMessagePacket("§cInvalid usage."));
            networkHandler.sendMessage("For admins: /boogeyman choose");
            networkHandler.sendMessage("For boogeymen: /boogeyman succeed|fail");
        }
    }

    public static int commandLogic(MinecraftServer server, ServerPlayerEntity player, String command, CommandSource source){
        if (command.equalsIgnoreCase("/boogeyman choose")) {
            if (server.playerManager.isOp(player.name)) {
                BoogeymanManager.rollBoogeymen(server);
            }
            else {
                source.sendMessage("§cYou do not have permission to use this command.");
            }
            return 1;
        }
        if (command.equalsIgnoreCase("/boogeyman succeed")) {
            if (BoogeymanManager.boogeymen.contains(player.name)) {
                BoogeymanManager.boogeymen.remove(player.name);
                PlayerUtils.playSoundToPlayer(player, "boogeyman_cure", 1, 1);
                PlayerUtils.sendPacketToPlayer(player, new TitlePacket("§aYou are cured!","", 20, 30, 20));
                PlayerUtils.sendPacketToAllPlayers(new ChatMessagePacket(player.name + "§7 has been cured of the Boogeyman curse!"));
                Main.log(player.name + " has been cured of the Boogeyman curse!");
            }
            else {
                source.sendMessage("§cYou are not a Boogeyman, you cannot use this command.");
            }
            return 1;
        }
        if (command.equalsIgnoreCase("/boogeyman fail")) {
            if (BoogeymanManager.boogeymen.contains(player.name)) {
                BoogeymanManager.boogeymen.remove(player.name);
                PlayerUtils.playSoundToPlayer(player, "boogeyman_fail", 1, 1);
                PlayerUtils.sendPacketToPlayer(player, new TitlePacket("§cYou have failed.","", 20, 30, 20));
                PlayerUtils.sendPacketToAllPlayers(new ChatMessagePacket(player.name + "§7 failed to kill a player while being the §cBoogeyman§7."));
                PlayerUtils.sendPacketToAllPlayers(new ChatMessagePacket("§7They have been dropped to their §cLast Life§7."));
                Main.log(player.name + " failed to kill a player while being the Boogeyman.");
                if (Main.livesManager != null && Main.livesManager.getLives(player) > 1) {
                    Main.livesManager.setLives(player, 1);
                }
            }
            else {
                source.sendMessage("§cYou are not a Boogeyman, you cannot use this command.");
            }
            return 1;
        }
        return -1;
    }
}

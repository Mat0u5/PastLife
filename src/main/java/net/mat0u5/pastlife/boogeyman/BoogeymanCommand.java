package net.mat0u5.pastlife.boogeyman;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.packets.TitlePacket;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.minecraft.network.packet.ChatMessagePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BoogeymanCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "boogeyman";
    }

    @Override
    public String getUsage(CommandSource source) {
        return "/boogeyman usage:\n§7For admins: '/boogeyman choose'"+"\n"+"For boogeymen: '/boogeyman success|fail'";
    }

    @Override
    public boolean canUse(CommandSource source) {
        return true;
    }

    @Override
    public void run(CommandSource source, String[] args) {
        ServerPlayerEntity player = asPlayer(source);
        if (args.length < 1) {
            sendUsageInfo(source);
            return;
        }
        MinecraftServer server = MinecraftServer.getInstance();
        if (args[0].equalsIgnoreCase("choose")) {
            if (server.getPlayerManager().isOp(player.name)) {
                BoogeymanManager.rollBoogeymen(server);
            }
            else {
                source.sendMessage("§cYou do not have permission to use this command.");
            }
            return;
        }
        if (args[0].equalsIgnoreCase("success")) {
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
            return;
        }
        if (args[0].equalsIgnoreCase("fail")) {
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
            return;
        }
        sendUsageInfo(source);
    }

    public void sendUsageInfo(CommandSource source) {
        source.sendMessage("§cInvalid usage.");
        source.sendMessage(getUsage(source));
    }

    @Override
    public List getSuggestions(CommandSource source, String[] args) {
        if (args.length == 1) {
            return suggestMatching(args, new String[]{"choose", "success", "fail"});
        }
        return null;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (o instanceof Command) {
            return this.getName().compareTo(((Command) o).getName());
        }
        return 0;
    }
}

package net.mat0u5.pastlife.secretsociety;

import net.mat0u5.pastlife.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SecretSocietyCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "society";
    }

    @Override
    public String getUsage(CommandSource source) {
        return "/society usage:§7\nFor admins: '/society begin' or '/society begin <secret_word>'" + "\n" + "For members: '/initiate' and '/society success|fail'";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean canUse(CommandSource source) {
        return MinecraftServer.getInstance().isSingleplayer() || super.canUse(source);
    }

    @Override
    public void run(CommandSource source, String[] args) {
        ServerPlayerEntity player = asPlayer(source);
        if (args.length < 1) {
            sendUsageInfo(source);
            return;
        }
        MinecraftServer server = MinecraftServer.getInstance();

        if (args[0].equalsIgnoreCase("begin")) {
            if (server.getPlayerManager().isOp(player.name)) {
                SecretSociety.beginSociety(server, args[2]);
            }
            else {
                source.sendMessage("§cYou do not have permission to use this command.");
            }
            return;
        }
        if (args[0].equalsIgnoreCase("success")) {
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
            return;
        }
        if (args[0].equalsIgnoreCase("fail")) {
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
            return;
        }
        sendUsageInfo(source);
    }

    public void sendUsageInfo(CommandSource source) {
        source.sendMessage(getUsage(source));
    }

    @Override
    public List getSuggestions(CommandSource source, String[] args) {
        if (args.length == 1) {
            return suggestMatching(args, new String[]{"begin", "success", "fail"});
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

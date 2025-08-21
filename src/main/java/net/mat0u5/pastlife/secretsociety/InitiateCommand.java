package net.mat0u5.pastlife.secretsociety;

import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class InitiateCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "initiate";
    }

    @Override
    public void run(CommandSource source, String[] args) {
        ServerPlayerEntity player = asPlayer(source);
        if (args.length < 1) return;
        if (args[0].equalsIgnoreCase("initiate")) {
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
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (o instanceof Command) {
            return this.getName().compareTo(((Command) o).getName());
        }
        return 0;
    }
}

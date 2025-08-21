package net.mat0u5.pastlife.secretsociety;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.exception.PlayerNotFoundException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class InitiateCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "initiate";
    }

    @Override
    public String getUsage(CommandSource source) {
        return "";
    }

    @Override
    public void run(MinecraftServer server, CommandSource source, String[] args) throws PlayerNotFoundException {
        ServerPlayerEntity player = asPlayer(source);
        if (SecretSociety.members.contains(player.getUuid())) {
            if (SecretSociety.yetToInitiate.contains(player.getUuid())) {
                SecretSociety.initiatePlayer(player);
            }
            else {
                source.sendMessage(new LiteralText("§cYou have already been initiated."));
                source.sendMessage(new LiteralText("§7Find the other members with the secret word: §d\""+SecretSociety.secretWord+"\""));
            }
        }
        else {
            source.sendMessage(new LiteralText("§cYou are not a Member, you cannot use this command."));
        }
    }

    @Override
    public boolean canUse(MinecraftServer server, CommandSource source) {
        return true;
    }
}

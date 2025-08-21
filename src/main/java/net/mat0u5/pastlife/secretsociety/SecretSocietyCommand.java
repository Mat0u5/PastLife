package net.mat0u5.pastlife.secretsociety;

import net.mat0u5.pastlife.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.exception.PlayerNotFoundException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

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
    public boolean canUse(MinecraftServer server, CommandSource source) {
        return true;
    }

    @Override
    public void run(MinecraftServer server, CommandSource source, String[] args) throws PlayerNotFoundException {
        ServerPlayerEntity player = asPlayer(source);
        if (args.length < 1) {
            sendUsageInfo(source);
            return;
        }

        if (args[0].equalsIgnoreCase("begin")) {
            if (server.getPlayerManager().isOp(player.getGameProfile())) {
                if (args.length >= 2) {
                    SecretSociety.beginSociety(server, args[1]);
                }
                else {
                    SecretSociety.beginSociety(server);
                }
            }
            else {
                source.sendMessage(new LiteralText("§cYou do not have permission to use this command."));
            }
            return;
        }
        if (args[0].equalsIgnoreCase("success")) {
            if (SecretSociety.members.contains(player.getUuid())) {
                if (SecretSociety.yetToInitiate.contains(player.getUuid())) {
                    source.sendMessage(new LiteralText("§cYou have not been initiated."));
                }
                else {
                    if (SecretSociety.ended) {
                        source.sendMessage(new LiteralText("§cThe society has already ended."));
                    }
                    else {
                        Main.log(player.getName()+" ran the '/society success' command");
                        SecretSociety.end(server, true);
                    }
                }
            }
            else {
                source.sendMessage(new LiteralText("§cYou are not a Member, you cannot use this command."));
            }
            return;
        }
        if (args[0].equalsIgnoreCase("fail")) {
            if (SecretSociety.members.contains(player.getUuid())) {
                if (SecretSociety.yetToInitiate.contains(player.getUuid())) {
                    source.sendMessage(new LiteralText("§cYou have not been initiated."));
                }
                else {
                    if (SecretSociety.ended) {
                        source.sendMessage(new LiteralText("§cThe society has already ended."));
                    }
                    else {
                        Main.log(player.getName()+" ran the '/society fail' command");
                        SecretSociety.end(server, false);
                    }
                }
            }
            else {
                source.sendMessage(new LiteralText("§cYou are not a Member, you cannot use this command."));
            }
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
            return suggestMatching(args, new String[]{"begin", "success", "fail"});
        }
        return null;
    }
}

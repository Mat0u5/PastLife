package net.mat0u5.pastlife.boogeyman;

import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.exception.PlayerNotFoundException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BoogeymanCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "boogeyman";
    }

    @Override
    public String getUsage(CommandSource source) {
        return "/boogeyman usage:\n§7For admins: '/boogeyman choose'"+"\n"+"For boogeymen: '/boogeyman succeed|fail'";
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
        if (server == null) return;
        if (args[0].equalsIgnoreCase("choose")) {
            if (server.getPlayerManager().isOp(player.getGameProfile())) {
                BoogeymanManager.rollBoogeymen(server);
            }
            else {
                source.sendMessage(new LiteralText("§cYou do not have permission to use this command."));
            }
            return;
        }
        if (args[0].equalsIgnoreCase("succeed")) {
            if (BoogeymanManager.boogeymen.contains(player.getUuid())) {
                BoogeymanManager.boogeymen.remove(player.getUuid());
                PlayerUtils.playSoundToPlayer(player, "boogeyman_cure", 1, 1);
                PlayerUtils.sendTitleToPlayer(player, "§aYou are cured!", 20, 30, 20););
                PlayerUtils.broadcast(player.getName() + "§7 has been cured of the Boogeyman curse!");
                Main.log(player.getName() + " has been cured of the Boogeyman curse!");
            }
            else {
                source.sendMessage(new LiteralText("§cYou are not a Boogeyman, you cannot use this command."));
            }
            return;
        }
        if (args[0].equalsIgnoreCase("fail")) {
            if (BoogeymanManager.boogeymen.contains(player.getUuid())) {
                BoogeymanManager.boogeymen.remove(player.getUuid());
                PlayerUtils.playSoundToPlayer(player, "boogeyman_fail", 1, 1);
                PlayerUtils.sendTitleToPlayer(player, "§cYou have failed.", 20, 30, 20);
                PlayerUtils.broadcast(player.getName() + "§7 failed to kill a player while being the §cBoogeyman§7.");
                PlayerUtils.broadcast("§7They have been dropped to their §cLast Life§7.");
                Main.log(player.getName() + " failed to kill a player while being the Boogeyman.");
                if (Main.livesManager != null && Main.livesManager.getLives(player) > 1) {
                    Main.livesManager.setLives(player, 1);
                }
            }
            else {
                source.sendMessage(new LiteralText("§cYou are not a Boogeyman, you cannot use this command."));
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
            return suggestMatching(args, new String[]{"choose", "succeed", "fail"});
        }
        return null;
    }
}

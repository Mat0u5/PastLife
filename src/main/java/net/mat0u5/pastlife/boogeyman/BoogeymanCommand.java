package net.mat0u5.pastlife.boogeyman;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.utils.PlayerUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.literal;

public class BoogeymanCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean b) {
        dispatcher.register(
            literal("boogeyman")
                .then(literal("choose")
                    .requires(PlayerUtils::isAdmin)
                    .executes(context -> choose(context.getSource()))
                )
                .then(literal("success")
                    .executes(context -> success(context.getSource()))
                )
                .then(literal("fail")
                    .executes(context -> fail(context.getSource()))
                )
        );
    }
    public static int choose(ServerCommandSource source) {
        BoogeymanManager.rollBoogeymen(source.getServer());
        return 1;
    }
    public static int success(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();

        if (!BoogeymanManager.boogeymen.contains(player.getUuid())) {
            source.sendError(Text.of("§cYou are not a Boogeyman, you cannot use this command."));
            return -1;
        }

        BoogeymanManager.boogeymen.remove(player.getUuid());
        PlayerUtils.playSoundToPlayer(player, new SoundEvent(new Identifier("pastlife_boogeyman_cure")), 1, 1);
        PlayerUtils.sendTitleToPlayer(player, "§aYou are cured!", 20, 30, 20);
        PlayerUtils.broadcast(player.getEntityName() + "§7 has been cured of the Boogeyman curse!");
        Main.log(player.getEntityName() + " has been cured of the Boogeyman curse!");
        return 1;
    }
    public static int fail(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();

        if (!BoogeymanManager.boogeymen.contains(player.getUuid())) {
            source.sendError(Text.of("§cYou are not a Boogeyman, you cannot use this command."));
            return -1;
        }

        BoogeymanManager.boogeymen.remove(player.getUuid());
        PlayerUtils.playSoundToPlayer(player, new SoundEvent(new Identifier("pastlife_boogeyman_fail")), 1, 1);
        PlayerUtils.sendTitleToPlayer(player, "§cYou have failed.", 20, 30, 20);
        PlayerUtils.broadcast(player.getEntityName() + "§7 failed to kill a player while being the §cBoogeyman§7.");
        PlayerUtils.broadcast("§7They have been dropped to their §cLast Life§7.");
        Main.log(player.getEntityName() + " failed to kill a player while being the Boogeyman.");
        if (Main.livesManager != null && Main.livesManager.getLives(player) > 1) {
            Main.livesManager.setLives(player, 1);
        }
        return 1;
    }
}

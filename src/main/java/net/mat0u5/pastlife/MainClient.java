package net.mat0u5.pastlife;

import net.fabricmc.api.ClientModInitializer;
import net.mat0u5.pastlife.utils.IClientHelper;
import net.minecraft.client.MinecraftClient;

import java.util.UUID;

public class MainClient implements ClientModInitializer, IClientHelper {
    @Override
    public void onInitializeClient() {
        Main.log("[CLIENT] Initializing Past Life!");
        Main.setClientHelper(this);
    }

    @Override
    public boolean isRunningIntegratedServer() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return false;
        return client.isIntegratedServerRunning();
    }

    @Override
    public boolean isMainClientPlayer(UUID uuid) {
        return isClientPlayer(uuid);
    }

    public static boolean isClientPlayer(UUID uuid) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return false;
        if (client.player == null) return false;
        return client.player.getUuid().equals(uuid);
    }
}

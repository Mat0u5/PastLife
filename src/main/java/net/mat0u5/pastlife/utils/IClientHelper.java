package net.mat0u5.pastlife.utils;

import java.util.UUID;

public interface IClientHelper {
    boolean isRunningIntegratedServer();
    boolean isMainClientPlayer(UUID uuid);
}

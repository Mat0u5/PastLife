package net.mat0u5.pastlife.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.mat0u5.pastlife.interfaces.IPlayerEntity;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayerUtils {
    public static void loadUUID(IPlayerEntity accessor) {
        String playerName = accessor.getName();
        if (playerName == null) {
            return;
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/"+playerName).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                String uuidStr = json.get("id").getAsString();
                accessor.setUUID(uuidStr);
            } else {
                System.out.println("Failed to fetch player UUID: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            System.out.println("Error while checking for player UUID: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}

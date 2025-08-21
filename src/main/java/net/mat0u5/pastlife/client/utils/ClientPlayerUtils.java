package net.mat0u5.pastlife.client.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.mat0u5.pastlife.Main;
import net.mat0u5.pastlife.client.interfaces.IPlayerEntity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClientPlayerUtils {

    public static void loadUUID(IPlayerEntity accessor) {
        String playerName = accessor.getName();
        if (playerName == null || playerName.trim().isEmpty()) {
            return;
        }

        String[] apiEndpoints = {
                "http://api.mojang.com/users/profiles/minecraft/" + playerName.trim(),
                "https://api.mojang.com/users/profiles/minecraft/" + playerName.trim()
        };

        for (String endpoint : apiEndpoints) {
            if (tryLoadUUIDFromEndpoint(accessor, playerName, endpoint)) {
                return;
            }
        }

        Main.log("Official API endpoints failed for player: " + playerName);
    }

    private static boolean tryLoadUUIDFromEndpoint(IPlayerEntity accessor, String playerName, String apiUrl) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Minecraft-Beta-Mod/1.0");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setUseCaches(false);

            connection.setInstanceFollowRedirects(false);

            int responseCode = connection.getResponseCode();

            if (responseCode == 307 || responseCode == 301 || responseCode == 302) {
                String redirectUrl = connection.getHeaderField("Location");
                if (redirectUrl != null && redirectUrl.startsWith("http://")) {
                    Main.log("Following HTTP redirect for: " + playerName);
                    connection.disconnect();
                    return tryLoadUUIDFromEndpoint(accessor, playerName, redirectUrl);
                } else {
                    Main.log("Skipping HTTPS redirect for: " + playerName);
                    return false;
                }
            }

            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(response.toString()).getAsJsonObject();

                if (json.has("id") && !json.get("id").isJsonNull()) {
                    String uuidStr = json.get("id").getAsString();
                    accessor.setUUID(uuidStr);
                    Main.log("Successfully loaded UUID for player: " + playerName);
                    return true;
                } else {
                    Main.log("No UUID found for player: " + playerName);
                    return false;
                }

            } else if (responseCode == 204) {
                Main.log("Player does not exist: " + playerName);
                return false;
            } else if (responseCode == 404) {
                Main.log("Player not found: " + playerName);
                return false;
            } else if (responseCode == 429) {
                Main.log("Rate limited by Mojang API. Try again later.");
                return false;
            } else {
                Main.log("API endpoint " + apiUrl + " returned error code: " + responseCode);
                return false;
            }

        } catch (java.net.UnknownHostException e) {
            Main.log("No internet connection or DNS resolution failed");
            return false;
        } catch (java.net.SocketTimeoutException e) {
            Main.log("Connection timeout for endpoint: " + apiUrl);
            return false;
        } catch (Exception e) {
            Main.log("Error with endpoint " + apiUrl + ": " + e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void loadUUIDAlternative(IPlayerEntity accessor) {
        String playerName = accessor.getName();
        if (playerName == null || playerName.trim().isEmpty()) {
            return;
        }

        String[] altServices = {
                "https://api.minetools.eu/uuid/" + playerName.trim(),
                "https://api.ashcon.app/mojang/v2/user/" + playerName.trim(),
                "https://playerdb.co/api/player/minecraft/" + playerName.trim()
        };

        for (String serviceUrl : altServices) {
            if (tryAlternativeService(accessor, playerName, serviceUrl)) {
                return;
            }
        }

        Main.log("All alternative services failed for: " + playerName);
    }

    private static boolean tryAlternativeService(IPlayerEntity accessor, String playerName, String serviceUrl) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(serviceUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Minecraft-Beta-Mod/1.0");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setUseCaches(false);

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(response.toString()).getAsJsonObject();

                String uuidStr = extractUUIDFromResponse(json, serviceUrl);
                if (uuidStr != null && !uuidStr.isEmpty()) {
                    accessor.setUUID(uuidStr);
                    Main.log("Successfully loaded UUID via alternative service for: " + playerName);
                    return true;
                }
            }

        } catch (Exception e) {
            Main.log("Alternative service " + serviceUrl + " failed: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }

    private static String extractUUIDFromResponse(JsonObject json, String serviceUrl) {
        try {
            if (serviceUrl.contains("minetools.eu")) {
                if (json.has("id")) {
                    return json.get("id").getAsString().replace("-", "");
                }
            } else if (serviceUrl.contains("ashcon.app")) {
                if (json.has("uuid")) {
                    return json.get("uuid").getAsString().replace("-", "");
                }
            } else if (serviceUrl.contains("playerdb.co")) {
                if (json.has("data") && json.getAsJsonObject("data").has("player")) {
                    JsonObject player = json.getAsJsonObject("data").getAsJsonObject("player");
                    if (player.has("id")) {
                        return player.get("id").getAsString().replace("-", "");
                    }
                }
            }
        } catch (Exception e) {
            Main.log("Error parsing response from " + serviceUrl + ": " + e.getMessage());
        }
        return null;
    }

    public static void loadUUIDManual(IPlayerEntity accessor) {
        String playerName = accessor.getName();
        if (playerName == null) return;

        Main.log("Could not fetch UUID for " + playerName + " - using offline mode UUID");

        // Generate offline mode UUID (deterministic based on username)
        String offlineUUID = generateOfflineUUID(playerName);
        accessor.setUUID(offlineUUID);
    }

    private static String generateOfflineUUID(String username) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(("OfflinePlayer:" + username).getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (Exception e) {
            return Integer.toHexString(username.hashCode()) + "000000000000000000000000";
        }
    }

    public static void loadUUIDWithAllMethods(IPlayerEntity accessor) {
        String playerName = accessor.getName();
        if (playerName == null || playerName.trim().isEmpty()) {
            return;
        }

        Main.log("Attempting to load UUID for: " + playerName);

        loadUUID(accessor);
        if (accessor.getUUID() != null && !accessor.getUUID().isEmpty()) {
            return;
        }

        Main.log("Trying alternative UUID service...");
        loadUUIDAlternative(accessor);
        if (accessor.getUUID() != null && !accessor.getUUID().isEmpty()) {
            return;
        }

        Main.log("Generating offline UUID as fallback...");
        loadUUIDManual(accessor);
    }
}
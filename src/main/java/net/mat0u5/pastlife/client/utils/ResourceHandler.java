package net.mat0u5.pastlife.client.utils;

import net.mat0u5.pastlife.Main;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ResourceHandler {
    public void copyBundledSingleFile(String resourcePath, Path targetFile) {
        try {
            // Ensure the target folder exists
            Files.createDirectories(targetFile.getParent());

            // Access the resource file as a URL
            URL resourceUrl = getClass().getResource(resourcePath);

            if (resourceUrl == null) {
                Main.error("File not found: " + resourcePath);
                return;
            }

            // Check the protocol to determine if we are inside a JAR or in a file system
            if (resourceUrl.getProtocol().equals("file")) {
                handleSingleFileNormal(targetFile, resourceUrl);
            }
            else if (resourceUrl.getProtocol().equals("jar")) {
                handleSingleFileJar(targetFile, resourcePath);
            }
            else {
                Main.error("Unsupported resource protocol: " + resourceUrl.getProtocol());
            }
        } catch (Exception e) {
            Main.error("Error copying bundled file: " + resourcePath + "_" + e);
        }
    }

    private void handleSingleFileNormal(Path targetFile, URL resourceUrl) {
        try {
            // Running in development or where resources are on the file system
            Path sourcePath = Paths.get(resourceUrl.toURI());

            // Copy the file directly from the file system
            if (Files.isRegularFile(sourcePath)) {
                // Copy the file to the target location
                Files.copy(sourcePath, targetFile, StandardCopyOption.REPLACE_EXISTING);
                Main.log("Copied file: " + sourcePath + " -> " + targetFile);
            } else {
                Main.error("Source is not a regular file: " + sourcePath);
            }
        } catch (Exception e) {
            Main.error("Error copying bundled file." + e);
        }
    }

    private void handleSingleFileJar(Path targetFile, String resourcePath) {
        try {
            // When running from JAR, we can use InputStream method
            try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
                if (in == null) {
                    Main.error("Could not find resource: " + resourcePath);
                    return;
                }

                // Copy from the input stream to the target file
                Files.copy(in, targetFile, StandardCopyOption.REPLACE_EXISTING);
                Main.log("Copied file from JAR: " + resourcePath + " -> " + targetFile);
            }
        } catch (Exception e) {
            Main.error("Error copying file from JAR: " + resourcePath + "_"+ e);
        }
    }
}

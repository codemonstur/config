package config;

import java.io.IOException;
import java.util.jar.Attributes;

import static java.util.jar.JarFile.MANIFEST_NAME;

public enum Manifest {;

    private static java.util.jar.Manifest instance;
    public static java.util.jar.Manifest loadManifest() {
        if (instance == null) {
            try (final var in = Manifest.class.getResourceAsStream("/"+MANIFEST_NAME)) {
                instance = new java.util.jar.Manifest(in);
            } catch (final IOException e) {
                throw new IllegalArgumentException("Failed to load manifest", e);
            }
        }
        return instance;
    }

    public static String getManifestValue(final String key) {
        final java.util.jar.Manifest manifest = loadManifest();
        if (manifest == null) return null;
        final Attributes attributes = manifest.getMainAttributes();
        if (attributes == null || attributes.isEmpty()) return null;
        final String value = attributes.getValue(key);
        return value == null || value.isEmpty() ? null : value;
    }

}

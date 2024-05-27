package config;

import config.Config.ConfigKey;

import java.io.IOException;
import java.util.*;

import static java.lang.String.join;

public final class ConfigBuilder {

    private final Map<String, String> conf = new HashMap<>();
    private final Set<String> mandatoryFields = new HashSet<>();

    public ConfigBuilder loadEnvironment() {
        conf.putAll(System.getenv());
        return this;
    }

    public ConfigBuilder loadManifest() throws IOException {
        return loadManifest(Manifest.loadManifest());
    }
    public ConfigBuilder loadManifest(final java.util.jar.Manifest manifest) {
        manifest.getMainAttributes().forEach((key, value) -> conf.put(key.toString(), value.toString()));
        return this;
    }

    public ConfigBuilder loadClasspathResource(final String resourcePath) throws IOException {
        final var properties = new Properties();
        try (final var in = ConfigBuilder.class.getResourceAsStream(resourcePath)) {
            properties.load(in);
        }
        properties.forEach((key, value) -> conf.put(key.toString(), value.toString()));
        return this;
    }

    public String get(final ConfigKey key) {
        return get(key.toString());
    }
    public String get(final String key) {
        return conf.get(key);
    }

    public ConfigBuilder put(final ConfigKey key, final String value) {
        return put(key.toString(), value);
    }
    public ConfigBuilder put(final String key, final String value) {
        conf.put(key, value);
        return this;
    }

    public ConfigBuilder put(final ConfigKey key, final int value) {
        return put(key.toString(), Integer.toString(value));
    }
    public ConfigBuilder putIfAbsent(final ConfigKey key, final String value) {
        conf.putIfAbsent(key.toString(), value);
        return this;
    }
    public ConfigBuilder put(final String key, final int value) {
        conf.put(key, Integer.toString(value));
        return this;
    }
    public ConfigBuilder putIfAbsent(final String key, final String value) {
        conf.putIfAbsent(key, value);
        return this;
    }

    public ConfigBuilder put(final Map<String, String> map) {
        conf.putAll(map);
        return this;
    }


    public ConfigBuilder mandatoryFields(final ConfigKey... keys) {
        for (final var key : keys) mandatoryFields.add(key.toString());
        return this;
    }
    public ConfigBuilder mandatoryFields(final String... keys) {
        mandatoryFields.addAll(Arrays.asList(keys));
        return this;
    }
    public ConfigBuilder mandatoryFields(final Collection<? extends String> keys) {
        mandatoryFields.addAll(keys);
        return this;
    }

    public Config build() {
        validate();
        return conf::getOrDefault;
    }

    private void validate() {
        final var missingValues = new LinkedList<String>();
        for (final var key : mandatoryFields) {
            if (!conf.containsKey(key)) missingValues.add(key);
        }
        if (!missingValues.isEmpty())
            throw new MissingConfigurationSetting("Missing configuration settings: " + join(", ", missingValues));
    }

}

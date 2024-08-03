package config;

import config.Config.ConfigKey;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

public enum ConfigParser {;

    public static String getMandatoryString(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        return value;
    }
    public static String getOptionalString(final Config config, final ConfigKey key, final String defaultValue) {
        final String value = config.cnf(key.toString(), null);
        return isNullOrEmpty(value) ? defaultValue : value;
    }


    public static URI getMandatoryURI(final Config config, final ConfigKey key)
            throws MissingConfigurationSetting, URISyntaxException {
        return new URI(getMandatoryString(config, key));
    }
    public static URI getOptionalURI(final Config config, final ConfigKey key, final URI defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;

        try { return new URI(value); }
        catch (final URISyntaxException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a URI", e);
        }
    }

    public static int getMandatoryInteger(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        return Integer.parseInt(value);
    }
    public static int getOptionalInteger(final Config config, final ConfigKey key, final int defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;

        try { return Integer.parseInt(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "integer", e);
        }
    }

    public static Path getMandatoryPath(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        return Paths.get(value);
    }
    public static Path getOptionalPath(final Config config, final ConfigKey key, final Path defaultValue) {
        final String value = config.cnf(key.toString(), null);
        return isNullOrEmpty(value) ? defaultValue : Paths.get(value);
    }

    public static double getMandatoryDouble(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);

        try { return Double.parseDouble(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "double", e);
        }
    }
    public static double getOptionalDouble(final Config config, final ConfigKey key, final double defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;

        try { return Double.parseDouble(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "double", e);
        }
    }

    public static boolean getMandatoryBoolean(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        return parseBoolean(value, key);
    }
    public static boolean getOptionalBoolean(final Config config, final ConfigKey key, final boolean defaultValue) {
        final String value = config.cnf(key.toString(), null);
        return isNullOrEmpty(value) ? defaultValue : parseBoolean(value, key);
    }
    private static boolean parseBoolean(final String input, final ConfigKey key) {
        if ("true".equalsIgnoreCase(input)) return true;
        if ("false".equalsIgnoreCase(input)) return false;
        throw new InvalidConfigurationSetting(format("Configuration setting %s must contain 'true' or 'false'", key));
    }

    public static long getMandatoryLong(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);

        try { return Long.parseLong(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "long", e);
        }
    }
    public static long getOptionalLong(final Config config, final ConfigKey key, final long defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;

        try { return Long.parseLong(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "long", e);
        }
    }

    public static Set<String> getMandatorySet(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) throw new MissingConfigurationSetting(key);
        return commaSeparatedListToSet(value);
    }
    public static Set<String> getOptionalSet(final Config config, final ConfigKey key, final Set<String> defaultValue) {
        final String value = config.cnf(key.toString(), null);
        return isNullOrEmpty(value) ? defaultValue : commaSeparatedListToSet(value);
    }

    private static boolean isNullOrEmpty(final String value) {
        return value == null || value.isEmpty();
    }
    private static Set<String> commaSeparatedListToSet(final String list) {
        return Arrays
            .stream(list.split("\\s"))
            .filter(Objects::nonNull)
            .filter(s -> !s.trim().isEmpty())
            .collect(Collectors.toSet());
    }

}

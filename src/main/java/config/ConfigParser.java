package config;

import config.Config.ConfigKey;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

public enum ConfigParser {;

    public static String getMandatoryString(final Config config, final ConfigKey var) throws MissingConfigurationSetting {
        return getMandatoryString(config, var.toString());
    }
    public static String getMandatoryString(final Config config, final String var) throws MissingConfigurationSetting {
        final String value = config.cnf(var, null);
        if (isNullOrEmpty(value)) throw new MissingConfigurationSetting("Configuration setting " + var + " must be set");
        return value;
    }

    public static URI getMandatoryURI(final Config config, final ConfigKey key)
            throws MissingConfigurationSetting, URISyntaxException {
        return new URI(getMandatoryString(config, key));
    }

    public static String getOptionalString(final Config config, final ConfigKey key, final String defaultValue) {
        final String value = config.cnf(key.toString(), null);
        return isNullOrEmpty(value) ? defaultValue : value;
    }

    public static int getMandatoryInteger(final Config config, final ConfigKey key) throws MissingConfigurationSetting {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        return Integer.parseInt(value);
    }
    public static int getOptionalInteger(final Config config, final ConfigKey key, final int defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;

        try { return Integer.parseInt(value); }
        catch (final NumberFormatException e) {
            throw new IllegalArgumentException(format("Configuration setting %s must contain an integer", key));
        }
    }

    public static boolean getMandatoryBoolean(final Config config, final ConfigKey key) throws MissingConfigurationSetting {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        return parseBoolean(value, key);
    }
    public static boolean getOptionalBoolean(final Config config, final ConfigKey key, final boolean defaultValue) {
        final String value = config.cnf(key.toString(), null);
        return isNullOrEmpty(value) ? defaultValue : parseBoolean(value, key);
    }
    private static boolean parseBoolean(final String input, final ConfigKey key) {
        if ("true".equalsIgnoreCase(input)) return true;
        if ("false".equalsIgnoreCase(input)) return false;
        throw new IllegalArgumentException(format("Configuration setting %s must contain 'true' or 'false'", key));
    }

    public static long getOptionalLong(final Config config, final ConfigKey key, final long defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;

        try { return Long.parseLong(value); }
        catch (final NumberFormatException e) {
            throw new IllegalArgumentException(format("Configuration setting %s must contain a long", key));
        }
    }

    public static Set<String> getMandatorySet(final Config config, final ConfigKey key) throws MissingConfigurationSetting {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        return commaSeparatedListToSet(value);
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

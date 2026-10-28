package config;

import config.Config.ConfigKey;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

public enum ConfigParser {;

    // presence
    public static boolean has(final Config config, final ConfigKey key) {
        return config.containsKey(key);
    }
    public static boolean has(final Config config, final String key) {
        return config.containsKey(key);
    }

    // String
    public static String getMandatoryString(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) throw new MissingConfigurationSetting(key);
        return value;
    }
    public static String getMandatoryString(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        return value;
    }
    public static String getOptionalString(final Config config, final ConfigKey key, final String defaultValue) {
        final String value = config.cnf(key.toString(), null);
        return isNullOrEmpty(value) ? defaultValue : value;
    }
    public static String getOptionalString(final Config config, final String key, final String defaultValue) {
        final String value = config.cnf(key, null);
        return isNullOrEmpty(value) ? defaultValue : value;
    }

    // URI
    public static URI getMandatoryURI(final Config config, final ConfigKey key) throws URISyntaxException {
        try {
            return new URI(getMandatoryString(config, key));
        } catch (final URISyntaxException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a URI", e);
        }
    }
    public static URI getMandatoryURI(final Config config, final String key) throws URISyntaxException {
        try {
            return new URI(getMandatoryString(config, key));
        } catch (final URISyntaxException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a URI", e);
        }
    }
    public static URI getOptionalURI(final Config config, final ConfigKey key, final URI defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return new URI(value); }
        catch (final URISyntaxException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a URI", e);
        }
    }
    public static URI getOptionalURI(final Config config, final String key, final URI defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return new URI(value); }
        catch (final URISyntaxException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a URI", e);
        }
    }

    // URL
    public static URL getMandatoryURL(final Config config, final ConfigKey key) {
        final String value = getMandatoryString(config, key);
        try {
            return URI.create(value).toURL();
        } catch (final IllegalArgumentException | java.net.MalformedURLException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a URL", e);
        }
    }
    public static URL getMandatoryURL(final Config config, final String key) {
        final String value = getMandatoryString(config, key);
        try {
            return URI.create(value).toURL();
        } catch (final IllegalArgumentException | java.net.MalformedURLException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a URL", e);
        }
    }
    public static URL getOptionalURL(final Config config, final ConfigKey key, final URL defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return URI.create(value).toURL(); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a URL", e);
        }
    }
    public static URL getOptionalURL(final Config config, final String key, final URL defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return URI.create(value).toURL(); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a URL", e);
        }
    }

    // Integer
    public static int getMandatoryInteger(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return Integer.parseInt(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "integer", e);
        }
    }
    public static int getMandatoryInteger(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return Integer.parseInt(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain an integer", e);
        }
    }
    public static int getOptionalInteger(final Config config, final ConfigKey key, final int defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Integer.parseInt(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "integer", e);
        }
    }
    public static int getOptionalInteger(final Config config, final String key, final int defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Integer.parseInt(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain an integer", e);
        }
    }

    // Short
    public static short getMandatoryShort(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return Short.parseShort(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "short", e);
        }
    }
    public static short getMandatoryShort(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return Short.parseShort(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a short", e);
        }
    }
    public static short getOptionalShort(final Config config, final ConfigKey key, final short defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Short.parseShort(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "short", e);
        }
    }
    public static short getOptionalShort(final Config config, final String key, final short defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Short.parseShort(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a short", e);
        }
    }

    // Byte
    public static byte getMandatoryByte(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return Byte.parseByte(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "byte", e);
        }
    }
    public static byte getMandatoryByte(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return Byte.parseByte(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a byte", e);
        }
    }
    public static byte getOptionalByte(final Config config, final ConfigKey key, final byte defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Byte.parseByte(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "byte", e);
        }
    }
    public static byte getOptionalByte(final Config config, final String key, final byte defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Byte.parseByte(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a byte", e);
        }
    }

    // Float
    public static float getMandatoryFloat(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return Float.parseFloat(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "float", e);
        }
    }
    public static float getMandatoryFloat(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return Float.parseFloat(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a float", e);
        }
    }
    public static float getOptionalFloat(final Config config, final ConfigKey key, final float defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Float.parseFloat(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "float", e);
        }
    }
    public static float getOptionalFloat(final Config config, final String key, final float defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Float.parseFloat(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a float", e);
        }
    }

    // Double
    public static double getMandatoryDouble(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return Double.parseDouble(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "double", e);
        }
    }
    public static double getMandatoryDouble(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return Double.parseDouble(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a double", e);
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
    public static double getOptionalDouble(final Config config, final String key, final double defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Double.parseDouble(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a double", e);
        }
    }

    // Long
    public static long getMandatoryLong(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return Long.parseLong(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "long", e);
        }
    }
    public static long getMandatoryLong(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return Long.parseLong(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a long", e);
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
    public static long getOptionalLong(final Config config, final String key, final long defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Long.parseLong(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a long", e);
        }
    }

    // Boolean
    public static boolean getMandatoryBoolean(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        return parseBoolean(value, key);
    }
    public static boolean getMandatoryBoolean(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        return parseBoolean(value, key);
    }
    public static boolean getOptionalBoolean(final Config config, final ConfigKey key, final boolean defaultValue) {
        final String value = config.cnf(key.toString(), null);
        return isNullOrEmpty(value) ? defaultValue : parseBoolean(value, key);
    }
    public static boolean getOptionalBoolean(final Config config, final String key, final boolean defaultValue) {
        final String value = config.cnf(key, null);
        return isNullOrEmpty(value) ? defaultValue : parseBoolean(value, key);
    }
    private static boolean parseBoolean(final String input, final Object key) {
        if ("true".equalsIgnoreCase(input)) return true;
        if ("false".equalsIgnoreCase(input)) return false;
        throw new InvalidConfigurationSetting(format("Configuration setting %s must contain 'true' or 'false'", key));
    }

    // Path
    public static Path getMandatoryPath(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        return Paths.get(value);
    }
    public static Path getMandatoryPath(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        return Paths.get(value);
    }
    public static Path getOptionalPath(final Config config, final ConfigKey key, final Path defaultValue) {
        final String value = config.cnf(key.toString(), null);
        return isNullOrEmpty(value) ? defaultValue : Paths.get(value);
    }
    public static Path getOptionalPath(final Config config, final String key, final Path defaultValue) {
        final String value = config.cnf(key, null);
        return isNullOrEmpty(value) ? defaultValue : Paths.get(value);
    }

    // File
    public static File getMandatoryFile(final Config config, final ConfigKey key) {
        return new File(getMandatoryString(config, key));
    }
    public static File getMandatoryFile(final Config config, final String key) {
        return new File(getMandatoryString(config, key));
    }
    public static File getOptionalFile(final Config config, final ConfigKey key, final File defaultValue) {
        final String value = config.cnf(key.toString(), null);
        return isNullOrEmpty(value) ? defaultValue : new File(value);
    }
    public static File getOptionalFile(final Config config, final String key, final File defaultValue) {
        final String value = config.cnf(key, null);
        return isNullOrEmpty(value) ? defaultValue : new File(value);
    }

    // BigInteger
    public static BigInteger getMandatoryBigInteger(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return new BigInteger(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "BigInteger", e);
        }
    }
    public static BigInteger getMandatoryBigInteger(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return new BigInteger(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a BigInteger", e);
        }
    }
    public static BigInteger getOptionalBigInteger(final Config config, final ConfigKey key, final BigInteger defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return new BigInteger(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "BigInteger", e);
        }
    }
    public static BigInteger getOptionalBigInteger(final Config config, final String key, final BigInteger defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return new BigInteger(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a BigInteger", e);
        }
    }

    // BigDecimal
    public static BigDecimal getMandatoryBigDecimal(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return new BigDecimal(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "BigDecimal", e);
        }
    }
    public static BigDecimal getMandatoryBigDecimal(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return new BigDecimal(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a BigDecimal", e);
        }
    }
    public static BigDecimal getOptionalBigDecimal(final Config config, final ConfigKey key, final BigDecimal defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return new BigDecimal(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting(key, "BigDecimal", e);
        }
    }
    public static BigDecimal getOptionalBigDecimal(final Config config, final String key, final BigDecimal defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return new BigDecimal(value); }
        catch (final NumberFormatException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a BigDecimal", e);
        }
    }

    // UUID
    public static UUID getMandatoryUUID(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return UUID.fromString(value); }
        catch (final IllegalArgumentException e) {
            throw new InvalidConfigurationSetting(key, "UUID", e);
        }
    }
    public static UUID getMandatoryUUID(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return UUID.fromString(value); }
        catch (final IllegalArgumentException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a UUID", e);
        }
    }
    public static UUID getOptionalUUID(final Config config, final ConfigKey key, final UUID defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return UUID.fromString(value); }
        catch (final IllegalArgumentException e) {
            throw new InvalidConfigurationSetting(key, "UUID", e);
        }
    }
    public static UUID getOptionalUUID(final Config config, final String key, final UUID defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return UUID.fromString(value); }
        catch (final IllegalArgumentException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a UUID", e);
        }
    }

    // InetAddress
    public static InetAddress getMandatoryInetAddress(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return InetAddress.getByName(value); }
        catch (final UnknownHostException e) {
            throw new InvalidConfigurationSetting(key, "InetAddress", e);
        }
    }
    public static InetAddress getMandatoryInetAddress(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return InetAddress.getByName(value); }
        catch (final UnknownHostException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain an InetAddress", e);
        }
    }
    public static InetAddress getOptionalInetAddress(final Config config, final ConfigKey key, final InetAddress defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return InetAddress.getByName(value); }
        catch (final UnknownHostException e) {
            throw new InvalidConfigurationSetting(key, "InetAddress", e);
        }
    }
    public static InetAddress getOptionalInetAddress(final Config config, final String key, final InetAddress defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return InetAddress.getByName(value); }
        catch (final UnknownHostException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain an InetAddress", e);
        }
    }

    // Duration
    public static Duration getMandatoryDuration(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return Duration.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting(key, "Duration", e);
        }
    }
    public static Duration getMandatoryDuration(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return Duration.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a Duration", e);
        }
    }
    public static Duration getOptionalDuration(final Config config, final ConfigKey key, final Duration defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Duration.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting(key, "Duration", e);
        }
    }
    public static Duration getOptionalDuration(final Config config, final String key, final Duration defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Duration.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a Duration", e);
        }
    }

    // Instant
    public static Instant getMandatoryInstant(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return Instant.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting(key, "Instant", e);
        }
    }
    public static Instant getMandatoryInstant(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return Instant.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain an Instant", e);
        }
    }
    public static Instant getOptionalInstant(final Config config, final ConfigKey key, final Instant defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Instant.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting(key, "Instant", e);
        }
    }
    public static Instant getOptionalInstant(final Config config, final String key, final Instant defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Instant.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain an Instant", e);
        }
    }

    // LocalDate
    public static LocalDate getMandatoryLocalDate(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return LocalDate.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting(key, "LocalDate", e);
        }
    }
    public static LocalDate getMandatoryLocalDate(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return LocalDate.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a LocalDate", e);
        }
    }
    public static LocalDate getOptionalLocalDate(final Config config, final ConfigKey key, final LocalDate defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return LocalDate.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting(key, "LocalDate", e);
        }
    }
    public static LocalDate getOptionalLocalDate(final Config config, final String key, final LocalDate defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return LocalDate.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a LocalDate", e);
        }
    }

    // LocalDateTime
    public static LocalDateTime getMandatoryLocalDateTime(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return LocalDateTime.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting(key, "LocalDateTime", e);
        }
    }
    public static LocalDateTime getMandatoryLocalDateTime(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return LocalDateTime.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a LocalDateTime", e);
        }
    }
    public static LocalDateTime getOptionalLocalDateTime(final Config config, final ConfigKey key, final LocalDateTime defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return LocalDateTime.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting(key, "LocalDateTime", e);
        }
    }
    public static LocalDateTime getOptionalLocalDateTime(final Config config, final String key, final LocalDateTime defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return LocalDateTime.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a LocalDateTime", e);
        }
    }

    // LocalTime
    public static LocalTime getMandatoryLocalTime(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return LocalTime.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting(key, "LocalTime", e);
        }
    }
    public static LocalTime getMandatoryLocalTime(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return LocalTime.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a LocalTime", e);
        }
    }
    public static LocalTime getOptionalLocalTime(final Config config, final ConfigKey key, final LocalTime defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return LocalTime.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting(key, "LocalTime", e);
        }
    }
    public static LocalTime getOptionalLocalTime(final Config config, final String key, final LocalTime defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return LocalTime.parse(value); }
        catch (final Exception e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a LocalTime", e);
        }
    }

    // Enum
    public static <E extends Enum<E>> E getMandatoryEnum(final Config config, final ConfigKey key, final Class<E> enumClass) {
        final String value = config.cnf(key.toString(), null);
        if (value == null) throw new MissingConfigurationSetting(key);
        try { return Enum.valueOf(enumClass, value); }
        catch (final IllegalArgumentException e) {
            throw new InvalidConfigurationSetting(key, enumClass.getSimpleName(), e);
        }
    }
    public static <E extends Enum<E>> E getMandatoryEnum(final Config config, final String key, final Class<E> enumClass) {
        final String value = config.cnf(key, null);
        if (value == null) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        try { return Enum.valueOf(enumClass, value); }
        catch (final IllegalArgumentException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a " + enumClass.getSimpleName(), e);
        }
    }
    public static <E extends Enum<E>> E getOptionalEnum(final Config config, final ConfigKey key, final Class<E> enumClass, final E defaultValue) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Enum.valueOf(enumClass, value); }
        catch (final IllegalArgumentException e) {
            throw new InvalidConfigurationSetting(key, enumClass.getSimpleName(), e);
        }
    }
    public static <E extends Enum<E>> E getOptionalEnum(final Config config, final String key, final Class<E> enumClass, final E defaultValue) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) return defaultValue;
        try { return Enum.valueOf(enumClass, value); }
        catch (final IllegalArgumentException e) {
            throw new InvalidConfigurationSetting("Configuration setting " + key + " must contain a " + enumClass.getSimpleName(), e);
        }
    }

    // Set
    public static Set<String> getMandatorySet(final Config config, final ConfigKey key) {
        final String value = config.cnf(key.toString(), null);
        if (isNullOrEmpty(value)) throw new MissingConfigurationSetting(key);
        return commaSeparatedListToSet(value);
    }
    public static Set<String> getMandatorySet(final Config config, final String key) {
        final String value = config.cnf(key, null);
        if (isNullOrEmpty(value)) throw new MissingConfigurationSetting("Configuration setting " + key + " must be set");
        return commaSeparatedListToSet(value);
    }
    public static Set<String> getOptionalSet(final Config config, final ConfigKey key, final Set<String> defaultValue) {
        final String value = config.cnf(key.toString(), null);
        return isNullOrEmpty(value) ? defaultValue : commaSeparatedListToSet(value);
    }
    public static Set<String> getOptionalSet(final Config config, final String key, final Set<String> defaultValue) {
        final String value = config.cnf(key, null);
        return isNullOrEmpty(value) ? defaultValue : commaSeparatedListToSet(value);
    }

    // List<String>
    public static List<String> getMandatoryStringList(final Config config, final ConfigKey key) {
        final String value = getMandatoryString(config, key);
        return commaSeparatedListToList(value);
    }
    public static List<String> getMandatoryStringList(final Config config, final String key) {
        final String value = getMandatoryString(config, key);
        return commaSeparatedListToList(value);
    }
    public static List<String> getOptionalStringList(final Config config, final ConfigKey key, final List<String> defaultValue) {
        final String value = config.cnf(key.toString(), null);
        return isNullOrEmpty(value) ? defaultValue : commaSeparatedListToList(value);
    }
    public static List<String> getOptionalStringList(final Config config, final String key, final List<String> defaultValue) {
        final String value = config.cnf(key, null);
        return isNullOrEmpty(value) ? defaultValue : commaSeparatedListToList(value);
    }

    // List<T> with converter
    public static <T> List<T> getMandatoryList(final Config config, final ConfigKey key, final Function<String, T> converter) {
        return getMandatoryStringList(config, key).stream().map(converter).collect(Collectors.toList());
    }
    public static <T> List<T> getMandatoryList(final Config config, final String key, final Function<String, T> converter) {
        return getMandatoryStringList(config, key).stream().map(converter).collect(Collectors.toList());
    }
    public static <T> List<T> getOptionalList(final Config config, final ConfigKey key, final List<T> defaultValue, final Function<String, T> converter) {
        final List<String> strings = getOptionalStringList(config, key, null);
        if (strings == null) return defaultValue;
        return strings.stream().map(converter).collect(Collectors.toList());
    }
    public static <T> List<T> getOptionalList(final Config config, final String key, final List<T> defaultValue, final Function<String, T> converter) {
        final List<String> strings = getOptionalStringList(config, key, null);
        if (strings == null) return defaultValue;
        return strings.stream().map(converter).collect(Collectors.toList());
    }

    private static boolean isNullOrEmpty(final String value) {
        return value == null || value.isEmpty();
    }
    private static Set<String> commaSeparatedListToSet(final String list) {
        return Arrays
            .stream(list.split("[,\\s]+"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toSet());
    }
    private static List<String> commaSeparatedListToList(final String list) {
        return Arrays
            .stream(list.split("[,\\s]+"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());
    }

}

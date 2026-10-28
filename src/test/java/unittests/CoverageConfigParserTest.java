package unittests;

import config.Config;
import config.ConfigParser;
import config.InvalidConfigurationSetting;
import config.MissingConfigurationSetting;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CoverageConfigParserTest {

    @Test
    public void ShortConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "7").build();
        assertEquals((short) 7, ConfigParser.getMandatoryShort(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryShort(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryShort(Config.newConfig().put(key, "x").build(), key));
        assertEquals((short) 7, ConfigParser.getOptionalShort(c, key, (short) 7));
        assertEquals((short) 7, ConfigParser.getOptionalShort(Config.newConfig().build(), key, (short) 7));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalShort(Config.newConfig().put(key, "x").build(), key, (short) 7));
    }
    @Test
    public void ByteConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "7").build();
        assertEquals((byte) 7, ConfigParser.getMandatoryByte(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryByte(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryByte(Config.newConfig().put(key, "x").build(), key));
        assertEquals((byte) 7, ConfigParser.getOptionalByte(c, key, (byte) 7));
        assertEquals((byte) 7, ConfigParser.getOptionalByte(Config.newConfig().build(), key, (byte) 7));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalByte(Config.newConfig().put(key, "x").build(), key, (byte) 7));
    }
    @Test
    public void FloatConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "1.5").build();
        assertEquals(1.5f, ConfigParser.getMandatoryFloat(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryFloat(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryFloat(Config.newConfig().put(key, "x").build(), key));
        assertEquals(1.5f, ConfigParser.getOptionalFloat(c, key, 2.5f));
        assertEquals(2.5f, ConfigParser.getOptionalFloat(Config.newConfig().build(), key, 2.5f));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalFloat(Config.newConfig().put(key, "x").build(), key, 2.5f));
    }
    @Test
    public void DoubleConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "1.5").build();
        assertEquals(1.5, ConfigParser.getMandatoryDouble(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryDouble(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryDouble(Config.newConfig().put(key, "x").build(), key));
        assertEquals(1.5, ConfigParser.getOptionalDouble(c, key, 2.5));
        assertEquals(2.5, ConfigParser.getOptionalDouble(Config.newConfig().build(), key, 2.5));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalDouble(Config.newConfig().put(key, "x").build(), key, 2.5));
    }
    @Test
    public void LongConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "123").build();
        assertEquals(123L, ConfigParser.getMandatoryLong(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryLong(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryLong(Config.newConfig().put(key, "x").build(), key));
        assertEquals(123L, ConfigParser.getOptionalLong(c, key, 7L));
        assertEquals(7L, ConfigParser.getOptionalLong(Config.newConfig().build(), key, 7L));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalLong(Config.newConfig().put(key, "x").build(), key, 7L));
    }
    @Test
    public void BooleanConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "true").build();
        assertEquals(true, ConfigParser.getMandatoryBoolean(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryBoolean(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryBoolean(Config.newConfig().put(key, "maybe").build(), key));
        assertEquals(true, ConfigParser.getOptionalBoolean(c, key, false));
        assertEquals(false, ConfigParser.getOptionalBoolean(Config.newConfig().build(), key, false));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalBoolean(Config.newConfig().put(key, "maybe").build(), key, false));
    }
    @Test
    public void BigIntegerConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "123").build();
        assertEquals(new BigInteger("123"), ConfigParser.getMandatoryBigInteger(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryBigInteger(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryBigInteger(Config.newConfig().put(key, "x").build(), key));
        assertEquals(new BigInteger("123"), ConfigParser.getOptionalBigInteger(c, key, new BigInteger("9")));
        assertEquals(new BigInteger("9"), ConfigParser.getOptionalBigInteger(Config.newConfig().build(), key, new BigInteger("9")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalBigInteger(Config.newConfig().put(key, "x").build(), key, new BigInteger("9")));
    }
    @Test
    public void BigDecimalConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "1.5").build();
        assertEquals(new BigDecimal("1.5"), ConfigParser.getMandatoryBigDecimal(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryBigDecimal(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryBigDecimal(Config.newConfig().put(key, "x").build(), key));
        assertEquals(new BigDecimal("1.5"), ConfigParser.getOptionalBigDecimal(c, key, new BigDecimal("2.5")));
        assertEquals(new BigDecimal("2.5"), ConfigParser.getOptionalBigDecimal(Config.newConfig().build(), key, new BigDecimal("2.5")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalBigDecimal(Config.newConfig().put(key, "x").build(), key, new BigDecimal("2.5")));
    }
    @Test
    public void DurationConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "PT1H").build();
        assertEquals(Duration.parse("PT1H"), ConfigParser.getMandatoryDuration(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryDuration(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryDuration(Config.newConfig().put(key, "nope").build(), key));
        assertEquals(Duration.parse("PT1H"), ConfigParser.getOptionalDuration(c, key, Duration.parse("PT2H")));
        assertEquals(Duration.parse("PT2H"), ConfigParser.getOptionalDuration(Config.newConfig().build(), key, Duration.parse("PT2H")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalDuration(Config.newConfig().put(key, "nope").build(), key, Duration.parse("PT2H")));
    }
    @Test
    public void InstantConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "2020-01-01T00:00:00Z").build();
        assertEquals(Instant.parse("2020-01-01T00:00:00Z"), ConfigParser.getMandatoryInstant(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryInstant(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryInstant(Config.newConfig().put(key, "nope").build(), key));
        assertEquals(Instant.parse("2020-01-01T00:00:00Z"), ConfigParser.getOptionalInstant(c, key, Instant.parse("2020-01-01T00:00:00Z")));
        assertEquals(Instant.parse("2020-01-01T00:00:00Z"), ConfigParser.getOptionalInstant(Config.newConfig().build(), key, Instant.parse("2020-01-01T00:00:00Z")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalInstant(Config.newConfig().put(key, "nope").build(), key, Instant.parse("2020-01-01T00:00:00Z")));
    }
    @Test
    public void LocalDateConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "2020-01-01").build();
        assertEquals(LocalDate.parse("2020-01-01"), ConfigParser.getMandatoryLocalDate(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryLocalDate(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryLocalDate(Config.newConfig().put(key, "nope").build(), key));
        assertEquals(LocalDate.parse("2020-01-01"), ConfigParser.getOptionalLocalDate(c, key, LocalDate.parse("2020-01-01")));
        assertEquals(LocalDate.parse("2020-01-01"), ConfigParser.getOptionalLocalDate(Config.newConfig().build(), key, LocalDate.parse("2020-01-01")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalLocalDate(Config.newConfig().put(key, "nope").build(), key, LocalDate.parse("2020-01-01")));
    }
    @Test
    public void LocalTimeConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "12:00:00").build();
        assertEquals(LocalTime.parse("12:00:00"), ConfigParser.getMandatoryLocalTime(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryLocalTime(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryLocalTime(Config.newConfig().put(key, "nope").build(), key));
        assertEquals(LocalTime.parse("12:00:00"), ConfigParser.getOptionalLocalTime(c, key, LocalTime.parse("12:00:00")));
        assertEquals(LocalTime.parse("12:00:00"), ConfigParser.getOptionalLocalTime(Config.newConfig().build(), key, LocalTime.parse("12:00:00")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalLocalTime(Config.newConfig().put(key, "nope").build(), key, LocalTime.parse("12:00:00")));
    }
    @Test
    public void LocalDateTimeConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "2020-01-01T12:00:00").build();
        assertEquals(LocalDateTime.parse("2020-01-01T12:00:00"), ConfigParser.getMandatoryLocalDateTime(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryLocalDateTime(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryLocalDateTime(Config.newConfig().put(key, "nope").build(), key));
        assertEquals(LocalDateTime.parse("2020-01-01T12:00:00"), ConfigParser.getOptionalLocalDateTime(c, key, LocalDateTime.parse("2020-01-01T12:00:00")));
        assertEquals(LocalDateTime.parse("2020-01-01T12:00:00"), ConfigParser.getOptionalLocalDateTime(Config.newConfig().build(), key, LocalDateTime.parse("2020-01-01T12:00:00")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalLocalDateTime(Config.newConfig().put(key, "nope").build(), key, LocalDateTime.parse("2020-01-01T12:00:00")));
    }
    @Test
    public void UUIDConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "123e4567-e89b-12d3-a456-426614174000").build();
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), ConfigParser.getMandatoryUUID(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryUUID(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryUUID(Config.newConfig().put(key, "bad").build(), key));
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), ConfigParser.getOptionalUUID(c, key, UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), ConfigParser.getOptionalUUID(Config.newConfig().build(), key, UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalUUID(Config.newConfig().put(key, "bad").build(), key, UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));
    }
    @Test
    public void StringConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "v").build();
        assertEquals("v", ConfigParser.getMandatoryString(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryString(Config.newConfig().build(), key));
        assertEquals("v", ConfigParser.getOptionalString(c, key, "d"));
        assertEquals("d", ConfigParser.getOptionalString(Config.newConfig().build(), key, "d"));
    }
    @Test
    public void PathConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "/tmp/x").build();
        assertEquals(Path.of("/tmp/x"), ConfigParser.getMandatoryPath(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryPath(Config.newConfig().build(), key));
        assertEquals(Path.of("/tmp/x"), ConfigParser.getOptionalPath(c, key, Path.of("/d")));
        assertEquals(Path.of("/d"), ConfigParser.getOptionalPath(Config.newConfig().build(), key, Path.of("/d")));
    }
    @Test
    public void FileConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "/tmp/x").build();
        assertEquals(new File("/tmp/x"), ConfigParser.getMandatoryFile(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryFile(Config.newConfig().build(), key));
        assertEquals(new File("/tmp/x"), ConfigParser.getOptionalFile(c, key, new File("/d")));
        assertEquals(new File("/d"), ConfigParser.getOptionalFile(Config.newConfig().build(), key, new File("/d")));
    }
    @Test
    public void URLConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "http://example.com").build();
        assertEquals(new URL("http://example.com"), ConfigParser.getMandatoryURL(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryURL(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryURL(Config.newConfig().put(key, "http://bad space").build(), key));
        assertEquals(new URL("http://example.com"), ConfigParser.getOptionalURL(c, key, new URL("http://example.com")));
        assertEquals(new URL("http://example.com"), ConfigParser.getOptionalURL(Config.newConfig().build(), key, new URL("http://example.com")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalURL(Config.newConfig().put(key, "http://bad space").build(), key, new URL("http://example.com")));
    }
    @Test
    public void InetAddressConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "localhost").build();
        assertEquals(InetAddress.getByName("localhost"), ConfigParser.getMandatoryInetAddress(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryInetAddress(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryInetAddress(Config.newConfig().put(key, "not a host!!!").build(), key));
        assertEquals(InetAddress.getByName("localhost"), ConfigParser.getOptionalInetAddress(c, key, InetAddress.getByName("localhost")));
        assertEquals(InetAddress.getByName("localhost"), ConfigParser.getOptionalInetAddress(Config.newConfig().build(), key, InetAddress.getByName("localhost")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalInetAddress(Config.newConfig().put(key, "not a host!!!").build(), key, InetAddress.getByName("localhost")));
    }
    @Test
    public void URIConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "http://example.com/api").build();
        assertEquals(URI.create("http://example.com/api"), ConfigParser.getMandatoryURI(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryURI(Config.newConfig().build(), key));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryURI(Config.newConfig().put(key, "http://bad space").build(), key));
        assertEquals(URI.create("http://example.com/api"), ConfigParser.getOptionalURI(c, key, URI.create("http://example.com/api")));
        assertEquals(URI.create("http://example.com/api"), ConfigParser.getOptionalURI(Config.newConfig().build(), key, URI.create("http://example.com/api")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalURI(Config.newConfig().put(key, "http://bad space").build(), key, URI.create("http://example.com/api")));
    }

    @Test
    public void EnumConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "NEW").build();
        assertEquals(Thread.State.NEW, ConfigParser.getMandatoryEnum(c, key, Thread.State.class));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryEnum(Config.newConfig().build(), key, Thread.State.class));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryEnum(Config.newConfig().put(key, "NOPE").build(), key, Thread.State.class));
        assertEquals(Thread.State.NEW, ConfigParser.getOptionalEnum(Config.newConfig().build(), key, Thread.State.class, Thread.State.NEW));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalEnum(Config.newConfig().put(key, "NOPE").build(), key, Thread.State.class, Thread.State.NEW));
    }


    @Test
    public void SetConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "a b,c").build();
        assertEquals(java.util.Set.of("a","b","c"), ConfigParser.getMandatorySet(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatorySet(Config.newConfig().build(), key));
        assertEquals(java.util.Set.of("x"), ConfigParser.getOptionalSet(Config.newConfig().build(), key, java.util.Set.of("x")));
    }


    @Test
    public void StringListConfigKey() throws Exception {
        final var key = Config.newConfigKey("k");
        final var c = Config.newConfig().put(key, "a b,c").build();
        assertEquals(java.util.List.of("a","b","c"), ConfigParser.getMandatoryStringList(c, key));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryStringList(Config.newConfig().build(), key));
        assertEquals(java.util.List.of("x"), ConfigParser.getOptionalStringList(Config.newConfig().build(), key, java.util.List.of("x")));
    }

}

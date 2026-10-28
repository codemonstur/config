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
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigParserExtendedTest {

    private Config cfg(String k, String v) {
        return Config.newConfig().put(k, v).build();
    }

    @Test
    public void has() {
        final var c = cfg("k", "v");
        assertTrue(ConfigParser.has(c, "k"));
        assertFalse(ConfigParser.has(c, "missing"));
    }

    @Test
    public void shortType() {
        assertEquals((short) 7, ConfigParser.getMandatoryShort(cfg("k", "7"), "k"));
        assertEquals((short) 7, ConfigParser.getOptionalShort(cfg("m", (String) null), "m", (short) 7));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryShort(cfg("b", "x"), "b"));
    }

    @Test
    public void byteType() {
        assertEquals((byte) 7, ConfigParser.getMandatoryByte(cfg("k", "7"), "k"));
        assertEquals((byte) 7, ConfigParser.getOptionalByte(cfg("m", (String) null), "m", (byte) 7));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryByte(cfg("b", "x"), "b"));
    }

    @Test
    public void floatType() {
        assertEquals(1.5f, ConfigParser.getMandatoryFloat(cfg("k", "1.5"), "k"));
        assertEquals(2.5f, ConfigParser.getOptionalFloat(cfg("m", (String) null), "m", 2.5f));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryFloat(cfg("b", "x"), "b"));
    }

    @Test
    public void bigInteger() {
        assertEquals(new BigInteger("123"), ConfigParser.getMandatoryBigInteger(cfg("k", "123"), "k"));
        assertEquals(new BigInteger("9"), ConfigParser.getOptionalBigInteger(cfg("m", (String) null), "m", new BigInteger("9")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryBigInteger(cfg("b", "x"), "b"));
    }

    @Test
    public void bigDecimal() {
        assertEquals(new BigDecimal("1.5"), ConfigParser.getMandatoryBigDecimal(cfg("k", "1.5"), "k"));
        assertEquals(new BigDecimal("2.5"), ConfigParser.getOptionalBigDecimal(cfg("m", (String) null), "m", new BigDecimal("2.5")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryBigDecimal(cfg("b", "x"), "b"));
    }

    @Test
    public void uuid() {
        final var u = "123e4567-e89b-12d3-a456-426614174000";
        assertEquals(UUID.fromString(u), ConfigParser.getMandatoryUUID(cfg("k", u), "k"));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryUUID(cfg("b", "not-uuid"), "b"));
    }

    @Test
    public void url() {
        try {
            final var url = new URL("http://example.com");
            assertEquals(url, ConfigParser.getMandatoryURL(cfg("k", "http://example.com"), "k"));
            assertEquals(url, ConfigParser.getOptionalURL(cfg("m", (String) null), "m", url));
            assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryURL(cfg("b", "not a url"), "b"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void file() {
        assertEquals(new File("/tmp/x"), ConfigParser.getMandatoryFile(cfg("k", "/tmp/x"), "k"));
        assertEquals(new File("/default"), ConfigParser.getOptionalFile(cfg("m", (String) null), "m", new File("/default")));
    }

    @Test
    public void inetAddress() throws Exception {
        assertEquals(InetAddress.getByName("localhost"), ConfigParser.getMandatoryInetAddress(cfg("k", "localhost"), "k"));
        assertEquals(InetAddress.getByName("localhost"), ConfigParser.getOptionalInetAddress(cfg("m", (String) null), "m", InetAddress.getByName("localhost")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryInetAddress(cfg("b", "not a host!!!"), "b"));
    }

    @Test
    public void duration() {
        assertEquals(Duration.parse("PT1H"), ConfigParser.getMandatoryDuration(cfg("k", "PT1H"), "k"));
        assertEquals(Duration.parse("PT1H"), ConfigParser.getOptionalDuration(cfg("m", (String) null), "m", Duration.parse("PT1H")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryDuration(cfg("b", "nope"), "b"));
    }

    @Test
    public void instant() {
        final var i = Instant.parse("2020-01-01T00:00:00Z");
        assertEquals(i, ConfigParser.getMandatoryInstant(cfg("k", "2020-01-01T00:00:00Z"), "k"));
        assertEquals(i, ConfigParser.getOptionalInstant(cfg("m", (String) null), "m", i));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryInstant(cfg("b", "nope"), "b"));
    }

    @Test
    public void localDate() {
        final var d = LocalDate.parse("2020-01-01");
        assertEquals(d, ConfigParser.getMandatoryLocalDate(cfg("k", "2020-01-01"), "k"));
        assertEquals(d, ConfigParser.getOptionalLocalDate(cfg("m", (String) null), "m", d));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryLocalDate(cfg("b", "nope"), "b"));
    }

    @Test
    public void localTime() {
        final var t = LocalTime.parse("12:00:00");
        assertEquals(t, ConfigParser.getMandatoryLocalTime(cfg("k", "12:00:00"), "k"));
        assertEquals(t, ConfigParser.getOptionalLocalTime(cfg("m", (String) null), "m", t));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryLocalTime(cfg("b", "nope"), "b"));
    }

    @Test
    public void localDateTime() {
        final var dt = LocalDateTime.parse("2020-01-01T12:00:00");
        assertEquals(dt, ConfigParser.getMandatoryLocalDateTime(cfg("k", "2020-01-01T12:00:00"), "k"));
        assertEquals(dt, ConfigParser.getOptionalLocalDateTime(cfg("m", (String) null), "m", dt));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryLocalDateTime(cfg("b", "nope"), "b"));
    }

    @Test
    public void enumType() {
        assertEquals(Thread.State.NEW, ConfigParser.getMandatoryEnum(cfg("k", "NEW"), "k", Thread.State.class));
        assertEquals(Thread.State.NEW, ConfigParser.getOptionalEnum(cfg("m", (String) null), "m", Thread.State.class, Thread.State.NEW));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryEnum(cfg("b", "NOPE"), "b", Thread.State.class));
    }

    @Test
    public void stringList() {
        assertEquals(List.of("a", "b", "c"), ConfigParser.getMandatoryStringList(cfg("k", "a b,c"), "k"));
        assertEquals(List.of("x"), ConfigParser.getOptionalStringList(cfg("m", (String) null), "m", List.of("x")));
    }

    @Test
    public void typedList() {
        final var list = ConfigParser.getMandatoryList(cfg("k", "1 2 3"), "k", Integer::parseInt);
        assertEquals(List.of(1, 2, 3), list);
        final var opt = ConfigParser.getOptionalList(cfg("m", (String) null), "m", List.of(9), Integer::parseInt);
        assertEquals(List.of(9), opt);
    }

    @Test
    public void configKeyOverloads() {
        final var key = Config.newConfigKey("typed");
        final var c = Config.newConfig().put(key, "42").build();
        assertEquals(42, ConfigParser.getMandatoryInteger(c, key));
        assertEquals(42, ConfigParser.getOptionalInteger(c, key, 0));
        assertTrue(ConfigParser.has(c, key));
    }
}

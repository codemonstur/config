package unittests;

import config.Config;
import config.ConfigParser;
import config.InvalidConfigurationSetting;
import config.MissingConfigurationSetting;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigParserTest {

    private Config configOf(final String key, final String value) {
        return Config.newConfig().put(key, value).build();
    }

    @Test
    public void mandatoryStringSuccessAndMissing() {
        final var config = configOf("k", "v");
        assertEquals("v", ConfigParser.getMandatoryString(config, "k"));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryString(config, "missing"));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryString(configOf("empty", ""), "empty"));
    }

    @Test
    public void optionalString() {
        final var config = configOf("k", "v");
        assertEquals("v", ConfigParser.getOptionalString(config, "k", "d"));
        assertEquals("d", ConfigParser.getOptionalString(config, "missing", "d"));
        assertEquals("d", ConfigParser.getOptionalString(configOf("empty", ""), "empty", "d"));
    }

    @Test
    public void mandatoryInteger() {
        final var config = configOf("k", "42");
        assertEquals(42, ConfigParser.getMandatoryInteger(config, "k"));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryInteger(configOf("m", (String) null) , "m"));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryInteger(configOf("b", "x"), "b"));
    }

    @Test
    public void optionalInteger() {
        assertEquals(42, ConfigParser.getOptionalInteger(configOf("k", "42"), "k", 0));
        assertEquals(7, ConfigParser.getOptionalInteger(configOf("m", (String) null), "m", 7));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalInteger(configOf("b", "x"), "b", 0));
    }

    @Test
    public void mandatoryLong() {
        final var config = configOf("k", "123456789");
        assertEquals(123456789L, ConfigParser.getMandatoryLong(config, "k"));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryLong(configOf("b", "x"), "b"));
    }

    @Test
    public void optionalLong() {
        assertEquals(5L, ConfigParser.getOptionalLong(configOf("m", (String) null), "m", 5L));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalLong(configOf("b", "x"), "b", 0L));
    }

    @Test
    public void mandatoryDouble() {
        final var config = configOf("k", "3.5");
        assertEquals(3.5, ConfigParser.getMandatoryDouble(config, "k"));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryDouble(configOf("b", "x"), "b"));
    }

    @Test
    public void optionalDouble() {
        assertEquals(1.5, ConfigParser.getOptionalDouble(configOf("m", (String) null), "m", 1.5));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalDouble(configOf("b", "x"), "b", 0.0));
    }

    @Test
    public void mandatoryBoolean() {
        assertTrue(ConfigParser.getMandatoryBoolean(configOf("k", "true"), "k"));
        assertFalse(ConfigParser.getMandatoryBoolean(configOf("k", "false"), "k"));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryBoolean(configOf("b", "maybe"), "b"));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryBoolean(configOf("m", (String) null), "m"));
    }

    @Test
    public void optionalBoolean() {
        assertTrue(ConfigParser.getOptionalBoolean(configOf("k", "true"), "k", false));
        assertFalse(ConfigParser.getOptionalBoolean(configOf("m", (String) null), "m", false));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalBoolean(configOf("b", "maybe"), "b", false));
    }

    @Test
    public void mandatoryPath() {
        final var config = configOf("k", "/tmp/example");
        assertEquals(Path.of("/tmp/example"), ConfigParser.getMandatoryPath(config, "k"));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryPath(configOf("m", (String) null), "m"));
    }

    @Test
    public void optionalPath() {
        assertEquals(Path.of("/tmp/example"), ConfigParser.getOptionalPath(configOf("k", "/tmp/example"), "k", Path.of("/default")));
        assertEquals(Path.of("/default"), ConfigParser.getOptionalPath(configOf("m", (String) null), "m", Path.of("/default")));
    }

    @Test
    public void mandatoryUri() throws Exception {
        final var config = configOf("k", "http://example.com/api");
        assertEquals(URI.create("http://example.com/api"), ConfigParser.getMandatoryURI(config, "k"));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getMandatoryURI(configOf("b", "http://bad space"), "b"));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatoryURI(configOf("m", (String) null), "m"));
    }

    @Test
    public void optionalUri() throws Exception {
        assertEquals(URI.create("http://example.com/api"), ConfigParser.getOptionalURI(configOf("k", "http://example.com/api"), "k", URI.create("http://default")));
        assertEquals(URI.create("http://default"), ConfigParser.getOptionalURI(configOf("m", (String) null), "m", URI.create("http://default")));
        assertThrows(InvalidConfigurationSetting.class, () -> ConfigParser.getOptionalURI(configOf("b", "http://bad space"), "b", URI.create("http://default")));
    }

    @Test
    public void mandatorySet() {
        final var config = configOf("k", "one two,three");
        assertEquals(Set.of("one", "two", "three"), ConfigParser.getMandatorySet(config, "k"));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatorySet(configOf("m", (String) null), "m"));
        assertThrows(MissingConfigurationSetting.class, () -> ConfigParser.getMandatorySet(configOf("empty", ""), "empty"));
    }

    @Test
    public void optionalSet() {
        assertEquals(Set.of("one", "two"), ConfigParser.getOptionalSet(configOf("k", "one two"), "k", Set.of("default")));
        assertEquals(Set.of("default"), ConfigParser.getOptionalSet(configOf("m", (String) null), "m", Set.of("default")));
    }

    @Test
    public void configKeyOverload() {
        final var key = Config.newConfigKey("typed.key");
        final var config = Config.newConfig().put(key, "value").build();
        assertEquals("value", ConfigParser.getMandatoryString(config, key));
        assertEquals("value", ConfigParser.getOptionalString(config, key, "d"));
    }

    @Test
    public void configKeyEquality() {
        assertEquals(Config.newConfigKey("a"), Config.newConfigKey("a"));
        assertNotEquals(Config.newConfigKey("a"), Config.newConfigKey("b"));
    }
}

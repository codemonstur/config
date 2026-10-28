package unittests;

import config.Config;
import config.MissingConfigurationSetting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigBuilderExtendedTest {

    @TempDir
    Path tempDir;

    @Test
    public void systemProperties() {
        final var config = Config.newConfig().loadSystemProperties().build();
        assertTrue(config.containsKey("java.version"));
        assertNotNull(config.cnf("java.version", null));
    }

    @Test
    public void propertiesObject() {
        final var props = new Properties();
        props.setProperty("k", "v");
        final var config = Config.newConfig().loadProperties(props).build();
        assertEquals("v", config.cnf("k", null));
    }

    @Test
    public void inputStream() throws Exception {
        final var in = new ByteArrayInputStream("k=v\n".getBytes());
        final var config = Config.newConfig().load(in).build();
        assertEquals("v", config.cnf("k", null));
    }

    @Test
    public void file() throws Exception {
        final var file = tempDir.resolve("a.properties");
        Files.writeString(file, "k=v\n");
        final var config = Config.newConfig().loadFile(file).build();
        assertEquals("v", config.cnf("k", null));
        final var config2 = Config.newConfig().loadFile(file.toString()).build();
        assertEquals("v", config2.cnf("k", null));
    }

    @Test
    public void url() throws Exception {
        final var file = tempDir.resolve("b.properties");
        Files.writeString(file, "k=v\n");
        final var url = file.toUri().toURL();
        final var config = Config.newConfig().loadUrl(url).build();
        assertEquals("v", config.cnf("k", null));
        final var config2 = Config.newConfig().loadUrl(url.toString()).build();
        assertEquals("v", config2.cnf("k", null));
    }

    @Test
    public void commandLineArguments() {
        final var config = Config.newConfig()
                .loadCommandLineArguments("--a=1", "-Db=2", "c=3", "flag")
                .build();
        assertEquals("1", config.cnf("a", null));
        assertEquals("2", config.cnf("b", null));
        assertEquals("3", config.cnf("c", null));
        assertEquals("true", config.cnf("flag", null));
    }

    @Test
    public void caseInsensitive() {
        final var config = Config.newConfig().caseInsensitive().put("Host", "localhost").build();
        assertEquals("localhost", config.cnf("host", null));
        assertEquals("localhost", config.cnf("HOST", null));
    }

    @Test
    public void prefix() {
        final var config = Config.newConfig().prefix("db.").put("host", "localhost").build();
        assertEquals("localhost", config.cnf("db.host", null));
        assertNull(config.cnf("host", null));
    }

    @Test
    public void defaults() {
        final var config = Config.newConfig().default_("k", "v").defaults(Map.of("m", "n")).build();
        assertEquals("v", config.cnf("k", null));
        assertEquals("n", config.cnf("m", null));
        // explicit put overrides default
        final var config2 = Config.newConfig().default_("k", "v").put("k", "x").build();
        assertEquals("x", config2.cnf("k", null));
    }

    @Test
    public void removeAndClearAndHasKey() {
        final var builder = Config.newConfig().put("a", "1").put("b", "2");
        final var config = builder.build();
        assertTrue(config.containsKey("a"));
        assertEquals(2, config.size());
        assertEquals("1", config.remove("a"));
        assertFalse(config.containsKey("a"));
        assertEquals(1, config.size());
        assertEquals(Map.of("b", "2"), config.asMap());
        config.clear();
        assertEquals(0, config.size());
        assertTrue(config.keySet().isEmpty());
    }

    @Test
    public void json() {
        final var config = Config.newConfig().loadJson("{\"a\": 2, \"b\": {\"c\": \"3\"}, \"arr\": [1, 2]}").build();
        assertEquals("2", config.cnf("a", null));
        assertEquals("3", config.cnf("b.c", null));
        assertEquals("1", config.cnf("arr[0]", null));
        assertEquals("2", config.cnf("arr[1]", null));
    }

    @Test
    public void yaml() throws Exception {
        final var yaml = "a: 1\nb:\n  c: 2\n";
        final var config = Config.newConfig().loadYaml(yaml).build();
        assertEquals("1", config.cnf("a", null));
        assertEquals("2", config.cnf("b.c", null));
    }

    @Test
    public void toml() {
        final var toml = "a = 1\n[b]\nc = 2\n";
        final var config = Config.newConfig().loadToml(toml).build();
        assertEquals("1", config.cnf("a", null));
        assertEquals("2", config.cnf("b.c", null));
    }

    @Test
    public void hocon() {
        final var config = Config.newConfig().loadHocon("a = 1\n").build();
        assertEquals("1", config.cnf("a", null));
    }

    @Test
    public void profileMissingIsIgnored() {
        final var config = Config.newConfig().loadProfile("nonexistent-profile-xyz").put("k", "v").build();
        assertEquals("v", config.cnf("k", null));
    }

    @Test
    public void mandatoryFieldsMissing() {
        assertThrows(MissingConfigurationSetting.class, () -> Config.newConfig().mandatoryFields("missing").build());
    }

    @Test
    public void builderGet() {
        final var builder = Config.newConfig().put("k", "v");
        assertEquals("v", builder.get("k"));
        assertNull(builder.get("missing"));
    }
}

package unittests;

import config.Config;
import config.ConfigBuilder;
import config.MissingConfigurationSetting;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigBuilderTest {

    @Test
    public void putAndGet() {
        final var config = Config.newConfig()
                .put("key", "value")
                .put(Config.newConfigKey("typed"), "typed-value")
                .build();
        assertEquals("value", config.cnf("key", "default"));
        assertEquals("typed-value", config.cnf(Config.newConfigKey("typed"), "default"));
        assertNull(config.cnf("missing", null));
    }

    @Test
    public void putIfAbsent() {
        final var builder = Config.newConfig().put("key", "original");
        builder.putIfAbsent("key", "ignored");
        builder.putIfAbsent("new", "added");
        final var config = builder.build();
        assertEquals("original", config.cnf("key", null));
        assertEquals("added", config.cnf("new", null));
    }

    @Test
    public void putMap() {
        final var config = Config.newConfig()
                .put(Map.of("a", "1", "b", "2"))
                .build();
        assertEquals("1", config.cnf("a", null));
        assertEquals("2", config.cnf("b", null));
    }

    @Test
    public void putInt() {
        final var config = Config.newConfig()
                .put("n", 7)
                .put(Config.newConfigKey("m"), 9)
                .build();
        assertEquals("7", config.cnf("n", null));
        assertEquals("9", config.cnf("m", null));
    }

    @Test
    public void loadClasspathResource() throws IOException {
        final var config = Config.newConfig()
                .loadClasspathResource("config/test.properties")
                .build();
        assertEquals("ConfigLibrary", config.cnf("name", null));
        assertEquals("8080", config.cnf("port", null));
    }

    @Test
    public void loadClasspathResourceWithLeadingSlash() throws IOException {
        final var config = Config.newConfig()
                .loadClasspathResource("/config/test.properties")
                .build();
        assertEquals("ConfigLibrary", config.cnf("name", null));
    }

    @Test
    public void loadClasspathResourceMissingThrows() {
        assertThrows(IOException.class, () -> Config.newConfig().loadClasspathResource("does/not/exist.properties"));
    }

    @Test
    public void mandatoryFieldMissingThrows() {
        assertThrows(MissingConfigurationSetting.class, () ->
                Config.newConfig().mandatoryFields("missing.key").build());
    }

    @Test
    public void mandatoryFieldPresentBuilds() {
        final var config = Config.newConfig()
                .put("present", "v")
                .mandatoryFields("present")
                .build();
        assertEquals("v", config.cnf("present", null));
    }

    @Test
    public void get() {
        final var builder = Config.newConfig().put("k", "v");
        assertEquals("v", builder.get("k"));
        assertEquals("v", builder.get(Config.newConfigKey("k")));
        assertNull(builder.get("missing"));
    }
}

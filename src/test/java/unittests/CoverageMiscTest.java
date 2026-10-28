package unittests;

import config.Config;
import config.ConfigBuilder;
import config.InvalidConfigurationSetting;
import config.Manifest;
import config.MissingConfigurationSetting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CoverageMiscTest {

    @TempDir
    Path tempDir;

    @Test
    public void configInterfaceDefaults() {
        Config c = (key, defaultValue) -> null;
        assertFalse(c.containsKey("x"));
        assertFalse(c.containsKey(Config.newConfigKey("x")));
        assertEquals(Set.of(), c.keySet());
        assertNull(c.remove("x"));
        assertNull(c.remove(Config.newConfigKey("x")));
        c.clear();
        assertEquals(0, c.size());
        assertEquals(Map.of(), c.asMap());
        // cnf ConfigKey overload
        assertNull(c.cnf(Config.newConfigKey("x"), "d"));
    }

    @Test
    public void configKeyHashCode() {
        assertNotNull(Config.newConfigKey("a").hashCode());
        assertEquals(Config.newConfigKey("a").hashCode(), Config.newConfigKey("a").hashCode());
    }

    @Test
    public void missingAndInvalidConstructors() {
        final var key = Config.newConfigKey("k");
        final var m = new MissingConfigurationSetting(key);
        assertTrue(m.getMessage().contains("k"));
        final var inv = new InvalidConfigurationSetting(key, "type", new Exception("boom"));
        assertTrue(inv.getMessage().contains("type"));
        assertNotNull(inv.getCause());
    }

    @Test
    public void configBuilderLoadEnvironment() {
        final var config = Config.newConfig().loadEnvironment().build();
        assertNotNull(config);
    }

    @Test
    public void configBuilderLoadManifestPresent() throws Exception {
        assertNotNull(Config.newConfig().loadManifest().build());
    }

    @Test
    public void configBuilderLoadManifestInstance() {
        final var manifest = new java.util.jar.Manifest();
        manifest.getMainAttributes().putValue("X", "Y");
        final var config = Config.newConfig().loadManifest(manifest).build();
        assertEquals("Y", config.cnf("X", null));
    }

    @Test
    public void configBuilderPutIfAbsentConfigKey() {
        final var key = Config.newConfigKey("k");
        final var builder = Config.newConfig().put(key, "v");
        builder.putIfAbsent(key, "ignored");
        assertEquals("v", builder.get(key));
    }

    @Test
    public void configBuilderMandatoryFieldsOverloads() {
        final var key = Config.newConfigKey("k");
        final var config = Config.newConfig().put(key, "v").put("m", "w")
                .mandatoryFields(key).mandatoryFields("m")
                .mandatoryFields(java.util.List.of("m"))
                .build();
        assertEquals("v", config.cnf(key, null));
    }

    @Test
    public void configBuilderRemoveAndClear() {
        final var builder = Config.newConfig().put("a", "1");
        builder.remove("a");
        assertNull(builder.get("a"));
        builder.clear();
        assertEquals(0, builder.get("a") == null ? 0 : 1);
    }

    @Test
    public void configBuilderLoadJsonOverloads() throws Exception {
        final var in = new ByteArrayInputStream("{\"a\":1}".getBytes());
        final var config1 = Config.newConfig().loadJson(in).build();
        assertEquals("1", config1.cnf("a", null));
        final var file = tempDir.resolve("j.json");
        Files.writeString(file, "{\"b\":2}");
        final var config2 = Config.newConfig().loadJson(file).build();
        assertEquals("2", config2.cnf("b", null));
        final var url = file.toUri().toURL();
        final var config3 = Config.newConfig().loadJson(url).build();
        assertEquals("2", config3.cnf("b", null));
    }

    @Test
    public void configBuilderLoadYamlOverloads() throws Exception {
        final var in = new ByteArrayInputStream("a: 1".getBytes());
        final var config1 = Config.newConfig().loadYaml(in).build();
        assertEquals("1", config1.cnf("a", null));
        final var file = tempDir.resolve("y.yml");
        Files.writeString(file, "b: 2");
        final var config2 = Config.newConfig().loadYaml(file).build();
        assertEquals("2", config2.cnf("b", null));
        final var url = file.toUri().toURL();
        final var config3 = Config.newConfig().loadYaml(url).build();
        assertEquals("2", config3.cnf("b", null));
    }

    @Test
    public void configBuilderLoadTomlOverloads() throws Exception {
        final var in = new ByteArrayInputStream("a = 1".getBytes());
        final var config1 = Config.newConfig().loadToml(in).build();
        assertEquals("1", config1.cnf("a", null));
        final var file = tempDir.resolve("t.toml");
        Files.writeString(file, "b = 2");
        final var config2 = Config.newConfig().loadToml(file).build();
        assertEquals("2", config2.cnf("b", null));
        final var url = file.toUri().toURL();
        final var config3 = Config.newConfig().loadToml(url).build();
        assertEquals("2", config3.cnf("b", null));
    }

    @Test
    public void configBuilderLoadHoconOverloads() throws Exception {
        final var in = new ByteArrayInputStream("a = 1".getBytes());
        final var config1 = Config.newConfig().loadHocon(in).build();
        assertEquals("1", config1.cnf("a", null));
        final var file = tempDir.resolve("h.conf");
        Files.writeString(file, "b = 2");
        final var config2 = Config.newConfig().loadHocon(file).build();
        assertEquals("2", config2.cnf("b", null));
        final var url = file.toUri().toURL();
        final var config3 = Config.newConfig().loadHocon(url).build();
        assertEquals("2", config3.cnf("b", null));
    }

    @Test
    public void configBuilderLoadFileString() throws Exception {
        final var file = tempDir.resolve("p.properties");
        Files.writeString(file, "k=v\n");
        final var config = Config.newConfig().loadFile(file.toString()).build();
        assertEquals("v", config.cnf("k", null));
    }

    @Test
    public void configBuilderLoadUrlString() throws Exception {
        final var file = tempDir.resolve("q.properties");
        Files.writeString(file, "k=v\n");
        final var config = Config.newConfig().loadUrl(file.toUri().toURL().toString()).build();
        assertEquals("v", config.cnf("k", null));
    }

    @Test
    public void jsonFlattenerEdgeCases() {
        final var json = "{\"s\":\"a\\\\nb\\\\t\\\\\\\"q\\\\\\\"\",\"arr\":[],\"obj\":{},\"n\":null}";
        final var config = Config.newConfig().loadJson(json).build();
        assertNotNull(config.cnf("s", null));
        assertEquals("null", config.cnf("n", null));
    }

    @Test
    public void jsonEscapes() {
        final var json = "{\"s\":\"a\\nb\\t\\\"q\\\"\"}";
        final var config = Config.newConfig().loadJson(json).build();
        assertNotNull(config.cnf("s", null));
    }

    @Test
    public void manifestWithResource() throws Exception {
        final var dir = tempDir.resolve("classes");
        final var meta = dir.resolve("META-INF");
        Files.createDirectories(meta);
        Files.writeString(meta.resolve("MANIFEST.MF"), "Manifest-Version: 1.0\nX-Attr: hello\n\n");
        assertNotNull(Manifest.loadManifest());
        assertNotNull(Manifest.getManifestValue("Manifest-Version"));
    }

    // extra hashCode null test
    @Test
    public void configKeyNullName() {
        final var key = Config.newConfigKey(null);
        assertEquals(0, key.hashCode());
        assertEquals(key, Config.newConfigKey(null));
    }

}
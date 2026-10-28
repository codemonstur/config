package config;

import config.Config.ConfigKey;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.join;

public final class ConfigBuilder {

    private final Map<String, String> conf = Collections.synchronizedMap(new HashMap<>());
    private final Set<String> mandatoryFields = Collections.synchronizedSet(new HashSet<>());

    private boolean caseInsensitive = false;
    private String prefix = null;

    public ConfigBuilder loadEnvironment() {
        System.getenv().forEach((key, value) -> conf.put(normalize(key), value));
        return this;
    }

    public ConfigBuilder loadSystemProperties() {
        System.getProperties().forEach((key, value) -> conf.put(normalize(key.toString()), value.toString()));
        return this;
    }

    public ConfigBuilder loadManifest() throws IOException {
        return loadManifest(Manifest.loadManifest());
    }
    public ConfigBuilder loadManifest(final java.util.jar.Manifest manifest) {
        manifest.getMainAttributes().forEach((key, value) -> conf.put(normalize(key.toString()), value.toString()));
        return this;
    }

    public ConfigBuilder loadClasspathResource(final String resourcePath) throws IOException {
        final var properties = new Properties();
        final var path = resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;
        try (final var in = ConfigBuilder.class.getResourceAsStream(path)) {
            if (in == null) throw new IOException("Classpath resource not found: " + resourcePath);
            properties.load(in);
        }
        return loadProperties(properties);
    }

    public ConfigBuilder loadProperties(final Properties properties) {
        properties.forEach((key, value) -> conf.put(normalize(key.toString()), value.toString()));
        return this;
    }

    public ConfigBuilder load(final InputStream in) throws IOException {
        final var properties = new Properties();
        properties.load(in);
        return loadProperties(properties);
    }

    public ConfigBuilder loadFile(final String path) throws IOException {
        return loadFile(Paths.get(path));
    }
    public ConfigBuilder loadFile(final Path path) throws IOException {
        try (final var in = Files.newInputStream(path)) {
            return load(in);
        }
    }

    public ConfigBuilder loadUrl(final String url) throws IOException {
        return loadUrl(URI.create(url).toURL());
    }
    public ConfigBuilder loadUrl(final URL url) throws IOException {
        try (final var in = url.openStream()) {
            return load(in);
        }
    }

    public ConfigBuilder loadCommandLineArguments(final String... args) {
        if (args == null) return this;
        for (final var arg : args) {
            if (arg == null) continue;
            var a = arg;
            if (a.startsWith("--")) a = a.substring(2);
            else if (a.startsWith("-D")) a = a.substring(2);
            final var idx = a.indexOf('=');
            if (idx > 0) {
                conf.put(normalize(a.substring(0, idx)), a.substring(idx + 1));
            } else if (!a.isEmpty()) {
                conf.put(normalize(a), "true");
            }
        }
        return this;
    }

    public ConfigBuilder loadProfile(final String profile) {
        if (profile == null || profile.isEmpty()) return this;
        try {
            return loadClasspathResource("application-" + profile + ".properties");
        } catch (final IOException e) {
            return this;
        }
    }

    public ConfigBuilder caseInsensitive() {
        caseInsensitive = true;
        return this;
    }

    public ConfigBuilder prefix(final String prefix) {
        this.prefix = prefix;
        return this;
    }

    public ConfigBuilder defaults(final Map<String, String> defaults) {
        if (defaults != null) {
            defaults.forEach((key, value) -> conf.putIfAbsent(normalize(key), value));
        }
        return this;
    }

    public ConfigBuilder default_(final String key, final String value) {
        conf.putIfAbsent(normalize(key), value);
        return this;
    }

    public String get(final ConfigKey key) {
        return get(key.toString());
    }
    public String get(final String key) {
        return conf.get(normalize(key, false));
    }

    public ConfigBuilder put(final ConfigKey key, final String value) {
        return put(key.toString(), value);
    }
    public ConfigBuilder put(final String key, final String value) {
        conf.put(normalize(key), value);
        return this;
    }

    public ConfigBuilder put(final ConfigKey key, final int value) {
        return put(key.toString(), Integer.toString(value));
    }
    public ConfigBuilder putIfAbsent(final ConfigKey key, final String value) {
        conf.putIfAbsent(normalize(key.toString()), value);
        return this;
    }
    public ConfigBuilder put(final String key, final int value) {
        conf.put(normalize(key), Integer.toString(value));
        return this;
    }
    public ConfigBuilder putIfAbsent(final String key, final String value) {
        conf.putIfAbsent(normalize(key), value);
        return this;
    }

    public ConfigBuilder put(final Map<String, String> map) {
        if (map != null) {
            map.forEach((key, value) -> conf.put(normalize(key), value));
        }
        return this;
    }

    public ConfigBuilder mandatoryFields(final ConfigKey... keys) {
        if (keys != null) {
            for (final var key : keys) mandatoryFields.add(normalize(key.toString()));
        }
        return this;
    }
    public ConfigBuilder mandatoryFields(final String... keys) {
        if (keys != null) {
            for (final var key : keys) mandatoryFields.add(normalize(key));
        }
        return this;
    }
    public ConfigBuilder mandatoryFields(final Collection<? extends String> keys) {
        if (keys != null) {
            for (final var key : keys) mandatoryFields.add(normalize(key));
        }
        return this;
    }

    public ConfigBuilder remove(final String key) {
        conf.remove(normalize(key, false));
        return this;
    }

    public ConfigBuilder clear() {
        conf.clear();
        mandatoryFields.clear();
        return this;
    }

    public ConfigBuilder loadJson(final String json) {
        return loadProperties(parseJson(json));
    }
    public ConfigBuilder loadJson(final InputStream in) throws IOException {
        return loadJson(new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
    }
    public ConfigBuilder loadJson(final Path path) throws IOException {
        return loadJson(Files.readString(path));
    }
    public ConfigBuilder loadJson(final URL url) throws IOException {
        try (final var in = url.openStream()) { return loadJson(in); }
    }

    public ConfigBuilder loadYaml(final String yaml) {
        final var props = new Properties();
        final var stack = new java.util.ArrayDeque<java.util.AbstractMap.SimpleEntry<Integer, String>>();
        stack.push(new java.util.AbstractMap.SimpleEntry<>(-1, ""));
        for (final var rawLine : yaml.split("\\R")) {
            if (rawLine.strip().isEmpty() || rawLine.strip().startsWith("#")) continue;
            if (rawLine.strip().startsWith("---")) continue;
            final var indent = rawLine.length() - rawLine.stripLeading().length();
            final var line = rawLine.strip();
            while (stack.size() > 1 && stack.peek().getKey() >= indent) stack.pop();
            final var parent = stack.peek().getValue();
            final var idx = line.indexOf(':');
            if (idx > 0) {
                final var key = line.substring(0, idx).trim();
                final var val = line.substring(idx + 1).trim();
                final var fullKey = parent.isEmpty() ? key : parent + "." + key;
                if (val.isEmpty()) {
                    stack.push(new java.util.AbstractMap.SimpleEntry<>(indent, fullKey));
                } else {
                    props.setProperty(fullKey, stripQuotes(val));
                }
            }
        }
        return loadProperties(props);
    }
    public ConfigBuilder loadYaml(final InputStream in) throws IOException {
        return loadYaml(new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
    }
    public ConfigBuilder loadYaml(final Path path) throws IOException {
        return loadYaml(Files.readString(path));
    }
    public ConfigBuilder loadYaml(final URL url) throws IOException {
        try (final var in = url.openStream()) { return loadYaml(in); }
    }

    public ConfigBuilder loadToml(final String toml) {
        final var props = new Properties();
        String section = "";
        for (final var rawLine : toml.split("\\R")) {
            final var line = rawLine.strip();
            if (line.isEmpty() || line.startsWith("#")) continue;
            if (line.startsWith("[") && line.endsWith("]")) { section = line.substring(1, line.length() - 1).trim(); continue; }
            final var idx = line.indexOf('=');
            if (idx > 0) {
                final var key = section.isEmpty() ? line.substring(0, idx).trim() : section + "." + line.substring(0, idx).trim();
                final var val = line.substring(idx + 1).trim();
                props.setProperty(key, stripQuotes(val));
            }
        }
        return loadProperties(props);
    }
    public ConfigBuilder loadToml(final InputStream in) throws IOException {
        return loadToml(new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
    }
    public ConfigBuilder loadToml(final Path path) throws IOException {
        return loadToml(Files.readString(path));
    }
    public ConfigBuilder loadToml(final URL url) throws IOException {
        try (final var in = url.openStream()) { return loadToml(in); }
    }

    public ConfigBuilder loadHocon(final String hocon) {
        // Minimal HOCON-like: treat as properties/TOML hybrid
        return loadToml(hocon);
    }
    public ConfigBuilder loadHocon(final InputStream in) throws IOException {
        return loadHocon(new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
    }
    public ConfigBuilder loadHocon(final Path path) throws IOException {
        return loadHocon(Files.readString(path));
    }
    public ConfigBuilder loadHocon(final URL url) throws IOException {
        try (final var in = url.openStream()) { return loadHocon(in); }
    }

    private static Properties parseJson(final String json) {
        final var props = new Properties();
        final var parser = new JsonFlattener(json, "", props);
        parser.parse();
        return props;
    }

    private static String stripQuotes(final String value) {
        if (value.length() >= 2 && ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'")))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static final class JsonFlattener {
        private final String text;
        private int i = 0;
        private final String prefix;
        private final Properties props;

        JsonFlattener(final String text, final String prefix, final Properties props) {
            this.text = text;
            this.prefix = prefix;
            this.props = props;
        }

        void parse() {
            skipWs();
            if (peek() == '{') {
                parseObject(prefix);
            }
        }

        private void parseObject(final String pfx) {
            expect('{');
            skipWs();
            if (peek() == '}') { i++; return; }
            while (true) {
                skipWs();
                final var key = parseString();
                skipWs();
                expect(':');
                skipWs();
                final var newPrefix = pfx.isEmpty() ? key : pfx + "." + key;
                parseValue(newPrefix);
                skipWs();
                if (peek() == ',') { i++; continue; }
                if (peek() == '}') { i++; break; }
                throw new IllegalArgumentException("Expected , or } in JSON");
            }
        }

        private void parseValue(final String pfx) {
            skipWs();
            final char c = peek();
            if (c == '{') {
                parseObject(pfx);
            } else if (c == '[') {
                parseArray(pfx);
            } else if (c == '"') {
                props.setProperty(pfx, parseString());
            } else {
                final var start = i;
                while (i < text.length() && "},]".indexOf(text.charAt(i)) < 0 && !Character.isWhitespace(text.charAt(i))) {
                    i++;
                }
                props.setProperty(pfx, text.substring(start, i).trim());
            }
        }

        private void parseArray(final String pfx) {
            expect('[');
            skipWs();
            if (peek() == ']') { i++; return; }
            int idx = 0;
            while (true) {
                parseValue(pfx + "[" + idx + "]");
                idx++;
                skipWs();
                if (peek() == ',') { i++; continue; }
                if (peek() == ']') { i++; break; }
                throw new IllegalArgumentException("Expected , or ] in JSON");
            }
        }

        private String parseString() {
            expect('"');
            final var sb = new StringBuilder();
            while (i < text.length()) {
                final char c = text.charAt(i++);
                if (c == '"') return sb.toString();
                if (c == '\\') {
                    if (i >= text.length()) break;
                    final char e = text.charAt(i++);
                    char mapped;
                    switch (e) {
                        case 'n': mapped = '\n'; break;
                        case 't': mapped = '\t'; break;
                        case 'r': mapped = '\r'; break;
                        case '"': mapped = '"'; break;
                        case '\\': mapped = '\\'; break;
                        default: mapped = e; break;
                    }
                    sb.append(mapped);
                } else {
                    sb.append(c);
                }
            }
            throw new IllegalArgumentException("Unterminated string in JSON");
        }

        private void skipWs() {
            while (i < text.length() && Character.isWhitespace(text.charAt(i))) i++;
        }

        private char peek() {
            skipWs();
            if (i >= text.length()) throw new IllegalArgumentException("Unexpected end of JSON");
            return text.charAt(i);
        }

        private void expect(final char c) {
            skipWs();
            if (i >= text.length() || text.charAt(i) != c) throw new IllegalArgumentException("Expected " + c + " in JSON");
            i++;
        }
    }

    public Config build() {
        validate();
        final var map = conf;
        final Function<String, String> normalizer = key -> normalize(key, false);
        return new Config() {
            public String cnf(final String key, final String defaultValue) {
                return map.getOrDefault(normalizer.apply(key), defaultValue);
            }
            public boolean containsKey(final String key) {
                return map.containsKey(normalizer.apply(key));
            }
            public Set<String> keySet() {
                return new java.util.HashSet<>(map.keySet());
            }
            public String remove(final String key) {
                return map.remove(normalizer.apply(key));
            }
            public void clear() {
                map.clear();
            }
            public int size() {
                return map.size();
            }
            public Map<String, String> asMap() {
                return new HashMap<>(map);
            }
        };
    }

    private void validate() {
        final var missingValues = new java.util.LinkedList<String>();
        for (final var key : mandatoryFields) {
            if (!conf.containsKey(key)) missingValues.add(key);
        }
        if (!missingValues.isEmpty())
            throw new MissingConfigurationSetting("Missing configuration settings: " + join(", ", missingValues));
    }

    private String normalize(final String key, final boolean withPrefix) {
        if (key == null) return null;
        var k = key;
        if (withPrefix && prefix != null && !prefix.isEmpty()) {
            k = prefix + k;
        }
        if (caseInsensitive) {
            k = k.toLowerCase(Locale.ROOT);
        }
        return k;
    }

    private String normalize(final String key) {
        return normalize(key, true);
    }

}

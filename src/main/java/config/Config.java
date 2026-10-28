package config;

import java.util.Map;
import java.util.Set;

public interface Config {

    public interface ConfigKey {}
    public static ConfigKey newConfigKey(final String name) {
        return new ConfigKey() {
            public String toString() {
                return name;
            }
            public boolean equals(final Object other) {
                return other instanceof ConfigKey && java.util.Objects.equals(name, other.toString());
            }
            public int hashCode() {
                return name == null ? 0 : name.hashCode();
            }
        };
    }

    String cnf(String key, String defaultValue);
    default String cnf(final ConfigKey key, final String defaultValue) {
        return cnf(key.toString(), defaultValue);
    }

    default boolean containsKey(final String key) {
        return cnf(key, null) != null;
    }
    default boolean containsKey(final ConfigKey key) {
        return containsKey(key.toString());
    }

    default Set<String> keySet() {
        return Set.of();
    }

    default String remove(final String key) {
        return null;
    }
    default String remove(final ConfigKey key) {
        return remove(key.toString());
    }

    default void clear() {
    }

    default int size() {
        return keySet().size();
    }

    default Map<String, String> asMap() {
        return Map.of();
    }

    public static ConfigBuilder newConfig() {
        return new ConfigBuilder();
    }

}

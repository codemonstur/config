package config;

public interface Config {

    public interface ConfigKey {}
    public static ConfigKey newConfigKey(final String name) {
        return new ConfigKey() {
            public String toString() {
                return name;
            }
        };
    }

    String cnf(String key, String defaultValue);
    default String cnf(final ConfigKey key, final String defaultValue) {
        return cnf(key.toString(), defaultValue);
    }

    public static ConfigBuilder newConfig() {
        return new ConfigBuilder();
    }

}

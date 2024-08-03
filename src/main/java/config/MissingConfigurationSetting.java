package config;

public final class MissingConfigurationSetting extends RuntimeException {

    public MissingConfigurationSetting(final String message) {
        super(message);
    }
    public MissingConfigurationSetting(final Config.ConfigKey key) {
        super("Configuration setting " + key + " must be set");
    }

}

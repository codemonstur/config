package config;

public final class MissingConfigurationSetting extends RuntimeException {
    public MissingConfigurationSetting(final String message) {
        super(message);
    }
}

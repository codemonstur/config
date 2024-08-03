package config;

import config.Config.ConfigKey;

import java.net.URISyntaxException;

import static java.lang.String.format;

public final class InvalidConfigurationSetting extends RuntimeException {

    public InvalidConfigurationSetting(final String message) {
        super(message);
    }

    public InvalidConfigurationSetting(final String message, final Exception e) {
        super(message, e);
    }

    public InvalidConfigurationSetting(final ConfigKey key, final String type, final Exception e) {
        super(format("Configuration setting %s must contain an %s", key, type), e);
    }

}

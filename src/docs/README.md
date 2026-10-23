# config

Utility classes for reading configuration from multiple layered sources —
environment variables, jar manifest attributes, and classpath resources — with
typed, validated accessors.

The library has **no external dependencies** (pure JDK).

## Usage

```java
Config config = Config.newConfig()
    .loadEnvironment()
    .loadManifest()
    .loadClasspathResource("application.properties")
    .build();

String value = config.cnf("my.key", "default-value");
```

### Typed access

`ConfigParser` provides mandatory/optional accessors that throw descriptive
exceptions instead of returning `null`:

```java
String name = parser.getMandatoryString("service.name");
URI endpoint = parser.getOptionalURI("service.endpoint");
```

- `MissingConfigurationSetting` — a mandatory key was absent.
- `InvalidConfigurationSetting` — the value could not be parsed to the requested
  type.

### Config keys

Keys are modelled with a `ConfigKey` marker interface and a `newConfigKey(String)`
factory, so strongly-typed key constants can be shared across an application while
backing values still resolve through the layered `Config` sources.

## Sources

| Source | Method | Notes |
| --- | --- | --- |
| Environment variables | `loadEnvironment()` | Process environment. |
| Jar manifest | `loadManifest()` | Reads `MANIFEST.MF` attributes (cached). |
| Classpath resource | `loadClasspathResource(path)` | Java properties file. |

Later sources layer on top of earlier ones; mandatory fields are tracked by the
builder so missing values fail at build time.

## Dependency

```xml
<dependency>
  <groupId>com.github.codemonstur</groupId>
  <artifactId>config</artifactId>
  <version>1.1.0</version>
</dependency>
```

## Building

```sh
make build
```

## License

MIT

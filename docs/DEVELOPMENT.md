# Development

## Building

Install JDK 21, then build:

```sh
./gradlew build
```

`gradle/gradle-daemon-jvm.properties` selects Java 21 for the Gradle daemon,
even when the terminal's default Java version is different. Gradle must be able
to discover a local JDK 21 installation.

The universal release is written to `build/libs/clock-hud-1.1.13.jar`; its name
follows `mod_version` in `gradle.properties`. The build compiles and remaps all
15 version adapters, compiles the shared main package, and assembles the release.
There are currently no automated tests.

The outer release contains the shared implementation, assets, and nested adapter
JARs. Each adapter contains a compiled copy of the shared Mod Menu bridge source.
Distribute the universal JAR, not the intermediate shared or adapter artifacts.

## Development clients

Run a specific Minecraft version using its module name:

```sh
./gradlew --configure-on-demand :mc-1.21.11:runClient
```

For example, use `:mc-1.18.2:runClient` for Minecraft 1.18.2.
`versions.json` lists the available build targets and their dependencies.

Development clients load the shared main package and the selected adapter.
Mod Menu itself remains an optional dependency; its
`modCompileOnly` declaration does not install it into a development client.
Install its matching version separately when checking the Mod Menu entrypoint.

## Shared behavior and version proxies

Start reading at `Main.onInitializeClient()`. Fabric invokes this
entrypoint from the outer mod. It obtains exactly one `ClientPlatform` provider
registered by the selected adapter, then initializes configuration, the HUD,
key bindings, and callbacks. Fabric creates the `Main` instance; adapters do not.

```text
Fabric -> Main.onInitializeClient()
              |
              +-- obtains the selected ClientPlatform from Fabric
              +-- initializes shared behavior
```

The shared code under `src/main/java` is compiled once for Java 16 as an ordinary
Java library without Minecraft dependencies or remapping. Native Minecraft
types, including `Screen`, stay in the version proxies.
All adapters use this same implementation, so shared feature changes reach every
supported version without backports.

- `Main` owns the Fabric entrypoint, platform discovery, initialization, key
  actions, configuration saving, menus, world lifecycle, and render guards.
- `core/ClockHud` owns clock layout, day transitions, and animation.
- `core/WorldClock` handles time calculations and day/night progress.
- `ClientPlatform` exposes native client operations through each version's proxy.
  Screens cross this interface as `Object`; casts to native `Screen` happen only
  in the version proxies and the Mod Menu bridge.
- `core/HudCanvas` exposes primitive drawing operations.
- `render/LegacyCanvas` owns the blend and shader color sequence required
  through 1.21.1, including color restoration after drawing.
- `Textures` shares texture identifier caching.
- `config/ModConfig` defines the configuration schema and Cloth annotations.
- `src/main/resources/assets` contains the shared textures and translations.

Each `adapters/<minecraft>/src/main/java/` directory contains ordinary Java
source compiled directly against that Minecraft version. Its
`src/main/resources/` directory contains Fabric and Mixin metadata. Edit these
files directly; there is no Java preprocessor or generated adapter source.
`MinecraftPlatform` is registered under the custom `clock-hud:platform`
entrypoint. `HudBridge` forwards native HUD render calls to `Main.render()`.

Adapters register native callbacks, expose game state, construct and set native
screens, and execute primitive drawing calls. Their repeated API wrappers are
intentional. Feature decisions, menu flow, layout, and rendering strategies
belong in the shared code, including strategies needed by only some versions.

`gradle/adapter.gradle` supplies common dependency, compilation, and resource
version expansion settings. `versions.json` pins the Minecraft, Yarn, Fabric
API, Cloth Config, Mod Menu, and Java versions for each build target.

## Mod Menu integration

`src/bridge/java/.../platform/ModMenuImpl.java` is the only source copy of the
Mod Menu bridge. `gradle/adapter.gradle` adds that source directory to every
adapter's compilation. Each compiled copy uses its own Minecraft types and
mappings, without relying on `Screen` having the same intermediary name across
versions. It forwards to `Main.configScreen()`, which
asks the selected platform to construct the native screen.

The outer metadata declares the Mod Menu entrypoint, so the configuration button
belongs to `clock-hud`; the implementation class comes from the selected adapter.
There is no separate integration subproject or runtime-generated bridge.
The version proxies retain the native Cloth Config screen construction call.

## Universal loading

The root `build.gradle` generates the release metadata and embeds the remapped
adapters under `META-INF/jars/`. All candidates have the same
`clock-hud-adapter` mod ID and declare different Minecraft ranges. The outer
mod requires that ID, so Fabric Loader selects the matching candidate before
loading its entrypoint or Mixin. Other candidates remain unloaded.
Only adapters declare Minecraft version ranges; the outer mod has no separate
Minecraft version restriction. Without a compatible adapter, the required
adapter dependency cannot be satisfied.

The identical adapter package and class names are intentional. Keep adapter
classes inside their nested JARs. The main mod and all adapters declare
`"environment": "client"`; using `src/main` does not enable server-side loading.

See [Compatibility](COMPATIBILITY.md) for version ranges, dependency requirements,
and manual validation guidance. A successful build checks compilation and
packaging; verify the release JAR in game before publishing.

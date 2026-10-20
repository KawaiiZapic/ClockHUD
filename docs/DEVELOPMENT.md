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

The outer release contains the shared implementation, assets, and a nested
`fabric-adapters.jar` container holding all 15 adapter JARs. Each adapter contains
a compiled copy of the shared Mod Menu bridge source.
Distribute the universal JAR, not the intermediate shared or adapter artifacts.

## Development clients

Run a specific Minecraft version using its module name:

```sh
./gradlew --configure-on-demand :fabric-1.21.11:runClient
```

For example, use `:fabric-1.18.2:runClient` for Minecraft 1.18.2.
`adapters/fabric/versions.json` lists the available build targets and their dependencies.

Development clients skip the external Minecraft asset download by default,
including sounds. Game JARs, libraries, and mod dependencies are still resolved
normally, and existing cached assets remain available. Missing assets can produce
resource-index errors and sound warnings in the log; Minecraft 1.19.2 was able
to launch in this mode for ClockHUD checks.

To download the complete asset bundle, opt in when launching:

```sh
./gradlew --configure-on-demand :fabric-1.21.11:runClient -PdownloadGameAssets=true
```

The same flag is required when running a module's `downloadAssets` task directly.

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

Each `adapters/fabric/<minecraft>/src/main/java/` directory contains ordinary Java
source compiled directly against that Minecraft version. Fabric and Mixin
metadata templates live in `adapters/fabric/shared/resources/`. Resource
processing expands each target's explicitly declared configuration; there is
no Java preprocessor or generated adapter source.
`MinecraftPlatform` is registered under the custom `clock-hud:platform`
entrypoint. `HudBridge` forwards native HUD render calls to `Main.render()`.

Adapters register native callbacks, expose game state, construct and set native
screens, and execute primitive drawing calls. Their repeated API wrappers are
intentional. Feature decisions, menu flow, layout, and rendering strategies
belong in the shared code, including strategies needed by only some versions.

`gradle/fabric-adpater.gradle` supplies common dependency, compilation, and resource
version expansion settings. `adapters/fabric/versions.json` pins the Minecraft, Yarn, Fabric
API, Cloth Config, Mod Menu, and Java versions for each build target.
It also declares `minecraft_range` (the runtime compatibility range) and
the optional `use_old_cloth_config_mod_id` flag (`true` uses `cloth-config2`;
omitted or `false` uses `cloth-config`). The existing `java`
and `fabric_resource_loader` fields also supply metadata values. The latter
declares the resource loader module suffix, such as `v0` or `v1`. The build substitutes
these values into the templates without inferring them from Minecraft versions.

## Loader organization

Fabric projects use `:fabric-<minecraft>` Gradle paths and live under
`adapters/fabric/<minecraft>/`. The Fabric version matrix is
`adapters/fabric/versions.json`; the outer metadata template is
`src/main/resources/fabric.mod.json`. The adapter container metadata
template is `adapters/fabric/resources/fabric.mod.json`; the build fills
its version and nested JAR list. The shared Mod Menu bridge lives
under `adapters/fabric/shared/java/` and is compiled once per Fabric adapter.
These directories contain only the current Fabric implementation; no NeoForge
projects or dependencies are configured.

The root build and universal release still target Fabric. In particular,
`Main` combines shared behavior with Fabric initialization and platform
discovery, and configuration uses Cloth Config's AutoConfig API. Supporting
another loader will require reviewing those boundaries, its configuration UI,
and its adapter discovery and packaging mechanism. The current directory
organization does not make those APIs loader-independent.

## Mod Menu integration

`adapters/fabric/shared/java/.../platform/ModMenuImpl.java` is the only source copy of the
Mod Menu bridge. `gradle/fabric-adpater.gradle` adds that source directory to every
adapter's compilation. Each compiled copy uses its own Minecraft types and
mappings, without relying on `Screen` having the same intermediary name across
versions. It forwards to `Main.configScreen()`, which
asks the selected platform to construct the native screen.

The outer metadata declares the Mod Menu entrypoint, so the configuration button
belongs to `clock-hud`; the implementation class comes from the selected adapter.
There is no separate integration subproject or runtime-generated bridge.
The version proxies retain the native Cloth Config screen construction call.

## Universal loading

The root `build.gradle` generates release and container metadata. The outer
release embeds `META-INF/jars/fabric-adapters.jar`, a metadata-only Fabric mod
with ID `clock-hud-fabric-adapters`. Its own `jars` list declares the remapped
adapters under its `META-INF/jars/` directory. Fabric Loader discovers these
nested mods recursively.

Each adapter's `storedJar` task repacks the final `remapJar` output with STORE
entries. The container also uses STORE for all entries. Only the outer JAR
compresses the container, allowing DEFLATE to reuse repeated content across
adapter boundaries. Intermediate artifacts remain under `build/intermediates/`
in their respective projects; distribute only the outer release.

All adapter candidates have the same
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

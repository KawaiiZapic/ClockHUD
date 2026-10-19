# Compatibility

## Scope

The universal release targets stable Minecraft releases from 1.17 to 1.21.11.
It does not promise compatibility with future releases, snapshots, other mod
loaders, or an arbitrary version of Fabric API / Cloth Config.

The historical repository starts at 1.18.2. Support for 1.17 is a new adapter,
not an existing implementation recovered from an older branch.

## Source ownership

- `src/main/java/.../Main`: Fabric startup, platform discovery, key actions, saving, menus, world lifecycle, and render guards.
- `src/main/java/.../ClientPlatform`: native operations, with opaque screen objects at the boundary.
- `src/main/java/.../core`: clock layout and animation, independent of Minecraft.
- `src/main/java/.../render`: shared rendering strategies, including legacy shader state management.
- `src/main/java/.../Textures`: shared texture identifier cache with a native identifier factory.
- `src/main/java/.../config`: the existing configuration schema and Cloth annotations.
- `src/main/resources/assets`: one copy of all textures and translations.
- `src/bridge/java/.../platform/ModMenuImpl`: one source copy, compiled separately by each adapter.
- `adapters/<minecraft>/src/main/java`: ordinary Java adapters for that Minecraft version.
- `adapters/<minecraft>/src/main/resources`: Fabric metadata and Mixin configuration.
- `gradle/adapter.gradle`: shared Gradle dependency and compilation settings.
- `versions.json`: build targets and dependency versions.

All behavior belongs in the main package, including behavior needed by only some
versions. Minecraft API calls belong in the relevant version directory. Adapters
must not decide key actions, menu flow, HUD visibility, layout, or drawing state
sequence. They register native callbacks, expose native state, construct/set native
screens, and execute primitive drawing operations. The outer mod's
`Main` obtains the selected adapter's `clock-hud:platform` entrypoint during
initialization. `HudBridge` forwards rendering callbacks to `Main.render()`.
The Mod Menu bridge forwards to `Main.configScreen()`.
`Main` also supplies the key category's legacy translation key and namespaced
identifier. The proxy maps these to the native category API and returns a
registrar for keys in that category; it does not define ClockHUD's categories.

The main package is an ordinary Java library without Minecraft type references.
Native types such as `Screen` are compiled and remapped separately for each
adapter, including its small Mod Menu bridge. This avoids requiring a single
compiled Screen reference to work across all supported versions. The main
package treats screens as `Object`, with native casts confined to version code.
There is no separate Mod Menu integration subproject.
The version-specific Cloth Config screen construction call stays in the proxy.
Mod Menu remains optional: its bridge is loaded by the Mod Menu entrypoint.

For example, `render/LegacyCanvas` owns blend setup, opacity conversion, and shader
color restoration for versions through 1.21.1. Their `MinecraftCanvas` proxies
implement the native operations it requests. Newer texture APIs accept the color
directly and need only a native draw call. No per-version rendering policy needs
to be copied between adapters.

A little repeated native glue is intentional: every adapter can be read and
edited as normal Java without understanding a preprocessor.

The adapters are compiled and remapped independently. Their identical package
names and Mixin names are intentional: only one candidate is loaded. Do not
flatten their classes into the outer JAR or list all adapters as root mods.

## Build targets

| Compiled against | Allowed Minecraft range | Java |
| --- | --- | --- |
| 1.17.1 | 1.17 - 1.17.1 | 16 |
| 1.18.2 | 1.18 - 1.18.2 | 17 |
| 1.19.2 | 1.19 - 1.19.2 | 17 |
| 1.19.3 | 1.19.3 | 17 |
| 1.19.4 | 1.19.4 | 17 |
| 1.20.1 | 1.20 - 1.20.1 | 17 |
| 1.20.4 | 1.20.2 - 1.20.4 | 17 |
| 1.20.6 | 1.20.5 - 1.20.6 | 21 |
| 1.21.1 | 1.21 - 1.21.1 | 21 |
| 1.21.3 | 1.21.2 - 1.21.3 | 21 |
| 1.21.4 | 1.21.4 | 21 |
| 1.21.5 | 1.21.5 | 21 |
| 1.21.8 | 1.21.6 - 1.21.8 | 21 |
| 1.21.10 | 1.21.9 - 1.21.10 | 21 |
| 1.21.11 | 1.21.11 | 21 |

The table is descriptive; each adapter's `fabric.mod.json` declares its supported
Minecraft range. `versions.json` pins build dependencies. Each adapter requires
Cloth Config with a `*` version range, leaving game compatibility to Cloth
Config's own metadata. The 1.17 adapter uses its historical `cloth-config2`
mod ID; the others use `cloth-config`. Players install the matching Fabric API,
Cloth Config, and optional Mod Menu themselves.

The outer mod does not declare a Minecraft version range. Its required
`clock-hud-adapter` dependency makes compatibility depend on an available adapter
whose own Minecraft and other dependency constraints can be satisfied. If no
adapter matches, Fabric Loader rejects the dependency set before initialization.

## Validation

`./gradlew build` compiles and remaps the 15 build targets and assembles the
universal JAR. Compilation alone does not prove runtime compatibility with all
patch versions.
Before publishing, launch the same release JAR on supported versions with their
matching dependencies. Check F10, Home, Mod Menu, existing settings, opacity,
scaling, a day transition, switching worlds, and disconnecting/reconnecting.
Record client validation separately from build results.

Fabric's nested-mod behavior:
https://wiki.fabricmc.net/documentation:fabric_loader

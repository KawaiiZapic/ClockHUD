package moe.zapic.clockhud;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Cache native identifiers without depending on a Minecraft Identifier class.
 */
public final class Textures<T> {
    private final Map<String, T> cache = new HashMap<>();
    private final BiFunction<String, String, T> identifier;

    public Textures(BiFunction<String, String, T> identifier) {
        this.identifier = identifier;
    }

    public T get(String name) {
        return cache.computeIfAbsent(name, path -> identifier.apply("clock-hud", path));
    }
}

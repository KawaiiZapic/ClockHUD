package moe.zapic.clockhud.core;

public final class WorldClock {
    public static final int DAY_TICKS = 24000;
    public static final int NIGHT_START = 13000;

    private WorldClock() {}

    public static boolean isDay(long worldTime) {
        return Math.floorMod(worldTime, DAY_TICKS) < NIGHT_START;
    }

    public static float progress(long worldTime) {
        int time = Math.floorMod(worldTime, DAY_TICKS);
        return time < NIGHT_START
                ? time / (float) NIGHT_START
                : (time - NIGHT_START) / (float) (DAY_TICKS - NIGHT_START);
    }
}

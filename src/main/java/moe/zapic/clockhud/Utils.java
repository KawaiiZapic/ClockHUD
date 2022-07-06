package moe.zapic.clockhud;

import net.minecraft.client.MinecraftClient;

public class Utils {
    public static final int DAY_TICKS = 24000;
    public static final int NIGHT_START = 13000;
    public static final int DAY_START = 0;

    public static final float DAY_LONG = (float) (NIGHT_START - DAY_START);
    public static final float NIGHT_LONG = (float) (DAY_TICKS + DAY_START - NIGHT_START);

    public static int getWorldTime() {
        assert MinecraftClient.getInstance().world != null;
        return (int) MinecraftClient.getInstance().world.getTimeOfDay() % DAY_TICKS;
    }

    public static boolean isDay() {
        var tick = getWorldTime();
        return tick >= DAY_START && tick < NIGHT_START;
    }

    public static float getScaleTime() {
        var time = getWorldTime();
        if (isDay()) {
            return (time - DAY_START) / DAY_LONG;
        } else {
            return (time - NIGHT_START) / NIGHT_LONG;
        }
    }
}

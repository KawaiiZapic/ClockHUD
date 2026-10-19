package moe.zapic.clockhud.core;

/**
 * Coordinates use the current GUI transform; colors are ARGB.
 */
public interface HudCanvas {
    void push();
    void pop();
    void scale(float x, float y);
    void translate(float x, float y);
    void texture(String name, int x, int y, int width, int height, int color);
    void centeredText(String translationKey, long argument, int x, int y, int color);
}

package moe.zapic.clockhud.render;

import moe.zapic.clockhud.core.HudCanvas;

/**
 * Texture rendering for versions that use RenderSystem's global shader color.
 * The version proxy supplies only the native operations.
 */
public abstract class LegacyCanvas implements HudCanvas {
    @Override
    public final void texture(String name, int x, int y, int width, int height, int color) {
        enableBlend();
        defaultBlendFunc();
        disableDepthTest();
        shaderColor(1, 1, 1, (color >>> 24) / 255f);
        try {
            blit(name, x, y, width, height);
        } finally {
            shaderColor(1, 1, 1, 1);
        }
    }

    protected abstract void enableBlend();
    protected abstract void defaultBlendFunc();
    protected abstract void disableDepthTest();
    protected abstract void shaderColor(float red, float green, float blue, float alpha);
    protected abstract void blit(String name, int x, int y, int width, int height);
}

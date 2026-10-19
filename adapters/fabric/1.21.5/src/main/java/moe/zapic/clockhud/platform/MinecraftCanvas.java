package moe.zapic.clockhud.platform;

import moe.zapic.clockhud.Textures;
import moe.zapic.clockhud.core.HudCanvas;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

final class MinecraftCanvas implements HudCanvas {
    private static final Textures<Identifier> TEXTURES = new Textures<>(Identifier::of);
    private final DrawContext context;

    MinecraftCanvas(DrawContext context) {
        this.context = context;
    }

    @Override
    public void push() {
        context.getMatrices().push();
    }

    @Override
    public void pop() {
        context.getMatrices().pop();
    }

    @Override
    public void scale(float x, float y) {
        context.getMatrices().scale(x, y, 1);
    }

    @Override
    public void translate(float x, float y) {
        context.getMatrices().translate(x, y, 0);
    }

    @Override
    public void texture(String name, int x, int y, int width, int height, int color) {
        context.drawTexture(net.minecraft.client.render.RenderLayer::getGuiTextured, TEXTURES.get(name), x, y, 0f, 0f, width, height, width, height, color);
    }

    @Override
    public void centeredText(String key, long argument, int x, int y, int color) {
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,
                net.minecraft.text.Text.translatable(key, argument), x, y, color);
    }
}

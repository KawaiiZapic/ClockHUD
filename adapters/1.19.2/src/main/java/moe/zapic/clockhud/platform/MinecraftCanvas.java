package moe.zapic.clockhud.platform;

import com.mojang.blaze3d.systems.RenderSystem;
import moe.zapic.clockhud.Textures;
import moe.zapic.clockhud.render.LegacyCanvas;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.util.Identifier;

final class MinecraftCanvas extends LegacyCanvas {
    private static final Textures<Identifier> TEXTURES = new Textures<>(Identifier::new);
    private final MatrixStack matrices;

    MinecraftCanvas(MatrixStack matrices) {
        this.matrices = matrices;
    }

    @Override
    public void push() {
        matrices.push();
    }

    @Override
    public void pop() {
        matrices.pop();
    }

    @Override
    public void scale(float x, float y) {
        matrices.scale(x, y, 1);
    }

    @Override
    public void translate(float x, float y) {
        matrices.translate(x, y, 0);
    }

    @Override
    protected void enableBlend() {
        RenderSystem.enableBlend();
    }

    @Override
    protected void defaultBlendFunc() {
        RenderSystem.defaultBlendFunc();
    }

    @Override
    protected void disableDepthTest() {
        RenderSystem.disableDepthTest();
    }

    @Override
    protected void shaderColor(float red, float green, float blue, float alpha) {
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    @Override
    protected void blit(String name, int x, int y, int width, int height) {
        RenderSystem.setShaderTexture(0, TEXTURES.get(name));
        DrawableHelper.drawTexture(matrices, x, y, 0f, 0f, width, height, width, height);
    }

    @Override
    public void centeredText(String key, long argument, int x, int y, int color) {
        DrawableHelper.drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer,
                net.minecraft.text.Text.translatable(key, argument), x, y, color);
    }
}

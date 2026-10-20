package moe.zapic.clockhud.platform;

import moe.zapic.clockhud.Textures;
import moe.zapic.clockhud.core.HudCanvas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

final class MinecraftCanvas implements HudCanvas {
    private static final Textures<Identifier> TEXTURES = new Textures<>(Identifier::fromNamespaceAndPath);
    private final GuiGraphicsExtractor context;

    MinecraftCanvas(GuiGraphicsExtractor context) {
        this.context = context;
    }

    @Override
    public void push() {
        context.pose().pushMatrix();
    }

    @Override
    public void pop() {
        context.pose().popMatrix();
    }

    @Override
    public void scale(float x, float y) {
        context.pose().scale(x, y);
    }

    @Override
    public void translate(float x, float y) {
        context.pose().translate(x, y);
    }

    @Override
    public void texture(String name, int x, int y, int width, int height, int color) {
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURES.get(name), x, y, 0f, 0f, width, height, width, height, color);
    }

    @Override
    public void centeredText(String key, long argument, int x, int y, int color) {
        context.centeredText(Minecraft.getInstance().font,
                Component.translatable(key, argument), x, y, color);
    }
}

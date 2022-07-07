package moe.zapic.clockhud.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import moe.zapic.clockhud.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public class ConfigScreenBuilder {
    public static Screen getConfigScreen() {
        var config = Main.config;
        var builder = ConfigBuilder.create()
                 .setParentScreen(MinecraftClient.getInstance().currentScreen)
                 .setTitle(new TranslatableText("text.autoconfig.clock-hud.title"));
        var category = builder.getOrCreateCategory(new TranslatableText("text.autoconfig.clock-hud.title"));
        var entry = builder.entryBuilder();
        category.addEntry(entry
                .startBooleanToggle(new TranslatableText("text.autoconfig.clock-hud.option.isShow"), config.isShow)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.isShow = v)
                .build()
        );
        category.addEntry(entry
                .startBooleanToggle(new TranslatableText("text.autoconfig.clock-hud.option.showDayCount"), config.showDayCount)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.showDayCount = v)
                .build()
        );
        category.addEntry(entry
                .startIntField(new TranslatableText("text.autoconfig.clock-hud.option.ScreenX"), config.ScreenX)
                .setDefaultValue(4)
                .setSaveConsumer(v -> config.ScreenX = v)
                .build()
        );
        category.addEntry(entry
                .startIntField(new TranslatableText("text.autoconfig.clock-hud.option.ScreenY"), config.ScreenY)
                .setDefaultValue(4)
                .setSaveConsumer(v -> config.ScreenY = v)
                .build()
        );
        category.addEntry(entry
                .startIntSlider(new TranslatableText("text.autoconfig.clock-hud.option.Scale"), config.Scale, 25, 200)
                .setDefaultValue(100)
                .setTextGetter(v -> new TranslatableText("text.autoconfig.clock-hud.option.ScalePercent", v))
                .setSaveConsumer(v -> config.Scale = v)
                .build()
        );
        category.addEntry(entry
                .startIntSlider(new TranslatableText("text.autoconfig.clock-hud.option.Opacity"), config.Opacity, 0, 255)
                .setDefaultValue(255)
                .setTextGetter(v -> new TranslatableText("text.autoconfig.clock-hud.option.ScalePercent", (int)(v/2.55f)))
                .setSaveConsumer(v -> config.Opacity = v)
                .build()
        );
        return builder.build();
    }
}

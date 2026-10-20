package moe.zapic.clockhud.platform;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import moe.zapic.clockhud.Main;

public final class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuImpl::createScreen;
    }

    @SuppressWarnings("unchecked")
    private static <S> S createScreen(S parent) {
        return (S) Main.configScreen(parent);
    }
}

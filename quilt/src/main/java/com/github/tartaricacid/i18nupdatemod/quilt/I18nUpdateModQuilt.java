package com.github.tartaricacid.i18nupdatemod.quilt;

import com.github.tartaricacid.i18nupdatemod.I18nUpdateMod;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class I18nUpdateModQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        I18nUpdateMod.init();
    }
}

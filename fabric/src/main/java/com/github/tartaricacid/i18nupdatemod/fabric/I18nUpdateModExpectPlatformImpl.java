package com.github.tartaricacid.i18nupdatemod.fabric;

import com.github.tartaricacid.i18nupdatemod.I18nUpdateMod;
import com.github.tartaricacid.i18nupdatemod.I18nUpdateModExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class I18nUpdateModExpectPlatformImpl {
    /**
     * This is our actual method to {@link I18nUpdateModExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
    public static String isDownloadLink(){
        return "https://ghproxy.com/https://raw.githubusercontent.com/zkitefly/TranslationPackConvert/main/files/Minecraft-Mod-Language-Modpack-1-18T1-19-1o2.zip";
    }
    public static String isMD5Link() {
        return "https://ghproxy.com/https://raw.githubusercontent.com/zkitefly/TranslationPackConvert/main/files/1.18-fabrict1.19.1o2-fabric.md5";
    }
    public static Path isMD5Path() {
        return I18nUpdateMod.CACHE_DIR.resolve("1.18-fabrict1.19.1o2-fabric.md5");
    }
    public static String isPackName() {
        return "Minecraft-Mod-Language-Modpack-1-18-Fabric.zip";
    }
}

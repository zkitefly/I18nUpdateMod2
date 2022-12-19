package com.github.tartaricacid.i18nupdatemod;

import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class I18nUpdateMod {
    public static final String MOD_ID = "i18nupdatemod";
    public static final Path CACHE_DIR = Paths.get(System.getProperty("user.home"), "." + MOD_ID, "1.18");
    public static final Path RESOURCE_FOLDER = Paths.get(MinecraftClient.getInstance().runDirectory.getPath(), "resourcepacks");
    public static final Path LOCAL_LANGUAGE_PACK = RESOURCE_FOLDER.resolve(I18nUpdateModExpectPlatform.isPackName());
    public static final Path LANGUAGE_PACK = CACHE_DIR.resolve(I18nUpdateModExpectPlatform.isPackName());
    public static final Path LANGUAGE_MD5 = I18nUpdateModExpectPlatform.isMD5Path();
    public static final String LINK = I18nUpdateModExpectPlatform.isDownloadLink();
    public static final String MD5 = I18nUpdateModExpectPlatform.isMD5Link();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static String MD5String = "";
    
    public static void init() {
        
        System.out.println(I18nUpdateModExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}

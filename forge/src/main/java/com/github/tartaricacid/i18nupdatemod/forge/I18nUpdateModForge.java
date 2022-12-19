package com.github.tartaricacid.i18nupdatemod.forge;

import com.github.tartaricacid.i18nupdatemod.I18nUpdateModExpectPlatform;
import dev.architectury.hooks.PackRepositoryHooks;
import dev.architectury.platform.forge.EventBuses;
import com.github.tartaricacid.i18nupdatemod.I18nUpdateMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

@Mod(I18nUpdateMod.MOD_ID)
public class I18nUpdateModForge {
    public I18nUpdateModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(I18nUpdateMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        I18nUpdateMod.init();

        MinecraftClient.getInstance().options.language = "zh_cn";

        // 检查主资源包目录是否存在
        if (!Files.isDirectory(I18nUpdateMod.CACHE_DIR)) {
            try {
                Files.createDirectories(I18nUpdateMod.CACHE_DIR);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // 检查游戏下资源包目录
        if (!Files.isDirectory(I18nUpdateMod.RESOURCE_FOLDER)) {
            try {
                Files.createDirectories(I18nUpdateMod.RESOURCE_FOLDER);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // 尝试加载 MD5 文件
        try {
            FileUtils.copyURLToFile(new URL(I18nUpdateMod.MD5), I18nUpdateMod.LANGUAGE_MD5.toFile());
        } catch (IOException e) {
            e.printStackTrace();
            I18nUpdateMod.LOGGER.error("Download MD5 failed.");
            setResourcesRepository();
            return;
        }
        try {
            StringBuilder stringBuffer = new StringBuilder();
            List<String> lines = Files.readAllLines(I18nUpdateMod.LANGUAGE_MD5);
            for (String line : lines) {
                stringBuffer.append(line);
                I18nUpdateMod.MD5String = stringBuffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            setResourcesRepository();
            return;
        }

        if (Files.exists(I18nUpdateMod.LANGUAGE_PACK)) {
            String md5;

            try {
                InputStream stream = Files.newInputStream(I18nUpdateMod.LANGUAGE_PACK);
                md5 = DigestUtils.md5Hex(stream).toUpperCase();
            } catch (IOException e) {
                e.printStackTrace();
                I18nUpdateMod.LOGGER.error("Error when compute md5.");
                setResourcesRepository();
                return;
            }

            try {
                if (!md5.equals(I18nUpdateMod.MD5String)) {
                    // TODO：阻塞式下载必不可少，但是否应该增加提示？
                    FileUtils.copyURLToFile(new URL(I18nUpdateMod.LINK), I18nUpdateMod.LANGUAGE_PACK.toFile());
                    InputStream stream = Files.newInputStream(I18nUpdateMod.LANGUAGE_PACK);
                    md5 = DigestUtils.md5Hex(stream).toUpperCase();
                    // 说明有可能下载损坏，就不要复制后加载了
                    if (!md5.equals(I18nUpdateMod.MD5String)) {
                        setResourcesRepository();
                        return;
                    }
                    if (Files.exists(I18nUpdateMod.LOCAL_LANGUAGE_PACK)) {
                        Files.delete(I18nUpdateMod.LOCAL_LANGUAGE_PACK);
                    }
                    Files.copy(I18nUpdateMod.LANGUAGE_PACK, I18nUpdateMod.LOCAL_LANGUAGE_PACK);
                }
            } catch (MalformedURLException e) {
                I18nUpdateMod.LOGGER.error("Download language pack failed.");
                e.printStackTrace();
                setResourcesRepository();
                return;
            } catch (IOException e) {
                I18nUpdateMod.LOGGER.error("Error when copy file.");
                e.printStackTrace();
                setResourcesRepository();
                return;
            }
        } else {
            try {
                FileUtils.copyURLToFile(new URL(I18nUpdateMod.LINK), I18nUpdateMod.LANGUAGE_PACK.toFile());
                Files.copy(I18nUpdateMod.LANGUAGE_PACK, I18nUpdateMod.LOCAL_LANGUAGE_PACK);
            } catch (IOException e) {
                I18nUpdateMod.LOGGER.error("Download language pack failed.");
                e.printStackTrace();
                return;
            }
        }

        if (!Files.exists(I18nUpdateMod.LOCAL_LANGUAGE_PACK)) {
            try {
                Files.copy(I18nUpdateMod.LANGUAGE_PACK, LOCAL_LANGUAGE_PACK);
            } catch (IOException e) {
                e.printStackTrace();
                I18nUpdateMod.LOGGER.error("Error when copy file.");
                return;
            }
        }

        if (Files.exists(I18nUpdateMod.LOCAL_LANGUAGE_PACK)) {
            try {
                String md5;
                try {
                    InputStream is = Files.newInputStream(I18nUpdateMod.LOCAL_LANGUAGE_PACK);
                    md5 = DigestUtils.md5Hex(is).toUpperCase();
                } catch (IOException e) {
                    e.printStackTrace();
                    I18nUpdateMod.LOGGER.error("Error when compute md5.");
                    return;
                }
                if (!md5.equals(I18nUpdateMod.MD5String)) {
                    Files.delete(I18nUpdateMod.LOCAL_LANGUAGE_PACK);
                    Files.copy(I18nUpdateMod.LANGUAGE_PACK, I18nUpdateMod.LOCAL_LANGUAGE_PACK);
                }
                setResourcesRepository();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setResourcesRepository() {
        MinecraftClient mc = MinecraftClient.getInstance();
        GameOptions gameSettings = mc.options;
        // 在 gameSetting 中加载资源包
        if (!gameSettings.resourcePacks.contains(I18nUpdateModExpectPlatform.isPackName())) {
            mc.options.resourcePacks.add(I18nUpdateModExpectPlatform.isPackName());
        } else {
            List<String> packs = new ArrayList<>(10);
            // 资源包的 index 越小优先级越低（在资源包 GUI 中置于更低层）
            packs.add(I18nUpdateModExpectPlatform.isPackName());
            packs.addAll(gameSettings.resourcePacks);
            gameSettings.resourcePacks = packs;
        }
        reloadResources();
    }

    public static void reloadResources() {
        MinecraftClient mc = MinecraftClient.getInstance();
        // 因为这时候资源包已经加载了，所以需要重新读取，重新加载
        ResourceManager resourcePackRepository = mc.getResourcePackDir();
        try {
            resourcePackRepository.reload();
        } catch (ConcurrentModificationException ignore) {
        }
    }
}

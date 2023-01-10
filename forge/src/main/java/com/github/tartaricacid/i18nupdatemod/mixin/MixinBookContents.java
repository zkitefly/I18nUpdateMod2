package com.github.tartaricacid.i18nupdatemod.mixin;

import com.github.tartaricacid.i18nupdatemod.I18nUpdateMod;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.patchouli.client.book.BookContents;

import java.io.IOException;
import java.io.InputStream;

@Mixin(BookContents.class)
public class MixinBookContents {
    @Inject(at = @At("HEAD"), method = "loadJson", cancellable = true, remap = false)
    private void loadJson(ResourceLocation resloc, ResourceLocation fallback, CallbackInfoReturnable<InputStream> cir) {
        I18nUpdateMod.LOGGER.debug("[Patchouli] loading json from {}.", resloc);
        try {
            cir.setReturnValue(Minecraft.getInstance().getResourceManager().getResource(resloc).getInputStream());
        } catch (IOException ignore) {
        }
    }
}

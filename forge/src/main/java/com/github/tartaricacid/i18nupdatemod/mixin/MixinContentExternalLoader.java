/*
Credit: https://github.com/kappa-maintainer/PRP
*/
package com.github.tartaricacid.i18nupdatemod.mixin;

import com.github.tartaricacid.i18nupdatemod.I18nUpdateMod;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.patchouli.client.book.BookContentExternalLoader;
import vazkii.patchouli.common.book.Book;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

@Mixin(BookContentExternalLoader.class)
public class MixinContentExternalLoader {
    @Inject(at = @At("HEAD"), method = "loadJson", cancellable = true, remap = false)
    private void loadJson(Book book, ResourceLocation resloc, @Nullable ResourceLocation fallback, CallbackInfoReturnable<InputStream> cir) {
        I18nUpdateMod.LOGGER.debug("loading json from {}.", resloc);
        try {
            cir.setReturnValue(Minecraft.getInstance().getResourceManager().getResource(resloc).getInputStream());
        } catch (IOException e) {
            //no-op
        }
    }
}
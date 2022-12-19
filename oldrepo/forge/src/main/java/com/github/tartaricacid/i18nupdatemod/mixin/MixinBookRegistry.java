/*
Credit: https://github.com/kappa-maintainer/PRP
1.19.x Version :https://github.com/RPMTW/RPMTW-Platform-Mod/blob/mc-1.19/forge/src/main/java/com/rpmtw/rpmtw_platform_mod/forge/mixins
*/
package com.github.tartaricacid.i18nupdatemod.mixin;

import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.patchouli.client.book.ClientBookRegistry;
import vazkii.patchouli.common.base.PatchouliConfig;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.util.Map;

@Mixin(value = BookRegistry.class, remap = false)
public class MixinBookRegistry {
    @Shadow
    @Final
    public Map<ResourceLocation, Book> books;

    @Shadow
    private boolean loaded;

    @Inject(method = "reloadContents", at = @At("HEAD"), cancellable = true)
    public void reloadContents(boolean resourcePackBooksOnly, CallbackInfo ci) {
        PatchouliConfig.reloadBuiltinFlags();
        for (Book book : books.values()) {
            book.reloadContents(false);
        }
        ClientBookRegistry.INSTANCE.reloadLocks(false);
        loaded = true;
        ci.cancel();
    }
}
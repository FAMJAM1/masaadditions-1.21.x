package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import com.red.masaadditions.tweakeroo_additions.config.FeatureToggleExtended;
import com.red.masaadditions.tweakeroo_additions.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MixinMinecraftClient {
    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void onProcessKeybindsPre(CallbackInfo ci)
    {
        if (((Minecraft) (Object) this).screen == null)
        {
            if (FeatureToggleExtended.TWEAK_MOVEMENT_HOLD.getBooleanValue())
            {
                for (KeyMapping movementKey : MiscUtils.MOVEMENT_HOLD_KEYS) {
                    movementKey.setDown(true);
                }
            }
        }
    }

    @Inject(method = "getWindowTitle", at = @At("HEAD"), cancellable = true)
    private void getWindowTitle(CallbackInfoReturnable<String> cir) {
        if (FeatureToggleExtended.TWEAK_OVERRIDE_WINDOW_TITLE.getBooleanValue()) {
            cir.setReturnValue(ConfigsExtended.Generic.WINDOW_TITLE_OVERRIDE.getStringValue());
        }
    }
}
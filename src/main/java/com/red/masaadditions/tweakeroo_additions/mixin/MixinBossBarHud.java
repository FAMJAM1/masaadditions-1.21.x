package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public abstract class MixinBossBarHud {
    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void render(GuiGraphicsExtractor drawContext, CallbackInfo ci) {
        if (ConfigsExtended.Disable.DISABLE_BOSS_BAR_RENDERING.getBooleanValue())
            ci.cancel();
    }
}

package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinInGameHud {
    @Inject(method = "displayScoreboardSidebar(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/scores/Objective;)V", at = @At("HEAD"), cancellable = true)
    private void getOffsetType(GuiGraphics graphics, Objective objective, CallbackInfo ci) {
        if (ConfigsExtended.Disable.DISABLE_SCOREBOARD_SIDEBAR_RENDERING.getBooleanValue()) {
            ci.cancel();
        }
    }

    @ModifyConstant(method = "displayScoreboardSidebar(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/scores/Objective;)V", constant = @Constant(longValue = 15))
    private long scoreboardSidebarMaxLength(long val) {
        return ConfigsExtended.Generic.SCOREBOARD_SIDEBAR_MAX_LENGTH.getIntegerValue();
    }
}
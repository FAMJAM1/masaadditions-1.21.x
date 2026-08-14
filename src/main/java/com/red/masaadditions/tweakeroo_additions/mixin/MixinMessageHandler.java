package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatListener.class)
public class MixinMessageHandler {
    @Inject(method = "handleSystemMessage", at = @At("HEAD"), cancellable = true)
    public void onGameMessage(Component message, boolean overlay, CallbackInfo ci) {
        if (ConfigsExtended.Disable.DISABLE_SLEEPING_NOTIFICATION.getBooleanValue() && message.getContents() instanceof TranslatableContents text && (text.getKey().equals("sleep.skipping_night") || text.getKey().equals("sleep.players_sleeping")))
            ci.cancel();
    }
}
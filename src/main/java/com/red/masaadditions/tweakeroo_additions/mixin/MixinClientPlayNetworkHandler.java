package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.FeatureToggleExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPlayNetworkHandler {
    // From UsefulMod by nessie
    @Inject(method = "onDeathMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    private void onPlayerDeath(ClientboundPlayerCombatKillPacket packet, CallbackInfo ci) {
        // LocalPlayer::showDeathScreen will prevent tweakPrintDeathCoordinates from working
        LocalPlayer player = Minecraft.getInstance().player;
        if (FeatureToggleExtended.TWEAK_RESPAWN_ON_DEATH.getBooleanValue() && player != null) {
            player.requestRespawn();
        }
    }
}

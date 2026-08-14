package com.red.masaadditions.tweakeroo_additions.mixin;

import com.mojang.authlib.GameProfile;
import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayer {
    @Shadow
    public abstract boolean isSubmergedInWater();

    public MixinClientPlayerEntity(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "canStartSprinting", at = @At("HEAD"), cancellable = true)
    private void setSprinting(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigsExtended.Disable.DISABLE_SPRINTING_UNDERWATER.getBooleanValue() && ((this.isInWater() && !this.isSubmergedInWater()) || (ConfigsExtended.Disable.DISABLE_SWIMMING.getBooleanValue()))) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canSpawnSprintParticle", at = @At("HEAD"), cancellable = true)
    private void shouldSpawnSprintingParticles(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigsExtended.Disable.DISABLE_FOOTSTEP_PARTICLES.getBooleanValue())
            cir.setReturnValue(false);
    }
}
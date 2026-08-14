package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Renderers hand their state to a collector now rather than drawing, so skipping
// the extraction is what leaves the beam undrawn.
@Mixin(BeaconRenderer.class)
public abstract class MixinBeaconBlockEntityRenderer {
    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void render(CallbackInfo ci) {
        if (ConfigsExtended.Disable.DISABLE_BEACON_BEAM_RENDERING.getBooleanValue()) {
            ci.cancel();
        }
    }
}

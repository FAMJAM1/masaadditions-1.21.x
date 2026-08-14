package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import com.red.masaadditions.tweakeroo_additions.config.FeatureToggleExtended;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// The day/night cycle runs off a world clock now, so the override hooks the level's
// own clock reading instead of the old lunar time on the world interface.
@Mixin(Level.class)
public abstract class MixinWorldAccess {
    @Inject(method = "getDefaultClockTime", at = @At("HEAD"), cancellable = true)
    private void overrideSkyTime(CallbackInfoReturnable<Long> cir) {
        if (FeatureToggleExtended.TWEAK_OVERRIDE_SKY_TIME.getBooleanValue()) {
            cir.setReturnValue((long) ConfigsExtended.Generic.SKY_TIME_OVERRIDE.getIntegerValue());
        }
    }
}

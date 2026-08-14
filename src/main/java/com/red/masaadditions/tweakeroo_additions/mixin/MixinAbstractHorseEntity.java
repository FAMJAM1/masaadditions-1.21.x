package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.FeatureToggleExtended;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractHorse.class)
public class MixinAbstractHorseEntity {
    @Redirect(method = "getControllingPassenger()Lnet/minecraft/world/entity/LivingEntity;", require = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/equine/AbstractHorse;isSaddled()Z"))
    public boolean spoofIsSaddled(AbstractHorse entity) {
        if (FeatureToggleExtended.TWEAK_LLAMA_STEERING.getBooleanValue() && (Object) this instanceof Llama && ((Llama) (Object) this).getCarpetColor() != null) // The only way to know on the client that the Llama has a Carpet
        {
            return true;
        }

        return entity.isSaddled();
    }
}

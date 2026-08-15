package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.FeatureToggleExtended;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractHorse.class)
public class MixinAbstractHorseEntity {
    @Redirect(method = "getControllingPassenger()Lnet/minecraft/world/entity/LivingEntity;", require = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;isSaddled()Z"))
    public boolean spoofIsSaddled(AbstractHorse entity) {
        // The carpet is an equipment slot now, so the client reads it off the body slot
        if (FeatureToggleExtended.TWEAK_LLAMA_STEERING.getBooleanValue() && (Object) this instanceof Llama && !((Llama) (Object) this).getItemBySlot(EquipmentSlot.BODY).isEmpty())
        {
            return true;
        }

        return entity.isSaddled();
    }
}

package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Chicken.class)
public abstract class MixinChickenEntity extends Animal {
    public MixinChickenEntity(EntityType<? extends Chicken> entityType, Level world) {
        super(entityType, world);
    }

    // From CutelessMod by nessie
    @Inject(method = "aiStep", at = @At("RETURN"))
    private void derpyChicken(CallbackInfo ci) {
        if (ConfigsExtended.Generic.DERPY_CHICKEN.getBooleanValue())
            this.setXRot(-90F);
    }
}

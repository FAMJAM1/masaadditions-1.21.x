package com.red.masaadditions.tweakeroo_additions.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.client.item.ModelPredicateProviderRegistry$1")
public class MixinModelPredicateProviderRegistry {
    @Redirect(method = "Lnet/minecraft/client/item/ModelPredicateProviderRegistry$1;unclampedCall(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/LivingEntity;I)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getSkyAngle(F)F"))
    private float getSkyAngle(ClientLevel clientWorld, float tickDelta) {
        return clientWorld.getDimension().getSkyAngle(clientWorld.getLevelData().getDayTime());
    }
}
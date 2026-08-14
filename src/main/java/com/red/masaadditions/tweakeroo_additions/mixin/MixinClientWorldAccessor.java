package com.red.masaadditions.tweakeroo_additions.mixin;

import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientLevel.class)
public interface MixinClientWorldAccessor {
    @Accessor("pendingUpdateManager")
    BlockStatePredictionHandler tweakermore_getPendingUpdateManager();
}

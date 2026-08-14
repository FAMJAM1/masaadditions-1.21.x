package com.red.masaadditions.tweakeroo_additions.mixin;

import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockStatePredictionHandler.PendingUpdate.class)
public interface MixinPendingUpdateAccessor {
    @Accessor
    int getSequence();
}

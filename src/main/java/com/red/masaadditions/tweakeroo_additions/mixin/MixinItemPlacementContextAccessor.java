package com.red.masaadditions.tweakeroo_additions.mixin;

import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockPlaceContext.class)
public interface MixinItemPlacementContextAccessor {
    @Accessor
    void setCanReplaceExisting(boolean canReplaceExisting);
}

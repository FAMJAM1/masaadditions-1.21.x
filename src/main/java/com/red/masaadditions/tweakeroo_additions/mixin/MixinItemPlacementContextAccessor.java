package com.red.masaadditions.tweakeroo_additions.mixin;

import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockPlaceContext.class)
public interface MixinItemPlacementContextAccessor {
    // Named outright: the field is replaceClicked, not the method's own name
    @Accessor("replaceClicked")
    void setCanReplaceExisting(boolean canReplaceExisting);
}

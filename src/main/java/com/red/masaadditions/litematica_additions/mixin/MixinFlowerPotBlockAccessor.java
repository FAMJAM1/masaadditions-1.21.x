package com.red.masaadditions.litematica_additions.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FlowerPotBlock.class)
public interface MixinFlowerPotBlockAccessor {
    // The flower a pot holds; the field is called potted now
    @Accessor("potted")
    Block getContent();
}

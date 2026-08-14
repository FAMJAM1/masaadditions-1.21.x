package com.red.masaadditions.tweakeroo_additions.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.class)
public interface MixinAbstractBlockAccessor {
    @Mutable
    @Accessor("speedFactor")
    void setVelocityMultiplier(float velocityMultiplier);

    @Mutable
    @Accessor("jumpFactor")
    void setJumpVelocityMultiplier(float jumpVelocityMultiplier);
}

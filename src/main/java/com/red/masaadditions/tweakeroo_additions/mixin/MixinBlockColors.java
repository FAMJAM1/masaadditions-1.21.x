package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.FeatureToggleExtended;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.Color;

// Colouring moved from one registry lookup to a tint source per block, and this
// is the one that still knows the position.
@Mixin(BlockTintSource.class)
public interface MixinBlockColors {
    // From UsefulMod by nessie
    @Inject(method = "colorInWorld", at = @At("HEAD"), cancellable = true)
    private void getColor(BlockState state, BlockAndTintGetter world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (!FeatureToggleExtended.TWEAK_RAINBOW_LEAVES.getBooleanValue() || pos == null || !(state.getBlock() instanceof LeavesBlock)) {
            return;
        }

        final int sc = 1024;
        final float hue = this.dist(pos.getX(), 32 * pos.getY(), pos.getX() + pos.getZ()) % sc / sc;
        cir.setReturnValue(Color.HSBtoRGB(hue, 0.7F, 1F));
    }

    @Unique
    private float dist(int x, int y, int z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
}

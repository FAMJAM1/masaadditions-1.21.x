package com.red.masaadditions.tweakeroo_additions.util;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Set;

// Stands in for the tint source leaves would otherwise use, so the colour follows
// wherever the block is drawn from. Everything without a position falls through to
// the original, which is what the inventory and the particles ask for. Cherry and
// pale oak have no original to fall through to: they are drawn straight from the
// texture, and this is the only source they ever get.
// From UsefulMod by nessie
public record RainbowLeavesTint(@Nullable BlockTintSource original) implements BlockTintSource {
    private static final int NO_TINT = -1;

    @Override
    public int color(BlockState state) {
        return this.original != null ? this.original.color(state) : NO_TINT;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter world, BlockPos pos) {
        final int sc = 1024;
        final float hue = dist(pos.getX(), 32 * pos.getY(), pos.getX() + pos.getZ()) % sc / sc;
        return Color.HSBtoRGB(hue, 0.7F, 1F);
    }

    @Override
    public Set<Property<?>> relevantProperties() {
        return this.original != null ? this.original.relevantProperties() : Set.of();
    }

    private static float dist(int x, int y, int z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
}

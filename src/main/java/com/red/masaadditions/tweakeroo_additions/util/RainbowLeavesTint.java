package com.red.masaadditions.tweakeroo_additions.util;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.awt.Color;
import java.util.Set;

// Stands in for the tint source leaves would otherwise use, so the colour follows
// wherever the block is drawn from. Everything without a position falls through to
// the original, which is what the inventory and the particles ask for.
// From UsefulMod by nessie
public record RainbowLeavesTint(BlockTintSource original) implements BlockTintSource {
    @Override
    public int color(BlockState state) {
        return this.original.color(state);
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter world, BlockPos pos) {
        final int sc = 1024;
        final float hue = dist(pos.getX(), 32 * pos.getY(), pos.getX() + pos.getZ()) % sc / sc;
        return Color.HSBtoRGB(hue, 0.7F, 1F);
    }

    @Override
    public Set<Property<?>> relevantProperties() {
        return this.original.relevantProperties();
    }

    private static float dist(int x, int y, int z) {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
}

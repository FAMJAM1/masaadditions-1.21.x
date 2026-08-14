package com.red.masaadditions.tweakeroo_additions.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;

public class BlockDustParticleExt extends TerrainParticle {
    // From 1.12 Tweakeroo by Masa
    public BlockDustParticleExt(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, BlockState state, BlockPos pos) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state, pos);
    }
}
package com.red.masaadditions.tweakeroo_additions.util;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Options;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;

public class MiscUtils {
    public static final ArrayList<KeyMapping> MOVEMENT_HOLD_KEYS = new ArrayList<>();

    public static void setMovementHoldKeys(boolean enabled) {
        if (!enabled) {
            KeyMapping.setAll();
            return;
        }

        MOVEMENT_HOLD_KEYS.clear();
        Options options = Minecraft.getInstance().options;
        KeyMapping[] movementKeys = { options.keyJump, options.keyLeft, options.keyRight, options.keyDown, options.keyUp };
        for (KeyMapping movementKey : movementKeys) {
            if (movementKey.isDown()) {
                MOVEMENT_HOLD_KEYS.add(movementKey);
            }
        }
    }

    // From 1.12 Tweakeroo by Masa
    public static void addCustomBlockBreakingParticles(ParticleEngine manager, ClientLevel world, RandomSource rand, BlockPos pos, BlockState state) {
        if (!state.isAir()) {
            int limit = ConfigsExtended.Generic.BLOCK_BREAKING_PARTICLE_LIMIT.getIntegerValue();

            for (int i = 0; i < limit; ++i) {
                double x = ((double) pos.getX() + rand.nextDouble());
                double y = ((double) pos.getY() + rand.nextDouble());
                double z = ((double) pos.getZ() + rand.nextDouble());
                double speedX = (0.5 - rand.nextDouble());
                double speedY = (0.5 - rand.nextDouble());
                double speedZ = (0.5 - rand.nextDouble());

                manager.add((new BlockDustParticleExt(world, x, y, z, speedX, speedY, speedZ, state, pos))
                        .setPower((float) ConfigsExtended.Generic.BLOCK_BREAKING_PARTICLE_SPEED.getDoubleValue())
                        .scale((float) ConfigsExtended.Generic.BLOCK_BREAKING_PARTICLE_SCALE.getDoubleValue()));
            }
        }
    }

    public static boolean handleUseSnowLayer(Block block, LocalPlayer player) {
        return ConfigsExtended.Disable.DISABLE_SNOW_LAYER_STACKING.getBooleanValue() && block instanceof SnowLayerBlock;
    }

    public static boolean handleUseDragonEgg(Block block, LocalPlayer player) {
        return ConfigsExtended.Disable.DISABLE_DRAGON_EGG_TELEPORTING.getBooleanValue() && block instanceof DragonEggBlock && !player.isShiftKeyDown();
    }

    // Whether a bed blows up is a property of the dimension as a whole here
    public static boolean handleUseBed(Block block, ClientLevel world) {
        return ConfigsExtended.Disable.DISABLE_BED_EXPLOSIONS.getBooleanValue() && block instanceof BedBlock && !world.dimensionType().bedWorks();
    }

    public static boolean handleUseTools(Block block, Item heldItem) {
        return (ConfigsExtended.Disable.DISABLE_FARMLAND_MAKING.getBooleanValue() && heldItem instanceof HoeItem && MiscUtils.isTillableBlock(block) || (ConfigsExtended.Disable.DISABLE_PATH_MAKING.getBooleanValue() && heldItem instanceof ShovelItem && block instanceof GrassBlock));
    }

    public static boolean isTillableBlock(Block block) {
        return block instanceof GrassBlock || block instanceof DirtPathBlock || block == Blocks.DIRT;
    }

    public static int getSlotNumberForEquipmentSlot(EquipmentSlot type) {
        return switch (type) {
            case HEAD -> 5;
            case CHEST -> 6;
            case LEGS -> 7;
            case FEET -> 8;
            default -> -1;
        };
    }
}

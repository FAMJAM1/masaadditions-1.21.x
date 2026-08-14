package com.red.masaadditions.tweakeroo_additions.tweaks;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import com.red.masaadditions.tweakeroo_additions.config.FeatureToggleExtended;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DragonEggBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlacementTweaks {
    @Nullable
    public static ItemStack replacementModeUseStack = null;

    public static final ArrayList<Block> PERIMETER_OUTLINE_BLOCKS = new ArrayList<>();

    public static boolean onProcessLeftClickBlock(BlockPos pos) {
        Player player = Minecraft.getInstance().player;
        return ConfigsExtended.Disable.DISABLE_DRAGON_EGG_TELEPORTING.getBooleanValue() && player != null && !player.isCreative() && player.level().getBlockState(pos).getBlock() instanceof DragonEggBlock;
    }

    public static boolean isPositionDisallowedByPerimeterOutlineList(BlockPos pos) {
        boolean restrictionEnabled = FeatureToggleExtended.TWEAK_PERIMETER_WALL_DIG_HELPER.getBooleanValue();

        if (!restrictionEnabled)
            return false;

        ClientLevel level = Minecraft.getInstance().level;
        return level != null && PERIMETER_OUTLINE_BLOCKS.contains(level.getBlockState(level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, pos).below()).getBlock());
    }

    public static void setPerimeterOutlineBlocks(List<String> blocks) {
        PERIMETER_OUTLINE_BLOCKS.clear();

        for (String str : blocks) {
            Block block = getBlockFromName(str);

            if (block != null)
                PERIMETER_OUTLINE_BLOCKS.add(block);
        }
    }

    @Nullable
    private static Block getBlockFromName(String name) {
        try {
            Identifier identifier = Identifier.parse(name);
            return BuiltInRegistries.BLOCK.getOptional(identifier).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}

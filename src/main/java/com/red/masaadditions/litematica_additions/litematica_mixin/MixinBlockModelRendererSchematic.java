package com.red.masaadditions.litematica_additions.litematica_mixin;

import com.red.masaadditions.litematica_additions.config.ConfigsExtended;
import com.red.masaadditions.litematica_additions.util.MiscUtils;
import fi.dy.masa.litematica.render.schematic.BlockModelRendererSchematic;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockModelRendererSchematic.class, remap = false)
public class MixinBlockModelRendererSchematic {
    @Inject(method = "shouldRenderModelSide", at = @At("RETURN"), cancellable = true)
    private static void shouldRenderModelSide(BlockAndTintGetter worldIn, BlockState stateIn, BlockPos posIn, Direction side, boolean translucent, BlockPos mutablePos, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack item = player != null ? player.getMainHandItem() : ItemStack.EMPTY;
        BlockState neighborBlockState = worldIn.getBlockState(mutablePos);
        cir.setReturnValue(ConfigsExtended.Generic.RENDER_HELD_ITEM_ONLY.getBooleanValue() && MiscUtils.checkHeldItem(item, neighborBlockState));
    }
}

package com.red.masaadditions.litematica_additions.litematica_mixin;

import com.red.masaadditions.litematica_additions.config.ConfigsExtended;
import com.red.masaadditions.litematica_additions.util.MiscUtils;
import fi.dy.masa.litematica.render.schematic.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(value = ChunkRendererSchematicVbo.class, remap = false)
public class MixinChunkRendererSchematicVbo {
    @Shadow
    protected ChunkCacheSchematic schematicWorldView;

    @Inject(method = "renderBlocksAndOverlay", at = @At("HEAD"), cancellable = true)
    private void renderBlocksAndOverlay(BlockPos pos, ChunkRenderDataSchematic data,
                                        BufferAllocatorCache buffers, Set<ChunkSectionLayer> layers,
                                        Set<RenderType> types, PoseStack poseStack, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack item = player != null ? player.getMainHandItem() : ItemStack.EMPTY;
        BlockState stateSchematic = this.schematicWorldView.getBlockState(pos);
        if (ConfigsExtended.Generic.RENDER_HELD_ITEM_ONLY.getBooleanValue() && MiscUtils.checkHeldItem(item, stateSchematic)) {
            ci.cancel();
        }
    }
}

package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemFrameRenderer.class)
public class MixinItemFrameEntityRenderer {
    private ItemFrame itemFrameEntity;

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void disableItemFrameFrameRendering(ItemFrame itemFrameEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
        this.itemFrameEntity = itemFrameEntity;
    }

    @ModifyVariable(method = "render", at = @At("STORE"))
    private boolean disableItemFrameFrameRendering(boolean bl) {
        return ConfigsExtended.Disable.DISABLE_ITEM_FRAME_FRAME_RENDERING.getBooleanValue() && !itemFrameEntity.getItem().isEmpty() || itemFrameEntity.isInvisible();
    }
}

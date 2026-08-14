package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class MixinAbstractBlockState {
    @Shadow public abstract Block getBlock();

    @Inject(method = "getModelOffset", at = @At("HEAD"), cancellable = true)
    private void getModelOffset(CallbackInfoReturnable<Vec3> cir) {
        Block block = getBlock();
        boolean isPlant = block instanceof DoublePlantBlock || block instanceof FlowerBlock;
        if (isPlant && ConfigsExtended.Disable.DISABLE_PLANT_BLOCK_MODEL_OFFSET.getBooleanValue()) {
            cir.setReturnValue(Vec3.ZERO);
        }
    }
}

package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeBlock.class)
public class MixinSlimeBlock extends HalfTransparentBlock {
    protected MixinSlimeBlock(Settings settings) {
        super(settings);
    }

    @Inject(method = "onEntityLand", at = @At("HEAD"), cancellable = true)
    private void onEntityLand(BlockGetter world, Entity entity, CallbackInfo ci) {
        if (ConfigsExtended.Disable.DISABLE_SLIME_BLOCK_BOUNCING.getBooleanValue() && entity instanceof Player) {
            super.onEntityLand(world, entity);
            ci.cancel();
        }
    }

    @Inject(method = "onLandedUpon", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;handleFallDamage(FFLnet/minecraft/entity/damage/DamageSource;)Z"), cancellable = true)
    private void handleFallDamage(Level world, BlockState state, BlockPos pos, Entity entity, float distance, CallbackInfo ci) {
        if (ConfigsExtended.Disable.DISABLE_SLIME_BLOCK_BOUNCING.getBooleanValue() && entity instanceof Player) {
            super.onLandedUpon(world, state, pos, entity, distance);
            ci.cancel();
        }
    }
}

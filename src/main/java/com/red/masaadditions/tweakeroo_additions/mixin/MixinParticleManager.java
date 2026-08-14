package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import com.red.masaadditions.tweakeroo_additions.config.FeatureToggleExtended;
import com.red.masaadditions.tweakeroo_additions.util.MiscUtils;
import fi.dy.masa.tweakeroo.config.Configs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ParticleEngine.class)
public class MixinParticleManager {
    @Shadow
    protected ClientLevel world;
    @Shadow
    @Final
    private RandomSource random;

    // From 1.12 Tweakeroo by Masa
    @Inject(method = "addBlockBreakParticles", at = @At("HEAD"), cancellable = true)
    private void onAddBlockDestroyEffects1(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!Configs.Disable.DISABLE_BLOCK_BREAK_PARTICLES.getBooleanValue() && FeatureToggleExtended.TWEAK_BLOCK_BREAKING_PARTICLES.getBooleanValue()) {
            MiscUtils.addCustomBlockBreakingParticles((net.minecraft.client.particle.ParticleEngine) (Object) this, this.world, this.random, pos, state);
            ci.cancel();
        }
    }

    @Inject(method = "addBlockBreakingParticles", at = @At("HEAD"), cancellable = true)
    private void onAddBlockDestroyEffects2(BlockPos pos, Direction direction, CallbackInfo ci) {
        if (ConfigsExtended.Disable.DISABLE_BLOCK_ATTACKED_PARTICLES.getBooleanValue()) {
            ci.cancel();
        }
    }
}

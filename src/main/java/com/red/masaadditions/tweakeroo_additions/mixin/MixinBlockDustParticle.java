package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.FeatureToggleExtended;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TerrainParticle.class)
public abstract class MixinBlockDustParticle extends SingleQuadParticle {
    protected MixinBlockDustParticle(ClientLevel worldIn, double posXIn, double posYIn, double posZIn, TextureAtlasSprite sprite) {
        super(worldIn, posXIn, posYIn, posZIn, sprite);
    }

    // From UsefulMod by nessie
    @Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At("RETURN"))
    private void removeRandomParticleMotion(CallbackInfo ci) {
        if (FeatureToggleExtended.TWEAK_INSANE_BLOCK_BREAKING_PARTICLES.getBooleanValue()) {
            final double multiplier = this.random.nextFloat() * 5;
            this.xd *= multiplier;
            this.yd *= multiplier;
            this.zd *= multiplier;
            this.lifetime *= multiplier;
            this.gravity = 0F;
        }
    }
}

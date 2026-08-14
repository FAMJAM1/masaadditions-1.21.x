package com.red.masaadditions.litematica_additions.mixin;
import com.red.masaadditions.litematica_additions.config.ConfigsExtended;
import fi.dy.masa.litematica.util.SchematicWorldRefresher;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(value = Player.class)
public class MixinPlayerEntity {
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"))
    private void renderBlocksAndOverlay(CallbackInfo ci) {
        if (ConfigsExtended.Generic.RENDER_HELD_ITEM_ONLY.getBooleanValue()) {
            SchematicWorldRefresher.INSTANCE.updateAll();
        }
    }
}

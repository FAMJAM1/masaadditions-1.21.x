package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.inventory.ContainerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookComponent.class)
public abstract class MixinRecipeBookWidget {
    @Shadow
    protected Minecraft client;

    // From UsefulMod by nessie
    @Inject(method = "refreshInputs", at = @At("RETURN"))
    private void refreshInputs(CallbackInfo ci) {
        if (ConfigsExtended.Generic.CLICK_RECIPE_CRAFT.getBooleanValue() && Screen.hasControlDown() && Screen.hasShiftDown()) {
            client.gameMode.clickSlot(client.player.containerMenu.containerId, 0, 1, Screen.hasAltDown() ? ContainerInput.THROW : ContainerInput.QUICK_MOVE, client.player);
        }
    }
}

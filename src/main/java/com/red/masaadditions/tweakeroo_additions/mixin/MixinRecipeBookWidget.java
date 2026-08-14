package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import fi.dy.masa.malilib.gui.GuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.inventory.ContainerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookComponent.class)
public abstract class MixinRecipeBookWidget {
    // From UsefulMod by nessie
    @Inject(method = "updateStackedContents", at = @At("RETURN"))
    private void refreshInputs(CallbackInfo ci) {
        if (!ConfigsExtended.Generic.CLICK_RECIPE_CRAFT.getBooleanValue() || !GuiBase.isCtrlDown() || !GuiBase.isShiftDown()) {
            return;
        }

        // The widget no longer holds a client reference of its own
        Minecraft client = Minecraft.getInstance();

        if (client.gameMode != null && client.player != null) {
            client.gameMode.handleContainerInput(client.player.containerMenu.containerId, 0, 1,
                    GuiBase.isAltDown() ? ContainerInput.THROW : ContainerInput.QUICK_MOVE, client.player);
        }
    }
}

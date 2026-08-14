package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.FeatureToggleExtended;
import com.red.masaadditions.tweakeroo_additions.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = Equipment.class)
public interface MixinEquipment {
    @Inject(method = "equipAndSwap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/InteractionResult;fail(Ljava/lang/Object;)Lnet/minecraft/world/InteractionResult;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void use(Item item, Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult<ItemStack>> cir, ItemStack itemStack) {
        Minecraft mc = Minecraft.getInstance();

        if (!FeatureToggleExtended.TWEAK_FORCE_SWAP_GEAR.getBooleanValue() || !user.isSneaking() || hand != InteractionHand.MAIN_HAND || mc.gameMode == null || user.containerMenu != user.inventoryMenu) {
            return;
        }

        mc.gameMode.clickSlot(user.inventoryMenu.containerId, MiscUtils.getSlotNumberForEquipmentSlot(user.getEquipmentSlotForItem(itemStack)), user.getInventory().selected, ContainerInput.SWAP, user);
        cir.setReturnValue(InteractionResult.sidedSuccess(itemStack, world.isClientSide()));
    }
}

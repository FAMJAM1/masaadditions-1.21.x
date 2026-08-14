package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import com.red.masaadditions.tweakeroo_additions.config.FeatureToggleExtended;
import com.red.masaadditions.tweakeroo_additions.tweaks.PlacementTweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.StreamSupport;

@Mixin(MultiPlayerGameMode.class)
public abstract class MixinClientPlayerInteractionManager {
    @Shadow
    @Final
    private ClientPacketListener networkHandler;

    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    private void handleBreakingRestriction1(BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (PlacementTweaks.onProcessLeftClickBlock(pos) || PlacementTweaks.isPositionDisallowedByPerimeterOutlineList(pos)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "updateBlockBreakingProgress", at = @At("HEAD"), cancellable = true)
    private void handleBreakingRestriction2(BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (PlacementTweaks.onProcessLeftClickBlock(pos) || PlacementTweaks.isPositionDisallowedByPerimeterOutlineList(pos)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "attackEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;attack(Lnet/minecraft/world/entity/Entity;)V"))
    private void onAttackEntity1(Player player, Entity target, CallbackInfo ci) {
        if (FeatureToggleExtended.TWEAK_ONE_HIT_KILL.getBooleanValue() && player.isCreative() && target instanceof LivingEntity && ((LivingEntity) target).getHealth() > 0f) {
            ((LocalPlayer) player).networkHandler.sendCommand(String.format("kill %s", target.getUuidAsString()));
        }
    }

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    private void onAttackEntity2(Player player, Entity target, CallbackInfo ci) {
        if (FeatureToggleExtended.TWEAK_PREVENT_ATTACK_ENTITIES.getBooleanValue() && ConfigsExtended.Lists.PREVENT_ATTACK_ENTITIES_LIST.getStrings().contains(EntityType.getId(target.getType()).toString())) {
            ci.cancel();
        }
    }

    @Inject(method = "interactEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;syncSelectedSlot()V", shift = At.Shift.AFTER), cancellable = true)
    private void onInteractEntity(Player player, Entity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (FeatureToggleExtended.TWEAK_NAME_TAG_PIGLINS.getBooleanValue() && player.getStackInHand(hand).getItem() instanceof NameTagItem) {
            if (!(entity instanceof Piglin piglinEntity)) {
                cir.setReturnValue(InteractionResult.PASS);
                return;
            }

            if (piglinEntity.isBaby() || piglinEntity.getCustomName() != null || StreamSupport.stream(piglinEntity.getHandItems().spliterator(), false).noneMatch(itemStack -> itemStack.getItem() instanceof SwordItem)) {
                cir.setReturnValue(InteractionResult.PASS);
            }
        }
    }

    @Inject(method = "method_41933", at = @At("HEAD"))
    private void resetReplacementModeFlag(CallbackInfoReturnable<Packet<?>> cir) {
        PlacementTweaks.replacementModeUseStack = null;
    }

    @Inject(method = "method_41933", at = @At("RETURN"), cancellable = true)
    private void modifyPlacementPacket(MutableObject<InteractionResult> result, LocalPlayer player, InteractionHand hand, BlockHitResult blockHitResult, int sequence, CallbackInfoReturnable<Packet<?>> cir) {
        if (PlacementTweaks.replacementModeUseStack != null) {
            if (!Minecraft.getInstance().isInSingleplayer()) {
                this.networkHandler.send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, blockHitResult.getBlockPos(), blockHitResult.getSide()));
                cir.setReturnValue(new ServerboundUseItemOnPacket(hand, blockHitResult, sequence));
            }
            PlacementTweaks.replacementModeUseStack = null;
        }
    }
}

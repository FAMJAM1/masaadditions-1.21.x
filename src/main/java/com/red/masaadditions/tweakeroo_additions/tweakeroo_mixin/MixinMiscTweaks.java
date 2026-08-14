package com.red.masaadditions.tweakeroo_additions.tweakeroo_mixin;

import fi.dy.masa.tweakeroo.tweaks.MiscTweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Pseudo
@Mixin(value = MiscTweaks.class, remap = false)
public class MixinMiscTweaks {
    @ModifyVariable(method = "doPotionWarnings", at = @At(value = "STORE", target = "Lfi/dy/masa/tweakeroo/tweaks/MiscTweaks;doPotionWarnings(Lnet/minecraft/world/entity/player/Player;)V"))
    private static Collection<MobEffectInstance> doPotionWarnings(Collection<MobEffectInstance> effects) {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && player.getEquippedStack(EquipmentSlot.HEAD).getItem() != Items.TURTLE_HELMET ? effects : effects.stream().filter(e -> e.getEffectType() != MobEffects.WATER_BREATHING).collect(Collectors.toCollection(TreeSet::new));
    }
}

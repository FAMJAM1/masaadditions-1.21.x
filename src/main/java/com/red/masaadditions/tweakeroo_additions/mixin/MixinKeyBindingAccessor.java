package com.red.masaadditions.tweakeroo_additions.mixin;

import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyMapping.class)
public interface MixinKeyBindingAccessor {
    @Accessor("KEY_TO_BINDINGS")
    static Map<InputConstants.Key, KeyMapping> getKeyToBindings() {
        throw new AssertionError();
    }
}
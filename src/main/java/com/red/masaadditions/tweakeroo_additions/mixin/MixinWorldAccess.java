package com.red.masaadditions.tweakeroo_additions.mixin;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import com.red.masaadditions.tweakeroo_additions.config.FeatureToggleExtended;
import net.minecraft.world.LunarWorldView;
import net.minecraft.world.level.CommonLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = LevelAccessor.class, priority = 1001)
public interface MixinWorldAccess extends CommonLevelAccessor, LunarWorldView {
    @Shadow
    LevelData getLevelProperties();

    /**
     * @author Red.#9015
     * @reason Isn't possible to inject into interfaces. Overwrite shouldn't affect most other mods though.
     */
    @Overwrite()
    default long getLunarTime() {
        if (FeatureToggleExtended.TWEAK_OVERRIDE_SKY_TIME.getBooleanValue())
            return ConfigsExtended.Generic.SKY_TIME_OVERRIDE.getIntegerValue();
        return this.getLevelProperties().getTimeOfDay();
    }
}

package com.red.masaadditions.tweakeroo_additions.tweakeroo_mixin;

import com.red.masaadditions.MasaAdditions;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    private static boolean hasTweakeroo() {
        return FabricLoader.getInstance().isModLoaded("tweakeroo");
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return hasTweakeroo();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        if (hasTweakeroo()) MasaAdditions.logger.info("TweakerooAdditions Loaded.");
    }

    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}

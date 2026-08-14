package com.red.masaadditions.litematica_additions.litematica_mixin;

import com.red.masaadditions.litematica_additions.util.FileImportUtil;
import fi.dy.masa.litematica.gui.GuiMainMenu;
import fi.dy.masa.litematica.gui.GuiSchematicLoad;
import fi.dy.masa.litematica.gui.GuiSchematicManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

import java.nio.file.Path;
import java.util.List;

import static fi.dy.masa.litematica.schematic.LitematicaSchematic.FILE_EXTENSION;

@Mixin(value = {GuiMainMenu.class, GuiSchematicManager.class, GuiSchematicLoad.class}, remap = false)
public class MixinLitematicaGui extends Screen {
    protected MixinLitematicaGui(Component title) {
        super(title);
    }

    @Override
    public void onFilesDrop(List<Path> paths) {
        paths.stream().filter(_path -> _path.toString().endsWith(FILE_EXTENSION)).findFirst().ifPresent(FileImportUtil::saveDraggedLitematic);
    }
}

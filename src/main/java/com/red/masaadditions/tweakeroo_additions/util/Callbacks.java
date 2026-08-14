package com.red.masaadditions.tweakeroo_additions.util;

import com.red.masaadditions.tweakeroo_additions.config.ConfigsExtended;
import com.red.masaadditions.tweakeroo_additions.config.HotkeysExtended;
import com.red.masaadditions.tweakeroo_additions.mixin.MixinAbstractBlockAccessor;
import fi.dy.masa.malilib.config.IConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeyCallbackToggleBooleanConfigWithMessage;
import fi.dy.masa.malilib.interfaces.IValueChangeCallback;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.tweakeroo.util.RayTraceUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class Callbacks {
    public static class KeyCallbackHotkeysGeneric implements IHotkeyCallback {
        private final Minecraft mc = Minecraft.getInstance();

        // From 1.12 Tweakeroo by Masa
        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key) {
            if (key == HotkeysExtended.BLINK_DRIVE.getKeybind()) {
                this.blinkDriveTeleport(false);
                return true;
            } else if (key == HotkeysExtended.BLINK_DRIVE_Y_LEVEL.getKeybind()) {
                this.blinkDriveTeleport(true);
                return true;
            }

            return false;
        }

        private void blinkDriveTeleport(boolean maintainY) {
            if (this.mc.player.isCreative()) {
                Entity entity = fi.dy.masa.malilib.util.EntityUtils.getCameraEntity();
                HitResult trace = RayTraceUtils.getRayTraceFromEntity(this.mc.level, entity, true, this.mc.options.getEffectiveRenderDistance() * 16 + 200);

                if (trace.getType() != HitResult.Type.MISS) {
                    Vec3 pos = trace.getLocation();
                    if (trace.getType() == HitResult.Type.BLOCK) {
                        pos = adjustPositionToSideOfEntity(pos, this.mc.player, ((BlockHitResult) trace).getDirection());
                    }

                    this.mc.player.connection.sendCommand(String.format("tp @p %.6f %.6f %.6f", pos.x, maintainY ? this.mc.player.getY() : pos.y, pos.z));
                }
            }
        }

        public static Vec3 adjustPositionToSideOfEntity(Vec3 pos, Entity entity, Direction side) {
            double x = pos.x;
            double y = pos.y;
            double z = pos.z;

            if (side == Direction.DOWN) {
                y -= entity.getBbHeight();
            } else if (side.getAxis().isHorizontal()) {
                x += side.getStepX() * (entity.getBbWidth() / 2 + 1.0E-4D);
                z += side.getStepZ() * (entity.getBbWidth() / 2 + 1.0E-4D);
            }

            return new Vec3(x, y, z);
        }
    }

    public static class KeyCallbackToggleFastRightClick extends KeyCallbackToggleBooleanConfigWithMessage {
        public KeyCallbackToggleFastRightClick(IConfigBoolean config) {
            super(config);
        }

        public boolean onKeyAction(KeyAction action, IKeybind key) {
            if (ConfigsExtended.Disable.DISABLE_FARMLAND_MAKING.getBooleanValue()) {
                this.config.setBooleanValue(false);
                String message = StringUtils.translate("masaadditions.message.fast_right_click_disabled");
                InfoUtils.printActionbarMessage(message);
            } else {
                super.onKeyAction(action, key);
            }
            return true;
        }
    }

    public static class FeatureCallbackHoney implements IValueChangeCallback<ConfigBoolean> {
        public FeatureCallbackHoney(ConfigBoolean feature) {
            if (feature.equals(ConfigsExtended.Disable.DISABLE_HONEY_BLOCK_SLOWDOWN)) {
                ConfigsExtended.Internal.HONEY_BLOCK_VELOCITY_MULTIPLIER_ORIGINAL.setDoubleValue(Blocks.HONEY_BLOCK.getSpeedFactor());

                if (feature.getBooleanValue()) {
                    ((MixinAbstractBlockAccessor) Blocks.HONEY_BLOCK).setVelocityMultiplier(Blocks.STONE.getSpeedFactor());
                }
            } else {
                ConfigsExtended.Internal.HONEY_BLOCK_JUMP_VELOCITY_MULTIPLIER_ORIGINAL.setDoubleValue(Blocks.HONEY_BLOCK.getJumpFactor());

                if (!feature.getBooleanValue()) {
                    ((MixinAbstractBlockAccessor) Blocks.HONEY_BLOCK).setJumpVelocityMultiplier(Blocks.STONE.getJumpFactor());
                }
            }
        }

        @Override
        public void onValueChanged(ConfigBoolean config) {
            if (config.equals(ConfigsExtended.Disable.DISABLE_HONEY_BLOCK_SLOWDOWN)) {
                if (config.getBooleanValue()) {
                    ((MixinAbstractBlockAccessor) Blocks.HONEY_BLOCK).setVelocityMultiplier(Blocks.STONE.getSpeedFactor());
                } else {
                    ((MixinAbstractBlockAccessor) Blocks.HONEY_BLOCK).setVelocityMultiplier((float) ConfigsExtended.Internal.HONEY_BLOCK_VELOCITY_MULTIPLIER_ORIGINAL.getDoubleValue());
                }
            } else {
                if (config.getBooleanValue()) {
                    ((MixinAbstractBlockAccessor) Blocks.HONEY_BLOCK).setJumpVelocityMultiplier((float) ConfigsExtended.Internal.HONEY_BLOCK_JUMP_VELOCITY_MULTIPLIER_ORIGINAL.getDoubleValue());
                } else {
                    ((MixinAbstractBlockAccessor) Blocks.HONEY_BLOCK).setJumpVelocityMultiplier(Blocks.STONE.getJumpFactor());
                }
            }
        }
    }
}

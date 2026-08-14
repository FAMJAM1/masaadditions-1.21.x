package com.red.masaadditions.tweakeroo_additions.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.LastSeenMessagesTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPacketListener.class)
public interface ClientPlayNetworkHandlerAccessor {
    @Accessor
    LastSeenMessagesTracker getLastSeenMessagesCollector();
}

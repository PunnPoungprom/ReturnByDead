package net.nopunintendeds.rbd;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.nopunintendeds.rbd.client.packet.Server2ClientPacket;
import net.nopunintendeds.rbd.client.screen.BlackScreen;

public class RBD implements ModInitializer {

    public static boolean isReturning = true;
    public static boolean exitCheck = false;

    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            isReturning = true;
            exitCheck = false;
        });

        Sound.registerSound();
        Server2ClientPacket.registerS2CPackets();
    }
}

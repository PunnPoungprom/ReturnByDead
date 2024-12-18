package net.nopunintendeds.rbd;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.nopunintendeds.rbd.client.packet.Server2ClientPacket;

import java.io.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RBDClient implements ClientModInitializer {

    private static ScheduledExecutorService schedule = new ScheduledThreadPoolExecutor(1);


    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.player == MinecraftClient.getInstance().player) {
                File gameDirectory = MinecraftClient.getInstance().runDirectory;
                File file = new File(gameDirectory, "rbd.txt");

                if (!file.exists()) {
                    return;
                }

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line = reader.readLine();
                    if (Boolean.parseBoolean(line)) {
                        returnByDeath(client);
                        try (FileWriter writer = new FileWriter(file)) {
                            writer.write(Boolean.toString(false));
                            writer.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        client.player.playSound(Sound.AHHYOOAAAWHOAAA, SoundCategory.MASTER, 1.0F, 1.0F);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void returnByDeath(MinecraftClient client) {

        if (client.currentScreen == null) {
            schedule.schedule(() -> {
                client.player.playSound(Sound.ReturnByDeath, SoundCategory.MASTER, 1.0F, 1.0F);
                ClientPlayNetworking.send(Server2ClientPacket.DISCONNECT_PACKET_ID, PacketByteBufs.create());
            }, 20 , TimeUnit.MILLISECONDS);
            return;
        }

        schedule.schedule(() -> {
            returnByDeath(client);
        }, 20 , TimeUnit.MILLISECONDS);
    }
}

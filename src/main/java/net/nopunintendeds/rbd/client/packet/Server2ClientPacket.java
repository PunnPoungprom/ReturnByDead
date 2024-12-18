package net.nopunintendeds.rbd.client.packet;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nopunintendeds.rbd.RBDClient;
import net.nopunintendeds.rbd.client.screen.BlackScreen;
import net.nopunintendeds.rbd.mixin.MinecraftServerAccessor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server2ClientPacket {
    public static final Identifier DISCONNECT_PACKET_ID = new Identifier("rbd", "disconnect_packet");

    private static ScheduledExecutorService schedule = new ScheduledThreadPoolExecutor(1);

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(DISCONNECT_PACKET_ID, (client, handler, buf, responseSender) -> {
            File gameDirectory = client.runDirectory;
            File file = new File(gameDirectory, "rbd.txt");

            String level = buf.readString();
            //System.out.println(level);

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(Boolean.toString(true));
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            client.execute(() -> {
                client.execute(client::disconnect);
                returnByDeath(client, level);
                client.setScreen(new BlackScreen());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(DISCONNECT_PACKET_ID, ((server, player, handler, buf, responseSender) -> {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 255, true, false, false));
        }));
    }

    public static void sendDisconnectPacket(ServerPlayerEntity player, String message) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(message);
        ServerPlayNetworking.send(player, DISCONNECT_PACKET_ID, buf);
    }

    private static void returnByDeath(MinecraftClient client, String levelName) {

        if (client.world == null && client.player == null) {
            client.execute(() -> {
                client.createIntegratedServerLoader().start(new BlackScreen(), levelName);
            });
            return;
        }

        schedule.schedule(() -> {
            returnByDeath(client, levelName);
        }, 20 , TimeUnit.MILLISECONDS);
    }
}

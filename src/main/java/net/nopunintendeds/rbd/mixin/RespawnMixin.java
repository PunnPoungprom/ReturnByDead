package net.nopunintendeds.rbd.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.DatapackFailureScreen;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.NoticeScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.nopunintendeds.rbd.RBD;
import net.nopunintendeds.rbd.Sound;
import net.nopunintendeds.rbd.client.packet.Server2ClientPacket;
import net.nopunintendeds.rbd.client.screen.BlackScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Mixin(PlayerEntity.class)
public class RespawnMixin {


	@Inject(at = @At(value = "TAIL"),
			method = "applyDamage",
			cancellable = true)
	private void requestRespawn(DamageSource source, float amount, CallbackInfo ci) {
		PlayerEntity player = (PlayerEntity)(Object)this;

		if (player.getHealth() <= 0) {
			// Play effects
			//player.playSound(Sound.ReturnByDeath, SoundCategory.MASTER, 1.0F, 1.0F);

			RBD.isReturning = true;
			RBD.exitCheck = true;

			player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20, 255, true, false, false));



			String levelName = ((MinecraftServerAccessor) player.getWorld().getServer()).getSession().getDirectoryName();

			player.getWorld().getServer().getPlayerManager().getPlayerList().forEach(player1 -> {
				Server2ClientPacket.sendDisconnectPacket(player1, levelName);
			});

			player.getWorld().getServer().execute(() -> player.getWorld().getServer().stop(false));
		}
	}


}
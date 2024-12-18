package net.nopunintendeds.rbd.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.world.level.storage.LevelStorage;
import net.nopunintendeds.rbd.RBD;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

import static com.mojang.text2speech.Narrator.LOGGER;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Shadow @Final protected LevelStorage.Session session;

    @Shadow private PlayerManager playerManager;

    @Inject(method = "save", at = @At("HEAD"), cancellable = true)
    private void onSave(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (RBD.isReturning) {
            System.out.println("Disable save Server");
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "saveAll", at = @At("HEAD"), cancellable = true)
    private void onSaveAll(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (RBD.isReturning) {
            System.out.println("Disable saveAll Server");
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "shutdown", at = @At("HEAD"), cancellable = true)
    private void onShutdown(CallbackInfo ci) {
        if (!RBD.exitCheck) {
            RBD.isReturning = false;
        }
        if (RBD.isReturning) {
            try {
                this.session.close();
            } catch (IOException var4) {
                LOGGER.error("Failed to unlock level {}", this.session.getDirectoryName(), var4);
            }
            ci.cancel();
        }
    }
}

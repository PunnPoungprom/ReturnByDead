package net.nopunintendeds.rbd.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.nopunintendeds.rbd.RBD;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(method = "saveLevel", at = @At("HEAD"), cancellable = true)
    private void onSaveAll(CallbackInfo ci) {
        if (RBD.isReturning) {
            System.out.println("Disable saveLevel Server");
            ci.cancel();
        }
    }

    @Inject(method = "save", at = @At("HEAD"), cancellable = true)
    private void disableAutoSaveTick(CallbackInfo ci) {
        if (RBD.isReturning) {
            System.out.println("Disable ServerWorld save");
            ci.cancel();
        }
    }
}

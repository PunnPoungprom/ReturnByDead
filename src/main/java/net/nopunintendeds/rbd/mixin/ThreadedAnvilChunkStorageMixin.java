package net.nopunintendeds.rbd.mixin;

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.Chunk;
import net.nopunintendeds.rbd.RBD;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThreadedAnvilChunkStorage.class)
public class ThreadedAnvilChunkStorageMixin {

    @Inject(method = "save(Z)V", at = @At("HEAD"), cancellable = true)
    private void onSave(boolean flush, CallbackInfo ci) {
        if (RBD.isReturning) {
            System.out.println("Disable Saving flush");
            ci.cancel();
        }

    }

    @Inject(method = "save(Lnet/minecraft/world/chunk/Chunk;)Z", at = @At("HEAD"), cancellable = true)
    private void onSave2(Chunk chunk, CallbackInfoReturnable<Boolean> cir) {
        //System.out.println("Disable Saving Chunk");
        if (RBD.isReturning) {
            cir.setReturnValue(false);
        }
    }

//    @Inject(method = "save(Lnet/minecraft/server/world/ChunkHolder;)Z", at = @At("HEAD"), cancellable = true)
//    private void onSave3(ChunkHolder chunkHolder, CallbackInfoReturnable<Boolean> cir) {
//        System.out.println("Disable Saving chunkHolder");
//        cir.setReturnValue(false);
//    }
}
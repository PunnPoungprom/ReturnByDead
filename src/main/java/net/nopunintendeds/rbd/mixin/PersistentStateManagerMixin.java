package net.nopunintendeds.rbd.mixin;

import net.minecraft.world.PersistentStateManager;
import net.nopunintendeds.rbd.RBD;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentStateManager.class)
public class PersistentStateManagerMixin {

    @Inject(method = "save", at = @At("HEAD"), cancellable = true)
    private void save(CallbackInfo ci) {
        if (RBD.isReturning) {
            System.out.println("Disable save PersistentStateManager");
            ci.cancel();
        }
    }
}

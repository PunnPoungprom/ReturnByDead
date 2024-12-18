package net.nopunintendeds.rbd.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.text.Text;
import net.nopunintendeds.rbd.Sound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Mixin(ProgressScreen.class)
public class ProgressScreenMixin extends Screen {

    @Shadow private @Nullable Text title;
    @Unique
    private boolean dead = false;

    protected ProgressScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "<init>",at= @At("TAIL"))
    public void init(boolean closeAfterFinished, CallbackInfo ci) {
        File gameDirectory = MinecraftClient.getInstance().runDirectory;
        File file = new File(gameDirectory, "rbd.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (Boolean.parseBoolean(line)) {
                dead = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "render",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"), cancellable = true)
    public void init(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (dead) {
            ci.cancel();
            context.fill(0, 0, width, height, 0xFF000000);
        }
    }
}

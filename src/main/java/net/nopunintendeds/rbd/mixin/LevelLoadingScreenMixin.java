package net.nopunintendeds.rbd.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.text.Text;
import net.nopunintendeds.rbd.Sound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin extends Screen {

    @Unique
    private boolean dead = false;

    protected LevelLoadingScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "<init>",at= @At("TAIL"))
    public void init(WorldGenerationProgressTracker progressProvider, CallbackInfo ci) {
        File gameDirectory = MinecraftClient.getInstance().runDirectory;
        File file = new File(gameDirectory, "rbd.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (Boolean.parseBoolean(line)) {
                dead = true;
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(Sound.AMDEAD, 1,20));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "render",at= @At("HEAD"), cancellable = true)
    public void init(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (dead) {
            ci.cancel();
            context.fill(0, 0, width, height, 0xFF000000);
        }
    }
}

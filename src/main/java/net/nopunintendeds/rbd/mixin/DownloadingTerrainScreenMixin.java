package net.nopunintendeds.rbd.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.text.Text;
import net.nopunintendeds.rbd.Sound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Mixin(DownloadingTerrainScreen.class)
public class DownloadingTerrainScreenMixin extends Screen {

    @Unique
    private boolean dead = false;

    protected DownloadingTerrainScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "<init>",at= @At("TAIL"))
    public void init(CallbackInfo ci) {
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

    @Inject(method = "render",at= @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (dead) {
            ci.cancel();
            context.fill(0, 0, width, height, 0xFF000000);
        }
    }
}

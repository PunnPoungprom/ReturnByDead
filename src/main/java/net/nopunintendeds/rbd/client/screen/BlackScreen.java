package net.nopunintendeds.rbd.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.nopunintendeds.rbd.Sound;

public class BlackScreen extends Screen {

    public BlackScreen() {
        super(Text.of("Black Screen")); // You can change the title text if needed
    }

    @Override
    protected void init() {
        // Initialize any buttons or elements here if needed
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0xFF000000);
        super.render(context, mouseX, mouseY, delta);
    }
}
package com.yericosan.fullbright;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class FullbrightConfigScreen extends Screen {
    private final Screen parent;

    public FullbrightConfigScreen(Screen parent) {
        super(Component.literal("Configuración - fullbright yerico_san"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addRenderableWidget(Button.builder(getToggleText(), button -> {
            FullbrightMod.setFullbright(!FullbrightMod.enabled);
            button.setMessage(getToggleText());
        }).bounds(centerX - 100, centerY - 24, 200, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Listo"), button -> {
            if (this.minecraft != null) {
                this.minecraft.setScreen(this.parent);
            }
        }).bounds(centerX - 100, centerY + 10, 200, 20).build());
    }

    private Component getToggleText() {
        return Component.literal("Fullbright: " + (FullbrightMod.enabled ? "§aACTIVADO" : "§cDESACTIVADO"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 50, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}

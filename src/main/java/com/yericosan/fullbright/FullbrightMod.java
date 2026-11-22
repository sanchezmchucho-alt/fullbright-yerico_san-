
package com.yericosan.fullbright;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FullbrightMod.MODID)
public class FullbrightMod {

    public static final String MODID = "fullbright";

    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(
                    Identifier.fromNamespaceAndPath(MODID, "keys")
            );

    public static final KeyMapping FULLBRIGHT_KEY = new KeyMapping(
            "key.fullbright.toggle",
            InputConstants.Type.KEYBOARD,
            InputConstants.KEY_B,
            CATEGORY
    );

    public static boolean enabled = false;
    private static double savedGamma = 1.0;

    public FullbrightMod(FMLJavaModLoadingContext context) {

        RegisterKeyMappingsEvent.BUS.addListener(
                this::onRegisterKeys
        );

        TickEvent.ClientTickEvent.Post.BUS.addListener(
                this::onClientTick
        );

        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, lastScreen) ->
                                new FullbrightConfigScreen(lastScreen)
                )
        );
    }

    private void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(FULLBRIGHT_KEY);
    }

    public static void setFullbright(boolean state) {

        Minecraft mc = Minecraft.getInstance();

        if (enabled == state) return;

        enabled = state;

        if (enabled) {
            savedGamma = mc.options.gamma().get();
            mc.options.gamma().set(15.0);
        } else {
            mc.options.gamma().set(savedGamma);
        }
    }

    private void onClientTick(TickEvent.ClientTickEvent.Post event) {

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;

        while (FULLBRIGHT_KEY.consumeClick()) {

            setFullbright(!enabled);

            String msg = enabled
                    ? "§a[Fullbright] Activado"
                    : "§c[Fullbright] Desactivado";

            mc.player.sendSystemMessage(
        Component.literal(msg)
);
        }
    }
}

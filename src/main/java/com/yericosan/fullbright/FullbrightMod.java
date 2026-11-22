package com.yericosan.fullbright;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod(FullbrightMod.MODID)
public class FullbrightMod {
    public static final String MODID = "fullbright";

    public static final KeyMapping FULLBRIGHT_KEY = new KeyMapping(
            "key.fullbright.toggle",
            GLFW.GLFW_KEY_B,
            "category.fullbright.keys"
    );

    public static boolean enabled = false;
    private static double savedGamma = 1.0;

    public FullbrightMod() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onRegisterKeys);

        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, lastScreen) -> new FullbrightConfigScreen(lastScreen)
                )
        );

        MinecraftForge.EVENT_BUS.register(this);
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

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        while (FULLBRIGHT_KEY.consumeClick()) {
            setFullbright(!enabled);
            String msg = enabled ? "§a[Fullbright] Activado" : "§c[Fullbright] Desactivado";
            mc.player.sendSystemMessage(Component.literal(msg));
        }
    }
}

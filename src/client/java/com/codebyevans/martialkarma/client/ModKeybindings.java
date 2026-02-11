package com.codebyevans.martialkarma.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeybindings {

    public static KeyBinding heartMarkKey;

    public static void register() {
        heartMarkKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.martialkarma.heart_mark",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.martialkarma.abilities"
        ));
    }
}
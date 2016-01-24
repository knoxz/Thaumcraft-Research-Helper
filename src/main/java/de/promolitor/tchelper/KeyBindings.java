package de.promolitor.tchelper;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {

	public static KeyBinding tcSolveScreen;

	public static void init() {
		tcSolveScreen = new KeyBinding("Solve TC Research Node", Keyboard.KEY_ADD , "TC Research Helper");

		ClientRegistry.registerKeyBinding(tcSolveScreen);
	}
}
package de.promolitor.tchelper;

import javax.swing.plaf.synth.SynthSplitPaneUI;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderGuiHandler {

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderExperienceBar(RenderGameOverlayEvent event) {
		if (event.type != ElementType.EXPERIENCE || !KeyInputEvent.drawing) {
			return;
		}
		new GuiMain(Minecraft.getMinecraft());
	}
}

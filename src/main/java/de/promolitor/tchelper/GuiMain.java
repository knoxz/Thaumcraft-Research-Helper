package de.promolitor.tchelper;

import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import de.promolitor.tchelper.helper.AspectCalculation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMain extends Gui {

	private ResourceLocation tchelperBackground = new ResourceLocation(TCHelperMain.MODID, "TCHelperBackground.png");
	private ResourceLocation tchelperRight = new ResourceLocation(TCHelperMain.MODID, "right.png");

	/**
	 * @param mc
	 * @param event
	 */
	public GuiMain(Minecraft mc) {
		ScaledResolution scaled = new ScaledResolution(mc);
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();
		int scale = TCHelperMain.aspectScale.getInt();

		GlStateManager.pushAttrib();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		// THX to <diesieben07> for figuring out why my exp bar was scrambled.
		GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);

		if (TCHelperMain.leftSide.getBoolean()) {
			int posX = 2;
			int posY = TCHelperMain.topDistance.getInt();
			for (String[] solved : AspectCalculation.solvedIssues) {
				// System.out.println(posX + "/" + posY);
				int aspects = solved.length;
				int overallSpaces = aspects + aspects - 1;
				for (int y = 0; y < aspects; y++) {
					Aspect as = AspectCalculation.map.get(solved[y]);
					mc.getTextureManager().bindTexture(as.getImage());
					Color c = new Color(as.getColor());
					int red = c.getRed();
					int green = c.getGreen();
					int blue = c.getBlue();

					// Thx for pointing out my float mistake -> gigaherz
					GlStateManager.color(red / 255.0f, green / 255.0f, blue / 255.0f, 1f);
					drawModalRectWithCustomSizedTexture((posX + (0 + y) * (scale * 2)), posY, 0, 0, scale, scale, scale,
							scale);
					if (y != aspects - 1) {
						mc.getTextureManager().bindTexture(tchelperRight);
						drawModalRectWithCustomSizedTexture((posX + ((0 + y) * (scale * 2) + scale)), posY, 0, 0, scale,
								scale, scale, scale);
					}

				}

				posY = posY + 20;
			}
		} else {
			int posX = width + scale - 1;
			int posY = TCHelperMain.topDistance.getInt();
			for (String[] solved : AspectCalculation.solvedIssues) {
				// System.out.println(posX + "/" + posY);
				int aspects = solved.length;
				int overallSpaces = aspects + aspects - 1;
				for (int y = 0; y < aspects; y++) {
					Aspect as = AspectCalculation.map.get(solved[y]);
					mc.getTextureManager().bindTexture(as.getImage());
					Color c = new Color(as.getColor());
					int red = c.getRed();
					int green = c.getGreen();
					int blue = c.getBlue();

					// Thx for pointing out my float mistake -> gigaherz
					GlStateManager.color(red / 255.0f, green / 255.0f, blue / 255.0f, 1f);
					drawModalRectWithCustomSizedTexture((posX - (aspects - y) * (scale * 2)), posY, 0, 0, scale, scale,
							scale, scale);
					if (y != aspects - 1) {
						mc.getTextureManager().bindTexture(tchelperRight);
						drawModalRectWithCustomSizedTexture((posX - ((aspects - y) * (scale * 2) - scale)), posY, 0, 0,
								scale, scale, scale, scale);
					}

				}

				posY = posY + 20;
			}
		}
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.resetColor();
		GlStateManager.popAttrib();
		GL11.glPopAttrib();
	}
}

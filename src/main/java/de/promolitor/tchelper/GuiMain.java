package de.promolitor.tchelper;

import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;

import java.awt.Color;
import java.util.ArrayList;

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
	int posY;
	int posX;
	ArrayList<Aspect> notPrimal = new ArrayList<Aspect>();

	/**
	 * @param mc
	 * @param event
	 */
	public GuiMain(Minecraft mc) {
		ScaledResolution scaled = new ScaledResolution(mc);
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();
		int scale = TCHelperMain.aspectScale.getInt();

		posX = 20;
		posY = TCHelperMain.topDistance.getInt();

		GlStateManager.pushAttrib();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		// THX to <diesieben07> for figuring out why my exp bar was scrambled.
		GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);

		if (TCHelperMain.leftSide.getBoolean()) {
			notPrimal = new ArrayList<Aspect>();
			for (String[] solved : AspectCalculation.solvedIssues) {
				// System.out.println(posX + "/" + posY);
				int aspects = solved.length;
				int overallSpaces = aspects + aspects - 1;
				for (int y = 0; y < aspects; y++) {
					Aspect as = Aspect.aspects.get(solved[y]);
					int tmpY = posY;
					if (!as.isPrimal()) {
						checkPrimalDeep(as);
					}
					posY = tmpY;
					posX = 20;
					drawAspect(as, mc, scale, y);

					if (y != aspects - 1) {
						mc.getTextureManager().bindTexture(tchelperRight);
						drawModalRectWithCustomSizedTexture((posX + ((0 + y) * (scale * 2) + scale)), posY, 0, 0, scale,
								scale, scale, scale);
					}

				}

				posY = posY + scale;

				for (Aspect as : notPrimal) {
					addParents(as, mc, scale, 1);
					posY = posY + scale;
				}
				if (!notPrimal.isEmpty()) {
					posY = posY + scale;
				}
				notPrimal.clear();
			}
		}
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.resetColor();
		GlStateManager.popAttrib();
		GL11.glPopAttrib();
	}

	public void drawAspect(Aspect as, Minecraft mc, int scale, int y) {
		mc.getTextureManager().bindTexture(as.getImage());
		Color c = new Color(as.getColor());
		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();

		// Thx for pointing out my float mistake -> gigaherz
		GlStateManager.color(red / 255.0f, green / 255.0f, blue / 255.0f, 1f);
		drawModalRectWithCustomSizedTexture((posX + (0 + y) * (scale * 2)), posY, 0, 0, scale, scale, scale, scale);

	}

	private void checkPrimalDeep(Aspect as) {
		if (!as.isPrimal()) {
			if (!notPrimal.contains(as)) {
				notPrimal.add(as);
			}
			checkPrimalDeep(as.getComponents()[0]);
			checkPrimalDeep(as.getComponents()[1]);
		}

	}

	public void addParents(Aspect as, Minecraft mc, int scale, int y) {
		y = 1;
		Aspect p1 = as.getComponents()[0];
		Aspect p2 = as.getComponents()[1];
		// System.out.println("Aspect: " + as.getTag() + " = " + p1.getTag() + "
		// + " + p2.getTag());
		drawAspect(as, mc, scale, y);
		mc.getTextureManager().bindTexture(tchelperRight);
		drawModalRectWithCustomSizedTexture((posX + ((0 + y) * (scale * 2) + scale)), posY, 0, 0, scale,
				scale, scale, scale);
		y++;
		drawAspect(p1, mc, scale, y);
		y++;
		drawAspect(p2, mc, scale, y);

	}
}

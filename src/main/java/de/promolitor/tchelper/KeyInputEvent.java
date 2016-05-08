package de.promolitor.tchelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import de.promolitor.tchelper.helper.AspectCalculation;
import de.promolitor.tchelper.helper.Combination;
import de.promolitor.tchelper.helper.Hexagon;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import thaumcraft.api.aspects.Aspect;

public class KeyInputEvent {

	public static boolean drawing = false;

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {

		if (KeyBindings.tcSolveScreen.isPressed()) {
			if (drawing) {
				drawing = false;
			} else {
				ItemStack heldItem = TCHelperMain.mc.thePlayer.inventory.getCurrentItem();
				if (heldItem != null) {
					if (heldItem.getItem().getUnlocalizedName().equals("item.research_notes")) {
						NBTTagList aspectsTagList = heldItem.getTagCompound().getTagList("aspects", 10);

						HashMap<String, Integer> givenAspects = new HashMap<String, Integer>();
						for (int i = 0; i < aspectsTagList.tagCount(); ++i) {
							NBTTagCompound tag = aspectsTagList.getCompoundTagAt(i);
							givenAspects.put(tag.getString("key"), tag.getInteger("amount"));
						}

						NBTTagList hexsTagList = heldItem.getTagCompound().getTagList("hexgrid", 10);
						ArrayList<Hexagon> researchAspects = new ArrayList<Hexagon>();
						NBTTagCompound currentHex = hexsTagList.getCompoundTagAt(0);
						for (int i = 0; i <= 100; i++) {
							if (currentHex.getTag("aspect") != null) {
								String aspect = currentHex.getString("aspect");
								researchAspects.add(
										new Hexagon(currentHex.getByte("hexq"), currentHex.getByte("hexr"), aspect));
							}
							if (hexsTagList.getCompoundTagAt(i) != null) {
								currentHex = hexsTagList.getCompoundTagAt(i);
							} else {
								currentHex = null;
							}
						}
						if (TCHelperMain.debugging) {
							System.out.println("Given Aspects: " + givenAspects);
							System.out.println("Given Research Nodes:" + researchAspects);
						}
						AspectCalculation.clearIssues();
						ArrayList<Combination> toCheck = new ArrayList<Combination>();
						checkRest(researchAspects, toCheck);
						System.out.println("Combinations to check: " + toCheck);
						for (Combination combination : toCheck) {
							if (!Aspect.aspects.containsKey(combination.h1.aspect)
									|| !Aspect.aspects.containsKey(combination.h2.aspect)) {
								Minecraft.getMinecraft().thePlayer
										.addChatMessage(new ChatComponentText("Sorry, no Addon Aspect supported yet!"));
								return;
							}
							System.out.println("Checking" + combination.h1 + " to");
							AspectCalculation.solveIussesDeep(Aspect.aspects.get(combination.h1.aspect),
									Aspect.aspects.get(combination.h2.aspect),
									AspectCalculation.getDistance(combination.h1, combination.h2));

						}

						drawing = true;
					}
				}
			}

		}
	}

	private void checkRest(ArrayList<Hexagon> researchAspects, ArrayList<Combination> toCheck) {
		int minDistance = 10000;
		Hexagon h1 = null;
		Hexagon h2 = null;
		for (int i = 0; i < researchAspects.size(); i++) {
			minDistance = 10000;
			for (Hexagon hexagon : researchAspects) {
				if (!researchAspects.get(i).equals(hexagon)) {
					if (AspectCalculation.getDistance(hexagon, researchAspects.get(i)) < minDistance
							&& (!hexagon.visited || !researchAspects.get(i).visited)) {
						minDistance = AspectCalculation.getDistance(hexagon, researchAspects.get(i));
						h1 = hexagon;
						h2 = researchAspects.get(i);
					}

				}
			}

		}
		toCheck.add(new Combination(h1, h2));
		h1.visited = true;
		h2.visited = true;
		boolean stillNeedToCheck = false;
		for (Hexagon hexagon : researchAspects) {
			if (!hexagon.visited) {
				stillNeedToCheck = true;
			}
		}
		if (stillNeedToCheck) {
			checkRest(researchAspects, toCheck);
		}
	}
}

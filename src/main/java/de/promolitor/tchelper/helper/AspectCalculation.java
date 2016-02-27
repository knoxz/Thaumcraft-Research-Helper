package de.promolitor.tchelper.helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.promolitor.tchelper.TCHelperMain;
import thaumcraft.api.aspects.Aspect;

public class AspectCalculation {

	public static final int[][] NEIGHBOURS = { { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, 0 }, { -1, 1 }, { 0, 1 } };
	public static HashMap<Aspect, Integer> cost;
	// public static HashMap<String, Aspect> map = new HashMap<String,
	// Aspect>();
	private static int iteration = 0;

	public static ArrayList<String[]> solvedIssues;

	private static ArrayList<Path> paths;
	private static ArrayList<Path> pathSolutions;

	public static int getDistance(Hexagon a1, Hexagon a2) {
		return (Math.abs(a1.q - a2.q) + Math.abs(a1.r - a2.r) + Math.abs(a1.q + a1.r - a2.q - a2.r)) / 2;
	}

	public static void clearIssues() {
		solvedIssues = new ArrayList<String[]>();
	}

	public static void solveIussesDeep(Aspect start, Aspect destination, int distance) {
		paths = new ArrayList<Path>();
		pathSolutions = new ArrayList<Path>();
		if (!start.isPrimal()) {
			for (Aspect component : start.getComponents()) {
				ArrayList<Aspect> pathSoFar = new ArrayList<Aspect>();
				pathSoFar.add(start);
				paths.add(new Path(component, pathSoFar));

			}
		}
		for (Map.Entry<String, Aspect> entry : Aspect.aspects.entrySet()) {
			Aspect as = entry.getValue();
			if (!as.isPrimal()) {
				for (Aspect component : as.getComponents()) {
					if (component.getTag().equals(start.getTag())) {
						ArrayList<Aspect> pathSoFar = new ArrayList<Aspect>();
						pathSoFar.add(start);
						paths.add(new Path(as, pathSoFar));
					}
				}
			}

		}
		for (int i = 1; i < distance + 2; i++) {
			createPathsNewPaths();
			if (checkForSolved(destination, distance + 1)) {
				break;
			}
		}
		if (pathSolutions.size() > 1) {
			// for (Path p : pathSolutions) {
			// System.out.println("Solution Path:");
			// for (Aspect is : p.pathSoFar) {
			// System.out.println(is.getTag() + " / ");
			// }
			// System.out.println("With Path Cost: " + p.getCost());
			//
			// }
			int cost = Integer.MAX_VALUE;
			int id = 0;
			int iter = 0;
			for (Path is : pathSolutions) {
				int tmpCost = is.getCost();
				if (tmpCost < cost) {
					id = iter;
					cost = tmpCost;
				}
				iter++;
			}
			String[] solution = new String[pathSolutions.get(id).pathSoFar.size() + 1];
			for (int i = 0; i < solution.length - 1; i++) {
				solution[i] = pathSolutions.get(id).pathSoFar.get(i).getTag();
			}
			solution[solution.length - 1] = pathSolutions.get(id).currentAspect.getTag();
			solvedIssues.add(solution);
			System.out.println("SOLUTION FOUND: " + solution.toString());

		} else {
			String[] solution = new String[pathSolutions.get(0).pathSoFar.size() + 1];
			for (int i = 0; i < solution.length - 1; i++) {
				solution[i] = pathSolutions.get(0).pathSoFar.get(i).getTag();
			}
			solution[solution.length - 1] = pathSolutions.get(0).currentAspect.getTag();
			solvedIssues.add(solution);
			System.out.println("SOLUTION FOUND: " + solution.toString());
		}

	}

	private static boolean checkForSolved(Aspect destination, int distance) {
		for (Path p : paths) {
			if (p.currentAspect.getTag().equals(destination.getTag()) && (p.pathSoFar.size() + 1 == distance
					|| p.pathSoFar.size() + 1 == distance + 1 || p.pathSoFar.size() + 1 == distance + 2)) {
				pathSolutions.add(p);
			}
		}
		if (pathSolutions.size() > 0) {
			return true;
		} else {
			return false;
		}
		// TODO Auto-generated method stub

	}

	private static void createPathsNewPaths() {
		ArrayList<Path> tempPaths = new ArrayList<Path>();
		for (Path p : paths) {
			if (!p.currentAspect.isPrimal()) {
				for (Aspect component : p.currentAspect.getComponents()) {
					ArrayList<Aspect> pathSoFar = new ArrayList<Aspect>();
					for (Aspect aspect : p.pathSoFar) {
						pathSoFar.add(aspect);
					}
					pathSoFar.add(p.currentAspect);
					tempPaths.add(new Path(component, pathSoFar));

				}
			}
			for (Map.Entry<String, Aspect> entry : Aspect.aspects.entrySet()) {
				Aspect as = entry.getValue();
				if (!as.isPrimal()) {
					for (Aspect component : as.getComponents()) {
						if (component.getTag().equals(p.currentAspect.getTag())) {
							ArrayList<Aspect> pathSoFar = new ArrayList<Aspect>();
							for (Aspect aspect : p.pathSoFar) {
								pathSoFar.add(aspect);
							}
							pathSoFar.add(p.currentAspect);
							tempPaths.add(new Path(as, pathSoFar));
						}
					}
				}

			}

		}
		paths = tempPaths;
	}

	public static void createCosts() {
		cost = new HashMap<Aspect, Integer>();
		for (Map.Entry<String, Aspect> entry : Aspect.aspects.entrySet()) {
			if (!entry.getValue().isPrimal()) {
				aspectCost = 0;
				checkCost(entry.getValue());
				System.out.println("Cost of " + entry.getValue().getTag() + " = " + aspectCost);
				cost.put(entry.getValue(), aspectCost);
			}
		}

	}

	private static int aspectCost;

	private static void checkCost(Aspect aspect) {

		for (Aspect as : aspect.getComponents()) {
			if (as.isPrimal()) {
				aspectCost++;
			} else {
				checkCost(as);
			}
		}
	}
}

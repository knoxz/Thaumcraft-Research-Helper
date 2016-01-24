package de.promolitor.tchelper.helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.WeightedGraph;

import de.promolitor.tchelper.TCHelperMain;
import thaumcraft.api.aspects.Aspect;

public class AspectCalculation {

	public static final int[][] NEIGHBOURS = { { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, 0 }, { -1, 1 }, { 0, 1 } };
	public static UndirectedGraph<String, DefaultWeightedEdge> graph;
	public static HashMap<String, Aspect> map = new HashMap<String, Aspect>();

	public static ArrayList<String[]> solvedIssues;

	public static int getDistance(Hexagon a1, Hexagon a2) {
		return (Math.abs(a1.q - a2.q) + Math.abs(a1.r - a2.r) + Math.abs(a1.q + a1.r - a2.q - a2.r)) / 2;
	}

	public static void clearIssues() {
		solvedIssues = new ArrayList<String[]>();
	}

	public static String[] solveIssues(Aspect start, Aspect destination) {
		DijkstraShortestPath test = new DijkstraShortestPath(graph, start.getTag(), destination.getTag());
		List list = test.getPathEdgeList();
		String[] paths = new String[list.size() + 1];
		DefaultEdge first = (DefaultWeightedEdge) list.remove(0);

		String stripped = first.toString().replaceAll(" ", "").replaceAll("\\(", "").replaceAll("\\)", "");
		String[] splitted = stripped.split(":");
		if (splitted[0].equals(start.getTag())) {
			paths[0] = start.getTag();
			paths[1] = splitted[1];
		} else {
			paths[0] = start.getTag();
			paths[1] = splitted[0];
		}

		if (TCHelperMain.debugging) {
			System.out.println("");
			System.out.println("-----STARTING DIJKSTRA-----");
			int i = 2;
			for (Object object : list) {
				DefaultEdge a = (DefaultWeightedEdge) object;
				stripped = a.toString().replaceAll(" ", "").replaceAll("\\(", "").replaceAll("\\)", "");
				splitted = stripped.split("\\:");
				if (paths[i - 1].equals(splitted[0])) {
					paths[i] = splitted[1];
				} else {
					paths[i] = splitted[0];
				}
				// System.out.println(a);
				i++;
			}
		}
		if (TCHelperMain.debugging) {
			System.out.println("");
			for (int i = 0; i < paths.length; i++) {
				System.out.println(paths[i]);
			}
			System.out.println("");
		}
		solvedIssues.add(paths);
		return paths;

	}

	public static void createGraph() {
		graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		graph.addVertex(thaumcraft.api.aspects.Aspect.AIR.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.EARTH.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.FIRE.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.WATER.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.ORDER.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.ENTROPY.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.VOID.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.LIGHT.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.MOTION.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.COLD.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.CRYSTAL.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.METAL.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.LIFE.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.DEATH.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.ENERGY.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.EXCHANGE.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.AURA.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.FLUX.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.DARKNESS.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.ELDRITCH.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.FLIGHT.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.PLANT.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.TOOL.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.CRAFT.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.MECHANISM.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.TRAP.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.SOUL.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.MIND.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.SENSES.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.AVERSION.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.PROTECT.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.DESIRE.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.UNDEAD.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.BEAST.getTag());
		graph.addVertex(thaumcraft.api.aspects.Aspect.MAN.getTag());

		// VOID = new Aspect("vacuos", "AIR", "ENTROPY", 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.VOID.getTag(), thaumcraft.api.aspects.Aspect.AIR.getTag(),
				2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.VOID.getTag(),
				thaumcraft.api.aspects.Aspect.ENTROPY.getTag(), 2);

		// LIGHT = new Aspect("lux", "AIR", "FIRE", 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.LIGHT.getTag(), thaumcraft.api.aspects.Aspect.AIR.getTag(),
				2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.LIGHT.getTag(), thaumcraft.api.aspects.Aspect.FIRE.getTag(),
				2);

		// MOTION = new Aspect("motus", "AIR", "ORDER", 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.MOTION.getTag(), thaumcraft.api.aspects.Aspect.AIR.getTag(),
				2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.MOTION.getTag(),
				thaumcraft.api.aspects.Aspect.ORDER.getTag(), 2);

		// COLD = new Aspect("gelum", "FIRE", "ENTROPY", 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.COLD.getTag(), thaumcraft.api.aspects.Aspect.FIRE.getTag(),
				2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.COLD.getTag(),
				thaumcraft.api.aspects.Aspect.ENTROPY.getTag(), 2);

		// CRYSTAL = new Aspect("vitreus", "EARTH", "AIR", 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.CRYSTAL.getTag(),
				thaumcraft.api.aspects.Aspect.EARTH.getTag(), 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.CRYSTAL.getTag(),
				thaumcraft.api.aspects.Aspect.AIR.getTag(), 2);

		// METAL = new Aspect("metallum", "EARTH", "ORDER", 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.METAL.getTag(),
				thaumcraft.api.aspects.Aspect.EARTH.getTag(), 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.METAL.getTag(),
				thaumcraft.api.aspects.Aspect.ORDER.getTag(), 2);

		// LIFE = new Aspect("victus", "EARTH", "WATER", 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.LIFE.getTag(), thaumcraft.api.aspects.Aspect.EARTH.getTag(),
				2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.LIFE.getTag(), thaumcraft.api.aspects.Aspect.WATER.getTag(),
				2);

		// DEATH = new Aspect("mortuus", "WATER", "ENTROPY", 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.DEATH.getTag(),
				thaumcraft.api.aspects.Aspect.WATER.getTag(), 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.DEATH.getTag(),
				thaumcraft.api.aspects.Aspect.ENTROPY.getTag(), 2);

		// ENERGY = new Aspect("potentia", "ORDER", "FIRE", 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.ENERGY.getTag(),
				thaumcraft.api.aspects.Aspect.ORDER.getTag(), 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.ENERGY.getTag(),
				thaumcraft.api.aspects.Aspect.FIRE.getTag(), 2);

		// EXCHANGE = new Aspect("permutatio", "ENTROPY", "ORDER", 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.EXCHANGE.getTag(),
				thaumcraft.api.aspects.Aspect.ENTROPY.getTag(), 2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.EXCHANGE.getTag(),
				thaumcraft.api.aspects.Aspect.ORDER.getTag(), 2);

		// AURA = new Aspect("auram", "ENERGY", "AIR", 3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.AURA.getTag(),
				thaumcraft.api.aspects.Aspect.ENERGY.getTag(), 3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.AURA.getTag(), thaumcraft.api.aspects.Aspect.AIR.getTag(),
				3);

		// FLUX = new Aspect("vitium", "ENTROPY", "ENERGY", 3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.FLUX.getTag(),
				thaumcraft.api.aspects.Aspect.ENTROPY.getTag(), 3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.FLUX.getTag(),
				thaumcraft.api.aspects.Aspect.ENERGY.getTag(), 3);

		// DARKNESS = new Aspect("tenebrae", "VOID", "LIGHT", 3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.DARKNESS.getTag(),
				thaumcraft.api.aspects.Aspect.VOID.getTag(), 3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.DARKNESS.getTag(),
				thaumcraft.api.aspects.Aspect.LIGHT.getTag(), 3);

		// ELDRITCH = new Aspect("alienis", "VOID", "DARKNESS", 3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.ELDRITCH.getTag(),
				thaumcraft.api.aspects.Aspect.VOID.getTag(), 3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.ELDRITCH.getTag(),
				thaumcraft.api.aspects.Aspect.DARKNESS.getTag(), 3);

		// FLIGHT = new Aspect("volatus", "AIR", "MOTION", 3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.FLIGHT.getTag(), thaumcraft.api.aspects.Aspect.AIR.getTag(),
				2);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.FLIGHT.getTag(),
				thaumcraft.api.aspects.Aspect.MOTION.getTag(), 3);

		// PLANT = new Aspect("herba", "LIFE", "EARTH", 3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.PLANT.getTag(), thaumcraft.api.aspects.Aspect.LIFE.getTag(),
				3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.PLANT.getTag(),
				thaumcraft.api.aspects.Aspect.EARTH.getTag(), 3);

		// TOOL = new Aspect("instrumentum", "METAL", "ENERGY", 4);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.TOOL.getTag(), thaumcraft.api.aspects.Aspect.METAL.getTag(),
				4);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.TOOL.getTag(),
				thaumcraft.api.aspects.Aspect.ENERGY.getTag(), 4);

		// CRAFT = new Aspect("fabrico", "EXCHANGE", "TOOL", 6);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.CRAFT.getTag(),
				thaumcraft.api.aspects.Aspect.EXCHANGE.getTag(), 6);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.CRAFT.getTag(), thaumcraft.api.aspects.Aspect.TOOL.getTag(),
				6);

		// MECHANISM = new Aspect("machina", "MOTION", "TOOL", 6);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.MECHANISM.getTag(),
				thaumcraft.api.aspects.Aspect.MOTION.getTag(), 6);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.MECHANISM.getTag(),
				thaumcraft.api.aspects.Aspect.TOOL.getTag(), 6);

		// TRAP = new Aspect("vinculum", "MOTION", "ENTROPY", 3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.TRAP.getTag(),
				thaumcraft.api.aspects.Aspect.MOTION.getTag(), 3);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.TRAP.getTag(),
				thaumcraft.api.aspects.Aspect.ENTROPY.getTag(), 3);

		// SOUL = new Aspect("spiritus", "LIFE", "DEATH", 4);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.SOUL.getTag(), thaumcraft.api.aspects.Aspect.LIFE.getTag(),
				4);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.SOUL.getTag(), thaumcraft.api.aspects.Aspect.DEATH.getTag(),
				4);

		// MIND = new Aspect("cognitio", "FIRE", "SOUL", 5);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.MIND.getTag(), thaumcraft.api.aspects.Aspect.FIRE.getTag(),
				5);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.MIND.getTag(), thaumcraft.api.aspects.Aspect.SOUL.getTag(),
				5);

		// SENSES = new Aspect("sensus", "AIR", "SOUL", 5);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.SENSES.getTag(), thaumcraft.api.aspects.Aspect.AIR.getTag(),
				5);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.SENSES.getTag(),
				thaumcraft.api.aspects.Aspect.SOUL.getTag(), 5);

		// AVERSION = new Aspect("aversio", "SOUL", "ENTROPY", 5);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.AVERSION.getTag(),
				thaumcraft.api.aspects.Aspect.SOUL.getTag(), 5);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.AVERSION.getTag(),
				thaumcraft.api.aspects.Aspect.ENTROPY.getTag(), 5);

		// PROTECT = new Aspect("praemunio", "SOUL", "EARTH", 5);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.PROTECT.getTag(),
				thaumcraft.api.aspects.Aspect.SOUL.getTag(), 5);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.PROTECT.getTag(),
				thaumcraft.api.aspects.Aspect.EARTH.getTag(), 5);

		// DESIRE = new Aspect("desiderium", "SOUL", "VOID", 6);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.DESIRE.getTag(),
				thaumcraft.api.aspects.Aspect.SOUL.getTag(), 6);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.DESIRE.getTag(),
				thaumcraft.api.aspects.Aspect.VOID.getTag(), 6);

		// UNDEAD = new Aspect("exanimis", "MOTION", "DEATH", 4);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.UNDEAD.getTag(),
				thaumcraft.api.aspects.Aspect.MOTION.getTag(), 4);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.UNDEAD.getTag(),
				thaumcraft.api.aspects.Aspect.DEATH.getTag(), 4);

		// BEAST = new Aspect("bestia", "MOTION", "LIFE", 4);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.BEAST.getTag(),
				thaumcraft.api.aspects.Aspect.MOTION.getTag(), 4);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.BEAST.getTag(), thaumcraft.api.aspects.Aspect.LIFE.getTag(),
				4);

		// MAN = new Aspect("humanus", "SOUL", "LIFE", 6);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.MAN.getTag(), thaumcraft.api.aspects.Aspect.SOUL.getTag(),
				6);
		Graphs.addEdge(graph, thaumcraft.api.aspects.Aspect.MAN.getTag(), thaumcraft.api.aspects.Aspect.LIFE.getTag(),
				6);

		map.put("aer", thaumcraft.api.aspects.Aspect.AIR);
		map.put("terra", thaumcraft.api.aspects.Aspect.EARTH);
		map.put("ignis", thaumcraft.api.aspects.Aspect.FIRE);
		map.put("aqua", thaumcraft.api.aspects.Aspect.WATER);
		map.put("perditio", thaumcraft.api.aspects.Aspect.ENTROPY);
		map.put("ordo", thaumcraft.api.aspects.Aspect.ORDER);
		map.put("vacuos", thaumcraft.api.aspects.Aspect.VOID);
		map.put("lux", thaumcraft.api.aspects.Aspect.LIGHT);
		map.put("motus", thaumcraft.api.aspects.Aspect.MOTION);
		map.put("gelum", thaumcraft.api.aspects.Aspect.COLD);
		map.put("vitreus", thaumcraft.api.aspects.Aspect.CRYSTAL);
		map.put("metallum", thaumcraft.api.aspects.Aspect.METAL);
		map.put("victus", thaumcraft.api.aspects.Aspect.LIFE);
		map.put("mortuus", thaumcraft.api.aspects.Aspect.DEATH);
		map.put("potentia", thaumcraft.api.aspects.Aspect.ENERGY);
		map.put("permutatio", thaumcraft.api.aspects.Aspect.EXCHANGE);
		map.put("auram", thaumcraft.api.aspects.Aspect.AURA);
		map.put("vitium", thaumcraft.api.aspects.Aspect.FLUX);
		map.put("tenebrae", thaumcraft.api.aspects.Aspect.DARKNESS);
		map.put("alienis", thaumcraft.api.aspects.Aspect.ELDRITCH);
		map.put("volatus", thaumcraft.api.aspects.Aspect.FLIGHT);
		map.put("herba", thaumcraft.api.aspects.Aspect.PLANT);
		map.put("instrumentum", thaumcraft.api.aspects.Aspect.TOOL);
		map.put("fabrico", thaumcraft.api.aspects.Aspect.CRAFT);
		map.put("machina", thaumcraft.api.aspects.Aspect.MECHANISM);
		map.put("vinculum", thaumcraft.api.aspects.Aspect.TRAP);
		map.put("spiritus", thaumcraft.api.aspects.Aspect.SOUL);
		map.put("cognitio", thaumcraft.api.aspects.Aspect.MIND);
		map.put("sensus", thaumcraft.api.aspects.Aspect.SENSES);
		map.put("aversio", thaumcraft.api.aspects.Aspect.AVERSION);
		map.put("praemunio", thaumcraft.api.aspects.Aspect.PROTECT);
		map.put("desiderium", thaumcraft.api.aspects.Aspect.DESIRE);
		map.put("exanimis", thaumcraft.api.aspects.Aspect.UNDEAD);
		map.put("bestia", thaumcraft.api.aspects.Aspect.BEAST);
		map.put("humanus", thaumcraft.api.aspects.Aspect.MAN);

	}
}

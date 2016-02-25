package de.promolitor.tchelper.helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	// public static HashMap<String, Aspect> map = new HashMap<String,
	// Aspect>();
	private static int iteration = 0;

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

	/**
	 * 
	 */
	/**
	 * 
	 */
	public static void createGraph() {
		graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		for (Map.Entry<String, Aspect> entry : Aspect.aspects.entrySet()) {
			System.out.println("ADDING ASPECT: " + entry.getKey());
			ArrayList<Aspect> ar = entry.getValue().getPrimalAspects();
			for (Aspect aspect : ar) {
				System.out.println(aspect.getTag());
			}
			System.out.println(entry.getValue().getPrimalAspects());
			graph.addVertex(entry.getKey());

		}

		for (Map.Entry<String, Aspect> entry : Aspect.aspects.entrySet()) {
			if (!entry.getValue().isPrimal()) {
				System.out.println("Adding ");
				Graphs.addEdge(graph, entry.getKey(), entry.getValue().getComponents()[0].getTag(),
						entry.getValue().getCompoundAspects().size());
				Graphs.addEdge(graph, entry.getKey(), entry.getValue().getComponents()[1].getTag(),
						entry.getValue().getCompoundAspects().size());

			}

		}

	}
}

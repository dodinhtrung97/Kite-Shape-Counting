import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

/**
 * Created by dodin on 4/7/2018.
 */
public class Omyim {

    public static void main(String[] args) throws IOException {
        SimpleGraph<Integer, DefaultEdge> graph = readGraph(args[0]);
        System.out.println("Number of vetices: " + graph.vertexSet().size());
        System.out.println("Number of edges: " + graph.edgeSet().size());
        System.out.println("Number of omyims: " + countOmyim(graph));
        long start = System.currentTimeMillis();
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - start)/1000);
    }

    private static SimpleGraph<Integer, DefaultEdge> readGraph(String fileName) throws IOException {
        FileReader file = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(file);
        SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        String line = reader.readLine();
        while (line != null) {
            if (!line.startsWith("#")) {
                String[] tokens = line.split("\\s+");
                Integer node1 = Integer.valueOf(tokens[0]);
                Integer node2 = Integer.valueOf(tokens[1]);
                graph.addVertex(node1);
                graph.addVertex(node2);
                if (node1 != node2)
                    try {
                        graph.addEdge(node1, node2);
                    } catch (IllegalArgumentException e) {}
            }
            line = reader.readLine();
        }
        reader.close();
        return graph;
    }

    /**
     * SOURCE: https://gist.github.com/linzhp/5efdf01533cf5d2d8271#file-app-java
     * The original code dealt with triangle counting
     * The code has been modified to work with Omyim shape counting
     * @param graph
     * @return
     */
    private static long countOmyim(SimpleGraph<Integer, DefaultEdge> graph) {
        long omyimCount = 0;
        for (Integer v : graph.vertexSet()) {
            Set<DefaultEdge> edges = graph.edgesOf(v);
            int[] neighbors = new int[edges.size()];
            int i = 0;
            for (DefaultEdge e : edges) {
                neighbors[i] = graph.getEdgeTarget(e);
                if (neighbors[i] == v) {
                    neighbors[i] = graph.getEdgeSource(e);
                }
                i++;
            }
            for (i=0; i < neighbors.length; i++) {
                for (int j = i + 1; j < neighbors.length; j++) {
                    if (graph.containsEdge(neighbors[i], neighbors[j]) || graph.containsEdge(neighbors[j], neighbors[i])) {
                        omyimCount += graph.degreeOf(v) - 2;
                    }
                }
            }
        }
        return omyimCount*2;
    }
}

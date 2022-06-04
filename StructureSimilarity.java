package ged;

import static ged.GED.formatter;
import static ged.Util.getAverageSimilarity;
import java.time.Instant;
import java.util.List;


/**
 *
 * Anoucka Giberius
 */
public class StructureSimilarity {

    public static double sim(DirectedGraph g1, DirectedGraph g2) {
        Double sim = 0.0;
        Integer commonNodes = commonNodes(g1, g2);
        Integer totalNodesG1 = totalNodes(g1);
        Integer totalNodesG2 = totalNodes(g2);
        Integer commonEdges = commonEdges(g1, g2);
        Integer totalEdgesG1 = totalEdges(g1);
        Integer totalEdgesG2 = totalEdges(g2);
        
        sim = 2 * (commonNodes.doubleValue() + commonEdges.doubleValue())
                / (totalNodesG1.doubleValue() + totalNodesG2.doubleValue() + totalEdgesG1.doubleValue() + totalEdgesG2.doubleValue());
        
        return sim;
    }
    
    public static double[][] buildSimStrucMatrix(List<Case> cases) {
        double[][] simStrucMatrix = new double[cases.size()][cases.size()];
        double sim;
        for (int i = 0; i < cases.size(); i++) {
            for (int j = 0; j < cases.size(); j++) {
                if (j > i) {
                    sim = StructureSimilarity.sim(cases.get(i).getGraph(), cases.get(j).getGraph());
                    simStrucMatrix[i][j] = sim;
                     simStrucMatrix[j][i] = sim;
                }
            }
        }
        return simStrucMatrix;
    }
    
    public static void printAverageSimilarityClusters(List<Cluster> clusters) {
         double[][] simMatrix;
         int counter =0;
        for (Cluster cluster : clusters) {
            simMatrix = buildSimStrucMatrix(cluster.getCases());
            System.out.println("Cluster " + counter + ": " + String.format("%5.4f", getAverageSimilarity(simMatrix)));
            counter++;
        }
    }
    
     public static double getAverageSimilarityClusters(List<Cluster> clusters) {
        double[][] simMatrix;
        double average=0.0;
        for (Cluster cluster : clusters ) {
            simMatrix = buildSimStrucMatrix(cluster.getCases());
            average = average + getAverageSimilarity(simMatrix);
        }
        return average/clusters.size();
    }

    private static int commonNodes(DirectedGraph g1, DirectedGraph g2) {
        int common = 0;
        if (g1 == null || g2 == null) {
            return common;
        }
        for (Node g1Node : g1.getNodes()) {
            for (Node g2Node : g2.getNodes()) {
                if (g1Node == g2Node) {
                    common++;
                }
            }
        }
        return common;
    }

    private static int totalNodes(DirectedGraph g) {
        return g.getSize();
    }

    private static int commonEdges(DirectedGraph g1, DirectedGraph g2) {
        int common = 0;
        if (g1 == null || g2 == null) {
            return common;
        }
        for (Node n1 : g1.getNodes()) {
            for (Edge e1 : g1.getEdges(n1)) {
                for (Node n2 : g2.getNodes()) {
                    for (Edge e2 : g2.getEdges(n2)) {
                        if (e1 == e2) {
                            common++;
                        }
                    }
                }
            }
        }
        return common;
    }

    private static int totalEdges(DirectedGraph g) {
        int total = 0;
        if (g == null) {
            return total;
        }
        for (Node n : g.getNodes()) {
            total = total + g.getEdges(n).size();
        }
        return total;
    }

}

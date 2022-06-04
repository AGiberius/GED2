package ged;

import static ged.GED.formatter;
import static ged.Util.getAverageSimilarity;
import java.time.Instant;
import java.util.List;

/**
 *
 * Anouchka Giberius
 */
public class Gsimilarity {

    private double[][] costMatrix;
    protected final double SUBSTITUTE_COST;
    protected final double INSERT_COST;
    protected final double DELETE_COST;

    protected final double MAXVALUE = Double.MAX_VALUE;
    //protected final double MAXVALUE = 99.0;

    private DirectedGraph g1, g2;

    public Gsimilarity(DirectedGraph g1, DirectedGraph g2, double subCost, double insCost, double delCost) {
        this.SUBSTITUTE_COST = subCost;
        this.INSERT_COST = insCost;
        this.DELETE_COST = delCost;
        this.g1 = g1;
        this.g2 = g2;
        this.costMatrix = createCostMatrix();
    }

    public Gsimilarity(DirectedGraph g1, DirectedGraph g2) {
        this(g1, g2, 3, 1, 1);
    }

    public static double[][] buildSimGedMatrix(List<Case> cases) {
        double[][] simGedMatrix = new double[cases.size()][cases.size()];
        double sim;
        for (int i = 0; i < cases.size(); i++) {
            for (int j = 0; j < cases.size(); j++) {
                if (j >= i) {
                    Gsimilarity gs = new Gsimilarity(cases.get(i).getGraph(), cases.get(j).getGraph());
                    sim = gs.getSim();
                    simGedMatrix[i][j] = sim;
                    simGedMatrix[j][i] = sim;
                }
            }
        }
        return simGedMatrix;
    }
    
    
    public static void printAverageSimilarityClusters(List<Cluster> clusters) {
         double[][] simMatrix;
         int counter =0;
        for (Cluster cluster : clusters) {
            simMatrix = Gsimilarity.buildSimGedMatrix(cluster.getCases());
            System.out.println("Cluster " + counter + ": " + String.format("%5.4f", getAverageSimilarity(simMatrix)));
            counter++;
        }
    }
    
    
     public static double getAverageSimilarityClusters(List<Cluster> clusters) {
        double[][] simMatrix;
        double average=0.0;
        for (Cluster cluster : clusters ) {
            simMatrix = buildSimGedMatrix(cluster.getCases());
            average = average + getAverageSimilarity(simMatrix);
        }
        return average/clusters.size();
    }

    private double getNormalizedDistance() {
        double graphLength = (g1.getSize() + g2.getSize()) / 2;
        return getDistance() / graphLength;
    }

    public double getDistance() {
        int[][] assignment = HungarianAlgorithm.hgAlgorithm(this.costMatrix, "min");
        // printCostMatrix2(assignment);
        double sum = 0;
        for (int i = 0; i < assignment.length; i++) {
            sum = (sum + costMatrix[assignment[i][0]][assignment[i][1]]);
        }

        return sum;
    }

    public double getSim() {
        double sim = 0.0;
        sim = 1.0 - (getDistance() / getMaxDistance());
        return sim;
    }

    private int getMaxDistance() {
        int max;
        max = g1.getSize() + g2.getSize();
        return max;
    }

    public double[][] getCostMatrix() {
        if (costMatrix == null) {
            this.costMatrix = createCostMatrix();
        }
        return costMatrix;
    }

    /**
     * Creates the cost matrix used as input to Munkres algorithm. The matrix
     * consists of 4 sectors: upper left, upper right, bottom left, bottom right. 
     * Upper-Left   - substitution
     * Bottom-Left  - insert
     * Upper-Right  - deletion
     * Bottom-Right - zero's
     */
    public double[][] createCostMatrix() {
        int n = g1.getNodes().size();
        int m = g2.getNodes().size();
        double[][] costMatrix = new double[n + m][n + m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                costMatrix[i][j] = getSubstituteCost(g1.getNode(i), g2.getNode(j));
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                costMatrix[i + n][j] = getInsertCost(i, j);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                costMatrix[j][i + m] = getDeleteCost(i, j);
            }
        }
       //printCostMatrix(costMatrix);
        return costMatrix;
    }

    //For testing purpose
    private void printCostMatrix(double[][] costMatrix) {
        System.out.println("costMatrix:");
        for (int i = 0; i < costMatrix.length; i++) {
            System.out.print("[ ");
            for (int j = 0; j < costMatrix[1].length; j++) {
                System.out.print("  " + costMatrix[i][j]);
            }
            System.out.println(" ]");
        }
    }

    //For testing purpose
    private void printCostMatrix2(int[][] costMatrix) {
        System.out.println("assignmentMatrix:");
        for (int i = 0; i < costMatrix.length; i++) {
            System.out.print("[ ");
            for (int j = 0; j < costMatrix[1].length; j++) {
                System.out.print("  " + costMatrix[i][j]);
            }
            System.out.println(" ]");
        }
    }

    private double getInsertCost(int i, int j) {
        if (i == j) {
            return INSERT_COST;
        }
        // return Double.MAX_VALUE;
        return MAXVALUE;
    }

    private double getDeleteCost(int i, int j) {
        if (i == j) {
            return DELETE_COST;
        }
        //return Double.MAX_VALUE;
        return MAXVALUE;
    }

    private double getSubstituteCost(Node node1, Node node2) {
        double diff = getRelabelCost(node1, node2) + getEdgeDiff(node1, node2);
       // System.out.println("substituteCost: " + diff * SUBSTITUTE_COST);
        return diff * SUBSTITUTE_COST;
    }

    private double getRelabelCost(Node node1, Node node2) {
        if (!node1.equals(node2)) {
            return SUBSTITUTE_COST;
        }
        return 0;
    }

    private double getEdgeDiff(Node node1, Node node2) {
        List<Edge> edges1 = g1.getEdges(node1);
        List<Edge> edges2 = g2.getEdges(node2);
        if (edges1.size() == 0 || edges2.size() == 0) {
            return Math.max(edges1.size(), edges2.size());
        }

        int n = edges1.size();
        int m = edges2.size();
        double[][] edgeCostMatrix = new double[n + m][m + n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                edgeCostMatrix[i][j] = getEdgeEditCost(edges1.get(i), edges2.get(j));
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                edgeCostMatrix[i + n][j] = getEdgeInsertCost(i, j);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                edgeCostMatrix[j][i + m] = getEdgeDeleteCost(i, j);
            }
        }

        //System.out.println("edgeCostMatrix");
        //printCostMatrix(edgeCostMatrix);
        int[][] assignment = HungarianAlgorithm.hgAlgorithm(edgeCostMatrix, "min");
        double sum = 0;
        for (int i = 0; i < assignment.length; i++) {
            sum += edgeCostMatrix[assignment[i][0]][assignment[i][1]];
        }
        return sum / ((n + m));
    }

    private double getEdgeInsertCost(int i, int j) {
        if (i == j) {
            return INSERT_COST;
        }
        //return Double.MAX_VALUE;
        return MAXVALUE;
    }

    private double getEdgeDeleteCost(int i, int j) {
        if (i == j) {
            return DELETE_COST;
        }
        //return Double.MAX_VALUE;
        return MAXVALUE;
    }

    private double getEdgeEditCost(Edge edge1, Edge edge2) {
        return edge1.equals(edge2) ? 0 : 1;
    }

}

package ged;


/**
 *
 * Anouchka Giberius
 */
public class Util {

    public static double[][] getDisSim(double[][] simMatrix) {
        double[][] matrix = new double[simMatrix.length][simMatrix.length];
        for (int i = 0; i < simMatrix.length; i++) {
            for (int j = 0; j < simMatrix.length; j++) {
                matrix[i][j] = 1 - simMatrix[i][j];
            }
        }
        return matrix;
    }

    public static int[] getSimDistribution(double[][] simMatrix) {
        int[] distribution = new int[10];
        double value;
        for (int i = 0; i < simMatrix.length; i++) {
            for (int j = 0; j < simMatrix.length; j++) {
                if (j > i) {
                    value = simMatrix[i][j];
                    if (value <= 0.1) {
                        distribution[0] = distribution[0] + 1;
                        continue;
                    }
                    if (value > 0.1 && value <= 0.2) {
                        distribution[1] = distribution[1] + 1;
                        continue;
                    }
                    if (value > 0.2 && value <= 0.3) {
                        distribution[2] = distribution[2] + 1;
                        continue;
                    }
                    if (value > 0.3 && value <= 0.4) {
                        distribution[3] = distribution[3] + 1;
                        continue;
                    }
                    if (value > 0.4 && value <= 0.5) {
                        distribution[4] = distribution[4] + 1;
                        continue;
                    }
                    if (value > 0.5 && value <= 0.6) {
                        distribution[5] = distribution[5] + 1;
                        continue;
                    }
                    if (value > 0.6 && value <= 0.7) {
                        distribution[6] = distribution[6] + 1;
                        continue;
                    }
                    if (value > 0.8 && value <= 0.9) {
                        distribution[8] = distribution[8] + 1;
                        continue;
                    }
                    if (value > 0.9 && value <= 1.0) {
                        distribution[9] = distribution[9] + 1;
                    }
                }
            }
        }
        return distribution;
    }

    public static void printSimDistribution(int[] distribution, String text) {
        System.out.println("");
        System.out.println("Similarity Distribution for " + text);
        for (int i = 0; i < 10; i++) {
            System.out.println("Range " + "0." + (i) + "-" + "0." + (i + 1) + ": " + distribution[i]);
        }
        System.out.println("");
    }

    public static double getAverageSimilarity(double[][] simMatrix) {
        double total = 0.0;
        int counter = 0;
        if (simMatrix.length == 1) {
            return 1.0;
        }
        for (int i = 0; i < simMatrix.length; i++) {
            for (int j = 0; j < simMatrix.length; j++) {
                if (j > i) {
                    counter++;
                    total = total + simMatrix[i][j];
                }
            }
        }
        return total / counter;
    }
    
//    public static void printAverageSimilarityClusters(List<Cluster> clusters) {
//         double[][] simMatrix;
//         int counter =0;
//        for (Cluster cluster : clusters) {
//            simMatrix = Gsimilarity.buildSimGedMatrix(cluster.getCases());
//            System.out.println("Cluster " + counter + ": " + String.format("%5.4f", getAverageSimilarity(simMatrix)));
//            counter++;
//        }
//    }
    
//    public static double getAverageSimilarityClusters(List<Cluster> clusters) {
//        double[][] simMatrix;
//        double average=0.0;
//        for (Cluster cluster : clusters ) {
//            simMatrix = Gsimilarity.buildSimGedMatrix(cluster.getCases());
//            average = average + getAverageSimilarity(simMatrix);
//        }
//        return average/clusters.size();
//    }

    public static void printSimMatrix(double[][] simMatrix) {
        for (int i = 0; i < simMatrix.length; i++) {
            System.out.print("[");
            for (int j = 0; j < simMatrix.length; j++) {
                System.out.printf("  %.4f", simMatrix[i][j]);
            }
            System.out.println(" ]");
        }
    }

    //Used for testing clustering
    public static double[][] getSimMatrix(int size) {
        double[][] simMatrix = new double[size][size];
        simMatrix[0][0] = 0;
        simMatrix[0][1] = 7;
        simMatrix[0][2] = 56;
        simMatrix[0][3] = 36;
        simMatrix[0][4] = 42;
        simMatrix[0][5] = 32;

        simMatrix[1][0] = 7;
        simMatrix[1][1] = 0;
        simMatrix[1][2] = 49;
        simMatrix[1][3] = 39;
        simMatrix[1][4] = 35;
        simMatrix[1][5] = 25;

        simMatrix[2][0] = 56;
        simMatrix[2][1] = 49;
        simMatrix[2][2] = 0;
        simMatrix[2][3] = 22;
        simMatrix[2][4] = 5;
        simMatrix[2][5] = 11;

        simMatrix[3][0] = 36;
        simMatrix[3][1] = 39;
        simMatrix[3][2] = 22;
        simMatrix[3][3] = 0;
        simMatrix[3][4] = 10;
        simMatrix[3][5] = 14;

        simMatrix[4][0] = 42;
        simMatrix[4][1] = 35;
        simMatrix[4][2] = 5;
        simMatrix[4][3] = 10;
        simMatrix[4][4] = 0;
        simMatrix[4][5] = 25;

        simMatrix[5][0] = 32;
        simMatrix[5][1] = 25;
        simMatrix[5][2] = 11;
        simMatrix[5][3] = 14;
        simMatrix[5][4] = 25;
        simMatrix[5][5] = 0;
        return simMatrix;
    }

}

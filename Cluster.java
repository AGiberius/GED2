
package ged;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 *
 * Anouchka Giberius
 */
public class Cluster {

    //A Cluster is a list of cases.
    private final List<Case> cases;

    
    /*
    
    The algoritm starts with a distance matrix
                 A  B  C  D  E  F
             A   0  7 56 36 42 32 
             B      0 49 39 35 25
             C         0 22  5 11 
             D            0 10 14
             E               0 25
             F                  0
    
    Each point is a cluster and a cluster contains a list of cases.
    
    List clusters = A,B,C,D,E,F
    
    Result after round 1
            A  B  D  F CE
        A   0  7 36 32 56 
        B      0 39 25 49
        D         0 14 22
        F            0 25
       CE               0
    
    List clusters = A,B,D,F,CE and cluster CE contains cases C and E
    
    The algoritymn:
    
    1) Search two clusters with the minimum value, this is C and E, minimum distance is 5
    2) Merge the clusters C and E and update the list: A B D E CE, cluster CE will containt case C and E
    3) Update the distance matrix:
        * All existing values are not changed
        * Recalculate the distance for each cluster to the new merged cluster CE
        - max(CE, A) = max(56,42)=56
        - max(CE, B) = max(49,35)=35 
        - max(CE, D) = max(22,10)=22
        - max(CE, F) = max(25,11)=25
    
    In each round the similarity matrix will shrink with one row and one kolom
    In esch round the list cases with shring with 1
    The process stops when the given number of clusters are reached.
    
    */
    
    
    
    public Cluster() {
        cases = new ArrayList<>();
    }

    public List<Case> getCases() {
        return cases;
    }

    public void addCase(Case c) {
        cases.add(c);
    }

    //A function to print the list of clusters, for each clusters the list 
    //of cases will be printed, each case has an outcome S for succes
    //or F for failure.
    public static void printCasesInClusterList(List<Cluster> clusters) {
        int counter =0;
        System.out.println("");
        String outcome = "";
        for (Cluster cluster : clusters ) {
            System.out.print("Cluster " + counter + ":");
            counter++;
            for (Case c : cluster.getCases()) {
                if (c.getOutCome() == OutCome.SUCCES) {
                    outcome = "(S)";
                } else {
                    outcome = "(F)";
                }
                System.out.print(" : " + c.getStringRepresentatie() + outcome);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    //Each cluster in the resulting list of Clusters has the cases and
    //each case has an outcome S of F. From this a succesFailure matrix
    //can be calcualted.
    public static int[][] buildSuccesFailureMatrix(List<Cluster> clusters) {
        Integer succes = 0;
        Integer failure = 0;
        Integer counter = 0;
        int[][] matrix = new int[clusters.size()][2];
        for (Cluster cluster : clusters) {
            for (Case c : cluster.getCases()) {
                if (c.getOutCome() == OutCome.SUCCES) {
                    succes++;
                } else {
                    failure++;
                }
            }
            matrix[counter][0] = succes;
            matrix[counter][1] = failure;
            counter++;
            //We sum succes, failure for each cluster, so we reset when we get a new cluster.
            succes = 0; 
            failure = 0;
        }
        return matrix;
    }

    //This will print the successFailureMatrix
    public static void printSuccesFailureMatrix(int[][] matrix) {
        Integer succes, failure, som, totalSucces = 0, totalFailure = 0, total = 0;
        Formatter fm = new Formatter();

        System.out.println("Succes/Failure Matrix");
        System.out.println("Cluster x    S    F    T      %S       %F  Average Sim");
        System.out.println("=========  ===  ===  ===  ======   ======  ===========");
        for (int i = 0; i < matrix.length; i++) {
            succes = matrix[i][0];
            failure = matrix[i][1];
            som = succes + failure;
            totalSucces = totalSucces + succes;
            totalFailure = totalFailure + failure;
            total = totalSucces + totalFailure;
            System.out.println("Cluster "
                    + String.format("%2s", i) + ": "
                    + String.format("%3s", succes) + "  "
                    + String.format("%3s", failure) + " "
                    + String.format("%3s", som) + "  "
                    + String.format("%6.2f", succes.doubleValue() / som.doubleValue() * 100) + "   "
                    + String.format("%6.2f", failure.doubleValue() / som.doubleValue() * 100)
            );
        }
        System.out.println("");
        System.out.println("Total cases         : " + total);
        System.out.println("Total Succes        : " + totalSucces);
        System.out.println("Total Failure       : " + totalFailure);
        System.out.println("Percentage Succes   : " + String.format("%2.2f", totalSucces.doubleValue() / total.doubleValue() * 100));
        System.out.println("Percentage Failure  : " + String.format("%2.2f", totalFailure.doubleValue() / total.doubleValue() * 100));

    }

    //This is the main routine, it gets a list of cases, a similarity matrix
    //The description is at the beginning of this class
    public static List<Cluster> getClusters(List<Case> cases, double[][] simMatrix, int numberOfClusters, Linkage linkage) {
        //Create a list of clusters to start with - the similarity matrix is a parameter
        List<Cluster> clusters = new ArrayList<>();
        for (Case c : cases) {
            Cluster cluster = new Cluster();
            cluster.addCase(c);
            clusters.add(cluster);
        }
        //Loop until the number of clusters is reached
        while (clusters.size() > numberOfClusters) {
            //Find the two clusters with the closest distance.
            List<Cluster> closestClusters = Cluster.getClosedClusters(simMatrix, clusters);
            //Update the distance matrix
            simMatrix = Cluster.updateDistanceMatrix(simMatrix, clusters, closestClusters, linkage);
            //Update the list with clusters.
            clusters = Cluster.updateClusters(clusters, closestClusters);
        }
        return clusters;
    }

    //In each round the distance matrix will be updated
    //This is difficult to understand - in the design there is a example with all the steps
    //You need to write out all the steps to understand the logic in this function
    private static double[][] updateDistanceMatrix(double[][] simMatrix, List<Cluster> clusters, List<Cluster> closestClusters, Linkage linkage) {
        //This is a new matrix which we will return - the size is 1 row and 1 column smaller
        double[][] matrix = new double[simMatrix.length - 1][simMatrix.length - 1];
        //Look for the columns and row which will be merged
        int index_1 = clusters.indexOf(closestClusters.get(0));
        int index_2 = clusters.indexOf(closestClusters.get(1));
        double distance;
        double value;
        
        for (int i = 0; i < simMatrix.length; i++) {
            //If linkage is "max" then we will take for each row the maximum value of the two columns
            if (linkage == Linkage.MAX) {
                distance = simMatrix[i][index_1] > simMatrix[i][index_2] ? simMatrix[i][index_1] : simMatrix[i][index_2];
            } else {
                distance = simMatrix[i][index_1] > simMatrix[i][index_2] ? simMatrix[i][index_2] : simMatrix[i][index_1];
            }
            
            //each area has different operations
            //for i and j we have the following
            // - Area before the first row/column has been reached
            // - Area between the first row/column
            // - Area after the second row/column

            //We need to go throug the matrix
            for (int j = 0; j < simMatrix.length; j++) {

                //i is vertical - this is the area untill the first row
                if (i < index_1) {
                    
                    //this is the area untill the first column
                    if (j < index_1) {
                        matrix[i][j] = simMatrix[i][j];
                    }
                    //this is the area between first and second colunn
                    if (j > index_1 && j < index_2) {
                        matrix[i][j - 1] = simMatrix[i][j];
                    }
                    //this is the area after the second column
                    if (j > index_2) {
                        matrix[i][j - 2] = simMatrix[i][j];
                    }
                    if (j == index_2) {
                        matrix[i][matrix.length - 1] = distance;
                    }
                }
                
                //i is vertical, this is the area between the first and second row
                if (i > index_1 && i < index_2) {
                    
                     //this is the area untill the first column
                    if (j < index_1) {
                        matrix[i - 1][j] = simMatrix[i][j];
                    }
                    if (j > index_1 && j < index_2) {
                        matrix[i - 1][j - 1] = simMatrix[i][j];
                    }
                    //this is the area between first and second colunn
                    if (j > index_2) {
                        matrix[i - 1][j - 2] = simMatrix[i][j];
                    }
                    //this is the area after the second column
                    if (j == index_2) {
                        matrix[i - 1][matrix.length - 1] = distance;
                    }
                }
                
                
                //i is vertical, this is the area after the second row
                if (i > index_2) {

                    //this is the area untill the first column
                    if (j < index_1) {
                        matrix[i - 2][j] = simMatrix[i][j];
                    }
                    //this is the area between first and second colunn
                    if (j > index_1 && j < index_2) {
                        matrix[i - 2][j - 1] = simMatrix[i][j];
                    }
                    //this is the area after the second column
                    if (j > index_2) {
                        matrix[i - 2][j - 2] = simMatrix[i][j];
                        matrix[i - 2][matrix.length - 1] = distance;
                    }
                }
            }
            
            //We only have the upper part of the matrix
            //The logic needs a full filled matrix, so now we fill the lower
            //part of the matrix, we do not touch the diagonal.
            for (int j = 0; j < matrix.length; j++) {
                matrix[matrix.length - 1][j] = matrix[j][matrix.length - 1];
            }

        }
        return matrix;
    }
  
    //This will upate the list of clusters
    private static List<Cluster> updateClusters(List<Cluster> clusters, List<Cluster> closestClusters) {
        ///First merged the two closest Clusters into one new cluster
        Cluster merged = new Cluster();
        merged = Cluster.mergeCluster(closestClusters.get(0), closestClusters.get(1));
        int index_1 = clusters.indexOf(closestClusters.get(0));
        int index_2 = clusters.indexOf(closestClusters.get(1));
        //remove the closest cluster from the clusters
        clusters.remove(index_1);
        clusters.remove(index_2 - 1);
        //add the merged cluster at the end.
        clusters.add(merged);
        return clusters;
    }

    private static List<Cluster> getClosedClusters(double[][] simMatrix, List<Cluster> clustersIn) {
        //This function search for the minimum value in the upper part of the simimlarity matrix.
        //It returns the two clusters related to this mininum
        List<Cluster> clustersOut = new ArrayList<>();
        double min = Double.MAX_VALUE;
        int pos_i = 0, pos_j = 0;
        for (int i = 0; i < simMatrix.length; i++) {
            for (int j = 0; j < simMatrix.length; j++) {
                if (j > i) {
                    double value = simMatrix[i][j];
                    if (value < min) {
                        min = value;
                        pos_i = i;
                        pos_j = j;
                    }
                }
            }
        }

        clustersOut.add(clustersIn.get(pos_i));
        clustersOut.add(clustersIn.get(pos_j));
        return clustersOut;
    }

    //Merge two clusters into new one 
    private static Cluster mergeCluster(Cluster cluster1, Cluster cluster2) {
        Cluster cluster = new Cluster();
        //Merging two clusters int one new clusters is about addding
        //the cases of each cluster into the new cluster.
        for (Case c : cluster1.getCases()) {
            cluster.addCase(c);
        }
        for (Case c : cluster2.getCases()) {
            cluster.addCase(c);
        }
        return cluster;
    }

    @Override
    public String toString() {
        String str = "{Cluster - cases: ";
        for (Case c : cases) {
            str = str + c.getId() + ", ";
        }
        str = str + "}";
        return str;
    }

}

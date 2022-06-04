
package ged;
/**
 *
 * Anouchka Giberius
 * Change History:
 * 01-05-2022: first build
 * 
 * This procedure is found in a GitHub repository on the internet
 * It is a public API with all kind of string operations and Levenhstein 
 * was one of those functions.
 * I have tested this procedure and it works fine.
 * It is based on dynamic programming, an explaining can be found on the internet.
 * The complexity O(n*m) is lineair 
 * 
 */

import static ged.GED.formatter;
import static ged.Util.getAverageSimilarity;
import java.time.Instant;
import java.util.*;

public class Levenshtein {

    public static Double sim(String str1, String str2) {
        //Decides on the length of the longest string
        Integer max;
        max = str1.length();
        if (max < str2.length()) {
            max = str2.length();
        }

        //Compute the distance between the strings
        Integer distance = dist(str1, str2);

        //Calculate the similarity
        Double sim = 1.0 - distance.doubleValue() / max.doubleValue();

        return sim;
    }
    
    public static double[][] buildSimLevMatrix(List<Case> cases) {
        double[][] simLevMatrix = new double[cases.size()][cases.size()];
        double sim;
        for (int i = 0; i < cases.size(); i++) {
            for (int j = 0; j < cases.size(); j++) {
                if (j > i) {
                    sim = Levenshtein.sim(cases.get(i).getStringRepresentatie(), cases.get(j).getStringRepresentatie());
                    simLevMatrix[i][j] = sim;
                    simLevMatrix[j][i] = sim;
                    
                }
            }
        }
        return simLevMatrix;
    }
    
    public static void printAverageSimilarityClusters(List<Cluster> clusters) {
         double[][] simMatrix;
         int counter =0;
        for (Cluster cluster : clusters) {
            simMatrix = buildSimLevMatrix(cluster.getCases());
            System.out.println("Cluster " + counter + ": " + String.format("%5.4f", getAverageSimilarity(simMatrix)));
            counter++;
        }
    }
    
     public static double getAverageSimilarityClusters(List<Cluster> clusters) {
        double[][] simMatrix;
        double average=0.0;
        for (Cluster cluster : clusters ) {
            simMatrix = buildSimLevMatrix(cluster.getCases());
            average = average + getAverageSimilarity(simMatrix);
        }
        return average/clusters.size();
    }

    public static int dist(String str1, String str2) {

        // A 2-D matrix to store previously calculated
        // answers of subproblems in order
        // to obtain the final
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            for (int j = 0; j <= str2.length(); j++) {

                // If str1 is empty, all characters of
                // str2 are inserted into str1, which is of
                // the only possible method of conversion
                // with minimum operations.
                if (i == 0) {
                    dp[i][j] = j;
                   
                } // If str2 is empty, all characters of str1
                // are removed, which is the only possible
                //  method of conversion with minimum
                //  operations.
                else if (j == 0) {
                    dp[i][j] = i;
                   
                } else {
                    // find the minimum of operations-
                    dp[i][j] = minm_edits(dp[i - 1][j - 1]
                            + NumOfReplacement(str1.charAt(i - 1), str2.charAt(j - 1)), // replace
                            dp[i - 1][j] + 1, // delete
                            dp[i][j - 1] + 1); // insert
                }
            }
             // printDP(dp); //For testing, you can investigate the build up of the matrix.
        }

        return dp[str1.length()][str2.length()];
    }

    //this procedure prints the matrix - we used this for testing
    private static void printDP(int[][] dp) {
        for (int i = 0; i < dp.length; i++) {
            String rij = "[ ";
            for (int j = 0; j < dp[i].length; j++) {
                rij = rij + dp[i][j] + " ";
            }
            System.out.println(rij + "]");
        }
        System.out.println("");
    }

    // check for distinct characters
    private static int NumOfReplacement(char c1, char c2) {
        return c1 == c2 ? 0 : 1;
    }

    // receives the count of different
    // operations performed and returns the
    // minimum value among them.
    private static int minm_edits(int... nums) {

        return Arrays.stream(nums).min().orElse(
                Integer.MAX_VALUE);
    }

}

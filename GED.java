package ged;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * Anouchka Giberius
 */
public class GED {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println(formatter.format(Instant.now()) + " Start program");
        
        //Toegevoegd aan Git
      

        //Variables you can change
        String file = "c:/DATA/Anouchka/Import/ImportTest22.txt";
        String outDir ="c:/Data/Anouchka/Export";
        int numberOfClusters = 4; //Number of Clusters created during clustering
        Boolean procesGED = true;
        Boolean procesLev = false;
        Boolean procesStruc = false;
        Linkage linkage = Linkage.MAX; //MIN does not work well, all data tends to go into one cluster
        Boolean exportClusters = true;
        Boolean splitSuccesFailure = false;

        List<Cluster> clusters;
        int[][] succesFailureMatrix;

        //Read the import file with the cases
        List<Case> cases;
        cases = ImpExpCases.importFile(file);
        if (cases == null) {
            System.out.println("No cases found, exit main");
            System.exit(0);
        }

        if (procesGED) {
            //Build the similarity Matrix of all cases with the Graph Edit Distance
            System.out.println("");
            System.out.println("Create similar and disimilar matrixes for GED");
            double[][] simGedMatrix = Gsimilarity.buildSimGedMatrix(cases);
            //Covnert similarity to dissimilary matrix.
            double[][] disGedMatrix = Util.getDisSim(simGedMatrix);
            //Calculate and print the similarity distribution
            int[] simDistribution = Util.getSimDistribution(simGedMatrix);
            Util.printSimDistribution(simDistribution, "Graph Edit Distance");
            System.out.println("Average simimlarity based on Graph Edit Distance: " + String.format("%5.4f", Util.getAverageSimilarity(simGedMatrix)));

            System.out.println("");
            System.out.println("Build Clusters based on GraphEdit Distance and print the Succces/Failure Matrix. #clusters=" + numberOfClusters);
            clusters = Cluster.getClusters(cases, disGedMatrix, numberOfClusters, linkage);
            succesFailureMatrix = Cluster.buildSuccesFailureMatrix(clusters);
            Cluster.printSuccesFailureMatrix(succesFailureMatrix);

            System.out.println("");
            System.out.println("Average Similirity of all clusters GED: " + String.format("%5.4f", Gsimilarity.getAverageSimilarityClusters(clusters)));
            System.out.println("");

            System.out.println("");
            System.out.println("Avereverage Similarity of clusters GED");
            Gsimilarity.printAverageSimilarityClusters(clusters);
            
            if (exportClusters) {
                ImpExpCases.exportClusters(clusters, outDir, file, "GED",splitSuccesFailure);
            }
            
        }

        if (procesLev) {
            System.out.println("");
            System.out.println("Create similar and disimilar matrixes for Levenhstein");
            //Build the similarity Matrix of all cases with the Levenhstein Distance
            double[][] simLevMatrix = Levenshtein.buildSimLevMatrix(cases);
            //Convert similarity to dissimilary matrix.
            double[][] disLevMatrix = Util.getDisSim(simLevMatrix);
            int[] simDistribution = Util.getSimDistribution(simLevMatrix);

            Util.printSimDistribution(simDistribution, "Levenhstein");
            System.out.println("Average simimlarity based on Levenhstein: " + String.format("%5.4f", Util.getAverageSimilarity(simLevMatrix)));

            //Build the custers and print them - Levenhstein
            System.out.println("");
            System.out.println("Build Clusters based on Levenhstein Distance and print the Succces/Failure Matrix. #clusters=" + numberOfClusters);
            clusters = Cluster.getClusters(cases, disLevMatrix, numberOfClusters, linkage);
            succesFailureMatrix = Cluster.buildSuccesFailureMatrix(clusters);
            Cluster.printSuccesFailureMatrix(succesFailureMatrix);

            System.out.println(""); System.out.println("");
            System.out.println("Average Similirity of all clusters Levenhstein: " + String.format("%5.4f", Levenshtein.getAverageSimilarityClusters(clusters)));
            System.out.println("");

            System.out.println("");
            System.out.println("Avereverage Similarity of clusters Levenhstein:");
            Levenshtein.printAverageSimilarityClusters(clusters);
            
            if (exportClusters) {
                ImpExpCases.exportClusters(clusters, outDir, file, "LEV",splitSuccesFailure);
            }
        }

        if (procesStruc) {
            System.out.println("");
            System.out.println("Create similar and disimilar matrixes for Graph Structure");
            //Build the similarity Matrix of all cases based on the structure of the Graph
            double[][] simStrucMatrix = StructureSimilarity.buildSimStrucMatrix(cases);
            //Convert similarity to dissimilary matrix.
            double[][] disStrucMatrix = Util.getDisSim(simStrucMatrix);

            int[] simDistribution = Util.getSimDistribution(simStrucMatrix);
            Util.printSimDistribution(simDistribution, "Graph Structure");
            System.out.println("Average similarity based on the Graph Structure: " + String.format("%5.4f", Util.getAverageSimilarity(simStrucMatrix)));

            System.out.println("");
            System.out.println("Build Clusters based on the Graph Structure and print the Succces/Failure Matrix. #clusters=" + numberOfClusters);
            clusters = Cluster.getClusters(cases, disStrucMatrix, numberOfClusters, linkage);
            succesFailureMatrix = Cluster.buildSuccesFailureMatrix(clusters);
            Cluster.printSuccesFailureMatrix(succesFailureMatrix);

            System.out.println("");
            System.out.println("Average Similirity of clusters Grahp Structure: " + String.format("%5.4f", StructureSimilarity.getAverageSimilarityClusters(clusters)));
            System.out.println("");

            System.out.println("");
            System.out.println("Avereverage Similarity of clusters Graph Structure");
            StructureSimilarity.printAverageSimilarityClusters(clusters);
            
            if (exportClusters) {
                ImpExpCases.exportClusters(clusters, outDir, file, "STR",splitSuccesFailure);
            }
        }

        System.out.println("");
        System.out.println(formatter.format(Instant.now()) + " End program");
    }

}

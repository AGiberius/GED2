package ged;

import static ged.GED.formatter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Anouchka Giberius
 */
public class ImpExpCases {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss").withZone(ZoneId.systemDefault());

    private final static Node a = new Node("A");
    private final static Node b = new Node("B");
    private final static Node c = new Node("C");
    private final static Node d = new Node("D");
    private final static Node e = new Node("E");
    private final static Node f = new Node("F");
    private final static Node g = new Node("G");
    private final static Node h = new Node("H");
    private final static Node i = new Node("I");
    private final static Node j = new Node("J");
    private final static Node k = new Node("K");
    private final static Node l = new Node("L");
    private final static Node m = new Node("M");
    private final static Node n = new Node("N");
    private final static Node o = new Node("O");
    private final static Node p = new Node("P");
    private final static Node q = new Node("Q");
    private final static Node r = new Node("R");
    private final static Node s = new Node("S");

    public static List<Case> importFile(String fileIn) {

        //From the input file we well crate a list of cases and return this this.
        //Each Case has a Graph and a String Represention.
        //This list of cases wil be used in all de calculations
        List<Case> cases = new ArrayList<>();
        Case currentCase = null;
        String caseId;
        String activityString;
        Date date;
        Date currentDate = null;
        String dateString;
        String succesString;
        OutCome outCome;
        SimpleDateFormat formatter1 = new SimpleDateFormat("MM/yyyy");
        String currentCaseId = "leeg";
        DirectedGraph graph = null;
        Node fromNode = null;
        Node toNode = null;
        Edge edge;
        String[] fields;
        int totalCases = 0;
        String parseString = "";

        System.out.println("");
        System.out.println(formatter.format(Instant.now()) + " Start reading import file");

        //This is the file location with the imput file
        //The structure of the input - on each line:
        //  CaseId,Activity,date,outcome
        File file = new File(fileIn);
        if (!file.exists()) {
            System.out.println("File " + file + " does not exitst");
            return null;
        }

        //Process all the lines in the input file
        try ( BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                //The fields on the line are comma seperated
                fields = line.split(",");
                caseId = fields[0];
                activityString = fields[1];
                dateString = fields[2];
                date = formatter1.parse(dateString);
                succesString = fields[3];
                if (succesString.equals("1")) {
                    outCome = OutCome.SUCCES;
                } else {
                    outCome = OutCome.FAILURE;
                }
                if (!caseId.equals(currentCaseId)) {
                    //Now we are on a new case - so we will create a new graph and a new case
                    graph = new DirectedGraph();
                    if (currentCase == null) {
                        //  System.out.println("Dit is de eeeeeeeeeeeeerste keer");
                    } else {
                        currentCase.setStringRepresentatie(currentCase.getStringRepresentatie() + parseString);
                    }
                    parseString = Activity.valueOfLabel(activityString).toString();
                    currentCase = new Case(caseId, outCome, graph);
                    fromNode = getNode(Activity.valueOfLabel(activityString));
                    graph.addNode(fromNode);
                    cases.add(currentCase);
                    currentCaseId = caseId;
                    currentDate = date;
                    totalCases++;
                } else {
                    //We are not on a new case, so we will update the Grap
                    toNode = getNode(Activity.valueOfLabel(activityString));
                    edge = new Edge(fromNode, toNode);
                    graph.addNode(toNode);
                    graph.addEdge(edge);
                    fromNode = toNode;
                    if (!date.equals(currentDate)) {
                        parseString = sorteer(parseString);
                        currentDate = date;
                        currentCase.setStringRepresentatie(currentCase.getStringRepresentatie() + parseString);
                        parseString = Activity.valueOfLabel(activityString).toString();
                    } else {
                        parseString = parseString + Activity.valueOfLabel(activityString);
                    }
                }
            }
            currentCase.setStringRepresentatie(currentCase.getStringRepresentatie() + parseString);
        } catch (IOException | ParseException z) {
            z.printStackTrace();
        }
        System.out.println(formatter.format(Instant.now()) + " Reading import file ready. Number of cases: " + totalCases);
        return cases;
    }

    public static void exportClusters(List<Cluster> clusters, String outDir, String file, String method, Boolean splitSuccsFailure) {
        List<Line> lines = new ArrayList<>();
        String[] fields;
        String caseId;
        String fileName;

        //Read the input file and create a list of the class Line
        try ( BufferedReader br = new BufferedReader(new FileReader(file))) {
            String lineString;
            while ((lineString = br.readLine()) != null) {
                //Line line = new Line();
                fields = lineString.split(",");
                caseId = fields[0];
                Line line = new Line(caseId, lineString);
                lines.add(line);
                //  System.out.println("line: " + line);
            }
        } catch (IOException z) {
            z.printStackTrace();
        }
        System.out.println("");
        System.out.println("Export clusters");
        int counter = 0;
        FileWriter fileOut = null;
        FileWriter fileOutSucces = null;
        FileWriter fileOutFailure = null;

        String date = formatter.format(Instant.now());
        for (Cluster cluster : clusters) {

            try {
                Line line;
                if (splitSuccsFailure) {
                    fileName = outDir + "/" + method + "_ " + date + "_Cluster_" + counter + "_Succes" + ".txt";
                    fileOutSucces = new FileWriter(fileName);
                    fileName = outDir + "/" + method + "_ " + date + "_Cluster_" + counter + "_Failure" + ".txt";
                    fileOutFailure = new FileWriter(fileName);
                } else {
                    fileName = outDir + "/" + method + "_ " + date + "_Cluster_" + counter + ".txt";
                    fileOut = new FileWriter(fileName);
                }
                counter++;

                //System.out.println("Write cluster " + counter + " to file: " + fileName);
                for (Case c : cluster.getCases()) {
                    //  System.out.println("Case: " + c.getId() + ", " + c.getOutCome() + ", " + c.getStringRepresentatie());
                    line = new Line(c.getId(), "xx");
                    int x = lines.indexOf(line);
                    // System.out.println("index eerste line: " + x + ", " + lines.get(x));
                    String currentCaseId = lines.get(x).id;
                    String nextCaseId = null;
                    Boolean gadoor = true;
                    while (gadoor) {
                        //   System.out.println("Line naar file: " + lines.get(x) + ", currentCaseId: " + currentCaseId + ", ");

                        if (splitSuccsFailure) {
                            if (c.getOutCome().equals(OutCome.SUCCES)) {
                                fileOutSucces.write(lines.get(x).line + System.getProperty("line.separator"));
                            } else {
                                fileOutFailure.write(lines.get(x).line + System.getProperty("line.separator"));
                            }

                        } else {
                            fileOut.write(lines.get(x).line + System.getProperty("line.separator"));
                        }

                        x++;
                        if (x < lines.size()) {
                            nextCaseId = lines.get(x).id;
                        } else {
                            gadoor = false;
                        }

                        if (!nextCaseId.equals(currentCaseId)) {
                            gadoor = false;
                        }
                    }

                }
                if (splitSuccsFailure) {
                    fileOutSucces.close();
                    fileOutFailure.close();

                } else {
                    fileOut.close();
                }

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        }
    }

    private static String sorteer(String str) {
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        String sorted = new String(chars);
        return sorted;

    }

    private static class Line {

        protected String id;
        protected String line;

        public Line(String id, String line) {
            this.id = id;
            this.line = line;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Line other = (Line) obj;
            return Objects.equals(this.id, other.id);
        }

        @Override
        public String toString() {
            return "Line{" + "id=" + id + ", line=" + line + '}';
        }
    }

    private static Node getNode(Activity activity) {
        switch (activity) {
            case A:
                return a;
            case B:
                return b;
            case C:
                return c;
            case D:
                return d;
            case E:
                return e;
            case F:
                return f;
            case G:
                return g;
            case H:
                return h;
            case I:
                return i;
            case J:
                return j;
            case K:
                return k;
            case L:
                return l;
            case M:
                return m;
            case N:
                return n;
            case O:
                return o;
            case P:
                return p;
            case Q:
                return q;
            case R:
                return r;
            case S:
                return s;
            default:
                return null;
        }
    }
}

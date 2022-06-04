package ged;

import java.util.Objects;

/**
 *
 * Anouchka Giberius
 */
public class Case {
    
   //Each case has a string, outcome, Graph and StringRepesentation.
   //The Graph will be used in de Graph Edit Distance and similarity based
   //on the structure of the Grap.
   //The Stringrepresention will be used in the Levenhstein algoritm
   //During reading the import file we will crate a list of cases. 
    
   private final String  id;
   //private final Boolean outcome; //True is succes en False is failure
   private OutCome outCome;
   private DirectedGraph graph;
   private String stringRepresentatie; //E.g ADCE

    public Case(String id, OutCome outCome) {
        this.id = id;
        this.outCome = outCome;
    }

    public Case(String id, OutCome outCome, DirectedGraph graph) {
        this.id = id;
        this.outCome = outCome;
        this.graph = graph;
        stringRepresentatie = "";
    }

    public String getId() {
        return id;
    }

    public OutCome getOutCome() {
        return outCome;
    }

   

    public DirectedGraph getGraph() {
        return graph;
    }

    public void setGraph(DirectedGraph graph) {
        this.graph = graph;
    }

    public String getStringRepresentatie() {
        return stringRepresentatie;
    }

    public void setStringRepresentatie(String stringRepresentatie) {
        this.stringRepresentatie = stringRepresentatie;
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
        final Case other = (Case) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Case{" + "id=" + id + ", outcome=" + outCome + ", graph=" + graph + '}';
    }
    
}

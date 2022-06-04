package ged;

/**
 *
 * Anouchka SGiberius
 */
public class Node {
    
    private String label;

    public Node(String label) {
        this.label = label;
      
    }

    public String  getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public  boolean equals(Object obj) {
        if (getClass() != obj.getClass()) return false ;
        Node other = (Node) obj;
        return label.equals(other.getLabel());

    }
    @Override
    public int hashCode() {
        return label.hashCode();
    }
    
}

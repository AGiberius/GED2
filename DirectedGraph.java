package ged;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Anouchka Giberius
 */
public class DirectedGraph {

    private final List<Node> nodes;
    private final HashMap<String, List<Edge>> edges;
    
    public DirectedGraph() {
        nodes = new ArrayList<>();
        edges = new HashMap<>();
    }

    public void addNode(Node node) {
        if (!edges.containsKey(node.getLabel())) {
            edges.put(node.getLabel(), new ArrayList<>());
        }
        nodes.add(node);
    }

    public int getSize() {
        return nodes.size();
    }

    public HashMap<String, List<Edge>> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        edges.get(edge.getFrom().getLabel()).add(edge);
    }

    List<Edge> getEdges(Node node) {
        return getEdges(node.getLabel());
    }

    private List<Edge> getEdges(String nodeId) {
        return edges.get(nodeId);
    }

    public Node getNode(int i) {
        return nodes.get(i);
    }

    public List<Node> getNodes() {
        return nodes;
    }
   
    @Override
    public String toString() {
        return "DirectedGraph{" + "nodes=" + nodes + ", edges=" + edges.toString() + '}';
    }

}

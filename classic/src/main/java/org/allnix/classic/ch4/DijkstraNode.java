package org.allnix.classic.ch4;

public class DijkstraNode implements Comparable<DijkstraNode> {
    public final int vertexIndex;
    /**
     * Distance from start to this vertex
     */
    public final double distance;
    
    public DijkstraNode(int vertex, double distance) {
        super();
        this.vertexIndex = vertex;
        this.distance = distance;
    }

    @Override
    public int compareTo(DijkstraNode other) {
        return Double.compare(this.distance, other.distance);
    }

}

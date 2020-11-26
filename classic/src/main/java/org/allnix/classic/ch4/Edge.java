package org.allnix.classic.ch4;

public class Edge {
    public final int u; // from vertex
    public final int v; // to vertex
    public Edge(int u, int v) {
        super();
        this.u = u;
        this.v = v;
    }
    
    public Edge reversed() {
        return new Edge(v,u);
    }
    
    public String toString() {
        return String.format("%d -> %d", u, v);
    }
}

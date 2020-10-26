package org.allnix.simple.ch4;

public class WeightedEdge extends Edge implements Comparable<WeightedEdge> {
    public final double weight;

    public WeightedEdge(int u, int v, double weight) {
        super(u, v);
        this.weight = weight;
    }

    @Override
    public int compareTo(WeightedEdge o) {
        return Double.compare(weight, o.weight);
    }

    @Override
    public WeightedEdge reversed() {
        return new WeightedEdge(v,u,weight);
    }

    @Override
    public String toString() {
        return String.format("%d %g > %d", u, weight, v);
    }
    
    
}

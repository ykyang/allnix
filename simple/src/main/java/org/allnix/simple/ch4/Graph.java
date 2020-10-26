package org.allnix.simple.ch4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author ykyang@gmail.com
 *
 * @param <V> Vertex
 * @param <E> Edge
 */
public abstract class Graph<V, E extends Edge> {
    private ArrayList<V> vertices = new ArrayList<>();
    /**
     * Edge list for each vertex
     * edges.get(i) -> List of edges for vertex[i]
     */
    private ArrayList<ArrayList<E>> edges = new ArrayList<>();
    
    public Graph() {
        
    }
    
    public Graph(Collection<V> vertices) {
        this.vertices.addAll(vertices);
        for (V vertex : vertices) {
            edges.add(new ArrayList<>());
        }
    }
    /*
     * List of edges a vertex is connected to
     * 
     * @param ind Vertex index
     * @return List of edges
     */
//    protected List<E> getEdgeOfVertex(int ind) {
//        return edges.get(ind);
//    }
    
    public int getVertexCount() {
        return vertices.size();
    }
    
    public int getEdgeCount() {
        // duplication?
        int count = edges.stream().mapToInt(ArrayList::size).sum();
        return count;
    }
    
    /**
     * Add a vertex
     * 
     * @param vertex Vertex to be added
     * @return Vertex index
     */
    public int addVertex(V vertex) {
        vertices.add(vertex);
        edges.add(new ArrayList<>());
        
        return getVertexCount() - 1;
    }
    
    public V vertexAt(int index) {
        return vertices.get(index);
    }
    
    public int indexOf(V vertex) {
        return vertices.indexOf(vertex);
    }
    
    /**
     * Find the neighbor vertices of a vertex
     * 
     * @param index Vertex index
     * @return Neighbors of the vertex
     */
    public List<V> neighborOf(int index) {
        List<V> ans = edges.get(index).stream() // Stream<Edge>
        .map(edge -> vertexAt(edge.v)) // Stream<Vertex>
        .collect(Collectors.toList());
        
        return ans;
    }
    
    public List<V> neighborOf(V vertex) {
        int index = indexOf(vertex);
        return neighborOf(index);
    }
    
    public List<E> edgeOf(int index) {
        return edges.get(index);
    }
    
    public List<E> edgeOf(V vertex) {
        int index = indexOf(vertex);
        return edgeOf(index);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getVertexCount(); i++) {
            sb.append(vertexAt(i));
            sb.append(" -> ");
            sb.append(Arrays.toString(neighborOf(i).toArray()));
            sb.append(System.lineSeparator());
        }
        
        return sb.toString();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}

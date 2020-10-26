package org.allnix.simple.ch4;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import org.allnix.simple.ch2.Node;
import org.allnix.simple.ch2.Search;
/**
 * 
 * @author ykyang@gmail.com
 *
 * @param <V> Vertex type
 */
public class UnweightedGraph<V> extends Graph<V,Edge> {
    /**
     * ./gradlew -PmainClass=org.allnix.simple.ch4.UnweightedGraph runApp
     * gradlew.bat -PmainClass=org.allnix.simple.ch4.UnweightedGraph runApp
     * 
     * @param args
     */
    public static void main(String[] args) {
        PrintStream out = System.out;
        
        List<String> cities = List.of("Seattle", "San Francisco",  
                "Los Angeles", "Riverside", "Phoenix", "Chicago", "Boston",
                "New York", "Atlanta", "Miami", "Dallas", "Houston", "Detroit",
                "Philadelphia", "Washington"
                );
        
        UnweightedGraph<String> cityGraph = new UnweightedGraph<>(cities);
        
        cityGraph.addEdge("Seattle", "Chicago").addEdge("Seattle","San Francisco")
        .addEdge("San Francisco", "Riverside").addEdge("San Francisco", "Los Angeles")
        .addEdge("Los Angeles", "Riverside").addEdge("Los Angeles", "Phoenix")
        .addEdge("Riverside", "Phoenix").addEdge("Riverside", "Chicago")
        .addEdge("Phoenix", "Houston").addEdge("Dallas", "Chicago")
        .addEdge("Dallas", "Atlanta").addEdge("Dallas", "Houston")
        .addEdge("Houston", "Atlanta").addEdge("Houston", "Miami")
        .addEdge("Atlanta", "Chicago").addEdge("Atlanta", "Washington")
        .addEdge("Atlanta", "Miami").addEdge("Miami", "Washington")
        .addEdge("Chicago", "Detroit").addEdge("Detroit", "Boston")
        .addEdge("Detroit", "Washington").addEdge("Detroit", "Washington")
        .addEdge("Detroit", "New York").addEdge("Boston", "New York")
        .addEdge("New York", "Philadelphia").addEdge("Philadelphia", "Washington");
        
        out.println(cityGraph.toString());
        
        Node<String> bfsAns = Search.bfs("Boston", v -> v.equals("Miami"), cityGraph::neighborOf);
        if (bfsAns == null) {
            out.println("No solution found using breadth-first search!");
        } else {
            Collection<String> path = Search.nodeToPath(bfsAns);
            out.println("Path from Boston to Miami:");
            out.println(path);
        }
    }
    
    public UnweightedGraph(List<V> vertices) {
        super(vertices);
    }
    
    /**
     * Add an edge to the graph
     * 
     * non-directional so add edges to both ends of vertices
     * 
     * @param edge
     */
    public UnweightedGraph<V> addEdge(Edge edge) {
        edgeOf(edge.u).add(edge);
        //edges.get(edge.u).add(edge);
        edgeOf(edge.v).add(edge.reversed());
        //edges.get(edge.v).add(edge.reversed());
        
        return this;
    }
    
    public UnweightedGraph<V> addEdge(int u, int v) {
        addEdge(new Edge(u,v));
        
        return this;
    }
    
    public UnweightedGraph<V> addEdge(V left, V right) {
//        System.out.println(left + ", " + right);
        
        Edge edge = new Edge(indexOf(left), indexOf(right));
        addEdge(edge);
        
        return this;
    }
}

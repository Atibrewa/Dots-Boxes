import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Graphics.GraphicsObject;

/**
 * A class that implements the Graph data structure using Hash Maps and Lists
 * 
 * @author Atibrewa
 * @author ae-bii
 * @author lescobos
 */
class Graph<T> {

    private Map<T, List<T> > map = new HashMap<>();

    /**
     * Add a vertex to the graph
     * 
     * @param s vertex added
     */
    public void addVertex(T s)
    {
        map.put(s, new LinkedList<T>());
    }

    /**
     * Add a link/edge between two vertices
     * 
     * @param source
     * @param destination
     */
    public void addEdge(T source,
                        T destination)
    {

        if (map.containsKey(source) && map.containsKey(destination)) {
            map.get(source).add(destination);
            map.get(destination).add(source);
        }
        
        
    }

    /**
     * Prints the number of vertices in the graph
     */
    public void getVertexCount()
    {
        System.out.println("The graph has "
                           + map.keySet().size()
                           + " vertex");
    }

    /**
     * Prints whether a vertex is present or not in the graph
     * 
     * @param s vertex to check
     */
    public void hasVertex(T s)
    {
        if (map.containsKey(s)) {
            System.out.println("The graph contains "
                               + s + " as a vertex.");
        }
        else {
            System.out.println("The graph does not contain "
                               + s + " as a vertex.");
        }
    }

    /**
     * Checks whether an edge is present or not between two specified vertices
     * 
     * @param s source vertex
     * @param d  destination vertex
     * @return true or false depending on if the edge exists or not
     */
    public boolean hasEdge(GraphicsObject s, GraphicsObject d)
    {
        if (map.get(s).contains(d)) {
            return true;
        }

        return false;
    }

    /**
     * Returns the graph and all its vertices and edges
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for (T v : map.keySet()) {
            builder.append(v.toString() + ": ");
            for (T w : map.get(v)) {
                builder.append(w.toString() + " ");
            }
            builder.append("\n");
        }

        return (builder.toString());
    }

    /**
     * Returns the key set of the hash map
     * 
     * @return set
     */
    public Set<T> getKeySet() {
        return map.keySet();
    }
}
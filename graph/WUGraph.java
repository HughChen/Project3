/* WUGraph.java */

package graph;
import list.*;
import dict.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {

	DList list;
	HashTableChained hashVertex, hashEdge;
	int vertices;
	int edges;

	/**
	 * WUGraph() constructs a graph having no vertices or edges.
	 *
	 * Running time:  O(1).
	 */
	public WUGraph(){
		this.list = new DList();
		this.hashVertex = new HashTableChained();
		this.hashEdge = new HashTableChained();
		this.vertices = 0;
		this.edges = 0;
	}

	/**
	 * vertexCount() returns the number of vertices in the graph.
	 *
	 * Running time:  O(1).
	 */
	public int vertexCount(){
		return vertices;
	}           
	/**
	 * edgeCount() returns the number of edges in the graph.
	 *
	 * Running time:  O(1).
	 */
	public int edgeCount(){
		return edges;
	}

	/**
	 * getVertices() returns an array containing all the objects that serve
	 * as vertices of the graph.  The array's length is exactly equal to the
	 * number of vertices.  If the graph has no vertices, the array has length
	 * zero.
	 *
	 * (NOTE:  Do not return any internal data structure you use to represent
	 * vertices!  Return only the same objects that were provided by the
	 * calling application in calls to addVertex().)
	 *
	 * Running time:  O(|V|).
	 */
	public Object[] getVertices(){
		Object[] result = new Object[list.length()];
		DListNode pointer = (DListNode) list.front();
		int counter = 0;
		while(pointer.isValidNode()){
			try {
				result[counter] = ((Vertex) pointer.item()).object();
				pointer = (DListNode) pointer.next();
				counter++;
			} catch (InvalidNodeException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * addVertex() adds a vertex (with no incident edges) to the graph.  The
	 * vertex's "name" is the object provided as the parameter "vertex".
	 * If this object is already a vertex of the graph, the graph is unchanged.
	 *
	 * Running time:  O(1).
	 */
	public void addVertex(Object vertex){
		if(hashVertex.find(vertex) == null){
			Vertex v = new Vertex(vertex);
			list.insertBack(v);
			v.myNode = (DListNode) list.back();
			hashVertex.insert(vertex, v);
			vertices++;
		}
	}

	/**
	 * removeVertex() removes a vertex from the graph.  All edges incident on the
	 * deleted vertex are removed as well.  If the parameter "vertex" does not
	 * represent a vertex of the graph, the graph is unchanged.
	 *
	 * Running time:  O(d), where d is the degree of "vertex".
	 */
	public void removeVertex(Object vertex){
		Entry v = hashVertex.find(vertex);
		if(v != null){
			Vertex v1 = (Vertex) v.value();
			hashVertex.remove(vertex);
			DListNode pointer = (DListNode) v1.edges.front();
			while(pointer.isValidNode()) {  //remove all edges
				try {
					DListNode temp = (DListNode) pointer.next();
					Vertex v2 = ((Edge) pointer.item()).otherVertex(v1.object());
					this.removeEdge(v1.object(), v2.object());
					pointer = temp;
				} catch (InvalidNodeException e) {
					e.printStackTrace();
				}
			}
			v1.remove();
			vertices--;
		}
	}

	/**
	 * isVertex() returns true if the parameter "vertex" represents a vertex of
	 * the graph.
	 *
	 * Running time:  O(1).
	 */
	public boolean isVertex(Object vertex){
		if(hashVertex.find(vertex) != null){
			return true;
		} else{
			return false;
		}
	}

	/**
	 * degree() returns the degree of a vertex.  Self-edges add only one to the
	 * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
	 * of the graph, zero is returned.
	 *
	 * Running time:  O(1).
	 */
	public int degree(Object vertex){
		Entry e = hashVertex.find(vertex);
		if(e != null){
			return ((Vertex) e.value()).degree();
		}
		return 0;
	}

	/**
	 * getNeighbors() returns a new Neighbors object referencing two arrays.  The
	 * Neighbors.neighborList array contains each object that is connected to the
	 * input object by an edge.  The Neighbors.weightList array contains the
	 * weights of the corresponding edges.  The length of both arrays is equal to
	 * the number of edges incident on the input vertex.  If the vertex has
	 * degree zero, or if the parameter "vertex" does not represent a vertex of
	 * the graph, null is returned (instead of a Neighbors object).
	 *
	 * The returned Neighbors object, and the two arrays, are both newly created.
	 * No previously existing Neighbors object or array is changed.
	 *
	 * (NOTE:  In the neighborList array, do not return any internal data
	 * structure you use to represent vertices!  Return only the same objects
	 * that were provided by the calling application in calls to addVertex().)
	 *
	 * Running time:  O(d), where d is the degree of "vertex".
	 */
	public Neighbors getNeighbors(Object vertex){
		Entry entry = hashVertex.find(vertex); //find appropriate entry
		if(entry != null){  //if the vertex is in the graph
			Vertex v = (Vertex) entry.value();  
			if(v.degree() == 0){   //if there are no edges, return null
				return null;
			} else { //if there are edges
				try {
					DListNode d = (DListNode) v.edges.front();
					Object[] neighbors = new Object[v.degree()];
					int[] weights = new int[v.degree()];
					int i = 0;
					while(d.isValidNode()){
						neighbors[i] = ((Edge) d.item()).otherVertex(vertex).object();
						weights[i] = ((Edge) d.item()).weight;
						d = (DListNode) d.next();
						i++;
					}
					Neighbors result = new Neighbors();
					result.neighborList = neighbors;
					result.weightList = weights;
					return result;
				} catch (InvalidNodeException e1) {
					e1.printStackTrace();
				}
			}
		} else{
			return null;
		}
		return null;
	}

	/**
	 * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
	 * u and v does not represent a vertex of the graph, the graph is unchanged.
	 * The edge is assigned a weight of "weight".  If the edge is already
	 * contained in the graph, the weight is updated to reflect the new value.
	 * Self-edges (where u == v) are allowed.
	 *
	 * Running time:  O(1).
	 */
	public void addEdge(Object u, Object v, int weight){
		Entry v1 = hashVertex.find(u);
		Entry v2 = hashVertex.find(v);
		if(v1 != null && v2 != null){  //if they are both not null
			VertexPair key = new VertexPair(u, v);   
			if(hashEdge.find(key) != null){
				((Edge) hashEdge.find(key).value()).weight = weight;
			} else{
				Edge edge = new Edge(u, v, weight);
				hashEdge.insert(key, edge);
				if (u == v) {
					((Vertex) v1.value()).edges.insertBack(edge);
					edge.myNode1 = (DListNode) ((Vertex) v1.value()).edges.back();
					edge.myNode2 = (DListNode) ((Vertex) v1.value()).edges.back();
 					edges++;
				} else {
					((Vertex) v1.value()).edges.insertBack(edge);
					((Vertex) v2.value()).edges.insertBack(edge);
					edge.myNode1 = (DListNode) ((Vertex) v1.value()).edges.back();
					edge.myNode2 = (DListNode) ((Vertex) v2.value()).edges.back();
					edges++; 
				}
			}
		}
	}

	/**
	 * removeEdge() removes an edge (u, v) from the graph.  If either of the
	 * parameters u and v does not represent a vertex of the graph, the graph
	 * is unchanged.  If (u, v) is not an edge of the graph, the graph is
	 * unchanged.
	 *
	 * Running time:  O(1).
	 */
	public void removeEdge(Object u, Object v){
		VertexPair key = new VertexPair(u, v);
		Entry entry = hashEdge.find(key);
		if(entry != null){
			hashEdge.remove(key);
			((Edge) entry.value()).remove();
			edges--;
		}
	}

	/**
	 * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
	 * if (u, v) is not an edge (including the case where either of the
	 * parameters u and v does not represent a vertex of the graph).
	 *
	 * Running time:  O(1).
	 */
	public boolean isEdge(Object u, Object v){
		VertexPair key = new VertexPair(u, v);
		if(hashEdge.find(key) == null){
			return false;          
		} else{
			return true;          
		}
	}

	/**
	 * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
	 * an edge (including the case where either of the parameters u and v does
	 * not represent a vertex of the graph).
	 *
	 * (NOTE:  A well-behaved application should try to avoid calling this
	 * method for an edge that is not in the graph, and should certainly not
	 * treat the result as if it actually represents an edge with weight zero.
	 * However, some sort of default response is necessary for missing edges,
	 * so we return zero.  An exception would be more appropriate, but
	 * also more annoying.)
	 *
	 * Running time:  O(1).
	 */
	public int weight(Object u, Object v){
		VertexPair key = new VertexPair(u, v);
		Entry entry = hashEdge.find(key);
		if(entry != null){
			return ((Edge) entry.value()).weight();
		} else{
			return 0;
		}
	}
}

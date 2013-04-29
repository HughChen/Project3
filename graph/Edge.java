package graph;
import list.*;

class Edge{

    Vertex vertex1, vertex2;
    int weight;
    DListNode myNode1;
    DListNode myNode2;

    Edge(Object v1, Object v2, int w){
        this.vertex1 = new Vertex(v1);
        this.vertex2 = new Vertex(v2);
        this.weight = w;
    }

    void remove(){
        this.vertex1 = null;
        this.vertex2 = null;
        try {
        	myNode1.remove();
        	myNode2.remove();
        } catch (InvalidNodeException e) {
        	return;
        }
    }

    Vertex otherVertex(Object vertex){
    	if (vertex.equals(vertex1.object()) && vertex.equals(vertex2.object())){
    		return vertex1;
    	} else if (vertex.equals(vertex1.object())){
    		return vertex2;
    	} else if (vertex.equals(vertex2.object())){
    		return vertex1;
    	} else {
    		return null;
    	}
    }
    
    int weight() {
    	return weight;
    }
    
}


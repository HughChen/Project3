package graph;
import list.*;

class Vertex{

    DList edges;
    Object item;
    DListNode myNode;

    Vertex(Object o){
        this.edges = new DList();
        this.item = o;
    }

    void addEdge(Edge e){
        edges.insertBack(e);
    }

    int degree(){
        return edges.length();
    }

    Object object(){
        return item;
    }

    void remove(){
        this.edges = null;
        try {
			myNode.remove();
		} catch (InvalidNodeException e) {
			e.printStackTrace();
		}
    }
}

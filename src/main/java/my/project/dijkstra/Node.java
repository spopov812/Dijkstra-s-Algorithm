package my.project.dijkstra;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * Blueprint for a node object that represents a place on the
 * board where it is possible for the solution to the maze to stop
 * going in a straight line and turn.
 * 
 * @author Aleksandr Popov
 *
 */
public class Node {

		private int row;
		private int col;
		private Node prevNode = null;
		private HashMap<Node, Integer> distanceToNode = new HashMap<Node, Integer>();
		private List<Node> possibleNodes = new ArrayList<Node>();
		
		//distance that has been traveled to a node
		private int distance;
		
		private boolean visited = false;
		private boolean hasExit = false;
	
		/**
		 * 
		 * Constructor for a node object
		 * 
		 * @param row row of the pixel where the node is placed
		 * @param col column of the pixel where the node is placed
		 */
		public Node (int row, int col){
			
			this.row = row;
			this.col = col;
		}
		
		/**
		 * 
		 * Returns the row of the node
		 * 
		 * @return row of the node
		 */
		public int getRow(){
			
			return row;
		}
		
		/**
		 * 
		 * Returns the column of the node
		 * 
		 * @return column of the node
		 */
		public int getCol(){
			
			return col;
		}
		
		/**
		 * 
		 * Sets the previous node through which the path has to go to
		 * reach this node
		 * 
		 * @param n the previous node through which the path has to go to reach this node
		 */
		public void setPrevNode(Node n){
			
			prevNode = n;
		}
		
		/**
		 * 
		 * Returns the previous node through which the path has to go to
		 * reach this node
		 * 
		 * @return the previous node through which the path has to go to reach this node
		 */
		public Node getPrevNode(){
			
			return prevNode;
		}
		
		/**
		 * 
		 * Returns if this node has a previous node through which the path must
		 * go to reach this node
		 * 
		 * @return true if this node has a previous node through which the path must go
		 * to reach this node, false otherwise
		 * 
		 */
		public boolean hasPrevNode(){
			
			if (prevNode == null){
				
				return false;
			}
			
			return true;
		}	
		
		/**
		 * 
		 * Adds a node to which this node connects and the distance to that node
		 * 
		 * @param distance distance to a connecting node
		 * @param n node to which this node will make a connection
		 */
		public void addNodeDistancePair(int distance, Node n){
			
			distanceToNode.put(n, distance);
			possibleNodes.add(n);
		}
		
		/**
		 * 
		 * Returns the nodes to which this node connects to only if they haven't
		 * been under consideration for a path
		 * 
		 * @return list of nodes to which this node connects to only if they haven't been
		 * under a consideration for a path
		 */
		public List<Node> getPossibleNodes(){
			
			List<Node> returnList = new ArrayList<Node>();
			
			for(int i = 0; i < possibleNodes.size(); i++){
				
				//won't add a node if it has been visited
				if(possibleNodes.get(i) != null && !possibleNodes.get(i).wasVisited()){
					
					possibleNodes.get(i).setVisited();
					returnList.add(possibleNodes.get(i));
				}
			}
			
			return returnList;
		}
		
		/**
		 * 
		 * Returns the distance between two nodes
		 * 
		 * @param n node to/from which distance will be computed to this node
		 * @return the distance between two nodes
		 */
		public Integer pathToNode(Node n){
			
			return distanceToNode.get(n);
		}
		
		/**
		 * 
		 * Sets the distance to this node from another
		 * 
		 * @param distance the distance required to get to this node
		 */
		public void setDistance(int distance){
		
			this.distance = distance;
		}
		
		/**
		 * 
		 * Adds the distance to get to this node to the existing distance
		 * 
		 * @param distance the distance that will be added to the existing distance
		 * in this node
		 */
		public void addDistance(int distance){
			
			this.distance += distance;
		}
		
		/**
		 * 
		 * Sets that this node has been considered for the path to go through
		 * 
		 */
		public void setVisited(){
			
			visited = true;
		}
		
		/**
		 * 
		 * Returns if this node has been considered for the path to go through
		 * 
		 * @return returns true if this node has been considered for the path
		 * to go through, false otherwise
		 */
		public boolean wasVisited(){
			
			return visited;
		}
		
		/**
		 * 
		 * Sets the distance of the connecting nodes to this node by adding the distance
		 * from the connecting node to this node, plus the distance this node has
		 * 
		 * @param n list of nodes that connect to this node, whose distances will be updated
		 */
		public void updateChildDistances(List<Node> n){
			
			Node testNode = null;
			
			for (int i = 0; i < n.size(); i ++){
				
				testNode = n.get(i);
				
				testNode.setDistance(getDistance() + testNode.distanceToNode.get(this));
			}
		}
		
		/**
		 * 
		 * Returns the distance to this node
		 * 
		 * @return distance to this node
		 */
		public int getDistance(){
			
			return distance;
		}

		/**
		 * 
		 * Returns if this node connects to the exit
		 * 
		 * @return true if this node connects to the exit, false otherwise
		 */
		public boolean hasExit(){
			
			return hasExit;
		}
		
		/**
		 * 
		 * Sets this node as a connection to the exit
		 * 
		 */
		public void setHasExit(){
			
			hasExit = true;
		}
}

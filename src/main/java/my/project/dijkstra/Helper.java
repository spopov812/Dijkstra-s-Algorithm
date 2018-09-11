package my.project.dijkstra;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Class that contains methods helpful for computing Dijkstra's Algorithm.
 * 
 * @author Aleksandr Popov
 *
 */
public class Helper {

	private BufferedImage image = null;
	private BufferedImage imageCopy = null;
	private int black = -16777216;
	private int white = -1;
	private HashMap<String, Node> map = new HashMap<String, Node>();
	public double timeSpentPrinting;

	/**
	 * Constructor for helper object.
	 * 
	 * @param s
	 *            name of .png file (maze).
	 * @throws IOException 
	 */
	public Helper(String s) throws IOException {
		File file = new File(s);

		image = ImageIO.read(file);
		
		imageCopy = ImageIO.read(file);
	}

	/**
	 * Finds the entrance and exit of the maze and returns them as a ArrayList
	 * where the first index is the entrance and second is the exit.
	 * 
	 * @return ArrayList of two nodes where the first node is the entrance and
	 *         the second is the exit.
	 * 
	 */
	public ArrayList<Node> setEntranceExit() {

		boolean foundEntrance = false;
		boolean foundExit = false;

		ArrayList<Node> entranceExit = new ArrayList<Node>();

		// loops through image pixel data
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {

				// if the current pixel being examined isn't on the edge of the
				// image
				if (isNotEdgeOfMap(i, j)) {

					continue;
				}

				// if the pixel is on the edge and is white, and the entrance
				// hasn't been found yet
				if (image.getRGB(i, j) == white && !foundEntrance) {

					// adds a node to the list of entrance and exit nodes
					entranceExit.add(new Node(i, j));

					foundEntrance = makeEntrance(i, j);
				}

				// if the pixel is on the edge and is white, and the entrance
				// has been found already
				else if (image.getRGB(i, j) == white && foundEntrance) {

					// adds a node to the list of entrance and exit nodes
					entranceExit.add(new Node(i, j));

					foundExit = makeExit(i, j);
					break;
				}
			}

			// entrance and exit has been found, then break
			if (foundExit) {

				break;
			}
		}

		/*
		 * passes the entrance and exit nodes to a helper method that links them
		 * to to other nodes in the maze
		 */
		return linkEntranceExit(entranceExit);
	}

	/**
	 *
	 * Determines if the x, y coordinate pair is at the edge of the maze.
	 *
	 * @param x x coordinate of the pixel in the maze
	 * @param y y coordinate of the pixel in the maze
	 * @return whether or not the coordinate pair is at the edge of the maze.
	 */
	private boolean isNotEdgeOfMap(int x, int y){

		return x != 0 && y != 0 && x != image.getHeight() - 1 && y != image.getWidth() - 1;
	}

	/**
	 *
	 * Colors the exit pixel red.
	 *
	 * @param x x coordinate of the exit pixel.
	 * @param y y coordinate of the exit pixel.
	 * @return true that an exit has been made.
	 */
	private boolean makeExit(int x, int y){

		System.out.println("Exit at (" + y + ", " + x + ")");

		// sets the pixel to red
		image.setRGB(x, y, Color.RED.getRGB());
		imageCopy.setRGB(x, y, Color.RED.getRGB());

		return true;
	}

	/**
	 *
	 * Colors the entrance pixel red.
	 *
	 * @param x x coordinate of the entrance pixel.
	 * @param y y coordinate of the entrance pixel.
	 * @return true that an entrance has been made.
	 */
	private boolean makeEntrance(int x, int y){

		System.out.println("Entrance at (" + y + ", " + x + ")");

		// sets the pixel to red
		image.setRGB(x, y, Color.RED.getRGB());
		imageCopy.setRGB(x, y, Color.RED.getRGB());

		return true;
	}

	/**
	 * 
	 * Helper method that links the entrance and exit nodes to other nodes.
	 * 
	 * @param entranceExit
	 *            ArrayList that contains the entrance and exit nodes.
	 * @return ArrayList that contains the entrance and exit nodes, but has
	 *         connected them to surrounding nodes.
	 * 
	 */
	private ArrayList<Node> linkEntranceExit(ArrayList<Node> entranceExit) {

		Node entrance = entranceExit.get(0);

		int currRow;
		int currCol;
		int targetRow = 0;
		int targetCol = 0;

		boolean foundNode = false;
		int counter = 0;
		boolean connected = false;

		// goes over the entrance and exit nodes
		for (int a = 0; a < entranceExit.size(); a++) {

			currRow = entranceExit.get(a).getRow();
			currCol = entranceExit.get(a).getCol();

			// looks right of entrance or exit node for a node to connect to
			try {
				for (int j = currRow + 1; true; j++) {

					if (image.getRGB(j, currCol) == black) {
						break;
					}

					if (image.getRGB(j, currCol) == Color.RED.getRGB()) {

						targetRow = j;
						targetCol = currCol;
						foundNode = true;
						break;
					}
				}
			} catch (ArrayIndexOutOfBoundsException oob) {

			}

			// looks left of entrance or exit node for a node to connect to
			if (!foundNode) {

				try {
					for (int j = currRow - 1; true; j--) {

						if (image.getRGB(j, currCol) == black) {
							break;
						}

						if (image.getRGB(j, currCol) == Color.RED.getRGB()) {

							targetRow = j;
							targetCol = currCol;
							foundNode = true;
							break;
						}
					}
				} catch (ArrayIndexOutOfBoundsException oob) {

				}
			}

			// looks down of entrance or exit node for a node to connect to
			if (!foundNode) {

				try {
					for (int j = currCol + 1; true; j++) {

						if (image.getRGB(currRow, j) == black) {
							break;
						}

						if (image.getRGB(currRow, j) == Color.RED.getRGB()) {

							targetRow = currRow;
							targetCol = j;
							foundNode = true;
							break;
						}
					}
				} catch (ArrayIndexOutOfBoundsException oob) {

				}
			}

			// looks up of entrance or exit node for a node to connect to
			if (!foundNode) {

				try {
					for (int j = currCol - 1; true; j--) {

						if (image.getRGB(currRow, j) == black) {
							break;
						}

						if (image.getRGB(currRow, j) == Color.RED.getRGB()) {

							targetRow = currRow;
							targetCol = j;
							foundNode = true;
							break;
						}
					}
				} catch (ArrayIndexOutOfBoundsException oob) {

				}
			}

			// finds proper key of the node to connect to
			for (int i = 1; i < image.getHeight() - 1; i++) {
				for (int j = 1; j < image.getWidth() - 1; j++) {

					counter++;

					if (i != targetRow || j != targetCol) {

						continue;
					}

					// connects entrance node
					if (a == 0) {

						entrance.addNodeDistancePair(1, map.get(Integer.toString(counter)));
						map.get(Integer.toString(counter)).addNodeDistancePair(1, entrance);
						connected = true;
						break;
					}

					// connects exit node
					else {

						map.get(Integer.toString(counter)).setHasExit();
						connected = true;
						break;
					}
				}

				if (connected) {
					connected = false;
					break;
				}
			}

			counter = 0;
			foundNode = false;
		}

		return entranceExit;
	}

	/**
	 * 
	 * Places a node where there is either a corner or an intersection and
	 * colors that pixel red.
	 */
	public void placeNodes() {

		int key = 0;
		int secondKey = 0;
		int counter = 0;
		int rightCounter;
		Node prevNode = null;

		int numNodes = 0;

		// starts timer
		System.out.println("Creating nodes...");
		long currTime = System.nanoTime();

		// loops over image
		for (int i = 1; i < image.getHeight() - 1; i++) {

			rightCounter = -1;

			for (int j = 1; j < image.getWidth() - 1; j++) {

				rightCounter++;
				key++;

				// if the pixel is black, there can't be a connection to be made
				// to a node above
				if (image.getRGB(i, j) == black) {

					rightCounter = -1;
					prevNode = null;
					continue;
				}

				// valid node position
				if (image.getRGB(i, j) == white && validNode(i, j)) {

					numNodes++;

					image.setRGB(i, j, Color.RED.getRGB());

					// makes a new node and places it in a hashmap
					map.put(Integer.toString(key), new Node(i, j));

					// available node connection to the above
					if (rightCounter != 0 && prevNode != null) {

						// links the discovered node to a node above (if
						// possible)
						prevNode.addNodeDistancePair(rightCounter, map.get(Integer.toString(key)));
						map.get(Integer.toString(key)).addNodeDistancePair(rightCounter, prevNode);
					}

					prevNode = map.get(Integer.toString(key));

					secondKey = key;

					// looks left for another node connection
					for (int up = i - 1; up > 0; up--) {

						counter++;
						secondKey -= (image.getHeight() - 2);

						// search encountered a wall before it found a node to
						// the left
						if (image.getRGB(up, j) == black) {

							break;
						}

						// found a node to the left of the new discovered node
						if (image.getRGB(up, j) == Color.RED.getRGB()) {

							// make a node connection
							prevNode.addNodeDistancePair(counter, map.get(Integer.toString(secondKey)));
							map.get(Integer.toString(secondKey)).addNodeDistancePair(counter, prevNode);

							break;
						}

					}

					counter = 0;
				}
			}

			prevNode = null;
		}

		// stops timer
		System.out.println("\n" + numNodes + " nodes have been created. ("
				+ ((double) (System.nanoTime() - currTime) / 1000000000) + " seconds)\n");
	}

	/**
	 * 
	 * Helper method that determines if the pixel under consideration is a valid
	 * place for a node.
	 * 
	 * @param row
	 *            row of the image where a pixel under consideration is.
	 * @param col
	 *            column of the image where a pixel under consideration is.
	 * @return whether or not this pixel is a valid position for a node.
	 */
	private boolean validNode(int row, int col) {

		boolean horizontal = false;
		boolean vertical = false;

		int numMoves = 0;


		for (int i = -1; i < 2; i += 2){

			// Looks above and below
			try {
				if (image.getRGB(row + i, col) == white || image.getRGB(row + i, col) == Color.RED.getRGB()) {

					vertical = true;
					numMoves++;
				}
			} catch (ArrayIndexOutOfBoundsException oob) {

			}

			// Looks left and right
			try {
				if (image.getRGB(row, col + i) == white || image.getRGB(row, col + i) == Color.RED.getRGB()) {

					horizontal = true;
					numMoves++;
				}
			} catch (ArrayIndexOutOfBoundsException oob) {

			}
		}

		return numMoves > 2 || (vertical && horizontal);
	}

	/**
	 * 
	 * Creates a png of the maze passed in, but colors the pixels red where a
	 * node has been placed.
	 */
	public void printNodes() {

		// starts timer
		System.out.println("\nGenerating Nodes.png...");
		long currTime = System.nanoTime();

		try {

			BufferedImage bi = image;
			File outputfile = new File("Nodes.png");
			ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {

		}

		// stops timer
		double timeSpent = (double) (System.nanoTime() - currTime) / 1000000000;
		timeSpentPrinting += timeSpent;

		System.out.println("Nodes.png has been generated (" + timeSpent + " seconds)\n");
	}

	/**
	 * 
	 * Sorts the list passed in with an Insertion Sort algorithm based on their
	 * distance instance variable.
	 * 
	 * @param list
	 *            list of nodes to be sorted according to their distance
	 *            instance variable.
	 * @return sorted list of nodes.
	 */
	public LinkedList<Node> sort(List<Node> list) {

		Node[] nodeSort = list.toArray(new Node[list.size()]);

		Node temp;

		boolean foundMistake = false;

		//loops over unsorted portion
		for (int i = 0; i < list.size(); i++){

			//loops over sorted portion
			for (int j = i; j > 0; j--){

				//if unsorted element is to be inserted before or into this index
				if (list.get(j).getDistance() < list.get(j - 1).getDistance()){

					//swap into this index
					temp = list.get(j);
					list.add(j, list.get(j - 1));
					list.add(j - 1, temp);
				}

				else{

					break;
				}
			}
		}

		return new LinkedList (Arrays.asList(nodeSort));
	}

	/**
	 * 
	 * Backpropogates path from the exit back to the entrance. Colors the path
	 * red between the nodes.
	 * 
	 * @param n
	 *            the exit node from which backpropogation will begin.
	 */
	public void backpropogate(Node n) {

		Node printNode = n;

		// starts timer
		System.out.println("Starting backpropogation...");
		long currTime = System.nanoTime();
		int sign;

		// while there is another node through which the path continues
		while (printNode.hasPrevNode()) {

			imageCopy.setRGB(printNode.getRow(), printNode.getCol(), Color.RED.getRGB());

			// draws lines between nodes
			sign = printNode.getCol() - printNode.getPrevNode().getCol();
			sign = sign / Math.abs(sign);


			// if the previous node is left of the current node
			if (printNode.getCol() > printNode.getPrevNode().getCol()) {

				for (int i = printNode.getCol() - 1; i > printNode.getPrevNode().getCol(); i--) {

					imageCopy.setRGB(printNode.getRow(), i, Color.RED.getRGB());
				}
			}

			// if the previous node is right of the current node
			else if (printNode.getCol() < printNode.getPrevNode().getCol()) {

				for (int i = printNode.getCol() + 1; i < printNode.getPrevNode().getCol(); i++) {

					imageCopy.setRGB(printNode.getRow(), i, Color.RED.getRGB());
				}
			}

			// if the previous node is above the current node
			else if (printNode.getRow() > printNode.getPrevNode().getRow()) {

				for (int i = printNode.getRow() - 1; i > printNode.getPrevNode().getRow(); i--) {

					imageCopy.setRGB(i, printNode.getCol(), Color.RED.getRGB());
				}
			}

			// if the previous node is below the current node
			else if (printNode.getRow() < printNode.getPrevNode().getRow()) {

				for (int i = printNode.getRow() + 1; i < printNode.getPrevNode().getRow(); i++) {

					imageCopy.setRGB(i, printNode.getCol(), Color.RED.getRGB());
				}
			}

			printNode = printNode.getPrevNode();
		}

		System.out.println("Backpropogation has finished " + ((double) (System.nanoTime() - currTime) / 1000000000)
				+ " seconds)\n");

		System.out.println("Generating Path.png...");
		currTime = System.nanoTime();

		// prints the png with the solution to the maze
		try {

			BufferedImage printing = imageCopy;
			File outputfile = new File("Path.png");
			ImageIO.write(printing, "png", outputfile);
		} catch (IOException e) {

		}

		double timeSpent = (double) (System.nanoTime() - currTime) / 1000000000;
		timeSpentPrinting += timeSpent;

		System.out.println("Path.png has been generated (" + timeSpent + " seconds)\n");
	}

}

package my.project.dijkstra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * Driver class for the implementation of Dijkstra's algorithm to solve mazes.
 * 
 * @author Aleksandr Popov
 *
 */
public class Dijkstra {

	public static void main(String[] args) {

		Scanner keyboard = new Scanner(System.in);
		String userInput;

		Helper helper = null;

		//creation of helper object
		while (true) {

			System.out.print("\nPlease type maze filename: ");
			userInput = keyboard.nextLine();

			try {

				helper = new Helper(userInput);
			} catch (IOException io1) {

				try {

					helper = new Helper(userInput + ".png");
				} catch (IOException io2) {

					System.out.println("Invalid file name.");
					continue;
				}
			}

			break;
		}

		// starts tracking execution time
		long totalTime = System.nanoTime();

		// places nodes on the board
		helper.placeNodes();

		// finds entrance and exit and returns them
		ArrayList<Node> entranceExit = helper.setEntranceExit();
		Node entrance = entranceExit.get(0);
		Node exit = entranceExit.get(1);

		// generates Nodes.png
		helper.printNodes();

		// this list is the list of nodes under consideration
		LinkedList<Node> nodeList = new LinkedList<Node>();

		// list of available nodes from the current node under consideration
		List<Node> possibleNodesFromTopNode = null;

		// sets proper information for the start node and adds it to the list of
		// nodes under consideration
		entrance.setVisited();
		entrance.setDistance(0);
		nodeList.add(entrance);

		Node topNode = null;

		System.out.println("Looking for path...");
		long currTime = System.nanoTime();

		// dijkstra
		while (true) {

			/*
			 * returns the top node under consideration (smallest distance
			 * value)
			 */
			topNode = nodeList.getFirst();

			/*
			 * if this node is linked to the exit, breaks loop and starts
			 * backpropagation
			 */
			if (topNode.hasExit()) {

				exit.setPrevNode(topNode);
				break;
			}

			// gets the possible nodes from the current node under consideration
			possibleNodesFromTopNode = topNode.getPossibleNodes();

			/*
			 * sets the current node as the node through which the possible
			 * nodes from this node must go
			 */
			for (int i = 0; i < possibleNodesFromTopNode.size(); i++) {

				possibleNodesFromTopNode.get(i).setPrevNode(topNode);
			}

			/*
			 * there are no possible directions from the top node (dead end),
			 * throws away the current node under consideration
			 */

			if (possibleNodesFromTopNode.isEmpty()) {

				nodeList.removeFirst();
				continue;
			}

			// updates the distance traveled
			topNode.updateChildDistances(possibleNodesFromTopNode);

			/*
			 * adds all possible nodes from the node under consideration and
			 * adds it to the list of nodes to consider later
			 */
			nodeList.addAll(possibleNodesFromTopNode);

			// sorts the list of nodes that are to be considered later based on
			// distance travelled
			nodeList = helper.sort(nodeList);

			// removes the current node under consideration
			nodeList.removeFirst();

		}

		System.out.println(
				"Path has been found (" + ((double) (System.nanoTime() - currTime) / 1000000000) + " seconds)\n");

		// backpropogation up through path stack
		helper.backpropogate(exit);

		double totalExecTime = (double) (System.nanoTime() - totalTime) / 1000000000;

		System.out.println("Total execution time- " + totalExecTime + " seconds");
		System.out.println("Time spent on computing Djikstra (not printing images)- "
				+ (totalExecTime - helper.timeSpentPrinting) + " seconds\n");

	}

}

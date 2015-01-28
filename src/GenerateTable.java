import java.util.ArrayList;

public class GenerateTable {

	public void GenerateRoutingTable(Router a, ArrayList<Link> network) {

		// initial variables
		String node = a.routerName;

		ArrayList<String> nodes = new ArrayList<String>();
		ArrayList<String> unvisited = new ArrayList<String>();
		ArrayList<String> visited = new ArrayList<String>();

		ArrayList<TableContent> routingTable = new ArrayList<TableContent>();

		int size = 0;

		// loop control
		int i = 0;
		int j = 0;

		// temp variable
		double tempcost = Double.POSITIVE_INFINITY;
		String tempnode = null;
		String tempparent = null;

		// record nodes in the network
		for (i = 0; i < network.size(); i++) {
			if (nodes.indexOf(network.get(i).source) == -1) {
				nodes.add(network.get(i).source);
			}

			if (nodes.indexOf(network.get(i).destination) == -1) {
				nodes.add(network.get(i).destination);
			}
		}

		// test nodes
		// System.out.println(nodes.size());

		// initial unvisited nodes
		for (i = 0; i < nodes.size(); i++) {
			if (nodes.get(i) != node) {
				unvisited.add(nodes.get(i));
			}
		}

		// test unvisited
		// System.out.println(unvisited.size());

		// initial visited
		visited.add(node);
		//System.out.println(node);

		// build routing table with Dijkstra
		while (unvisited.size() > 0) {

			size = visited.size();

			// System.out.println(size);
			// System.out.println(node);

			tempcost = Double.POSITIVE_INFINITY;

			for (i = 0; i < size; i++) {
				for (j = 0; j < network.size(); j++) {
					if (network.get(j).source == visited.get(i)
							&& visited.indexOf(network.get(j).destination) == -1) {
						if (visited.get(i) == node) {
							if (network.get(j).cost < tempcost) {
								tempcost = network.get(j).cost;
								tempparent = node;
								tempnode = network.get(j).destination;
							}
						} else {
							if (network.get(j).cost
									+ routingTable.get(i - 1).cost < tempcost) {
								tempcost = network.get(j).cost
										+ routingTable.get(i - 1).cost;
								tempparent = network.get(j).source;
								tempnode = network.get(j).destination;
							}
						}
					}

					if (network.get(j).destination == visited.get(i)
							&& visited.indexOf(network.get(j).source) == -1) {
						if (visited.get(i) == node) {
							if (network.get(j).cost < tempcost) {
								tempcost = network.get(j).cost;
								tempparent = node;
								tempnode = network.get(j).source;
							}
						} else {
							if (network.get(j).cost
									+ routingTable.get(i - 1).cost < tempcost) {
								tempcost = network.get(j).cost
										+ routingTable.get(i - 1).cost;
								tempparent = network.get(j).destination;
								tempnode = network.get(j).source;
							}
						}
					}
				}
			}

			TableContent content = new TableContent();
			content.cost = tempcost;
			content.destination = tempnode;
			content.parent = tempparent;

			//System.out.println(content.cost);
			//System.out.println(content.destination);
			//System.out.println(content.parent);

			routingTable.add(content);

			unvisited.remove(tempnode);

			visited.add(tempnode);
		}

		// track parent to find port
		for (i = 0; i < routingTable.size(); i++) {
				if (routingTable.get(i).parent == node) {
					routingTable.get(i).port = routingTable.get(i).destination;
				} else {
					for(j=0;j<routingTable.size();j++){
						if(routingTable.get(j).destination == routingTable.get(i).parent){
							routingTable.get(i).port = routingTable.get(j).port;
						}
				}
			}
		}

		a.routingTable = routingTable;

	}

}

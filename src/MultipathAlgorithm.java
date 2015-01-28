import java.util.ArrayList;

public class MultipathAlgorithm {
	public boolean isFinished;
	public boolean isDeadEnd;
	public ArrayList<Router> visitedRouters;

	public ArrayList<ArrayList<Link>> searchpath(Router source_,
			Router destination_, ArrayList<Link> networklinks,
			ArrayList<Router> networkrouters, int pathnumber) {
		System.out.println("---------------Start searchpath-----------------");
		ArrayList<ArrayList<Link>> pathSet = new ArrayList<ArrayList<Link>>();
		System.out.println("We have " + pathnumber + " path at most!");
		for (int i = 1; i <= pathnumber; i++) {
			System.out.print("The " + i + "th path is: ");
			ArrayList<Link> path = new ArrayList<Link>(0);
			isDeadEnd = false;
			isFinished = false;
			visitedRouters = new ArrayList<Router>(0);
			path = searchsinglepath(source_, destination_, networklinks, path,
					networkrouters);
			if (path.size() == 0) {
				System.out.print("No more ways!!");
				break;
			}
			System.out.print("[");
			for (int j = 0; j < path.size(); j++) {
				System.out.print(path.get(j).source + " "
						+ path.get(j).destination + ",  ");
			}
			System.out.println("]");
			pathSet.add(path);
			for (int j = 0; j < path.size(); j++) {
				Router left = new GenerateNetwork().GetRouterByName(
						path.get(j).source, networkrouters);
				Router right = new GenerateNetwork().GetRouterByName(
						path.get(j).destination, networkrouters);
				if (right != null && left != null) {
					left.neighborRouters.remove(right);
					right.neighborRouters.remove(left);
					networklinks.remove(path.get(j));
				}
			}
//			System.out.print("\tThe left network is: ");
//			for (int j = 0; j < networklinks.size(); j++) {
//				System.out.print(networklinks.get(j).source + " "
//						+ networklinks.get(j).destination + " \t");
//			}
//			System.out.println();
		}
		// find pathnumber ways
		return pathSet;
	}

	public ArrayList<Link> searchsinglepath(Router start, Router end,
			ArrayList<Link> way, ArrayList<Link> currentpath,
			ArrayList<Router> nodes) {
		if (isFinished)
			return currentpath;
		for (int i = 0; i < visitedRouters.size(); i++) {
			if (visitedRouters.get(i).routerName.equals(start.routerName) == true) {
//				System.out.print("Circult!");
				isDeadEnd = true;
				return currentpath;
			}
		}
		visitedRouters.add(start);
		if (start.routerName.equals(end.routerName)) {
			isFinished = true;
			return currentpath;
		}
		for (int i = 0; i < start.neighborRouters.size(); i++) {
			if (start.neighborRouters.get(i).routerName.equals(end.routerName) == true) {
				Link finalLink = new GenerateNetwork().LocateLink(start, end,
						way);
				currentpath.add(finalLink);
				visitedRouters.add(end);
//				System.out.print(finalLink.source + " " + finalLink.destination
//						+ "end \t");
				isFinished = true;
				return currentpath;
			}
		}
		if (start.neighborRouters.isEmpty() || end.neighborRouters.isEmpty()) {
			// remove extra links
//			System.out.print("Dead!");
			isDeadEnd = true;
			return currentpath;
		}
		while (start.neighborRouters.size() != 0) {
			isDeadEnd = false;
			Link templink;
			templink = new GenerateNetwork().LocateLink(start,
					start.neighborRouters.get(0), way);
			if (templink != null) {
				currentpath.add(templink);
//				System.out.print(templink.source + " " + templink.destination
//						+ "\t");
			} else
				break;

			int indexOflinkToRemove = new GenerateNetwork().GetLinkIndex(start,
					start.neighborRouters.get(0), way);
			start.neighborRouters.get(0).neighborRouters.remove(start);
			if (indexOflinkToRemove >= 0)
				way.remove(indexOflinkToRemove);
			searchsinglepath(start.neighborRouters.get(0), end, way,
					currentpath, nodes);
			if (isDeadEnd) {
				currentpath.remove(templink);
			}
			if (isFinished) {
//				System.out.print("over");
				break;
			}
			start.neighborRouters.remove(0);
		}
		return currentpath;
	}

	public static void main(String[] args) {
		System.out.println("------------Generate Network--------------");
		ArrayList<Link> testNetworkLinks = new GenerateNetwork()
				.GenerateNetworkLink(20);
		ArrayList<Router> testNetworkRouters = new GenerateNetwork()
				.GenerateNetworkRouter(testNetworkLinks);
		for (int i = 0; i < testNetworkRouters.size(); i++) {
			testNetworkRouters.get(i).neighborRouters = new GenerateNetwork()
					.GenerateRouterNeighbors(testNetworkRouters.get(i),
							testNetworkLinks, testNetworkRouters);
		}
		for (int i = 0; i < testNetworkRouters.size(); i++) {
			System.out.print("The Router "
					+ testNetworkRouters.get(i).routerName
					+ "'s neighbors are: ");
			for (int j = 0; j < testNetworkRouters.get(i).neighborRouters
					.size(); j++) {
				System.out.print(testNetworkRouters.get(i).neighborRouters
						.get(j).routerName + " ");
			}
			System.out.println();
		}
		for (int i = 0; i < testNetworkRouters.size(); i++) {
			testNetworkRouters.get(i).bandwidth = new GenerateNetwork()
					.GenerateRelatedBandwidth(testNetworkRouters.get(i),
							testNetworkLinks);
//			System.out.print("Router " + testNetworkRouters.get(i).routerName
//					+ "'s bandwithslist is: ");
//			for (int j = 0; j < testNetworkRouters.get(i).bandwidth.size(); j++) {
//				System.out.print(" "
//						+ testNetworkRouters.get(i).bandwidth.get(j) + " ");
//			}
//			System.out.println();
		}
		GenerateTable b = new GenerateTable();
		for (int i = 0; i < testNetworkRouters.size(); i++) {
			b.GenerateRoutingTable(testNetworkRouters.get(i), testNetworkLinks);
		}

		// output routing table for test
		/*
		 * for (int i = 0; i < testNetworkRouters.get(1).routingTable.size();
		 * i++) {
		 * System.out.print(testNetworkRouters.get(1).routingTable.get(i).cost +
		 * " "); System.out
		 * .print(testNetworkRouters.get(1).routingTable.get(i).destination +
		 * " "); System.out
		 * .print(testNetworkRouters.get(1).routingTable.get(i).parent + " ");
		 * System.out
		 * .println(testNetworkRouters.get(1).routingTable.get(i).port); }
		 */

		System.out.println("----------------Generate Flow---------------");
		NodeOutput output = new GenerateOutput()
				.generateNode(testNetworkRouters.size());
		int pathnumber = new PathConstraint().GenerateConstraint(
				testNetworkRouters.get(output.startNode).routerName,
				testNetworkRouters.get(output.finalNode).routerName,
				output.bandwidth, testNetworkRouters);
		System.out.println("From "
				+ testNetworkRouters.get(output.startNode).routerName + " to "
				+ testNetworkRouters.get(output.finalNode).routerName + ":");
		ArrayList<ArrayList<Link>> testpath = new MultipathAlgorithm()
				.searchpath(testNetworkRouters.get(output.startNode),
						testNetworkRouters.get(output.finalNode),
						testNetworkLinks, testNetworkRouters, pathnumber);
		System.out.println("-----------------Split Flow in multipath-------------");
		ArrayList<Double> sp = new SplitPath()
				.Split(testpath, output.bandwidth);
		for (int i = 0; i < sp.size(); i++) {
			System.out.println("The " + (i + 1) + "th path gets " + sp.get(i)
					+ " Mbps");
		}

	}
}

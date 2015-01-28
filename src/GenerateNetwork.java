import java.util.ArrayList;
import java.util.Random;

public class GenerateNetwork {
	public ArrayList<Link> GenerateNetworkLink(int nodeNumber) {
		ArrayList<String> unvisited = new ArrayList<String>();
		ArrayList<String> visited = new ArrayList<String>();
		ArrayList<Link> generatedLink = new ArrayList<Link>();
		for (int i = 1; i <= nodeNumber; i++) {
			String tempString = String.valueOf(i);
			unvisited.add(tempString);
		}
		Random randomGenerator = new Random();
		int firstNode = randomGenerator.nextInt(unvisited.size());
		visited.add(unvisited.get(firstNode));
		unvisited.remove(firstNode);
		while (unvisited.size() != 0) {
			Link newlink = new Link();
			int visitedNode = randomGenerator.nextInt(visited.size());
			int unvisitedNode = randomGenerator.nextInt(unvisited.size());
			newlink.source = visited.get(visitedNode);
			newlink.destination = unvisited.get(unvisitedNode);
			newlink.bandwidth = 50 + randomGenerator.nextInt(100);
			newlink.cost = 100/(double)newlink.bandwidth;
			if(newlink.cost < 1) newlink.cost = 1;
			generatedLink.add(newlink);
			visited.add(unvisited.get(unvisitedNode));
			unvisited.remove(unvisitedNode);
		}
		// Tree built finished
		for (int i = 0; i < visited.size(); i++) {
			for (int j = 0; j < visited.size(); j++) {
				if (i != j) {
					boolean iflinkexists = false;
					int k = 0;
					while (k < generatedLink.size()) {
						if (visited.get(i).equals(generatedLink.get(k).source) == true) {
							if (visited.get(j).equals(
									generatedLink.get(k).destination) == true) {
								iflinkexists = true;
								break;
							}
						}
						if (visited.get(i).equals(
								generatedLink.get(k).destination) == true) {
							if (visited.get(j).equals(
									generatedLink.get(k).source) == true) {
								iflinkexists = true;
								break;
							}
						}
						k++;
					}
					if (!iflinkexists) {
						int choice = randomGenerator.nextInt(2);
						if (choice == 1) {
							Link newlink = new Link();
							newlink.source = visited.get(i);
							newlink.destination = visited.get(j);
							newlink.bandwidth = 50 + randomGenerator
									.nextInt(100);
							newlink.cost = 100/(double)newlink.bandwidth;
							if(newlink.cost < 1) newlink.cost = 1;
							generatedLink.add(newlink);
						}
					}
				}
			}
		}
		for (int i = 0; i < generatedLink.size(); i++) {
			System.out.print("Link: source: " + generatedLink.get(i).source
					+ "    ");
			System.out.println("destination: "
					+ generatedLink.get(i).destination + "	Bandwidth: "
					+ generatedLink.get(i).bandwidth + " Mbps");
		}
		return generatedLink;
	}

	public ArrayList<Router> GenerateNetworkRouter(ArrayList<Link> generatedLink) {
		ArrayList<Router> generatedRouter = new ArrayList<Router>();
		for (int i = 0; i < generatedLink.size(); i++) {
			int j = 0;
			while (j < generatedRouter.size()) {
				if (generatedLink.get(i).source
						.equals(generatedRouter.get(j).routerName) == true)
					break;
				j++;
			}
			if (j == generatedRouter.size()) {
				Router newRouter = new Router();
				newRouter.routerName = generatedLink.get(i).source;
				generatedRouter.add(newRouter);
			}
		}
		for (int i = 0; i < generatedLink.size(); i++) {
			int j = 0;
			while (j < generatedRouter.size()) {
				if (generatedLink.get(i).destination.equals(generatedRouter
						.get(j).routerName) == true)
					break;
				j++;
			}
			if (j == generatedRouter.size()) {
				Router newRouter = new Router();
				newRouter.routerName = generatedLink.get(i).destination;
				generatedRouter.add(newRouter);
			}
		}
		for (int i = 0; i < generatedRouter.size(); i++) {
			System.out.println("Router: " + generatedRouter.get(i).routerName);
		}

		return generatedRouter;
	}

	public ArrayList<Router> GenerateRouterNeighbors(Router local,
			ArrayList<Link> generatedLinks, ArrayList<Router> generatedRouters) {
		ArrayList<Router> neighbors = new ArrayList<Router>();
		for (int i = 0; i < generatedLinks.size(); i++) {
			if (local.routerName.equals(generatedLinks.get(i).source) == true) {
				for (int j = 0; j < generatedRouters.size(); j++) {
					if (generatedRouters.get(j).routerName
							.equals(generatedLinks.get(i).destination) == true) {
						neighbors.add(generatedRouters.get(j));
						break;
					}
				}
			} else if (local.routerName
					.equals(generatedLinks.get(i).destination) == true) {
				for (int j = 0; j < generatedRouters.size(); j++) {
					if (generatedRouters.get(j).routerName
							.equals(generatedLinks.get(i).source) == true) {
						neighbors.add(generatedRouters.get(j));
						break;
					}
				}
			}
		}

		return neighbors;
	}

	public ArrayList<Integer> GenerateRelatedBandwidth(Router local,
			ArrayList<Link> generatedLinks) {
		ArrayList<Integer> bandwidthSet = new ArrayList<Integer>(0);
		for (int i = 0; i < local.neighborRouters.size(); i++) {
			Link relatedlink = new GenerateNetwork().LocateLink(local,
					local.neighborRouters.get(i), generatedLinks);
			bandwidthSet.add(relatedlink.bandwidth);
		}
		return bandwidthSet;
	}

	public Link LocateLink(Router a, Router b, ArrayList<Link> generatedLinks) {
		Link locatedlink = new Link();
		for (int i = 0; i < generatedLinks.size(); i++) {
			if ((generatedLinks.get(i).source.equals(a.routerName) && generatedLinks
					.get(i).destination.equals(b.routerName))
					|| (generatedLinks.get(i).source.equals(b.routerName) && generatedLinks
							.get(i).destination.equals(a.routerName))) {
				locatedlink = generatedLinks.get(i);
				break;
			}
		}
		return locatedlink;
	}

	public int GetLinkIndex(Router a, Router b, ArrayList<Link> generatedLinks) {
		for (int i = 0; i < generatedLinks.size(); i++) {
			if ((generatedLinks.get(i).source.equals(a.routerName) && generatedLinks
					.get(i).destination.equals(b.routerName))
					|| (generatedLinks.get(i).source.equals(b.routerName) && generatedLinks
							.get(i).destination.equals(a.routerName))) {
				return i;
			}
		}
		return -1;
	}

	public Router GetRouterByName(String a, ArrayList<Router> generatedRouters) {
		for (int i = 0; i < generatedRouters.size(); i++) {
			if (generatedRouters.get(i).routerName.equals(a) == true) {
				return generatedRouters.get(i);
			}
		}
		return null;
	}
	

	public static int numberOfNodes = 10;

	public static void main(String args[]) {

		System.out
				.println("-------------------------Start test-------------------------");
		GenerateNetwork a = new GenerateNetwork();
		ArrayList<Link> newlinks = a.GenerateNetworkLink(numberOfNodes);
		ArrayList<Router> newrouters = a.GenerateNetworkRouter(newlinks);
		Router sampleRouter = newrouters.get(3);
		ArrayList<Router> sampleNeighbor = a.GenerateRouterNeighbors(
				sampleRouter, newlinks, newrouters);
		System.out.print("The neighbor routers of router "
				+ sampleRouter.routerName + " are: ");
		for (int i = 0; i < sampleNeighbor.size(); i++) {
			System.out.print(sampleNeighbor.get(i).routerName + " ");
		}
		System.out.println();
		Link aaa = a.LocateLink(newrouters.get(1), newrouters.get(2), newlinks);
		System.out.println(aaa.source + " " + aaa.destination);
		int b = a.GetLinkIndex(newrouters.get(1), newrouters.get(2), newlinks);
		System.out.println(b);
		GenerateOutput generateVariable = new GenerateOutput();
		NodeOutput pathStatus = new NodeOutput();
		pathStatus = generateVariable.generateNode(numberOfNodes);
	}
}

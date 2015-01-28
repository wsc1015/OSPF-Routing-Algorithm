import java.util.ArrayList;

public class PathConstraint {
	public int GenerateConstraint(String source, String destination, int flow,
			ArrayList<Router> routers) {
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;

		int pathconstraint = 1;
		int p = 0;

		int routersrc = 0;
		int routerdest = 0;

		double tempbandwidth = 0;

		int nexthop = 0;

		for (i = 0; i < routers.size(); i++) {
			if (routers.get(i).routerName.equals(source)) {
				routersrc = i;
			}

			if (routers.get(i).routerName.equals(destination)) {
				routerdest = i;
			}
		}

		nexthop = routersrc;

		for (i = 0; i < routers.size(); i++) {
			if (nexthop == routerdest) {
				break;
			}
			for (j = 0; j < routers.get(nexthop).routingTable.size(); j++) {
				if (routers.get(nexthop).routingTable.get(j).destination
						.equals(routers.get(routerdest).routerName)) {
					for (k = 0; k < routers.size(); k++) {
						if (routers.get(k).routerName.equals(routers
								.get(nexthop).routingTable.get(j).port)) {
							for (l = 0; l < routers.get(nexthop).neighborRouters
									.size(); l++) {
								if (routers.get(nexthop).neighborRouters.get(l).routerName == routers
										.get(k).routerName) {
									if (routers.get(nexthop).bandwidth.get(l) > tempbandwidth) {
										tempbandwidth = routers.get(nexthop).bandwidth
												.get(l);
									}
									nexthop = k;
								}
							}
						}
					}
				}
			}
		}

		p = (int) Math.rint(flow * 1.8 / tempbandwidth);

		if (p <= 0) {
			pathconstraint = 1;
		}else{
			pathconstraint = p;
		}

		return pathconstraint;
	}

}

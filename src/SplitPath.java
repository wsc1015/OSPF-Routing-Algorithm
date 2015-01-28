import java.util.ArrayList;

public class SplitPath {
	public ArrayList<Double> Split(ArrayList<ArrayList<Link>> pathSet, int flow) {
		ArrayList<Double> setways = new ArrayList<Double>(0);
		ArrayList<Integer> limit = new ArrayList<Integer>(0);
		for (int i = 0; i < pathSet.size(); i++) {
			Integer templimit = 10000;
			for (int j = 0; j < pathSet.get(i).size(); j++) {
				if (pathSet.get(i).get(j).bandwidth < templimit)
					templimit = pathSet.get(i).get(j).bandwidth;
			}
			limit.add(templimit);
			System.out.println("The " + (i+1) + "th path limit is " +  templimit + " Mbps");
		}
		Integer sum = 0;
		for (int i = 0; i < limit.size(); i++) {
			sum += limit.get(i);
		}
		for (int i = 0; i < limit.size(); i++) {
			Double tempRatio;
			tempRatio = (double) limit.get(i) / (double) sum;
			setways.add(tempRatio * flow);
		}

		return setways;
	}
}

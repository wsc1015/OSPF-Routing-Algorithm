import java.util.Random;

public class GenerateOutput {

	public NodeOutput generateNode(int size) {


		Random randomGenerator = new Random();
		NodeOutput output = new NodeOutput();
		output.startNode = randomGenerator.nextInt(size);

		output.finalNode = randomGenerator.nextInt(size);
		output.bandwidth = 300 + randomGenerator.nextInt(200);

		while (output.startNode == output.finalNode) {
			output.finalNode = randomGenerator.nextInt(size);
		}

		System.out.println("Bandwidth is : " + output.bandwidth + "Mbps");

		return output;

	}

}

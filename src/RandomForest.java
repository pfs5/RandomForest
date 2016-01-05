import java.util.ArrayList;
import java.util.Random;

public class RandomForest {
	
	private ArrayList<Sample> data;
	private ArrayList<DecisionTree> forest;
	private int forestSize;
	
	public RandomForest (ArrayList<Sample> data, int forestSize) {
		this.data = data;
		this.forestSize = forestSize;
		forest = new ArrayList<DecisionTree>();
		createRandomForest();
	}
	
	private void createRandomForest() {
		for (int i=0 ; i<forestSize; i++) {
			//Take random sample
			Random random = new Random();
			int start = random.nextInt(data.size());
			int end = random.nextInt(data.size());
			if (start>end) {
				int dummy = start;
				start = end;
				end = dummy;
			}
			ArrayList<Sample> randomDataSet = new ArrayList<Sample> ();
			for (int j=start; j<=end; j++) {
				Sample sample = data.get(j);
				randomDataSet.add(sample);
			}
			DecisionTree tree = new DecisionTree(randomDataSet);
			forest.add(tree);
		}
	}

	public ArrayList<DecisionTree> getForest() {
		return forest;
	}

	public void setForest(ArrayList<DecisionTree> forest) {
		this.forest = forest;
	}
}

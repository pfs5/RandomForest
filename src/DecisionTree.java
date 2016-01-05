import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DecisionTree {

	private static int CLASSES_NUMBER = 12;
	private int TREE_SIZE = 0;
	ArrayList<Sample> allData;
	TreeNode root;

	public DecisionTree(ArrayList<Sample> data) {
		this.allData = data;
		root = new TreeNode();
		split (root, data);
	}
	
	private void printData(ArrayList<Sample> data) {
		System.out.println("");
		System.out.println(" ### Tree output ###");
		System.out.println("SIZE: "+data.size());
		for (Sample current: data)
			System.out.println(current.getR()+" "+current.getG()+" "+current.getB()+" "+current.getColor());
		System.out.println("");
		System.out.println(" ###");
	}
	
	private TreeNode split (TreeNode root, ArrayList<Sample> data) {
		
		TREE_SIZE++;
		//Check if empty
		if (data.size()==0) {
			root.setType("Leaf");
			root.setColor(-2);
			return root;
		}

		//Check if single element
		if (data.size()==1) {
			root.setType("Leaf");
			root.setColor(data.get(0).getColor());
			return root;
		}
		
		//Check if all data is in same class
		int color = -1;
		for (Sample current : data) {
			if (color==-1)
				color = current.getColor();
			else if (color != current.getColor()) {
				color = -2;
				break;
			}
		}
		
		if (color>0) {
			root.setType("Leaf");
			root.setColor(color);
			return root;
		}
		
		
		
		//Calculate E(s)
		double entropy = calcEntropy(data);
		
		//Find best attribute split
		double entropies [][] = new double [5][5];
		entropies [0] = testSplit("R", data);
		entropies [1] = testSplit("G", data);
		entropies [2] = testSplit("B", data);
		
		String bestSplit = findBestSplit(entropies[0][0], entropies[1][0], entropies[2][0]);
		
		//Configure node
		ArrayList<Sample> leftList = new ArrayList<Sample>();
		ArrayList<Sample> rightList = new ArrayList<Sample>();
		
		int splitCriteria=-1;
		if (bestSplit.equals("R"))
			splitCriteria = (int)entropies[0][1];
		if (bestSplit.equals("G"))
			splitCriteria = (int)entropies[1][1];
		if (bestSplit.equals("B"))
			splitCriteria = (int)entropies[2][1];
		
		if (bestSplit.equals("R")){
			root.setSplitCriteria(splitCriteria);
			for (Sample current : data) {
				if (current.getR()<splitCriteria)
					leftList.add(current);
				else
					rightList.add(current);
			}
		}
		if (bestSplit.equals("G")){
			root.setSplitCriteria(splitCriteria);
			for (Sample current : data) {
				if (current.getG()<splitCriteria)
					leftList.add(current);
				else
					rightList.add(current);
			}
		}
		if (bestSplit.equals("B")){
			root.setSplitCriteria(splitCriteria);
			for (Sample current : data) {
				if (current.getB()<splitCriteria)
					leftList.add(current);
				else
					rightList.add(current);
			}
		}
		
		//Recursive function call
		TreeNode left = new TreeNode();
		TreeNode right = new TreeNode();
		split(left, leftList);
		split(right, rightList);
		
		root.setSplitCriteria(splitCriteria);
		root.setSplitAttribute(bestSplit);
		root.setLeft(left);
		root.setRight(right);
		return root;
		
	}

	private double [] testSplit (String attribute, ArrayList<Sample> data) {
		double entropy = 0;
		int splitValue = -1;
		
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		if (attribute.equals("R"))
			Collections.sort(data, new RCompare());
		if (attribute.equals("G"))
			Collections.sort(data, new GCompare());
		if (attribute.equals("B"))
			Collections.sort(data, new BCompare());
		
		//Find best split
		for (Sample current : data) {
			int currentSplit=-1;
			if (attribute.equals("R"))
				currentSplit = current.getR();
			if (attribute.equals("G"))
				currentSplit = current.getG();
			if (attribute.equals("B"))
				currentSplit = current.getB();
			
			ArrayList <Sample> leftList = new ArrayList<Sample>();
			ArrayList <Sample> rightList = new ArrayList<Sample>();
			
			//Split into two lists
			for (Sample currentSample : data) {
				int sampleData = -1;
				if (attribute.equals("R"))
					sampleData = currentSample.getR();
				if (attribute.equals("G"))
					sampleData = currentSample.getG();
				if (attribute.equals("B"))
					sampleData = currentSample.getB();
				
				if (sampleData<currentSplit)
					leftList.add(currentSample);
				else
					rightList.add(currentSample);
			}
			
			double currentEntropy = ((double)leftList.size()/data.size())*calcEntropy(leftList)+
					((double)rightList.size()/data.size())*calcEntropy(rightList);
			
			if (entropy==0 || currentEntropy<entropy){
				entropy = currentEntropy;
				splitValue = currentSplit;
			}
		}
		
		double returnList [] = new double [2];
		returnList[0] = entropy;
		returnList[1] = splitValue;
		
		return returnList;
	}

	private double calcEntropy (ArrayList<Sample> data) {
		double entropy = 0;
		int N = data.size();
		int frequency [] = new int [CLASSES_NUMBER];
		if (N==0)
			return 0;
		for (Sample current : data) {
			int color = current.getColor();
			frequency[color]++;
		}
		for (int i=0; i<CLASSES_NUMBER; i++) {
			double p = frequency[i]/(double)N;
			if (p!=0)
				entropy+= (p*Math.log(p)) / Math.log(2);
		}
		
		entropy*=-1;
		return entropy;
	}
	
	private String findBestSplit (double R, double G, double B) {
		double min = R;
		
		if (G<min)
			return "G";
		if (B<min)
			return "B";
		return "R";
	}
	
	public TreeNode getRoot() {
		return root;
	}
}

class RCompare implements Comparator<Sample> {
	@Override
	public int compare (Sample s1, Sample s2) {
		if (s1.getR() > s2.getR())
			return 1;
		else
			return -1;
	}
}

class GCompare implements Comparator<Sample> {
	@Override
	public int compare (Sample s1, Sample s2) {
		if (s1.getG() > s2.getG())
			return 1;
		else
			return -1;
	}
}

class BCompare implements Comparator<Sample> {
	@Override
	public int compare (Sample s1, Sample s2) {
		if (s1.getB() > s2.getB())
			return 1;
		else
			return -1;
	}
}


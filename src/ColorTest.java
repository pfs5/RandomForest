import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.text.StyledEditorKit.ForegroundAction;

public class ColorTest extends JPanel implements ActionListener {

	//Settings
	private int LEARNING_SET_SIZE = 600;
	private int TESTING_SET_SIZE = 100;
	private int MAX_FOREST_SIZE = 300;
	private int FOREST_SIZE_STEP = 1;
	private boolean PARAMETER_TEST = true;
	private boolean SHOW_PREDICTION = false;
	private boolean TEST_FROM_FILE = true;
	private boolean OUTPUT_CHOICE = false;
	private boolean OUTPUT_FOR_PLOT = true;
	private int FOREST_SIZE = 100;
	private String ALGORITHM = "RandomForest";		//DecisionTree, RandomForest

	private int COUNT = 0;
	private int HITS = 0;

	private String prediction="";

	static int CLASS_NUMBER = 12;
	static String Red = "Red";
	static String Green = "Green";
	static String Blue = "Blue";
	static String Purple = "Purple";
	static String Brown = "Brown";
	static String Pink = "Pink";
	static String Orange = "Orange";
	static String Grey = "Grey";
	static String Black = "Black";
	static String White = "White";
	static String Yellow = "Yellow";
	static String Other = "Other";

	int R;
	int G;
	int B;

	DecisionTree tree;
	RandomForest forest;
	ArrayList<Sample> data;

	JLabel picture;
	PrintWriter writer;

	public ColorTest() {
		super(new BorderLayout());

		//Initialize samples
		initData();

		//Create the radio buttons.
		JButton button1 = new JButton(Red);
		button1.setActionCommand(Red);

		JButton button2 = new JButton(Green);
		button2.setActionCommand(Green);

		JButton button3 = new JButton(Blue);
		button3.setActionCommand(Blue);

		JButton button4 = new JButton(Purple);
		button4.setActionCommand(Purple);

		JButton button5 = new JButton(Brown);
		button5.setActionCommand(Brown);

		JButton button6 = new JButton(Pink);
		button6.setActionCommand(Pink);

		JButton button7 = new JButton(Orange);
		button7.setActionCommand(Orange);

		JButton button8 = new JButton(Grey);
		button8.setActionCommand(Grey);

		JButton button9 = new JButton(Black);
		button9.setActionCommand(Black);

		JButton button10 = new JButton(White);
		button10.setActionCommand(White);

		JButton button11 = new JButton(Yellow);
		button11.setActionCommand(Yellow);

		JButton button12 = new JButton(Other);
		button12.setActionCommand(Other);

		//Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(button1);
		group.add(button2);
		group.add(button3);
		group.add(button4);
		group.add(button5);
		group.add(button6);
		group.add(button7);
		group.add(button8);
		group.add(button9);
		group.add(button10);
		group.add(button11);
		group.add(button12);

		//Register a listener for the radio buttons.
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);
		button6.addActionListener(this);
		button7.addActionListener(this);
		button8.addActionListener(this);
		button9.addActionListener(this);
		button10.addActionListener(this);
		button11.addActionListener(this);
		button12.addActionListener(this);

		//Put the radio buttons in a column in a panel.
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		radioPanel.add(button1);
		radioPanel.add(button2);
		radioPanel.add(button3);
		radioPanel.add(button4);
		radioPanel.add(button5);
		radioPanel.add(button6);
		radioPanel.add(button7);
		radioPanel.add(button8);
		radioPanel.add(button9);
		radioPanel.add(button10);
		radioPanel.add(button11);
		radioPanel.add(button12);

		add(radioPanel, BorderLayout.LINE_START);
		setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	}

	private void initData() {
		//Learning part
		int learnCounter = 0;
		data = new ArrayList<Sample>();
		String path = "learning_set.txt";
		try {
			FileReader fr = new FileReader(path);
			BufferedReader reader = new BufferedReader(fr);
			String line;
			while ((line = reader.readLine()) != null && learnCounter<LEARNING_SET_SIZE) {
				String splitted [] = line.split(",");
				Sample newSample = new Sample(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
				data.add(newSample);
				learnCounter++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (data.size()>0) {
			//Create tree
			tree = new DecisionTree(data);
			//Create forest
			forest = new RandomForest(data, FOREST_SIZE);
		}

		//Testing part
		int testCounter = 0;
		path = "testing_set.txt";
		if (TEST_FROM_FILE)
			try {
				FileReader fr = new FileReader(path);
				BufferedReader reader = new BufferedReader(fr);
				String line;
				while ((line = reader.readLine()) != null && testCounter<TESTING_SET_SIZE) {
					//Get data
					String splitted [] = line.split(",");
					int R = Integer.parseInt(splitted[0]);
					int G = Integer.parseInt(splitted[1]);
					int B = Integer.parseInt(splitted[2]);
					int expectedColor = Integer.parseInt(splitted[3]);

					Sample sample = new Sample(R, G, B, -1);
					int treeColor = -1;
					if (ALGORITHM.equals("DecisionTree"))
						treeColor = classify(sample, tree.getRoot());
					else if (ALGORITHM.equals("RandomForest"))
						treeColor = classify(sample, forest);
					else
						System.out.println("No such algorithm");

					COUNT++;
					testCounter++;
					if (expectedColor == treeColor)
						HITS++;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}


		//Learning
		data = new ArrayList<Sample>();
		path = "learning_set.txt";
		try {
			FileReader fr = new FileReader(path);
			BufferedReader reader = new BufferedReader(fr);
			String line;
			while ((line = reader.readLine()) != null) {
				String splitted [] = line.split(",");
				Sample newSample = new Sample(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
				data.add(newSample);
				learnCounter++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (PARAMETER_TEST) {

			ArrayList<Sample> currentLearningSet = new ArrayList<Sample>();
			
			for (int i=0;i<LEARNING_SET_SIZE; i=10) {
				//Create data structures
				currentLearningSet.add(data.get(i));
				tree = new DecisionTree(currentLearningSet);
				forest = new RandomForest(currentLearningSet, FOREST_SIZE);
				
				//Testing
				int testCount = 0;
				int forestHits = 0;
				int treeHits = 0;
				testCounter = 0;

				forest = new RandomForest(data, FOREST_SIZE);

				try {
					FileReader fr = new FileReader(path);
					BufferedReader reader = new BufferedReader(fr);
					String line;
					while ((line = reader.readLine()) != null && testCounter<TESTING_SET_SIZE) {
						//Get data
						String splitted [] = line.split(",");
						int R = Integer.parseInt(splitted[0]);
						int G = Integer.parseInt(splitted[1]);
						int B = Integer.parseInt(splitted[2]);
						int expectedColor = Integer.parseInt(splitted[3]);
						Sample sample = new Sample(R, G, B, -1);

						int treeColor = classify(sample, tree.getRoot());
						int forestColor = classify(sample, forest);

						if (treeColor == expectedColor)
							treeHits++;
						if (forestColor == expectedColor)
							forestHits++;
						testCount++;
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//Output text
				int decisionTreeSuccess = (int) ((double)treeHits/testCount*100);
				int randomForestSuccess = (int) ((double)forestHits/testCount*100);
				if (OUTPUT_FOR_PLOT) {
					System.out.println(i+","+decisionTreeSuccess+","+randomForestSuccess);
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Random random = new Random();
		R = random.nextInt(256);
		G = random.nextInt(256);
		B = random.nextInt(256);

		g.setColor(new Color(R, G, B));
		g.fillRect(200, 150, 400, 400);

		//Print estimation
		if (COUNT>10 || data.size()>10) {
			Sample sample = new Sample(R, G, B, -1);
			int colorNumber = -1;
			if (ALGORITHM.equals("DecisionTree"))
				colorNumber = classify(sample, tree.getRoot());
			else if (ALGORITHM.equals("RandomForest"))
				colorNumber = classify(sample, forest);
			else
				System.out.println("No such algorithm");
			String color = numberToString(colorNumber);
			prediction = color;
			if (!SHOW_PREDICTION)
				color = "";
			g.setFont(new Font("SansSerif", Font.BOLD, 30));
			g.drawString(color + "   "+HITS+"/"+COUNT, 350, 50);
		}
	}

	/** Listens to the radio buttons. */
	public void actionPerformed(ActionEvent e) {
		String choice = e.getActionCommand();
		int choiceClass = clasify(choice);
		if (OUTPUT_CHOICE)
			System.out.println(R+","+G+","+B+","+choiceClass);
		Sample sample = new Sample(R, G, B, choiceClass);
		data.add(sample);
		COUNT++;

		if (prediction.equals(choice))
			HITS++;

		//Create new tree every 10 samples
		if (COUNT%10 == 0) {
			tree = new DecisionTree(data);
			forest = new RandomForest(data, FOREST_SIZE);
		}

		repaint();
	}

	private int clasify (String color) {
		if (color.equals("Red"))
			return 0;
		if (color.equals("Green"))
			return 1;
		if (color.equals("Blue"))
			return 2;
		if (color.equals("Purple"))
			return 3;
		if (color.equals("Brown"))
			return 4;
		if (color.equals("Pink"))
			return 5;
		if (color.equals("Orange"))
			return 6;
		if (color.equals("Grey"))
			return 7;
		if (color.equals("Black"))
			return 8;
		if (color.equals("White"))
			return 9;
		if (color.equals("Yellow"))
			return 10;
		if (color.equals("Other"))
			return 11;
		return -1;
	}

	private String numberToString (int color) {
		switch (color) {
		case 0:
			return "Red";
		case 1:
			return "Green";
		case 2:
			return "Blue";
		case 3:
			return "Purple";
		case 4:
			return "Brown";
		case 5:
			return "Pink";
		case 6:
			return "Orange";
		case 7:
			return "Grey";
		case 8:
			return "Black";
		case 9:
			return "White";
		case 10:
			return "Yellow";
		case 11:
			return "Other";
		default:
			return "";
		}
	}



	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("Color test");
		frame.setPreferredSize(new Dimension(700, 700));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		JComponent newContentPane = new ColorTest();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private void makeTree () {
		//Input data set
		ArrayList<Sample> data = new ArrayList<Sample>();
		String path = "input.txt";
		try {
			FileReader fr = new FileReader(path);
			BufferedReader reader = new BufferedReader(fr);
			String line;
			while ((line = reader.readLine()) != null) {
				String splitted [] = line.split(",");
				Sample newSample = new Sample(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
				data.add(newSample);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Create tree
		DecisionTree tree = new DecisionTree(data);
		//printDecisionTree(tree);
	}

	private void printDecisionTree(DecisionTree tree) {
		TreeNode root = tree.getRoot();
		printTree(root,0);
	}

	private void printTree(TreeNode node, int level) {
		System.out.println("###################################################");
		System.out.println("level: "+ level);
		if (node.getType().equals("Leaf")){
			System.out.println("Color: "+node.getColor());
			System.out.println();
		}

		if (node.getType().equals("Node")) {
			System.out.println("Split attribute "+node.getSplitAttribute());
			System.out.println("Split criteria: "+node.getSplitCriteria());
			System.out.println();
			printTree(node.getLeft(),level+1);
			printTree(node.getRight(),level+1);
		}
	}

	private int classify(Sample data, TreeNode node) {
		if (node.getType().equals("Leaf"))
			return node.getColor();
		else if (node.getType().equals("Node")) {
			int attributeValue = -1;
			if (node.getSplitAttribute().equals("R")) 
				attributeValue=data.getR();
			if (node.getSplitAttribute().equals("G")) 
				attributeValue=data.getG();
			if (node.getSplitAttribute().equals("B")) 
				attributeValue=data.getB();

			if (attributeValue<node.getSplitCriteria())
				return classify(data, node.getLeft());
			else
				return classify(data, node.getRight());

		}
		return -1;
	}

	private int classify(Sample data, RandomForest forest) {
		//Get tree outputs
		ArrayList<DecisionTree> treeList = forest.getForest();
		int treeOutputs [] = new int [treeList.size()];
		for (int i=0 ; i<treeList.size(); i++) {
			DecisionTree tree = treeList.get(i);
			treeOutputs[i] = classify(data, tree.getRoot());
		}

		//Get best prediction from tree outputs
		int colors [] = new int [CLASS_NUMBER+1];
		for (int i=0; i<treeOutputs.length; i++) {
			colors[treeOutputs[i]]++;
		}
		int outputColor = -1;
		int max = -1;
		for (int i=0; i<colors.length; i++) {
			if (colors[i]>max) {
				max = colors[i];
				outputColor = i;
			}
		}
		return outputColor;
	}

}



import java.awt.Label;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class test {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("How many nodes do you want to prune?");
		int numOfPrune = in.nextInt();
		System.out.println("The path of the training set");
		String trainingSet = in.next();
		System.out.println("The path of the validation set");
		String validationSet = in.next();
		System.out.println("The path of the test set");
		String testSet = in.next();
		System.out.println("Do you want to print the model (1/0) :");
		int isPrint = in.nextInt();
		System.out.println("Which algorithm do you want to use to build the model? ID3(1) / Random(0) :");
		int isID3 = in.nextInt();;
		File trainingFile = new File(trainingSet);
		File validationFile = new File(validationSet);
		File testFile = new File(testSet);
		Tree tree = new Tree();
		
		
		Node root = ID3train(trainingFile, isID3);
		System.out.println("The number of nodes is: " + getMaxLabel(root));
		System.out.println("The average depth of the leaf nodes is:" + getAverageDepth(root));
		pruneTree(root, numOfPrune, validationFile, isID3);
		if (isPrint == 1) {
			System.out.println("The decision tree after pruning is :");
			tree.printTree(root);
		}
		
		
		
		System.out.println("The accuracy on the test data is :" + testData(root, testFile));
		
	}

	
	
	
	public static Node ID3train(File file, int isID3) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String str = null;

			try {

				// get the attributeName
				str = reader.readLine();
				ArrayList<String> attributeName = new ArrayList<String>();
				String[] attribute = str.split(",");
				for (String s : attribute) {
					attributeName.add(s);
				}

				// get the dataSet
				ArrayList<ArrayList<String>> dataSet = new ArrayList<>();
				while ((str = reader.readLine()) != null) {
					ArrayList<String> tmpData = new ArrayList<>();
					String[] data = str.split(",");
					for (String s : data) {
						tmpData.add(s);
					}
					dataSet.add(tmpData);
				}
				reader.close();

				Tree tree = new Tree();
				Node root = tree.buildTree(dataSet, attributeName, 0, isID3);
				return root;


			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static double testData(Node root, File file) {
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String str = null;

			try {

				// get the attributeName
				str = reader.readLine();
				ArrayList<String> testAttributeName = new ArrayList<String>();
				String[] attribute = str.split(",");
				for (String s : attribute) {
					testAttributeName.add(s);
				}

				// get the dataSet
				ArrayList<ArrayList<String>> testDataSet = new ArrayList<>();
				while ((str = reader.readLine()) != null) {
					ArrayList<String> tmpData = new ArrayList<>();
					String[] data = str.split(",");
					for (String s : data) {
						tmpData.add(s);
					}
					testDataSet.add(tmpData);
				}
				reader.close();

				Tree tree = new Tree();
				double result = tree.test(testDataSet, testAttributeName, root);
				return result;


			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static void pruneTree(Node root, int num, File file, int isID3) {
		for (int i = 0; i < num; i++) {
			pruneNode(root, file, isID3);
		}
	}

	public static int getMaxLabel(Node root) {
		ArrayList<Node> children = root.getChildren();
		if (children == null || children.size() == 0) {
			return root.getLabel();
		}
		int max = 0;
		for (Node tmp : children) {
			int tmpLable = getMaxLabel(tmp);
			max = Math.max(max, tmpLable);
		}
		return max;
	}

	public static void pruneNode(Node root, File file, int isID3) {
		int index = 0;
		if (isID3 == 1) {
			int max = getMaxLabel(root);
			double accuracy = 0;
			for (int i = 1; i <= max; i++) {
				Node copy = copyTree(root);
				Tree tree = new Tree();
				Node tmpNode = getNode(copy, i);
				if (tmpNode == null || tmpNode.getChildren() == null || tmpNode.getChildren().size() == 0) {
					continue;
				} else {
					double acc = deleteNode(copy, tmpNode, file);
					if (accuracy < acc) {
						accuracy = acc;
						index = i;
					}

				}
			}
		} else {
			Random random = new Random();
			int maxLabel = getMaxLabel(root);
			index = random.nextInt(maxLabel) + 1;
		}
		Node deleteNode = getNode(root, index);
		String maxType = maxType(deleteNode);
		deleteNode.setType(maxType);
		deleteNode.setChildren(null);

	}

	public static String maxType(Node root) {
		Tree tree = new Tree();
		ArrayList<ArrayList<String>> dataSet = root.getDataSet();
		ArrayList<String> attributeName = root.getAttributeName();
		ArrayList<String> value = entropy.getKey(dataSet, attributeName.size() - 1);
		HashMap<String, Integer> hash = entropy.getUniqueResult(dataSet, attributeName.size() - 1);
		String maxValue = "";
		int max = 0;
		
		for (String tmpString : value) {
			if (max < hash.get(tmpString)) {
				max = hash.get(tmpString);
				maxValue = tmpString;
			}
		}
		return maxValue;
	}

	public static Node copyTree(Node root) {
		Node copy = new Node();

		if (root.getChildren() == null || root.getChildren().size() == 0) {
			copy.setChildren(null);
			copy.setDataSet(null);
			copy.setAttributName(null);
			copy.setLabel(root.getLabel());
			copy.setLevel(root.getLevel());
			copy.setSplitAttributeName(root.getSplitAttributeName());
			copy.setSplitAttributeValue(root.getSplitAttributeValue());
			copy.setType(root.getType());
			return copy;
		} else {
			copy.setDataSet(new ArrayList<ArrayList<String>>(root.getDataSet()));
			copy.setAttributName(new ArrayList<String>(root.getAttributeName()));
			copy.setLabel(root.getLabel());
			copy.setLevel(root.getLevel());
			copy.setSplitAttributeName(root.getSplitAttributeName());
			copy.setSplitAttributeValue(root.getSplitAttributeValue());
			copy.setType(root.getType());
		}

		ArrayList<Node> copyChildren = new ArrayList<>();
		if (root.getChildren() == null || root.getChildren().size() == 0) {
			copyChildren = null;
		} else {
			for (Node tmpNode : root.getChildren()) {
				copyChildren.add(copyTree(tmpNode));
			}
		}
		
		copy.setChildren(copyChildren);
		return copy;
	}

	public static double deleteNode(Node root, Node pruneNode, File file) {
		if (root == pruneNode) {
			return 0;
		}
		ArrayList<ArrayList<String>> dataSet = pruneNode.getDataSet();
		ArrayList<String> value = entropy.getKey(dataSet, dataSet.get(0).size() - 1);
		HashMap<String, Integer> hash = entropy.getUniqueResult(dataSet, dataSet.get(0).size() - 1);
		String maxValue = "";
		int max = 0;
		for (String tmp : value) {
			if (max <= hash.get(tmp)) {
				max = hash.get(tmp);
				maxValue = tmp;
			}
		}
		pruneNode.setChildren(null);
		pruneNode.setType(maxValue);
		double accuracy = testData(root, file);
		return accuracy;
	}

	public static Node getNode(Node root, int label) {
		if (root.getLabel() == label) {
			return root;
		}
		ArrayList<Node> children = root.getChildren();
		if (children == null || children.size() == 0) {
			return null;
		}
		for (Node tmpNode : children) {
			Node targetNode = getNode(tmpNode, label);
			if (targetNode != null) {
				return targetNode;
			}
		}
		return null;
	}
	
	public static int getAverageDepth(Node root) {
		int maxLabel = getMaxLabel(root);
		int levelSum = 0;
		int count = 0;
		for (int i = 1; i <= maxLabel; i++) {
			Node temp = getNode(root, i);
			if (temp.getChildren() == null) {
				count++;
				levelSum += temp.getLevel();
			}
		}
		return levelSum / count;
	}
}

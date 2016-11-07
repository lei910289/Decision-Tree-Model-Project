import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.text.AbstractDocument.LeafElement;


public class Tree {
	int label = 0;
	public Node buildTree(ArrayList<ArrayList<String>> dataSet, ArrayList<String> attributeName, int level, int isID3) {
		int attrSize = dataSet.get(0).size();
		ArrayList<String> classType = entropy.getKey(dataSet, attrSize - 1);
		if (classType.size() == 1) {
			Node leaf = new Node();
			leaf.setAttributName(attributeName);
			leaf.setDataSet(dataSet);
			leaf.setChildren(null);
			leaf.setType(classType.get(0));
			leaf.setLevel(level);
			leaf.setLabel(label++);
			return leaf;
		}

		if (attributeName.size() == 1) {
			Node leaf = new Node();
			ArrayList<String> classValue = entropy.getKey(dataSet, 0);
			HashMap<String, Integer> hash = entropy.getUniqueResult(dataSet, 0);
			String maxValue = "";
			int max = 0;
			for (String valueString : classValue) {
				if (max <= hash.get(valueString)) {
					max = hash.get(valueString);
					maxValue = valueString;
				}
			}
			leaf.setAttributName(attributeName);
			leaf.setDataSet(dataSet);
			leaf.setChildren(null);
			leaf.setType(maxValue);
			leaf.setLevel(level);
			leaf.setLabel(label++);
			return leaf;
		}

		Node root = new Node(dataSet, attributeName);
		root.setLabel(label++);

		// implement ID3 algorithm to choose the next split
		int attributeIndex = -1;
		if (isID3 == 1) {
			double maxGain = 0;
			int attributeSize = attributeName.size();
			for (int i = 0; i < attributeSize - 1; i++) {
				double gain = entropy.getGain(dataSet, i);
				if (maxGain <= gain) {
					attributeIndex = i;
					maxGain = gain;
				}
			}
		} else {
			int size = attributeName.size() - 1;
			Random random = new Random();
			attributeIndex = random.nextInt(size + 1);
		}


		ArrayList<Node> children = new ArrayList<>();
		ArrayList<String> keyValue = entropy.getKey(dataSet, attributeIndex);
		for (String value : keyValue) {
			ArrayList<ArrayList<String>> splitDataSet = entropy.getSplitDataSet(dataSet, attributeIndex, value);
			ArrayList<String> splitAttributeName = entropy.getSplitAttributeName(attributeName, attributeIndex);
			Node child = buildTree(splitDataSet, splitAttributeName, level + 1, isID3);
			child.setSplitAttributeName(attributeName.get(attributeIndex));
			child.setSplitAttributeValue(value);
			children.add(child);
		}
		root.setChildren(children);
		root.setLevel(level);
		return root;

	}

	public void searchTree() {

	}

	public void printTree(Node root) {
		if (root.getType() != null) {
			for (int i = 0; i < root.getLevel(); i++) {
				System.out.print("|");
			}
			System.out.println(root.getSplitAttributeName() + " = " + root.getSplitAttributeValue() + " : " + root.getType());
			return;
		}
		ArrayList<Node> children = root.getChildren();
		if (root.getSplitAttributeName() != null) {
			for (int i = 0; i < root.getLevel(); i++) {
				System.out.print("|");
			}
			System.out.println(root.getSplitAttributeName() + " = " + root.getSplitAttributeValue() + " : ");
		}
		for (Node child : children) {
			printTree(child);
		}
	}

	public int getIndex(ArrayList<String> testAttributeName, String Attribute) {
		for (int i = 0; i < testAttributeName.size(); i++) {
			if (testAttributeName.get(i).equals(Attribute)) {
				return i;
			}
		}
		return -1;
	}

	public int compare(ArrayList<String> dataSet, ArrayList<String> testAttributeName, Node root) {
		ArrayList<Node> children = root.getChildren();
		if (children == null || children.size() == 0) {
			if (root.getType().equals(dataSet.get(dataSet.size() - 1))) {
				return 1;
			} else {
				return 0;
			}
		} else {
			int index = getIndex(testAttributeName, children.get(0).getSplitAttributeName());
			for (Node child : children) {
				if (dataSet.get(index).equals(child.getSplitAttributeValue())) {
					return compare(dataSet, testAttributeName, child);
				}
			}
		}
		return - 1;
	}

	public double test (ArrayList<ArrayList<String>> testDataSet, ArrayList<String> testAttributeName, Node root) {
		int count = 0;
		for (ArrayList<String> tmp : testDataSet) {
			count += compare(tmp, testAttributeName, root);
		}
		int size = testDataSet.size();
		return (double)count / size;
	}



}

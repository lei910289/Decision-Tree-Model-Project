import java.util.ArrayList;


public class Node {
	private	ArrayList<String> attributeName;
	private ArrayList<ArrayList<String>> dataSet;
	private String splitAttributeName;
	private String splitAttributeValue;
	private ArrayList<Node> children;
	private String type;
	private int level;
	int label;
	
	
	public Node() {}
		
	public Node(ArrayList<ArrayList<String>> dataSet, ArrayList<String> attributeName ){  
         this.dataSet = dataSet;
         this.attributeName = attributeName;
    }
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public ArrayList<String> getAttributeName() {
		return attributeName;
	}
	
	public void setAttributName(ArrayList<String> attributeName) {
		this.attributeName = attributeName;
	}
	
	public ArrayList<ArrayList<String>> getDataSet() {
		return dataSet;
	}
	
	public void setDataSet(ArrayList<ArrayList<String>> dataSet) {
		this.dataSet = dataSet;
	}
	
	public String getSplitAttributeName() {
		return splitAttributeName;
	}
	
	public void setSplitAttributeName(String splitAttributeName) {
		this.splitAttributeName = splitAttributeName;
	}
	
	public String getSplitAttributeValue() {
		return splitAttributeValue;
	}
	
	public void setSplitAttributeValue(String nodeName) {
		this.splitAttributeValue = nodeName;
	}
	
	public ArrayList<Node> getChildren() {
		return children;
	}
	
	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}
	
	public void setLabel(int label) {
		this.label = label;
	}
	
	public int getLabel() {
		return label;
	}
}


public class TreeNode {
	private int splitCriteria;
	private String splitAttribute;
	private int color;
	private TreeNode left;
	private TreeNode right;
	private String type;	// Node, Leaf
	
	public TreeNode () {
		type = "Node";
		left = null;
		right = null;
		splitCriteria = -1;
		splitAttribute = null;
		color = -1;
	}
	
	public int getSplitCriteria() {
		return splitCriteria;
	}
	public int getColor() {
		return color;
	}
	public TreeNode getLeft() {
		return left;
	}
	public TreeNode getRight() {
		return right;
	}
	public String getType() {
		return type;
	}
	public void setSplitCriteria(int splitCriteria) {
		this.splitCriteria = splitCriteria;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public void setLeft(TreeNode left) {
		this.left = left;
	}
	public void setRight(TreeNode right) {
		this.right = right;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getSplitAttribute() {
		return splitAttribute;
	}

	public void setSplitAttribute(String splitAttribute) {
		this.splitAttribute = splitAttribute;
	}
	
}
	
	
	

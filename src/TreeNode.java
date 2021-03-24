
public class TreeNode {
    private String className = null;
    private double probability = 0.0;
    private TreeNode leftChild = null;
    private TreeNode rightChild = null;
    private String bestCategory = null;

    public TreeNode(String cL, double pR, TreeNode lC, TreeNode rC, String bC) {
        className = cL;
        probability = pR;
        leftChild = lC;
        rightChild = rC;
        bestCategory = bC;
    }

    public String getClassName() {
        return className;
    }

    public double getProbability() {
        return probability;
    }

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }

    public String getBestCategory() {
        return bestCategory;
    }

}

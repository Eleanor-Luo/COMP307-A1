import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DecisionTree {
    private static int numCategories;
    public static List<String> categoryNames;
    public static List<Instance> trainingInstances = new ArrayList<>();
    public static List<Instance> testInstances = new ArrayList<>();

    public static void main(String[] args) {
        new DecisionTree();

        if (args.length > 1) {

            // Try catch for training dataset being loaded
            try {
                FileReader fileReader = new FileReader(args[0]);
                System.out.println("Reading data from file "+ args[0]);
                Scanner sc = new Scanner(fileReader);
                String firstLine = sc.nextLine();
                categoryNames = new ArrayList<>();
                Scanner sc1 = new Scanner(firstLine);
                while(sc1.hasNext()) { categoryNames.add(sc1.next()); }
                while(sc.hasNext()) {
                    String input = sc.nextLine();
                    Scanner inputSc = new Scanner(input);
                    List<Boolean> tmp = new ArrayList<>();
                    String tmpStr = inputSc.next();
                    while(inputSc.hasNextBoolean()){
                        tmp.add(inputSc.nextBoolean());
                    }
                    trainingInstances.add(new Instance(tmpStr, tmp));
                    //features = tmp.size();
                }

            } catch (NumberFormatException | FileNotFoundException e) {
                System.err.println("Make sure " + args[0] + " is in the same folder as the .jar file");
                System.exit(1);
            }

            TreeNode tree = loadNodes(new ArrayList<>(trainingInstances), new ArrayList<>(categoryNames));

            printMethod(tree, "");

            // Try catch for testing dataset being loaded
            try {
                FileReader fileReader1 = new FileReader(args[1]);
                System.out.println("Reading data from file "+ args[1]);
                Scanner sc = new Scanner(fileReader1);
                sc.nextLine();
                while(sc.hasNext()) {
                    String input = sc.nextLine();
                    Scanner inputSc = new Scanner(input);
                    List<Boolean> tmp = new ArrayList<>();
                    String tmpStr = inputSc.next();
                    while(inputSc.hasNextBoolean()){
                        tmp.add(inputSc.nextBoolean());
                    }
                    testInstances.add(new Instance(tmpStr, tmp));
                }

            } catch (NumberFormatException | FileNotFoundException e) {
                System.err.println("Make sure " + args[1] + " is in the same folder as the .jar file");
                System.exit(1);
            }

            int count = 0;
            for (Instance ins: testInstances) {
                String ans = classifier(tree, ins);
                if(ans.equals(ins.getCategory())) { count++; }

                System.out.println("Result: " + ans);
                System.out.println("True Result: " + ins.getCategory());
            }
            System.out.println(count + " out of " + testInstances.size());
        }
    }

    private static double impurityMeasurement(List<Instance> currentList) {
        double countLive = 0.0;
        double countDie = 0.0;
        for (Instance ins: currentList) {
            if(ins.getCategory().equalsIgnoreCase("live")) { countLive++; }
            if(ins.getCategory().equalsIgnoreCase("die")) { countDie++; }
        }
        return countLive*countDie/Math.pow((countLive+countDie),2);
    }

    private static double probabilityMeasurement(List<Instance> currentList) {
        double countLive = 0.0;
        double countDie = 0.0;
        for (Instance ins: currentList) {
            if(ins.getCategory().equalsIgnoreCase("live")) { countLive++; }
            if(ins.getCategory().equalsIgnoreCase("die")) { countDie++; }
        }
        double prob = 0.0;
        if(countLive > countDie) { prob = countLive/(countLive*countDie); }
        else if(countDie > countLive) {
            prob = countDie/(countDie*countLive);
        }
        else { prob = 0.5; }
        return prob;
    }

    private static String topCategory(List<Instance> currentList) {
        double countLive = 0.0;
        double countDie = 0.0;
        for (Instance ins: currentList) {
            if(ins.getCategory().equalsIgnoreCase("live")) { countLive++; }
            if(ins.getCategory().equalsIgnoreCase("die")) { countDie++; }
        }
        if(countLive > countDie) { return "live"; }
        else { return "die"; }
    }

    public static void printMethod(TreeNode root, String indent) {
        if(root.getLeftChild() == null) {
            if (root.getProbability() == 0){ //Error-checking
                System.out.printf("%sUnknown%n", indent);
            }else{
                System.out.printf("%sClass %s, prob=%.2f%n", indent, root.getClassName(), root.getProbability());
            }
        }
        else {
            System.out.printf("%s%s = True:%n", indent, root.getBestCategory());
            TreeNode left = root.getLeftChild();
            printMethod(left, indent+"\t");
            System.out.printf("%s%s = False:%n", indent, root.getBestCategory());
            TreeNode right = root.getRightChild();
            printMethod(right, indent+"\t");
        }
    }

    private static String classifier(TreeNode tree, Instance instance) {
        if(tree.getLeftChild() == null) {
            return tree.getClassName();
        }
        else {
            int num = categoryNames.indexOf(tree.getBestCategory());
            if(instance.getVal(num-1) == true) {
                TreeNode left = tree.getLeftChild();
                return classifier(left, instance);
            }
            else {
                TreeNode right = tree.getRightChild();
                return classifier(right, instance);
            }
        }
    }

    private static TreeNode loadNodes(List<Instance> instanceList, List<String> categoryList) {
        double impurity = (impurityMeasurement(instanceList));
        if(instanceList.isEmpty()) {
            return new TreeNode(topCategory(trainingInstances), probabilityMeasurement(trainingInstances), null, null, null);
        }
        else if(impurity == 0.0) {
            return new TreeNode(instanceList.get(0).getCategory(), 1, null, null, null);
        }
        else if(categoryList.isEmpty()) {
            return new TreeNode(topCategory(instanceList), probabilityMeasurement(instanceList), null, null, null);
        }
        else {
            String bestCat = null;
            List<Instance> bestTrueInstance = new ArrayList<>();
            List<Instance> bestFalseInstance = new ArrayList<>();
            double minImpurity = Double.MAX_VALUE;
            for(int i = 1; i < categoryList.size(); i++) {
                String cat = categoryList.get(i);
                List<Instance> tempTrue = new ArrayList<>();
                List<Instance> tempFalse = new ArrayList<>();
                int num = categoryNames.indexOf(cat);
                for(Instance ins: instanceList) {
                    if(ins.getVal(num-1) == true) { tempTrue.add(ins); }
                    else if(ins.getVal(num-1) == false) { tempFalse.add(ins); }
                }
                double trueImpurity;
                if(!tempTrue.isEmpty()) { trueImpurity = impurityMeasurement(tempTrue); }
                else { trueImpurity = 0; }
                double falseImpurity = impurityMeasurement(tempFalse);
                if(!tempFalse.isEmpty()) { falseImpurity = impurityMeasurement(tempFalse); }
                else { falseImpurity = 0; }
                double weightedAvgImpurity = trueImpurity * ((double) tempTrue.size() / (double) instanceList.size())
                        + falseImpurity * ((double) tempFalse.size() / (double) instanceList.size());
                if(weightedAvgImpurity < minImpurity) {
                    bestCat = cat;
                    bestTrueInstance = tempTrue;
                    bestFalseInstance = tempFalse;
                    minImpurity = weightedAvgImpurity;
                }
            }
            categoryList.remove(bestCat);
            TreeNode leftChild = loadNodes(bestTrueInstance, new ArrayList<>(categoryList));
            TreeNode rightChild = loadNodes(bestFalseInstance, new ArrayList<>(categoryList));
            return new TreeNode(null, Double.MAX_VALUE,leftChild,rightChild,bestCat);
        }
    }
}

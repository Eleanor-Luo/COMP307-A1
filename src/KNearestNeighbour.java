import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
*   Main class for k-Nearest Neighbour (Part 1) for assignment 1.
*   Finds the nearest neighbour for k.
*   @author alfredabra 300509598
**/

public class KNearestNeighbour {
    public static List<WineContent> winConTrainHolder = new ArrayList<>(); // Holds the training data set
    public static List<WineContent> winConTestHolder = new ArrayList<>(); // Holds the testing data set
    public static List<Double> rangeList = new ArrayList<>();
    public static double features = 0.0;
    private static int k;

    /**
     * Main method for the KNearestNeighbour class.
     * Includes the load function for both training and testing dataset.
     *
     * @param args args
     **/
    public static void main(String[] args){
        new KNearestNeighbour();

        // now we go through and load each file in starting off with the training set,
        // then loading in the testing set.

        if (args.length > 1) {

            // Try catch for training dataset being loaded
            try {
                FileReader fileReader = new FileReader(args[0]);
                Scanner sc = new Scanner(fileReader);
                sc.nextLine();
                while(sc.hasNext()) {
                    String input = sc.nextLine();
                    Scanner inputSc = new Scanner(input);
                    List<Double> tmp = new ArrayList<>();
                    while(!(inputSc.hasNextInt())){
                        tmp.add(inputSc.nextDouble());
                    }
                    int tmpInt = inputSc.nextInt();
                    winConTrainHolder.add(new WineContent(tmp, tmpInt));
                    features = tmp.size();
                }

            } catch (NumberFormatException | FileNotFoundException e) {
                System.err.println("Make sure " + args[0] + " is in the same folder as the .jar file");
                System.exit(1);
            }

            colRangeFinder();

            // Try catch for testing dataset being loaded
            try {
                FileReader fileReader1 = new FileReader(args[1]);
                Scanner sc1 = new Scanner(fileReader1);
                sc1.nextLine();
                while(sc1.hasNext()) {
                    String input = sc1.nextLine();
                    Scanner inputSc = new Scanner(input);
                    List<Double> tmp = new ArrayList<>();
                    while(!(inputSc.hasNextInt())){
                        tmp.add(inputSc.nextDouble());
                    }
                    int tmpInt = inputSc.nextInt();
                    winConTestHolder.add(new WineContent(tmp, tmpInt));
                }

            } catch (NumberFormatException | FileNotFoundException e) {
                System.err.println("Make sure " + args[1] + " is in the same folder as the .jar file");
                System.exit(1);
            }

            // printing being handled here so it looks pretty on powershell, cmd and bash
            System.out.println("\nHere are the results:");

            System.out.println("\n=====================");
            System.out.println("For k = 1: ");
            System.out.println("=====================");
            k = 1;
            classifierLoop(); // for testing

            System.out.println("\n=====================");
            System.out.println("For k = 3: ");
            System.out.println("=====================");
            k = 3;
            classifierLoop(); // for testing

            System.out.println("\n=====================");
            System.out.println("For k = 9: ");
            System.out.println("=====================");
            k = 9;
            classifierLoop();

            System.out.println(" ");
        }
    }

    /**
     * Comparator to sort between the training dataset
     *
     **/
    public static void dataSort() {
        winConTrainHolder.sort(Comparator.comparingDouble(WineContent::getDist));
    }

    public static double accuracyCalc() {
        int accuracyCounter = 0;

        for(WineContent test: winConTestHolder) {
            if(test.getLabelGuess() == test.getClassNumber()) { accuracyCounter++; }
        }
        return accuracyCounter/(double)winConTestHolder.size(); // forced cast as a double for the list.size because of integer division in floating point context issue
    }

    /**
     * Method to loop through the training and testing data
     *
     **/
    public static void classifierLoop() {
        for(WineContent testContent : winConTestHolder) {
            for(WineContent trainContent : winConTrainHolder) {
                double dub = trainContent.distanceFinder(testContent, rangeList);
            }
            dataSort();

            int lineLabel = votingMap();
            testContent.setLabelGuess(lineLabel);
            System.out.println("Wine classified as: " + lineLabel);
        }
        double finalAccuracy = accuracyCalc();

        System.out.println("Accuracy %: " + String.format("%.2f" ,finalAccuracy*100));
    }

    /**
     * Method to find ranges through the columns in the training dataset
     *
     **/
    public static void colRangeFinder() {
        for (int i = 0; i < features; i++) {
            double minValue = Double.MAX_VALUE;
            double maxValue = Double.MIN_VALUE;
            for (WineContent wC : winConTrainHolder) {
                if (wC.getDataList(i) < minValue) {
                    minValue = wC.getDataList(i);
                }
                else if (wC.getDataList(i) > maxValue) {
                    maxValue = wC.getDataList(i);
                }
            }
            double range = minValue-maxValue;
            rangeList.add(range);
        }
    }

    /**
     * Method for voting to get the best pick.
     *
     * @return int labelVotes
     **/
    public static int votingMap() {
        Map<Integer, Integer> voted = new HashMap<>();
        for(int i = 0; i < k; i++) {
            int key = winConTrainHolder.get(i).getClassNumber();
            if (voted.containsKey(key)) {
                voted.put(key, voted.get(key) + 1);
            } else {
                voted.put(key, 1);
            }
        }

        int numVotes = 0;
        int labelVotes = 0;

        for (int vote : voted.keySet()) {
            if (voted.get(vote) > numVotes) {
                numVotes = voted.get(vote);
                labelVotes = vote;
            }
        }

        return labelVotes;
    }
}